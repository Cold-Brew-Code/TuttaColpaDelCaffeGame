package it.tutta.colpa.del.caffe.start.boundary;

import it.tutta.colpa.del.caffe.game.boundary.GUI;
import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.game.utility.AudioManager;
import it.tutta.colpa.del.caffe.start.control.MainPageController;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class MainPage extends JFrame implements GUI {
    MainPageController c;
    private boolean isAudioPaused = false;
    private boolean isAudioEnabled = true;

    JPanel wallpaper = new JPanel() {
        private final Image wp;

        {
            URL imgUrl = getClass().getResource("/images/copertina.png");
            if (imgUrl != null) {
                wp = new ImageIcon(imgUrl).getImage();
            } else {
                wp = null;
                System.err.println("Immagine non trovata: images/copertina.png");
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

    JButton start = new PButton("INIZIA");
    JButton load = new PButton("CARICA PARTITA");
    JButton exit = new PButton("ESCI");
    JButton audioControlButton = new PButton("AUDIO");

    JPopupMenu audioMenu = new JPopupMenu();
    JMenuItem togglePauseItem = new JMenuItem("Pausa/Riprendi");
    JMenuItem volumeUpItem = new JMenuItem("Aumenta Volume");
    JMenuItem volumeDownItem = new JMenuItem("Diminuisci Volume");
    JMenuItem toggleAudioItem = new JMenuItem("Disattiva Audio");

    public MainPage() {
        System.out.println("=== VERIFICA RISORSE ===");
        checkResource("/images/button.png", "Immagine pulsante");
        checkResource("/sounds/menu_theme.wav", "Audio menu");
        checkResource("/sounds/game_theme.wav", "Audio gioco");
        checkResource("/sounds/victory.wav", "Audio vittoria");
        checkResource("/sounds/defeat.wav", "Audio sconfitta");

        AudioManager audioManager = AudioManager.getInstance();
        audioManager.loadAudio("menu_theme", "menu_theme.wav");
        audioManager.setVolume(0.7f);

        this.setResizable(false);
        this.setPreferredSize(new Dimension(960, 540));
        this.setTitle("Tutta colpa del Caffè!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        URL gameIcon = getClass().getResource("/images/icon.png");
        if (gameIcon != null) {
            this.setIconImage(new ImageIcon(gameIcon).getImage());
        }

        audioMenu.add(togglePauseItem);
        audioMenu.add(volumeUpItem);
        audioMenu.add(volumeDownItem);
        audioMenu.addSeparator();
        audioMenu.add(toggleAudioItem);

        togglePauseItem.addActionListener(e -> toggleAudioPause());
        volumeUpItem.addActionListener(e -> adjustVolume(0.1f));
        volumeDownItem.addActionListener(e -> adjustVolume(-0.1f));
        toggleAudioItem.addActionListener(e -> toggleAudio());
        audioControlButton
                .addActionListener(e -> audioMenu.show(audioControlButton, 0, audioControlButton.getHeight()));

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        buttonPanel.setOpaque(false);

        buttonPanel.add(start);
        buttonPanel.add(load);
        buttonPanel.add(exit);
        buttonPanel.add(audioControlButton);

        buttonPanel.setBounds(((960 - 260) / 2) + 325,
                540 - 300 - 50,
                260, 300);

        wallpaper.setLayout(null);
        wallpaper.add(buttonPanel);
        this.add(wallpaper);

        this.pack();
        setLocationRelativeTo(null);
        this.setVisible(true);

        if (isAudioEnabled) {
            SwingUtilities.invokeLater(() -> {
                audioManager.fadeIn("menu_theme", true, 800);
            });
        }

        // ====================================================
        // = BUTTON's LISTENERS =
        // ====================================================
        start.addActionListener(e -> {
            if (isAudioEnabled) {
                AudioManager.getInstance().stop("menu_theme");
            }
            new Thread(() -> {
                try {
                    Thread.sleep(300);
                    c.startGame();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        });

        load.addActionListener(e -> {
            if (isAudioEnabled) {
                AudioManager.getInstance().stop("menu_theme");
            }
            c.loadGame();
        });

        load.addActionListener(e -> {
            if (isAudioEnabled) {
                audioManager.fadeOut("menu_theme", 300);
            }
            new it.tutta.colpa.del.caffe.loadsave.LoadGameHandler(c);
        });

        exit.addActionListener(e -> {
            if (isAudioEnabled) {
                audioManager.fadeOut("menu_theme", 300);
            }
            c.quit();
        });
    }

    private class PButton extends JButton {
        private final Image backgroundImage = new ImageIcon(getClass().getResource("/images/button.png"))
                .getImage();

        public PButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setForeground(new Color(0xFFFFFF));
            setPreferredSize(new Dimension(250, 75));
            setFont(new Font("Arial", Font.BOLD, 24));
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            super.paintComponent(g);
        }
    }

    private void toggleAudio() {
        AudioManager audioManager = AudioManager.getInstance();
        isAudioEnabled = !isAudioEnabled;

        toggleAudioItem.setText(isAudioEnabled ? "Disattiva Audio" : "Attiva Audio");
        togglePauseItem.setEnabled(isAudioEnabled);
        volumeUpItem.setEnabled(isAudioEnabled);
        volumeDownItem.setEnabled(isAudioEnabled);

        if (isAudioEnabled) {
            audioManager.fadeIn("menu_theme", true, 500);
        } else {
            audioManager.stop("menu_theme");
        }
    }

    private void toggleAudioPause() {
        AudioManager audioManager = AudioManager.getInstance();
        if (isAudioPaused) {
            audioManager.resume("menu_theme");
            togglePauseItem.setText("Pausa");
        } else {
            audioManager.pause("menu_theme");
            togglePauseItem.setText("Riprendi");
        }
        isAudioPaused = !isAudioPaused;
    }

    private void adjustVolume(float delta) {
        if (isAudioEnabled) {
            AudioManager audioManager = AudioManager.getInstance();
            float newVolume = Math.max(0, Math.min(1, audioManager.getVolume() + delta));
            audioManager.setVolume(newVolume);
        }
    }

    @Override
    public void close() {
        if (isAudioEnabled) {
            AudioManager.getInstance().fadeOut("menu_theme", 300);
        }
        this.setVisible(false);
    }

    @Override
    public void linkController(Controller c) {
        if (c instanceof MainPageController) {
            this.c = (MainPageController) c;
        } else {
            throw new RuntimeException("Il controller c non è un controller valido per MainPage");
        }
    }

    @Override
    public void open() {
        this.setVisible(true);
        if (isAudioEnabled) {
            SwingUtilities.invokeLater(() -> {
                AudioManager.getInstance().fadeIn("menu_theme", true, 800);
            });
        }
    }

    private void checkResource(String path, String descrizione) {
        URL url = getClass().getResource(path);
        if (url != null) {
            System.out.println("[OK] " + descrizione + " trovato: " + path);
        } else {
            System.err.println("[ERR] " + descrizione + " NON trovato: " + path);
            System.err.println("Percorso assoluto tentato: " + new File("src/main/resources" + path).getAbsolutePath());
        }
    }
}