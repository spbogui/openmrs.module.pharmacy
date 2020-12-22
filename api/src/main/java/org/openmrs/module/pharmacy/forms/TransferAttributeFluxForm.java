package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.ProductAttributeFluxService;
import org.openmrs.module.pharmacy.api.ProductAttributeService;
import org.openmrs.module.pharmacy.api.ProductReceptionService;

public class TransferAttributeFluxForm extends ProductAttributeFluxForm {
    private Integer selectedProductStockId;

//    public InventoryAttributeFluxForm() {
//    }
//
//
//    public Integer getQuantityToDeliver() {
//        return quantityToDeliver;
//    }
//
//    public void setQuantityToDeliver(Integer quantityToDeliver) {
//        this.quantityToDeliver = quantityToDeliver;
//    }
//
//    public void setProductAttributeFlux(ProductAttributeFlux productAttributeFlux, ProductReception productReception) {
//        super.setProductAttributeFlux(productAttributeFlux, productReception);
//        ProductAttributeOtherFlux otherFlux = fluxService().getOneProductAttributeOtherFluxByAttributeAndOperation(productAttributeFlux.getProductAttribute(), productReception);
//        setQuantityToDeliver(otherFlux.getQuantity());
//    }

//    public ProductAttributeOtherFlux getProductAttributeOtherFlux() {
//        ProductAttributeOtherFlux productAttributeOtherFlux = fluxService().getOneProductAttributeOtherFluxByAttributeAndOperation(
//                attributeService().getOneProductAttributeByBatchNumber(getBatchNumber()),
//                receptionService().getOneProductReceptionById(getProductOperationId())
//        );
//        if (productAttributeOtherFlux != null){
//            productAttributeOtherFlux.setQuantity(getQuantityToDeliver());
//        } else {
//            productAttributeOtherFlux = new ProductAttributeOtherFlux();
//            productAttributeOtherFlux.setQuantity(getQuantityToDeliver());
//            productAttributeOtherFlux.setLabel("Quantitié livrée");
//            productAttributeOtherFlux.setLocation(Context.getLocationService().getDefaultLocation());
//            productAttributeOtherFlux.setProductAttribute(getProductAttribute());
//            productAttributeOtherFlux.setProductOperation(receptionService().getOneProductReceptionById(getProductOperationId()));
//        }
//
//        return productAttributeOtherFlux;
//    }

    public Integer getSelectedProductStockId() {
        return selectedProductStockId;
    }

    public void setSelectedProductStockId(Integer selectedProductStockId) {
        this.selectedProductStockId = selectedProductStockId;
    }

    private ProductReceptionService receptionService() {
        return Context.getService(ProductReceptionService.class);
    }

    private ProductAttributeFluxService fluxService() {
        return Context.getService(ProductAttributeFluxService.class);
    }

    private ProductAttributeService attributeService() {
        return Context.getService(ProductAttributeService.class);
    }
}
