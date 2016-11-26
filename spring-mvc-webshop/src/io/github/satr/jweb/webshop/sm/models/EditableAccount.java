package io.github.satr.jweb.webshop.sm.models;

import io.github.satr.jweb.components.entities.Account;

public class EditableAccount extends Account {
    private String repeatedEmail;
    private String currentPassword;
    private String newPassword;
    private String repeatedPassword;
    private String action;

    public boolean isEditAction() {
        return Action.EDIT.equals(action);
    }

    public boolean isSignUpAction() {
        return Action.SIGNUP.equals(action);
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setActionAsEdit() {
        this.action = Action.EDIT;
    }

    public void setActionAsSignUp() {
        this.action = Action.SIGNUP;
    }

    public String getRepeatedEmail() {
        return repeatedEmail;
    }

    public void setRepeatedEmail(String repeatedEmail) {
        this.repeatedEmail = repeatedEmail;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }

    public EditableAccount copyFrom(Account account) {
        copyBetween(account, this);
        setId(account.getId());
        setCreatedOn(account.getCreatedOn());
        setUpdatedOn(account.getUpdatedOn());
        return this;
    }

    public EditableAccount copyTo(Account account) {
        copyBetween(this, account);
        return this;
    }

    private void copyBetween(Account source, Account dest) {
        dest.setFirstName(source.getFirstName());
        dest.setMiddleName(source.getMiddleName());
        dest.setLastName(source.getLastName());
        dest.setEmail(source.getEmail());
    }

    private class Action {
        public static final String EDIT = "edit";
        public static final String SIGNUP = "signup";
    }
}
