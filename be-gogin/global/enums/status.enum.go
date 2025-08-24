package enums

type StatusEnum int

const (
	Disabled StatusEnum = 0
	Enabled  StatusEnum = 1
)

type DeletedFlag int

const (
	Deleted    DeletedFlag = 0
	NotDeleted DeletedFlag = 1
)
