package seedu.ptman.commons.encrypter;

import static org.junit.Assert.assertEquals;
import static seedu.ptman.commons.encrypter.DataEncrypter.decrypt;
import static seedu.ptman.commons.encrypter.DataEncrypter.encrypt;

import org.junit.Test;

public class EncrypterTest {

    @Test
    public void encrypt_validString_returnsCorrectEncryptedString() throws Exception {
        String plainText = "Alex Yeoh";
        String expectedCipherText = "CurU9CCNY0mueTcpOaMg/w==";
        String cipherText = encrypt(plainText);
        assertEquals(cipherText, expectedCipherText);
    }

    @Test
    public void decrypt_validString_returnsCorrectDecryptedString() throws Exception {
        String expectedPlainText = "Alex Yeoh";
        String cipherText = "CurU9CCNY0mueTcpOaMg/w==";
        String plainText = decrypt(cipherText);
        assertEquals(plainText, expectedPlainText);
    }
}
