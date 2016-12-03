package io.github.satr.jweb.webshop.gwt.main.client.common.presenters;

import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.ApplicationPresenter;
import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.ComponentPresenter;
import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.ComponentView;

public abstract class ComponentPresenterBase implements ComponentPresenter {
    private ApplicationPresenter applicationPresenter;

    protected ComponentPresenterBase(ApplicationPresenter applicationPresenter) {
        this.applicationPresenter = applicationPresenter;
    }

    protected void showView(ComponentView view) {
        applicationPresenter.showView(view);
    }
}
