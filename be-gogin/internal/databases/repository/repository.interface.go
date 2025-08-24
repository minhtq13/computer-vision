package repository

import "context"

// IRepository Khai báo các func dùng chung cho các domain repository/**
type IRepository interface {
	FindById(id int64) (interface{}, error)
	Save(context context.Context, entity interface{}) error
	SaveAll(context context.Context, entities []interface{}) error
	DeleteById(context context.Context, id int64) error
}
