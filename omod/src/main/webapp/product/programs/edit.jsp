<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/localHeader.jsp"%>
<openmrs:require privilege="Save Program" otherwise="/login.htm" redirect="/module/pharmacy/product/programs/edit.form" />

<hr>
<div class="row">
    <div class="col-6">
        <h4>${title}</h4>
    </div>
    <div class="col-6 text-right">
        <c:url value="/module/pharmacy/product/programs/list.form" var="createUrl"/>
        <button class="btn btn-primary" onclick="window.location='${createUrl}'" title="Voir la liste">
            <i class="fa fa-list"></i>
        </button>
    </div>
</div>
<hr>
<div class="container">
    <form:form modelAttribute="programForm" method="post" action="" id="form">
        <form:hidden path="productProgramId"/>
        <form:hidden path="uuid"/>

        <div class="row">
            <div class="col-6 mb-2">
                <labe><spring:message code="pharmacy.name"/> <span class="required">*</span></labe>
                <form:input path="name" cssClass="form-control" />
                <form:errors path="name" cssClass="error"/>
            </div>
        </div>
        <div class="row">
            <div class="col-2 mb-2">
                <labe>Num&eacute;ro unique</labe>
                <form:input path="programNumber" cssClass="form-control" />
                <form:errors path="programNumber" cssClass="error"/>
            </div>
        </div>
        <div class="row">
            <div class="col-8 mb-2">
                <labe><spring:message code="pharmacy.description"/></labe>
                <form:textarea path="description" cssClass="form-control" />
                <form:errors path="description" cssClass="error"/>
            </div>
        </div>
        <openmrs:hasPrivilege privilege="Manage Program">
            <div class="row">
                <div class="col-12">
                    <button class="btn btn-success">
                        <c:if test="${not empty programForm.productProgramId}">
                            <i class="fa fa-edit"></i>
                            <spring:message code="pharmacy.edit" />
                        </c:if>
                        <c:if test="${empty programForm.productProgramId}">
                            <i class="fa fa-save"></i>
                            <spring:message code="pharmacy.save" />
                        </c:if>
                    </button>
                </div>
            </div>
        </openmrs:hasPrivilege>
    </form:form>
</div>


<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
