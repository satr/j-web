package io.github.satr.jweb.webshop.gwt.main.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("MainService")
public interface MainService extends RemoteService {
    // Sample interface method of remote interface
    String getMessage(String msg);

    /**
     * Utility/Convenience class.
     * Use MainService.App.getInstance() to access static instance of MainServiceAsync
     */
    public static class App {
        private static MainServiceAsync ourInstance = GWT.create(MainService.class);

        public static synchronized MainServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
