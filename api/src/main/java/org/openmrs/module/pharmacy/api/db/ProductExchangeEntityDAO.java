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
package org.openmrs.module.pharmacy.api.db;

import org.openmrs.Location;
import org.openmrs.module.pharmacy.entities.ProductExchangeEntity;

import java.util.List;

/**
 *  Database methods for {@link org.openmrs.module.pharmacy.api.ProductExchangeEntityService}.
 */
public interface ProductExchangeEntityDAO {

	List<ProductExchangeEntity> getAllProductExchange();
	List<ProductExchangeEntity> getAllProductExchange(Location location);
	ProductExchangeEntity saveProductExchange(ProductExchangeEntity productExchangeEntity);
	ProductExchangeEntity editProductExchange(ProductExchangeEntity productExchangeEntity);
	void removeProductExchange(ProductExchangeEntity productExchangeEntity);
	ProductExchangeEntity getOneProductExchangeById(Integer productExchangeId);
	ProductExchangeEntity getOneProductExchangeByUuid(String uuid);
	ProductExchangeEntity getOneProductExchangeByName(String name);

}
