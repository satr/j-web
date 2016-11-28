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
    private ApplicationPresenter presenter;

    @Override
    public void setPresenter(ApplicationPresenter presenter) {

        this.presenter = presenter;
    }

    interface MenuViewUiBinder extends UiBinder<HTMLPanel, MenuViewImpl> {
    }

    private static MenuViewUiBinder ourUiBinder = GWT.create(MenuViewUiBinder.class);

    public MenuViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("home")
    void onHomeItemClick(ClickEvent event) {
        presenter.navigateHome();
    }

    @UiHandler("products")
    void onProductsItemClick(ClickEvent event) {
        presenter.showProducts();
    }

    @UiHandler("login")
    void onLoginItemClick(ClickEvent event) {
        presenter.login();
    }

    @UiHandler("logout")
    void onLogoutItemClick(ClickEvent event) {
        presenter.logout();
    }

    @UiHandler("accountDetail")
    void onAccountDetailItemClick(ClickEvent event) {
        presenter.showAccountDetail();
    }
}