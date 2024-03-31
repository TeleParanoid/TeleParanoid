package org.teleparanoid;

import static org.teleparanoid.ExceptionsHelper.throwWhenContextIsNotInit;

import android.content.SharedPreferences;


import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.NotificationCenter;

import java.io.IOException;
import java.security.GeneralSecurityException;


public class TeleParanoidConfig extends BaseController {

    public boolean isCaptureScreenAllowed;
    public boolean shouldIgnoreReadPackets;
    public boolean shouldSetOfflineInUpdatePackets;
    public boolean shouldIgnoreSendTypingPackets;
    public boolean shouldMarkReadAfterSend;

    private boolean allowReadPacket;
    private int readHistoryCountToResetAllowReadPacket;

    private static volatile TeleParanoidConfig Instance;
    private final Object sync = new Object();
    private volatile boolean configLoaded;

    public TeleParanoidConfig(int num) {
        super(num);
    }

    public static TeleParanoidConfig getInstance(int num) {
        TeleParanoidConfig localInstance = Instance;

        if (localInstance == null) {
            synchronized (TeleParanoidConfig.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new TeleParanoidConfig(num);
                }
            }
        }

        return localInstance;
    }


    public void saveConfig() {
        NotificationCenter.getInstance(currentAccount).doOnIdle(() -> {
            if (!configLoaded) {
                return;
            }
            synchronized (sync) {
                try {
                    SharedPreferences sharedPreferences = SecureSharedPrefs.getSharedPreferences(ApplicationLoader.applicationContext);

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putBoolean("isCaptureScreenAllowed", isCaptureScreenAllowed);
                    editor.putBoolean("shouldIgnoreReadPackets", shouldIgnoreReadPackets);
                    editor.putBoolean("shouldSetOfflineInUpdatePackets", shouldSetOfflineInUpdatePackets);
                    editor.putBoolean("shouldIgnoreSendTypingPackets", shouldIgnoreSendTypingPackets);
                    editor.putBoolean("shouldMarkReadAfterSend", shouldMarkReadAfterSend);


                    editor.apply();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        });
    }


    public void loadConfig() {
        synchronized (sync) {
            if (configLoaded) {
                return;
            }
            try {
                SharedPreferences sharedPreferences = SecureSharedPrefs.getSharedPreferences(ApplicationLoader.applicationContext);

                isCaptureScreenAllowed = sharedPreferences.getBoolean("isCaptureScreenAllowed", false);
                shouldIgnoreSendTypingPackets = sharedPreferences.getBoolean("shouldIgnoreSendTypingPackets", false);
                shouldIgnoreReadPackets = sharedPreferences.getBoolean("shouldIgnoreReadPackets", false);
                shouldSetOfflineInUpdatePackets = sharedPreferences.getBoolean("shouldSetOfflineInUpdatePackets", false);
                shouldMarkReadAfterSend = sharedPreferences.getBoolean("shouldMarkReadAfterSend", false);

                configLoaded = true;
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }


    public void setAllowReadPacket(boolean val, int resetAfter) {
        allowReadPacket = val;
        readHistoryCountToResetAllowReadPacket = resetAfter;
    }

    public boolean shouldSendReadPacket() {
        if(!shouldIgnoreReadPackets)
            return true;

        synchronized (sync) {
            if (readHistoryCountToResetAllowReadPacket == -1) {
                return allowReadPacket;
            }

            readHistoryCountToResetAllowReadPacket -= 1;
            boolean currentVal = allowReadPacket;

            if (readHistoryCountToResetAllowReadPacket == 0) {
                allowReadPacket = false;
            }

            return currentVal;
        }
    }


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
