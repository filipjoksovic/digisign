package org.example;

import digisign.signers.DigitalSigner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class App {
    JFrame mainFrame;
    String title = "DigiSign";
    int width = 400;
    int height = 800;
    int padding = 20;

    JButton createKeyButton;
    JLabel createKeyLabel;


    public void init(){
       this.mainFrame = new JFrame();

        this.initKeyGenSection();

        mainFrame.setTitle("Digisign");
        mainFrame.setSize(400,800);
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void initKeyGenSection(){
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
                }
                catch(Exception ex){

                    System.out.println("Error occured");
                }
            }
        });
        mainFrame.add(createKeyButton);
    }

    public static void main(String[] args){
        App app = new App();
        app.init();
    }
}
