package postgres

import (
	"be-gogin/global/enums"
	"be-gogin/global/sqls"
	"be-gogin/internal/databases/postgres_sqlc"
	"be-gogin/internal/domains/user/dtos"
	"be-gogin/internal/domains/user/entities"
	"be-gogin/internal/errors"
	"be-gogin/internal/initializers"
	"be-gogin/pkg/utils"

	"github.com/gin-gonic/gin"
)

type IUserPGRepository interface {
	FindUserByUsernameAndDeletedFlagAndStatus(username string, deletedFlag int, status int) (entities.UserEntity, error)
	LoadUserByUsername(username string) (entities.UserEntity, error)
	GetUserDetail(id int64, context *gin.Context) (dtos.UserDetailDTO, error)
}

type UserPGRepository struct {
	postgresGorm *initializers.PostgresGormConnection
	postgresSqlc *initializers.PostgresSqlcConnection
}

func NewUserPGRepository(postgresGorm *initializers.PostgresGormConnection, postgresSqlc *initializers.PostgresSqlcConnection) IUserPGRepository {
	return &UserPGRepository{
		postgresGorm: postgresGorm,
		postgresSqlc: postgresSqlc,
	}
}

func (userRepo *UserPGRepository) FindUserByUsernameAndDeletedFlagAndStatus(username string, deletedFlag int, status int) (entities.UserEntity, error) {
	var user entities.UserEntity
	userRepo.postgresGorm.PostgresMaster.
		Where("username = ? and deleted_flag = ? and status = ?", username, deletedFlag, status).
		First(&user)
	return user, nil
}

func (userRepo *UserPGRepository) LoadUserByUsername(username string) (entities.UserEntity, error) {
	var user entities.UserEntity
	userRepo.postgresGorm.PostgresMaster.
		Where("username = ? and deleted_flag = ? and status = ?", username, enums.NotDeleted, enums.Enabled).
		Preload("Roles").
		Preload("Departments").
		First(&user)
	return user, nil
}

func (userRepo *UserPGRepository) GetUserDetail(id int64, context *gin.Context) (dtos.UserDetailDTO, error) {
	var userDetail dtos.UserDetailDTO
	sqlQuery := utils.PrepareSqlQuery(sqls.SqlGetUserDetail)
	userDetail, err := postgres_sqlc.QueryOneTo[dtos.UserDetailDTO](context, userRepo.postgresSqlc.PostgresSlave.Queries.DB(),
		sqlQuery, id)
	if err != nil {
		errors.LogError(err)
		return userDetail, err
	}
	return userDetail, nil
}
