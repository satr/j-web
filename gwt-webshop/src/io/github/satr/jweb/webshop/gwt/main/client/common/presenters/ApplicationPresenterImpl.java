package io.github.satr.jweb.webshop.gwt.main.client.common.presenters;

import io.github.satr.jweb.webshop.gwt.main.client.account.interfaces.AccountPresenter;
import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.*;
import io.github.satr.jweb.webshop.gwt.main.client.product.interfaces.ProductPresenter;

public class ApplicationPresenterImpl implements ApplicationPresenter {
    private MainView mainView;
    private HomePresenter homePresenter;
    private ProductPresenter productPresenter;
    private AccountPresenter accountPresenter;

    public ApplicationPresenterImpl(MainView view, MenuView menuView) {
        this.mainView = view;
        menuView.setPresenter(this);
        view.addMenu(menuView);
    }

    @Override
    public void navigateHome() {
        homePresenter.show();
    }

    @Override
    public void login() {
        accountPresenter.login();
    }

    @Override
    public void showView(ComponentView view) {
        this.mainView.show(view);
    }

    @Override
    public void logout() {
        accountPresenter.logout();
        navigateHome();
    }

    @Override
    public void showAccountDetail() {
        accountPresenter.showDetail();
    }

    @Override
    public void showProducts() {
        productPresenter.showList();
    }

    public void setProductPresenter(ProductPresenter productPresenter) {
        this.productPresenter = productPresenter;
        productPresenter.setApplicationPresenter(this);
    }

    public void setAccountPresenter(AccountPresenter accountPresenter) {
        this.accountPresenter = accountPresenter;
    }

    public void setHomePresenter(HomePresenter homePresenter) {
        this.homePresenter = homePresenter;
    }
}
