package pl.blackwater.guilds.utils;

public class StringUtil {

    public static boolean isAlphaNumeric(String s) {
        return s.matches("^[a-zA-Z0-9_]*$");
    }

    public static boolean isAlphaNumeric1(String s) {
        return s.matches("^[A-Z0-9_]*$");
    }
}
