package io.github.satr.jweb.webshop.gwt.main.client.common.interfaces;

import java.util.List;

public interface ErrorsView extends ComponentView {
    void setError(String error);
    void setErrors(List<String> errors);
    void clearErrors();
}
