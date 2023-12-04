package com.tradebit.encryption;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptionUtil {
    private final AES256TextEncryptor textEncryptor = new AES256TextEncryptor();

    @Autowired
    public void setSecretKey(@Value("${app.encryption.secret-key}") String secretKey) {
        textEncryptor.setPassword(secretKey);
    }

    public String encrypt(String data) {
        return textEncryptor.encrypt(data);
    }

    public String decrypt(String encryptedData) {
        return textEncryptor.decrypt(encryptedData);
    }
}
