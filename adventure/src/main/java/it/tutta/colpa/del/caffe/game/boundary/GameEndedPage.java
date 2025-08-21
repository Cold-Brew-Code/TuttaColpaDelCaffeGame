package it.tutta.colpa.del.caffe.game.boundary;

import com.sun.tools.javac.Main;
import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.game.utility.GameStatus;
import it.tutta.colpa.del.caffe.game.utility.AudioManager;
import it.tutta.colpa.del.caffe.start.control.MainPageController;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 *
 * @author giovanni
 */
public class GameEndedPage extends javax.swing.JFrame implements GUI {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(GameEndedPage.class.getName());
    private final GameStatus gameStatus;
    private javax.swing.JPanel wallpaper;
    private MainPageController mpc;

    /**
     * Creates new form GameEndedPage
     */
    public GameEndedPage(GameStatus s, MainPageController mpc) {
        this.gameStatus = s;
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        initComponents(s);
        this.mpc = mpc;

        // inizializzazione audio basata sullo stato memorizzato
        AudioManager audioManager = AudioManager.getInstance();
        if (this.gameStatus == GameStatus.PROMOSSO) {
            audioManager.loadAudio("victory", "victory.wav");
            audioManager.play("victory", false);
        } else {
            audioManager.loadAudio("defeat", "defeat.wav");
            audioManager.play("defeat", false);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents(GameStatus s) {
        wallpaper = new javax.swing.JPanel() {
            private final Image wp;
            {
                URL imgUrl = getClass().getResource(getImagePath(s));
                if (imgUrl != null) {
                    wp = new ImageIcon(imgUrl).getImage();
                } else {
                    wp = null;
                    System.err.println("Immagine non trovata");
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
        this.setTitle("Tutta colpa del Caffè!");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        javax.swing.GroupLayout wallpaperLayout = new javax.swing.GroupLayout(wallpaper);
        wallpaper.setLayout(wallpaperLayout);
        wallpaperLayout.setHorizontalGroup(
                wallpaperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1280, Short.MAX_VALUE));
        wallpaperLayout.setVerticalGroup(
                wallpaperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 720, Short.MAX_VALUE));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(wallpaper, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(wallpaper, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        this.setResizable(false);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        mpc.openGUI();
    }

    private String getImagePath(GameStatus s) {
        String victory_type;
        victory_type = switch (s) {
            case PROMOSSO -> "partita_vinta";
            case BAGNO_USATO_TEMPO_ESAURITO, ESAME_DA_FARE, BOCCIATO-> "bocciato";
            case TEMPO_ESAURITO -> "partita_persa";
            default -> "partita_persa"; //abbandonata
        };
        return "/images/" + victory_type + ".png";
    }

    private String currentlyPlayingTrack() {
        return (this.gameStatus == GameStatus.PROMOSSO) ? "victory" : "defeat";
    }

    @Override
    public void open() {
        this.setVisible(true);
    }

    @Override
    public void close() {
        AudioManager.getInstance().stop(currentlyPlayingTrack());
        this.dispose();
    }

    @Override
    public void linkController(Controller c) {
        this.mpc = (MainPageController) c;
    }
}