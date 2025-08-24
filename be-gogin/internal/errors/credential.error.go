package errors

type BadCredentialError struct {
	Message string `json:"message"`
	Code    string `json:"code"`
	Status  int    `json:"status"`
}

func (e *BadCredentialError) Error() string { return e.Message }
