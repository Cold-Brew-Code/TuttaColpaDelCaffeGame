package it.tutta.colpa.del.caffe.game.boundary;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

/**
 * La classe MapPage crea un'interfaccia grafica con un'immagine zoomabile.
 * La finestra non è ridimensionabile e permette lo zoom e il panning
 * dell'immagine tramite bottoni e scrollbar.
 */
public class MapPage extends JDialog {

    /**
     * Pannello interno per la visualizzazione dell'immagine.
     */
    private final ImagePanel imagePanel;

    /**
     * Pannello scorrevole che gestisce il panning dell'immagine.
     */
    private final JScrollPane scrollPane;

    /**
     * Il livello di zoom corrente.
     */
    private double zoomLevel = 1.0;

    private int zoomInCounter = 0;
    private static final int MAX_ZOOM_CLICKS = 5;

    /**
     * Costruisce una nuova istanza di MapPage.
     * Configura la finestra principale, i pannelli, i bottoni
     * e gli ascoltatori di eventi.
     */
    public MapPage() {
        super((Frame) null, "Mappa del Fuoricorso", true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        Color camoscio = new Color(222, 184, 135);

        imagePanel = new ImagePanel();

        scrollPane = new JScrollPane(imagePanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(camoscio);

        Color marroneSabbia = new Color(244, 164, 96);
        JButton zoomInButton = new JButton("+");
        JButton zoomOutButton = new JButton("-");

        zoomInButton.setBackground(marroneSabbia);
        zoomOutButton.setBackground(marroneSabbia);

        zoomInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (zoomInCounter < MAX_ZOOM_CLICKS) {
                    zoom(1.1);
                    zoomInCounter++;
                }
            }
        });

        zoomOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoom(0.9);
                if (zoomInCounter > 0) {
                    zoomInCounter--;
                }
            }
        });

        buttonPanel.add(zoomInButton);
        buttonPanel.add(zoomOutButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setSize(960, 540);
        setLocationRelativeTo(null);

        getContentPane().setBackground(camoscio);

        // Calcola e imposta il livello di zoom iniziale per adattare l'immagine alla finestra
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                // Questo listener viene attivato la prima volta che la finestra diventa visibile
                double minZoomLevel = Math.max((double) scrollPane.getViewport().getWidth() / imagePanel.originalImage.getWidth(),
                        (double) scrollPane.getViewport().getHeight() / imagePanel.originalImage.getHeight());
                zoom(minZoomLevel / zoomLevel);
                removeComponentListener(this); // Rimuove il listener per evitare esecuzioni multiple
            }
        });
    }

    /**
     * Applica un fattore di zoom all'immagine e aggiorna la visualizzazione.
     * Il livello di zoom è limitato per non essere inferiore alle dimensioni
     * della finestra.
     *
     * @param factor Il fattore di scala da applicare al livello di zoom corrente.
     */
    private void zoom(double factor) {
        zoomLevel *= factor;

        double minZoomLevel = Math.max((double) scrollPane.getViewport().getWidth() / imagePanel.originalImage.getWidth(),
                (double) scrollPane.getViewport().getHeight() / imagePanel.originalImage.getHeight());

        if (zoomLevel < minZoomLevel) {
            zoomLevel = minZoomLevel;
        }

        imagePanel.setPreferredSize(new Dimension((int) (imagePanel.originalImage.getWidth() * zoomLevel),
                (int) (imagePanel.originalImage.getHeight() * zoomLevel)));
        imagePanel.revalidate();
        imagePanel.repaint();
    }

    /**
     * Classe interna che estende JPanel per disegnare l'immagine zoomabile.
     */
    private class ImagePanel extends JPanel {
        /**
         * L'immagine originale da visualizzare.
         */
        private BufferedImage originalImage;

        /**
         * Costruisce il pannello caricando l'immagine dal percorso specificato.
         */
        public ImagePanel() {
            try {
                URL imageUrl = getClass().getResource("/images/mappa_aperta.png");
                if (imageUrl == null) {
                    imageUrl = getClass().getResource("/images/resource_not_found.png");
                }
                if (imageUrl != null) {
                    originalImage = ImageIO.read(imageUrl);
                } else {
                    originalImage = new BufferedImage(960, 540, BufferedImage.TYPE_INT_ARGB);
                    System.err.println("Immagine di riserva non trovata.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (originalImage != null) {
                setPreferredSize(new Dimension(originalImage.getWidth(), originalImage.getHeight()));
            }
        }

        /**
         * Sovrascrive il metodo paintComponent per disegnare l'immagine scalata.
         *
         * @param g L'oggetto Graphics da utilizzare per il disegno.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (originalImage != null) {
                Graphics g2d = g.create();
                AffineTransform at = AffineTransform.getScaleInstance(zoomLevel, zoomLevel);
                ((java.awt.Graphics2D) g2d).drawImage(originalImage, at, null);
                g2d.dispose();
            }
        }
    }
}