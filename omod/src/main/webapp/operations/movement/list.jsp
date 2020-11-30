<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/operationHeader.jsp"%>

<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery('.table').DataTable();
        });
        function addStockEntryType(){
            const selector = document.querySelector('#stockEntryType')
            if (selector.value !== null && selector.value !== undefined) {
                window.location="${pageContext.request.contextPath}/module/pharmacy/operations/movement/edit.form?type="+selector.value;
            }
            else {jQuery('#stockEntryType').addClass('is-invalid');}
        }

        function addStockOutType(){
            const selector = document.querySelector('#stockOutType')
            if (selector.value !== null && selector.value !== undefined) {
                window.location="${pageContext.request.contextPath}/module/pharmacy/operations/movement/edit.form?type="+selector.value;
            }
            else {jQuery('#stockOutType').addClass('is-invalid');}
        }
    }
</script>
<div class="container-fluid mt-2">
    <div class="row">
        <div class="col-6">
            <div class="row mb-2 ml-1 mr-1">
                <div class="col-3">
                    <div class="h5 pt-2"><i class="fa fa-list"></i> Entre&eacute;</div>
                </div>
                <div class="col-9 text-right">
                    <select name="stockEntryType" id="stockEntryType" class="s2">
                        <option value=""></option>
                        <c:forEach var="stockEntryType"  items="${stockEntryTypes}">
                            <option value="${stockEntryType.key}">${stockEntryType.value}</option>
                        </c:forEach>
                    </select>
                    <button class="btn btn-primary" onclick="addStockEntryType()" title="Créer nouveau">
                        <i class="fa fa-plus"></i> Nouvelle entre
                    </button>
                </div>
            </div>
            <div class="row bg-light pt-2 pb-2 border border-secondary">
                <div class="col-12">
                    <table class="table table-striped table-sm">
                        <thead>
                        <tr>
                            <th>
                                Type de mouvement
                            </th>
                            <th>
                                Date de mouvement
                            </th>
                            <th>
                                Programme
                            </th>
                            <th style="width: 30px"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="entry" items="${ entries }">
                            <tr>
                                <td>${ entry.stockEntryType}</td>
                                <td><fmt:formatDate value="${entry.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                                <td>${entry.productProgram.name}</td>
                                <td>
                                    <c:url value="/module/pharmacy/operations/movement/edit.form" var="editUrl">
                                        <c:param name="id" value="${entry.productOperationId}"/>
                                        <c:param name="type" value="${entry.stockEntryType}"/>
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
        <div class="col-6">
            <div class="row mb-2 ml-1 mr-1">
                <div class="col-3">
                    <div class="h5 pt-2"><i class="fa fa-list"></i> Sortie</div>
                </div>
                <div class="col-9 text-right">
                    <select name="stockOutType" id="stockOutType" class="s2">
                        <option value=""></option>
                        <c:forEach var="stockOutType" items="${stockOutTypes}">
                            <option value="${stockOutType.key}">${stockOutType.value}</option>
                        </c:forEach>
                    </select>
<%--                    <c:url value="/module/pharmacy/operations/movement/edit.form" var="url"/>--%>
                    <button class="btn btn-primary" onclick="addStockOutType()" title="Créer nouveau">
                        <i class="fa fa-plus"></i> Nouvelle Sortie
                    </button>
                </div>
            </div>
            <div class="row bg-light pt-2 pb-2 border border-secondary">
                <div class="col-12">
                    <table class="table table-striped table-sm">
                        <thead>
                        <tr>
                            <th>
                                Type de mouvement
                            </th>
                            <th>
                                Date de mouvement
                            </th>
                            <th>
                                Programme
                            </th>
                            <th style="width: 30px"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="out" items="${ outs }">
                            <tr>
                                <td>${out.stockOutType}</td>
                                <td><fmt:formatDate value="${out.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                                <td>${out.productProgram.name}</td>
                                <td>
                                    <c:url value="/module/pharmacy/operations/movement/edit.form" var="editUrl">
                                        <c:param name="id" value="${out.productOperationId}"/>
                                        <c:param name="type" value="${out.stockOutType}"/>
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

    </div>
</div>
<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>