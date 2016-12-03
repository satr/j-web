package io.github.satr.jweb.webshop.gwt.main.client.product.presenters;

import io.github.satr.jweb.webshop.gwt.main.client.common.presenters.ApplicationPresenterImpl;
import io.github.satr.jweb.webshop.gwt.main.client.common.presenters.ComponentPresenterBase;
import io.github.satr.jweb.webshop.gwt.main.client.product.interfaces.ProductListView;
import io.github.satr.jweb.webshop.gwt.main.client.product.interfaces.ProductPresenter;

public class ProductPresenterImpl extends ComponentPresenterBase implements ProductPresenter {
    private ProductListView listView;

    public ProductPresenterImpl(ApplicationPresenterImpl applicationPresenter, ProductListView listView) {
        super(applicationPresenter);
        this.listView = listView;
    }

    @Override
    public void showList() {
        showView(listView);
    }
}
