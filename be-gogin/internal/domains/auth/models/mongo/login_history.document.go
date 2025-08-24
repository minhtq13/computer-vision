package mongo

import (
	"go.mongodb.org/mongo-driver/v2/bson"
	"time"
)

type LoginHistoryDocument struct {
	// _id tự sinh, phải có omitempty nếu không -> zero value 000000....
	Id          bson.ObjectID `bson:"_id,omitempty"`
	UserId      int64         `bson:"userId"`
	LoggedInAt  time.Time     `bson:"loggedInAt"`
	IpAddresses string        `bson:"ipAddress"`
}
