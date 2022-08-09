package com.bithumbsystems.management.api.core.util;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

/**
 * The type Aes 256 util.
 */
@Slf4j
public class AES256Util {
    public static final String CLIENT_AES_KEY_ADM = "fWISVCRBVpGh25HCS1U3a6bwqYewKUop";

    /**
     * Encrypt (AES)
     *
     * @param keyString the key string
     * @param plainText the plain text
     * @param bUrlSafe  the b url safe
     * @return string
     */
    public static String encryptAES(String keyString, String plainText, boolean bUrlSafe) {
        String cipherText = "";
        if ((keyString == null) || keyString.length() == 0 || (plainText == null) || plainText.length() == 0) {
            throw new RuntimeException("Key is not found!");
        }

        // 키의 길이는 16, 24, 32 만 지원
        if ((keyString.length() != 16) && (keyString.length() != 24) && (keyString.length() != 32)) {
            throw new RuntimeException("Key is invalidate");
        }

        try {
            byte[] keyBytes = keyString.getBytes(StandardCharsets.UTF_8);
            byte[] plainTextBytes = plainText.getBytes(StandardCharsets.UTF_8);

            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            int bsize = cipher.getBlockSize();
            byte[] iv = Arrays.copyOfRange(keyBytes, 0, bsize);
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeySpec secureKey = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secureKey, ivspec);
            byte[] encrypted = cipher.doFinal(plainTextBytes);

            if (bUrlSafe) {
                cipherText = Base64.encodeBase64URLSafeString(encrypted);
            } else {
                cipherText = new String(Base64.encodeBase64(encrypted), StandardCharsets.UTF_8);
            }

        } catch (Exception e) {
            cipherText = "";
            e.printStackTrace();
        }

        log.debug("chipherTest => {}", cipherText);

        return cipherText;
    }


    /**
     * Decrypt (AES)
     *
     * @param keyString  the key string
     * @param cipherText the cipher text
     * @return string
     */
    public static String decryptAES(String keyString, String cipherText) {
        String plainText = "";
        if ((keyString == null) || keyString.length() == 0 || (cipherText == null) || cipherText.length() == 0) {
            return plainText;
        }

        if ((keyString.length() != 16) && (keyString.length() != 24) && (keyString.length() != 32)) {
            return plainText;
        }

        try {
            byte[] keyBytes = keyString.getBytes(StandardCharsets.UTF_8);
            byte[] cipherTextBytes = Base64.decodeBase64(cipherText.getBytes(StandardCharsets.UTF_8));

            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            int bsize = cipher.getBlockSize();
            IvParameterSpec ivspec = new IvParameterSpec(Arrays.copyOfRange(keyBytes, 0, bsize));

            SecretKeySpec secureKey = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secureKey, ivspec);
            byte[] decrypted = cipher.doFinal(cipherTextBytes);

            plainText = new String(decrypted, StandardCharsets.UTF_8);

        } catch (Exception e) {
            plainText = "";
            e.printStackTrace();
        }

        return plainText;
    }
}
