package io.github.satr.jweb.webshop.gwt.main.client.common.interfaces;

public interface ApplicationPresenter {
    void showProducts();
    void navigateHome();
    void login();
    void showView(ComponentView view);
    void logout();
    void showAccountDetail();
}
