package impls

import "be-gogin/internal/domains/common/services"

type ComboBoxService struct{}

func NewComboBoxService() services.IComboBoxService {
	return &ComboBoxService{}
}
