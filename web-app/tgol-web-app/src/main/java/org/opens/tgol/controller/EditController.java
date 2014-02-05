package org.opens.tgol.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opens.tgol.command.CreateContractCommand;
import org.opens.tgol.command.factory.CreateContractCommandFactory;
import org.opens.tgol.entity.contract.Contract;
import org.opens.tgol.exception.ForbiddenPageException;
import org.opens.tgol.exception.ForbiddenUserException;
import org.opens.tgol.form.parameterization.ContractOptionFormField;
import org.opens.tgol.form.parameterization.helper.ContractOptionFormFieldHelper;
import org.opens.tgol.util.TgolKeyStore;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/edit")
public class EditController extends AbstractUserAndContractsController {
    
    @ModelAttribute("RefMap")
    public Map<String, Boolean> getRefMap(@RequestParam("cr") int contractId) {
        return null;
    }
    
    @RequestMapping(value = "/contract", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#contractId, 'org.opens.tgol.entity.contract.ContractImpl', 'write') or hasPermission(#contractId, 'org.opens.tgol.entity.contract.ContractImpl', 'admin')")
    public String getContract(
            @RequestParam(TgolKeyStore.CONTRACT_ID_KEY) String contractId,
            HttpServletRequest request, HttpServletResponse response,
            Model model) {

        Long lContractId;
        try {
            lContractId = Long.valueOf(contractId);
        } catch (NumberFormatException nfe) {
            throw new ForbiddenUserException();
        }

        Contract contract = getContractDataService().read(lContractId);
        if (contract == null) {
            throw new ForbiddenPageException();
        }
        request.getSession().setAttribute(TgolKeyStore.CONTRACT_ID_KEY,
                contract.getId());

        CreateContractCommand createContractCommand;

        createContractCommand = CreateContractCommandFactory.getInstance()
                .getInitialisedCreateContractCommand(contract);
        model.addAttribute(TgolKeyStore.CONTRACT_NAME_KEY,
                createContractCommand.getLabel());

        model.addAttribute(TgolKeyStore.USER_LIST_KEY, getUserDataService()
                .findAll());

        model.addAttribute(TgolKeyStore.CREATE_CONTRACT_COMMAND_KEY,
                createContractCommand);
        model.addAttribute(
                TgolKeyStore.OPTIONS_MAP_KEY,
                ContractOptionFormFieldHelper
                        .getFreshContractOptionFormFieldMap());

        return "edit-contract-acl";

    }

    @RequestMapping(value = "/contract", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#contractId, 'org.opens.tgol.entity.contract.ContractImpl', 'write') or hasPermission(#contractId, 'org.opens.tgol.entity.contract.ContractImpl', 'admin')")
    public String postContract(
            @ModelAttribute(TgolKeyStore.CREATE_CONTRACT_COMMAND_KEY) CreateContractCommand createContractCommand,
            BindingResult result, HttpServletRequest request,
            HttpServletResponse response, Model model) {

        Object contractId = request.getSession().getAttribute(
                TgolKeyStore.CONTRACT_ID_KEY);
        Long lContractId;
        if (contractId instanceof Long) {
            lContractId = (Long) contractId;
        } else {
            try {
                lContractId = Long.valueOf(contractId.toString());
            } catch (NumberFormatException nfe) {
                throw new ForbiddenUserException();
            }
        }

        Contract contract = getContractDataService().read(lContractId);
        Map<String, List<ContractOptionFormField>> optionFormFieldMap = ContractOptionFormFieldHelper
                .getFreshContractOptionFormFieldMap();

        getCreateContractFormValidator().setContractOptionFormFieldMap(
                optionFormFieldMap);
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
            model.addAttribute(TgolKeyStore.OPTIONS_MAP_KEY, optionFormFieldMap);
            model.addAttribute(TgolKeyStore.USER_LIST_KEY, getUserDataService()
                    .findAll());
            return TgolKeyStore.EDIT_CONTRACT_VIEW_NAME;
        }

        contract = CreateContractCommandFactory.getInstance()
                .updateContractFromCommand(createContractCommand, contract);
        getContractDataService().saveOrUpdate(contract);

        request.getSession().setAttribute(
                TgolKeyStore.UPDATED_CONTRACT_NAME_KEY, contract.getLabel());
        request.getSession().removeAttribute(TgolKeyStore.CONTRACT_ID_KEY);
        return TgolKeyStore.MANAGE_CONTRACTS_VIEW_REDIRECT_NAME;
    }
}
