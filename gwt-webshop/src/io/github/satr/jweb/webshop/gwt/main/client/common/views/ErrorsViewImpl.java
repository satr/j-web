package io.github.satr.jweb.webshop.gwt.main.client.common.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.ErrorsView;

import java.util.ArrayList;
import java.util.List;

public class ErrorsViewImpl extends Composite implements ErrorsView {
    @UiField
    public ErrorView errorView;

    interface ErrorsViewUiBinder extends UiBinder<HTMLPanel, ErrorsViewImpl> {
    }

    private static ErrorsViewUiBinder ourUiBinder = GWT.create(ErrorsViewUiBinder.class);

    public ErrorsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setError(String error) {
        ArrayList<String> errors = new ArrayList<>();
        errors.add(error);
        setErrors(errors);
    }

    @Override
    public void setErrors(List<String> errors) {
        errorView.setVisible(errors.size() > 0);
        errorView.setVerticalPanel(errors);
    }

    @Override
    public void clearErrors() {
        setErrors(new ArrayList<>());
    }
}