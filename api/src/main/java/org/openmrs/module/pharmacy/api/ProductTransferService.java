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

import org.openmrs.Location;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacy.entities.ProductTransfer;
//import org.openmrs.module.pharmacy.models.ProductTransferFluxDTO;
import org.openmrs.module.pharmacy.utils.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
public interface ProductTransferService extends OpenmrsService {
     
	/*
	 * Add service methods here
	 * 
	 */
	@Authorized(value = {PrivilegeConstants.VIEW_TRANSFER})
	List<ProductTransfer> getAllProductTransfers(Location location, Boolean includeVoided);
	@Authorized(value = {PrivilegeConstants.VIEW_TRANSFER})
	List<ProductTransfer> getAllProductTransfers(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate);
	@Authorized(value = {PrivilegeConstants.VIEW_TRANSFER})
	List<ProductTransfer> getAllProductTransfers(Location location);
	@Authorized(value = {PrivilegeConstants.VIEW_TRANSFER})
	List<ProductTransfer> getAllProductTransfers(Boolean includeVoided);
	@Authorized(value = {PrivilegeConstants.VIEW_TRANSFER})
	ProductTransfer getOneProductTransferById(Integer id);
	@Authorized(value = {PrivilegeConstants.SAVE_TRANSFER})
	ProductTransfer saveProductTransfer(ProductTransfer productTransfer);
	@Authorized(value = {PrivilegeConstants.SAVE_TRANSFER})
	ProductTransfer editProductTransfer(ProductTransfer productTransfer);
	@Authorized(value = {PrivilegeConstants.DELETE_TRANSFER})
	void removeProductTransfer(ProductTransfer productTransfer);
	@Authorized(value = {PrivilegeConstants.VIEW_TRANSFER})
	ProductTransfer getOneProductTransferByUuid(String uuid);
	@Authorized(value = {PrivilegeConstants.VIEW_TRANSFER})
	List<Location> getAllClientLocation(Boolean includeVoided);
//	List<ProductOutFluxDTO> getProductTransferFluxDTOs(ProductTransfer productTransfer);
//	ProductTransfer getLastProductTransfer(Location location, ProductProgram productProgram);
//	ProductTransfer getLastProductTransferByDate(Location location, ProductProgram productProgram, Date inventoryDate);
//	List<ProductTransferFluxDTO> getProductTransferFluxDTOs(ProductTransfer productTransfer);
}
