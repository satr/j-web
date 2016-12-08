package io.github.satr.jweb.frontend.models;

import io.github.satr.jweb.components.entities.Account;

import java.io.Serializable;

public class EditableAccount extends Account implements Serializable {
    private String repeatedEmail;
    private String currentPassword;
    private String newPassword;
    private String repeatedPassword;

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
}
