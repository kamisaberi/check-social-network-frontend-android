package com.narij.checkv2.env;

import android.graphics.Typeface;
import android.os.Parcelable;

import com.narij.checkv2.model.ContactModel;
import com.narij.checkv2.model.Duty;
import com.narij.checkv2.model.Expert;
import com.narij.checkv2.model.Group;
import com.narij.checkv2.model.Priority;
import com.narij.checkv2.model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Globals {

    //    public static String BASE_URL = "http://192.168.1.200/laravel-check/public/";
    public static String BASE_URL = "http://check.narij.ir/public/";
    public static String PROFILE_URL = BASE_URL + "uploads/";

    public static File selectedProfilePhotoToUpload;


    public static User loggedInUser = new User();

    public static Typeface regularRoboto;
    public static Typeface boldRoboto;

    public static final String LOG_TAG = "CHECK_APP";

    public static final int SPLASH_TIME_OUT = 2000;

    public static final boolean DEBUG_MODE = true;

    public static ArrayList<Duty> duties = new ArrayList<>();
    public static ArrayList<Group> groups = new ArrayList<>();

    public static ArrayList<Group> justGroups = new ArrayList<>();
    public static ArrayList<Expert> experts = new ArrayList<>();


    //    public static ArrayList<MyExpandableGroup> exGroups = new ArrayList<>();
    public static ArrayList<User> friends = new ArrayList<>();
    public static int selectedDutyIndex = 0;
    public static int selectedDutyId = 1;


    public static final HashMap<String, Long> milliseconds = new HashMap<>();
    //    public static final HashMap<String, Integer> priorities = new HashMap<>();
    public static final ArrayList<Priority> priorities = new ArrayList<>();


    public static final String dateTimeFormat = "yyyy/MM/dd HH:mm";
    public static final String dateFormat = "EEE MMM dd yyyy";
//    public static final String dateTimeFormat = "yyyy/MM/dd HH:mm:ss";

    public static final String contactListFile = "contacts.json";
    public static Set<ContactModel> contacts = new HashSet<>();


    public static ArrayList<User> registeredUsers = new ArrayList<>();


    public static HashMap<Integer, String> friendship = new HashMap<>();

    public static HashMap<Integer, String> friendshipAction = new HashMap<>();
    public static HashMap<Integer, Integer> friendshipActionCode = new HashMap<>();

    public static Duty newDutyToRegister = new Duty();

    public static Parcelable[] getGroups() {
        return groups.toArray(new Parcelable[0]);
    }

    public static void setGroups(ArrayList<Group> groups) {
        Globals.groups = groups;
    }


    public static final String SHARED_PREF = "check.pref";
    public static final String PREF_LAST_CONTACT_UPDATE_KEY = "last_contact_update";
    public static final String PREF_PHONE_KEY = "phone";
    public static final String PREF_PASSWORD_KEY = "password";






}
