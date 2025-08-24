package converters

import (
	"fmt"
	"reflect"
	"time"
)

// ApplyStandardizingWithoutTimeZone : Func apply việc convert time để lưu vào db do gorm tự động tính theo utc khi lưu vào db
func ApplyStandardizingWithoutTimeZone(entity any, timezone string) error {
	// lấy offset theo local time
	location, _ := time.LoadLocation(timezone)
	zone, offsetInSecond := time.Now().In(location).Zone()
	fmt.Println(zone, offsetInSecond)
	offsetInMinute := offsetInSecond / 60

	// loop các field có dạng time -> nếu timezone offset <> +/-0 thì + duration = offsetInHour để giữ định dạng cũ trong DB Postgres
	values := reflect.ValueOf(entity).Elem()
	types := reflect.TypeOf(entity).Elem()

	for i := 0; i < types.NumField(); i++ {
		field := types.Field(i)
		fieldValue := values.Field(i)

		// kiểm tra định dạng kiểu có phải time.Time hay không
		if field.Type != reflect.TypeOf(time.Time{}) {
			continue
		}

		// kiểm tra zone offset của field hiện tại là bao nhiêu, nếu +0 thì không apply
		// inputOffsetInSecond/1800s => số phút lệch với UTC (diff nhỏ nhất là 30p)
		if _, inputOffsetInSecond := fieldValue.Interface().(time.Time).Zone(); inputOffsetInSecond/1800 == 0 {
			continue
		}

		// thêm duration vào để giữ value time gốc theo timezone của cấu hình ứng dụng
		newTime := fieldValue.Interface().(time.Time).Add(time.Duration(offsetInMinute) * time.Minute)
		fieldValue.Set(reflect.ValueOf(newTime))
	}
	return nil
}

// ApplyStandardizingTimeZone : Func apply việc convert time từ db without timezone -> timezone trong go
func ApplyStandardizingTimeZone(entity any, timezone string) error {
	// lấy offset theo local time
	location, _ := time.LoadLocation(timezone)
	zone, offsetInSecond := time.Now().In(location).Zone()
	fmt.Println(zone, offsetInSecond)
	offsetInMinute := offsetInSecond / 60

	// loop các field có dạng time -> nếu timezone offset = +0 thì - duration = offsetInHour chuyển đổi về định dạng trên go
	values := reflect.ValueOf(entity).Elem()
	types := reflect.TypeOf(entity).Elem()

	for i := 0; i < types.NumField(); i++ {
		field := types.Field(i)
		fieldValue := values.Field(i)

		// kiểm tra định dạng kiểu có phải time.Time hay không
		if field.Type != reflect.TypeOf(time.Time{}) {
			continue
		}

		// kiểm tra zone offset của field hiện tại là bao nhiêu <> 0 thì không apply
		// inputOffsetInSecond/1800s => số phút lệch với UTC (diff nhỏ nhất là 30p)
		if _, inputOffsetInSecond := fieldValue.Interface().(time.Time).Zone(); inputOffsetInSecond/1800 != 0 {
			continue
		}

		// trừ duration vào convert timezone của cấu hình ứng dụng
		newTime := fieldValue.Interface().(time.Time).Add(time.Duration(-offsetInMinute) * time.Minute).In(location)
		fieldValue.Set(reflect.ValueOf(newTime))
	}
	return nil
}
