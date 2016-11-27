package io.github.satr.jweb.components.repositories;

import io.github.satr.jweb.components.entities.Product;

public class ProductRepository extends HibernateRepositoryBase<Product> {
    @Override
    protected String getSqlForList() {
        return "from Product";
    }

    @Override
    protected Class getEntityClass() {
        return Product.class;
    }
}
