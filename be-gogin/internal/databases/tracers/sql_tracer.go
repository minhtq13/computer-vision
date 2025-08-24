package tracers

import (
	"context"
	"fmt"

	"github.com/jackc/pgx/v5"
	"go.uber.org/zap"
)

type SqlQueryTracer struct {
	logger *zap.Logger
}

func NewSqlQueryTracer(logger *zap.Logger) *SqlQueryTracer {
	return &SqlQueryTracer{logger: logger}
}

func (tracer *SqlQueryTracer) TraceQueryStart(ctx context.Context, _ *pgx.Conn, data pgx.TraceQueryStartData) context.Context {
	tracer.logger.Info(fmt.Sprintf("[PostgreSQLC] Started executing query: %s with args %v", data.SQL, data.Args))
	return ctx
}

func (tracer *SqlQueryTracer) TraceQueryEnd(_ context.Context, _ *pgx.Conn, data pgx.TraceQueryEndData) {
	if err := data.Err; err == nil {
		tracer.logger.Info(fmt.Sprintf("[PostgreSQLC] Ended executing query successfully with result: [%v]", data.CommandTag.String()))
	} else {
		tracer.logger.Error(fmt.Sprintf("[PostgreSQLC] Ended executing query with error: [%v]", data.Err.Error()))
	}
}
