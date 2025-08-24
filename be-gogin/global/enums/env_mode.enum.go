package enums

type EnvMode string

const (
	Local       EnvMode = "local"
	Development EnvMode = "development"
	Production  EnvMode = "production"
)

type PostgresNodeType string

const (
	PGMasterNode PostgresNodeType = "master"
	PGSlaveNode  PostgresNodeType = "slave"
)
