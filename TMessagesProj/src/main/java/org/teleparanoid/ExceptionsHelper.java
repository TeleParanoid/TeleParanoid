package org.teleparanoid;

import org.telegram.messenger.ApplicationLoader;

public class ExceptionsHelper {

    public static void throwWhenSdkLess(int sdk) throws UnsupportedOperationException {
        if (android.os.Build.VERSION.SDK_INT < sdk) {
            throw new UnsupportedOperationException("Minimum SDK: " + sdk);
        }
    }


    public static void throwWhenContextIsNotInit() {
        if (ApplicationLoader.applicationContext == null) {
            throw new NullPointerException("ApplicationLoader.applicationContext was not initialized.");
        }
    }
}
