package io.github.satr.jweb.webshop.gwt.main.client.common.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Created by naveronics on 28/11/2016.
 */
public class FooterView extends Composite {
    interface FooterViewUiBinder extends UiBinder<HTMLPanel, FooterView> {
    }

    private static FooterViewUiBinder ourUiBinder = GWT.create(FooterViewUiBinder.class);

    public FooterView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}