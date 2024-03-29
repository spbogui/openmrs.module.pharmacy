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

import org.openmrs.annotation.Authorized;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacy.entities.Product;
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.entities.ProductUnit;
import org.openmrs.module.pharmacy.dto.ProductUploadResumeDTO;
import org.openmrs.module.pharmacy.utils.PrivilegeConstants;
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
	@Authorized(value = {PrivilegeConstants.SAVE_PRODUCT})
	Product saveProduct(Product product);
	@Authorized(value = {PrivilegeConstants.SAVE_PRODUCT})
	Product editProduct(Product product);
	@Authorized(value = {PrivilegeConstants.VIEW_PRODUCT})
	Product getOneProductById(Integer productId);
	@Authorized(value = {PrivilegeConstants.VIEW_PRODUCT})
	Product getOneProductByCode(String code);
	@Authorized(value = {PrivilegeConstants.VIEW_PRODUCT})
	Product getOneProductByUuid(String uuid);
	@Authorized(value = {PrivilegeConstants.VIEW_PRODUCT})
	Product getOneProductByRetailName(String retailName);
	@Authorized(value = {PrivilegeConstants.VIEW_PRODUCT})
	Product getOneProductByWholesaleName(String wholesaleName);
	@Authorized(value = {PrivilegeConstants.VIEW_PRODUCT})
	Product getOneProductByName(String name);
	@Authorized(value = {PrivilegeConstants.VIEW_PRODUCT})
	List<Product> getAllProduct();
	@Authorized(value = {PrivilegeConstants.VIEW_PRODUCT})
	List<Product> getAllProductByRetailUnit(ProductUnit retailUnit);
	@Authorized(value = {PrivilegeConstants.VIEW_PRODUCT})
	List<Product> getAllProductByWholesaleUnit(ProductUnit wholesaleUnit);
	@Authorized(value = {PrivilegeConstants.VIEW_PRODUCT})
	List<Product> searchProductByNameLike(String nameSearch);
	@Authorized(value = {PrivilegeConstants.IMPORT_PRODUCT})
	ProductUploadResumeDTO uploadProducts(MultipartFile file);
	@Authorized(value = {PrivilegeConstants.IMPORT_REGIMEN})
	ProductUploadResumeDTO uploadProductRegimens(MultipartFile file);
	@Authorized(value = {PrivilegeConstants.VIEW_PRODUCT})
	List<Product> getProductWithoutRegimenByProgram(ProductProgram productProgram);
}
