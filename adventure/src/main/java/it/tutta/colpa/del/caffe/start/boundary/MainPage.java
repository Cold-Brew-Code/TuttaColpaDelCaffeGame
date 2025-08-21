package it.tutta.colpa.del.caffe.start.boundary;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

import it.tutta.colpa.del.caffe.game.boundary.GUI;
import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.game.utility.AudioManager;
import it.tutta.colpa.del.caffe.start.control.MainPageController;

/**
 * La classe {@code MainPage} rappresenta la finestra principale (menu iniziale)
 * del gioco.
 * <p>
 * Estende {@link JFrame} e implementa l'interfaccia {@link GUI}, fornendo i
 * pulsanti e le funzionalità per avviare una nuova partita, caricare un
 * salvataggio, uscire dal gioco e gestire le opzioni audio (attiva/disattiva,
 * pausa/riprendi, volume).
 * </p>
 * <p>
 * La schermata principale include uno sfondo personalizzato, pulsanti grafici e
 * un menu popup per le impostazioni audio. La gestione dell'audio avviene
 * tramite la classe {@link AudioManager}.
 * </p>
 */
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
    //JMenuItem volumeUpItem = new JMenuItem("Aumenta Volume");
    JMenuItem volumeDownItem = new JMenuItem("Aumenta/Abbassa Volume");
    JMenuItem toggleAudioItem = new JMenuItem("Disattiva Audio");

    /**
     * Costruttore della schermata principale.
     * <p>
     * Si occupa di:
     * <ul>
     * <li>verificare la disponibilità delle risorse (immagini, suoni)</li>
     * <li>inizializzare il gestore audio e impostarne il volume</li>
     * <li>costruire l'interfaccia grafica con pulsanti e sfondo</li>
     * <li>collegare i listener ai pulsanti e al menu audio</li>
     * <li>avviare la musica di sottofondo del menu</li>
     * </ul>
     */
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
        //audioMenu.add(volumeUpItem);
        audioMenu.add(volumeDownItem);
        audioMenu.addSeparator();
        audioMenu.add(toggleAudioItem);

        togglePauseItem.addActionListener(e -> toggleAudioPause());
        //volumeUpItem.addActionListener(e -> adjustVolume(0.1f));
        volumeDownItem.addActionListener(this::increaseDecreaseVolumeMenuItemActionPerformed);
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
            c.loadGame();
        });

        exit.addActionListener(e -> {
            if (isAudioEnabled) {
                audioManager.fadeOut("menu_theme", 300);
            }
            c.quit();
        });
    }

    /**
     * Classe interna per la gestione dei pulsanti personalizzati con immagine
     * di sfondo e testo sovrapposto.
     */
    private class PButton extends JButton {

        private final Image backgroundImage = new ImageIcon(getClass().getResource("/images/button.png"))
                .getImage();

        /**
         * Costruisce un pulsante personalizzato con il testo indicato.
         *
         * @param text il testo da mostrare sul pulsante
         */
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

    /**
     * Attiva o disattiva completamente l'audio del gioco.
     */
    private void toggleAudio() {
        AudioManager audioManager = AudioManager.getInstance();
        isAudioEnabled = !isAudioEnabled;

        toggleAudioItem.setText(isAudioEnabled ? "Disattiva Audio" : "Attiva Audio");
        togglePauseItem.setEnabled(isAudioEnabled);
        // volumeUpItem.setEnabled(isAudioEnabled);
        volumeDownItem.setEnabled(isAudioEnabled);

        if (isAudioEnabled) {
            audioManager.fadeIn("menu_theme", true, 500);
        } else {
            audioManager.stop("menu_theme");
        }
    }

    /**
     * Mette in pausa o riprende la musica di sottofondo.
     */
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

    /**
     * Listener per la gestione del menu di aumento/riduzione del volume.
     *
     * @param evt l'evento di click sul menu
     */
    private void increaseDecreaseVolumeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Aumenta volume cliccato");
        int volumePercent = askVolumeSlider("Imposta Volume", "Scegli il livello del volume:");
        if (volumePercent >= 0) {
            float volume = volumePercent / 100f; // converti in float 0.0 - 1.0
            AudioManager.getInstance().setVolume(volume);
        }
    }

    /**
     * Mostra una finestra di dialogo con uno slider per impostare il volume.
     *
     * @param title il titolo della finestra
     * @param message il messaggio da mostrare
     * @return il livello di volume scelto (0-100) oppure -1 se annullato
     */
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

    /**
     * Chiude la schermata principale e interrompe la musica di sottofondo.
     */
    @Override
    public void close() {
        if (isAudioEnabled) {
            AudioManager.getInstance().fadeOut("menu_theme", 300);
        }
        this.setVisible(false);
    }

    /**
     * Collega un controller alla pagina principale.
     *
     * @param c il controller da associare
     * @throws RuntimeException se il controller non è un
     * {@link MainPageController}
     */
    @Override
    public void linkController(Controller c) {
        if (c instanceof MainPageController) {
            this.c = (MainPageController) c;
        } else {
            throw new RuntimeException("Il controller c non è un controller valido per MainPage");
        }
    }

    /**
     * Apre la schermata principale e avvia la musica di sottofondo.
     */
    @Override
    public void open() {
        this.setVisible(true);
        if (isAudioEnabled) {
            SwingUtilities.invokeLater(() -> {
                AudioManager.getInstance().fadeIn("menu_theme", true, 800);
            });
        }
    }

    /**
     * Verifica che una risorsa sia presente nel percorso indicato e stampa
     * l'esito nel log.
     *
     * @param path percorso relativo della risorsa
     * @param descrizione descrizione testuale della risorsa
     */
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
