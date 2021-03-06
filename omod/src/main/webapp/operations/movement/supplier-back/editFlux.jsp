<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ page import="org.openmrs.module.pharmacy.enumerations.OperationStatus" %>

<%@ include file="../../../template/operationHeader.jsp"%>
<openmrs:require privilege="Save Edit Product Back Supplier" otherwise="/login.htm" redirect="/module/pharmacy/operations/movement/supplier-back/editFlux.form" />
<script>
    if (jQuery) {
        jQuery(document).ready(function () {

        });

    }

</script>
<div class="container-fluid mt-2">
    <div class="row mb-2">
        <div class="col-6 text-uppercase font-italic text-secondary">
            <div class="h6"><i class="fa fa-pen-square"></i> ${subTitle}</div>
        </div>
        <div class="col-6 text-right">
            <c:if test="${productBackSupplier.operationStatus != 'VALIDATED' &&
                      productBackSupplier.operationStatus != 'DISABLED'}">

                <c:if test="${productBackSupplier.operationStatus == 'NOT_COMPLETED' && fct:length(productAttributeFluxes) != 0}">
                    <c:url value="/module/pharmacy/operations/movement/back/complete.form" var="completeUrl">
                        <c:param name="backSupplierId" value="${productBackSupplier.productOperationId}"/>
                    </c:url>
                    <button class="btn btn-success btn-sm mr-2" onclick="window.location='${completeUrl}'">
                        <i class="fa fa-save"></i> Terminer
                    </button>
                </c:if>
                <c:if test="${productBackSupplier.operationStatus != 'NOT_COMPLETED'}">
                    <c:url value="/module/pharmacy/operations/movement/back/incomplete.form" var="incompleteUrl">
                        <c:param name="backSupplierId" value="${productBackSupplier.productOperationId}"/>
                    </c:url>
                    <button class="btn btn-primary btn-sm mr-2" onclick="window.location='${incompleteUrl}'">
                        <i class="fa fa-pen"></i> Editer l'inventaire
                    </button>
                    <openmrs:hasPrivilege privilege="Validate Product Back Supplier">
                        <c:url value="/module/pharmacy/operations/movement/back/validate.form" var="validationUrl">
                            <c:param name="backSupplierId" value="${productBackSupplier.productOperationId}"/>
                        </c:url>
                        <button class="btn btn-success btn-sm mr-2" onclick="window.location='${validationUrl}'">
                            <i class="fa fa-pen"></i> Valider
                        </button>
                    </openmrs:hasPrivilege>
                </c:if>
            </c:if>
            <c:if test="${productBackSupplier.operationStatus == 'NOT_COMPLETED'}">
                <c:url value="/module/pharmacy/operations/movement/supplier-back/edit.form" var="editUrl">
                    <c:param name="id" value="${productBackSupplier.productOperationId}"/>
                </c:url>
                <button class="btn btn-primary btn-sm" onclick="window.location='${editUrl}'" title="Voir la liste">
                    <i class="fa fa-edit"></i> Editer l'ent&ecirc;te
                </button>
            </c:if>
            <c:url value="/module/pharmacy/operations/movement/supplier-back/list.form" var="url"/>
            <button class="btn btn-primary btn-sm" onclick="window.location='${url}'" title="Voir la liste">
                <i class="fa fa-list"></i> Voir la liste
            </button>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <div class="col-12">
            <table class="bg-light table table-borderless table-light border">
                <thead class="thead-light">
                <tr>
                    <td>Programme </td>
                    <td class="font-weight-bold text-info">${productBackSupplier.productProgram.name}</td>
                    <td>Site / PPS</td>
                    <td class="font-weight-bold text-info">
                        ${productBackSupplier.exchangeLocation.name}
                    </td>
                </tr>
                <tr>
                    <td>Date de r&eacute;ception du retour</td>
                    <td class="font-weight-bold text-info">
                        <fmt:formatDate value="${productBackSupplier.operationDate}" pattern="dd/MM/yyyy" type="DATE"/>
                    </td>
                    <td>BL</td>
                    <td class="font-weight-bold text-info">${productBackSupplier.operationNumber}</td>
                </tr>
                <tr>
                    <td>Observation</td>
                    <td class="font-weight-bold text-info">${productBackSupplier.observation}</td>
                    <td colspan="2"></td>
                </tr>
                </thead>
            </table>

            <c:if test="${productBackSupplier.operationStatus == 'NOT_COMPLETED'}">
                <form:form modelAttribute="backSupplierAttributeFluxForm" method="post" action="" id="form">
                    <form:hidden path="productAttributeFluxId"/>
                    <form:hidden path="productOperationId"/>
                    <form:hidden path="locationId"/>
                    <div>
                        <form:errors path="productId" cssClass="error"/>
                        <form:errors path="batchNumber" cssClass="error"/>
                        <form:errors path="expiryDate" cssClass="error"/>
                        <form:errors path="quantity" cssClass="error"/>
                    </div>
                    <table class="table table-condensed table-striped table-sm table-bordered">
                        <thead class="thead-light">
                        <tr class="bg-belize-hole">
                            <th colspan="3" style="width: 250px">Produit <span class="required">*</span></th>
                            <th style="width: 200px">Num&eacute;ro <br>de lot <span class="required">*</span></th>
                            <th style="width: 150px">Date de <br>peremption <span class="required">*</span></th>
                            <th style="width: 60px">Quantit&eacute; <span class="required">*</span></th>
                            <th style="width: 250px">Observation</th>
                            <th style="width: 50px"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${productBackSupplier.operationStatus == 'NOT_COMPLETED'}">
                            <tr>
                                <td colspan="3">
                                    <form:select path="productId" cssClass="form-control s2" >
                                        <form:option value="" label=""/>
                                        <form:options items="${products}" itemValue="productId" itemLabel="retailNameWithCode" />
                                    </form:select>
                                </td>
                                <td>
                                    <c:if test="${empty backSupplierAttributeFluxForm.productAttributeFluxId}">
                                        <form:input path="batchNumber" cssClass="form-control form-control-sm"  />
                                    </c:if>
                                    <c:if test="${not empty backSupplierAttributeFluxForm.productAttributeFluxId}">
                                        <form:input path="batchNumber" cssClass="form-control form-control-sm" readonly="true"  />
                                    </c:if>
                                </td>
                                <td>
                                    <c:if test="${empty backSupplierAttributeFluxForm.productAttributeFluxId}">
                                        <form:input path="expiryDate" cssClass="form-control form-control-sm picker" />
                                    </c:if>
                                    <c:if test="${not empty backSupplierAttributeFluxForm.productAttributeFluxId}">
                                        <form:input path="expiryDate" cssClass="form-control form-control-sm picker" readonly="true"  />
                                    </c:if>
                                </td>
                                <td>
                                    <form:input path="quantity" cssClass="form-control form-control-sm" />
                                </td>
                                <td>
                                    <form:input path="observation" cssClass="form-control form-control-sm" />
                                </td>
                                <td>
                                    <button class="btn btn-success">
                                        <c:if test="${not empty backSupplierAttributeFluxForm.productAttributeFluxId}">
                                            <i class="fa fa-edit"></i>
                                        </c:if>
                                        <c:if test="${empty backSupplierAttributeFluxForm.productAttributeFluxId}">
                                            <i class="fa fa-plus"></i>
                                        </c:if>
                                    </button>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="8"></td>
                            </tr>
                            <c:forEach var="productFlux" items="${productAttributeFluxes}">
                                <tr>
                                    <td>${productFlux.productAttribute.product.code}</td>
                                    <td>${productFlux.productAttribute.product.retailName}</td>
                                    <td>${productFlux.productAttribute.product.productRetailUnit.name}</td>
                                    <td class="text-center">${productFlux.productAttribute.batchNumber}</td>
                                    <td class="text-center">
                                        <fmt:formatDate value="${productFlux.productAttribute.expiryDate}" pattern="dd/MM/yyyy" type="DATE"/>
                                    </td>
                                    <td class="text-center">${productFlux.quantity}</td>
                                    <td>${productFlux.observation}</td>
                                    <td>
                                        <c:if test="${productBackSupplier.operationStatus == 'NOT_COMPLETED'}">
                                            <c:url value="/module/pharmacy/operations/movement/supplier-back/editFlux.form" var="editUrl">
                                                <c:param name="backSupplierId" value="${productBackSupplier.productOperationId}"/>
                                                <c:param name="fluxId" value="${productFlux.productAttributeFluxId}"/>
                                            </c:url>
                                            <a href="${editUrl}" class="text-info"><i class="fa fa-edit"></i></a>
                                            <c:url value="/module/pharmacy/operations/movement/back/deleteFlux.form" var="deleteUrl">
                                                <c:param name="backSupplierId" value="${productBackSupplier.productOperationId}"/>
                                                <c:param name="fluxId" value="${productFlux.productAttributeFluxId}"/>
                                            </c:url>
                                            <a href="${deleteUrl}" onclick="return confirm('Voulez vous supprimer ce regime ?')" class="text-danger"><i class="fa fa-trash"></i></a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${fct:length(productAttributeFluxes) == 0}">
                                <tr><td colspan="8" class="text-center text-warning h5">Aucun produit dans la liste</td></tr>
                            </c:if>
                        </c:if>
                        </tbody>
                    </table>
                </form:form>
            </c:if>

            <c:if test="${productBackSupplier.operationStatus != 'NOT_COMPLETED'}">
                <table class="table table-condensed table-striped table-sm table-bordered">
                    <thead class="thead-light">
                    <tr class="bg-belize-hole">
                        <th colspan="3" style="width: 250px">Produit</th>
                        <th style="width: 200px">Num&eacute;ro de lot </th>
                        <th style="width: 150px">Date de p&eacute;remption </th>
                        <th style="width: 60px">Quantit&eacute;</th>
                        <th style="width: 100px">observation</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="productFlux" items="${productAttributeFluxes}">
                        <tr>
                            <td>${productFlux.productAttribute.product.code}</td>
                            <td>${productFlux.productAttribute.product.retailName}</td>
                            <td>${productFlux.productAttribute.product.productRetailUnit.name}</td>
                            <td class="text-center">${productFlux.productAttribute.batchNumber}</td>
                            <td class="text-center">
                                <fmt:formatDate value="${productFlux.productAttribute.expiryDate}" pattern="dd/MM/yyyy"
                                                type="DATE"/>
                            </td>
                            <td class="text-center">${productFlux.quantity}</td>
                            <td>${productFlux.observation}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
    </div>
</div>

<%@ include file="../../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
