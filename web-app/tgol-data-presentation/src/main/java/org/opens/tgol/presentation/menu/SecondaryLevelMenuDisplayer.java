/*
 *  Tanaguru - Automated webpage assessment
 *  Copyright (C) 2008-2011  Open-S Company
 * 
 *  This file is part of Tanaguru.
 * 
 *  Tanaguru is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 * 
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *  Contact us by mail: open-s AT open-s DOT com
 */

package org.opens.tgol.presentation.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.opens.tgol.entity.contract.Contract;
import org.opens.tgol.entity.referential.Referential;
import org.opens.tgol.entity.service.contract.ContractDataService;
import org.opens.tgol.entity.user.User;
import org.opens.tgol.util.TgolKeyStore;
import org.springframework.ui.Model;

/**
 * Some pages need to display a second level menu. This class enables to determine
 * when this menu has to be displayed and populated a model with the 
 * appropriate data for the menu to be displayed
 * 
 * @author jkowalczyk
 */
public class SecondaryLevelMenuDisplayer {

    List<String> referentialWithModifiableTestWeight = new ArrayList<String>();
    public void setReferentialWithModifiableTestWeight(
            List<String> referentialWithModifiableTestWeight) {
        this.referentialWithModifiableTestWeight = referentialWithModifiableTestWeight;
    }
    
    private ContractDataService contractDataService;
    public ContractDataService getContractDataService() {
        return contractDataService;
    }

    public final void setContractDataService(ContractDataService contractDataService) {
        this.contractDataService = contractDataService;
    }

    /**
     * private constructor
     */
    public SecondaryLevelMenuDisplayer() {}

    /**
     * If the current user owns contracts with modifiable test weight 
     * referentials, the link to the modification pages has to be displayed. 
     * The different jsp checks the presence of the MODIFIABLE_TEST_WEIGHT_REFS_KEY
     * in the model to determine whether the menu has to be displayed and to 
     * get the needed data.
     * 
     * @param user
     * @param model
     * @return 
     */
    public void setModifiableReferentialsForUserToModel(
            User user, 
            Model model) {
        Collection<String> refList = new HashSet<String>();
        for(Contract contract : getContractDataService().getAllContractsByUser(user)) {
            for (Referential ref : contract.getReferentialSet()) {
                if (referentialWithModifiableTestWeight.contains(ref.getCode())) {
                    refList.add(ref.getCode());
                }
            }
        }
        if (!refList.isEmpty()) {
            model.addAttribute(TgolKeyStore.MODIFIABLE_TEST_WEIGHT_REFS_KEY, refList);
        }
    }

    /**
     * 
     * @param refCode
     * @return 
     */
    public boolean isRequestedReferentialModifiable(String refCode) {
        return referentialWithModifiableTestWeight.contains(refCode);
    }
    
}