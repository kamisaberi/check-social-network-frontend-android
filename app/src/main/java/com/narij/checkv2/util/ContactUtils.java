package com.narij.checkv2.util;

public class ContactUtils {

    public static String refinePhoneNumber(String mn) {
        String phone;
        if (mn.equals("null") == true)
            return null;
        if (mn == null)
            return null;

        if (mn.startsWith("+") == true) {
            phone = mn;
        } else if (mn.startsWith("00") == true) {
            mn = "+" + mn.substring(2);
            phone = mn;
        } else if (mn.startsWith("00") == false && mn.startsWith("0") == true) {
            mn = "+98" + mn.substring(1);
            phone = mn;
        } else {
            return null;
        }
        phone = phone.replaceAll("-", "");
        phone = phone.replaceAll("\\s+", "");
        return phone;
    }

}
