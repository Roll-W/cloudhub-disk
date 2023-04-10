package org.huel.cloudhub.client.disk.domain.user.common;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author RollW
 */
public class UserChecker {
    public static final String USERNAME_REGEX = "^[a-zA-Z_\\-][\\w\\-]{3,20}$";
    public static final String PASSWORD_REGEX = "^[A-Za-z\\d._\\-~!@#$^&*+=<>%;'\"\\\\\\/|()\\[\\]{}]{4,20}$";
    public static final String EMAIL_REGEX = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    private static final Pattern sUsernamePattern = Pattern.compile(USERNAME_REGEX);
    private static final Pattern sPasswordPattern = Pattern.compile(PASSWORD_REGEX);
    private static final Pattern sEmailPattern = Pattern.compile(EMAIL_REGEX);

    /**
     * Allow only letters, numbers, underscores and dash,
     * length between 3 and 20,
     * no special characters except underscore and dash.
     */
    public static boolean checkUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            return false;
        }
        if (username.length() > 20 || username.length() < 3) {
            return false;
        }
        Matcher matcher = sUsernamePattern.matcher(username);
        return matcher.matches();
    }

    /**
     * Allow alphanumeric and some special characters,
     * minimum 4 characters, maximum 20 characters.
     */
    public static boolean checkPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            return false;
        }
        if (password.length() > 18 || password.length() < 3) {
            return false;
        }
        Matcher matcher = sPasswordPattern.matcher(password);
        return matcher.matches();
    }

    public static boolean checkEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        Matcher matcher = sEmailPattern.matcher(email);
        return matcher.matches();
    }
}
