package repository

import (
	"be-gogin/internal/databases"
	"be-gogin/internal/errors"
	"be-gogin/internal/initializers"
	"be-gogin/pkg/utils"
	"context"
	"encoding/json"

	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

// CommonPGRepository Implement IRepository các func dùng chung của các domain repository/**
type CommonPGRepository struct {
	PostgresGorm *initializers.PostgresGormConnection
	PostgresSqlc *initializers.PostgresSqlcConnection
}

func NewCommonPGRepository(
	postgresGorm *initializers.PostgresGormConnection,
	postgresSqlc *initializers.PostgresSqlcConnection) *CommonPGRepository {
	return &CommonPGRepository{
		PostgresGorm: postgresGorm,
		PostgresSqlc: postgresSqlc,
	}
}

func (p *CommonPGRepository) FindById(id int64) (interface{}, error) {
	//TODO implement me
	panic("implement me")
}

func (p *CommonPGRepository) Save(context context.Context, entity interface{}) error {
	defer utils.LogFunction()()
	var saveError error
	// sử dụng transaction tổng, rollback khi error khác nil
	if transactionManager := databases.GetTx(context.(*gin.Context)); transactionManager != nil {
		saveError = transactionManager.PgGormTx.Save(entity).Error
	} else {
		// sử dụng transaction chỉ của hàm save, rollback khi error khác nil
		saveError = p.PostgresGorm.PostgresMaster.Transaction(func(tx *gorm.DB) error {
			return tx.Save(entity).Error
		})
	}
	// log error và thông tin entity
	if saveError != nil {
		itemError, _ := json.Marshal(entity)
		errors.LogError(saveError, string(itemError))
		return saveError
	}
	return nil
}

func (p *CommonPGRepository) SaveAll(context context.Context, entities []interface{}) error {
	defer utils.LogFunction()()
	var saveError error
	var errorEntity interface{}
	// sử dụng transaction tổng, rollback khi error khác nil
	if transactionManager := databases.GetTx(context.(*gin.Context)); transactionManager != nil {
		for i := 0; i < len(entities); i++ {
			saveError = transactionManager.PgGormTx.Save(entities[i]).Error
			if saveError != nil {
				errorEntity = entities[i]
				break
			}
		}
	} else {
		// sử dụng transaction chỉ của hàm save, rollback khi error khác nil
		saveError = p.PostgresGorm.PostgresMaster.Transaction(func(tx *gorm.DB) error {
			for i := 0; i < len(entities); i++ {
				errorEntity = entities[i]
				if err := tx.Save(entities[i]).Error; err != nil {
					errorEntity = entities[i]
					return err
				}
			}
			return nil
		})
	}
	if saveError != nil && errorEntity != nil {
		itemError, _ := json.Marshal(errorEntity)
		errors.LogError(saveError, string(itemError))
		return saveError
	}
	return nil
}

func (p *CommonPGRepository) DeleteById(_ context.Context, id int64) error {
	//TODO implement me
	panic("implement me")
}
