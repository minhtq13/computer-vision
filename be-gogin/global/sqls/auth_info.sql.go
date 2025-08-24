package sqls

/**
Native sqlc using with gorm
*/

const GetLatestAuthInfoByUserId = "select * \n" +
	"from ${schema}auth_info \n" +
	"where \n" +
	"user_id = @userId \n" +
	"order by created_at desc \n"

const GetFirstAuthInfoByRefreshToken = "select * \n" +
	"from ${schema}auth_info \n" +
	"where \n" +
	"	refresh_token = @refreshToken \n" +
	"order by created_at desc " +
	"limit 1 \n"
