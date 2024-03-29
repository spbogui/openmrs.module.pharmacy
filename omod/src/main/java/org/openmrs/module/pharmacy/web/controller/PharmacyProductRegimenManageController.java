package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.entities.ProductRegimen;
import org.openmrs.module.pharmacy.api.ProductRegimenService;
import org.openmrs.module.pharmacy.api.ProductService;
import org.openmrs.module.pharmacy.forms.product.ProductRegimenForm;
import org.openmrs.module.pharmacy.dto.ProductUploadResumeDTO;
import org.openmrs.module.pharmacy.utils.CSVHelper;
import org.openmrs.module.pharmacy.forms.product.validators.ProductRegimenFormValidation;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PharmacyProductRegimenManageController {
    protected final Log log = LogFactory.getLog(getClass());

    private ProductRegimenService service() {
        return Context.getService(ProductRegimenService.class);
    }
    private ProductService productService() { return Context.getService(ProductService.class);}

    @RequestMapping(value = "/module/pharmacy/product/regimens/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("regimens", service().getAllProductRegimen());
            modelMap.addAttribute("title", "Liste des Régimes");
        }
    }

    @RequestMapping(value = "/module/pharmacy/product/regimens/delete.form", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                         HttpServletRequest request) {
        if (!Context.isAuthenticated())
            return null;

        if (id != 0) {
            HttpSession session = request.getSession();
            ProductRegimen productRegimen = service().getOneProductRegimenById(id);
            if (productRegimen != null) {
                service().removeProductRegimen(productRegimen);
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Regime supprimé avec succès");
            }
        }

        return "redirect:/module/pharmacy/product/regimens/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/product/regimens/edit.form", method = RequestMethod.GET)
    public void edit(ModelMap modelMap,
                     @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                     ProductRegimenForm productRegimenForm) {
        if (Context.isAuthenticated()) {
            if (id != 0) {
                productRegimenForm.setProductRegimen(service().getOneProductRegimenById(id));
            } else {
                productRegimenForm = new ProductRegimenForm();
            }

            modelMap.addAttribute("regimenForm", productRegimenForm);
            modelMap.addAttribute("conceptList", getConcepts());
            modelMap.addAttribute("products", productService().getAllProduct());
            modelMap.addAttribute("title", "Formulaire de saisie des Regimes");
        }
    }

    @RequestMapping(value = "/module/pharmacy/product/regimens/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                       HttpServletRequest request,
                       ProductRegimenForm regimenForm,
                       BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductRegimenFormValidation().validate(regimenForm, result);

            if (!result.hasErrors()) {
                boolean idExist = (regimenForm.getProductRegimenId() != null);
                service().saveProductRegimen(regimenForm.getProductRegimen());
                if (!idExist) {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Regimenme ajouté avec succès");
                } else {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Regimenme modifié avec succès");
                }
                return "redirect:/module/pharmacy/product/regimens/list.form";
            }
            modelMap.addAttribute("regimenForm", regimenForm);
            modelMap.addAttribute("products", productService().getAllProduct());
            modelMap.addAttribute("conceptList", getConcepts());
            modelMap.addAttribute("title", "Formulaire de saisie des Regimes");
        }

        return null;
    }

    @RequestMapping(value = "/module/pharmacy/product/regimens/upload.form", method = RequestMethod.POST)
    public String uploadProduct(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();
            String message = "";
            if (CSVHelper.hasCSVFormat(file)) {
                try {
                    ProductUploadResumeDTO resumeDTO = productService().uploadProductRegimens(file);
                    message = "Produits importés avec succès : " + file.getOriginalFilename();
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, message + ". [" + resumeDTO.toString() + "]");
                } catch (Exception e) {
                    message = "Could not upload the file : " + file.getOriginalFilename() + " : "  + e.getMessage();
                    System.out.println("---------------------" + e.getMessage());
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, message);
                }
            } else
                session.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "S'il vous plait importez un fichier CSV !");
            return "redirect:/module/pharmacy/product/regimens/list.form";
        }
        return null;
    }

    private Map<Integer, String> getConceptMapList() {
        String conceptId = Context.getAdministrationService().getGlobalProperty("pharmacy.regimenConcept");
        List<Concept> concepts = getConceptFromRegimen(service().getAllProductRegimen());
        Map<Integer, String> conceptList = new LinkedHashMap<Integer, String>();

        for (ConceptAnswer conceptAnswer :
                Context.getConceptService().getConcept(Integer.parseInt(conceptId)).getAnswers()) {
            if (!concepts.contains(conceptAnswer.getAnswerConcept())) {
                conceptList.put(conceptAnswer.getAnswerConcept().getConceptId(), conceptAnswer.getAnswerConcept().getName().getName());
            }
        }
        return conceptList;
    }

    private List<Concept> getConcepts() {
        String conceptId = Context.getAdministrationService().getGlobalProperty("pharmacy.regimenConcept");
        List<Concept> concepts = new ArrayList<Concept>();
        for (ConceptAnswer answer : Context.getConceptService().getConcept(Integer.parseInt(conceptId)).getAnswers()) {
            concepts.add(answer.getAnswerConcept());
        }
        concepts.add(Context.getConceptService().getConcept(105281));
        return concepts;
    }

    List<Concept> getConceptFromRegimen(List<ProductRegimen> regimenList) {
        List<Concept> concepts = new ArrayList<Concept>();
        for (ProductRegimen regimen : regimenList) {
            concepts.add(regimen.getConcept());
        }
        return concepts;
    }
}
