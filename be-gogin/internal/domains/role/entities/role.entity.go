package entities

import "be-gogin/global"

type Role struct {
	Id            uint64 `gorm:"column:id; type:int8; not null; primary_key;unique"`
	Code          string `gorm:"column:code"`
	Name          string `gorm:"column:name"`
	DisplayedName string `gorm:"column:displayed_name"`
	IsDefault     bool   `gorm:"column:is_default; type:boolean"`
}

func (role *Role) TableName() string {
	return global.AppConfig.Databases.Postgres.Master.Schema + "." + "role"
}
