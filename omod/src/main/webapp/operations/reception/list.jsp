<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/operationHeader.jsp"%>

<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery('.table').DataTable();
        });
    }
</script>
<div class="container-fluid mt-2">

    <div class="row mb-2 ml-1 mr-1">
        <div class="col-6">
            <div class="h5 pt-2"><i class="fa fa-list"></i> ${subTitle}</div>
        </div>
        <div class="col-6 text-right">
            <c:url value="/module/pharmacy/operations/reception/edit.form" var="url"/>
            <button class="btn btn-primary" onclick="window.location='${url}'" title="Créer nouveau">
                <i class="fa fa-plus"></i> Nouvelle reception
            </button>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <div class="col-12">
            <table class="table table-striped table-sm">
                <thead>
                <tr>
                    <th>
                        <%--            <spring:message code="pharmacy.supplier"/>--%>
                        Fournisseur
                    </th>
                    <th>
                        <%--            <spring:message code="pharmacy.receptionDate"/>--%>
                        Date de reception
                    </th>
                    <th>
                        <%--            <spring:message code="pharmacy.receptionCode"/>--%>
                        BL
                    </th>
                    <th>
                        <%--            <spring:message code="pharmacy.program"/>--%>
                        Programme
                    </th>
                    <th>
                        <%--            <spring:message code="pharmacy.program"/>--%>
                        Type de saisie
                    </th>
                    <th>Nombre de produits</th>
                    <th>
                        <%--            <spring:message code="pharmacy.status"/>--%>
                        Etat
                    </th>
                    <th style="width: 30px"></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="reception" items="${ receptions }">
                    <tr>
                        <td>${reception.productSupplier.name}</td>
                        <td><fmt:formatDate value="${reception.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                        <td>${reception.operationNumber}</td>
                        <td>${reception.productProgram.name}</td>
                        <td>${reception.receptionQuantityMode}</td>
                        <c:choose>
                            <c:when test="${fct:length(reception.productAttributeFluxes) == 0}">
                                <c:url value="/module/pharmacy/operations/reception/editFlux.form" var="addLineUrl">
                                    <c:param name="receptionId" value="${reception.productOperationId}"/>
                                </c:url>
                                <td class="text-danger">
                                    <a href="${addLineUrl}">Ajouter des produits</a>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td class="text-center">
                                        ${fct:length(reception.productAttributeFluxes)}
                                </td>
                            </c:otherwise>
                        </c:choose>
                        <td>${reception.operationStatus}</td>
                        <td>
                            <c:url value="/module/pharmacy/operations/reception/edit.form" var="editUrl">
                                <c:param name="id" value="${reception.productOperationId}"/>
                            </c:url>
                            <a href="${editUrl}" class="text-info mr-2"><i class="fa fa-edit"></i></a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>

            </table>
        </div>
    </div>
</div>
<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
