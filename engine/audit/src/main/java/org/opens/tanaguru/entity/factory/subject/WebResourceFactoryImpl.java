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
package org.opens.tanaguru.entity.factory.subject;

import org.opens.tanaguru.entity.subject.Page;
import org.opens.tanaguru.entity.subject.PageImpl;
import org.opens.tanaguru.entity.subject.Site;
import org.opens.tanaguru.entity.subject.SiteImpl;
import org.opens.tanaguru.entity.subject.WebResource;

/**
 * 
 * @author jkowalczyk
 */
public class WebResourceFactoryImpl implements WebResourceFactory {

    public WebResourceFactoryImpl() {
        super();
    }

    @Override
    public WebResource create() {
        return new PageImpl();
    }

    @Override
    public Page createPage(String pageURL) {
        return new PageImpl(pageURL);
    }

    @Override
    public Site createSite(String siteURL) {
        return new SiteImpl(siteURL);
    }

}