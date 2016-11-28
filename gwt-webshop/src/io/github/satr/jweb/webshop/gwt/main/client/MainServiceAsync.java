package io.github.satr.jweb.webshop.gwt.main.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MainServiceAsync {
    void getMessage(String msg, AsyncCallback<String> async);
}
