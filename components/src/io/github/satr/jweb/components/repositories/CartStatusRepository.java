package io.github.satr.jweb.components.repositories;

import io.github.satr.jweb.components.entities.CartItemStatus;

import java.sql.SQLException;
import java.util.HashMap;

public class CartStatusRepository extends HibernateRepositoryBase<CartItemStatus> {
    private HashMap<String, CartItemStatus> loadedStatusMap = new HashMap<>();
    private Object sync = new Object();

    @Override
    protected String getSqlForList() {
        return "from CartItemStatus";
    }

    @Override
    protected Class getEntityClass() {
        return CartItemStatus.class;
    }

    public CartItemStatus getByKey(String key) throws SQLException {
        if(!loadedStatusMap.containsKey(key)) {
            synchronized (sync) {
                if (!loadedStatusMap.containsKey(key))
                    loadedStatusMap.put(key, getQueryableEnity("from CartItemStatus where ItemKey = :key", query -> query.setParameter("key", key)));
            }
        }
        return loadedStatusMap.get(key);
    }

    public CartItemStatus getStatusNotPayed() throws SQLException {
        return getByKey(StatusKey.NotPayed);
    }

    private class StatusKey {

        public final static String NotPayed = "NotPayed";
    }
}
