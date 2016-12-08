package io.github.satr.jweb.webshop.gwt.main.client.account.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import io.github.satr.jweb.webshop.gwt.main.client.common.models.AccountDTO;

import java.util.ArrayList;

public interface AccountServiceAsync {
    void validateEditableAccount(AccountDTO editableAccount, boolean isSignUpAction, AsyncCallback<ArrayList<String>> async);

    void createAccount(AsyncCallback<AccountDTO> async);

    void authenticate(String email, String password, AsyncCallback<AccountDTO> async);

    void saveAccount(AccountDTO editableAccount, boolean isSignUpAction, AsyncCallback<AccountDTO> async);

    void getLoggedAccount(AsyncCallback<AccountDTO> async);

    void logout(AsyncCallback async);
}
