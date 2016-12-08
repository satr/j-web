package io.github.satr.jweb.webshop.gwt.main.client.account.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import io.github.satr.jweb.webshop.gwt.main.client.account.interfaces.AccountDetailView;
import io.github.satr.jweb.webshop.gwt.main.client.account.interfaces.AccountPresenter;

public class AccountDetailViewImpl extends Composite implements AccountDetailView {
    private AccountPresenter presenter;

    @Override
    public void setPresenter(AccountPresenter presenter) {
        this.presenter = presenter;
    }

    interface AccountDetailViewImplUiBinder extends UiBinder<HTMLPanel, AccountDetailViewImpl> {
    }

    private static AccountDetailViewImplUiBinder ourUiBinder = GWT.create(AccountDetailViewImplUiBinder.class);

    public AccountDetailViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}