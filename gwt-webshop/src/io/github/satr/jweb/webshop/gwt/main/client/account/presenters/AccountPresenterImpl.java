package io.github.satr.jweb.webshop.gwt.main.client.account.presenters;

import com.google.gwt.user.client.rpc.AsyncCallback;
import io.github.satr.jweb.webshop.gwt.main.client.account.interfaces.AccountDetailView;
import io.github.satr.jweb.webshop.gwt.main.client.account.interfaces.AccountLoginView;
import io.github.satr.jweb.webshop.gwt.main.client.account.interfaces.AccountPresenter;
import io.github.satr.jweb.webshop.gwt.main.client.account.services.AccountService;
import io.github.satr.jweb.webshop.gwt.main.client.account.services.AccountServiceAsync;
import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.ApplicationPresenter;
import io.github.satr.jweb.webshop.gwt.main.client.common.models.AccountDTO;
import io.github.satr.jweb.webshop.gwt.main.client.common.presenters.ComponentPresenterBase;

public class AccountPresenterImpl extends ComponentPresenterBase implements AccountPresenter {
    private final AccountDetailView detailView;
    private final AccountLoginView loginView;
    private final AccountServiceAsync accountService;

    public AccountPresenterImpl(ApplicationPresenter applicationPresenter, AccountDetailView detailView, AccountLoginView loginView) {
        super(applicationPresenter);
        this.detailView = detailView;
        this.loginView = loginView;
        this.detailView.setPresenter(this);
        this.loginView.setPresenter(this);
        accountService = AccountService.App.getInstance();
        loadLoggedAccount();
    }

    private void loadLoggedAccount() {
        accountService.getLoggedAccount(new AsyncCallback<AccountDTO>() {
            @Override
            public void onFailure(Throwable caught) {
                showError(caught.getMessage());
            }

            @Override
            public void onSuccess(AccountDTO accountDTO) {
                setLoggedAccount(accountDTO);
            }
        });
    }

    @Override
    public void login() {
        loginView.clearErrors();
        showView(loginView);
    }

    @Override
    public void processLogin(String email, String password) {
        accountService.authenticate(email, password, new AsyncCallback<AccountDTO>() {
            @Override
            public void onFailure(Throwable caught) {
                showError(caught.getMessage());
            }

            @Override
            public void onSuccess(AccountDTO accountDTO) {
                if(accountDTO.errors.size() > 0) {
                    loginView.setErrors(accountDTO.errors);
                    showView(loginView);
                    return;
                }
                setLoggedAccount(accountDTO);
                navigateHome();
            }
        });
    }

    @Override
    public void logout() {
        removeAccountFromSession();
        navigateHome();
    }

    private void removeAccountFromSession() {
        accountService.logout(new AsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                showError(caught.getMessage());
            }

            @Override
            public void onSuccess(Object result) {
                setLoggedAccount(null);
            }
        });
    }

    @Override
    public void showDetail() {
        showView(detailView);
    }
}
