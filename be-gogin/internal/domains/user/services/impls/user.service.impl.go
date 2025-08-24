package impls

import (
	"be-gogin/global/constants"
	"be-gogin/internal/databases/repository"
	"be-gogin/internal/domains/auth/repositories/postgres"
	"be-gogin/internal/domains/common/utils"
	"be-gogin/internal/domains/user/dtos"
	postgres2 "be-gogin/internal/domains/user/repositories/postgres"
	"be-gogin/internal/domains/user/services"
	"be-gogin/internal/errors"
	pkgutils "be-gogin/pkg/utils"
	"database/sql"
	"encoding/json"
	errors2 "errors"
	"github.com/gin-gonic/gin"
	"github.com/jinzhu/copier"
	"strconv"
)

type UserService struct {
	pgCommonRepository repository.IRepository
	userRepository     postgres2.IUserPGRepository
	authInfoRepository postgres.IAuthInfoPGRepository
}

func NewUserService(
	pgCommonRepository repository.IRepository,
	userRepo postgres2.IUserPGRepository,
	authInfo postgres.IAuthInfoPGRepository,
) services.IUserService {
	return &UserService{
		pgCommonRepository: pgCommonRepository,
		userRepository:     userRepo,
		authInfoRepository: authInfo,
	}
}

/**
Implementation
*/

func (us *UserService) GetProfile(c *gin.Context) (dtos.ProfileUserDTO, error) {
	currentUserId := utils.GetCurrentUserId(c)
	userDetail, err := us.userRepository.GetUserDetail(currentUserId, c)
	if err != nil {
		errors.LogError(err)
		if errors2.Is(err, sql.ErrNoRows) {
			return dtos.ProfileUserDTO{}, errors.ThrowResourceNotFoundError(constants.ResourceUser, constants.ErrorKeyId, strconv.FormatInt(currentUserId, 10))
		} else {
			return dtos.ProfileUserDTO{}, err
		}
	}
	response := dtos.ProfileUserDTO{}
	copyErr := copier.Copy(&response, &userDetail)
	if copyErr != nil {
		errors.LogError(copyErr)
		return response, copyErr
	}

	// xử lý các custom convert: nested nested object/ datetime format
	roleMappingErr := json.Unmarshal([]byte(*userDetail.RoleJSON), &response.Roles)
	if roleMappingErr != nil {
		errors.LogError(roleMappingErr)
		return response, roleMappingErr
	}
	response.CreatedAt = pkgutils.ParseTimeToString(*userDetail.CreatedAt, pkgutils.YyyyMmDdHhMmSs)
	response.ModifiedAt = pkgutils.ParseTimeToString(*userDetail.ModifiedAt, pkgutils.YyyyMmDdHhMmSs)
	response.BirthDate = pkgutils.ParseTimeToString(*userDetail.BirthDate, pkgutils.DdMmYyyySlash)

	return response, nil
}
