APP_NAME := server
BUILD_DIR := ./build
CMD_DIR := ./cmd/server
WIRE_DIR := ./internal/wire
makefile.PHONY: build run wire clean test
# Generate wire code
.PHONY: wire
wire:
	cd  ${WIRE_DIR} && wire

# Build application
.PHONY: build
build: wire
	go build -o ${BUILD_DIR} ${CMD_DIR}/main.go

.PHONY: run
# Run application
run: wire
	go run ${CMD_DIR}/main.go

.PHONY: clean
# Clean generated files
clean:
	rm -f ${WIRE_DIR}/wire_gen.go
	rm -rf ${BUILD_DIR}

# Install tools
tools:
	go mod download
	go install github.com/google/wire/cmd/wire@latest

# Development workflow
dev: clean wire run