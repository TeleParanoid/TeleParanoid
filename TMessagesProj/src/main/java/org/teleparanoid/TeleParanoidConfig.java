package org.teleparanoid;

import static org.teleparanoid.ExceptionsHelper.throwWhenContextIsNotInit;

import android.content.SharedPreferences;


import org.telegram.messenger.ApplicationLoader;

import java.io.IOException;
import java.security.GeneralSecurityException;


public class TeleParanoidConfig {

    public static int getApiId() throws GeneralSecurityException, IOException, NullPointerException, UnsupportedOperationException {

        return getInt("APP_ID", TeleParanoidBuildVars.APP_ID);
    }

    public static void setApiId(int appId) throws GeneralSecurityException, IOException, NullPointerException, UnsupportedOperationException {

        org.telegram.messenger.BuildVars.APP_ID = appId;
        setInt("APP_ID", appId);
    }

    public static String getAppHash() throws GeneralSecurityException, IOException, NullPointerException, UnsupportedOperationException {
        return getString("APP_HASH", TeleParanoidBuildVars.APP_HASH);
    }

    public static void setAppHash(String appHash) throws GeneralSecurityException, IOException, NullPointerException, UnsupportedOperationException {

        org.telegram.messenger.BuildVars.APP_HASH = appHash;
        setString("APP_HASH", appHash);
    }

    public static boolean isCaptureScreenAllowed() {

        boolean defaultValue = false;
        boolean value;

        try{
            value = getBoolean("isCaptureScreenAllowed", defaultValue);
        } catch (Throwable e){
            value = defaultValue;
        }

        return value;
    }

    public static void setIsCaptureScreenAllowed(boolean isCaptureScreenAllowed) throws GeneralSecurityException, IOException, NullPointerException, UnsupportedOperationException {
        setBoolean("isCaptureScreenAllowed", isCaptureScreenAllowed);
    }


    private static int getInt(String key, int defaultValue) throws GeneralSecurityException, IOException, NullPointerException, UnsupportedOperationException {
        throwWhenContextIsNotInit();

        SharedPreferences sharedPreferences = SecureSharedPrefs.getSharedPreferences(ApplicationLoader.applicationContext);
        final int value = sharedPreferences.getInt(key, defaultValue);

        return value;
    }


    private static void setInt(String key, int value) throws GeneralSecurityException, IOException, NullPointerException, UnsupportedOperationException {

        throwWhenContextIsNotInit();

        SharedPreferences sharedPreferences = SecureSharedPrefs.getSharedPreferences(ApplicationLoader.applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(key, value);
        editor.apply();
    }


    private static boolean getBoolean(String key, boolean defaultValue) throws GeneralSecurityException, IOException, NullPointerException, UnsupportedOperationException {
        throwWhenContextIsNotInit();

        SharedPreferences sharedPreferences = SecureSharedPrefs.getSharedPreferences(ApplicationLoader.applicationContext);
        final boolean value = sharedPreferences.getBoolean(key, defaultValue);

        return value;
    }


    private static void setBoolean(String key, boolean value) throws GeneralSecurityException, IOException, NullPointerException, UnsupportedOperationException {

        throwWhenContextIsNotInit();

        SharedPreferences sharedPreferences = SecureSharedPrefs.getSharedPreferences(ApplicationLoader.applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(key, value);
        editor.apply();
    }


    private static String getString(String key, String defaultValue) throws GeneralSecurityException, IOException, NullPointerException, UnsupportedOperationException {
        throwWhenContextIsNotInit();

        SharedPreferences sharedPreferences = SecureSharedPrefs.getSharedPreferences(ApplicationLoader.applicationContext);
        final String value = sharedPreferences.getString(key, defaultValue);

        return value;
    }


    private static void setString(String key, String value) throws GeneralSecurityException, IOException, NullPointerException, UnsupportedOperationException {

        throwWhenContextIsNotInit();

        SharedPreferences sharedPreferences = SecureSharedPrefs.getSharedPreferences(ApplicationLoader.applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, value);
        editor.apply();
    }
}

