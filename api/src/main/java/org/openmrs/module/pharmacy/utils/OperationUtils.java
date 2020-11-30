package org.openmrs.module.pharmacy.utils;

import org.hibernate.HibernateException;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductAttributeFlux;
import org.openmrs.module.pharmacy.ProductAttributeStock;
import org.openmrs.module.pharmacy.ProductOperation;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductAttributeFluxService;
import org.openmrs.module.pharmacy.api.ProductAttributeStockService;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;

import java.util.List;

public class OperationUtils {
    public static Boolean validateOperation(ProductOperation operation) throws HibernateException {
        operation.setOperationStatus(OperationStatus.VALIDATED);
        service().saveProductOperation(operation);

        for (ProductAttributeFlux flux : operation.getProductAttributeFluxes()) {
            flux.setStatus(OperationStatus.VALIDATED);
            fluxService().saveProductAttributeFlux(flux);

            if (!operation.getIncidence().equals(Incidence.NONE)) {
                //List<ProductAttributeStock> productAttributeStocks = stockService().getAllProductAttributeStockByAttribute(flux.getProductAttribute(), false);
                ProductAttributeStock attributeStock = stockService().getOneProductAttributeStockByAttribute(flux.getProductAttribute(), getUserLocation(), false);
                if (attributeStock != null) {
                    Integer quantity = operation.getIncidence().equals(Incidence.POSITIVE) ?
                            attributeStock.getQuantityInStock() + flux.getQuantity() :
                            (operation.getIncidence().equals(Incidence.NEGATIVE) ? attributeStock.getQuantityInStock() - flux.getQuantity() : flux.getQuantity());
                    attributeStock.setQuantityInStock(quantity);
                } else {
                    attributeStock = new ProductAttributeStock();
                    attributeStock.setQuantityInStock(flux.getQuantity());
                    attributeStock.setLocation(getUserLocation());
                    attributeStock.setProductAttribute(flux.getProductAttribute());
                }
                //attributeStock.setDateCreated(new Date());
                stockService().saveProductAttributeStock(attributeStock);
            }
        }
        return true;
    }

    private static PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }

    private static ProductAttributeFluxService fluxService() {
        return Context.getService(ProductAttributeFluxService.class);
    }

    private static ProductAttributeStockService stockService() {
        return Context.getService(ProductAttributeStockService.class);
    }

    public static Location getUserLocation() {
        if (Context.getUserContext().getLocation() != null) {
            return Context.getUserContext().getLocation();
        }
        return Context.getLocationService().getDefaultLocation();
    }
}