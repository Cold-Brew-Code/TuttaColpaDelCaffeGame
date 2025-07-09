package it.tutta.colpa.del.caffe.adventure.boundary;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.net.URL;

public class MainPage extends JFrame {

    JPanel wallpaper = new JPanel(){
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

    public MainPage(){
        this.setResizable(false);
        this.setPreferredSize(new Dimension(960,540));
        this.setTitle("Tutta colpa del Caffè!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        URL gameIcon = getClass().getResource("/images/icon.png");
        try{
            this.setIconImage(new ImageIcon(gameIcon).getImage());
        }catch(Exception e){
            System.err.println("Icona del gioco non presente.");
        }
        wallpaper.setVisible(true);

        JPanel buttonPanel = new JPanel(new GridLayout(3,1,0,10));
        buttonPanel.setOpaque(false);
        this.add(wallpaper);
        buttonPanel.add(start);
        buttonPanel.add(load);
        buttonPanel.add(exit);

        buttonPanel.setBounds(((960 - 260) / 2 )+325,
                                540 - 250 - 50,
                                260, 250);

        wallpaper.add(buttonPanel);

        wallpaper.add(buttonPanel);
        wallpaper.setLayout(null);
        this.pack();
        setLocationRelativeTo(null);
        this.setVisible(true);


        // ====================================================
        // =            BUTTON's LISTENERS                    =
        // ====================================================

        start.addActionListener(e -> {
            new GamePage(this).setVisible(true);
        });

        load.addActionListener(e -> {
            System.out.println("Caricamento partita...");
        });

        exit.addActionListener(e -> {
            System.out.println("Chiusura del gioco...");
            System.exit(0);
        });
    }

    public static void main(String[] args){
        new MainPage();
    }

    private class PButton extends JButton{

        private final Image backgroundImage = new ImageIcon(getClass().getResource("/images/button.png"))
                .getImage();

        public PButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setForeground(new Color(0xFFFFFF));
            setPreferredSize(new Dimension(250,75));
            setFont(new Font("Arial", Font.BOLD, 24));
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            super.paintComponent(g);
        }
    }
}
