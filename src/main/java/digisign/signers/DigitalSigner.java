package digisign.signers;

import java.io.*;
import java.security.*;

public class DigitalSigner {

    public static void createKeys(String location) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException, SignatureException {
        System.out.println(location);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA","SUN");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG","SUN");
        keyPairGenerator.initialize(1024,random);
        KeyPair pair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();
        byte[] key = publicKey.getEncoded();
        byte[] privateKeyEncoded = privateKey.getEncoded();
        FileOutputStream publicKeyFos = new FileOutputStream(location + ".pub");
        FileOutputStream privateKeyFos = new FileOutputStream(location + "-priv");
        publicKeyFos.write(key);
        publicKeyFos.close();
        privateKeyFos.write(privateKeyEncoded);
        privateKeyFos.close();
    }
}
