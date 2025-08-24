package constants

const (
	RequestBodyContextKey   = "body"
	RequestHeaderContextKey = "header"
	TransactionContextKey   = "tx"
)

// AllowedHeaders các header allowed
var AllowedHeaders = []string{"Authorization", "refreshToken"}
var AllowedMethods = []string{"GET", "POST", "PUT", "DELETE", "PATCH"}
var AllowedOrigins = []string{"*"}

const (
	AuthorizationHeader = "Authorization"
	TokenScheme         = "Bearer"
	ServerDefaultPrefix = "/e-learning/go"
	RedisTokenKeyPrefix = "ELS@CACHE-AUTH_USERNAME_"
	NoRequiredToken     = "NO_REQUIRED_TOKEN"
	RequiredToken       = "REQUIRED_TOKEN"
)

// Danh sách role name và role code tương ứng
const (
	RoleSuperAdminCode  = "ROLE_SUPER_ADMIN"
	RoleSystemAdminCode = "ROLE_SYSTEM_ADMIN"
	RoleTeacherCode     = "ROLE_TEACHER"
	RoleStudentCode     = "ROLE_STUDENT"
	RoleSuperAdmin      = "SUPER_ADMIN"
	RoleSystemAdmin     = "SYSTEM_ADMIN"
	RoleTeacher         = "TEACHER"
	RoleStudent         = "STUDENT"
)

// ApiRoleBaseAuthorizationMap map convert phân quyền api theo role dạng int -> string
var ApiRoleBaseAuthorizationMap = map[string]string{
	"-1": NoRequiredToken,
	"0":  RequiredToken,  // yêu cầu token với toàn bộ role
	"1":  RoleSuperAdmin, // chỉ dành cho role SUPER_ADMIN
	"2":  RoleTeacher,    // chỉ dành cho role TEACHER
	"3":  RoleStudent,    // chỉ dành cho role STUDENT
}
