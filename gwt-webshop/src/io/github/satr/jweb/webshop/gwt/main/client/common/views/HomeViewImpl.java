package io.github.satr.jweb.webshop.gwt.main.client.common.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.HomeView;

public class HomeViewImpl extends Composite implements HomeView {
    interface HomeViewImplUiBinder extends UiBinder<HTMLPanel, HomeViewImpl> {
    }

    private static HomeViewImplUiBinder ourUiBinder = GWT.create(HomeViewImplUiBinder.class);

    public HomeViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}