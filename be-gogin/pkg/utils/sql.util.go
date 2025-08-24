package utils

import (
	"be-gogin/global"
	"regexp"
	"strconv"
	"strings"
)

// PrepareSqlQuery Hàm format và định dạng schema, params/**
func PrepareSqlQuery(rawSql string) string {
	preparedSql := strings.Replace(rawSql, "${schema}", global.AppConfig.Databases.Postgres.Master.Schema+".", -1)

	// replace param dạng @param -> $position
	re := regexp.MustCompile(`@\w+`)
	matches := re.FindAllString(preparedSql, -1)
	for idx, match := range matches {
		preparedSql = strings.Replace(preparedSql, match, "$"+strconv.Itoa(idx+1), -1)
	}

	return preparedSql
}
