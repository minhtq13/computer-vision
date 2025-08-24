package constants

const (
	MessageResourceNotFound    string = "Not found"
	MessageUsernameNotExists   string = "Username not exists"
	MessagePasswordIncorrect   string = "Username/Password incorrect"
	MessageAccessDenied        string = "Access denied"
	MessageInternalServerError string = "Internal Server Error"
)

const (
	ResourceUser           string = "user"
	ResourceRole           string = "role"
	ResourceRequestBody    string = "requestBody"
	ResourceRequestsHeader string = "requestHeader"
	ResourceAuthInfo       string = "authInfo"
)

const (
	ErrorCodeUnauthorized        = "error.unauthorized"
	ErrorCodeResourceNotFound    = "error.resource.not.found"
	ErrorCodeUserNotFound        = "user.error.not.found"
	ErrorCodeInvalidJwtToken     = "error.invalid.jwt.token"
	ErrorCodeInvalidField        = "error.field.invalid"
	ErrorCodeFieldMustNotEmpty   = "error.field.must.not.empty"
	ErrorCodeFieldLengthMin      = "error.field.length.min"
	ErrorCodeFieldLengthMax      = "error.field.length.max"
	ErrorCodeInvalidFormat       = "error.field.invalid.format"
	ErrorCodeExpiredRefreshToken = "error.expired.refresh.token"
)

var MapMessageError = map[string]string{
	ErrorCodeResourceNotFound:    "Not found",
	ErrorCodeUserNotFound:        "User not found",
	ErrorCodeInvalidJwtToken:     "Invalid JWT token",
	ErrorCodeExpiredRefreshToken: "Expired refresh token",
}

// error field key

const (
	ErrorKeyId           string = "id"
	ErrorKeyCode         string = "code"
	ErrorKeyUsername     string = "username"
	ErrorKeyRefreshToken string = "refreshToken"
)
