package org.operatorfoundation.shadow;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.libsodium.jni.NaCl;
import org.libsodium.jni.Sodium;
import org.operatorfoundation.shapeshifter.shadow.java.ShadowConfig;
import org.operatorfoundation.shapeshifter.shadow.java.ShadowSocket;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void libsodiumTest() {
        String message = "Test Message";
        Sodium sodium = NaCl.sodium();

        byte[] nonce = new byte[Sodium.crypto_aead_chacha20poly1305_npubbytes()];
        byte[] key = new byte[Sodium.crypto_aead_chacha20poly1305_keybytes()];
        Sodium.randombytes_buf(nonce, nonce.length);
        Sodium.randombytes_buf(key, key.length);

        int mlen = message.length();
        int[] clen_p = new int[0];
        byte[] c = new byte[mlen + Sodium.crypto_aead_chacha20poly1305_abytes()];

        int encryptReturn = Sodium.crypto_aead_chacha20poly1305_encrypt(c, clen_p, message.getBytes(), mlen, new byte[0], 0, new byte[0], nonce, key);

        assertEquals(0, encryptReturn);

        byte[] m = new byte[message.length()];

        int[] mlen_p = new int[1];

        Sodium.crypto_aead_chacha20poly1305_decrypt(m, mlen_p, new byte[0], c, c.length, new byte[0], 0, nonce, key);
        assertEquals(message, new String(m));
    }

@Test
public void ShadowConnect(Context applicationContext) throws IOException {
    try{

        ShadowConfig config = new ShadowConfig("1234", "AES-128-GCM");
        ShadowSocket socket = new ShadowSocket(config,"1234", 2222);
        //**********************Operaotor************
        String plaintext = "GET / HTTP/1.0\r\n\r\n";
        byte[] textBytes = plaintext.getBytes();
        try {
            socket.getOutputStream().write(textBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.getOutputStream().flush();
            byte[] textOutput = new byte[2];
            System.out.println("Output.before read"+textOutput.toString());
            socket.getInputStream().read(textOutput); 			System.out.println("Output after read"+textOutput.toString());

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }

}



}