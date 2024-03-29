package org.openmrs.module.pharmacy.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.Location;
import org.openmrs.module.pharmacy.AbstractPharmacyData;

import javax.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "ProductAttributeStock")
@Table(name = "pharmacy_product_attribute_stock")
public class ProductAttributeStock extends AbstractPharmacyData {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_attribute_stock_id")
    private Integer productAttributeStockId;

    @ManyToOne
    @JoinColumn(name = "product_attribute_id", nullable = false)
    private ProductAttribute productAttribute;

    @Column(name = "quantity_in_stock", nullable = false)
    private Integer quantityInStock;

    @ManyToOne
    @JoinColumn(name = "operation_id")
    private ProductOperation operation;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    public ProductAttributeStock() {
    }

    public Integer getProductAttributeStockId() {
        return productAttributeStockId;
    }

    public void setProductAttributeStockId(Integer productAttributeStockId) {
        this.productAttributeStockId = productAttributeStockId;
    }

    public ProductAttribute getProductAttribute() {
        return productAttribute;
    }

    public void setProductAttribute(ProductAttribute productAttribute) {
        this.productAttribute = productAttribute;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public ProductOperation getOperation() {
        return operation;
    }

    public void setOperation(ProductOperation operation) {
        this.operation = operation;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getProductInStock() {
        return productAttribute.getProduct().getRetailNameWithCode() + " [" + productAttribute.getBatchNumber() + "]";
    }
}
