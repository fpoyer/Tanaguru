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
package org.opens.tgol.form.parameterization.helper;

import java.util.*;
import org.opens.tgol.form.parameterization.ContractOptionFormField;
import org.opens.tgol.form.parameterization.builder.ContractOptionFormFieldBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * That class handles utility methods to deal with ContractOptionFormFields. 
 * 
 * @author jkowalczyk
 */
@Component
public final class ContractOptionFormFieldHelper {

    @Autowired
    private ContractOptionFormFieldHelper(
            @Qualifier("nbMaxAuditsPerContractOptionBuilder") ContractOptionFormFieldBuilder nbMaxAuditsPerContractOptionBuilder,
            @Qualifier("nbMaxActsToDisplayPerContractOptionBuilder") ContractOptionFormFieldBuilder nbMaxActsToDisplayPerContractOptionBuilder,
            @Qualifier("depthOptionBuilder") ContractOptionFormFieldBuilder depthOptionBuilder,
            @Qualifier("maxDurationOptionBuilder") ContractOptionFormFieldBuilder maxDurationOptionBuilder,
            @Qualifier("maxDocumentsOptionBuilder") ContractOptionFormFieldBuilder maxDocumentsOptionBuilder,
            @Qualifier("exclusionRegexpOptionBuilder") ContractOptionFormFieldBuilder exclusionRegexpOptionBuilder) {
        ContractOptionFormFieldHelper._instance = this;
        init(nbMaxAuditsPerContractOptionBuilder,
                nbMaxActsToDisplayPerContractOptionBuilder,
                depthOptionBuilder,
                maxDurationOptionBuilder,
                maxDocumentsOptionBuilder,
                exclusionRegexpOptionBuilder);
    }

    private static final Map<String, List<ContractOptionFormFieldBuilder>> defaultOptionFormFieldBuilderMap = new HashMap<String, List<ContractOptionFormFieldBuilder>>();

    private void init(
            ContractOptionFormFieldBuilder nbMaxAuditsPerContractOptionBuilder,
            ContractOptionFormFieldBuilder nbMaxActsToDisplayPerContractOptionBuilder,
            ContractOptionFormFieldBuilder depthOptionBuilder,
            ContractOptionFormFieldBuilder maxDurationOptionBuilder,
            ContractOptionFormFieldBuilder maxDocumentsOptionBuilder,
            ContractOptionFormFieldBuilder exclusionRegexpOptionBuilder) {
        
        List<ContractOptionFormFieldBuilder> generalOptions = new ArrayList<ContractOptionFormFieldBuilder>();
        generalOptions.add(nbMaxAuditsPerContractOptionBuilder);
        generalOptions.add(nbMaxActsToDisplayPerContractOptionBuilder);
        defaultOptionFormFieldBuilderMap.put("general-options", generalOptions);
        
        List<ContractOptionFormFieldBuilder> crawlOptions = new ArrayList<ContractOptionFormFieldBuilder>();
        crawlOptions.add(depthOptionBuilder);
        crawlOptions.add(maxDurationOptionBuilder);
        crawlOptions.add(maxDocumentsOptionBuilder);
        crawlOptions.add(exclusionRegexpOptionBuilder);
        defaultOptionFormFieldBuilderMap.put("crawl-options", crawlOptions);
    }


    private static ContractOptionFormFieldHelper _instance;

    private static ContractOptionFormFieldHelper getInstance() {
        return _instance;
    }

    public static Map<String, List<ContractOptionFormField>> getFreshContractOptionFormFieldMap() {
        return getInstance().getFreshContractOptionFormFieldMap(
                defaultOptionFormFieldBuilderMap);
    }

    /**
     * Create a fresh map of contractOptionFormField from an contractOptionFormFieldBuilder
     * map
     * 
     * @param contractOptionFormFieldBuilderMap
     * @return 
     */
    private Map<String, List<ContractOptionFormField>> getFreshContractOptionFormFieldMap(
            Map<String, List<ContractOptionFormFieldBuilder>> contractOptionFormFieldBuilderMap) {

        // Copy the audit setup form field map from the builders
        Map<String, List<ContractOptionFormField>> initialisedContractOptionFormFielMap = 
                new LinkedHashMap<String, List<ContractOptionFormField>>();
        for (Map.Entry<String, List<ContractOptionFormFieldBuilder>> entry : contractOptionFormFieldBuilderMap.entrySet()) {
            initialisedContractOptionFormFielMap.put(
                    entry.getKey(), 
                    getFreshContractOptionFormFieldList(entry.getValue()));
        }
        return initialisedContractOptionFormFielMap;
    }
    
    /**
     * Create a fresh list of auditSetUpFormField from a auditSetUpFormFieldBuilder
     * list
     * 
     * @param auditSetUpFormFieldBuilderList
     * @return 
     */
    private List<ContractOptionFormField> getFreshContractOptionFormFieldList(
            List<ContractOptionFormFieldBuilder> contractOptionFormFieldBuilderList) {
        List<ContractOptionFormField> setUpFormFieldList = new LinkedList<ContractOptionFormField>();
        for (Iterator<ContractOptionFormFieldBuilder> it = contractOptionFormFieldBuilderList.iterator(); it.hasNext();) {
            ContractOptionFormFieldBuilder seb = it.next();
            setUpFormFieldList.add(seb.build());
        }
        return setUpFormFieldList;
    }
    
}