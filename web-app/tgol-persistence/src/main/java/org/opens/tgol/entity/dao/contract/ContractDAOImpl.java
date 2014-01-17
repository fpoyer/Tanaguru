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
package org.opens.tgol.entity.dao.contract;

import java.util.Collection;
import java.util.Iterator;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.opens.tanaguru.sdk.entity.dao.jpa.AbstractJPADAO;
import org.opens.tgol.entity.contract.Contract;
import org.opens.tgol.entity.contract.ContractImpl;
import org.opens.tgol.entity.user.User;

/**
 * 
 * @author jkowalczyk
 */
public class ContractDAOImpl extends AbstractJPADAO<Contract, Long> implements
        ContractDAO {

    public ContractDAOImpl() {
        super();
    }

    @Override
    protected Class<? extends Contract> getEntityClass() {
        return ContractImpl.class;
    }

    public Collection<Long> findAllIndex() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT o.id_" + getEntityClassName() + " FROM "
                        + getEntityClassName() + " o", getKeyClass());
        return query.getResultList();
    }

    protected Class<Long> getKeyClass() {
        return Long.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Contract> findAllContractsByUser(User user) {
        Query query = entityManager.createQuery("SELECT distinct(c) FROM "
                + getEntityClassName() + " c"
                + " LEFT JOIN FETCH c.optionElementSet o"
                + " LEFT JOIN FETCH c.functionalitySet f"
                + " LEFT JOIN FETCH c.referentialSet f"
                + " LEFT JOIN FETCH c.scenarioSet f" + " WHERE c.user = :user");
        query.setParameter("user", user);
        return query.getResultList();
    }

    @Override
    public Contract read(Long id) {
        try {
            Query query = entityManager.createQuery("SELECT c FROM "
                    + getEntityClassName() + " c"
                    + " LEFT JOIN FETCH c.optionElementSet o"
                    + " LEFT JOIN FETCH c.functionalitySet f"
                    + " LEFT JOIN FETCH c.referentialSet f"
                    + " LEFT JOIN FETCH c.scenarioSet f" + " WHERE c.id = :id");
            query.setParameter("id", id);
            Contract contract = (Contract) query.getSingleResult();
            // to ensure the options associated with the contract is
            // retrieved
            contract.getOptionElementSet().size();
            contract.getFunctionalitySet().size();
            contract.getScenarioSet().size();
            contract.getReferentialSet().size();
            return contract;
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public Collection<Contract> findByIndexes(Collection<Long> indexes) {
        // Optimization: setting capacity at the correct value right away
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ").append(getEntityClassName())
                .append(" o WHERE o.id IN (");
        Iterator<Long> it = indexes.iterator();
        while (it.hasNext()) {
            Long index = it.next();
            queryBuilder.append(index.longValue());
            if (it.hasNext()) {
                queryBuilder.append(',');
            }
        }
        queryBuilder.append(")");
        Query query = entityManager.createQuery(queryBuilder.toString());
        return query.getResultList();
    }

}