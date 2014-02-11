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
package org.opens.tgol.entity.service.contract;

import java.util.Collection;
import org.opens.tanaguru.sdk.entity.service.GenericDataService;
import org.opens.tgol.entity.contract.Contract;
import org.opens.tgol.entity.user.User;

/**
 *
 * @author jkowalczyk
 */
public interface ContractDataService extends GenericDataService<Contract, Long> {

    /**
     * The Url associated with the contract is an option. We need to iterate 
     * through the options of the contract to retrieve the one that handles 
     * the Url. If this option is missing, an empty String is returned.
     * 
     * @param contract
     * @return 
     */
    String getUrlFromContractOption(Contract contract);
    
    Collection<Contract> searchAll();

}