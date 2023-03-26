package org.example;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException, SignatureException, InvalidKeySpecException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA","SUN");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG","SUN");
        keyPairGenerator.initialize(1024,random);
        KeyPair pair = keyPairGenerator.generateKeyPair();
        PrivateKey priv = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();
        Signature dsaInstance = Signature.getInstance("SHA1withDSA", "SUN");
        dsaInstance.initSign(priv);
        FileInputStream fis = new FileInputStream("toSign.txt"); //specify the file name
        BufferedInputStream bufin = new BufferedInputStream(fis);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = bufin.read(buffer)) >= 0)
        {
            dsaInstance.update(buffer, 0, len);
        };
        bufin.close();
        byte[] realSig = dsaInstance.sign();
        FileOutputStream sigfos = new FileOutputStream("sig"); //saving signature in the file sig
        sigfos.write(realSig); //writes in the file
        sigfos.close(); //closes the file
        byte[] key = publicKey.getEncoded();  //getting encoded key in bytes
        FileOutputStream keyfos = new FileOutputStream("publickey"); //file name in which key will store
        keyfos.write(key); //writes in the file
        keyfos.close(); //closes the file


        //Verify
        FileInputStream keyfis = new FileInputStream("publickey");  //specify the file name that contains public key
        byte[] encKey = new byte[keyfis.available()];  //byte array converted into the encoded public key bytes
        keyfis.read(encKey);
        keyfis.close();

        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
        FileInputStream sigfis = new FileInputStream("sig"); //specify the file name that contains signature
        byte[] sigToVerify = new byte[sigfis.available()];  //contains the signature bytes
        sigfis.read(sigToVerify);
        sigfis.close();
        Signature sig = Signature.getInstance("SHA1withDSA", "SUN"); //object of Signature class provides algorithm name with the provider
        sig.initVerify(pubKey);
        FileInputStream datafis = new FileInputStream("toSign.txt");  //specify the file name for which digital signature was generated
        BufferedInputStream bufin2 = new BufferedInputStream(datafis);
        byte[] buffer2 = new byte[1024];
        int len2;
        while (bufin2.available() != 0)
        {
            len2 = bufin2.read(buffer2);
            sig.update(buffer2, 0, len2);
        };
        bufin2.close();
        boolean verifies = sig.verify(sigToVerify); //returns true if the signature matches
        System.out.println("signature verifies: " + verifies);


    }
}