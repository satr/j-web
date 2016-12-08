package io.github.satr.jweb.webshop.gwt.main.client.common.interfaces;

import io.github.satr.jweb.webshop.gwt.main.client.common.models.AccountDTO;

public interface MenuView extends ComponentView {
    void setApplicationPresenter(ApplicationPresenter presenter);

    void setLoggedAccount(AccountDTO accountDTO);
}
