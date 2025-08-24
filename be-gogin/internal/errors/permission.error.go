package errors

type AccessDeniedError struct {
	Message string `json:"message"`
	Status  int    `json:"status"`
}

func (err *AccessDeniedError) Error() string {
	return err.Message
}
