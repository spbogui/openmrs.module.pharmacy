<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="View Movement" otherwise="/login.htm" redirect="/module/pharmacy/operations/movement/other/list.form" />

<%@ include file="../../../template/operationHeader.jsp"%>

<script>

    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery('.table').DataTable({
                "columnDefs" : [{"targets":0, "type":"date-eu"}],
            });

            checkTypeValue();

            // jQuery("#program").on('change', function () {
            //     if (jQuery(this).val()) {
            //         let stockEntryType = jQuery('#entry-type-selector');
            //         let stockOutType = jQuery("#out-type-selector");
            //         stockEntryType.hide();
            //         stockOutType.hide();
            //         let typeValue = jQuery("input[name='movementType']:checked").val();
            //         if (typeValue === 'entry') {
            //             stockEntryType.show();
            //         } else {
            //             stockOutType.show();
            //         }
            //     }
            // });
        });

        function checkTypeValue() {
            let stockEntryType = jQuery('#entry-type-selector');
            let stockOutType = jQuery("#out-type-selector");
            let program = jQuery('#program');
            stockEntryType.hide();
            stockOutType.hide();
            program.attr('disabled', 'disabled');

            let typeValue = jQuery("input[name='movementType']:checked").val();
            if (typeValue) {
                program.removeAttr('disabled');
                if (program.val()) {
                    if (typeValue === 'entry') {
                        stockEntryType.show();
                    } else
                    {
                        stockOutType.show();
                    }
                }

            }
        }
        function openMovementForm() {
            let typeValue = jQuery("input[name='movementType']:checked").val();
            let program = jQuery("#program").val();
            console.log('Movement Type : ', typeValue);
            console.log('Program Id : ', program);
            let selectorValue;
            if (typeValue === 'entry') {
                selectorValue = jQuery('#stockEntryType').val();
            } else if (typeValue === 'out') {
                selectorValue = jQuery('#stockOutType').val();
            }
            if (selectorValue && program) {
                go(program, selectorValue);
            }
            // Add movement function to create
        }

        function go(program, type) {
            window.location="${pageContext.request.contextPath}/module/pharmacy/operations/movement/other/edit.form?type=" +
                type + "&programId=" + program;
        }

        function addStockEntryType(){
            const selector = document.querySelector('#stockEntryType')
            if (selector.value !== null && selector.value !== undefined) {
                window.location="${pageContext.request.contextPath}/module/pharmacy/operations/movement/other/edit.form?type="+selector.value;
            }
            else {jQuery('#stockEntryType').addClass('is-invalid');}
        }

        function addStockOutType(){
            const selector = document.querySelector('#stockOutType')
            if (selector.value !== null && selector.value !== undefined) {
                window.location="${pageContext.request.contextPath}/module/pharmacy/operations/movement/other/edit.form?type="+selector.value;
            }
            else {jQuery('#stockOutType').addClass('is-invalid');}
        }
    }
</script>
<div class="container-fluid mt-2">
    <div class="row">
        <div class="col-12">
            <div class="row align-items-center border-bottom border-secondary pb-2">
                <div class="col-3">
                    <label>Cr&eacute;er une</label><br>
                    <label for="entry" class="">
                        <input type="radio" name="movementType" class="" id="entry" onclick="checkTypeValue()" value="entry">
                        Entr&eacute;e de stock
                    </label> &nbsp;&nbsp;&nbsp;
                    <label for="out">
                        <input type="radio" name="movementType" id="out" value="out" class="" onclick="checkTypeValue()">
                        Sortie de stock
                    </label>
                </div>
                <div class="col-2">
                    <label for="program">Programme</label> <br>
                    <select name="program" class="s2 form-control form-control-sm" id="program" onchange="checkTypeValue()">
                        <option value="Choisir Programme"></option>
                        <c:forEach var="program" items="${programs}">
                            <option value="${program.productProgramId}">${program.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-3">
                    <div id="entry-type-selector">
                        <label for="stockEntryType">Type d'entr&eacute;e</label> <br>
                        <select name="stockEntryType" id="stockEntryType" class="s2 form-control form-control-sm"
                                style="width: 150px;">
                            <option value=""></option>
                            <c:forEach var="stockEntryType"  items="${stockEntryTypes}">
                                <option value="${stockEntryType.key}">${stockEntryType.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div id="out-type-selector">
                        <label for="stockOutType">Type de sortie</label> <br>
                        <select name="stockOutType" id="stockOutType" class="s2 form-control form-control-sm"
                                style="width: 250px;">
                            <option value=""></option>
                            <c:forEach var="stockOutType" items="${stockOutTypes}">
                                <option value="${stockOutType.key}">${stockOutType.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="col-4 text-right">
                    <button class="btn btn-primary" onclick="openMovementForm()" title="">
                        <i class="fa fa-plus"></i>
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-12">
            <div class="row mb-2">
                <div class="col-3">
                    <div class="h6 pt-2"><i class="fa fa-list"></i> Liste des autres pertes et ajustements</div>
                </div>
            </div>
            <div class="row bg-light pt-2 pb-2 border border-secondary">
                <div class="col-12">
                    <table class="table table-striped table-sm">
                        <thead>
                        <tr>
                            <th>Date du mouvement</th>
                            <th>Type de mouvement</th>
                            <th>Programme</th>
                            <th>Lignes de produit</th>
                            <th>Etat</th>
                            <th style="width: 40px"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="entry" items="${ entries }">
                            <tr class="align-middle">
                                <td><fmt:formatDate value="${entry.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                                <td>${ entry.stockEntryType == 'DONATION' ? 'DON' : 'Ajustement inventaire positif'}</td>
                                <td>${entry.productProgram.name}</td>
                                <c:choose>
                                    <c:when test="${fct:length(entry.productAttributeFluxes) == 0}">
                                        <c:url value="/module/pharmacy/operations/movement/editFlux.form" var="addLineUrl">
                                            <c:param name="movementId" value="${entry.productOperationId}"/>
                                            <c:param name="type" value="entry"/>
                                        </c:url>
                                        <td class="text-danger">
                                            <div class="btn-group">
                                                <a href="${addLineUrl}"><div class="btn btn-sm btn-success">Ajouter des produits</div></a>
                                            </div>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td class="text-center">
                                            <div class="btn-group">
                                                <div class="btn btn-sm btn-primary">
                                                        ${fct:length(entry.productAttributeFluxes)}
                                                </div>
                                            </div>
                                        </td>
                                    </c:otherwise>
                                </c:choose>
                                <td>${entry.operationStatus == 'NOT_COMPLETED' ? 'EN COURS DE SAISIE' : (entry.operationStatus == 'VALIDATED' ? 'VALIDE' : 'EN ATTENTE DE VALIDATION')}</td>
                                <td>
                                    <c:if test="${entry.operationStatus == 'VALIDATED'}">
                                        <c:url value="/module/pharmacy/operations/movement/other/editFlux.form" var="addLineUrl">
                                            <c:param name="movementId" value="${entry.productOperationId}"/>
                                            <c:param name="type" value="entry"/>
                                        </c:url>
                                        <a href="${addLineUrl}" class="text-success primary">
                                            <i class="fa fa-eye"></i>
                                        </a>
                                    </c:if>

                                    <c:if test="${entry.operationStatus != 'VALIDATED'}">
                                        <c:url value="/module/pharmacy/operations/movement/other/editFlux.form" var="editUrl">
                                            <c:param name="movementId" value="${entry.productOperationId}"/>
                                            <c:param name="type" value="entry"/>
                                        </c:url>
                                        <c:url value="/module/pharmacy/operations/movement/other/delete.form" var="delUrl">
                                            <c:param name="id" value="${entry.productOperationId}"/>
                                            <c:param name="type" value="entry"/>
                                        </c:url>
                                        <a href="${editUrl}" class="text-info primary"><i class="fa fa-edit"></i></a>
                                        <a href="${delUrl}" onclick="return confirm('Vous etes sur le point de supprimer le mouvement, Voulez-vous continuer ?')" class="text-danger">
                                            <i class="fa fa-trash"></i>
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:forEach var="out" items="${ outs }">
                            <tr class="align-middle">
                                <td><fmt:formatDate value="${out.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                                <td>
                                    <c:if test="${out.stockOutType == 'THIEF'}">
                                        VOLS
                                    </c:if>
                                    <c:if test="${out.stockOutType == 'DESTROYED'}">
                                        Produits Endommag&eacute;s
                                    </c:if>
                                    <c:if test="${out.stockOutType == 'EXPIRED_PRODUCT'}">
                                        Produits Perim&eacute;s
                                    </c:if>
                                    <c:if test="${out.stockOutType == 'SPOILED_PRODUCT'}">
                                        Produits Avari&eacute;s
                                    </c:if>
                                    <c:if test="${out.stockOutType == 'NEGATIVE_INVENTORY_ADJUSTMENT'}">
                                        Ajustement inventaire n&eacute;gatif
                                    </c:if>
                                    <c:if test="${out.stockOutType == 'OTHER_LOST'}">
                                        Autres pertes
                                    </c:if>
                                </td>
                                <td>${out.productProgram.name}</td>
                                <c:choose>
                                    <c:when test="${fct:length(out.productAttributeFluxes) == 0}">
                                        <c:url value="/module/pharmacy/operations/movement/other/editFlux.form" var="addLineUrl">
                                            <c:param name="movementId" value="${out.productOperationId}"/>
                                            <c:param name="type" value="out"/>
                                        </c:url>
                                        <td class="text-danger">
                                            <div class="btn-group">
                                                <a href="${addLineUrl}"><div class="btn btn-sm btn-success">Ajouter des produits</div></a>
                                            </div>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td class="text-center">
                                            <div class="btn-group">
                                                <div class="btn btn-sm btn-primary">
                                                        ${fct:length(out.productAttributeFluxes)}
                                                </div>
                                            </div>
                                        </td>
                                    </c:otherwise>
                                </c:choose>
                                <td>${out.operationStatus == 'NOT_COMPLETED' ? 'EN COURS DE SAISIE' : (out.operationStatus == 'VALIDATED' ? 'VALIDE' : 'EN ATTENTE DE VALIDATION')}</td>
                                <td>
                                    <c:if test="${out.operationStatus == 'VALIDATED'}">
                                        <c:url value="/module/pharmacy/operations/movement/other/editFlux.form" var="addLineUrl">
                                            <c:param name="movementId" value="${out.productOperationId}"/>
                                            <c:param name="type" value="out"/>
                                        </c:url>
                                        <a href="${addLineUrl}" class="text-success primary"><i class="fa fa-eye"></i></a>
                                    </c:if>
                                    <c:if test="${out.operationStatus != 'VALIDATED'}">
                                        <c:url value="/module/pharmacy/operations/movement/other/editFlux.form" var="editUrl">
                                            <c:param name="movementId" value="${out.productOperationId}"/>
                                            <c:param name="type" value="out"/>
                                        </c:url>
                                        <a href="${editUrl}" class="text-info primary"><i class="fa fa-edit"></i></a>
                                        <openmrs:hasPrivilege privilege="Delete Movement">
                                            <c:url value="/module/pharmacy/operations/movement/other/delete.form"
                                                   var="delUrl">
                                                <c:param name="id" value="${out.productOperationId}"/>
                                                <c:param name="type" value="out"/>
                                            </c:url>
                                            <a href="${delUrl}"
                                               onclick="return confirm('Vous etes sur le point de supprimer le mouvement, Voulez-vous continuer ?')"
                                               class="text-danger">
                                                <i class="fa fa-trash"></i>
                                            </a>
                                        </openmrs:hasPrivilege>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>

                    </table>
                </div>
            </div>
        </div>
        <%--        <div class="col-6">--%>
        <%--            <div class="row mb-2 ml-1 mr-1">--%>
        <%--                <div class="col-3">--%>
        <%--                    <div class="h6 pt-2"><i class="fa fa-list"></i> Sortie</div>--%>
        <%--                </div>--%>
        <%--            </div>--%>
        <%--            <div class="row bg-light pt-2 pb-2 border border-secondary">--%>
        <%--                <div class="col-12">--%>
        <%--                    <table class="table table-striped table-sm">--%>
        <%--                        <thead>--%>
        <%--                        <tr>--%>
        <%--                            <th>Type de mouvement</th>--%>
        <%--                            <th>Date de mouvement</th>--%>
        <%--                            <th>Programme</th>--%>
        <%--                            <th>Nombre de produits</th>--%>
        <%--                            <th>Etat</th>--%>
        <%--                            <th style="width: 40px"></th>--%>
        <%--                        </tr>--%>
        <%--                        </thead>--%>
        <%--                        <tbody style="font-size: 11px">--%>

        <%--                        </tbody>--%>

        <%--                    </table>--%>
        <%--                </div>--%>
        <%--            </div>--%>
        <%--        </div>--%>

    </div>
</div>
<%@ include file="../../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
