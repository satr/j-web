package io.github.satr.jweb.webshop.gwt.main.client.common.presenters;

import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.ApplicationPresenter;
import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.ComponentPresenter;
import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.ComponentView;
import io.github.satr.jweb.webshop.gwt.main.client.common.models.AccountDTO;

public abstract class ComponentPresenterBase implements ComponentPresenter {
    private ApplicationPresenter applicationPresenter;

    protected ComponentPresenterBase(ApplicationPresenter applicationPresenter) {
        this.applicationPresenter = applicationPresenter;
    }

    protected void showView(ComponentView view) {
        applicationPresenter.showView(view);
    }

    protected void navigateHome() {
        this.applicationPresenter.navigateHome();
    }

    protected void showError(String error) {
        this.applicationPresenter.showError(error);
    }

    protected void setLoggedAccount(AccountDTO accountDTO) {
        this.applicationPresenter.setLoggedAccount(accountDTO);
    }
}
