package io.github.satr.jweb.webshop.gwt.main.client.common.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Created by naveronics on 28/11/2016.
 */
public class HeaderView extends Composite {
    interface HeaderViewUiBinder extends UiBinder<HTMLPanel, HeaderView> {
    }

    private static HeaderViewUiBinder ourUiBinder = GWT.create(HeaderViewUiBinder.class);

    public HeaderView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}