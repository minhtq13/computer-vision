package utils

import "time"

const (
	TimeZone       = "Asia/Ho_Chi_Minh"
	YyyyMmDdHhMmSs = "2006-01-02 15:04:05"
	YyyyMmDd       = "2006-01-02"
	DdMmYyyy       = "02-01-2006"
	DdMmYyyySlash  = "02/01/2006"
)

/**
Các hàm convert qua lại string và time với pattern cụ thể
*/

func ParseTimeToString(input time.Time, pattern string) string {
	return time.Time.Format(input, pattern)
}

func ParseStringToTime(input string, pattern string) (time.Time, error) {
	return time.Parse(pattern, input)
}

func InCurrentTimeZone(input time.Time) time.Time {
	return input.Local()
}

func InUTC(input time.Time) time.Time {
	return input.In(time.UTC)
}
