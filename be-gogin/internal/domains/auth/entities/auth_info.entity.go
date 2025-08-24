package entities

import (
	"be-gogin/global"
	"be-gogin/internal/domains/user/entities"
	"be-gogin/pkg/converters"
	"be-gogin/pkg/utils"
	"gorm.io/gorm"
	"time"
)

type AuthInfo struct {
	Id                    uint64              `gorm:"column:id; type:int8; not null; primary_key; unique"`
	UserId                int64               `gorm:"column:user_id; type:int8; not null"`
	User                  entities.UserEntity `gorm:"foreignKey:user_id"`
	Token                 string              `gorm:"column:token; type:text"`
	Status                int                 `gorm:"column:status; type:int2"`
	IpAddress             string              `gorm:"column:ip_address; type:varchar(32)"`
	CreatedAt             time.Time           `gorm:"column:created_at"`
	LastLoginAt           time.Time           `gorm:"column:last_login_at"`
	RefreshToken          string              `gorm:"column:refresh_token; type:text"`
	RefreshTokenExpiredAt time.Time           `gorm:"column:rf_token_expired_at"`
	TokenIssuedAt         time.Time           `gorm:"column:token_issued_at"`
}

func (auth *AuthInfo) TableName() string {
	return global.AppConfig.Databases.Postgres.Master.Schema + "." + "auth_info"
}

func (auth *AuthInfo) BeforeSave(tx *gorm.DB) (err error) {
	return converters.ApplyStandardizingWithoutTimeZone(auth, utils.TimeZone)
}

func (auth *AuthInfo) AfterFind(tx *gorm.DB) (err error) {
	return converters.ApplyStandardizingTimeZone(auth, utils.TimeZone)
}
