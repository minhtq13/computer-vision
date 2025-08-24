package errors

type InternalServerError struct {
	Message   string `json:"message"`
	Status    int    `json:"status"`
	Error     string `json:"error"`
	Timestamp string `json:"timestamp"`
	Path      string `json:"path"`
}
