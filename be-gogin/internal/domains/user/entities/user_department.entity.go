package entities

import "be-gogin/global"

type UserDepartment struct {
	Id           uint64 `gorm:"column:id; type=int8; not null; primary_key; unique"`
	UserId       uint64 `gorm:"column:user_id; type=int8; not null"`
	DepartmentId uint64 `gorm:"column:department_id; type=int8; not null"`
}

func (user *UserDepartment) TableName() string {
	return global.AppConfig.Databases.Postgres.Master.Schema + "." + "users_departments"
}
