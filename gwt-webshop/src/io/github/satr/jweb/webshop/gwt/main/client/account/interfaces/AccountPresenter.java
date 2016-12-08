package io.github.satr.jweb.webshop.gwt.main.client.account.interfaces;

public interface AccountPresenter {
    void showDetail();
    void login();
    void logout();
    void processLogin(String email, String password);
}
