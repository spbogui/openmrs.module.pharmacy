/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.pharmacy.api;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductUnit;
import org.openmrs.module.pharmacy.models.ProductUploadResumeDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(PharmacyService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface ProductService extends OpenmrsService {
     
	/*
	 * Add service methods here
	 * 
	 */

	Product saveProduct(Product product);
	Product editProduct(Product product);
	Product getOneProductById(Integer productId);
	Product getOneProductByCode(String code);
	Product getOneProductByUuid(String uuid);
	Product getOneProductByRetailName(String retailName);
	Product getOneProductByWholesaleName(String wholesaleName);
	Product getOneProductByName(String name);
	List<Product> getAllProduct();
	List<Product> getAllProductByRetailUnit(ProductUnit retailUnit);
	List<Product> getAllProductByWholesaleUnit(ProductUnit wholesaleUnit);
	List<Product> searchProductByNameLike(String nameSearch);
	ProductUploadResumeDTO uploadProducts(MultipartFile file);

}
