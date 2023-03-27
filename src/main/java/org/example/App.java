package org.example;

import digisign.signers.DigitalSigner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

public class App {
    JFrame mainFrame;
    String title = "DigiSign";
    int width = 400;
    int height = 800;
    int padding = 20;

    JButton createKeyButton;
    JLabel createKeyLabel;

    JButton uploadFileButton;
    JButton signFileButton;
    JButton privateKeyPickerButton;
    JLabel privateKeyLocationLabel;
    JButton publicKeyPickerButton;
    JLabel publicKeyLocationLabel;

    JButton signDocumentButton;
    JButton verifySignatureButton;

    File fileToSign;
    File privateKeyFile;
    File publicKeyFile;

    PublicKey publicKey;
    PrivateKey privateKey;


    public void init() {
        this.mainFrame = new JFrame();


        mainFrame.setTitle("Digisign");
        mainFrame.setSize(400, 800);
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);

        this.initKeyGenSection();
        this.initFileUploadSection();
        this.initPublicKeyPickerSection();
        this.initPrivateKeyPickerSection();
        this.initDocumentSignSection();
        this.initDocumentVerifySection();

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public void initKeyGenSection() {
        createKeyButton = new JButton("Create key");
        createKeyButton.setBounds(this.padding, this.padding, this.width - 2 * padding, 30);

        createKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked");
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Select a location for storing keys");
                    int selection = fileChooser.showSaveDialog(mainFrame);
                    if (selection == JFileChooser.APPROVE_OPTION) {
                        File fileToSave = fileChooser.getSelectedFile();
                        DigitalSigner.createKeys(fileToSave.getAbsolutePath());

                        System.out.println("Save as file: " + fileToSave.getAbsolutePath());
                        JOptionPane.showMessageDialog(mainFrame,
                                "Keys successfully saved at: " + fileToSave.getAbsolutePath(),
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {

                    System.out.println("Error occured");
                }
            }
        });
        mainFrame.add(createKeyButton);
    }

    public void initFileUploadSection() {
        System.out.println("Adding");
        uploadFileButton = new JButton("Pick a file");
        uploadFileButton.setBounds(padding, createKeyButton.getY() + padding + 35, mainFrame.getWidth() - 2 * padding, 30);


        uploadFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked file upload button");
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(mainFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    fileToSign = fileChooser.getSelectedFile();
                    System.out.println("Selected file: " + fileToSign.getAbsolutePath());
                }
            }
        });
        mainFrame.add(uploadFileButton);
        mainFrame.repaint();
    }

    public void initPublicKeyPickerSection() {
        System.out.println("Adding");
        publicKeyPickerButton = new JButton("Pick a public key");
        publicKeyPickerButton.setBounds(padding, uploadFileButton.getY() + padding + 35, mainFrame.getWidth() / 2 - 2 * padding, 30);


        publicKeyPickerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked file upload button");
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select location for the public key");
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(mainFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    publicKeyFile = fileChooser.getSelectedFile();
                    publicKeyLocationLabel = new JLabel("Key location: " + publicKeyFile.getAbsolutePath());
                    publicKeyLocationLabel.setBounds(padding, publicKeyPickerButton.getY() + padding, mainFrame.getWidth() / 2 - 2 * padding, 50);
                    mainFrame.add(publicKeyPickerButton);
                    mainFrame.repaint();

                    System.out.println("Selected file: " + publicKeyFile.getAbsolutePath());
                    try {
                        publicKey = DigitalSigner.readPublicKey(publicKeyFile);
                        JOptionPane.showMessageDialog(mainFrame,
                                "Public Key successfully parsed" ,
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);

                    }
                    catch (Exception exception){
                        System.out.println("Error while parsing public key");
                        exception.printStackTrace();
                    }
                }
            }
        });
        mainFrame.add(publicKeyPickerButton);
        mainFrame.repaint();
    }

    public void initPrivateKeyPickerSection() {
        System.out.println("Adding");
        privateKeyPickerButton = new JButton("Pick a private key");
        privateKeyPickerButton.setBounds(padding + publicKeyPickerButton.getWidth() + padding, publicKeyPickerButton.getY(), mainFrame.getWidth() / 2 - 2 * padding, 30);


        privateKeyPickerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked file upload button");
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select location for the private key");
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(mainFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    privateKeyFile = fileChooser.getSelectedFile();
                    privateKeyLocationLabel = new JLabel("Private key location: " + privateKeyFile.getAbsolutePath());
                    privateKeyLocationLabel.setBounds(padding, privateKeyPickerButton.getY() + padding, mainFrame.getWidth() / 2 - 2 * padding, 50);
                    mainFrame.add(privateKeyPickerButton);
                    mainFrame.repaint();

                    System.out.println("Selected file: " + privateKeyFile.getAbsolutePath());
                    try {
                        privateKey = DigitalSigner.readPrivateKey(privateKeyFile);
                        JOptionPane.showMessageDialog(mainFrame,
                                "Private key successfully parsed" ,
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);

                    }
                    catch (Exception exception){
                        System.out.println("Error while parsing public key");
                        exception.printStackTrace();
                    }
                }
            }
        });
        mainFrame.add(privateKeyPickerButton);
        mainFrame.repaint();
    }

    public void initDocumentSignSection() {
        System.out.println("Adding");
        signDocumentButton = new JButton("Sign document");
        signDocumentButton.setBounds(padding, publicKeyPickerButton.getY() + padding * 2, mainFrame.getWidth() - 2 * padding, 30);
        signDocumentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Select a location for storing signed file");
                    int selection = fileChooser.showSaveDialog(mainFrame);
                    if (selection == JFileChooser.APPROVE_OPTION) {
                        File signedDocument = fileChooser.getSelectedFile();
                        DigitalSigner.signFile(fileToSign,privateKey,publicKey, signedDocument.getPath());
                        System.out.println("Save as file: " + signedDocument.getAbsolutePath());
                        JOptionPane.showMessageDialog(mainFrame,
                                "Document signed and stored at: " + signedDocument.getAbsolutePath(),
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {

                    System.out.println("Error occured");
                }
            }
        });
        mainFrame.add(signDocumentButton);
        mainFrame.repaint();
    }

    public void initDocumentVerifySection() {
        System.out.println("Adding");
        verifySignatureButton = new JButton("Verify signature");
        verifySignatureButton.setBounds(padding, signDocumentButton.getY() + padding * 2, mainFrame.getWidth() - 2 * padding, 30);
        verifySignatureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser signedFileChooser = new JFileChooser();
                    signedFileChooser.setDialogTitle("Select a signed file");
                    int selection = signedFileChooser.showOpenDialog(mainFrame);
                    File signedFile = null;
                    if (selection == JFileChooser.APPROVE_OPTION) {
                        signedFile = signedFileChooser.getSelectedFile();
                    }

                    signedFileChooser.setDialogTitle("Select original file");
                    selection = signedFileChooser.showOpenDialog(mainFrame);
                    File originalFile = null;
                    if(selection == JFileChooser.APPROVE_OPTION){
                        originalFile = signedFileChooser.getSelectedFile();
                    }

                    signedFileChooser.setDialogTitle("Select public key for verification");
                    selection = signedFileChooser.showOpenDialog(mainFrame);
                    File publicKeyFile = null;
                    if(selection == JFileChooser.APPROVE_OPTION){
                        publicKeyFile = signedFileChooser.getSelectedFile();
                    }

                    PublicKey pubKey = DigitalSigner.readPublicKey(publicKeyFile);

                    if(pubKey != null && originalFile != null && signedFile != null){
                        DigitalSigner.verifySignature(pubKey,signedFile,originalFile);
                    }

                } catch (Exception ex) {
                    System.out.println("Error occured");
                }
            }
        });
        mainFrame.add(verifySignatureButton);
        mainFrame.repaint();
    }

    public static void main(String[] args) {
        App app = new App();
        app.init();
    }
}
