package entities

import (
	"be-gogin/global"
	entities2 "be-gogin/internal/domains/department/entities"
	entities3 "be-gogin/internal/domains/role/entities"
	"be-gogin/internal/models"
	"be-gogin/pkg/converters"
	"be-gogin/pkg/utils"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgtype"
	"gorm.io/gorm"
)

type UserEntity struct {
	Id                   uint64                 `gorm:"column:id; type:int8; primary_key; not null; unique"`
	Code                 string                 `gorm:"column:code type:varchar(20)"`
	Gender               int                    `gorm:"column:gender type:int4"`
	IdentificationNumber string                 `gorm:"column:identification_number; type:varchar(20)"`
	IdentityType         int16                  `gorm:"column:identity_type; type:int4"`
	LastName             string                 `gorm:"column:last_name; type:text"`
	FirstName            string                 `gorm:"column:first_name"`
	BirthDate            time.Time              `gorm:"column:birth_date; type:timestamp"`
	PhoneNumber          string                 `gorm:"column:phone_number"`
	Address              string                 `gorm:"column:address"`
	Username             string                 `gorm:"column:username"`
	AvatarId             int64                  `gorm:"column:avatar_id"`
	Email                string                 `gorm:"column:email"`
	Password             string                 `gorm:"column:password" json:"-"`
	Status               int                    `gorm:"column:status type:int4"`
	IsEnabled            bool                   `gorm:"column:is_enabled; type:boolean"`
	FcmToken             string                 `gorm:"column:fcm_token"`
	ActivationKey        string                 `gorm:"column:activation_key"`
	CreatedSource        int                    `gorm:"column:created_source"`
	UserUUID             uuid.UUID              `gorm:"column:user_uuid; type:uuid"`
	Roles                []entities3.Role       `gorm:"many2many:users_roles; foreignKey:id; joinForeignKey:user_id; References:id; joinReferences:role_id"`
	Departments          []entities2.Department `gorm:"many2many:UserDepartment;foreignKey:Id;joinForeignKey:UserId;References:Id;joinReferences:DepartmentId"`
	MetaData             pgtype.JSONB           `gorm:"column:meta_data; type:jsonb"`
	models.BaseEntity    `gorm:"embedded"`
}

func (user *UserEntity) TableName() string {
	return global.AppConfig.Databases.Postgres.Master.Schema + "." + "users"
}

func (user *UserEntity) BeforeSave(_ *gorm.DB) (err error) {
	return converters.ApplyStandardizingWithoutTimeZone(user, utils.TimeZone)
}

func (user *UserEntity) AfterFind(_ *gorm.DB) (err error) {
	return converters.ApplyStandardizingTimeZone(user, utils.TimeZone)
}
