package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacy.forms.ProductBackSupplierForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Handler(supports = {ProductBackSupplierForm.class}, order = 50)
public class ProductBackSupplierFormValidation extends ProductOperationFormValidation {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ProductBackSupplierForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductBackSupplierForm form = (ProductBackSupplierForm) o;

        super.validate(form, errors);

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "exchangeLocationId", null, "Ce champ est requis");
        }
    }
}
