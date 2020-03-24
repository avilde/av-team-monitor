package server.sec;

import global.L;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ChangePassword {

    public static void main(String[] args) {
        SecurityUtils secUtils = new SecurityUtils();

        try {
            L.LOGGER.info(secUtils.encrypt(""));
        } catch (NoSuchAlgorithmException |
                InvalidKeySpecException |
                InvalidKeyException |
                NoSuchPaddingException |
                InvalidAlgorithmParameterException |
                BadPaddingException |
                IllegalBlockSizeException |
                IOException e) {
            e.printStackTrace();
        }
    }
}
