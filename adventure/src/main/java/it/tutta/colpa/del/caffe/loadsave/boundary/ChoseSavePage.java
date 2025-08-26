/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package it.tutta.colpa.del.caffe.loadsave.boundary;

import it.tutta.colpa.del.caffe.game.boundary.GUI;
import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.loadsave.control.LoadController;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;

/**
 * @author giovanni
 */
public class ChoseSavePage extends JFrame implements GUI {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(ChoseSavePage.class.getName());
    LoadController c;

    public ChoseSavePage() {
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

        try {
            setSaves();
        } catch (FileNotFoundException e) {
            this.saves = null;
        }

        initComponents();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel() {
            private final Image wp;

            {
                URL imgUrl = getClass().getResource("/images/backgroundLoadSave.png");
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
        deleteButton = new javax.swing.JButton();
        scrollPanePanel = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        insideScrollPanePanel = new javax.swing.JPanel();
        indicationalLabel = new JLabel();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Carica salvataggio");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBackground(new Color(0, 0, 0, 0));
        scrollPane.getViewport().setBackground(new Color(0, 0, 0, 0));

        insideScrollPanePanel.setOpaque(false);
        insideScrollPanePanel.setBackground(new Color(255, 255, 255, 0));

        scrollPane.setOpaque(true);
        scrollPane.setBackground(new Color(255, 255, 255, 150));

        scrollPanePanel.setOpaque(false);
        scrollPanePanel.setBackground(new Color(0, 0, 0, 0));

        javax.swing.GroupLayout insideScrollPanePanelLayout = new javax.swing.GroupLayout(insideScrollPanePanel);
        insideScrollPanePanel.setLayout(insideScrollPanePanelLayout);
        insideScrollPanePanelLayout.setHorizontalGroup(
                insideScrollPanePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 748, Short.MAX_VALUE));
        insideScrollPanePanelLayout.setVerticalGroup(
                insideScrollPanePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 518, Short.MAX_VALUE));

        scrollPane.setViewportView(insideScrollPanePanel);

        javax.swing.GroupLayout scrollPanePanelLayout = new javax.swing.GroupLayout(scrollPanePanel);
        scrollPanePanel.setLayout(scrollPanePanelLayout);
        scrollPanePanelLayout.setHorizontalGroup(
                scrollPanePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane));
        scrollPanePanelLayout.setVerticalGroup(
                scrollPanePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane));

        indicationalLabel.setText("<html><h2>Scegli un salvataggio...</h2></html>");

        saveButton.setText("Carica");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        saveButton.setEnabled(false);

        deleteButton.setText("Elimina");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        deleteButton.setEnabled(false);

        cancelButton.setText("Annulla");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        populateLabels();
        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(mainPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(mainPanelLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(indicationalLabel)
                                                .addComponent(scrollPanePanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(25, 25, 25)));
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(indicationalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrollPanePanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, 38,
                                                Short.MAX_VALUE)
                                        .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap()));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        this.setResizable(false);
        setSize(800, 700); // o qualunque misura desideri
        setResizable(false);
        scrollPanePanel.setPreferredSize(new Dimension(750, 500)); // o la dimensione che vuoi
        scrollPanePanel.setMinimumSize(new Dimension(750, 500));
        scrollPanePanel.setMaximumSize(new Dimension(750, 500));
        scrollPane.setPreferredSize(new Dimension(750, 500));
        scrollPane.setMinimumSize(new Dimension(750, 500));
        scrollPane.setMaximumSize(new Dimension(750, 500));
        saveButton.setBackground(Color.WHITE);
        saveButton.setIcon(
                new ImageIcon((new ImageIcon(getClass().getResource("/images/save_icon.png")))
                        .getImage()
                        .getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        deleteButton.setBackground(Color.WHITE);
        deleteButton.setIcon(
                new ImageIcon((new ImageIcon(getClass().getResource("/images/trashcan.png")))
                        .getImage()
                        .getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setIcon(
                new ImageIcon((new ImageIcon(getClass().getResource("/images/exit_icon.png")))
                        .getImage()
                        .getScaledInstance(32, 32, Image.SCALE_SMOOTH)));

        pack();
        indicationalLabel.setForeground(Color.WHITE);
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveButtonActionPerformed
        if (selectedSave != null && !selectedSave.isDeleted()) {
            c.load(selectedSave.getPath());
        }
    }// GEN-LAST:event_saveButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (selectedSave != null && !selectedSave.isDeleted()) {
            int scelta = javax.swing.JOptionPane.showConfirmDialog(
                    this,
                    "Vuoi davvero eliminare il salvataggio selezionato?",
                    "Conferma",
                    javax.swing.JOptionPane.YES_NO_OPTION);

            if (scelta == javax.swing.JOptionPane.YES_OPTION) {
                c.deleteSave(selectedSave.getPath());

                selectedSave.delete();
                clearSelection();
                populateLabels();
            }
        }
    }

    public void updateSaveList(List<String> saveFiles) {
        this.saves.clear();
        for (String fileName : saveFiles) {
            this.saves.add(new Save(fileName));
        }
        populateLabels();
        saveButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }// GEN-LAST:event_deleteButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();

        if (c != null) {
            c.cancelOperation();
        }
    }// GEN-LAST:event_cancelButtonActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowClosed
        c.cancelOperation();
        /**
         * FIXIT, logica da spostare in EngineController
         * try {
         * deleteFileFromFileSystem();
         * } catch (FileNotFoundException e) {
         * System.err.println("Eliminazione file non riuscita");
         * }
         */
    }// GEN-LAST:event_formWindowClosed

    private void deleteFileFromFileSystem() throws FileNotFoundException {
        File dir = new File("./src/main/resources/saves");
        if (!(dir.exists() && dir.isDirectory())) {
            throw new FileNotFoundException("Cartella salvataggi non presente");
        }

        for (Save s : saves) {
            if (s.isDeleted()) {
                File fileToDelete = new File(dir, s.getPath());
                if (fileToDelete.exists()) {
                    boolean deleted = fileToDelete.delete();
                    if (!deleted) {
                        System.err.println("Impossibile eliminare: " + s.getPath());
                    }
                }
            }
        }
    }

    private void setSaves() throws FileNotFoundException {
        List<String> saveFiles = it.tutta.colpa.del.caffe.loadsave.control.SaveLoad.getSaveFiles();

        this.saves = new java.util.ArrayList<>();
        for (String fileName : saveFiles) {
            this.saves.add(new Save(fileName));
        }
    }

    public void refreshSaveList() {
        try {
            setSaves();
            populateLabels();
        } catch (FileNotFoundException e) {
            notifyError("Errore", "Impossibile aggiornare la lista dei salvataggi");
        }
    }

    private void populateLabels() {
        insideScrollPanePanel.removeAll(); // PULISCI prima di riempire
        insideScrollPanePanel.setLayout(new BoxLayout(insideScrollPanePanel, BoxLayout.Y_AXIS));

        try {
            if (this.saves.isEmpty()) {
                JLabel label = new JLabel("<html><h3> Nessun salvataggio trovato</h3></html>");
                label.setOpaque(false);
                label.setForeground(Color.WHITE);
                insideScrollPanePanel.add(label);
            } else {
                for (Save save : saves) {
                    JLabel label = new JLabel();
                    save.setLabel(label);

                    if (save.isDeleted()) {
                        label.setBackground(new Color(255, 150, 150, 255)); // alpha value 255 (opaco)
                        label.setForeground(Color.WHITE);
                    } else {
                        label.setBackground(new Color(255, 255, 255, 180)); // sfondo semi-trasparente
                        label.setForeground(Color.BLACK);
                    }

                    label.setOpaque(true);
                    label.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.GRAY, 1),
                            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                    label.setFont(new Font("Arial", Font.PLAIN, 16));
                    label.setAlignmentX(Component.CENTER_ALIGNMENT);
                    label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

                    if (!save.isDeleted()) {
                        label.addMouseListener(new java.awt.event.MouseAdapter() {
                            @Override
                            public void mouseClicked(java.awt.event.MouseEvent e) {
                                selectedSave = save;
                                for (Save s : saves) {
                                    if (!s.isDeleted() && s.getLabel() != null) {
                                        s.getLabel().setBackground(new Color(255, 255, 255, 180));
                                        s.getLabel().setForeground(Color.BLACK);
                                    }
                                }
                                label.setBackground(new Color(173, 216, 230, 200)); // azzurro chiaro
                                label.setForeground(Color.BLACK);
                                saveButton.setEnabled(true);
                                deleteButton.setEnabled(true);
                            }
                        });
                    } else {
                        // Disabilita completamente i salvataggi eliminati
                        label.setEnabled(false);
                        label.setCursor(Cursor.getDefaultCursor()); // Cursore normale invece di mano
                    }

                    insideScrollPanePanel.add(label);
                    insideScrollPanePanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spaziatura
                }
            }
        } catch (NullPointerException e) {
            JLabel label = new JLabel("<html><h3> Nessun salvataggio trovato</h3></html>");
            label.setOpaque(false);
            label.setForeground(Color.WHITE);
            insideScrollPanePanel.add(label);
        }

        // Aggiorna il pannello
        insideScrollPanePanel.revalidate();
        insideScrollPanePanel.repaint();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton deleteButton;
    private JLabel indicationalLabel;
    private javax.swing.JPanel insideScrollPanePanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton saveButton;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JPanel scrollPanePanel;
    // End of variables declaration//GEN-END:variables
    private List<Save> saves;
    private Save selectedSave;

    @Override
    public void open() {
        this.setVisible(true);
    }

    @Override
    public void close() {
        this.dispose();
    }

    @Override
    public void linkController(Controller c) {
        try {
            this.c = (LoadController) c;
            takeSaves();
        } catch (Exception e) {
            throw new RuntimeException("Il controller fornito non è adeguato per ChoseSavePage");
        }
    }

    public void takeSaves() {
        if (c != null) {
            c.takeSaves();
        } else {
            try {
                setSaves();
                populateLabels();
            } catch (FileNotFoundException e) {
                notifyError("Errore", "Impossibile caricare i salvataggi");
            }
        }
    }

    private class Save {
        private JLabel saveLabel;
        private String path;
        private boolean deleted;

        public Save(String path) {
            this.path = path;
            this.saveLabel = null;
            this.deleted = false;
        }

        public void setLabel(JLabel label) {
            this.saveLabel = label;
            updateLabelAppearance(); // aggiorna l'aspetto quando viene impostata la label
        }

        public String getPath() {
            return path;
        }

        public JLabel getLabel() {
            return saveLabel;
        }

        public void delete() {
            this.deleted = true;
            updateLabelAppearance(); // aggiorna l'aspetto quando viene eliminato
        }

        public boolean isDeleted() {
            return deleted;
        }

        private void updateLabelAppearance() {
            if (saveLabel != null) {
                if (deleted) {
                    saveLabel.setBackground(new Color(255, 150, 150, 255)); // alpha value 255
                    saveLabel.setForeground(Color.WHITE);
                    saveLabel.setText("<html><div style='text-align: center;'>" +
                            getFormattedName() +
                            "<br><b>[ELIMINATO]</b></div></html>");
                    saveLabel.setEnabled(false);
                    saveLabel.setCursor(Cursor.getDefaultCursor());
                } else {
                    saveLabel.setBackground(new Color(255, 255, 255, 180));
                    saveLabel.setForeground(Color.BLACK);
                    saveLabel.setText(getFormattedName());
                    saveLabel.setEnabled(true);
                }

                // Forza il repaint per assicurarsi che i cambiamenti siano visibili
                saveLabel.repaint();
            }
        }

        private String getFormattedName() {
            // Formatta il nome del file in modo più leggibile
            String name = path.replace(".save", "");
            name = name.replace("_", " ");
            name = name.replace("-", ":");
            return "Salvataggio del " + name;
        }

    }

    private void clearSelection() {
        selectedSave = null;
        saveButton.setEnabled(false);
        deleteButton.setEnabled(false);

        // Ripristina i colori di tutte le label non eliminate
        for (Save s : saves) {
            if (!s.isDeleted() && s.getLabel() != null) {
                s.getLabel().setBackground(new Color(255, 255, 255, 180));
                s.getLabel().setForeground(Color.BLACK);
            }
        }
    }

    @Override
    public void notifyError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showInformation(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
