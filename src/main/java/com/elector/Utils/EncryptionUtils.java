package com.elector.Utils;

import com.elector.Objects.Entities.*;
import com.elector.Objects.General.BaseUser;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionUtils.class);

    private static String key = "Bar12345Bar12345"; // 128 bit key
    private static String initVector = "RandomInitVector"; // 16 bytes IV

    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            LOGGER.error("encrypt", ex);
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            LOGGER.error("exception in decryptUser since it does not sent encoded");
        }

        return null;
    }

    public static String encryptUser(BaseUser user) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("class").append(":").append(user.getClass().getSimpleName()).append(";");
            stringBuilder.append("oid").append(":").append(user.getOid()).append(";");
            stringBuilder.append("email").append(":").append(user.getEmail()).append(";");
            stringBuilder.append("password").append(":").append(user.getMD5Password()).append(";");
            return encrypt(stringBuilder.toString());
        } catch (Exception ex) {
            LOGGER.error("encryptUser", ex);
        }
        return null;
    }

    public static BaseUser decryptUser(String encrypted) {
        try {
            String plain = decrypt(encrypted);
            if (plain != null) {
                String[] splitted = plain.split(";");
                Map<String, String> data = new HashMap<>();
                for (String token : splitted) {
                    String[] keyValue = token.split(":");
                    if (keyValue.length == 2) {
                        data.put(keyValue[0], keyValue[1]);
                    }
                }
                BaseUser baseUser = new BaseUser();
                switch (data.get("class")) {
                    case "AdminUserObject":
                        baseUser = new AdminUserObject();
                        break;
                    case "CallerObject":
                        baseUser = new CallerObject();
                        break;
                    case "ActivistObject":
                        baseUser = new ActivistObject();
                        break;
                    case "ObserverObject":
                        baseUser = new ObserverObject();
                        break;
                    case "DriverObject":
                        baseUser = new DriverObject();
                        break;
                    case "GroupManagerObject":
                        baseUser = new GroupManagerObject();
                        break;

                }
                baseUser.setOid(Integer.valueOf(data.get("oid")));
                baseUser.setEmail(data.get("email"));
                baseUser.setPassword(data.get("password"));
                return baseUser;
            }
        } catch (Exception ex) {
            LOGGER.error("exception in decryptUser since it does not sent encoded");
        }
        return null;
    }


}
