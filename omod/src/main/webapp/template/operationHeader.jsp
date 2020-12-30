<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fct" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ include file="includeStyle.jsp"%>

<script type="application/javascript" >
	if (jQuery) {
		jQuery(document).ready(function (){
			jQuery('.s2').select2();

			jQuery.datepicker.setDefaults({
				showOn: "both",
				buttonImageOnly: false,
				//buttonImage: "${pageContext.request.contextPath}/moduleResources/ptme/images/calendar.gif",
				//buttonText: "Calendar"
			});

			jQuery.datepicker.setDefaults( $.datepicker.regional[ "fr" ] );

			jQuery(".picker").datepicker({
				dateFormat: 'dd/mm/yy',
				dayNamesShort: [ "Dim", "Lun", "Mar", "Mer", "Jeu", "Ven", "Sam" ],
				monthNamesShort: [ "Jan", "Fev", "Mar", "Avr", "Mai", "Jui", "Juil", "Aou", "Sep", "Oct", "Nov", "Dec" ],
				changeMonth: true,
				changeYear: true,
			});

			jQuery('.ui-datepicker-trigger').css("display","none");
		});
	}
</script>

<spring:htmlEscape defaultHtmlEscape="true" />

<div class="container-fluid mb-0 pb-0">
	<h5>
		<spring:message code="pharmacy.title" /> :
		Dispensation & Gestion de Stock
	</h5>
	<div class="row bg-info border-top border-bottom border-secondary align-items-center">
		<div class="col-11 pl-0">
			<div class="btn-toolbar m-1 pl-0">
				<div class="btn-group mr-3 ml-0">
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/manage.form'"
						<c:choose>
							<c:when test='<%= request.getRequestURI().contains("/manage") %>'>
								class=" btn btn-secondary btn-sm"
							</c:when>
							<c:otherwise>
								class="btn btn-outline-secondary text-white btn-sm"
							</c:otherwise>
						</c:choose>
					>
						Vue sur le stock
					</button>
				</div>

				<div class="btn-group mr-3">
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/dispensation/list.form'"
							<c:choose>
								<c:when test='<%= request.getRequestURI().contains("/dispensation") %>'>
									class=" btn btn-secondary btn-sm"
								</c:when>
								<c:otherwise>
									class="btn btn-outline-secondary text-white btn-sm"
								</c:otherwise>
							</c:choose>>
						Dispensation aux patients
					</button>
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/distribution/list.form'"
							<c:choose>
								<c:when test='<%= request.getRequestURI().contains("/distribution") %>'>
									class=" btn btn-secondary btn-sm"
								</c:when>
								<c:otherwise>
									class="btn btn-outline-secondary text-white btn-sm"
								</c:otherwise>
							</c:choose>>
						Distribution
					</button>
				</div>

				<div class="btn-group mr-3">
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/reception/list.form'"
							<c:choose>
								<c:when test='<%= request.getRequestURI().contains("/reception") %>'>
									class=" btn btn-secondary btn-sm"
								</c:when>
								<c:otherwise>
									class="btn btn-outline-secondary text-white btn-sm"
								</c:otherwise>
							</c:choose>>
						Reception de produits
					</button>
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/movement/index.form'"
							<c:choose>
								<c:when test='<%= request.getRequestURI().contains("/movement") %>'>
									class=" btn btn-secondary btn-sm"
								</c:when>
								<c:otherwise>
									class="btn btn-outline-secondary text-white btn-sm"
								</c:otherwise>
							</c:choose>>
						Pertes & ajustements
					</button>
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/inventory/list.form'"
							<c:choose>
								<c:when test='<%= request.getRequestURI().contains("/inventory") %>'>
									class=" btn btn-secondary btn-sm"
								</c:when>
								<c:otherwise>
									class="btn btn-outline-secondary text-white btn-sm"
								</c:otherwise>
							</c:choose>>
						Inventaire
					</button>
				</div>

				<div class="btn-group">
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/stock/list.form'"
							<c:choose>
								<c:when test='<%= request.getRequestURI().contains("/stock") %>'>
									class="btn btn-secondary"
								</c:when>
								<c:otherwise>
									class="btn btn-outline-secondary text-white btn-sm"
								</c:otherwise>
							</c:choose>>
						Etat du stock
					</button>
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/report/list.form'"
							<c:choose>
								<c:when test='<%= request.getRequestURI().contains("/report") %>'>
									class=" btn btn-secondary btn-sm"
								</c:when>
								<c:otherwise>
									class="btn btn-outline-secondary text-white btn-sm"
								</c:otherwise>
							</c:choose>>
						Rapport d'activite
					</button>
				</div>

			</div>
		</div>
		<div class="col-1 text-right">
			<div class="btn-group">
				<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/product/list.form'"
						class="btn btn-outline-secondary text-white btn-sm">
					<i class="fa fa-cog"></i>
				</button>
			</div>
		</div>
	</div>
	<div class="row mt-0 mb-1 pb-2 pt-2 pl-0 border-bottom border-secondary bg-light">
		<div class="col-12">
			<div class="text-uppercase text-secondary font-weight-bold">
				<div class="row align-items-center mt-0 mb-0">
					<div class="col-5 pt-1">
						<div class="h5">${title}</div>
					</div>
					<c:if test='<%= request.getRequestURI().contains("/movement") %>'>
						<div class="col-7 text-right">
							<div class="btn-group">
								<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/movement/transfer/list.form'"
										<c:choose>
											<c:when test='<%= request.getRequestURI().contains("/transfer") %>'>
												class=" btn btn-secondary btn-sm"
											</c:when>
											<c:otherwise>
												class="btn btn-outline-secondary btn-sm"
											</c:otherwise>
										</c:choose>>
									Transfert de produits
								</button>
								<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/movement/site-back/list.form'"
										<c:choose>
											<c:when test='<%= request.getRequestURI().contains("/site-back") %>'>
												class=" btn btn-secondary btn-sm"
											</c:when>
											<c:otherwise>
												class="btn btn-outline-secondary btn-sm"
											</c:otherwise>
										</c:choose>>
									Retour de produit
								</button>
								<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/movement/supplier-back/list.form'"
										<c:choose>
											<c:when test='<%= request.getRequestURI().contains("/supplier-back") %>'>
												class=" btn btn-secondary btn-sm"
											</c:when>
											<c:otherwise>
												class="btn btn-outline-secondary btn-sm"
											</c:otherwise>
										</c:choose>>
									Retour au fournisseur
								</button>
								<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/movement/other/list.form'"
										<c:choose>
											<c:when test='<%= request.getRequestURI().contains("/other") %>'>
												class=" btn btn-secondary btn-sm"
											</c:when>
											<c:otherwise>
												class="btn btn-outline-secondary btn-sm"
											</c:otherwise>
										</c:choose>>
									Autres Pertes & ajustements
								</button>
							</div>
						</div>
					</c:if>
				</div>
				<h5 class="m-0"></h5>
			</div>
		</div>
	</div>
</div>
