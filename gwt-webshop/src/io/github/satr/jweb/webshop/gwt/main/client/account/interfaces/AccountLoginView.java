package io.github.satr.jweb.webshop.gwt.main.client.account.interfaces;

import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.ComponentView;

import java.util.List;

public interface AccountLoginView extends ComponentView {
    void setPresenter(AccountPresenter presenter);
    void setErrors(List<String> errors);
    void clearErrors();
}
