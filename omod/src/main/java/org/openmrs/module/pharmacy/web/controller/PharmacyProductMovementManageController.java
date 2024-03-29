package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.entities.*;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.enumerations.StockEntryType;
import org.openmrs.module.pharmacy.enumerations.StockOutType;
import org.openmrs.module.pharmacy.forms.movements.MovementAttributeFluxForm;
import org.openmrs.module.pharmacy.forms.movements.ProductMovementForm;
import org.openmrs.module.pharmacy.dto.ProductOutFluxDTO;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.openmrs.module.pharmacy.forms.movements.validators.ProductMovementAttributeFluxFormValidation;
import org.openmrs.module.pharmacy.forms.movements.validators.ProductMovementFormValidation;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class PharmacyProductMovementManageController {
    protected final Log log = LogFactory.getLog(getClass());

    private ProductMovementService service() {
        return Context.getService(ProductMovementService.class);
    }
    private ProductProgramService programService(){
        return Context.getService(ProductProgramService.class);
    }
    private ProductExchangeEntityService ExchangeService() {
        return Context.getService(ProductExchangeEntityService.class);
    }
    private PharmacyService pharmacyService() {
        return Context.getService(PharmacyService.class);
    }
    private ProductAttributeFluxService productAttributeFluxService(){
        return Context.getService(ProductAttributeFluxService.class);
    }
    private ProductAttributeFluxService attributeFluxService(){
        return Context.getService(ProductAttributeFluxService.class);
    }
    private ProductAttributeService productAttributeService(){
        return Context.getService(ProductAttributeService.class);
    }

    private ProductAttributeStockService stockService() {
        return Context.getService(ProductAttributeStockService.class);
    }

    @ModelAttribute("title")
    public String getTile() {
        return "Pertes et Ajustements";
    }

    @ModelAttribute("isDirectClient")
    public Boolean isDirectClient() {
        return OperationUtils.isDirectClient(OperationUtils.getUserLocation());
    }

    @ModelAttribute("canDistribute")
    public Boolean canDistribute() {
        return OperationUtils.canDistribute(OperationUtils.getUserLocation());
    }

    public Location getUserLocation() {
        return Context.getLocationService().getDefaultLocation();
    }
    @RequestMapping(value = "/module/pharmacy/operations/movement/index.form", method = RequestMethod.GET)
    public void index(ModelMap modelMap) {

    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/other/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {

        if (Context.isAuthenticated()) {
            modelMap.addAttribute("entries", service().getAllProductMovementEntry(OperationUtils.getUserLocation(), false));
            modelMap.addAttribute("outs", service().getAllProductMovementOut(OperationUtils.getUserLocation(), false));
            modelMap.addAttribute("stockEntryTypes", getEntryTypeLabels());
            modelMap.addAttribute("stockOutTypes", getOutTypeLabels());
            modelMap.addAttribute("programs", OperationUtils.getUserLocationPrograms());
            modelMap.addAttribute("subTitle", "Liste des Mouvements");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/other/edit.form", method = RequestMethod.GET)
    public String edit(ModelMap modelMap,
                       @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                       @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId,
                       @RequestParam(value = "type") String type,
                       ProductMovementForm productMovementForm) {
        if (Context.isAuthenticated()) {
            String movementType = "";
            StockEntryType entryType;
            StockOutType outType;
            if (id != 0){
              ProductOperation productOperation = pharmacyService().getOneProductOperationById(id);
              if (productOperation != null) {
                  modelMap.addAttribute("program", productOperation.getProductProgram());
              }
            }
            if (getOutTypeLabels().containsKey(type)){
                movementType = "out";
                outType = StockOutType.valueOf(type);
                if (id != 0) {
                    productMovementForm.setProductMovementOut(service().getOneProductMovementOutById(id));
                } else {
                    productMovementForm = new ProductMovementForm();
                    productMovementForm.setIncidence(Incidence.NEGATIVE);
                    productMovementForm.setLocationId(getUserLocation().getLocationId());
                    productMovementForm.setStockOutType(outType);
                }
                modelMap.addAttribute("subTitle", getOutTypeLabels().get(outType.name()) );
            }
            else {
                movementType = "entry";
                entryType = StockEntryType.valueOf(type);
                if (id != 0) {
                    productMovementForm.setProductMovementEntry(service().getOneProductMovementEntryById(id));
                }
                else {
                    productMovementForm = new ProductMovementForm();
                    productMovementForm.setIncidence(Incidence.POSITIVE);
                    productMovementForm.setLocationId(getUserLocation().getLocationId());
                    productMovementForm.setStockEntryType(entryType);
                }
                modelMap.addAttribute("subTitle", getEntryTypeLabels()
                        .get(entryType.name()));
            }
            if (programId != 0){
                productMovementForm.setProductProgramId(programId);
                modelMap.addAttribute("program", programService().getOneProductProgramById(programId));
            }
            modelMap.addAttribute("productMovementForm", productMovementForm);
            modelMap.addAttribute("type", type);
            modelMap.addAttribute("exchanges", ExchangeService().getAllProductExchange());
            modelMap.addAttribute("movementType", movementType);
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/other/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                       HttpServletRequest request,
                       @RequestParam(value = "action", defaultValue = "addLine", required = false) String action,
                       @RequestParam(value = "type") String type,
                       ProductMovementForm productMovementEntryForm,
                       BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();
            new ProductMovementFormValidation().validate(productMovementEntryForm, result);

            Integer movementId;
            String movementType;
            Set<ProductAttributeFlux> fluxes = new HashSet<ProductAttributeFlux>();

            if (!result.hasErrors()) {
                if (getOutTypeLabels().containsKey(type)){
                    ProductMovementOut out = productMovementEntryForm.getProductMovementOut();
                    movementId = service().saveProductMovementOut(out).getProductOperationId();
                    movementType = "out";
                    fluxes = out.getProductAttributeFluxes();
                }
                else {
                    ProductMovementEntry entry = productMovementEntryForm.getProductMovementEntry();
                    movementId =  service().saveProductMovementEntry(entry).getProductOperationId();
                    movementType = "entry";
                    fluxes = entry.getProductAttributeFluxes();
                }
                if (action.equals("addLine")) {
                    if (fluxes.size() == 0) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez maintenant ajouter les produits !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez continuer à ajouter les produits !");
                    }

                    return "redirect:/module/pharmacy/operations/movement/other/editFlux.form?type="+ movementType +"&movementId=" + movementId;
                }
                else {
                    if (productMovementEntryForm.getProductOperationId() != null){
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Votre Operation a été Modifiée avec succès !");
                    }else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Votre Operation a été enregistrée avec succès !");
                    }
                    return "redirect:/module/pharmacy/operations/movement/other/list.form";
                }
            }
            modelMap.addAttribute("productMovementEntryForm", productMovementEntryForm);
//            modelMap.addAttribute("product", receptionHeaderForm.getProduct());
            modelMap.addAttribute("programs", programService().getAllProductProgram());
            modelMap.addAttribute("exchanges", ExchangeService().getAllProductExchange());
            modelMap.addAttribute("subTitle", "Saisie  de Mouvements");
        }

        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/other/editFlux.form", method = RequestMethod.GET)
    public String editFlux(ModelMap modelMap,
                           @RequestParam(value = "movementId") Integer movementId,
                           @RequestParam(value = "selectedProductId", defaultValue = "0", required = false) Integer selectedProductId,
                           @RequestParam(value = "type") String type ,
                           @RequestParam(value = "fluxId", defaultValue = "0", required = false) Integer fluxId,
                           MovementAttributeFluxForm movementAttributeFluxForm) {
        if (Context.isAuthenticated()) {
            ProductOperation productMovement = pharmacyService().getOneProductOperationById(movementId);
            if (productMovement != null){
                if (fluxId != 0) {
                    ProductAttributeFlux productAttributeFlux = attributeFluxService().getOneProductAttributeFluxById(fluxId);
                    if (productAttributeFlux != null) {
                        movementAttributeFluxForm.setProductAttributeFlux(productAttributeFlux, productMovement);
                    } else {
                        movementAttributeFluxForm = new MovementAttributeFluxForm();
                        movementAttributeFluxForm.setProductOperationId(movementId);
                    }

                }
                else {
                    movementAttributeFluxForm = new MovementAttributeFluxForm();
                    movementAttributeFluxForm.setProductOperationId(movementId);
                }

                selectProduct(modelMap, selectedProductId, type, movementAttributeFluxForm, productMovement);
//                modelMappingForView(modelMap, movementAttributeFluxForm, productMovement, type);
            }
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/other/editFlux.form", method = RequestMethod.POST)
    public String saveFlux(ModelMap modelMap,
                           @RequestParam(value = "type") String type,
                           HttpServletRequest request,
                           @RequestParam(value = "selectedProductId", defaultValue = "0", required = false) Integer selectedProductId,
                           MovementAttributeFluxForm movementAttributeFluxForm,
                           BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();
            new ProductMovementAttributeFluxFormValidation().validate(movementAttributeFluxForm, result);
            ProductOperation productMovement = pharmacyService().getOneProductOperationById(movementAttributeFluxForm.getProductOperationId());

            if (!result.hasErrors()) {
                ProductAttribute productAttribute = productAttributeService().saveProductAttribute(movementAttributeFluxForm.getProductAttribute());
                if (productAttribute != null) {
                    ProductAttributeFlux productAttributeFlux = movementAttributeFluxForm.getProductAttributeFlux(productAttribute);
                    productAttributeFlux.setStatus(productMovement.getOperationStatus());
                    productAttributeFluxService().saveProductAttributeFlux(productAttributeFlux);
                    if (movementAttributeFluxForm.getProductOperationId() == null) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit insérés avec succès !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit modifié avec succès");
                    }

                    return "redirect:/module/pharmacy/operations/movement/other/editFlux.form?type=" + type +"&movementId="
                            + movementAttributeFluxForm.getProductOperationId();
                }
            }
            selectProduct(modelMap, selectedProductId, type, movementAttributeFluxForm, productMovement);
//            modelMappingForView(modelMap, movementAttributeFluxForm, productMovement, type);
        }

        return null;
    }

    private void selectProduct(ModelMap modelMap, @RequestParam(value = "selectedProductId", defaultValue = "0", required = false) Integer selectedProductId,
                               @RequestParam(value = "type") String type,
                               MovementAttributeFluxForm movementAttributeFluxForm,
                               ProductOperation productOperation) {
        ProductAttributeStock stock = null;
        if (selectedProductId != 0) {
            stock =  stockService().getOneProductAttributeStockById(selectedProductId);
            movementAttributeFluxForm.setSelectedProductStockId(selectedProductId);
        }else{
            if (movementAttributeFluxForm.getProductAttributeFluxId() != null){
              stock = stockService().getOneProductAttributeStockByAttribute(movementAttributeFluxForm.getProductAttribute(),OperationUtils.getUserLocation(), false);
            }
        }
        if (stock != null){
            modelMap.addAttribute("stock", stock);
            movementAttributeFluxForm.setProductId(stock.getProductAttribute().getProduct().getProductId());
            movementAttributeFluxForm.setBatchNumber(stock.getProductAttribute().getBatchNumber());
            movementAttributeFluxForm.setExpiryDate(stock.getProductAttribute().getExpiryDate());
            if (stock.getQuantityInStock() == 0) {
                modelMap.addAttribute("productMessage", "Ce produit est en rupture de stock");
            }
        }
        modelMappingForView(modelMap, movementAttributeFluxForm, productOperation, type);

    }

    private void modelMappingForView(ModelMap modelMap, MovementAttributeFluxForm movementAttributeFluxForm,
                                     ProductOperation productMovement, String type) {
        String movementTypes;

        if (type.equals("out")){
            movementTypes = service().getOneProductMovementOutById(productMovement.getProductOperationId()).getStockOutType().name();
            modelMap.addAttribute("stocks", stockService().getAllProductAttributeStocks(OperationUtils.getUserLocation(), false));
            modelMap.addAttribute("productMovement", service().getOneProductMovementOutById(productMovement.getProductOperationId()));
        }
        else {
            movementTypes = service().getOneProductMovementEntryById(productMovement.getProductOperationId()).getStockEntryType().name();
            modelMap.addAttribute("products", programService().getOneProductProgramById(productMovement.getProductProgram().getProductProgramId()).getProducts());
            modelMap.addAttribute("productMovement", service().getOneProductMovementEntryById(productMovement.getProductOperationId()));
        }
        modelMap.addAttribute("movementAttributeFluxForm", movementAttributeFluxForm);
        modelMap.addAttribute("type", type);
        if (!productMovement.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
            if (productMovement.getOperationStatus().equals(OperationStatus.VALIDATED)){
                modelMap.addAttribute("subTitle",   getTranslateType(movementTypes) +"- APPROUVEE");
            }
            else if (productMovement.getOperationStatus().equals(OperationStatus.AWAITING_VALIDATION)) {
                modelMap.addAttribute("subTitle", getTranslateType(movementTypes) +" - EN ATTENTE DE VALIDATION");
            }
            List<ProductAttributeFlux> productAttributeFluxes = attributeFluxService()
                    .getAllProductAttributeFluxByOperation(productMovement, false);
            if (productAttributeFluxes.size() != 0) {
                Collections.sort(productAttributeFluxes, Collections.<ProductAttributeFlux>reverseOrder());
            }
            modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
        } else {
            if (type.equals("entry")) {
                List<ProductAttributeFlux> productAttributeFluxes = attributeFluxService()
                        .getAllProductAttributeFluxByOperation(productMovement, false);
                if (productAttributeFluxes.size() != 0) {
                    Collections.sort(productAttributeFluxes, Collections.<ProductAttributeFlux>reverseOrder());
                }
                modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
            } else {
                List<ProductOutFluxDTO> productAttributeFluxes = pharmacyService().getProductOutFluxDTOs(productMovement);
                modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
            }

            if (type.equals("entry")) {
                StockEntryType stockEntryType = service().getOneProductMovementEntryById(productMovement.getProductOperationId()).getStockEntryType();
                modelMap.addAttribute("subTitle", getTranslateType(movementTypes) +" - Ajout de produits");
            } else {
                StockOutType stockOutType = service().getOneProductMovementOutById(productMovement.getProductOperationId()).getStockOutType();
                modelMap.addAttribute("subTitle", getTranslateType(movementTypes) + " <i class=\"fa fa-play\"></i> Ajout de produits");
            }
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/other/complete.form", method = RequestMethod.GET)
    public String complete(HttpServletRequest request,
                           @RequestParam(value = "movementId") Integer movementId,
                           @RequestParam(value = "type") String type){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        if (type.equals("out")){
            ProductMovementOut movementOut = service().getOneProductMovementOutById(movementId);
            movementOut.setOperationStatus(OperationStatus.AWAITING_VALIDATION);
            service().saveProductMovementOut(movementOut);
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Le Mouvement de sortiea été enregistré avec " +
                    "succès et est en attente de validation !");
        }
        else{
            ProductMovementEntry movementEntry = service().getOneProductMovementEntryById(movementId);
            movementEntry.setOperationStatus(OperationStatus.AWAITING_VALIDATION);
            service().saveProductMovementEntry(movementEntry);
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Le Mouvement d'entée a été enregistré avec " +
                    "succès et est en attente de validation !");
        }

        return "redirect:/module/pharmacy/operations/movement/other/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/other/incomplete.form", method = RequestMethod.GET)
    public String incomplete(HttpServletRequest request,
                             @RequestParam(value = "movementId") Integer movementId,
                             @RequestParam(value = "type") String type){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        if (type.equals("out")){
            ProductMovementOut movementOut = service().getOneProductMovementOutById(movementId);
            movementOut.setOperationStatus(OperationStatus.NOT_COMPLETED);
            service().saveProductMovementOut(movementOut);
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez continuer à modifier le Mouvement de sortie");
        }
        else{
            ProductMovementEntry movementEntry = service().getOneProductMovementEntryById(movementId);
            movementEntry.setOperationStatus(OperationStatus.NOT_COMPLETED);
            service().saveProductMovementEntry(movementEntry);
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez continuer à modifier le Mouvement d'entée");
        }
        return "redirect:/module/pharmacy/operations/movement/other/editFlux.form?type=" +type+ "&movementId=" + movementId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/other/delete.form", method = RequestMethod.GET)
    public String deleteOperation(HttpServletRequest request,
//                                  @RequestParam(value = "movementId") Integer movementId,
                                  @RequestParam(value = "id") Integer id,
                                  @RequestParam(value = "type") String type){
        if (!Context.isAuthenticated())
            return null;

        HttpSession session = request.getSession();
        if (type.equals("out")){
            ProductMovementOut movementOut = service().getOneProductMovementOutById(id);
            for (ProductAttributeFlux flux : productAttributeFluxService().getAllProductAttributeFluxByOperation(movementOut, false)){
                productAttributeFluxService().removeProductAttributeFlux(flux);
            }
            service().removeProductMovementOut(movementOut);
        } else {
            ProductMovementEntry movementEntry = service().getOneProductMovementEntryById(id);
            for (ProductAttributeFlux flux : productAttributeFluxService().getAllProductAttributeFluxByOperation(movementEntry, false)){
                productAttributeFluxService().removeProductAttributeFlux(flux);
            }
            service().removeProductMovementEntry(movementEntry);
        }
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Le movement de sortie a été supprimée avec succès !");
        return "redirect:/module/pharmacy/operations/movement/other/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/other/deleteFlux.form", method = RequestMethod.GET)
    public String deleteFlux(HttpServletRequest request,
                             @RequestParam(value = "movementId") Integer movementId,
                             @RequestParam(value = "fluxId") Integer fluxId,
                             @RequestParam(value = "type") String type){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductAttributeFlux flux = productAttributeFluxService().getOneProductAttributeFluxById(fluxId);
        if (flux != null) {
            productAttributeFluxService().removeProductAttributeFlux(flux);
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La ligne du produit a été supprimée avec succès !");
        }
        return "redirect:/module/pharmacy/operations/movement/other/editFlux.form?type="+ type +"&movementId=" + movementId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/other/validate.form", method = RequestMethod.GET)
    public String validate(HttpServletRequest request,
                           @RequestParam(value = "movementId") Integer movementId,
                           @RequestParam(value = "type") String type){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        if (type.equals("out")){
            ProductMovementOut movementOut = service().getOneProductMovementOutById(movementId);
            if (OperationUtils.validateOperation(movementOut)) {
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez " +
                        "continuer à modifier le movement de sortie !");
            }
        }
        else {
            ProductMovementEntry movementEntry = service().getOneProductMovementEntryById(movementId);
            if (OperationUtils.validateOperation(movementEntry)) {
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez " +
                        "continuer à modifier le movement d'entrée !");
            }
        }
        return "redirect:/module/pharmacy/operations/movement/other/list.form";
    }

    public Map<String, String> getEntryTypeLabels(){
        Map<String, String> entryTypeMap = new HashMap<String, String>();
        for (StockEntryType value : StockEntryType.values()){
            String label = "";
            if (value == StockEntryType.DONATION) {
                label = "DONS";
                //                case TRANSFER_IN: label = "Transfert Entrant";
//                    break;
//                case SITE_PRODUCT_BACK: label = "Retour de produit du site";
//                    break;
//                case POSITIVE_INVENTORY_ADJUSTMENT: label = "Ajustement inventaire positif";
//                    break;
                entryTypeMap.put(value.name(), label);
            }
        }
        return entryTypeMap;
    }
    public Map<String, String> getOutTypeLabels(){
        Map<String, String> outTypeMap = new HashMap<String, String>();
        for (StockOutType value : StockOutType.values()){
            String label = "";
            switch (value){
                case THIEF: label = "VOLS";
                    break;
                case DESTROYED: label = "Produits Endommagés";
                    break;
                case EXPIRED_PRODUCT: label = "Produits Perimés";
                    break;
                case SPOILED_PRODUCT: label = "Produits Avariés";
                    break;
//                case TRANSFER_OUT: label = "Transfert Sortant";
//                    break;
//                case NEGATIVE_INVENTORY_ADJUSTMENT: label = "Ajustement inventaire négatif";
//                    break;
                case OTHER_LOST: label = "Autres pertes";
                    break;
            }
            if (!label.isEmpty()) {
                outTypeMap.put(value.name(), label);
            }
        }
        return outTypeMap;
    }

    public String getTranslateType(String type) {
        String typeTranslate = "";

        if (type.equals("DONATION")){
            typeTranslate = "DON";
        }
        if (type.equals("THIEF")){
            typeTranslate = "Produits Volés";
        }
        if (type.equals("DESTROYED")){
            typeTranslate = "Produits Endommagés";
        }
        if (type.equals("EXPIRED_PRODUCT")){
            typeTranslate = "Produits Perimés";
        }
        if (type.equals("SPOILED_PRODUCT")){
            typeTranslate = "Produits Avariés";
        }
        if (type.equals("NEGATIVE_INVENTORY_ADJUSTMENT")){
            typeTranslate = "Ajustement inventaire négatif";
        }
        if (type.equals("OTHER_LOST")){
            typeTranslate = "Autres pertes";
        }
        if (type.equals("POSITIVE_INVENTORY_ADJUSTMENT")){
            typeTranslate = "Ajustement inventaire positif";
        }
        return typeTranslate;
    }
}
