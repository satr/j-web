package io.github.satr.jweb.webshop.gwt.main.client.account.presenters;

import io.github.satr.jweb.webshop.gwt.main.client.account.interfaces.AccountDetailView;
import io.github.satr.jweb.webshop.gwt.main.client.account.interfaces.AccountLoginView;
import io.github.satr.jweb.webshop.gwt.main.client.account.interfaces.AccountPresenter;
import io.github.satr.jweb.webshop.gwt.main.client.common.presenters.ComponentPresenterBase;

public class AccountPresenterImpl extends ComponentPresenterBase implements AccountPresenter {
    private final AccountDetailView detailView;
    private final AccountLoginView loginView;

    public AccountPresenterImpl(AccountDetailView detailView, AccountLoginView loginView) {
        this.detailView = detailView;
        this.loginView = loginView;
    }

    @Override
    public void login() {
        showView(loginView);
    }

    @Override
    public void logout() {
        //TODO
    }

    @Override
    public void showDetail() {
        showView(detailView);
    }
}
