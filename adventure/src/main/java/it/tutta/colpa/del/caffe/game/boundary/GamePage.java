/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.boundary;

import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.game.control.GameController;
import it.tutta.colpa.del.caffe.game.exception.ImageNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * @author giovav
 * @since 10/07/2025
 */
public class GamePage extends javax.swing.JFrame implements GameGUI {

    private GameController controller;


    public GamePage() {
        // <editor-fold defaultstate="collapsed" desc="< Java Layout >">
        // Imposta il layout predefinito di Java
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }// </editor-fold>>
        initComponents();
        this.setVisible(true);
    }

    private int showYesNoDialoguePage(String title, String message) {
        return javax.swing.JOptionPane.showConfirmDialog(
                null,
                message,
                title,
                javax.swing.JOptionPane.YES_NO_OPTION
        );
    }

    public void showError(String title, String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }
    public void showWarning(String title, String message) {
        JOptionPane.showMessageDialog(
                this,
                 message,
                title,
                JOptionPane.WARNING_MESSAGE
        );
    }

    // <editor-fold defaultstate="collapsed" desc="< GUI variables >">
    private javax.swing.JPanel DialogPanel;
    private javax.swing.JTextArea DialogTextArea;
    private javax.swing.JPanel FooterPanel;
    private javax.swing.JPanel HeaderPanel;
    private javax.swing.JLabel ImageLabel;
    private javax.swing.JPanel ImagePanel;
    private javax.swing.JButton InvButton;
    private javax.swing.JTextField inputField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainContainer;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton quitButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton sendButton;
    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(GamePage.class.getName());
    // </editor-fold>

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="< GUI init >">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainContainer = new javax.swing.JPanel() {
            private final Image wp;

            {
                URL imgUrl = getClass().getResource("/images/gameCover.png");
                if (imgUrl != null) {
                    wp = new ImageIcon(imgUrl).getImage();
                } else {
                    wp = null;
                    System.err.println("Immagine non trovata: images/gameCover.png");
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (wp != null) {
                    g.drawImage(wp, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        HeaderPanel = new javax.swing.JPanel();
        ImagePanel = new javax.swing.JPanel();
        ImageLabel = new javax.swing.JLabel();
        DialogPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        DialogTextArea = new javax.swing.JTextArea();
        FooterPanel = new javax.swing.JPanel();
        inputField = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar(0, 1200);
        quitButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        InvButton = new javax.swing.JButton();
        HeaderPanel.setOpaque(false);
        HeaderPanel.setBackground(new java.awt.Color(0, 0, 0, 0));
        ImagePanel.setOpaque(false);
        ImagePanel.setBackground(new java.awt.Color(0, 0, 0, 0));
        FooterPanel.setOpaque(false);
        FooterPanel.setBackground(new java.awt.Color(0, 0, 0, 0));
        DialogPanel.setOpaque(false);
        DialogPanel.setBackground(new java.awt.Color(0, 0, 0, 0));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        setTitle("Tutta Colpa del Caffè!");
        setResizable(false);

        javax.swing.GroupLayout HeaderPanelLayout = new javax.swing.GroupLayout(HeaderPanel);
        HeaderPanel.setLayout(HeaderPanelLayout);
        HeaderPanelLayout.setHorizontalGroup(
                HeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );
        HeaderPanelLayout.setVerticalGroup(
                HeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 50, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout ImagePanelLayout = new javax.swing.GroupLayout(ImagePanel);
        ImagePanel.setLayout(ImagePanelLayout);
        ImagePanelLayout.setHorizontalGroup(
                ImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(ImageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE)
        );
        ImagePanelLayout.setVerticalGroup(
                ImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(ImageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        DialogPanel.setBackground(new java.awt.Color(204, 204, 255, 0));

        saveButton.setBackground(Color.WHITE);
        quitButton.setBackground(Color.WHITE);
        sendButton.setBackground(Color.WHITE);
        InvButton.setBackground(Color.WHITE);

        DialogTextArea.setEditable(false);
        DialogTextArea.setBackground(new java.awt.Color(255, 255, 255, 128));
        DialogTextArea.setColumns(20);
        DialogTextArea.setRows(5);
        DialogTextArea.setFocusable(false);
        DialogTextArea.setOpaque(false);
        DialogTextArea.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 15));
        DialogTextArea.setLineWrap(true);
        DialogTextArea.setWrapStyleWord(true);
        jScrollPane1.setViewportView(DialogTextArea);
        jScrollPane1.setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);

        javax.swing.GroupLayout DialogPanelLayout = new javax.swing.GroupLayout(DialogPanel);
        DialogPanel.setLayout(DialogPanelLayout);
        DialogPanelLayout.setHorizontalGroup(
                DialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
        );
        DialogPanelLayout.setVerticalGroup(
                DialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
        );

        inputField.setToolTipText("");

        sendButton.setText("Invia");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        quitButton.setText("Abbandona");
        quitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Salva");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        InvButton.setText("Zaino");
        InvButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InvButtonActionPerformed(evt);
            }
        });
        ImageLabel.setOpaque(true);
        InvButton.setIcon(
                new ImageIcon((new ImageIcon(getClass().getResource("/images/zaino_icon.png")))
                        .getImage()
                        .getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        quitButton.setIcon(
                new ImageIcon((new ImageIcon(getClass().getResource("/images/exit_icon.png")))
                        .getImage()
                        .getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        saveButton.setIcon(
                new ImageIcon((new ImageIcon(getClass().getResource("/images/save_icon.png")))
                        .getImage()
                        .getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        javax.swing.GroupLayout FooterPanelLayout = new javax.swing.GroupLayout(FooterPanel);
        FooterPanel.setLayout(FooterPanelLayout);
        FooterPanelLayout.setHorizontalGroup(
                FooterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(FooterPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(inputField, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sendButton)
                                .addGap(18, 18, 18)
                                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(InvButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(saveButton)
                                .addGap(18, 18, 18)
                                .addComponent(quitButton)
                                .addContainerGap())
        );
        FooterPanelLayout.setVerticalGroup(
                FooterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FooterPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(FooterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(InvButton, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                                        .addComponent(saveButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(quitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(inputField, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(sendButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(progressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        javax.swing.GroupLayout mainContainerLayout = new javax.swing.GroupLayout(mainContainer);
        mainContainer.setLayout(mainContainerLayout);
        mainContainerLayout.setHorizontalGroup(
                mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(HeaderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainContainerLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(DialogPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ImagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addComponent(FooterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        mainContainerLayout.setVerticalGroup(
                mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainContainerLayout.createSequentialGroup()
                                .addComponent(HeaderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(ImagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(DialogPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(FooterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold desc="< ActionPerformed(s) >">
    private void quitButtonActionPerformed(java.awt.event.ActionEvent evt) {
        controller.endGame();
    }

    private void InvButtonActionPerformed(java.awt.event.ActionEvent evt) {
        controller.showInventory();
    }

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        controller.saveGame();
    }

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (!this.inputField.getText().isEmpty()) {
            controller.executeNewCommand(this.inputField.getText());
        }
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        controller.endGame();
    }
    // </editor-fold>


    @Override
    public void out(String message) {
        this.DialogTextArea.append("\n" + message);
    }

    @Override
    public int notifySomething(String header, String message) {
        return showYesNoDialoguePage(header, message);
    }

    @Override
    public void notifyWarning(String header, String message) {
        this.showWarning(header, message);
    }

    @Override
    public void notifyError(String header, String message) {
        this.showError(header, message);
    }

    @Override
    public void setImage(String path) throws  ImageNotFoundException{
        System.out.println(path);
        URL imgUrl = getClass().getResource(path);
        if (imgUrl != null) {
            this.ImageLabel.setIcon(new ImageIcon(
                    new ImageIcon(imgUrl).getImage().getScaledInstance(this.ImageLabel.getWidth(),this.ImageLabel.getHeight(), Image.SCALE_SMOOTH)));
        } else {
            throw new ImageNotFoundException("Resource not found: " + path);
        }
    }

    @Override
    public void open() {
        this.setVisible(true);
    }

    @Override
    public void close() {
        this.dispose();
    }

    @Override
    public void linkController(Controller controller) {
        if(controller instanceof GameController) {
            this.controller = (GameController) controller;
        } else {
            new RuntimeException("Il controller per GamePage non è un GameController");
        }
    }

    @Override
    public void setDisplayedClock(String time) {

    }

    @Override
    public void increaseProgressBar() {
        this.progressBar.setValue(this.progressBar.getValue() + 1);
    }


}
