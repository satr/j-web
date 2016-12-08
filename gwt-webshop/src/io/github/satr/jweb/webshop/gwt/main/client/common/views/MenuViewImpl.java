package io.github.satr.jweb.webshop.gwt.main.client.common.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.ApplicationPresenter;
import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.MenuView;
import io.github.satr.jweb.webshop.gwt.main.client.common.models.AccountDTO;

public class MenuViewImpl extends Composite implements MenuView {
    private ApplicationPresenter applicationPresenter;
    @UiField
    Button login;
    @UiField
    Button logout;
    @UiField
    HorizontalPanel loginDetailPanel;
    @UiField
    Label accountFirstName;

    interface MenuViewUiBinder extends UiBinder<HTMLPanel, MenuViewImpl> {

    }
    private static MenuViewUiBinder ourUiBinder = GWT.create(MenuViewUiBinder.class);

    public MenuViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        setLoggedAccount(null);
    }

    public void setApplicationPresenter(ApplicationPresenter applicationPresenter) {
        this.applicationPresenter = applicationPresenter;
    }

    @Override
    public void setLoggedAccount(AccountDTO accountDTO) {
        if(accountDTO == null) {
            loginDetailPanel.setVisible(false);
            login.setVisible(true);
            return;
        }
        loginDetailPanel.setVisible(true);
        accountFirstName.setText("Hello, " + accountDTO.firstName);
        login.setVisible(false);
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