package io.github.satr.jweb.webshop.gwt.main.client.product.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import io.github.satr.jweb.webshop.gwt.main.client.product.interfaces.ProductListView;


public class ProductListViewImpl extends Composite implements ProductListView {
    interface ListViewUiBinder extends UiBinder<HTMLPanel, ProductListViewImpl> {
    }

    private static ListViewUiBinder ourUiBinder = GWT.create(ListViewUiBinder.class);

    public ProductListViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}