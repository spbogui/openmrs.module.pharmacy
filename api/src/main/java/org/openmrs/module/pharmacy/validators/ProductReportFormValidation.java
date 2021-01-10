package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductInventory;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.ProductReport;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.forms.ProductReportForm;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Handler(supports = {ProductReportForm.class}, order = 50)
public class ProductReportFormValidation extends ProductOperationFormValidation  {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ProductReportForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductReportForm form = (ProductReportForm) o;

        super.validate(form, errors);

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
//            ValidationUtils.rejectIfEmpty(errors, "productSupplierId", null, "Ce champ est requis");
//            ValidationUtils.rejectIfEmpty(errors, "receptionQuantityMode", null, "Ce champ est requis");
//            ValidationUtils.rejectIfEmpty(errors, "operationNumber", null, "Ce champ est requis");

//            if (form.getOperationNumber() != null && !form.getOperationNumber().isEmpty()) {
//                ProductReport reception = (ProductReport) service().getOneProductOperationByOperationNumber(form.getOperationNumber(), form.getIncidence());
//                if (reception != null) {
//                    if (form.getProductSupplierId() != null) {
//                        if (!reception.getProductOperationId().equals(form.getProductOperationId())) {
//                            errors.rejectValue("operationNumber", null, "Une réception avec ce BL existe déjà ");
//                        }
//                    }
//                }
//            }

//            if (form.getOperationDate() != null) {
//                ProductProgram productProgram = programService().getOneProductProgramById(form.getProductProgramId());
//                if (productProgram != null) {
//                    ProductInventory productInventory = inventoryService().getLastProductInventory(
//                            OperationUtils.getUserLocation(), productProgram);
//                    if (productInventory == null) {
//                        createAlert("Vous devez avant toute opération réaliser le premier inventaire complet de votre centre !");
//                        errors.rejectValue("productProgramId", null, "Premier Inventaire complet non réalisé");
//                    }
//                }
//            }
        }
    }

    PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }
}