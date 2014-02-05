package org.opens.tgol.controller;

import java.beans.PropertyEditorSupport;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.opens.tgol.command.CreateContractCommand;
import org.opens.tgol.command.factory.CreateContractCommandFactory;
import org.opens.tgol.entity.contract.Contract;
import org.opens.tgol.entity.functionality.Functionality;
import org.opens.tgol.entity.referential.Referential;
import org.opens.tgol.entity.service.functionality.FunctionalityDataService;
import org.opens.tgol.entity.service.referential.ReferentialDataService;
import org.opens.tgol.entity.user.User;
import org.opens.tgol.form.parameterization.ContractOptionFormField;
import org.opens.tgol.form.parameterization.helper.ContractOptionFormFieldHelper;
import org.opens.tgol.util.TgolKeyStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/admin/contract")
@SessionAttributes(TgolKeyStore.CREATE_CONTRACT_COMMAND_KEY)
public class AddController extends AbstractUserAndContractsController {

    @ModelAttribute(TgolKeyStore.USER_LIST_KEY)
    public Collection<User> getAllUsers() {
        return getUserDataService().findAll();
    }

    @ModelAttribute(TgolKeyStore.OPTIONS_MAP_KEY)
    public Map<String, List<ContractOptionFormField>> getOptionsMap() {
        return ContractOptionFormFieldHelper
                .getFreshContractOptionFormFieldMap();
    }

    @Autowired
    private FunctionalityDataService functionalityDataService;

    @ModelAttribute("functionalityMap")
    public Map<String, Boolean> getFunctionalityList() {
        Map<String, Boolean> functMap = new LinkedHashMap<String, Boolean>();
        for (Functionality funct : functionalityDataService.findAll()) {
            functMap.put(funct.getCode(), Boolean.FALSE);
        }
        return functMap;
    }

    @Autowired
    private ReferentialDataService referentialDataService;

    @ModelAttribute("referentialMap")
    public Map<String, Boolean> getReferentialList() {
        Map<String, Boolean> refMap = new LinkedHashMap<String, Boolean>();

        for (Referential ref : referentialDataService.findAll()) {
            refMap.put(ref.getCode(), Boolean.FALSE);
        }
        return refMap;
    }

    @InitBinder
    @Override
    protected void initBinder(org.springframework.web.bind.WebDataBinder binder) {
        super.initBinder(binder);
        binder.registerCustomEditor(List.class, "readers",
                new CustomCollectionEditor(List.class, true) {

                    @Override
                    protected Object convertElement(Object element) {
                        Long id = null;

                        if (element instanceof String
                                && !((String) element).equals("")) {
                            // From the JSP 'element' will be a String
                            try {
                                id = Long.parseLong((String) element);
                            } catch (NumberFormatException e) {
                                Logger.getLogger(this.getClass()).warn(
                                        "Element was " + ((String) element));
                            }
                        } else if (element instanceof Long) {
                            // From the database 'element' will be a Long
                            id = (Long) element;
                        }
                        Logger.getLogger(this.getClass()).debug("convert reader : " + element + " into ID : " + id);
                        return id != null ? getUserDataService().read(id)
                                : null;
                    }
                });
        binder.registerCustomEditor(User.class, "owner",
                new PropertyEditorSupport() {

                    @Override
                    public void setAsText(String element) {
                        Long id = null;

                        if (!element.isEmpty()) {
                            // From the JSP 'element' will be a String
                            try {
                                id = Long.parseLong((String) element);
                            } catch (NumberFormatException e) {
                                Logger.getLogger(this.getClass()).warn(
                                        "Element was " + ((String) element));
                            }
                        }

                        setValue(id != null ? getUserDataService().read(id)
                                : null);
                    }
                });
    };

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @Secured(TgolKeyStore.ROLE_ADMIN_KEY)
    public String getContract(HttpServletRequest request,
            HttpServletResponse response, Model model) {

        CreateContractCommand createContractCommand = CreateContractCommandFactory
                .getInstance().getNewCreateContractCommand();

        model.addAttribute(TgolKeyStore.CREATE_CONTRACT_COMMAND_KEY,
                createContractCommand);
        return "add-contract-acl";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @Secured(TgolKeyStore.ROLE_ADMIN_KEY)
    public String postContract(
            @ModelAttribute(TgolKeyStore.CREATE_CONTRACT_COMMAND_KEY) CreateContractCommand createContractCommand,
            BindingResult result, HttpServletRequest request,
            HttpServletResponse response, Model model, SessionStatus status) {

        // We check whether the form is valid
        getCreateContractFormValidator()
                .validate(createContractCommand, result);

        // If the form has some errors, we display it again with errors' details
        if (result.hasErrors()) {
            model.addAttribute(
                    TgolKeyStore.CREATE_CONTRACT_COMMAND_KEY,
                    CreateContractCommandFactory.getInstance()
                            .getInitialisedCreateContractCommand(
                                    createContractCommand));

            return "add-contract-acl";
        }

        // Create actual contract
        Contract contract = getContractDataService().create();
        contract = CreateContractCommandFactory.getInstance()
                .updateContractFromCommand(createContractCommand, contract);
        getContractDataService().saveOrUpdate(contract);

        // Set default admin ACLs to ROLE_ADMIN
        setAdminPermissions(contract);

        // Add ACLs read rights to selected Users
        Collection<User> users = createContractCommand.getReaders();
        setReadPermission(users, contract);

        // WRITE/CREATE/DELETE/ADMIN rights for owner
        setWritePermission(createContractCommand.getOwner(), contract);

        request.getSession().setAttribute(TgolKeyStore.ADDED_CONTRACT_NAME_KEY,
                contract.getLabel());

        // Remove session data not needed anymore.
        status.setComplete();

        return TgolKeyStore.MANAGE_CONTRACTS_VIEW_REDIRECT_NAME;
    }

    /**
     * Gives all (base) permissions to ROLE_ADMIN granted authority on the
     * passed contract.
     * 
     * @param contract
     */
    private void setAdminPermissions(Contract contract) {
        ObjectIdentityImpl objectId = new ObjectIdentityImpl(contract);
        MutableAcl acl = getAclService().createAcl(objectId);

        Sid adminSid = new GrantedAuthoritySid("ROLE_ADMIN");
        int index = 0;
        acl.insertAce(index++, BasePermission.CREATE, adminSid, true);
        acl.insertAce(index++, BasePermission.READ, adminSid, true);
        acl.insertAce(index++, BasePermission.WRITE, adminSid, true);
        acl.insertAce(index++, BasePermission.DELETE, adminSid, true);
        acl.insertAce(index++, BasePermission.ADMINISTRATION, adminSid, true);

        // Write the new ACL in base
        getAclService().updateAcl(acl);
    }

    /**
     * Gives read (base) permission to passed users on the passed contract.
     * 
     * @param contract
     */
    private void setReadPermission(Collection<User> users, Contract contract) {
        ObjectIdentityImpl objectId = new ObjectIdentityImpl(contract);
        // Guaranteed to never throw NotFoundException because at least admin
        // right have been set before.
        MutableAcl acl = (MutableAcl) getAclService().readAclById(objectId);

        for (User user : users) {
            Sid sid = new PrincipalSid(user.getEmail1());
            acl.insertAce(acl.getEntries().size(), BasePermission.READ, sid,
                    true);
        }

        // Write the new ACL in base
        getAclService().updateAcl(acl);
    }

    /**
     * Gives write/create/delete/admin (base) permission to passed user on the
     * passed contract.
     * 
     * @param user
     * @param contract
     */
    private void setWritePermission(User user, Contract contract) {
        ObjectIdentityImpl objectId = new ObjectIdentityImpl(contract);
        // Guaranteed to never throw NotFoundException because at least admin
        // right have been set before.
        MutableAcl acl = (MutableAcl) getAclService().readAclById(objectId);

        Sid sid = new PrincipalSid(user.getEmail1());
        acl.setOwner(sid);
        acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, sid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.CREATE, sid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.DELETE, sid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION,
                sid, true);

        // Write the new ACL in base
        getAclService().updateAcl(acl);
    }

}
