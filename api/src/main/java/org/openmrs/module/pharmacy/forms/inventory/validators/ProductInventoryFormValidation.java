package org.openmrs.module.pharmacy.forms.inventory.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacy.entities.ProductInventory;
import org.openmrs.module.pharmacy.enumerations.InventoryType;
import org.openmrs.module.pharmacy.forms.inventory.ProductInventoryForm;
import org.openmrs.module.pharmacy.forms.ProductOperationForm;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.openmrs.module.pharmacy.forms.ProductOperationFormValidation;
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

            if (form.getOperationNumber() != null && !form.getOperationNumber().isEmpty()) {
                if (form.getInventoryType().equals(InventoryType.TOTAL)) {
                    ProductInventory inventory = inventoryService().getOneProductInventoryByOperationNumber(OperationUtils.getUserLocation(),
                            programService().getOneProductProgramById(form.getProductProgramId()), form.getOperationNumber(), InventoryType.TOTAL);

                    if (inventory != null) {
//                    System.out.println("-------------------------------> inventory operationNumber : " + inventory.toString());
                        if (form.getProductOperationId() == null ||
                                (form.getProductOperationId() != null && !form.getProductOperationId().equals(inventory.getProductOperationId()))) {
                            errors.rejectValue("inventoryType", null, "Un inventaire mensuel ayant ce numéro existe déjà !");
                        }
                    }
                }
            }

            if (form.getOperationDate() != null) {
                ProductInventory inventory = inventoryService().getProductInventoryByDate(OperationUtils.getUserLocation(),
                        programService().getOneProductProgramById(form.getProductProgramId()), form.getOperationDate());
                if (inventory != null) {
//                    System.out.println("-------------------------------> inventory operationDate : " + inventory.toString());
                    if (form.getProductOperationId() == null ||
                            (form.getProductOperationId() != null && !inventory.getProductOperationId().equals(form.getProductOperationId())))
                    errors.rejectValue("operationDate", null, "Un inventaire à cette date existe déjà !");
                }
            }
        }

    }

    @Override
    protected void lastInventoryCheck(ProductOperationForm form, Errors errors) {

    }
}
