package routers

type RootRouterGroup struct {
	PrivateRouterGroup *PrivateRouterGroup
	PublicRouterGroup  *PublicRouterGroup
}

func NewRootRouterGroup(privateGroup *PrivateRouterGroup, publicGroup *PublicRouterGroup) *RootRouterGroup {
	return &RootRouterGroup{
		PrivateRouterGroup: privateGroup,
		PublicRouterGroup:  publicGroup,
	}
}
