/*
 * Tanaguru - Automated webpage assessment
 * Copyright (C) 2008-2013  Open-S Company
 *
 * This program is free software: you can redistribute it and/or modify
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

package org.opens.tanaguru.rules.rgaa22;

import org.opens.tanaguru.entity.audit.TestSolution;
import org.opens.tanaguru.ruleimplementation.AbstractDetectionPageRuleImplementation;
import org.opens.tanaguru.rules.elementselector.ElementSelector;
import org.opens.tanaguru.rules.elementselector.SimpleElementSelector;
import static org.opens.tanaguru.rules.keystore.HtmlElementStore.BLINK_ELEMENT;
import static org.opens.tanaguru.rules.keystore.RemarkMessageStore.DETECTED_TAG_MSG;

/**
 * Implementation of the rule 5.19 of the referential RGAA 2.2.
 * <br/>
 * For more details about the implementation, refer to <a href="http://www.tanaguru.org/en/content/rgaa22-rule-5-19">the rule 5.19 design page.</a>
 * @see <a href="http://rgaa.net/Absence-de-l-element-blink.html"> 5.19 rule specification
 *
 * @author jkowalczyk
 */
public class Rgaa22Rule05191 extends AbstractDetectionPageRuleImplementation {

    /** The element selector */
    private static final ElementSelector ELEMENT_SELECTOR = 
            new SimpleElementSelector(BLINK_ELEMENT);
    
    /**
     * Default constructor
     */
    public Rgaa22Rule05191 () {
        super(
                ELEMENT_SELECTOR,
                // solution when at least one element is found
                TestSolution.FAILED,
                // solution when no element is found
                TestSolution.PASSED,
                // detected tag message
                DETECTED_TAG_MSG,
                null
            );
    }

}