package digisign.signers;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DigitalSigner {

    public static void createKeys(String location) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException, SignatureException {
        System.out.println(location);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA", "SUN");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyPairGenerator.initialize(1024, random);
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

    public static PublicKey readPublicKey(File file) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        FileInputStream fis = new FileInputStream(file);
        byte[] key = new byte[fis.available()];
        fis.read(key);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
        return (PublicKey) keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey readPrivateKey(File file) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        FileInputStream fis = new FileInputStream(file);
        byte[] key = new byte[fis.available()];
        fis.read(key);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        return (PrivateKey) keyFactory.generatePrivate(keySpec);
    }

    public static void signFile(File file, PrivateKey privateKey, PublicKey publicKey, String signedPath) throws NoSuchAlgorithmException, NoSuchProviderException, IOException, SignatureException, InvalidKeyException {
        Signature dsaInstance = Signature.getInstance("SHA1withDSA", "SUN");
        dsaInstance.initSign(privateKey);
        FileInputStream fis = new FileInputStream(file.getAbsolutePath());
        BufferedInputStream bufin = new BufferedInputStream(fis);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = bufin.read(buffer)) >= 0) {
            dsaInstance.update(buffer, 0, len);
        }
        ;
        bufin.close();
        byte[] realSig = dsaInstance.sign();
        FileOutputStream sigfos = new FileOutputStream(signedPath); //saving signature in the file sig
        sigfos.write(realSig); //writes in the file
        sigfos.close(); //closes the file
    }

    public static void verifySignature(PublicKey pubKey, File signedFile, File originalFile) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        FileInputStream sigfis = new FileInputStream(signedFile); //specify the file name that contains signature
        byte[] sigToVerify = new byte[sigfis.available()];  //contains the signature bytes
        sigfis.read(sigToVerify);
        sigfis.close();
        Signature sig = Signature.getInstance("SHA1withDSA", "SUN"); //object of Signature class provides algorithm name with the provider
        sig.initVerify(pubKey);
        FileInputStream datafis = new FileInputStream(originalFile);  //specify the file name for which digital signature was generated
        BufferedInputStream bufin2 = new BufferedInputStream(datafis);
        byte[] buffer2 = new byte[1024];
        int len2;
        while (bufin2.available() != 0) {
            len2 = bufin2.read(buffer2);
            sig.update(buffer2, 0, len2);
        }
        bufin2.close();
        boolean verifies = sig.verify(sigToVerify); //returns true if the signature matches
        System.out.println("signature verifies: " + verifies);


    }
}
