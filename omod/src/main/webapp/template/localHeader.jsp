<%--<%@ page contentType="text/html; charset=UTF-8" %>--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fct" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Fira+Sans:400,500,600">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:wght@300;400;500&display=swap">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Material+Icons">

<%@ include file="includeStyle.jsp"%>
<script type="application/javascript" >
	if (jQuery) {
		jQuery(document).ready(function () {
			jQuery('.s2').select2();

			jQuery('[data-toggle="tooltip"]').tooltip();

			jQuery('#customFile').on('change',function(){
				//get the file name
				const fileName = $(this).val();
				if (fileName) {
					let tFileName = fileName.split('\\')
					jQuery(this).next('.custom-file-label').html(tFileName[tFileName.length - 1]);
				} else {
					jQuery(this).next('.custom-file-label').html("Choisir le fichier pour importation");
				}
			});
		});

	}
</script>

<spring:htmlEscape defaultHtmlEscape="true" />
<ul id="menu">
	<openmrs:hasPrivilege privilege="View Product">
		<li <c:if test='<%= request.getRequestURI().contains("/product/list") ||
						request.getRequestURI().contains("/product/edit") %>'>
			class="active first"
		</c:if>
				<c:if test='<%= !request.getRequestURI().contains("/product/list") &&
						!request.getRequestURI().contains("/product/edit") %>'>
					class="first"
				</c:if>>
			<a href="${pageContext.request.contextPath}/module/pharmacy/product/list.form">
					<%--			<spring:message code="pharmacy.productManagement" />--%>
				Produits
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="View Program">
		<li <c:if test='<%= request.getRequestURI().contains("/programs") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/pharmacy/product/programs/list.form">
					<%--			<spring:message code="pharmacy.programManagement" />--%>
				Programmes
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="View Regimen">
		<li <c:if test='<%= request.getRequestURI().contains("/regimens") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/pharmacy/product/regimens/list.form">
					<%--			<spring:message code="pharmacy.regimenManagement" />--%>
				R&eacute;gimes
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="View Price">
		<li <c:if test='<%= request.getRequestURI().contains("/prices") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/pharmacy/product/prices/list.form">
					<%--			<spring:message code="pharmacy.priceManagement" />--%>
				Prix de produits
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="View Unit">
		<li <c:if test='<%= request.getRequestURI().contains("/units") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/pharmacy/product/units/list.form">
					<%--			<spring:message code="pharmacy.unitManagement" />--%>
				Unit&eacute;s de produits
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="View Supplier">
		<li <c:if test='<%= request.getRequestURI().contains("/suppliers/") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/pharmacy/product/suppliers/list.form">
					<%--			<spring:message code="pharmacy.supplierManagement" />--%>
				Fournisseurs
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="View Exchange Entity">
		<li <c:if test='<%= request.getRequestURI().contains("/exchanges") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/pharmacy/product/exchanges/list.form">
					<%--			<spring:message code="pharmacy.exchangeManagement" />--%>
				Partenaires
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="Manage Locations">
		<li <c:if test='<%= request.getRequestURI().contains("/center") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/pharmacy/center/manage.form">
				Gestion du centre / district
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="View Providers">
		<li <c:if test='<%= request.getRequestURI().contains("/prescriber") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/pharmacy/prescriber/list.form">
				Prescripteurs
			</a>
		</li>
	</openmrs:hasPrivilege>
	<li>
		<a href="${pageContext.request.contextPath}/module/pharmacy/manage.form">
			Retour &agrave; la pharmacie
		</a>
	</li>
	
	<!-- Add further links here -->
</ul>
<h2 class="pt-2">
	<spring:message code="pharmacy.title" />
</h2>
