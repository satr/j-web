package io.github.satr.jweb.webshop.gwt.main.client.account.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import io.github.satr.jweb.webshop.gwt.main.client.common.models.AccountDTO;

import java.util.ArrayList;

@RemoteServiceRelativePath("AccountService")
public interface AccountService extends RemoteService {
    ArrayList<String> validateEditableAccount(AccountDTO editableAccount, boolean isSignUpAction);
    AccountDTO createAccount();
    AccountDTO authenticate(String email, String password);
    AccountDTO saveAccount(AccountDTO editableAccount, boolean isSignUpAction);
    AccountDTO getLoggedAccount();
    void logout();

    /**
     * Utility/Convenience class.
     * Use AccountService.App.getInstance() to access static instance of AccountServiceAsync
     */
    public static class App {
        private static final AccountServiceAsync ourInstance = (AccountServiceAsync) GWT.create(AccountService.class);

        public static AccountServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
