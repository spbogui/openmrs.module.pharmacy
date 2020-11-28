package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductReception;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductSupplierService;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.ReceptionQuantityMode;

public class ProductReceptionForm extends ProductOperationForm {
    private Integer productSupplierId;
    private ReceptionQuantityMode receptionQuantityMode;

    public ProductReceptionForm() {
        super();
        setIncidence(Incidence.POSITIVE);
    }

    public Integer getProductSupplierId() {
        return productSupplierId;
    }

    public void setProductSupplierId(Integer productSupplierId) {
        this.productSupplierId = productSupplierId;
    }

    public ReceptionQuantityMode getReceptionQuantityMode() {
        return receptionQuantityMode;
    }

    public void setReceptionQuantityMode(ReceptionQuantityMode receptionQuantityMode) {
        this.receptionQuantityMode = receptionQuantityMode;
    }

    public void setProductReception(ProductReception productReception) {
        super.setProductOperation(productReception);
        setProductSupplierId(productReception.getProductSupplier().getProductSupplierId());
        setReceptionQuantityMode(productReception.getReceptionQuantityMode());
    }

    public ProductReception getProductReception() {
        ProductReception productReception = (ProductReception) super.getProductOperation(new ProductReception());
        productReception.setProductSupplier(Context.getService(ProductSupplierService.class).getOneProductSupplierById(getProductSupplierId()));
        productReception.setReceptionQuantityMode(getReceptionQuantityMode());
        return productReception;
    }
}
