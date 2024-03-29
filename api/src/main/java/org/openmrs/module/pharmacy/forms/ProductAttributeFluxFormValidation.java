package org.openmrs.module.pharmacy.forms;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.entities.ProductAttribute;
import org.openmrs.module.pharmacy.entities.ProductAttributeFlux;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductAttributeFluxService;
import org.openmrs.module.pharmacy.api.ProductAttributeService;
import org.openmrs.module.pharmacy.forms.ProductAttributeFluxForm;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Date;

@Handler(supports = {ProductAttributeFluxForm.class}, order = 50)
public class ProductAttributeFluxFormValidation implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ProductAttributeFluxForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductAttributeFluxForm form = (ProductAttributeFluxForm) o;

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "productId", null, "Le produit est requis");
            ValidationUtils.rejectIfEmpty(errors, "batchNumber", null, "Le numéro de lot est requis");
//            ValidationUtils.rejectIfEmpty(errors, "receptionQuantity", null, "La quantié livrée est requise");
            ValidationUtils.rejectIfEmpty(errors, "quantity", null, "la quantité reçue est requise");
            ValidationUtils.rejectIfEmpty(errors, "expiryDate", null, "La date de péremption est requise");

            if (form.getBatchNumber() != null) {
                ProductAttribute productAttribute = attributeService().getOneProductAttributeByBatchNumber(form.getBatchNumber(), OperationUtils.getUserLocation());
                if (productAttribute != null) {
                    if (!productAttribute.getProduct().getProductId().equals(form.getProductId())) {
                        if(form.getProductAttributeFluxId() == null ) {
                            errors.rejectValue("batchNumber", null, "Le produit <<" +
                                    productAttribute.getProduct().getRetailName() + ">> a déjà été ajouté avec ce numéro de lot !");
                        }
                    }
                }

                ProductAttributeFlux productAttributeFlux =
                        fluxService().getOneProductAttributeFluxByAttributeAndOperation(productAttribute,
                                service().getOneProductOperationById(form.getProductOperationId()));
                if (productAttributeFlux != null) {
                    if (!productAttributeFlux.getProductAttributeFluxId().equals(form.getProductAttributeFluxId())) {
                        errors.rejectValue("batchNumber", null, "Cette ligne du produit déjà été ajoutée !");
                    }
                }
            }

            if (form.getExpiryDate() != null && form.getExpiryDate().before(new Date())) {
                errors.rejectValue("expiryDate", null, "La date de péremption n'est pas correcte");
            }

//            if (form.getReceivedQuantity() != null && form.getReceivedQuantity() == 0) {
//                errors.rejectValue("receivedQuantity", "La quantité reçue ne peut pas être égale à 0");
//            }

//            if (form.getQuantityToDeliver() != null && form.getQuantityToDeliver() == 0) {
//                errors.rejectValue("receivedQuantity", null, "La quantité livrée ne peut pas être égale à 0");
//            }
        }


    }

    protected ProductAttributeFluxService fluxService() {
        return Context.getService(ProductAttributeFluxService.class);
    }

    protected ProductAttributeService attributeService() {
        return Context.getService(ProductAttributeService.class);
    }

    protected PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }
}
