package io.github.satr.jweb.frontend.helpers;

import java.sql.Timestamp;

public class Env {

    public static Timestamp getTimestamp() {
        return new Timestamp(new java.util.Date().getTime());
    }

}
