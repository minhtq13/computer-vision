package converters

import (
	"be-gogin/global"
	"database/sql/driver"
	"encoding/json"

	"go.uber.org/zap"
)

type JsonB map[string]interface{}

// Value marshal convert go type -> database value
func (j *JsonB) Value() (driver.Value, error) {
	return json.Marshal(&j)
}

// Scan marshal, convert database value -> go type
func (j *JsonB) Scan(value interface{}) error {
	data, ok := value.([]byte)
	if !ok {
		global.Logger.Error("Type assertion .([]byte) failed", zap.String("jsonb", "map"))
	}
	return json.Unmarshal(data, &j)
}
