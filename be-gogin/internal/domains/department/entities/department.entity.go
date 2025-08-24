package entities

import (
	"be-gogin/global"
	"be-gogin/internal/models"
	"be-gogin/pkg/converters"
	"be-gogin/pkg/utils"
	"gorm.io/gorm"
)

type Department struct {
	Id                uint64 `gorm:"column:id; type:int8; not null; primary_key;unique"`
	ParentId          int64  `gorm:"column:parent_id; type:int8"`
	ParentCode        string `gorm:"column:parent_code; type:varchar(50)"`
	Code              string `gorm:"column:code"`
	Name              string `gorm:"column:name"`
	Logo              string `gorm:"column:logo"`
	Address           string `gorm:"column:address"`
	PhoneNumber       string `gorm:"column:phone_number; type:varchar(11)"`
	Email             string `gorm:"column:email; type:varchar(100)"`
	Description       string `gorm:"column:description; type:text"`
	models.BaseEntity `gorm:"embedded"`
}

func (department *Department) TableName() string {
	return global.AppConfig.Databases.Postgres.Master.Schema + "." + "department"
}

func (department *Department) BeforeSave(_ *gorm.DB) (err error) {
	return converters.ApplyStandardizingWithoutTimeZone(department, utils.TimeZone)
}

func (department *Department) AfterFind(_ *gorm.DB) (err error) {
	return converters.ApplyStandardizingTimeZone(department, utils.TimeZone)
}
