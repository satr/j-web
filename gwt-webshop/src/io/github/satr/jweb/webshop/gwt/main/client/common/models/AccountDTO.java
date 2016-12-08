package io.github.satr.jweb.webshop.gwt.main.client.common.models;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.sql.Timestamp;
import java.util.ArrayList;

public class AccountDTO implements IsSerializable {
    public int id;
    public String firstName;
    public String lastName;
    public String middleName;
    public String email;
    public Timestamp createdOn;
    public Timestamp updatedOn;
    public ArrayList<String> errors = new ArrayList<>();
}
