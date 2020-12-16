package com.qinglianyun.mediascan.utils;

import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by tang_xqing on 2020/12/8.
 */
public class CryptoUtils {

    //    private final static String DEFAULT_USER_ALIAS = "default_user_alias";
    private final static String DEFAULT_USER_ALIAS = "default_secretkey_name";

    private final static String FILE_KEY_STORE = "Cryptp_file";

    private final static String KEY = "AES";
    private final static String ANDROID_KEY_STORE_PROIVER = "AndroidKeyStore";
    private final static String sSHA1PRNG = "SHA1PRNG";
    private static final String CBC_PKCS7_PADDING = "AES/CBC/PKCS7Padding";

    private static CryptoUtils sCryptoUtils;

    private KeyStore mKeyStore;

    private CryptoUtils(KeyStore keyStore) {
        this.mKeyStore = keyStore;
    }

    public static CryptoUtils getInstance(Context context) {
        if (null == sCryptoUtils) {
            synchronized (CryptoUtils.class) {
                if (null == sCryptoUtils) {
                    File file = new File(context.getFilesDir(), FILE_KEY_STORE);

                    KeyStore keyStore = generatorKeystore(file);
                    initKey(keyStore, file);
                    sCryptoUtils = new CryptoUtils(keyStore);
                }
            }
        }
        return sCryptoUtils;
    }

    public EncryptData encrypt(String alias, String content) {
        SecretKey secreKey = getSecreKey();
        try {

//             为什么使用PKCS7Padding模式，加密没问题；使用PKCS5Padding，提示“Attempt to get length of null array”
            Cipher cipher = Cipher.getInstance(CBC_PKCS7_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secreKey);
            byte[] bytes = cipher.doFinal(content.getBytes());
            String encode = Base64.encodeToString(bytes, Base64.NO_WRAP);
            EncryptData data = new EncryptData(alias, encode, cipher.getIV());
            return data;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(EncryptData content) {
        byte[] decode = Base64.decode(content.encryptString, Base64.NO_WRAP);
        SecretKey secreKey = getSecreKey();
        try {
            Cipher cipher = Cipher.getInstance(CBC_PKCS7_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secreKey, new IvParameterSpec(content.getIv()));
            byte[] bytes = cipher.doFinal(decode);
            return new String(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private SecretKey getSecreKey() {
        try {
            return (SecretKey) mKeyStore.getKey(DEFAULT_USER_ALIAS, null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化证书
     *
     * @param keyStore
     * @param file
     */
    private static void initKey(KeyStore keyStore, File file) {
        try {
            if (!keyStore.containsAlias(DEFAULT_USER_ALIAS)) {
                KeyGenerator keyGenerator = generateKeyGenerator();
                SecretKey secretKey = keyGenerator.generateKey();
                storeKey(keyStore, file, secretKey);
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 存储证书
     *
     * @param keyStore
     * @param file
     * @param secretKey
     */
    private static void storeKey(KeyStore keyStore, File file, SecretKey secretKey) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        int sdkInt = Build.VERSION.SDK_INT;
        keyStore.setKeyEntry(DEFAULT_USER_ALIAS, secretKey, null, null);
        if (sdkInt < 23) {
            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            keyStore.store(fos, null);
            fos.close();
        }
    }

    /**
     * 生成密钥
     *
     * @return
     */
    private static KeyGenerator generateKeyGenerator() {
        KeyGenerator keyGenerator = null;
        try {
            int sdkInt = Build.VERSION.SDK_INT;

            if (sdkInt >= 23) {  // 6.0及以上
                // AndroidKeyStore主要是存储密钥key
                keyGenerator = KeyGenerator.getInstance(KEY, ANDROID_KEY_STORE_PROIVER);
                keyGenerator.init(new KeyGenParameterSpec.Builder(DEFAULT_USER_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(false)
                        // Require that the user has unlocked in the last 30 seconds
//                            .setUserAuthenticationValidityDurationSeconds(AUTHENTICATION_DURATION_SECONDS)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());

            } else {
                keyGenerator = KeyGenerator.getInstance(KEY);
                SecureRandom secureRandom = null;
                if (sdkInt >= 17) {  //4.2及以上
                    secureRandom = SecureRandom.getInstance(sSHA1PRNG, "Crypto");

                } else {
                    secureRandom = SecureRandom.getInstance("SHA1PRNG");
                }
                secureRandom.setSeed(generateSeed());
                keyGenerator.init(128, secureRandom);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return keyGenerator;
    }

    private static byte[] generateSeed() {
        try {
            ByteArrayOutputStream seedBuffer = new ByteArrayOutputStream();
            DataOutputStream seedBufferOut =
                    new DataOutputStream(seedBuffer);
            seedBufferOut.writeLong(System.currentTimeMillis());
            seedBufferOut.writeLong(System.nanoTime());
            seedBufferOut.writeInt(android.os.Process.myPid());
            seedBufferOut.writeInt(android.os.Process.myUid());
            seedBufferOut.write(Build.BOARD.getBytes());
            return seedBuffer.toByteArray();
        } catch (IOException e) {
            throw new SecurityException("Failed to generate seed", e);
        }
    }

    /**
     * 生成密钥库
     * <p>
     * KeyStore： 加密密钥和证书的存储工具。 AndroidKeyStore对于SecretKey只支持AES和HMAC
     *
     * @return
     */
    private static KeyStore generatorKeystore(File file) {
        try {
            int sdkInt = Build.VERSION.SDK_INT;
            if (sdkInt >= 23) { // 6.0及以上版本
                KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE_PROIVER);
                keyStore.load(null);
                return keyStore;
            } else if (sdkInt >= 14) {
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                if (!file.exists()) {
                    boolean newFile = file.createNewFile();
                    if (!newFile) {
                        throw new SecurityException("创建内部文件失败");
                    }

                    keyStore.load(null, null);

                } else if (file.length() <= 0) {
                    keyStore.load(null, null);

                } else {
                    FileInputStream fis = null;
                    fis = new FileInputStream(file);
                    keyStore.load(fis, null);
                    fis.close();
                }

                return keyStore;
            } else {
                throw new RuntimeException("兼容只Android 4.0以上版本");
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
