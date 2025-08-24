package postgres

import (
	"be-gogin/global/sqls"
	"be-gogin/internal/domains/auth/entities"
	"be-gogin/internal/initializers"
	"be-gogin/pkg/utils"
	"database/sql"
)

type IAuthInfoPGRepository interface {
	FindFirstByUserId(userId int64) (entities.AuthInfo, error)
	FindByRefreshToken(refreshToken string) (entities.AuthInfo, error)
}

type AuthInfoPGRepository struct {
	postgresGorm *initializers.PostgresGormConnection
}

func NewAuthInfoPGRepository(postgresGorm *initializers.PostgresGormConnection) IAuthInfoPGRepository {
	return &AuthInfoPGRepository{
		postgresGorm: postgresGorm,
	}
}

/**
Implementation
*/

func (authInfoRepo *AuthInfoPGRepository) FindFirstByUserId(userId int64) (entities.AuthInfo, error) {
	var authInfo entities.AuthInfo
	query := utils.FormatSqlQuery(sqls.GetLatestAuthInfoByUserId)
	authInfoRepo.postgresGorm.PostgresMaster.Raw(query, sql.Named("userId", userId)).First(&authInfo)
	return authInfo, nil
}

func (authInfoRepo *AuthInfoPGRepository) FindByRefreshToken(refreshToken string) (entities.AuthInfo, error) {
	var authInfo entities.AuthInfo
	query := utils.FormatSqlQuery(sqls.GetFirstAuthInfoByRefreshToken)
	authInfoRepo.postgresGorm.PostgresMaster.
		Raw(query, sql.Named("refreshToken", refreshToken)).
		Preload("User").
		First(&authInfo)
	return authInfo, nil
}
