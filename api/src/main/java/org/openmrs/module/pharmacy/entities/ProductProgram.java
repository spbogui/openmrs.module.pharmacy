package org.openmrs.module.pharmacy.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.module.pharmacy.AbstractPharmacyObject;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "ProductProgram")
@Table(name = "pharmacy_product_program")
public class ProductProgram extends AbstractPharmacyObject {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_program_id")
    private Integer productProgramId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "program_number", nullable = false)
    private Integer programNumber;

    @ManyToMany(mappedBy="productPrograms", fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<Product>();

    @Override
    public Integer getId() {
        return getProductProgramId();
    }

    @Override
    public void setId(Integer integer) {
        setProductProgramId(integer);
    }

    public Integer getProductProgramId() {
        return productProgramId;
    }

    public void setProductProgramId(Integer productProgramId) {
        this.productProgramId = productProgramId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getProgramNumber() {
        return programNumber;
    }

    public void setProgramNumber(Integer programNumber) {
        this.programNumber = programNumber;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
