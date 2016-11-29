package io.github.satr.jweb.webshop.sm.controllers;

import io.github.satr.jweb.components.entities.Account;
import io.github.satr.jweb.components.models.OperationResult;
import io.github.satr.jweb.components.models.OperationValueResult;
import io.github.satr.jweb.webshop.sm.helpers.Env;
import io.github.satr.jweb.webshop.sm.models.AccountModel;
import io.github.satr.jweb.webshop.sm.models.EditableAccount;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AccountController {

    private final AccountModel accountModel;

    public AccountController() {
        accountModel = new AccountModel();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return View.LOGIN;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String processLogin(HttpServletRequest request, @RequestAttribute String email, @RequestAttribute String password, Model model) {
        OperationValueResult<Account> authenticationResult = new OperationValueResult<>();
        if(!accountModel.validateCredentialParams(email, password, authenticationResult)
                ||  !accountModel.authenticate(email, password, authenticationResult)) {
            model.addAttribute(ModelAttr.ERRORS, authenticationResult.getErrors());
            return View.LOGIN;
        }
        setAccountToSession(request, authenticationResult.getValue());
        return View.HOME;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        removeAccountFromSession(request);
        return View.HOME;
    }

    @RequestMapping(value = "/account/detail", method = RequestMethod.GET)
    public String viewDetail(HttpServletRequest request) {
        io.github.satr.jweb.components.entities.Account account = getAccountFromSession(request);
        if(account == null)
            return login();
        return View.DETAIL;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signUp(Model model) {
        EditableAccount editableAccount = new EditableAccount().copyFrom(accountModel.createAccount());
        editableAccount.setActionAsSignUp();
        model.addAttribute(ModelAttr.ACCOUNT, editableAccount);
        return View.EDIT;
    }

    @RequestMapping(value = "/account/edit", method = RequestMethod.GET)
    public String editAccount(HttpServletRequest request, Model model) {
        io.github.satr.jweb.components.entities.Account account = getAccountFromSession(request);
        if(account == null)
            return login();

        EditableAccount editableAccount = new EditableAccount().copyFrom(account);
        editableAccount.setActionAsEdit();
        model.addAttribute(ModelAttr.ACCOUNT, editableAccount);
        return View.EDIT;
    }

    @RequestMapping(value = "/account/edit", method = RequestMethod.POST)
    public String processEdit(@ModelAttribute EditableAccount editableAccount, BindingResult bindingResult, HttpServletRequest request, Model model) {
        io.github.satr.jweb.components.entities.Account account = editableAccount.isSignUpAction() ? accountModel.createAccount() : getAccountFromSession(request);

        OperationResult validationResult = accountModel.validateEditableProduct(editableAccount, account, bindingResult);

        if(validationResult.isFailed()) {
            model.addAttribute(ModelAttr.ERRORS, validationResult.getErrors());
            model.addAttribute(ModelAttr.ACCOUNT, editableAccount);
            return View.EDIT;
        }

        OperationValueResult<Account> saveAccountResult = accountModel.saveAccount(editableAccount, account);
        if(saveAccountResult.isFailed())
            model.addAttribute(ModelAttr.ERRORS, saveAccountResult.getErrors());

        setAccountToSession(request, saveAccountResult.getValue());

        return editableAccount.isSignUpAction() ? View.WELCOME_REGISTERED : View.DETAIL;
    }

    private void setAccountToSession(HttpServletRequest request, io.github.satr.jweb.components.entities.Account account) {
        request.getSession().setAttribute(Env.SessionAttr.ACCOUNT, account);
    }

    private void removeAccountFromSession(HttpServletRequest request) {
        request.getSession().removeAttribute(Env.SessionAttr.ACCOUNT);
    }

    private io.github.satr.jweb.components.entities.Account getAccountFromSession(HttpServletRequest request) {
        return (io.github.satr.jweb.components.entities.Account) request.getSession().getAttribute(Env.SessionAttr.ACCOUNT);
    }

    //-- Constants --
    private class View {

        static final String LOGIN = "account/LoginView";
        static final String EDIT = "account/EditView";
        static final String DETAIL = "account/DetailView";
        static final String WELCOME_REGISTERED = "account/WelcomeRegisteredView";
        static final String HOME = "HomeView";
    }

    private class ModelAttr {
        final static String ACCOUNT = "account";
        static final String ERRORS = "errors";
    }
}
