package errors

import "time"

// BadRequestError Bao gồm các error: not found, invalid jwt, .../**
type BadRequestError struct {
	Code       string    `json:"code,omitempty"`
	Message    string    `json:"message,omitempty"`
	Entity     string    `json:"entity,omitempty"`
	FieldError string    `json:"fieldError,omitempty"`
	Timestamp  time.Time `json:"timestamp,omitempty"`
	Values     string    `json:"values,omitempty"`
}

func (err *BadRequestError) Error() string {
	return err.Message
}
