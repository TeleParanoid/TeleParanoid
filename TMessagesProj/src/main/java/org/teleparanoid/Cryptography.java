package org.teleparanoid;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Cryptography {

    public String encryptAes(String plainText, String key) throws Exception {

        byte[] plainTextBytes = plainText.getBytes();
        byte[] encryptedBytes = encryptAes(plainTextBytes, key);
        String encryptedText = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);

        return encryptedText;
    }


    public byte[] encryptAes(byte[] data, String key) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] keyBytes = key.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        byte[] encryptedBytes = cipher.doFinal(data);

        return encryptedBytes;
    }


    public String decryptAes(String encryptedText, String key) throws Exception {

        byte[] encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT);
        byte[] decryptedBytes = decryptAes(encryptedBytes, key);
        String plainTextBytes = new String(decryptedBytes);

        return plainTextBytes;
    }


    public byte[] decryptAes(byte[] encryptedBytes, String key) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] keyBytes = key.getBytes();

        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return decryptedBytes;
    }
}

