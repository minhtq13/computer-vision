package basic

import (
	"github.com/go-playground/assert/v2"
	"testing"
)

func AddOne(input int) int {
	return input + 1
}

func TestAddOne(testing *testing.T) {
	assert.Equal(testing, AddOne(1), 4)
}
