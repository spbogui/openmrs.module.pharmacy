package org.openmrs.module.pharmacy.web.controller;

import org.hibernate.HibernateException;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductAttribute;
import org.openmrs.module.pharmacy.ProductAttributeFlux;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductAttributeFluxService;
import org.openmrs.module.pharmacy.api.ProductAttributeService;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.Collection;
import java.util.List;

@Controller
public class AjaxCustomController {

    private PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }
    private ProductAttributeFluxService attributeFluxService() {
        return Context.getService(ProductAttributeFluxService.class);
    }
    private ProductAttributeService attributeService() {
        return Context.getService(ProductAttributeService.class);
    }

    private LocationService locationService() {
        return Context.getLocationService();
    }
    private ProductProgramService programService() {
        return Context.getService(ProductProgramService.class);
    }

    @ResponseBody
    @RequestMapping(value = "search/api/getSearchResult")
    public String getProductQuantityInStock() {

        return "il n'y a rien pour l'instant";
    }

    @RequestMapping(value = "save-flux.form", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> saveAttributeFluxAjax(
            @RequestParam("batchNumber") String batchNumber,
            @RequestParam("operationId") Integer operationId,
            @RequestParam("quantity") Integer quantity) {

        ProductAttribute attribute = attributeService().getOneProductAttributeByBatchNumber(batchNumber, OperationUtils.getUserLocation());
        ProductAttributeFlux flux = attributeFluxService().getOneProductAttributeFluxByAttributeAndOperation(attribute, service().getOneProductOperationById(operationId));
        flux.setQuantity(quantity);
        return new ResponseEntity<String>(attributeFluxService().saveProductAttributeFlux(flux).getQuantity().toString(), HttpStatus.OK);
    }


    @RequestMapping(value = "save-observation.form", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> saveObservationAjax(
            @RequestParam("batchNumber") String batchNumber,
            @RequestParam("operationId") Integer operationId,
            @RequestParam("observation") String observation) {

        ProductAttribute attribute = attributeService().getOneProductAttributeByBatchNumber(batchNumber, OperationUtils.getUserLocation());
        ProductAttributeFlux flux = attributeFluxService().getOneProductAttributeFluxByAttributeAndOperation(attribute, service().getOneProductOperationById(operationId));
        flux.setObservation(observation);
        return new ResponseEntity<String>(attributeFluxService().saveProductAttributeFlux(flux).getObservation(), HttpStatus.OK);
    }

    @RequestMapping(value = "save-location-program.form", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> saveLocationAttributeAjax(
            @RequestParam("locationId") Integer locationId,
            @RequestParam("programIds") String programIds) {
        Location location = locationService().getLocation(locationId);
        LocationAttribute locationAttribute = new LocationAttribute();
        for (LocationAttribute attribute : location.getActiveAttributes()) {
            if (attribute.getAttributeType().getName().equals("Programmes Disponibles")) {
                locationAttribute = attribute;
                break;
            }
        }

        if (locationAttribute.getLocation() == null) {
            //locationAttribute.setLocation(location);
            locationAttribute.setAttributeType(locationService()
                    .getLocationAttributeTypeByUuid("AVAILPRGRMCCCCCCCCCCCCCCCCCCCCCCCCCC"));
        }
        StringBuilder programs = new StringBuilder();
        String[] programsIdList = programIds.split(",");

        for (int i = 0; i < programsIdList.length; i++) {
            programs.append(programService().getOneProductProgramById(Integer.parseInt(programsIdList[i])).getName());
            if ((i + 1) != programsIdList.length) {
                programs.append(",");
            }
        }
        locationAttribute.setValue(programs.toString());
        location.addAttribute(locationAttribute);

        locationService().saveLocation(location);

        return new ResponseEntity<String>("Location attribute saved : [" + programs.toString() + "]", HttpStatus.OK);
    }

    @RequestMapping(value = "save-location.form", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> saveLocationAjax(
            @RequestParam("name") String name,
            @RequestParam("prefix") String prefix) {
        Location location = locationService().getLocation(name);
        System.out.println("Prefix -----------------------------------------> " + prefix);
        String result = "";
        if (location == null) {
            location = new Location();
            location.setName(name);
        }
        location.setPostalCode(prefix);
        location.setParentLocation(OperationUtils.getUserLocation());
        Location locationSaved = locationService().saveLocation(location);

        StringBuilder programList = new StringBuilder();
        for (ProductProgram program : programService().getAllProductProgram())
        {
            if (OperationUtils.getLocationPrograms(locationSaved).contains(program)) {
                programList.append("\t\t\t<option value=\"").append(program.getProductProgramId()).append("\" selected=\"selected\">").append(program.getName()).append("</option>\n");
            } else {
                programList.append("\t\t\t<option value=\"").append(program.getProductProgramId()).append("\">").append(program.getName()).append("</option>\n");
            }
        }
        result = "<tr id=\"location-"+ location.getLocationId() + "\">" +
                    "\t<td>" + name + " (" + locationSaved.getPostalCode() + ") </td>" +
                    "\t<td>" +
                        "\t\t<select id=\"" + locationSaved.getLocationId() + "\" class=\"form-control s2\" multiple=\"multiple\">" +
                            "\t\t\t<option value=\"\"></option>" +
                            programList.toString() +
                        "\t\t</select>" +
                    "\t</td>" +
                    "\t<td><button type=\"button\" class=\"btn btn-sm btn-danger\" onclick=\"retireLocation("+ location.getLocationId()
                        +"\"><i class=\"fa fa-trash\"></i> Retirer</button></td>" +
                "</tr>";



        return new ResponseEntity<String>(result, HttpStatus.OK);
    }


    @RequestMapping(value = "retire-location.form", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> retireLocationAjax(
            @RequestParam("locationId") Integer locationId) {
        Location location = locationService().getLocation(locationId);

        if (location != null) {
            location.setParentLocation(null);
            Context.getLocationService().retireLocation(location, "Not in use at all");
            try {
                Context.getLocationService().purgeLocation(location);
            } catch (HibernateException e) {
                System.out.println(e.getMessage());
            }
        }

        return new ResponseEntity<String>("Location is retired successfully", HttpStatus.OK);
    }


    @RequestMapping(value = "save-location-client-info.form", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> saveLocationClientInfoAjax(
            @RequestParam("value") Boolean value,
            @RequestParam("locationId") Integer locationId) {
        Location location = locationService().getLocation(locationId);

        if (location != null) {
            LocationAttribute locationAttribute = null;
            for (LocationAttribute attribute : location.getActiveAttributes()) {
                if (attribute.getAttributeType().getName().equals("Client Direct NPSP")) {
                    locationAttribute = attribute;
                    break;
                }
            }
            if (locationAttribute == null) {
                locationAttribute = new LocationAttribute();
                locationAttribute.setAttributeType(Context.getLocationService()
                        .getLocationAttributeTypeByUuid("NPSPCLIENTCCCCCCCCCCCCCCCCCCCCCCCCCC"));
            }
//            if (value.equals("true"))
//                locationAttribute.setValue(value);
//            else
                locationAttribute.setValue(value);
            location.addAttribute(locationAttribute);
            locationService().saveLocation(location);
        }

        return new ResponseEntity<String>("Location is retired successfully", HttpStatus.OK);
    }
}
