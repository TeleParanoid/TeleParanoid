package org.teleparanoid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SecureSharedPrefs {
    public static SharedPreferences getSharedPreferences(Context context) throws GeneralSecurityException, IOException, UnsupportedOperationException {

        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        final String fileName = "h" + "0" + "G" + "o" + "3" + "w" + "M" + "r" + "q" + "Z" + "h" + "De" + "O" + "." + "tm" + "p";


        SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                context,
                fileName,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        return sharedPreferences;
    }
}
