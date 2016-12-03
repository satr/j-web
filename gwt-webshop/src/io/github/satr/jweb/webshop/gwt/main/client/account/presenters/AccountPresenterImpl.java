package io.github.satr.jweb.webshop.gwt.main.client.account.presenters;

import io.github.satr.jweb.webshop.gwt.main.client.account.interfaces.AccountDetailView;
import io.github.satr.jweb.webshop.gwt.main.client.account.interfaces.AccountLoginView;
import io.github.satr.jweb.webshop.gwt.main.client.account.interfaces.AccountPresenter;
import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.ApplicationPresenter;
import io.github.satr.jweb.webshop.gwt.main.client.common.presenters.ComponentPresenterBase;

import java.util.ArrayList;
import java.util.List;

public class AccountPresenterImpl extends ComponentPresenterBase implements AccountPresenter {
    private final AccountDetailView detailView;
    private final AccountLoginView loginView;


    public AccountPresenterImpl(ApplicationPresenter applicationPresenter, AccountDetailView detailView, AccountLoginView loginView) {
        super(applicationPresenter);
        this.detailView = detailView;
        this.loginView = loginView;
    }

    @Override
    public void login() {
        List<String> errors = new ArrayList<>();
        loginView.setErrors(errors);
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
