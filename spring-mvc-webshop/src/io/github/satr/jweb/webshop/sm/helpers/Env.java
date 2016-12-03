package io.github.satr.jweb.webshop.sm.helpers;

import io.github.satr.jweb.components.entities.Account;
import io.github.satr.jweb.components.models.OperationResult;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class Env {
    public static Account getAccountFromSession(HttpServletRequest request) {
        return (Account) request.getSession().getAttribute(SessionAttr.ACCOUNT);
    }

    public static String showError(Model model, String format, Object... arg) {
        ArrayList<String> errors = new ArrayList<>();
        errors.add(String.format(format, arg));
        return showErrors(model, errors);
    }

    public static String showErrors(Model model, OperationResult operationResult) {
        return showErrors(model, operationResult.getErrors());
    }

    public static String showErrors(Model model, List<String> errors) {
        model.addAttribute(ModelAttr.ERRORS, errors);
        return View.ERROR;
    }

    public static List<String> getErrors(BindingResult bindingResult) {
        ArrayList<String> errors = new ArrayList<>();
        for (ObjectError err: bindingResult.getAllErrors())
            errors.add(String.format("Invalid value in %s", err.getObjectName()));
        return errors;
    }


    public class ModelAttr {
        public final static String ERRORS = "errors";
        public static final String TOTAL = "total";
        public static final String DISCOUNT = "discount";
        public static final String SUM = "sum";
    }

    public class SessionAttr {
        public final static String ACCOUNT = "account";
    }

    public class View {
        public final static String ERROR = "ErrorView";
    }
}
