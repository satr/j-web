package io.github.satr.jweb.frontend.models;

import io.github.satr.jweb.components.entities.Account;
import io.github.satr.jweb.components.helpers.StringHelper;
import io.github.satr.jweb.components.models.OperationResult;
import io.github.satr.jweb.components.models.OperationValueResult;
import io.github.satr.jweb.components.repositories.AccountRepository;
import io.github.satr.jweb.frontend.helpers.Env;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AccountModel {
    private final AccountRepository accountRepository;
    private final Random random;

    public AccountModel() {
        accountRepository = new AccountRepository();
        random = new Random(System.currentTimeMillis());
    }

    public boolean validateCredentialParams(String email, String password, OperationResult result) {
        if(StringHelper.isEmptyOrWhitespace(email))
            result.addError("Missed Email.");

        if(StringHelper.isEmptyOrWhitespace(password))
            result.addError("Missed Password.");

        return result.isSucceeded();
    }

    private String getHashBy(String password, String passwordSalt) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(StandardCharsets.UTF_8.encode(password));
        md5.update(StandardCharsets.UTF_8.encode(passwordSalt));
        return String.format("%032x", new BigInteger(1, md5.digest()));
    }

    private boolean validatePassword(io.github.satr.jweb.components.entities.Account account, String password) {
        String passwordHash = account.getPasswordHash();
        String passwordSalt = account.getPasswordSalt();
        try {
            String generatedHash = getHashBy(password, passwordSalt);
            return passwordHash != null && passwordHash.compareTo(generatedHash) == 0;
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    private void validateNewPassword(EditableAccount editableAccount, io.github.satr.jweb.components.entities.Account account, OperationResult operationResult) {
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

    private void validateNewEmail(EditableAccount editableAccount, io.github.satr.jweb.components.entities.Account account, OperationResult operationResult) {
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

    public OperationResult validateEditableProduct(EditableAccount editableAccount, Account account, List<String> errors) {
        OperationResult operationResult = new OperationResult();

        for (String err: errors)
            operationResult.addError(err);

        validateNewNames(editableAccount, operationResult);
        validateNewEmail(editableAccount, account, operationResult);
        validateNewPassword(editableAccount, account, operationResult);

        return operationResult;
    }

    private boolean shouldChangePassword(EditableAccount editableAccount) {
        return editableAccount.isSignUpAction() ||  !StringHelper.isEmptyOrWhitespace(editableAccount.getNewPassword());
    }

    private void setAccountPassword(io.github.satr.jweb.components.entities.Account account, String password) throws NoSuchAlgorithmException {
        account.setPasswordHash(getHashBy(password, account.getPasswordSalt()));
    }

    private String createRandomString() {
        return "" + random.nextLong();
    }

    public io.github.satr.jweb.components.entities.Account createAccount() {
        io.github.satr.jweb.components.entities.Account account = new io.github.satr.jweb.components.entities.Account();
        account.setCreatedOn(Env.getTimestamp());
        account.setPasswordSalt(createRandomString());
        return account;
    }

    public boolean authenticate(String email, String password, OperationValueResult<Account> result) {
        Account account;
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

        result.setValue(account);
        return true;
    }

    public OperationValueResult<Account> saveAccount(EditableAccount editableAccount, Account account) {
        editableAccount.copyTo(account);
        OperationValueResult<Account> result = new OperationValueResult<>();
        try {
            if(shouldChangePassword(editableAccount))
                setAccountPassword(account, editableAccount.getNewPassword());
            account.setUpdatedOn(new Timestamp(new Date().getTime()));
            accountRepository.save(account);
            account = accountRepository.getByEmail(account.getEmail());
            result.setValue(account);
        } catch (Exception e) {
            result.addError(e.getMessage());//TODO -  wrap with user-friendly message and log
        }
        return result;
    }
}
