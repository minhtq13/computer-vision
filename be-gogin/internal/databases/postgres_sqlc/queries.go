package postgres_sqlc

import (
	"context"
	"github.com/jackc/pgx/v5"
)

// DB Hàm return DBTX for custom query execution/**
func (queries *Queries) DB() DBTX {
	return queries.db
}

// QueryOneTo Executing a query and getting only one row -> mapping to target struct
func QueryOneTo[T any](c context.Context, db DBTX, query string, args ...interface{}) (T, error) {
	rows, queryErr := db.Query(c, query, args...)
	// Khởi tạo result kiểu là T
	result := *new(T)
	defer rows.Close()
	if queryErr != nil {
		return result, queryErr
	}

	// mapping to target struct
	result, mappingErr := pgx.CollectOneRow(rows, pgx.RowToStructByName[T])
	if mappingErr != nil {
		return result, mappingErr
	}

	return result, nil
}

// QueryManyTo Executing a query and getting list rows -> mapping to target struct
func QueryManyTo[T any](c context.Context, db DBTX, query string, args ...any) ([]T, error) {
	rows, queryErr := db.Query(c, query, args...)
	// Khởi tạo 1 slice T
	results := make([]T, 0)
	defer rows.Close()
	if queryErr != nil {
		return results, queryErr
	}

	// mapping to target struct
	results, mappingErr := pgx.CollectRows(rows, pgx.RowToStructByName[T])
	if mappingErr != nil {
		return results, mappingErr
	}
	return results, nil
}
