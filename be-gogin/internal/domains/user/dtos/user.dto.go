package dtos

import (
	"be-gogin/internal/domains/role/dtos"
	"time"
)

type UserDetailDTO struct {
	ID                *int64     `json:"id"`
	Name              *string    `json:"name"`
	AvatarID          *int64     `json:"avatarId"`
	AvatarPath        *string    `json:"avatarPath"`
	AvatarStoredType  *int       `json:"avatarStoredType"`
	FirstName         *string    `json:"firstName"`
	LastName          *string    `json:"lastName"`
	Username          *string    `json:"username"`
	Code              *string    `json:"code"`
	IdentificationNum *string    `json:"identificationNum"`
	IdentityType      *string    `json:"identityType"`
	Address           *string    `json:"address"`
	Gender            *string    `json:"gender"`
	BirthDate         *time.Time `json:"birthDate"`
	PhoneNumber       *string    `json:"phoneNumber"`
	Email             *string    `json:"email"`
	UserName          *string    `json:"userName"`
	DepartmentName    *string    `json:"department"`
	LstDepartmentID   *string    `json:"lstDepartmentId"`
	CreatedAt         *time.Time `json:"createdAt"`
	ModifiedAt        *time.Time `json:"modifiedAt"`
	UserType          *int       `json:"userType"`
	RoleJSON          *string    `json:"roleJson"`
	CourseNum         *int       `json:"courseNum"`
	MetaData          *string    `json:"metaData"`
	FCMToken          *string    `json:"fcmToken"`
}

type ProfileUserDTO struct {
	Id                   int64          `json:"id,omitempty"`
	Name                 string         `json:"name,omitempty"`
	AvatarId             int64          `json:"avatarId,omitempty"`
	AvatarPath           string         `json:"avatarPath,omitempty"`
	AvatarStoredType     int            `json:"avatarStoredType,omitempty"`
	Code                 string         `json:"code,omitempty"`
	IdentificationNumber string         `json:"identificationNum,omitempty"`
	IdentityType         int            `json:"identityType,omitempty"`
	Address              string         `json:"address,omitempty"`
	BirthDate            string         `json:"birthDate,omitempty"`
	PhoneNumber          string         `json:"phoneNumber,omitempty"`
	Email                string         `json:"email,omitempty"`
	Username             string         `json:"username,omitempty"`
	Roles                []dtos.RoleDTO `json:"roles,omitempty"`
	DepartmentName       *string        `json:"departmentName"`
	DepartmentID         *int64         `json:"departmentId"`
	CreatedAt            string         `json:"createdAt,omitempty"`
	ModifiedAt           string         `json:"modifiedAt,omitempty"`
	FcmToken             string         `json:"fcmToken,omitempty"`
}
