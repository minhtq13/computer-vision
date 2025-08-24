# Build stage
FROM golang:1.24.3-alpine as builder

# Install base libs in os
RUN apk update && apk add --no-cache git ca-certificates tzdata gcc musl-dev

# Install Wire for dependency injection
RUN go install github.com/google/wire/cmd/wire@latest

WORKDIR /build

# Copy go.mod and go.sum first for better layer caching
COPY go.mod go.sum ./

# Download dependencies with cache mount for faster rebuilds
RUN --mount=type=cache,target=/go/pkg/mod go mod download

# Copy source
COPY . .

RUN wire ./internal/wire
RUN CGO_ENABLED=1 GOOS=linux GOARCH=amd64 go build -a -tags musl -installsuffix cgo -o server ./cmd/server/main.go

# Run stage
from alpine:latest as runner
# Install base libs in os
RUN apk update && apk add --no-cache ca-certificates tzdata

WORKDIR /app

# Copy config files
COPY ./configs ./configs
COPY ./*.json .

# Copy resources
ARG SOURCE_RESOURCE_DIR=/resources
ARG TARGET_APP_RESOURCE_DIR=./resources
# Copy app resources
COPY ${SOURCE_RESOURCE_DIR}/upload ${TARGET_APP_RESOURCE_DIR}/upload

# Copy excutable file to
COPY --from=builder /build/server .

# Setup env variables and expose port
ENV ENV_MODE=production \
    GIN_MODE=release \
    PORT=8090
EXPOSE $PORT

# Run server with entrypoint
ENTRYPOINT ["./server"]