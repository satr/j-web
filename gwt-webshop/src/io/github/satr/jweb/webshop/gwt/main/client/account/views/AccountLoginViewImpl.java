package io.github.satr.jweb.webshop.gwt.main.client.account.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import io.github.satr.jweb.webshop.gwt.main.client.account.interfaces.AccountLoginView;
import io.github.satr.jweb.webshop.gwt.main.client.account.interfaces.AccountPresenter;
import io.github.satr.jweb.webshop.gwt.main.client.common.views.ErrorView;

import java.util.ArrayList;
import java.util.List;

public class AccountLoginViewImpl extends Composite implements AccountLoginView {
    @UiField
    public ErrorView errorView;
    @UiField
    public TextBox email;
    @UiField
    public PasswordTextBox password;

    private AccountPresenter presenter;

    @Override
    public void setPresenter(AccountPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setErrors(List<String> errors) {
        errorView.setVisible(errors.size() > 0);
        errorView.setVerticalPanel(errors);
    }

    @Override
    public void clearErrors() {
        setErrors(new ArrayList<>());
    }

    interface AccountLoginViewImplUiBinder extends UiBinder<HTMLPanel, AccountLoginViewImpl> {
    }

    private static AccountLoginViewImplUiBinder ourUiBinder = GWT.create(AccountLoginViewImplUiBinder.class);

    public AccountLoginViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("processLogin")
    void processLogin(ClickEvent event) {
        presenter.processLogin(email.getText(), password.getText());
    }
}