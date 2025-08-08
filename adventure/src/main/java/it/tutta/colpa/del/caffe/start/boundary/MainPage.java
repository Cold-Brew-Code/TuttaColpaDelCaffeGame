package it.tutta.colpa.del.caffe.start.boundary;

import it.tutta.colpa.del.caffe.game.boundary.GUI;
import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.game.utility.AudioManager;
import it.tutta.colpa.del.caffe.start.control.MainPageController;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MainPage extends JFrame implements GUI {
    MainPageController c;
    private boolean isAudioPaused = false;
    private boolean isAudioEnabled = true; // Aggiunto stato globale audio

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

    JButton start = createStyledButton("INIZIA");
    JButton load = createStyledButton("CARICA PARTITA");
    JButton exit = createStyledButton("ESCI");
    JButton audioControlButton = createStyledButton("CONTROLLO AUDIO");

    JPopupMenu audioMenu = new JPopupMenu();
    JMenuItem togglePauseItem = new JMenuItem("Pausa/Riprendi");
    JMenuItem volumeUpItem = new JMenuItem("Aumenta Volume");
    JMenuItem volumeDownItem = new JMenuItem("Diminuisci Volume");
    JMenuItem toggleAudioItem = new JMenuItem("Disattiva Audio");

    public MainPage() {
        // Inizializzazione audio
        AudioManager audioManager = AudioManager.getInstance();
        audioManager.loadAudio("menu_theme", "menu_theme.wav");
        audioManager.setVolume(0.7f);

        // Configurazione finestra
        this.setResizable(false);
        this.setPreferredSize(new Dimension(960, 540));
        this.setTitle("Tutta colpa del Caffè!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Icona applicazione
        URL gameIcon = getClass().getResource("/images/icon.png");
        if (gameIcon != null) {
            this.setIconImage(new ImageIcon(gameIcon).getImage());
        }

        // Menu audio
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

        // Layout pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addButtonWithSpacing(buttonPanel, start);
        addButtonWithSpacing(buttonPanel, load);
        addButtonWithSpacing(buttonPanel, exit);
        addButtonWithSpacing(buttonPanel, audioControlButton);

        // Posizionamento
        int panelWidth = 260;
        int panelHeight = 300;
        int xPos = (960 - panelWidth) / 2 + 325;
        int yPos = 540 - panelHeight - 20;
        buttonPanel.setBounds(xPos, yPos, panelWidth, panelHeight);

        // Controllo volume
        JSlider volumeSlider = new JSlider(0, 100, 70);
        volumeSlider.setPreferredSize(new Dimension(100, 20));
        volumeSlider.addChangeListener(e -> {
            if (isAudioEnabled) { // Solo se l'audio è abilitato
                float volume = volumeSlider.getValue() / 100f;
                AudioManager.getInstance().setVolume(volume);
            }
        });

        JPanel volumePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        volumePanel.setOpaque(false);
        volumePanel.add(new JLabel("Volume:"));
        volumePanel.add(volumeSlider);
        volumePanel.setBounds(20, 20, 200, 30);

        // Aggiunta componenti
        wallpaper.setLayout(null);
        wallpaper.add(buttonPanel);
        wallpaper.add(volumePanel);
        this.add(wallpaper);

        this.pack();
        setLocationRelativeTo(null);
        this.setVisible(true);

        // Avvio musica
        if (isAudioEnabled) {
            SwingUtilities.invokeLater(() -> {
                audioManager.fadeIn("menu_theme", true, 800);
            });
        }

        // Listener pulsanti
        start.addActionListener(e -> {
            if (isAudioEnabled) {
                audioManager.fadeOut("menu_theme", 500);
            }
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    c.startGame();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        });

        load.addActionListener(e -> {
            if (isAudioEnabled) {
                audioManager.fadeOut("menu_theme", 300);
            }
            c.loadGame();
        });

        exit.addActionListener(e -> {
            if (isAudioEnabled) {
                audioManager.fadeOut("menu_theme", 300);
            }
            c.quit();
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(250, 75));
        button.setMaximumSize(new Dimension(250, 75));
        button.setFont(new Font("Arial", Font.BOLD, 24));

        try {
            URL buttonImageUrl = getClass().getResource("/images/button.png");
            if (buttonImageUrl != null) {
                ImageIcon icon = new ImageIcon(buttonImageUrl);
                button.setIcon(new ImageIcon(icon.getImage().getScaledInstance(250, 75, Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) {
            System.err.println("Immagine del pulsante non trovata");
        }

        return button;
    }

    private void addButtonWithSpacing(JPanel panel, JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
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
}