package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacy.forms.ProductInventoryForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Handler(supports = {ProductInventoryForm.class}, order = 50)
public class ProductInventoryFormValidation extends ProductOperationFormValidation {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ProductInventoryForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductInventoryForm form = (ProductInventoryForm) o;

        super.validate(form, errors);

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "inventoryType", null, "Ce champ est requis");
        }

    }
}
