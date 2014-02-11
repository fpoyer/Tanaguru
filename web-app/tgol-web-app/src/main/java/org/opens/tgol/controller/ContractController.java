package org.opens.tgol.controller;

/*
 * Tanaguru - Automated webpage assessment
 * Copyright (C) 2008-2011  Open-S Company
 *
 * This file is part of Tanaguru.
 *
 * Tanaguru is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact us by mail: open-s AT open-s DOT com
 */


import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.opens.tgol.entity.contract.Contract;
import org.opens.tgol.entity.contract.ContractImpl;
import org.opens.tgol.entity.functionality.Functionality;
import org.opens.tgol.exception.ForbiddenPageException;
import org.opens.tgol.exception.ForbiddenUserException;
import org.opens.tgol.util.TgolKeyStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;


/** 
 *
 * @author jkowalczyk
 */
@Controller
public class ContractController extends AbstractController {

    private LocaleResolver localeResolver;
    @Autowired
    public final void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    private List<String> authorizedFunctionalityForTrend = new ArrayList<String>();
    public List<String> getAuthorizedFunctionalityForTrend() {
        return authorizedFunctionalityForTrend;
    }

    public void setAuthorizedFunctionalityForTrend(List<String> authorizedFunctionalityForTrend) {
        this.authorizedFunctionalityForTrend = authorizedFunctionalityForTrend;
    }

//    private ActionHandler actionHandler;
//    public ActionHandler getActionHandler() {
//        return actionHandler;
//    }
//
//    public void setActionHandler(ActionHandler contractActionHandler) {
//        this.actionHandler = contractActionHandler;
//    }

    public ContractController() {
        super();
    }

    /**
     * 
     * @param contractId
     * @param request
     * @param response
     * @param model
     * @return 
     */
    @RequestMapping(value=TgolKeyStore.CONTRACT_URL, method=RequestMethod.GET)
    @Secured({TgolKeyStore.ROLE_USER_KEY, TgolKeyStore.ROLE_ADMIN_KEY})
    public String displayContractPage (
            @RequestParam(TgolKeyStore.CONTRACT_ID_KEY) String contractId,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) {
        Long contractIdValue;
        try {
            contractIdValue = Long.valueOf(contractId);
        } catch (NumberFormatException nfe) {
            throw new ForbiddenPageException(getCurrentUser());
        }
        if (!currentUserCanRead(contractIdValue)) {
            throw new ForbiddenUserException(getCurrentUser());
        }
        return displayContractPage(request, model, contractIdValue);
    }
    
    private boolean currentUserCanRead(Long contractId) {
        List<Permission> permissions = new ArrayList<Permission>(1);
        permissions.add(BasePermission.READ);
        return isHoldingPermissions(getCurrentSid(), permissions, new ObjectIdentityImpl(ContractImpl.class, contractId));
    }

    /**
     * 
     * @param request
     * @param model
     * @param contractId
     * @return 
     */
    private String displayContractPage(
            HttpServletRequest request,
            Model model,
            Long contractId) {
        model.addAttribute(TgolKeyStore.LOCALE_KEY,localeResolver.resolveLocale(request));
        Contract contract = getContractDataService().read(contractId);
        if (isContractExpired(contract)) {
            throw new ForbiddenUserException(getCurrentUser());
        }
        // add the action list to the view
//        model.addAttribute(TgolKeyStore.CONTRACT_ACTION_LIST_KEY, actionHandler.getActionList(contract));
        if (isContractHasFunctionalityAllowingTrend(contract)) {
            model.addAttribute(TgolKeyStore.DISPLAY_RESULT_TREND_KEY, true);
        }
        return displayContractView(contract, model);
    }

    /**
     * We iterate through the list of functionalities associated with the contract
     * to determine whether a trend has to be displayed. 
     * @param contract
     * @return 
     */
    private boolean isContractHasFunctionalityAllowingTrend(Contract contract) {
        for (Functionality functionality : contract.getFunctionalitySet()) {
            if (authorizedFunctionalityForTrend.contains(functionality.getCode())) {
                return true;
            }
        }
        return false;
    }

}