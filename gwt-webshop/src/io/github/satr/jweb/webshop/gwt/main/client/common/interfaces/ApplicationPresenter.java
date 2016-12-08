package io.github.satr.jweb.webshop.gwt.main.client.common.interfaces;

import io.github.satr.jweb.webshop.gwt.main.client.common.models.AccountDTO;

public interface ApplicationPresenter {
    void showProducts();
    void navigateHome();
    void login();
    void showView(ComponentView view);
    void logout();
    void showAccountDetail();
    void showError(String error);

    void setLoggedAccount(AccountDTO accountDTO);
}
