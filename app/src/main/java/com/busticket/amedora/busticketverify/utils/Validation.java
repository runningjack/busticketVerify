package com.busticket.amedora.busticketverify.utils;

import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Created by Amedora on 7/10/2015.
 */
public class Validation {
    //Regular Expression
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "(^\\+)?[0-9()-]*";

    //Error messages
    private static final String REQUIRED_MSG  ="Required";
    private static final String EMAIL_MSG     = "Invalid Email";
    private static final String PHONE_MSG     = "Invalid Phone Number";
    private static final String CONFIRM_MSG   = "Password Mismatch";
    private static final String ACCOUNT_NO   = "Invalid Account Number";

    public static boolean isEmailAddress(EditText editText, boolean required){
        return isValid(editText,EMAIL_REGEX,EMAIL_MSG,required);
    }
    public static boolean isPasswordMatch(EditText passText,EditText confirmText, boolean required){
        return isMatch(passText,confirmText,CONFIRM_MSG,required);
    }

    public static boolean isAccountNo(EditText accText,Integer fixed){
        return numberFixed(accText,ACCOUNT_NO,10);
    }
    public static boolean isMatch(EditText passText,EditText confirmText,String errMsg, boolean required){
        String pwd = passText.getText().toString().trim();
        String cfm = confirmText.getText().toString().trim();
        confirmText.setError(null);
        if ( required && !hasText(confirmText) ) return false;
        // pattern doesn't match so returning false
        if (required && !cfm.equals(pwd)) {
            confirmText.setError(errMsg);
            return false;
        };
        return true;
    }

    public static boolean inRange(EditText editText,Integer min, Integer max){
        String txt = editText.getText().toString();
        return true;
    }

    public static boolean  numberFixed(EditText editText, String errMsg, Integer limit ) {
        String txt = editText.getText().toString();
        editText.setError(null);
        if (txt.length() != limit){
            editText.setError(errMsg);
            return false;
        }
        return true;
    }


    public static boolean isValid(EditText editText,String regex,String errMsg, boolean required){
        // text required and editText is blank, so return false
        String txt = editText.getText().toString().trim();
        editText.setError(null);
        if ( required && !hasText(editText) ) return false;
        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, txt)) {
            editText.setError(errMsg);
            return false;
        };
        return true;
    }

    public static boolean hasText(EditText editText){
        String txt = editText.getText().toString().trim();
        editText.setError(null);
        if(txt.length() == 0){
            return false;
        }else{
            return true;
        }
    }
}
