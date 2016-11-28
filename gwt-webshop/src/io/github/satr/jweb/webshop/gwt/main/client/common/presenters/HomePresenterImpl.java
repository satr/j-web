package io.github.satr.jweb.webshop.gwt.main.client.common.presenters;

import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.HomePresenter;
import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.HomeView;

public class HomePresenterImpl extends ComponentPresenterBase implements HomePresenter {
    private final HomeView view;

    public HomePresenterImpl(HomeView view) {
        this.view = view;
    }

    @Override
    public void show() {
        applicationPresenter.showView(view);
    }
}
