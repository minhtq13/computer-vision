package impls

import (
	"be-gogin/global"
	"be-gogin/global/constants"
	"be-gogin/internal/databases/repository"
	"be-gogin/internal/domains/auth/dtos"
	entities2 "be-gogin/internal/domains/auth/entities"
	mongo2 "be-gogin/internal/domains/auth/models/mongo"
	authrepos "be-gogin/internal/domains/auth/repositories/postgres"
	"be-gogin/internal/domains/auth/services"
	authutils "be-gogin/internal/domains/auth/utils"
	utils2 "be-gogin/internal/domains/common/utils"
	entities3 "be-gogin/internal/domains/role/entities"
	"be-gogin/internal/domains/user/entities"
	"be-gogin/internal/domains/user/repositories/postgres"
	"be-gogin/internal/errors"
	"be-gogin/pkg/utils"
	"context"
	"encoding/base64"
	"encoding/json"
	"net/http"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/jinzhu/copier"
	"github.com/redis/go-redis/v9"
	"github.com/todocoder/go-stream/stream"
	"go.mongodb.org/mongo-driver/v2/mongo"
	"golang.org/x/crypto/bcrypt"
)

type AuthenticationService struct {
	pgCommonRepository repository.IRepository
	userRepository     postgres.IUserPGRepository
	authInfoRepository authrepos.IAuthInfoPGRepository
	redisClient        *redis.Client
	mongoClient        *mongo.Client
}

func NewAuthenticationService(
	pgCommonRepository repository.IRepository,
	userRepository postgres.IUserPGRepository,
	authInfoRepository authrepos.IAuthInfoPGRepository,
	redisClient *redis.Client,
	mongoClient *mongo.Client,
) services.IAuthenticationService {
	return &AuthenticationService{
		pgCommonRepository: pgCommonRepository,
		userRepository:     userRepository,
		authInfoRepository: authInfoRepository,
		redisClient:        redisClient,
		mongoClient:        mongoClient,
	}
}

const (
	TokenInvalidStatus = 0
	TokenValidStatus   = 1
)

/**
Implementations
*/

// Login /**
func (authService *AuthenticationService) Login(loginRequest dtos.LoginRequestDTO, c *gin.Context) (dtos.LoginResponseDTO, error) {
	defer utils.LogFunction()()
	jwtConfig := global.AppConfig.Jwt
	// tạo response
	var response dtos.LoginResponseDTO

	// kiểm tra username của user
	userEntity, findErr := authService.findUserByUserName(loginRequest.Username)
	if findErr != nil {
		errors.LogError(findErr, utils.GetCallLocation())
		return response, &errors.BadCredentialError{
			Message: constants.MessageUsernameNotExists,
			Status:  http.StatusUnauthorized,
		}
	}
	var userDetail dtos.UserDetails
	copyErr := copier.Copy(&userDetail, &userEntity)
	if copyErr != nil {
		errors.LogError(copyErr)
		return response, copyErr
	}

	// Validate password
	badCredentialErr := &errors.BadCredentialError{
		Message: constants.MessagePasswordIncorrect,
		Status:  http.StatusUnauthorized,
	}
	var rawPassword string
	// rawPassword = decode + reverse
	decodedBytes, decodeErr := base64.StdEncoding.DecodeString(loginRequest.Password)
	rawPassword = utils.ReverseString(string(decodedBytes))
	if decodeErr != nil {
		errors.LogError(decodeErr)
		return response, decodeErr
	}
	// kiểm tra input password với bcrypt password trong db
	passErr := bcrypt.CompareHashAndPassword([]byte(userDetail.Password), []byte(rawPassword))
	if passErr != nil {
		errors.LogError(passErr)
		return response, badCredentialErr
	}

	// Kiểm tra token trong redis còn hiệu lực hay không thì ưu tiên sử dụng token cũ còn hiệu lực
	var authTokenDTO dtos.AuthValidationDTO
	redisTokenData := authService.redisClient.Get(c, constants.RedisTokenKeyPrefix+userDetail.Username).Val()
	redisTokenErr := json.Unmarshal([]byte(redisTokenData), &authTokenDTO)
	// Get currentAuthInfo từ DB
	currentAuthInfo, err := authService.authInfoRepository.FindFirstByUserId(int64(userDetail.Id))
	if redisTokenErr != nil || !authutils.ValidateJwt(authTokenDTO.AccessToken) {
		// log error để thuận tiện cho việc trace
		if redisTokenErr != nil {
			errors.LogError(redisTokenErr)
		}
		// Generate jwt token
		tokenIssuedAt := time.Now()
		jwtToken, tokenErr := authutils.GenerateJwt(userDetail, global.AppConfig.Jwt)
		// generate refresh token
		refreshToken := authutils.GenerateRefreshToken(userDetail, tokenIssuedAt.UnixMilli()+jwtConfig.RefreshTokenExpiredMs)
		if tokenErr != nil {
			errors.LogError(tokenErr)
			return response, tokenErr
		}
		// set lại token mới vào redis
		authTokenDTO.AccessToken = jwtToken
		authTokenDTO.RefreshToken = refreshToken
		authTokenDTO.Status = TokenValidStatus
		authTokenDTO.IssuedAt = tokenIssuedAt.UnixMilli()
		authTokenDTOJson, marshalErr := json.Marshal(authTokenDTO)
		if marshalErr == nil {
			authService.redisClient.Set(c, constants.RedisTokenKeyPrefix+userDetail.Username, authTokenDTOJson, time.Duration(jwtConfig.AccessTokenExpiredMs+1000)*time.Millisecond)
		} else {
			errors.LogError(marshalErr)
			return response, marshalErr
		}

		// Cập nhật/Tạo bản auth_info
		if err != nil || currentAuthInfo.Id == 0 { // trường hợp chưa có tồn bản ghi auth_info nào
			currentAuthInfo = entities2.AuthInfo{
				UserId:                int64(userDetail.Id),
				Token:                 authTokenDTO.AccessToken,
				RefreshToken:          authTokenDTO.RefreshToken,
				Status:                TokenValidStatus,
				CreatedAt:             time.Now(),
				LastLoginAt:           time.Now(),
				IpAddress:             c.ClientIP(),
				RefreshTokenExpiredAt: time.UnixMilli(authTokenDTO.IssuedAt + jwtConfig.RefreshTokenExpiredMs),
			}
		} else {
			currentAuthInfo.Token = authTokenDTO.AccessToken
			currentAuthInfo.RefreshToken = authTokenDTO.RefreshToken
			currentAuthInfo.IpAddress = c.ClientIP()
			currentAuthInfo.Status = TokenValidStatus
			currentAuthInfo.LastLoginAt = time.Now()
			currentAuthInfo.RefreshTokenExpiredAt = time.UnixMilli(authTokenDTO.IssuedAt + jwtConfig.RefreshTokenExpiredMs)
		}

		// Set thông tin thời gian token được cấp mới
		currentAuthInfo.TokenIssuedAt = tokenIssuedAt

		// set thông tin vào response
		response = dtos.LoginResponseDTO{
			AccessToken:  jwtToken,
			RefreshToken: refreshToken,
			IssuedAt:     utils.ParseTimeToString(tokenIssuedAt, utils.YyyyMmDdHhMmSs),
			Roles: stream.Of(userDetail.Roles...).MapToString(func(item entities3.Role) string {
				return item.Code
			}).ToSlice(),
		}
	} else {
		currentAuthInfo.LastLoginAt = utils.InCurrentTimeZone(time.Now())
		response.AccessToken = authTokenDTO.AccessToken
		response.RefreshToken = authTokenDTO.RefreshToken
		response.IssuedAt = utils.ParseTimeToString(time.UnixMilli(authTokenDTO.IssuedAt), utils.YyyyMmDdHhMmSs)
		response.Roles = stream.Of(userDetail.Roles...).MapToString(func(item entities3.Role) string {
			return item.Code
		}).ToSlice()
	}
	// Lưu các thay đổi về thông tin auth mới nhất
	saveError := authService.pgCommonRepository.Save(c, &currentAuthInfo)
	if saveError != nil {
		errors.LogError(saveError)
		return response, saveError
	}

	// Lưu login_history vào MongoDB làm async với go routine
	go authService.saveLoginHistory(&currentAuthInfo)
	return response, nil
}

// RefreshToken Hàm refresh accessToken sử dụng accessToken cung cấp/**
func (authService *AuthenticationService) RefreshToken(refreshToken string, c *gin.Context) (dtos.RefreshTokenResDTO, error) {
	// Kiểm tra refreshToken có đang được lưu không
	authInfo, findErr := authService.authInfoRepository.FindByRefreshToken(refreshToken)
	if findErr != nil || authInfo.Id == 0 {
		errors.LogError(findErr, utils.GetCallLocation())
		return dtos.RefreshTokenResDTO{}, errors.ThrowResourceNotFoundError(constants.ResourceAuthInfo, constants.ErrorKeyRefreshToken, refreshToken)
	}

	// Kiểm tra đã có tokenAuth trong Redis hay chưa, nếu chưa có <-> chưa login thì reject việc refresh
	numExistedKeys, notLoggedInErr := authService.redisClient.Exists(c, constants.RedisTokenKeyPrefix+authInfo.User.Username).Result()
	if notLoggedInErr != nil {
		return dtos.RefreshTokenResDTO{}, &errors.UnauthorizedError{Message: notLoggedInErr.Error()}
	} else if numExistedKeys == 0 {
		return dtos.RefreshTokenResDTO{}, &errors.UnauthorizedError{Message: "Not logged in"}
	}

	// Kiểm tra trạng thái accessToken và refreshToken
	if !authutils.ValidateJwt(authInfo.Token) || authInfo.Status == TokenInvalidStatus {
		// Kiểm tra trạng thái của refreshToken
		if authInfo.RefreshTokenExpiredAt.Before(time.Now()) {
			return dtos.RefreshTokenResDTO{}, errors.ThrowBadRequestError(constants.ErrorCodeExpiredRefreshToken, constants.ErrorKeyRefreshToken, refreshToken)
		}

		// Cấp lại accessToken và lưu lại tin vào bảng auth_info
		authInfo.Status = TokenValidStatus
		newAccessToken, genJwtErr := authutils.GenerateJwtWithUserEntity(authInfo.User, global.AppConfig.Jwt)
		if genJwtErr != nil {
			errors.LogError(genJwtErr)
			return dtos.RefreshTokenResDTO{}, genJwtErr
		}
		authInfo.Token = newAccessToken
		authInfo.TokenIssuedAt = time.Now()
		saveErr := authService.pgCommonRepository.Save(c, &authInfo)
		if saveErr != nil {
			errors.LogError(saveErr)
			return dtos.RefreshTokenResDTO{}, saveErr
		}

		// set new token vào redis token store
		authTokenDTO := dtos.AuthValidationDTO{
			AccessToken:  authInfo.Token,
			RefreshToken: authInfo.RefreshToken,
			IssuedAt:     authInfo.TokenIssuedAt.UnixMilli(),
			Status:       TokenValidStatus,
		}
		authTokenDTOJson, marshalErr := json.Marshal(authTokenDTO)
		if marshalErr == nil {
			errors.LogError(marshalErr)
			return dtos.RefreshTokenResDTO{}, marshalErr
		}

		authService.redisClient.Set(c, constants.RedisTokenKeyPrefix+authInfo.User.Username, authTokenDTOJson,
			time.Duration(global.AppConfig.Jwt.AccessTokenExpiredMs+1000)*time.Millisecond)

		return dtos.RefreshTokenResDTO{AccessToken: authInfo.Token, RefreshToken: authInfo.RefreshToken}, nil
	}
	return dtos.RefreshTokenResDTO{AccessToken: authInfo.Token, RefreshToken: authInfo.RefreshToken}, nil
}

func (authService *AuthenticationService) Logout(_ dtos.LogoutRequestDTO, c *gin.Context) error {
	// TODO: logoutRequest -> deviceToken để implement việc revoke token theo device
	username := utils2.GetCurrentUsername(c)
	redisAuthKey := constants.RedisTokenKeyPrefix + username
	// revoke token auth in redis
	cmd := authService.redisClient.Del(c, redisAuthKey)
	if cmd.Err() != nil {
		errors.LogError(cmd.Err())
		return cmd.Err()
	}
	return nil
}

// VerifyToken verify accessToken dạng jwt
func (authService *AuthenticationService) VerifyToken(tokenHeader string, c *gin.Context) (bool, error) {
	defer utils.LogFunction()()

	isValid := false
	// Validate định dạng token và lấy giá trị của token bỏ prefix
	jwtToken, ok, err := authutils.ExtractAndValidateTokenFormat(tokenHeader)
	if !ok && err != nil {
		errors.LogError(err)
		isValid = false
		return isValid, err
	}
	// lấy config và parse token -> claims
	jwtConfig := global.AppConfig.Jwt
	mapClaims, parseJwtErr := authutils.ParseJwt(jwtToken, jwtConfig)
	if parseJwtErr != nil {
		errors.LogError(parseJwtErr)
		return isValid, parseJwtErr
	}

	// Validate thời hạn token
	expireTime, expiredTimeErr := mapClaims.GetExpirationTime()
	if expiredTimeErr != nil || expireTime.Before(time.Now()) {
		if err != nil {
			errors.LogError(expiredTimeErr)
		}
		return isValid, expiredTimeErr
	}

	// Check thông tin user
	username := mapClaims["username"].(string)
	userEntity, findError := authService.findUserByUserName(username)
	if findError != nil {
		errors.LogError(findError, utils.GetCallLocation())
		return isValid, findError
	}
	// sử dụng type UserDetails để lưu vào context
	var userDetail dtos.UserDetails
	copyErr := copier.Copy(&userDetail, &userEntity)
	if copyErr != nil {
		errors.LogError(copyErr)
		return isValid, copyErr
	}

	// Check token trong Redis
	var tokenDTO dtos.AuthValidationDTO
	redisGetCmd := authService.redisClient.Get(c, constants.RedisTokenKeyPrefix+userDetail.Username)
	if redisGetCmd.Err() != nil {
		errors.LogError(redisGetCmd.Err())
		return isValid, &errors.UnauthorizedError{Message: redisGetCmd.Err().Error()}
	} else {
		redisTokenErr := json.Unmarshal([]byte(redisGetCmd.Val()), &tokenDTO)
		if redisTokenErr != nil {
			errors.LogError(redisTokenErr)
			return isValid, redisTokenErr
		} else {
			if tokenDTO.AccessToken == jwtToken {
				isValid = true
				// set thông tin vào context của request -> sử dụng trong toàn bộ luồng request
				utils2.SetSecurityContext(c, userDetail)
			}
		}
	}
	return isValid, nil
}

// Hàm lấy user theo username -> xử lý error not found trong hàm
func (authService *AuthenticationService) findUserByUserName(username string) (entities.UserEntity, error) {
	userEntity, err := authService.userRepository.LoadUserByUsername(username)
	if err != nil {
		errors.LogError(err, utils.GetCallLocation())
		return userEntity, err
	} else if userEntity.Id == 0 {
		return userEntity, errors.ThrowResourceNotFoundError(constants.ResourceUser, constants.ErrorKeyUsername, username)
	}
	return userEntity, nil
}

// Lưu log lịch sử đăng nhập vào mongo
func (authService *AuthenticationService) saveLoginHistory(authInfo *entities2.AuthInfo) {
	defer utils.LogFunction()()
	loginHistory := mongo2.LoginHistoryDocument{
		UserId:      authInfo.UserId,
		LoggedInAt:  authInfo.LastLoginAt,
		IpAddresses: authInfo.IpAddress,
	}
	// save bản ghi mới vào collection
	_, err := authService.mongoClient.Database(global.AppConfig.Databases.MongoDB.Database).
		Collection(constants.LoginHistoryCollection).InsertOne(context.TODO(), loginHistory)
	if err != nil {
		errors.LogError(err, "Save login history error!", utils.GetCallLocation())
	}
}
