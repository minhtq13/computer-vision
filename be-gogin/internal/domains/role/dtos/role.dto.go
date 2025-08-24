package dtos

type RoleDTO struct {
	Id            int64  `json:"roleId"`
	Code          string `json:"roleCode"`
	DisplayedName string `json:"displayedName"`
}
