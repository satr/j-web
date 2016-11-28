package io.github.satr.jweb.webshop.gwt.main.client.common.interfaces;

public interface MenuPresenter {
    MenuView getView();

    void navigateHome();

    void setApplicationPresenter(ApplicationPresenter applicationPresenter);

    void showProducts();

    void login();
}
