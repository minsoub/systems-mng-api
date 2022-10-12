package com.bithumbsystems.management.api.core.cipher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class RsaCipherService {
    public final static String RSA_ALGORITHM = "RSA";
    public final static String PRIVATE_KEY_NAME = "privateKey";
    public final static String PUBLIC_KEY_NAME = "publicKey";

    public KeyPairGenerator rsaGenerator;

    public RsaCipherService() throws NoSuchAlgorithmException {
        rsaGenerator = KeyPairGenerator.getInstance(RsaCipherService.RSA_ALGORITHM);
        rsaGenerator.initialize(2048, new SecureRandom());
    }

    public Map<String, String> getRsaKeys() {
        KeyPair pair = rsaGenerator.generateKeyPair();

        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        String base64PrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String base64PublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put(RsaCipherService.PRIVATE_KEY_NAME, base64PrivateKey);
        resultMap.put(RsaCipherService.PUBLIC_KEY_NAME, base64PublicKey);

        return resultMap;
    }

    private PublicKey getPublicKeyFromBase64Encrypted(String base64PublicKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decodedBase64PubKey = Base64.getDecoder().decode(base64PublicKey);

        return KeyFactory.getInstance(RsaCipherService.RSA_ALGORITHM)
                .generatePublic(new X509EncodedKeySpec(decodedBase64PubKey));
    }

    private PrivateKey getPrivateKeyFromBase64Encrypted(String base64PrivateKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decodedBase64PrivateKey = Base64.getDecoder().decode(base64PrivateKey);

        return KeyFactory.getInstance(RsaCipherService.RSA_ALGORITHM)
                .generatePrivate(new PKCS8EncodedKeySpec(decodedBase64PrivateKey));
    }


    public String encryptRSA(String plainText, String publicKey) {
        String encryptedText = "";

        try {
            Cipher cipher = Cipher.getInstance(RsaCipherService.RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, this.getPublicKeyFromBase64Encrypted(publicKey));

            byte[] bytePlain = cipher.doFinal(plainText.getBytes());
            encryptedText = Base64.getEncoder().encodeToString(bytePlain);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return encryptedText;
    }

    public String decryptRSA(String encryptedText, String privateKey) {
        String plainText = "";

        try {
            Cipher cipher = Cipher.getInstance(RsaCipherService.RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, this.getPrivateKeyFromBase64Encrypted(privateKey));
            byte[] byteEncrypted = Base64.getDecoder().decode(encryptedText.getBytes());

            byte[] bytePlain = cipher.doFinal(byteEncrypted);
            plainText = new String(bytePlain, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return plainText;
    }
}
