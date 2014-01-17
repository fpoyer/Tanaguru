/*
 * Tanaguru - Automated webpage assessment
 * Copyright (C) 2008-2013  Open-S Company
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
package org.opens.tgol.entity.user;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.opens.tgol.entity.contract.Contract;
import org.opens.tgol.entity.option.OptionElement;
import org.opens.tgol.entity.option.OptionElementImpl;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

/**
 * @author fpoyer
 * 
 */
public class UserFromLdap implements User, Serializable {
    private static final long serialVersionUID = -4539972443064346871L;

    private LdapUserDetails detailsFromLdap;

    private Collection<Contract> contractSet = new LinkedHashSet<Contract>();

    Set<OptionElement> optionElementSet = new HashSet<OptionElement>();

    public UserFromLdap(LdapUserDetails ldapUserDetails,
            Collection<Contract> collection) {
        this.detailsFromLdap = ldapUserDetails;
        this.contractSet = collection;
    }

    @Override
    public Long getId() {
        throw new UnsupportedOperationException();
        // return Long.valueOf(1);
    }

    @Override
    public void setId(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getEmail1() {
        return detailsFromLdap.getUsername();
    }

    @Override
    public void setEmail1(String email) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPassword() {
        return detailsFromLdap.getPassword();
    }

    @Override
    public void setPassword(String password) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getFirstName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFirstName(String firstName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAddress() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAddress(String address) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPhoneNumber() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getWebUrl1() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWebUrl1(String webUrl1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getWebUrl2() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWebUrl2(String webUrl2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getIdenticaId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setIdenticaId(String identicaId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTwitterId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTwitterId(String twitterId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Role getRole() {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRole(Role role) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public void addContract(Contract contract) {
        // cannot call that : contract.setUser(this);
        // because ugly cast to UserImpl in implementation
        // TODO: use ACLs instead
        this.contractSet.add(contract);
    }

    @Override
    public void addAllContracts(Collection<Contract> contractSet) {
        for (Contract contract : contractSet) {
            // cannot call that : contract.setUser(this);
            // because ugly cast to UserImpl in implementation
            // TODO: use ACLs instead
            this.contractSet.add(contract);

        }
    }

    @Override
    public Collection<Contract> getContractSet() {
        // TODO: read base/ACLs to get contract associated with this user.

        return contractSet;
    }

    @Override
    public boolean isAccountActivated() {
        return detailsFromLdap.isEnabled();
    }

    @Override
    public void setAccountActivation(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<OptionElement> getOptionElementSet() {
        return this.optionElementSet;
    }

    @Override
    public void addOptionElement(OptionElement option) {
        optionElementSet.add((OptionElementImpl) option);
    }

    @Override
    public void addAllOptionElement(Collection<OptionElement> optionElementSet) {
        this.optionElementSet = new HashSet<OptionElement>();
        for (OptionElement optionElement : optionElementSet) {
            if (optionElement instanceof OptionElementImpl) {
                this.optionElementSet.add((OptionElementImpl) optionElement);
            }
        }
    }

}
