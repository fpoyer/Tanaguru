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
package org.opens.tanaguru.entity.dao.parameterization;

import java.util.Collection;
import java.util.Set;
import org.opens.tanaguru.entity.audit.Audit;
import org.opens.tanaguru.entity.parameterization.Parameter;
import org.opens.tanaguru.entity.parameterization.ParameterElement;
import org.opens.tanaguru.entity.parameterization.ParameterFamily;
import org.opens.tanaguru.sdk.entity.dao.GenericDAO;

/**
 * 
 * @author jkowalczyk
 */
public interface ParameterDAO extends GenericDAO<Parameter, Long> {

    /**
     *
     * @param parameterElement
     * @param audit
     * @return
     *      a parameter from a parameterElement and an audit
     */
    Parameter findParameter(ParameterElement parameterElement, String value);
    
    /**
     * 
     * @param audit
     * @param parameterElementCode
     * @return 
     */
    Parameter findParameter(Audit audit, String parameterElementCode);

    /**
     *
     * @param parameterFamily
     * @param audit
     * @return
     *      a collection of parameters for given parameterFamily and audit
     */
    Set<Parameter> findParameterSet(ParameterFamily parameterFamily, Audit audit);

    /**
     *
     * @return
     *      the collection of default parameters
     */
    Set<Parameter> findDefaultParameterSet();

    /**
     *
     * @param audit
     * @return
     *      the collection of parameters for a given audit
     */
    Set<Parameter> findParameterSetFromAudit(Audit audit);

    /**
     * 
     * @param parameterElement
     * @return
     *      the default parameter for a given parameterElement
     */
    Parameter findDefaultParameter(ParameterElement parameterElement);
    
    /**
     * 
     * @param parameterFamily
     * @param paramSet
     * @return 
     */
    Set<Parameter> findParametersFromParameterFamily(
            ParameterFamily parameterFamily, 
            Collection<Parameter> paramSet);

}