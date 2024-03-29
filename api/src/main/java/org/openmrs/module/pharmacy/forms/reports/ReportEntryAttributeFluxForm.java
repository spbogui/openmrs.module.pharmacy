package org.openmrs.module.pharmacy.forms.reports;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.entities.Product;
import org.openmrs.module.pharmacy.entities.ProductAttributeOtherFlux;
import org.openmrs.module.pharmacy.entities.ProductReport;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.utils.OperationUtils;

import java.util.ArrayList;
import java.util.List;

public class ReportEntryAttributeFluxForm {
    private Integer productId;
    private Integer locationId;
    private Integer productOperationId;
    private Integer initialQuantity;
    private Integer receivedQuantity;
    private Integer distributedQuantity;
    private Integer lostQuantity;
    private Integer adjustmentQuantity;
    private Integer quantityInStock;
    private Integer numDaysOfRupture;
    private Integer quantityDistributed2monthAgo;
    private Integer quantityDistributed1monthAgo;

    public ReportEntryAttributeFluxForm() {
        locationId = OperationUtils.getUserLocation().getLocationId();
    }

    public Integer getProductId() {
        return productId;
    }

    public ReportEntryAttributeFluxForm setProductId(Integer productId) {
        this.productId = productId;
        return this;
    }

    public Integer getProductOperationId() {
        return productOperationId;
    }

    public ReportEntryAttributeFluxForm setProductOperationId(Integer productOperationId) {
        this.productOperationId = productOperationId;
        return this;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(Integer initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public Integer getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(Integer receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    public Integer getDistributedQuantity() {
        return distributedQuantity;
    }

    public void setDistributedQuantity(Integer distributedQuantity) {
        this.distributedQuantity = distributedQuantity;
    }

    public Integer getLostQuantity() {
        return lostQuantity;
    }

    public void setLostQuantity(Integer lostQuantity) {
        this.lostQuantity = lostQuantity;
    }

    public Integer getAdjustmentQuantity() {
        return adjustmentQuantity;
    }

    public void setAdjustmentQuantity(Integer adjustmentQuantity) {
        this.adjustmentQuantity = adjustmentQuantity;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Integer getNumDaysOfRupture() {
        return numDaysOfRupture;
    }

    public void setNumDaysOfRupture(Integer numDaysOfRupture) {
        this.numDaysOfRupture = numDaysOfRupture;
    }

    public Integer getQuantityDistributed2monthAgo() {
        return quantityDistributed2monthAgo;
    }

    public void setQuantityDistributed2monthAgo(Integer quantityDistributed2monthAgo) {
        this.quantityDistributed2monthAgo = quantityDistributed2monthAgo;
    }

    public Integer getQuantityDistributed1monthAgo() {
        return quantityDistributed1monthAgo;
    }

    public void setQuantityDistributed1monthAgo(Integer quantityDistributed1monthAgo) {
        this.quantityDistributed1monthAgo = quantityDistributed1monthAgo;
    }

    public List<ProductAttributeOtherFlux> getAllOtherFluxes() {
        List<ProductAttributeOtherFlux> otherFluxes = new ArrayList<>();
        Product product = productService().getOneProductById(getProductId());
        if (product != null) {

            otherFluxes.add(createProductAttributeOtherFlux(product, getInitialQuantity() == null ? 0.0 : getInitialQuantity().doubleValue(), "SI"));
            otherFluxes.add(createProductAttributeOtherFlux(product, getReceivedQuantity().doubleValue(), "QR"));
            otherFluxes.add(createProductAttributeOtherFlux(product, getDistributedQuantity().doubleValue(), "QD"));
            otherFluxes.add(createProductAttributeOtherFlux(product, getLostQuantity().doubleValue(), "QL"));
            otherFluxes.add(createProductAttributeOtherFlux(product, getAdjustmentQuantity().doubleValue(), "QA"));
            otherFluxes.add(createProductAttributeOtherFlux(product, getQuantityInStock().doubleValue(), "SDU"));
            otherFluxes.add(createProductAttributeOtherFlux(product, getNumDaysOfRupture().doubleValue(), "NDR"));
            otherFluxes.add(createProductAttributeOtherFlux(product, getQuantityDistributed1monthAgo() == null ? 0.0 : getQuantityDistributed1monthAgo().doubleValue(), "DM1"));
            otherFluxes.add(createProductAttributeOtherFlux(product, getQuantityDistributed2monthAgo() == null ? 0.0 : getQuantityDistributed2monthAgo().doubleValue(), "DM2"));
        }

        return otherFluxes;
    }

    private ProductAttributeOtherFlux createProductAttributeOtherFlux(Product product, Double quantity, String label) {
        ProductReport report = reportService().getOneProductReportById(getProductOperationId()).getChildLocationReport();
        ProductAttributeOtherFlux productAttributeOtherFlux = fluxService().getOneProductAttributeOtherFluxByProductAndOperationAndLabel(
                product,
                report,
                label,
                report.getLocation()
        );
        if (productAttributeOtherFlux != null) {
            productAttributeOtherFlux.setQuantity(quantity);
        } else {
            productAttributeOtherFlux = OperationUtils.getProductAttributeOtherFlux(product, quantity, label, report, reportService());
            productAttributeOtherFlux.setProductOperation(reportService().getOneProductReportById(getProductOperationId()).getChildLocationReport());
        }
        return  productAttributeOtherFlux;
    }

    public void setProductAttributeOtherFluxes(Integer productId) {
        setProductId(productId);
        Product product = productService().getOneProductById(getProductId());
        if (product != null) {
            setInitialQuantity(getProductAttributeOtherFlux(product, "SI"));
            setReceivedQuantity(getProductAttributeOtherFlux(product, "QR"));
            setDistributedQuantity(getProductAttributeOtherFlux(product, "QD"));
            setLostQuantity(getProductAttributeOtherFlux(product, "QL"));
            setAdjustmentQuantity(getProductAttributeOtherFlux(product, "QA"));
            setQuantityInStock(getProductAttributeOtherFlux(product, "SDU"));
            setNumDaysOfRupture(getProductAttributeOtherFlux(product, "NDR"));
            setQuantityDistributed1monthAgo(getProductAttributeOtherFlux(product, "DM1"));
            setQuantityDistributed2monthAgo(getProductAttributeOtherFlux(product, "DM2"));
        }
    }

    private Integer getProductAttributeOtherFlux(Product product, String label) {
        ProductReport report = reportService().getOneProductReportById(getProductOperationId()).getChildLocationReport();
        return fluxService().getOneProductAttributeOtherFluxByProductAndOperationAndLabel(
                product,
                report,
                label,
                report.getLocation()
        ).getQuantity().intValue();
    }

    private ProductReportService reportService() {
        return Context.getService(ProductReportService.class);
    }

    private ProductAttributeFluxService fluxService() {
        return Context.getService(ProductAttributeFluxService.class);
    }

    private ProductService productService() {
        return Context.getService(ProductService.class);
    }

    private ProductInventoryService inventoryService() {
        return Context.getService(ProductInventoryService.class);
    }

    private ProductAttributeStockService stockService() {
        return Context.getService(ProductAttributeStockService.class);
    }
}
