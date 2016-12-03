package io.github.satr.jweb.webshop.sm.helpers;

import io.github.satr.jweb.components.helpers.StringHelper;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class ParameterHelper {

    public static String getString(HttpServletRequest request, String parameterName, List<String> errorMessages) {
        String[] values = request.getParameterValues(parameterName);
        if (values != null && values.length > 0)
            return values[0];
        addParameterNotFoundErrorMessage(parameterName, errorMessages);
        return null;
    }

    public static int getInt(HttpServletRequest request, String parameterName, List<String> errorMessages) {
        String[] values = request.getParameterValues(parameterName);
        if (values == null || values.length == 0 ) {
            addParameterNotFoundErrorMessage(parameterName, errorMessages);
            return 0;
        }
        if (!StringHelper.isInteger(values[0])) {
            errorMessages.add(String.format("\"%s\" expected as a number.", parameterName));
            return 0;
        }
        return Integer.parseInt(values[0]);
    }

    public static double getDouble(HttpServletRequest request, String parameterName, List<String> errors) {
        String[] values = request.getParameterValues(parameterName);
        if (values == null && values.length == 0 ) {
            addParameterNotFoundErrorMessage(parameterName, errors);
            return 0.0;
        }
        return getDouble(values[0], parameterName, errors);
    }

    public static double getDouble(String value, String parameterName, List<String> errorMessages) {
        if (!StringHelper.isDouble(value)) {
            errorMessages.add(String.format("\"%s\" expected as a number.", parameterName));
            return 0.0;
        }
        return Double.parseDouble(value);
    }

    public static double getNonNegativeDouble(String value, String parameterName, List<String> errorMessages) {
        if (StringHelper.isDouble(value)) {
            double num = Double.parseDouble(value);
            if(num >= 0)
                return num;
        }
        errorMessages.add(String.format("\"%s\" expected as a positive number.", parameterName));
        return 0.0;
    }

    private static void addParameterNotFoundErrorMessage(String parameterName, List<String> errorMessages) {
        errorMessages.add(String.format("\"%s\" not found.", parameterName));
    }

}
