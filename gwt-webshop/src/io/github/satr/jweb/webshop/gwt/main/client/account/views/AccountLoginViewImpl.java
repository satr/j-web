package io.github.satr.jweb.webshop.gwt.main.client.account.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import io.github.satr.jweb.webshop.gwt.main.client.account.interfaces.AccountLoginView;
import io.github.satr.jweb.webshop.gwt.main.client.common.views.ErrorView;

import java.util.List;

public class AccountLoginViewImpl extends Composite implements AccountLoginView {
    @UiField
    public ErrorView errorView;

    @Override
    public void setErrors(List<String> errors) {
        errorView.setVisible(errors.size() > 0);
        errorView.setVerticalPanel(errors);
    }

    interface AccountLoginViewImplUiBinder extends UiBinder<HTMLPanel, AccountLoginViewImpl> {
    }

    private static AccountLoginViewImplUiBinder ourUiBinder = GWT.create(AccountLoginViewImplUiBinder.class);

    public AccountLoginViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}