package io.github.satr.jweb.components.repositories;

import io.github.satr.jweb.components.entities.Account;

import java.sql.SQLException;

public class AccountRepository extends HibernateRepositoryBase<Account> {
    @Override
    protected Class getEntityClass() {
        return Account.class;
    }

    @Override
    protected String getSqlForList() {
        return "from Account";
    }

    public Account getByEmail(String email) throws SQLException {
        return getQueryableEnity("from Account where email = :email",
                                            query -> query.setParameter("email", email));
    }
}
