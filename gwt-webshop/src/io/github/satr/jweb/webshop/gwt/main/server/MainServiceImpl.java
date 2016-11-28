package io.github.satr.jweb.webshop.gwt.main.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import io.github.satr.jweb.webshop.gwt.main.client.MainService;

public class MainServiceImpl extends RemoteServiceServlet implements MainService {
    // Implementation of sample interface method
    public String getMessage(String msg) {
        return "Client said: \"" + msg + "\"<br>Server answered: \"Hi!\"";
    }
}