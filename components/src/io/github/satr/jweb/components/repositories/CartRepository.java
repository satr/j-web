package io.github.satr.jweb.components.repositories;

import io.github.satr.jweb.components.entities.CartItem;
import io.github.satr.jweb.components.entities.CartItemStatus;

import java.sql.SQLException;
import java.util.ArrayList;
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

    public List<CartItem> getList(int accountId, CartItemStatus... statusList) throws SQLException {
        List<Integer> statusIdList = new ArrayList<>(statusList.length);
        for(CartItemStatus status: statusList)
            statusIdList.add(status.getId());

        return getQueryable("from CartItem where accountId = :accountId and status.id in :statusIdList",
                                            query -> {
                                                query.setParameter("accountId", accountId);
                                                query.setParameter("statusIdList", statusIdList);
                                            });
    }
}
