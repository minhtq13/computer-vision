package errors

type UnauthorizedError struct {
	Message string `json:"message"`
	Code    string `json:"code"`
}

func (err *UnauthorizedError) Error() string {
	return err.Message
}
