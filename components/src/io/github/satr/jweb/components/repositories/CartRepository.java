package io.github.satr.jweb.components.repositories;

import io.github.satr.jweb.components.entities.CartItem;

import java.sql.SQLException;
import java.util.List;

public class CartRepository extends HibernateRepositoryBase<CartItem> {
    @Override
    protected Class getEntityClass() {
        return CartItem.class;
    }

    @Override
    protected String getSqlForList() {
        return "from CartItem";
    }

    public List<CartItem> getByAccountId(int accountId, int[] statusIdList) throws SQLException {
        return getQueryable("from CartItem where AccountID = :accountId and StatusID in (:statusIdList)",
                                            query -> {
                                                query.setParameter("accountId", accountId);
                                                query.setParameter("statusIdList", statusIdList);
                                            });
    }
}
