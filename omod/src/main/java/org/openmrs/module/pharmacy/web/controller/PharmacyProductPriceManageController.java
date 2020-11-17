package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductPrice;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.forms.ProductPriceForm;
import org.openmrs.module.pharmacy.forms.validations.ProductPriceFormValidation;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PharmacyProductPriceManageController {

    protected final Log log = LogFactory.getLog(getClass());

    private PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }

    @RequestMapping(value = "/module/pharmacy/product/prices/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("prices", service().getAllProductPrices());
            modelMap.addAttribute("title", "Liste des prix");
        }
    }

    @RequestMapping(value = "/module/pharmacy/product/prices/delete.form", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                         HttpServletRequest request) {
        if (!Context.isAuthenticated())
            return null;

        if (id != 0) {
            HttpSession session = request.getSession();
            ProductPrice productPrice = service().getOneProductPriceById(id);
            if (productPrice != null) {
                service().removeProductPrice(productPrice);
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "prix ajouté avec succès");
            }
        }
        return "redirect:/module/pharmacy/product/prices/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/product/prices/edit.form", method = RequestMethod.GET)
    public void edit(ModelMap modelMap,
                         @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                     ProductPriceForm productPriceForm ) {
        if (Context.isAuthenticated()) {

            if (id != 0) {
                productPriceForm.setProductPrice(service().getOneProductPriceById(id));
            } else {
                productPriceForm = new ProductPriceForm();
            }

            modelMap.addAttribute("priceForm", productPriceForm);
            modelMap.addAttribute("availableProduct", service().getAllProduct());
            modelMap.addAttribute("availablePrograms", service().getAllProductProgram());
            modelMap.addAttribute("title", "Formulaire de saisie des prix");
        }
    }

    @RequestMapping(value = "/module/pharmacy/product/prices/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                            HttpServletRequest request,
                       ProductPriceForm priceForm,
                            BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductPriceFormValidation().validate(priceForm, result);

            if (!result.hasErrors()) {
                boolean idExist = (priceForm.getProductPriceId() != null);
                service().saveProductPrice(priceForm.getProductPrice());
                if (!idExist) {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Prix ajouté avec succès");
                } else {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Prix modifié avec succès");
                }
                return "redirect:/module/pharmacy/product/prices/list.form";
            }
            modelMap.addAttribute("priceForm", priceForm);
            modelMap.addAttribute("availableProduct", service().getAllProduct());
            modelMap.addAttribute("availablePrograms", service().getAllProductProgram());
            modelMap.addAttribute("title", "Formulaire de saisie des Prix");
        }

        return null;
    }
}
