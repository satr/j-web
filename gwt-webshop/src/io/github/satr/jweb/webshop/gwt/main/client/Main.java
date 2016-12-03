package io.github.satr.jweb.webshop.gwt.main.client;

import com.google.gwt.core.client.EntryPoint;
import io.github.satr.jweb.webshop.gwt.main.client.account.interfaces.AccountPresenter;
import io.github.satr.jweb.webshop.gwt.main.client.account.presenters.AccountPresenterImpl;
import io.github.satr.jweb.webshop.gwt.main.client.account.views.AccountDetailViewImpl;
import io.github.satr.jweb.webshop.gwt.main.client.account.views.AccountLoginViewImpl;
import io.github.satr.jweb.webshop.gwt.main.client.common.presenters.ApplicationPresenterImpl;
import io.github.satr.jweb.webshop.gwt.main.client.common.presenters.HomePresenterImpl;
import io.github.satr.jweb.webshop.gwt.main.client.common.views.HomeViewImpl;
import io.github.satr.jweb.webshop.gwt.main.client.common.views.MainViewImpl;
import io.github.satr.jweb.webshop.gwt.main.client.common.views.MenuViewImpl;
import io.github.satr.jweb.webshop.gwt.main.client.product.interfaces.ProductPresenter;
import io.github.satr.jweb.webshop.gwt.main.client.product.presenters.ProductPresenterImpl;
import io.github.satr.jweb.webshop.gwt.main.client.product.views.ProductListViewImpl;

public class Main implements EntryPoint {

    private ApplicationPresenterImpl applicationPresenter;

    public void onModuleLoad() {
        MainViewImpl mainView = new MainViewImpl();
        applicationPresenter = new ApplicationPresenterImpl(mainView, new MenuViewImpl());

        HomePresenterImpl homePresenter = new HomePresenterImpl(applicationPresenter, new HomeViewImpl());
        applicationPresenter.setHomePresenter(homePresenter);

        ProductPresenter productPresenter = new ProductPresenterImpl(applicationPresenter, new ProductListViewImpl());
        applicationPresenter.setProductPresenter(productPresenter);

        AccountPresenter accountPresenter = new AccountPresenterImpl(applicationPresenter, new AccountDetailViewImpl(), new AccountLoginViewImpl());
        applicationPresenter.setAccountPresenter(accountPresenter);
    }
}
