package messaging

import (
	"context"
)

type IMessageHandler interface {
	Handle(ctx context.Context, topic string, partition int, offset string, value []byte) error
}
