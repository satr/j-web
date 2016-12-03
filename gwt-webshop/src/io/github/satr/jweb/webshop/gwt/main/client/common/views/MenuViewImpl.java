package io.github.satr.jweb.webshop.gwt.main.client.common.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.ApplicationPresenter;
import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.MenuView;

public class MenuViewImpl extends Composite implements MenuView {
    private ApplicationPresenter applicationPresenter;

    interface MenuViewUiBinder extends UiBinder<HTMLPanel, MenuViewImpl> {

    }
    private static MenuViewUiBinder ourUiBinder = GWT.create(MenuViewUiBinder.class);

    public MenuViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setApplicationPresenter(ApplicationPresenter applicationPresenter) {
        this.applicationPresenter = applicationPresenter;
    }

    @UiHandler("home")
    void onHomeItemClick(ClickEvent event) {
        applicationPresenter.navigateHome();
    }

    @UiHandler("products")
    void onProductsItemClick(ClickEvent event) {
        applicationPresenter.showProducts();
    }

    @UiHandler("login")
    void onLoginItemClick(ClickEvent event) {
        applicationPresenter.login();
    }

    @UiHandler("logout")
    void onLogoutItemClick(ClickEvent event) {
        applicationPresenter.logout();
    }

    @UiHandler("accountDetail")
    void onAccountDetailItemClick(ClickEvent event) {
        applicationPresenter.showAccountDetail();
    }
}