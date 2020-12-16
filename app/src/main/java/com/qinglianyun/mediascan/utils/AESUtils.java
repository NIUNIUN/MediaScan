package com.qinglianyun.mediascan.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 学习链接：https://blog.csdn.net/goytao/article/details/106325970
 * Created by tang_xqing on 2020/12/8.
 */
public class AESUtils {

    // 加密key
    private final static String KEY = "1368eb9458001afc";

    private final static String HEX = "0123456789ABCDEF";

    /**
     * 加密模式：CBC。  CBC有向量模式；ECB无向量模式
     * <p>
     * 数据块：128。
     * <p>
     * 填充：PKCS5Padding。  NoPadding：位数不足不自动补位，需要开发者使用0补位； PKCS5Padding：位数不足使用余数补位。（例如{65,65,65,5,5,5,5,5}）
     * <p>
     * <p>
     * AES: 密钥和向量长度16；  DES：密钥和向量长度：8；   DES3：密钥长度为24，向量长度为8
     */
    private static final String CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM_AES = "AES";
    private static final int KEY_SIZE = 16;

    private static final String SHA1PRNG = "SHA1PRNG";
    private static String CRYPTO_PROVIDER = "Crypto";

    /**
     * 加密
     *
     * @param text
     * @return
     */
    public static String encrypt(String text) {
        if (TextUtils.isEmpty(text)) {
            return text;
        }
        byte[] encrypt = encrypt(KEY, text.getBytes());
        Log.e("测试", " 加密密文 = " + toHex(encrypt));
        return new String(Base64.encode(encrypt, Base64.DEFAULT));
    }

    public static byte[] encrypt(String key, byte[] text) {
        try {
            byte[] bytes = generatorKey(key.getBytes());

            /**
             * SecretKeySpec : 采用某种加密算法加密后的密钥
             *
             * 采用某种加密算法加密后的密钥
             */
            SecretKeySpec keySpec = new SecretKeySpec(bytes, ALGORITHM_AES);

            // Cipher：提供了用于加密和解密的功能
            Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));

            // 在单部分操作中加密数据
            byte[] bytes1 = cipher.doFinal(text);

            return bytes1;
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

    /**
     * 解密
     */
    public static String decrypt(String encrypted) {
        if (TextUtils.isEmpty(encrypted)) {
            return encrypted;
        }
        try {
            byte[] enc = Base64.decode(encrypted, Base64.DEFAULT);
            byte[] result = decrypt(KEY, enc);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     */
    private static byte[] decrypt(String key, byte[] encrypted) throws Exception {
        byte[] raw = generatorKey(key.getBytes());
        SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM_AES);
        Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    /**
     * 根据密钥种子生成对应的安全密钥。
     * 利用安全随机数生成的密钥比较安全。
     *
     * @param key 相同字节数组类型的密钥种子，可以生成相同的密钥
     * @return
     */
    @SuppressLint("DeletedProvider")
    private static byte[] generatorKey(byte[] key) {
        try {
            //KeyGenerator:密钥生成器 指定AES生成密钥
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM_AES);

            // SecureRandom 安全随机数
            SecureRandom secureRandom = null;

            int sdkInt = Build.VERSION.SDK_INT;

            if (sdkInt >= Build.VERSION_CODES.P) {    // 9.0   Crypto provider 在 Android N（7.0）中已弃用，在Android P（9.0） 中已删除。使用谷歌提供的InsecureSHA1PRNGKeyDerivator类
                return InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(key, KEY_SIZE);

            } else if (sdkInt >= Build.VERSION_CODES.N) {  // 7.0及以上
                // 指定随机数生成器算法  SHA1PRNG算法：
                secureRandom = SecureRandom.getInstance(SHA1PRNG, new CryptoProvider());
            } else if (sdkInt >= 17) {  // 4.2及以上
                secureRandom = SecureRandom.getInstance(SHA1PRNG, CRYPTO_PROVIDER);
            } else {
                secureRandom = SecureRandom.getInstance(SHA1PRNG);
            }

            // 重新调整随机对象，补充而不是替代现有的种子
            secureRandom.setSeed(key);    // 种子相同生成的密钥也相同

            // 使用用户提供的随机源生成密钥。 必须指定密钥大小为128。
            generator.init(128, secureRandom);

            // 生成密钥
            SecretKey secretKey = generator.generateKey();

            byte[] encoded = secretKey.getEncoded();
            Log.e("测试", "随机数 = " + toHex(secureRandom.generateSeed(128)));
            Log.e("测试", "密钥 = " + toHex(encoded));
            return encoded;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    //二进制转字符
    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    static class CryptoProvider extends Provider {

        protected CryptoProvider() {
            super("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
            put("SecureRandom.SHA1PRNG",
                    "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
            put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        }
    }
}
