package io.github.satr.jweb.webshop.gwt.main.server.account;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import io.github.satr.jweb.components.entities.Account;
import io.github.satr.jweb.components.models.OperationValueResult;
import io.github.satr.jweb.frontend.models.AccountModel;
import io.github.satr.jweb.webshop.gwt.main.client.account.services.AccountService;
import io.github.satr.jweb.webshop.gwt.main.client.common.models.AccountDTO;

import java.util.ArrayList;

public class AccountServiceImpl extends RemoteServiceServlet implements AccountService {
    private final AccountModel accountModel;

    public AccountServiceImpl() {
        accountModel = new AccountModel();
    }

    @Override
    public ArrayList<String> validateEditableAccount(AccountDTO editableAccount, boolean isSignUpAction) {
        return new ArrayList<>();
    }

    @Override
    public AccountDTO createAccount() {
        return new AccountDTO();//TODO
    }

    @Override
    public AccountDTO authenticate(String email, String password) {
        OperationValueResult<Account> result = accountModel.authenticate(email, password);
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.email = email;
        if(result.isFailed()) {
            accountDTO.errors = result.getErrors();
            return accountDTO;
        }
        Account account = result.getValue();
        accountDTO.id = account.getId();
        accountDTO.firstName = account.getFirstName();
        accountDTO.middleName = account.getMiddleName();
        accountDTO.lastName = account.getLastName();
        accountDTO.createdOn = account.getCreatedOn();
        accountDTO.updatedOn = account.getUpdatedOn();
        storeAccountDTOInSession(accountDTO);
        return accountDTO;
    }

    @Override
    public AccountDTO saveAccount(AccountDTO editableAccount, boolean isSignUpAction) {
        return new AccountDTO();//TODO
    }

    @Override
    public AccountDTO getLoggedAccount()
    {
        Object accountDTO = this.getThreadLocalRequest().getSession().getAttribute(SessionAttr.Account);
        if (accountDTO != null && accountDTO instanceof AccountDTO)
        {
            return (AccountDTO) accountDTO;
        }
        return null;
    }

    @Override
    public void logout() {
        this.getThreadLocalRequest().getSession(true).removeAttribute(SessionAttr.Account);
    }

    private void storeAccountDTOInSession(AccountDTO user)
    {
        this.getThreadLocalRequest().getSession(true).setAttribute(SessionAttr.Account, user);
    }

    private class SessionAttr {
        final static String Account = "account";
    }
}