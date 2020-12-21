<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/operationHeader.jsp"%>

<script>

    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery('.table').DataTable();

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
            window.location="${pageContext.request.contextPath}/module/pharmacy/operations/movement/edit.form?type=" +
                type + "&programId=" + program;
        }

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
                    <label for="program">Programme</label>
                    <select name="program" class="s2 form-control form-control-sm" id="program" onchange="checkTypeValue()">
                        <option value="Choisir Programme"></option>
                        <c:forEach var="program" items="${programs}">
                            <option value="${program.productProgramId}">${program.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-3">
                    <div class="row p-0" id="entry-type-selector">
                        <div class="col-12 p-0 m-0">
                            <label for="stockEntryType">Type d'entr&eacute;e</label>
                            <select name="stockEntryType" id="stockEntryType" class="s2 form-control form-control-sm">
                                <option value=""></option>
                                <c:forEach var="stockEntryType"  items="${stockEntryTypes}">
                                    <option value="${stockEntryType.key}">${stockEntryType.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="row p-0 m-0" id="out-type-selector">
                        <div class="col-12 p-0 m-0">
                            <label for="stockOutType">Type de sortie</label>
                            <select name="stockOutType" id="stockOutType" class="s2 form-control form-control-sm">
                                <option value=""></option>
                                <c:forEach var="stockOutType" items="${stockOutTypes}">
                                    <option value="${stockOutType.key}">${stockOutType.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="col-4 text-right">
                    <button class="btn btn-primary btn-sm" onclick="openMovementForm()" title="Créer nouveau">
                        <i class="fa fa-plus"></i>
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-6">
            <div class="row mb-2">
                <div class="col-3">
                    <div class="h5 pt-2"><i class="fa fa-list"></i> Entr&eacute;e</div>
                </div>
                <%--                <div class="col-9">--%>
                <%--                    <select name="stockEntryType" id="stockEntryType" class="s2">--%>
                <%--                        <option value=""></option>--%>
                <%--                        <c:forEach var="stockEntryType"  items="${stockEntryTypes}">--%>
                <%--                            <option value="${stockEntryType.key}">${stockEntryType.value}</option>--%>
                <%--                        </c:forEach>--%>
                <%--                    </select>--%>
                <%--                    <button class="btn btn-primary btn-sm" onclick="addStockEntryType()" title="Créer nouveau">--%>
                <%--                        <i class="fa fa-plus"></i> Nouvelle entr&eacute;e--%>
                <%--                    </button>--%>
                <%--                </div>--%>
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
                <%--                <div class="col-9 text-right">--%>
                <%--                    <select name="stockOutType" id="stockOutType" class="s2">--%>
                <%--                        <option value=""></option>--%>
                <%--                        <c:forEach var="stockOutType" items="${stockOutTypes}">--%>
                <%--                            <option value="${stockOutType.key}">${stockOutType.value}</option>--%>
                <%--                        </c:forEach>--%>
                <%--                    </select>--%>
                <%--                    &lt;%&ndash;                    <c:url value="/module/pharmacy/operations/movement/edit.form" var="url"/>&ndash;%&gt;--%>
                <%--                    <button class="btn btn-primary" onclick="addStockOutType()" title="Créer nouveau">--%>
                <%--                        <i class="fa fa-plus"></i> Nouvelle Sortie--%>
                <%--                    </button>--%>
                <%--                </div>--%>
            </div>
            ${fct:length(outs)}
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
