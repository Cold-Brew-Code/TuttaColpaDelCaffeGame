/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.boundary;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.game.control.GameController;
import it.tutta.colpa.del.caffe.game.exception.ImageNotFoundException;
import it.tutta.colpa.del.caffe.game.utility.AudioManager;
import it.tutta.colpa.del.caffe.game.utility.TypeWriterEffect;

/**
 * @author giovav
 * @since 10/07/2025
 */
public class GamePage extends javax.swing.JFrame implements GameGUI {

    private GameController controller;
    private boolean barUtilHasUsedRestroom = false;

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
        } // </editor-fold>>
        initComponents();

        AudioManager.getInstance().loadAudio("game_theme", "game_theme.wav");
        AudioManager.getInstance().fadeIn("game_theme", true, 1000);

        typeWriterEffect = new TypeWriterEffect(DialogTextArea, 50);

        this.setVisible(true);
    }

    private int showYesNoDialoguePage(String title, String message) {
        return javax.swing.JOptionPane.showConfirmDialog(
                null,
                message,
                title,
                javax.swing.JOptionPane.YES_NO_OPTION);
    }

    public void showError(String title, String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.ERROR_MESSAGE);
    }

    public void showInformation(String title, String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void showWarning(String title, String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                JOptionPane.WARNING_MESSAGE);
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
    private javax.swing.JButton skipButton;
    private javax.swing.JButton audioButton;
    private javax.swing.JPopupMenu audioPopupMenu;
    private javax.swing.JMenuItem abbassa_alza;
    //private javax.swing.JMenuItem decreaseVolumeMenuItem;
    private javax.swing.JMenuItem toggleMuteMenuItem;
    // --- NUOVI COMPONENTI EFFETTI VISIVI ---
    private javax.swing.JButton visualEffectButton;
    private javax.swing.JPopupMenu visualEffectPopupMenu;
    private javax.swing.JMenuItem slowEffectMenuItem;
    private javax.swing.JMenuItem mediumEffectMenuItem;
    private javax.swing.JMenuItem fastEffectMenuItem;
    private javax.swing.JMenuItem disabledEffectMenuItem;
    private TypeWriterEffect typeWriterEffect;
    private javax.swing.JLabel timerLabel;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GamePage.class.getName());
    // </editor-fold>

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="< GUI init >">
    // GEN-BEGIN:initComponents
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
        skipButton = new javax.swing.JButton();

        audioButton = new javax.swing.JButton();
        audioPopupMenu = new javax.swing.JPopupMenu();
        abbassa_alza = new javax.swing.JMenuItem("Aumenta/Abbassa Volume");
        //decreaseVolumeMenuItem = new javax.swing.JMenuItem("Abbassa Volume");
        toggleMuteMenuItem = new javax.swing.JMenuItem("Disattiva/Attiva Audio");

        // --- INIZIALIZZAZIONE NUOVI COMPONENTI ---
        visualEffectButton = new javax.swing.JButton();
        visualEffectPopupMenu = new javax.swing.JPopupMenu();
        slowEffectMenuItem = new javax.swing.JMenuItem("Lento");
        mediumEffectMenuItem = new javax.swing.JMenuItem("Medio");
        fastEffectMenuItem = new javax.swing.JMenuItem("Veloce");
        disabledEffectMenuItem = new javax.swing.JMenuItem("Disattivo");
        timerLabel = new javax.swing.JLabel();

        HeaderPanel.setOpaque(false);
        ImagePanel.setOpaque(false);
        FooterPanel.setOpaque(false);
        DialogPanel.setOpaque(false);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent evt) {
                inputField.requestFocusInWindow();
            }

            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        setTitle("Tutta Colpa del Caffè!");
        setResizable(false);

        // --- STILE LABEL TIMER ---
        timerLabel.setFont(new java.awt.Font("Arial", 1, 24));
        timerLabel.setForeground(new java.awt.Color(255, 255, 255));
        timerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timerLabel.setText("00:00");
        timerLabel.setOpaque(false);

        // --- STILE BOTTONI MENU ---
        audioButton.setText("Audio \u25BC");
        audioButton.setBackground(Color.WHITE);
        visualEffectButton.setText("Effetto visivo \u25BC");
        visualEffectButton.setBackground(Color.WHITE);

        // --- STILE DEGLI ELEMENTI DI TUTTI I MENU ---
        Dimension menuItemSize = new Dimension(170, 35);
        Color itemBorderColor = new Color(220, 220, 220);
        JMenuItem[] allMenuItems = {
                abbassa_alza, toggleMuteMenuItem,
                slowEffectMenuItem, mediumEffectMenuItem, fastEffectMenuItem, disabledEffectMenuItem
        };

        for (JMenuItem item : allMenuItems) {
            item.setBackground(Color.WHITE);
            item.setOpaque(true);
            item.setPreferredSize(menuItemSize);
            item.setBorder(new LineBorder(itemBorderColor));
            item.setFont(new Font("Arial", Font.PLAIN, 14));
        }

        // --- SETUP MENU AUDIO ---
        audioPopupMenu.setBorder(new LineBorder(Color.GRAY));
        audioPopupMenu.add(abbassa_alza);
        //audioPopupMenu.add(decreaseVolumeMenuItem);
        audioPopupMenu.add(toggleMuteMenuItem);
        audioButton.addActionListener(this::audioButtonActionPerformed);
        abbassa_alza.addActionListener(this::increaseDecreaseVolumeMenuItemActionPerformed);
        toggleMuteMenuItem.addActionListener(this::toggleMuteMenuItemActionPerformed);

        // --- SETUP MENU EFFETTI VISIVI ---
        visualEffectPopupMenu.setBorder(new LineBorder(Color.GRAY));
        visualEffectPopupMenu.add(slowEffectMenuItem);
        visualEffectPopupMenu.add(mediumEffectMenuItem);
        visualEffectPopupMenu.add(fastEffectMenuItem);
        visualEffectPopupMenu.add(disabledEffectMenuItem);
        visualEffectButton.addActionListener(this::visualEffectButtonActionPerformed);
        slowEffectMenuItem.addActionListener(this::slowEffectMenuItemActionPerformed);
        mediumEffectMenuItem.addActionListener(this::mediumEffectMenuItemActionPerformed);
        fastEffectMenuItem.addActionListener(this::fastEffectMenuItemActionPerformed);
        disabledEffectMenuItem.addActionListener(this::disabledEffectMenuItemActionPerformed);
        inputField.addActionListener(this::sendButtonActionPerformed);

        // --- LAYOUT HEADERPANEL ---
        javax.swing.GroupLayout HeaderPanelLayout = new javax.swing.GroupLayout(HeaderPanel);
        HeaderPanel.setLayout(HeaderPanelLayout);
        HeaderPanelLayout.setHorizontalGroup(
                HeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(HeaderPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(audioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(visualEffectButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(timerLabel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(saveButton)
                                .addContainerGap())
        );
        HeaderPanelLayout.setVerticalGroup(
                HeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(HeaderPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(HeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(audioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(visualEffectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(timerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ImagePanelLayout = new javax.swing.GroupLayout(ImagePanel);
        ImagePanel.setLayout(ImagePanelLayout);
        ImagePanelLayout.setHorizontalGroup(
                ImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(ImageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE));
        ImagePanelLayout.setVerticalGroup(
                ImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(ImageLabel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        DialogPanel.setBackground(new java.awt.Color(204, 204, 255, 0));

        saveButton.setBackground(Color.WHITE);
        quitButton.setBackground(Color.WHITE);
        sendButton.setBackground(Color.WHITE);
        InvButton.setBackground(Color.WHITE);
        skipButton.setBackground(Color.WHITE);

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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE));
        DialogPanelLayout.setVerticalGroup(
                DialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE));

        inputField.setToolTipText("");

        sendButton.setText("Invia");
        sendButton.addActionListener(this::sendButtonActionPerformed);

        quitButton.setText("Abbandona");
        quitButton.addActionListener(this::quitButtonActionPerformed);

        saveButton.setText("Salva partita");
        saveButton.addActionListener(this::saveButtonActionPerformed);

        InvButton.setText("Zaino");
        InvButton.addActionListener(this::InvButtonActionPerformed);

        skipButton.setText("Skip");
        skipButton.addActionListener(this::skipButtonActionPerformed);

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
        sendButton.setIcon(
                new ImageIcon((new ImageIcon(getClass().getResource("/images/send_icon.png")))
                        .getImage()
                        .getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        URL skipIconUrl = getClass().getResource("/images/skip_icon.png");
        if (skipIconUrl != null) {
            skipButton.setIcon(
                    new ImageIcon(new ImageIcon(skipIconUrl)
                            .getImage()
                            .getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        }

        javax.swing.GroupLayout FooterPanelLayout = new javax.swing.GroupLayout(FooterPanel);
        FooterPanel.setLayout(FooterPanelLayout);
        FooterPanelLayout.setHorizontalGroup(
                FooterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(FooterPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(inputField, javax.swing.GroupLayout.PREFERRED_SIZE, 454,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sendButton)
                                .addGap(18, 18, 18)
                                .addComponent(skipButton)
                                .addGap(18, 18, 18)
                                .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(InvButton)
                                .addGap(18, 18, 18)
                                .addComponent(quitButton)
                                .addContainerGap()));
        FooterPanelLayout.setVerticalGroup(
                FooterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FooterPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(FooterPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(skipButton, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(InvButton, javax.swing.GroupLayout.DEFAULT_SIZE, 39,
                                                Short.MAX_VALUE)
                                        .addComponent(quitButton, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(inputField, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(sendButton, javax.swing.GroupLayout.Alignment.LEADING,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(progressBar, javax.swing.GroupLayout.Alignment.LEADING,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap()));

        javax.swing.GroupLayout mainContainerLayout = new javax.swing.GroupLayout(mainContainer);
        mainContainer.setLayout(mainContainerLayout);
        mainContainerLayout.setHorizontalGroup(
                mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(HeaderPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainContainerLayout
                                .createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(DialogPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ImagePanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addComponent(FooterPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        mainContainerLayout.setVerticalGroup(
                mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainContainerLayout.createSequentialGroup()
                                .addComponent(HeaderPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(mainContainerLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(ImagePanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(DialogPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(FooterPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainContainer, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainContainer, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
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

    private void skipButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (typeWriterEffect != null && typeWriterEffect.isRunning()) {
            typeWriterEffect.skip(); // Mostra tutto il testo immediatamente
        }
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        controller.endGame();
    }
    // </editor-fold>

    // <editor-fold desc="< Audio ActionPerformed(s) >">
    private void audioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        audioPopupMenu.show(audioButton, 0, audioButton.getHeight());
    }

    private void increaseDecreaseVolumeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Aumenta volume cliccato");
        int volumePercent = askVolumeSlider("Imposta Volume", "Scegli il livello del volume:");
        if (volumePercent >= 0) {
            float volume = volumePercent / 100f;
            AudioManager.getInstance().setVolume(volume);
        }
    }

    private int askVolumeSlider(String title, String message) {
        JSlider slider = new JSlider(0, 100, (int) (AudioManager.getInstance().getVolume() * 100));
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        int option = JOptionPane.showConfirmDialog(
                this,
                new Object[]{message, slider},
                title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.CLOSED_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            return slider.getValue();
        }
        return -1; //ha premuto annulla
    }

    private void toggleMuteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        AudioManager audioManager = AudioManager.getInstance();
        if (audioManager.isPaused("game_theme")) {
            audioManager.resume("game_theme");
            toggleMuteMenuItem.setText("Disattiva Audio");
        } else {
            audioManager.pause("game_theme");
            toggleMuteMenuItem.setText("Attiva Audio");
        }
    }
    // </editor-fold>

    // <editor-fold desc="< Visual Effect ActionPerformed(s) >">
    private void visualEffectButtonActionPerformed(java.awt.event.ActionEvent evt) {
        visualEffectPopupMenu.show(visualEffectButton, 0, visualEffectButton.getHeight());
    }

    private void slowEffectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (typeWriterEffect != null) {
            typeWriterEffect.setDelay(100);
            System.out.println("Velocità effetto impostata: Lento");
        }
    }

    private void mediumEffectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (typeWriterEffect != null) {
            typeWriterEffect.setDelay(50);
            System.out.println("Velocità effetto impostata: Medio");
        }
    }

    private void fastEffectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (typeWriterEffect != null) {
            typeWriterEffect.setDelay(20);
            System.out.println("Velocità effetto impostata: Veloce");
        }
    }

    private void disabledEffectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (typeWriterEffect != null) {
            typeWriterEffect.setDelay(0);
            System.out.println("Effetto visivo disattivato");
        }
    }
    // </editor-fold>

    @Override
    public void out(String message) {
        if (typeWriterEffect != null && typeWriterEffect.isRunning()) {
            typeWriterEffect.skip(); // Completa l'animazione corrente
        }
        typeWriterEffect.start("\n" + message); // Avvia la nuova animazione
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
    public void setImage(String path) throws ImageNotFoundException {
        URL imgUrl = getClass().getResource(path);
        if (imgUrl != null) {
            this.ImageLabel.setIcon(new ImageIcon(
                    new ImageIcon(imgUrl).getImage().getScaledInstance(this.ImageLabel.getWidth(),
                            this.ImageLabel.getHeight(), Image.SCALE_SMOOTH)));
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
        AudioManager.getInstance().fadeOut("game_theme", 500);
        this.dispose();
    }

    @Override
    public void linkController(Controller controller) {
        if (controller instanceof GameController) {
            this.controller = (GameController) controller;
        } else {
            throw new RuntimeException("Il controller per GamePage non è un GameController");
        }
    }

    @Override
    public void setDisplayedClock(String time) {
        if (this.timerLabel != null) {
            this.timerLabel.setText(time);

        }
    }

    @Override
    public void initProgressBar(int sec, boolean hasUsedRestroom) {
        this.progressBar.setMaximum(sec);
        this.progressBar.setValue(0);
        this.barUtilHasUsedRestroom = hasUsedRestroom;
    }

    /**
     * <p>
     * Il metodo esegue l'aggiornamento della {@code progressBar} all'interno
     * Thread (EDT) che si occupa di gestire tutti gli eventi GUI, utilizzando
     * {@link SwingUtilities#invokeLater(Runnable)}, garantendo che le modifiche
     * alla GUI siano sicure e corrette.invokeLater assicura che l'aggiornamente
     * della barra venga fatto dal thread corretto indipendentemente dal thread
     * dell'oggetto timer. Assicurandoci un corretto aggiornamento e garantendo
     * la corretta visualizzazione. Incrementa il valore corrente della barra di
     * progresso di 1 unità in modo thread-safe.
     * </p>
     * <p>
     * <p>
     * In base al valore aggiornato della barra, cambia il colore e il testo
     * visualizzato:</p>
     * <ul>
     * <li>Valore < 6000: testo "FORZA IL DOVERE CHIAMA" con colore di
     * default</li> <li>Valore tra 6001 e 9000: testo "ahi ho paura di mol lare"
     * con colore arancione</li>
     * <li>Valore > 9000: testo "Sto quasi per mollare" con colore rosso</li>
     * </ul>
     */
    @Override
    public void increaseProgressBar() {
        if (!barUtilHasUsedRestroom) {
            // Incrementa di 1 secondo
            int newValue = progressBar.getValue() + 1;
            progressBar.setValue(newValue);

            SwingUtilities.invokeLater(() -> {
                progressBar.setStringPainted(true);
                progressBar.setFont(new Font("Verdana", Font.BOLD, 16));

                // Cambia solo il colore del riempimento in base al tempo
                if (newValue < 6000) {
                    progressBar.setForeground(Color.GREEN);  // riempimento
                    progressBar.setBackground(Color.LIGHT_GRAY); // sfondo neutro
                    progressBar.setString("FORZA IL DOVERE CHIAMA");

                } else if (newValue < 9000) {
                    progressBar.setForeground(Color.ORANGE);
                    progressBar.setBackground(Color.LIGHT_GRAY);
                    progressBar.setString("AHI HO PAURA DI MOLLARE");

                } else {
                    progressBar.setForeground(Color.RED);
                    progressBar.setBackground(Color.LIGHT_GRAY);
                    progressBar.setString("STO QUASI PER MOLLARE");
                }

                // Scritta sempre nera
                progressBar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
                    @Override
                    protected void paintString(Graphics g, int x, int y,
                                               int width, int height,
                                               int amountFull, Insets b) {
                        g.setColor(Color.BLACK);
                        super.paintString(g, x, y, width, height, amountFull, b);
                    }
                });
            });
        } else {
            // Incrementa di 1 secondo
            int newValue = progressBar.getValue() + 1;
            progressBar.setValue(newValue);

            SwingUtilities.invokeLater(() -> {
                progressBar.setStringPainted(true);
                progressBar.setFont(new Font("Verdana", Font.BOLD, 16));

                // Cambia solo il colore del riempimento in base al tempo
                if (newValue < 300) {
                    progressBar.setForeground(Color.GREEN);  // riempimento
                    progressBar.setBackground(Color.LIGHT_GRAY); // sfondo neutro
                    progressBar.setString("VAI A FARE L'ESAME, SBRIGATI!");

                } else if (newValue < 450) {
                    progressBar.setForeground(Color.ORANGE);
                    progressBar.setBackground(Color.LIGHT_GRAY);
                    progressBar.setString("IL LIBRETTO NON SI RIEMPIE DA SOLO!");

                } else {
                    progressBar.setForeground(Color.RED);
                    progressBar.setBackground(Color.LIGHT_GRAY);
                    progressBar.setString("ORMAI... SARÀ PER IL PROSSIMO APPELLO...");
                }

                // Scritta sempre nera
                progressBar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
                    @Override
                    protected void paintString(Graphics g, int x, int y,
                                               int width, int height,
                                               int amountFull, Insets b) {
                        g.setColor(Color.BLACK);
                        super.paintString(g, x, y, width, height, amountFull, b);
                    }
                });
            });
        }
    }

    @Override
    public void executedCommand() {
        typeWriterEffect.skip();
        this.DialogTextArea.append("\n > " + inputField.getText());
        this.inputField.setText("");
    }
}
