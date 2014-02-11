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
package org.opens.tgol.command.factory;

import java.io.Serializable;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.opens.tgol.command.CreateContractCommand;
import org.opens.tgol.entity.contract.Contract;
import org.opens.tgol.entity.functionality.Functionality;
import org.opens.tgol.entity.option.Option;
import org.opens.tgol.entity.option.OptionElement;
import org.opens.tgol.entity.referential.Referential;
import org.opens.tgol.entity.service.functionality.FunctionalityDataService;
import org.opens.tgol.entity.service.option.OptionDataService;
import org.opens.tgol.entity.service.option.OptionElementDataService;
import org.opens.tgol.entity.service.referential.ReferentialDataService;

/**
 *
 * @author jkowalczyk
 */
public class CreateContractCommandFactory  implements Serializable {

    private static final long serialVersionUID = -7369205970056064644L;
	private static final String DOMAIN_OPTION_CODE = "DOMAIN";
    private static CreateContractCommandFactory createContractCommandFactory;
    
    private Collection<Referential> referentialList;
    private Collection<Functionality> functionalityList;
    private Collection<Option> optionList = new HashSet<Option>();
    
    public void setReferentialDataService (ReferentialDataService referentialDataService) {
        referentialList = referentialDataService.findAll();
    }
    
    public void setFunctionalityDataService (FunctionalityDataService funcitonalityDataService) {
        functionalityList = funcitonalityDataService.findAll();
    }

    private Option contractUrlOption;
    
    public void setOptionDataService (OptionDataService optionDataService) {
        for (Option option : optionDataService.findAll()) {
            if (optionNameList.contains(option.getCode())){
                if (option.getCode().equals(DOMAIN_OPTION_CODE)){
                    contractUrlOption = option;
                } else {
                    optionList.add(option);    
                }
            }
        }
    }

    private OptionElementDataService optionElementDataService;
    public void setOptionElementDataService (OptionElementDataService optionElementDataService) {
        this.optionElementDataService = optionElementDataService;
    }

    private Collection<String> optionNameList;
    public void setOptionNameList (Collection<String> optionNameList) {
        this.optionNameList = optionNameList;
    }
    
    /**
     * Factory has default constructor
     */
    private CreateContractCommandFactory(){}

    public static synchronized CreateContractCommandFactory getInstance() {
        if (createContractCommandFactory == null) {
            createContractCommandFactory = new CreateContractCommandFactory();
        }
        return createContractCommandFactory;
    }
    
    /**
     * 
     * @param contract
     * @return 
     */
    public CreateContractCommand getInitialisedCreateContractCommand(Contract contract) {
        CreateContractCommand createContractCommand = new CreateContractCommand();
        
        createContractCommand.setLabel(contract.getLabel());
        createContractCommand.setBeginDate(contract.getBeginDate());
        createContractCommand.setEndDate(contract.getEndDate());
        
        addFunctionalityToCommand(createContractCommand, contract);
        addReferentialToCommand(createContractCommand, contract);
        addOptionToCommand(createContractCommand,contract);
        addContractUrlToCommand(createContractCommand,contract);

        return createContractCommand;
    }

    /**
     * 
     * @return 
     */
    public CreateContractCommand getNewCreateContractCommand() {
       //CreateContractCommand createContractCommand = getCreateContractCommand();
        CreateContractCommand createContractCommand = new CreateContractCommand();
        Calendar cal = Calendar.getInstance();
        createContractCommand.setBeginDate(cal.getTime());
        cal.add(Calendar.YEAR,1);
        createContractCommand.setEndDate(cal.getTime());
        return createContractCommand;
    }
    

    /**
     * 
     */
    private void addFunctionalityToCommand(CreateContractCommand ccc, Contract contract) {
        List<String> functList = new ArrayList<String>();
        
        for (Functionality funct : this.functionalityList){
            if (contract.getFunctionalitySet().contains(funct)) {
                functList.add(funct.getCode());
            } else {
                functList.add(funct.getCode());
            }
        }
        ccc.setEnabledFunctionalities(functList);
    }
    
    /**
     * 
     * @param ccc
     * @param contract 
     */
    private void addReferentialToCommand(CreateContractCommand ccc, Contract contract) {
        Set<String> refMap = new HashSet<String>();
        
        for (Referential ref : referentialList){
            if (contract.getReferentialSet().contains(ref)) {
                refMap.add(ref.getCode());
            } else {
                refMap.add(ref.getCode());
            }
        }
        ccc.setEnabledReferentials(refMap);
    }
    


    /**
     * 
     * @param ccc
     * @param contract 
     */
    private void addOptionToCommand(CreateContractCommand ccc, Contract contract) {
        Map<String,String> optionMap = new LinkedHashMap<String, String>();
        
        for (Option option : optionList){
            optionMap.put(
                option.getCode(),
                getValueFromOptionElementCollection(contract.getOptionElementSet(),option));
        }
        ccc.setOptionMap(optionMap);
    }
    
    /**
     * 
     * @param ccc
     * @param contract 
     */
    private void addContractUrlToCommand(CreateContractCommand ccc, Contract contract) {
        for (OptionElement optionElement : contract.getOptionElementSet()){
            if (optionElement.getOption().equals(contractUrlOption)) {
                ccc.setContractUrl(optionElement.getValue());
            }
        }
    }

    /**
     * Retrieve the option value from a collection of option elements.
     * 
     * @param optionElementCollection
     * @param option
     * @return 
     */
    private String getValueFromOptionElementCollection(
            Collection<OptionElement> optionElementCollection, 
            Option option) {
        for (OptionElement optionElement : optionElementCollection) {
            if (optionElement.getOption().getCode().equals(option.getCode())) {
                return optionElement.getValue();
            }
        }
        return "";
    }

    /**
     * 
     * @param createContractCommand
     * @param contract
     * @return 
     */
    public Contract updateContractFromCommand(
            CreateContractCommand ccc, 
            Contract contract) {
        
        Set<Functionality> functSet = new HashSet<Functionality>();
        Set<Referential> refSet = new HashSet<Referential>();
        Set<OptionElement> optionElementSet = new HashSet<OptionElement>();
        Logger.getLogger(this.getClass()).error("Functionnalities in command: " + ccc.getEnabledFunctionalities());
        for (String functionalityCode : ccc.getEnabledFunctionalities()) {
            if (functionalityCode != null) {
                functSet.add(getFunctionalityFromCode(functionalityCode));
            }
        }
        Logger.getLogger(this.getClass()).error("Referentials in command: " + ccc.getEnabledReferentials());
        for (String referentialCode : ccc.getEnabledReferentials()) {
            if (referentialCode != null ) {
                refSet.add(getReferentialFromCode(referentialCode));
            }
        }
        Logger.getLogger(this.getClass()).error("Options in command: " + ccc.getOptionMap());
        for (Map.Entry<String,String> entry : ccc.getOptionMap().entrySet()) {
            if (!StringUtils.isEmpty(entry.getValue())) {
                optionElementSet.add(getOptionElementFromOptionAndValue(entry.getKey(), entry.getValue()));
            }
        }
        if (!StringUtils.isEmpty(ccc.getContractUrl()) && !ccc.getContractUrl().equalsIgnoreCase("http://")) {
            optionElementSet.add(addUrlToContract(ccc.getContractUrl()));
        }
        
        contract.addAllFunctionality(functSet);
        contract.addAllReferential(refSet);
        contract.addAllOptionElement(optionElementSet);
        
        contract.setBeginDate(ccc.getBeginDate());
        Calendar cal = Calendar.getInstance();
        cal.setTime(ccc.getEndDate());
        // the end contract date is added without the hour/minute/second info
        // we force the end of the filled-in day before we persist.
        cal.add(Calendar.MINUTE, 59);
        cal.add(Calendar.HOUR_OF_DAY, 23);
        cal.add(Calendar.SECOND, 59);
        contract.setEndDate(cal.getTime());
        contract.setLabel(ccc.getLabel());

        return contract;
    }

    
    /**
    * 
    * @param url
    * @return 
    */
    private OptionElement addUrlToContract(String url) {
        return createOptionElement(contractUrlOption, url);
    }
    
    /**
     * @param value
     * @param optionCode
     * @return 
     */
    private OptionElement getOptionElementFromOptionAndValue(String optionCode, String value) {
        Option option = getOptionFromCode(optionCode);
        return createOptionElement(option, value);
    }
    
    /**
     * 
     * @param value
     * @param option
     * @return 
     */
    private OptionElement createOptionElement(Option option, String value) {
        OptionElement optionElement = optionElementDataService.getOptionElementFromValueAndOption(value, option);
        if (optionElement != null) {
            return optionElement;
        }
        optionElement = optionElementDataService.create();
        optionElement.setValue(value);
        optionElement.setOption(option);
        optionElementDataService.saveOrUpdate(optionElement);
        return optionElement;
    }

    /**
     * Retrieve a referential from its code regarding the local referential list.
     * 
     * @param refCode
     * @return 
     */
    private Referential getReferentialFromCode(String refCode) {
        for (Referential ref : referentialList) {
            if (ref.getCode().equalsIgnoreCase(refCode)) {
                return ref;
            }
        }
        return null;
    }
    
    /**
     * Retrieve a functionality from its code regarding the local functionality
     * list.
     * 
     * @param functCode
     * @return 
     */
    private Functionality getFunctionalityFromCode(String functCode) {
        for (Functionality funct : functionalityList) {
            if (funct.getCode().equalsIgnoreCase(functCode)) {
                return funct;
            }
        }
        return null;
    }
    
    /**
     * Retrieve an option from its code regarding the local option list
     * 
     * @param optionCode
     * @return 
     */
    private Option getOptionFromCode(String optionCode) {
        for (Option option : optionList) {
            if (option.getCode().equalsIgnoreCase(optionCode)) {
                return option;
            }
        }
        return null;
    }
    
}