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
package org.opens.tgol.orchestrator;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.opens.tanaguru.entity.parameterization.Parameter;
import org.opens.tgol.entity.contract.Act;
import org.opens.tgol.entity.contract.Contract;

/**
 *
 * @author jkowalczyk
 */
public interface TanaguruOrchestrator {

    /**
     *
     * @param contract
     * @param pageUrl
     * @param ipClient
     * @param paramSet
     * @param locale
     * @return
     */
    Act auditPage(Act act, 
            String pageUrl,
            Set<Parameter> paramSet, 
            Locale locale,
            String emailTo);
    
    /**
     *
     * @param contract
     * @param fileMap
     * @param ipClient
     * @param paramSet
     * @param locale
     * @return
     */
    Act auditPageUpload(
            Act act,
            Contract contract,
            Map <String, String> fileMap,
            Set<Parameter> paramSet, 
            Locale locale,
            String emailTo);

    /**
     *
     * @param contract
     * @param siteUrl
     * @param ipClient
     * @param paramSet
     * @param locale
     */
    Act auditSite(
            Act act,
            Contract contract,
            String siteUrl,
            Set<Parameter> paramSet, 
            Locale locale,
            String emailTo);

    /**
     *
     * @param contract
     * @param siteUrl
     * @param ipClient
     * @param paramSet
     * @param locale
     * @return
     */
    Act auditSite(
            Act act,
            Contract contract,
            String siteUrl,
            List<String> pageUrlList,
            Set<Parameter> paramSet, 
            Locale locale,
            String emailTo);
    
    /**
     * 
     * @param contract
     * @param scenarioName
     * @param scenario
     * @param clientIp
     * @param parameterSet
     * @param locale 
     */
    Act auditScenario(
            Act act,
            Contract contract,
            Long idScenario, 
            Set<Parameter> parameterSet, 
            Locale locale,
            String emailTo);
    
}