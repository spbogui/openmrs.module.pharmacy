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
import org.openmrs.module.pharmacy.entities.ProductUnit;
import org.openmrs.module.pharmacy.utils.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

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
public interface ProductUnitService extends OpenmrsService {
	@Authorized(value = {PrivilegeConstants.SAVE_PRICE})
    ProductUnit saveProductUnit(ProductUnit productUnit);
	@Authorized(value = {PrivilegeConstants.SAVE_PRICE})
	ProductUnit editProductUnit(ProductUnit productUnit);
	@Authorized(value = {PrivilegeConstants.DELETE_PRICE})
	void removeProductUnit(ProductUnit productUnit);
	@Authorized(value = {PrivilegeConstants.VIEW_PRICE})
	ProductUnit getOneProductUnitById(Integer productUnitId);
	@Authorized(value = {PrivilegeConstants.VIEW_PRICE})
	ProductUnit getOneProductUnitByUuid(String uuid);
	@Authorized(value = {PrivilegeConstants.VIEW_PRICE})
	ProductUnit getOneProductUnitByName(String name);
	@Authorized(value = {PrivilegeConstants.VIEW_PRICE})
	List<ProductUnit> getAllProductUnit();

}
