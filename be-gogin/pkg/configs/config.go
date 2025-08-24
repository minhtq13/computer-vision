package configs

// DatabaseConfig AppEnvConfig /**
type DatabaseConfig struct {
	Host        string `mapstructure:"host"`
	Port        int    `mapstructure:"port"`
	User        string `mapstructure:"user"`
	Password    string `mapstructure:"password"`
	Database    string `mapstructure:"database"`
	Schema      string `mapstructure:"schema"`
	SSLRequired bool   `mapstructure:"sslRequired"`
	TimeZone    string `mapstructure:"timeZone"`
}

type PostgresNodeConfig struct {
	DatabaseConfig          `mapstructure:",squash"`
	MaxIdleConnections      int `mapstructure:"maxIdleConnections"`
	MaxOpenConnections      int `mapstructure:"maxOpenConnections"`
	ConnectionMaxLifetimeMs int `mapstructure:"connectionMaxLifetimeMs"`
}
type PostgresConfig struct {
	Master PostgresNodeConfig `mapstructure:"master"`
	Slave  PostgresNodeConfig `mapstructure:"slave"`
}
type MysqlConfig struct {
	Master DatabaseConfig `mapstructure:"master"`
	Slave  DatabaseConfig `mapstructure:"slave"`
}
type RedisConfig struct {
	Host                string `mapstructure:"host"`
	Port                int    `mapstructure:"port"`
	Username            string `mapstructure:"username"`
	Password            string `mapstructure:"password"`
	DB                  int    `mapstructure:"db"`
	ConnectionTimeoutMs int64  `mapstructure:"connectionTimeoutMs"`
	PoolSize            int    `mapstructure:"poolSize"`
}

type MongoConfig struct {
	Host     string `mapstructure:"host"`
	Port     int    `mapstructure:"port"`
	Username string `mapstructure:"username"`
	Password string `mapstructure:"password"`
	Database string `mapstructure:"database"`
}

type JwtConfig struct {
	SecretKey             string `mapstructure:"secretKey"`
	AccessTokenExpiredMs  int64  `mapstructure:"accessTokenExpiredMs"`
	RefreshTokenExpiredMs int64  `mapstructure:"refreshTokenExpiredMs"`
	SignatureAlgorithm    string `mapstructure:"signatureAlgorithm"`
}

type LoggingConfig struct {
	Level      string `mapstructure:"level"`
	FileName   string `mapstructure:"fileName"`
	MaxSizeMBs int    `mapstructure:"maxSizeMBs"`
	MaxBackups int    `mapstructure:"maxBackups"`
	MaxAgeDays int    `mapstructure:"maxAgeDays"`
	Compress   bool   `mapstructure:"compress"`
}

type FirebaseConfig struct {
	ProjectID string `mapstructure:"projectId"`
	AppName   string `mapstructure:"appName"`
	KeyFile   string `mapstructure:"keyFile"`
}

type KafkaConfig struct {
	Brokers  []string `mapstructure:"brokers"`
	Consumer struct {
		GroupId         string `mapstructure:"groupId"`
		AutoOffsetReset string `mapstructure:"autoOffsetReset"`
		MinBytes        int    `mapstructure:"minBytes"`
		MaxBytes        int    `mapstructure:"maxBytes"`
		MaxWaitInSecond int    `mapstructure:"maxWaitInSecond"`
		GroupInstanceId string `mapstructure:"groupInstanceId"`
	} `mapstructure:"consumer"`
	Producer struct {
		Retries      int    `mapstructure:"retries"`
		BatchSize    int    `mapstructure:"batchSize"`
		DefaultTopic string `mapstructure:"defaultTopic"`
		ClientID     string `mapstructure:"clientId"`
	} `mapstructure:"producer"`
	Options struct {
		Enabled bool `mapstructure:"enabled"`
	} `mapstructure:"options"`
}

type ApiAuthConfig struct {
	Apis []string `mapstructure:"apis"`
}

type AppEnvConfig struct {
	Server struct {
		Port           int      `mapstructure:"port"`
		Mode           string   `mapstructure:"mode"`
		AllowedOrigins []string `mapstructure:"allowedOrigins"`
	} `mapstructure:"server"`
	Databases struct {
		Postgres PostgresConfig `mapstructure:"postgres"`
		MongoDB  MongoConfig    `mapstructure:"mongodb"`
		//Mysql    MysqlConfig   `mapstructure:"mysql"`
	} `mapstructure:"databases"`
	Redis    RedisConfig    `mapstructure:"redis"`
	Kafka    KafkaConfig    `mapstructure:"kafka"`
	Jwt      JwtConfig      `mapstructure:"jwt"`
	Logging  LoggingConfig  `mapstructure:"logging"`
	Firebase FirebaseConfig `mapstructure:"firebase"`
}
