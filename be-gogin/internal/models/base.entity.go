package models

import "time"

type BaseEntity struct {
	CreatedAt   time.Time `gorm:"column:created_at"`
	ModifiedAt  time.Time `gorm:"column:modified_at"`
	CreatedBy   int64     `gorm:"column:created_by"`
	ModifiedBy  int64     `gorm:"column:modified_by"`
	DeletedFlag int16     `gorm:"column:deleted_flag"`
}
