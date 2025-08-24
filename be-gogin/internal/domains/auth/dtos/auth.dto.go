package dtos

import (
	entities2 "be-gogin/internal/domains/department/entities"
	entities3 "be-gogin/internal/domains/role/entities"
	"github.com/google/uuid"
	"github.com/jackc/pgtype"
	"time"
)

type LoginRequestDTO struct {
	Username string `json:"username" binding:"required,min=5,max=50"`
	Password string `json:"password" binding:"required,min=5,max=100"`
}

type LoginResponseDTO struct {
	AccessToken  string   `json:"accessToken"`
	RefreshToken string   `json:"refreshToken"`
	IssuedAt     string   `json:"issuedAt"`
	Roles        []string `json:"roles"`
}

type RefreshTokenResDTO struct {
	AccessToken  string `json:"accessToken"`
	RefreshToken string `json:"refreshToken"`
}

type AuthValidationDTO struct {
	AccessToken  string `json:"accessToken"`
	RefreshToken string `json:"refreshToken"`
	Status       int    `json:"status"`
	DeviceToken  string `json:"deviceToken"`
	IssuedAt     int64  `json:"issuedAt"`
}

type UserDetails struct {
	Id            uint64                 `json:"id"`
	Code          string                 `json:"code"`
	LastName      string                 `json:"lastName"`
	FirstName     string                 `json:"firstName"`
	PhoneNumber   string                 `json:"phoneNumber"`
	Username      string                 `json:"username"`
	Password      string                 `json:"password"`
	Email         string                 `json:"email"`
	Status        int                    `json:"status"`
	IsEnabled     bool                   `json:"isEnabled"`
	FcmToken      string                 `json:"fcmToken"`
	ActivationKey string                 `json:"activationKey"`
	CreatedSource int                    `json:"createdSource"`
	UserUUID      uuid.UUID              `json:"userUUID"`
	Roles         []entities3.Role       `json:"roles"`
	Departments   []entities2.Department `json:"departments"`
	MetaData      pgtype.JSONB           `json:"metaData"`
	CreatedAt     time.Time              `json:"createdAt"`
	ModifiedAt    time.Time              `json:"modifiedAt"`
	CreatedBy     int64                  `json:"createdBy"`
	ModifiedBy    int64                  `json:"modifiedBy"`
	DeletedFlag   int16                  `json:"deletedFlag"`
}

type RefreshTokenRequestHeader struct {
	RefreshToken string `json:"refreshToken" header:"refreshToken" binding:"required"`
}

type LogoutRequestDTO struct {
	DeviceToken string `json:"deviceToken" binding:"required,max=255"`
}
