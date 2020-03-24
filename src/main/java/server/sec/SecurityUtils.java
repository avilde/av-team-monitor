package server.sec;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@SuppressWarnings("ALL")
public class SecurityUtils {
    /*  === server.protection config === */
    // random 8-byte key
    private static String CIPHER_KEY = "_70v3RpoWer3D&9_c1pH3rP0l";
    private byte[] SALT_KEY = {
            (byte) 0xA5, (byte) 0x9C, (byte) 0xC8, (byte) 0x34,
            (byte) 0x56, (byte) 0x3E, (byte) 0xE3, (byte) 0x03
    };
    private int SALT_ITERATIONS = 20;
    private String ENCODING = "UTF-8";
    private String ALGORITHM = "PBEWithMD5AndDES";
    /*  === server.protection config: end === */

    // reusables
    private Cipher ecipher;
    private Cipher dcipher;

    public SecurityUtils() {

    }

    public String encrypt(String plainText)
            throws NoSuchAlgorithmException,
            InvalidKeySpecException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            UnsupportedEncodingException,
            IllegalBlockSizeException,
            BadPaddingException {

        KeySpec keySpec = new PBEKeySpec(CIPHER_KEY.toCharArray(), SALT_KEY, SALT_ITERATIONS);
        SecretKey key = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(keySpec);

        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT_KEY, SALT_ITERATIONS);

        ecipher = Cipher.getInstance(key.getAlgorithm());
        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

        byte[] in = plainText.getBytes(ENCODING);
        byte[] out = ecipher.doFinal(in);

        return new String(Base64.getEncoder().encode(out));
    }

    public String decrypt(String encryptedText)
            throws NoSuchAlgorithmException,
            InvalidKeySpecException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException,
            IOException {

        KeySpec keySpec = new PBEKeySpec(CIPHER_KEY.toCharArray(), SALT_KEY, SALT_ITERATIONS);
        SecretKey key = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(keySpec);

        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT_KEY, SALT_ITERATIONS);

        dcipher = Cipher.getInstance(key.getAlgorithm());
        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        byte[] enc = Base64.getDecoder().decode(encryptedText);
        byte[] utf8 = dcipher.doFinal(enc);

        return new String(utf8, ENCODING);
    }
}