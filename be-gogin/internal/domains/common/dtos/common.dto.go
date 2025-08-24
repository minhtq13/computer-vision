package dtos

type CommonIdName struct {
	Id   int64  `json:"id"`
	Name string `json:"name"`
}

type CommonIdNameCode struct {
	Id   int64  `json:"id"`
	Name string `json:"name"`
	Code string `json:"code"`
}
