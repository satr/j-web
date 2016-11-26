package io.github.satr.jweb.webshop.sm.controllers;

import io.github.satr.jweb.components.entities.Account;
import io.github.satr.jweb.components.helpers.StringHelper;
import io.github.satr.jweb.components.models.OperationResult;
import io.github.satr.jweb.components.repositories.AccountRepository;
import io.github.satr.jweb.webshop.sm.helpers.Env;
import io.github.satr.jweb.webshop.sm.models.EditableAccount;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

@Controller
public class AccountController {

    private final AccountRepository accountRepository;
    private final Random random;

    public AccountController() {
        accountRepository = new AccountRepository();
        random = new Random(System.currentTimeMillis());
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return View.LOGIN;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String processLogin(HttpServletRequest request, @RequestAttribute String email, @RequestAttribute String password, Model model) {
        OperationResult result = new OperationResult();
        if(!validateCredentialParams(email, password, result)
                ||  !authenticate(request, email, password, result)) {
            model.addAttribute(ModelAttr.ERRORS, result.getErrors());
            return View.LOGIN;
        }
        return View.HOME;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        removeAccountFromSession(request);
        return View.HOME;
    }

    private boolean validateCredentialParams(String email, String password, OperationResult result) {
        if(StringHelper.isEmptyOrWhitespace(email))
            result.addError("Missed Email.");

        if(StringHelper.isEmptyOrWhitespace(password))
            result.addError("Missed Password.");

        return result.isSucceeded();
    }

    private boolean authenticate(HttpServletRequest request, String email, String password, OperationResult result) {
        Account account = null;
        try {
            account = accountRepository.getByEmail(email);
        } catch (SQLException e) {
            result.addError(e.getMessage());//TODO -  wrap with user-friendly message and log
            return false;
        }

        if(account == null || !validatePassword(account, password)) {
            result.addError("Invalid Email or Password.");
            return false;
        }

        setAccountToSession(request, account);
        return true;
    }

    private void setAccountToSession(HttpServletRequest request, Account account) {
        request.getSession().setAttribute(Env.SessionAttr.ACCOUNT, account);
    }


    private void removeAccountFromSession(HttpServletRequest request) {
        request.getSession().removeAttribute(Env.SessionAttr.ACCOUNT);
    }

    @RequestMapping(value = "/account/detail", method = RequestMethod.GET)
    public String viewDetail(HttpServletRequest request) {
        Account account = getAccountFromSession(request);
        if(account == null)
            return login();
        return View.DETAIL;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signUp(HttpServletRequest request, Model model) {
        EditableAccount editableAccount = new EditableAccount().copyFrom(createAccount());
        editableAccount.setActionAsSignUp();
        model.addAttribute(ModelAttr.ACCOUNT, editableAccount);
        return View.EDIT;
    }

    @RequestMapping(value = "/account/edit", method = RequestMethod.GET)
    public String editAccount(HttpServletRequest request, Model model) {
        Account account = getAccountFromSession(request);
        if(account == null)
            return login();

        EditableAccount editableAccount = new EditableAccount().copyFrom(account);
        editableAccount.setActionAsEdit();
        model.addAttribute(ModelAttr.ACCOUNT, editableAccount);
        return View.EDIT;
    }

    private Account getAccountFromSession(HttpServletRequest request) {
        return (Account) request.getSession().getAttribute(Env.SessionAttr.ACCOUNT);
    }

    @RequestMapping(value = "/account/edit", method = RequestMethod.POST)
    public String processEdit(@ModelAttribute EditableAccount editableAccount, BindingResult bindingResult, HttpServletRequest request, Model model) {
        Account account = editableAccount.isSignUpAction() ? createAccount() : getAccountFromSession(request);
        OperationResult operationResult = validateEditableProduct(editableAccount, account, bindingResult);
        if(operationResult.isFailed()) {
            model.addAttribute(ModelAttr.ERRORS, operationResult.getErrors());
            model.addAttribute(ModelAttr.ACCOUNT, editableAccount);
            return View.EDIT;
        }

        editableAccount.copyTo(account);
        try {
            if(shouldChangePassword(editableAccount))
                setAccountPassword(account, editableAccount.getNewPassword());
            account.setUpdatedOn(new Timestamp(new Date().getTime()));
            accountRepository.save(account);
            account = accountRepository.getByEmail(account.getEmail());
            setAccountToSession(request, account);
        } catch (Exception e) {
            model.addAttribute(ModelAttr.ERRORS, e.getMessage());//TODO -  wrap with user-friendly message and log
        }
        return editableAccount.isSignUpAction() ? View.WELCOME_REGISTERED : View.DETAIL;
    }

    private Account createAccount() {
        Account account = new Account();
        account.setCreatedOn(new Timestamp(new Date().getTime()));
        account.setPasswordSalt(createRandomString());
        return account;
    }


    private String createRandomString() {
        return "" + random.nextLong();
    }

    private void setAccountPassword(Account account, String password) throws NoSuchAlgorithmException {
        account.setPasswordHash(getHashBy(password, account.getPasswordSalt()));
    }


    private boolean shouldChangePassword(EditableAccount editableAccount) {
        return editableAccount.isSignUpAction() ||  !StringHelper.isEmptyOrWhitespace(editableAccount.getNewPassword());
    }


    private boolean validatePassword(Account account, String password) {
        String passwordHash = account.getPasswordHash();
        String passwordSalt = account.getPasswordSalt();
        try {
            String generatedHash = getHashBy(password, passwordSalt);
            return passwordHash != null && passwordHash.compareTo(generatedHash) == 0;
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    private String getHashBy(String password, String passwordSalt) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(StandardCharsets.UTF_8.encode(password));
        md5.update(StandardCharsets.UTF_8.encode(passwordSalt));
        return String.format("%032x", new BigInteger(1, md5.digest()));
    }

    private OperationResult validateEditableProduct(EditableAccount editableAccount, Account account, BindingResult bindingResult) {
        OperationResult operationResult = new OperationResult();

        for (ObjectError err: bindingResult.getAllErrors())
            operationResult.addError("Invalid value in %s", err.getObjectName());

        validateNewNames(editableAccount, operationResult);
        validateNewEmail(editableAccount, account, operationResult);
        validateNewPassword(editableAccount, account, operationResult);

        return operationResult;
    }

    private void validateNewPassword(EditableAccount editableAccount, Account account, OperationResult operationResult) {
        boolean isSignUpAction = editableAccount.isSignUpAction();

        boolean missedNewPassword = StringHelper.isEmptyOrWhitespace(editableAccount.getNewPassword());
        if(isSignUpAction && missedNewPassword)
            operationResult.addError("Missed Password.");
        boolean missedRepeatedPassword = StringHelper.isEmptyOrWhitespace(editableAccount.getRepeatedPassword());
        if(!missedNewPassword && missedRepeatedPassword)
            operationResult.addError("Missed Repeated Password.");
        if(!isSignUpAction && (missedNewPassword || missedRepeatedPassword))
            return;
        if(!missedNewPassword && !editableAccount.getNewPassword().equals(editableAccount.getRepeatedPassword()))
            operationResult.addError("Password and Repeated Password do not match.");
        if(isSignUpAction || operationResult.isFailed())
            return;
        if(StringHelper.isEmptyOrWhitespace(editableAccount.getCurrentPassword()))
            operationResult.addError("Missed Current Password");
        else if (!validatePassword(account, editableAccount.getCurrentPassword()))
            operationResult.addError("Invalid Current Password");
    }

    private void validateNewEmail(EditableAccount editableAccount, Account account, OperationResult operationResult) {
        boolean isSignUpAction = editableAccount.isSignUpAction() || account == null;

        if (StringHelper.isEmptyOrWhitespace(editableAccount.getEmail())) {
            if (isSignUpAction)
                operationResult.addError("Missed Email");
            return;
        }

        if (!isSignUpAction && editableAccount.getEmail().equals(account.getEmail()))
            return;

        if(!editableAccount.getEmail().equals(editableAccount.getRepeatedEmail())) {
            operationResult.addError("Invalid Repeated Email");
            return;
        }

        if(checkEmailAlreadyRegistered(editableAccount.getEmail(), operationResult))
            operationResult.addError("User with this Email already registered");
    }

    private boolean checkEmailAlreadyRegistered(String email, OperationResult operationResult) {
        try {
            return accountRepository.getByEmail(email) != null;
        } catch (SQLException e) {
            operationResult.addError(e.getMessage());//TODO -  wrap with user-friendly message and log
        }
        return true;
    }

    private void validateNewNames(EditableAccount editableAccount, OperationResult operationResult) {
        if(StringHelper.isEmptyOrWhitespace(editableAccount.getFirstName()))
            operationResult.addError("Missed First Name");
        if(StringHelper.isEmptyOrWhitespace(editableAccount.getLastName()))
            operationResult.addError("Missed Last Name");
    }

    //-- Constants --
    private class View {

        public static final String LOGIN = "account/LoginView";
        public static final String EDIT = "account/EditView";
        public static final String DETAIL = "account/DetailView";
        public static final String WELCOME_REGISTERED = "account/WelcomeRegisteredView";
        public static final String HOME = "HomeView";
    }

    private class ModelAttr {
        public final static String ACCOUNT = "account";
        public static final String ERRORS = "errors";
    }
}
