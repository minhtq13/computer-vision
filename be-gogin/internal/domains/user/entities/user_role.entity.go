package entities

import "be-gogin/global"

type UserRole struct {
	Id     uint64 `gorm:"column:id; type=int8; not null; primary_key; unique"`
	UserId uint64 `gorm:"column:user_id; type=int8; not null"`
	RoleId uint64 `gorm:"column:role_id; type=int8; not null"`
}

func (user *UserRole) TableName() string {
	return global.AppConfig.Databases.Postgres.Master.Schema + "." + "users_roles"
}
