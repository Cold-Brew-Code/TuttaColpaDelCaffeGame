/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.boundary;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

import it.tutta.colpa.del.caffe.adventure.entity.Inventory;

public class InventoryPage extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(InventoryPage.class.getName());

    public InventoryPage(Inventory i) {
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
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel(){
            private final Image wp;
            {
                URL imgUrl = getClass().getResource("/images/zaino_interno.png");
                if (imgUrl != null) {
                    wp = new ImageIcon(imgUrl).getImage();
                } else {
                    wp = null;
                    System.err.println("Immagine non trovata: images/zaino_interno.png");
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
        firstItem = new javax.swing.JLabel();
        secondItem = new javax.swing.JLabel();
        thirdItem = new javax.swing.JLabel();
        fourthItem = new javax.swing.JLabel();
        descriptionLabel = new javax.swing.JTextArea();
        qtyFirstItem = new javax.swing.JLabel();
        qtySecondItem = new javax.swing.JLabel();
        qtyThirdItem = new javax.swing.JLabel();
        qtyFourthItem = new javax.swing.JLabel();
        scrollPane = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Inventario");

        firstItem.setBackground(new java.awt.Color(255, 255, 255));
        firstItem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        firstItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                firstItemMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                firstItemMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                firstItemMouseExited(evt);
            }
        });

        secondItem.setBackground(new java.awt.Color(255, 255, 255));
        secondItem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        secondItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                secondItemMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                secondItemMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                secondItemMouseExited(evt);
            }
        });

        thirdItem.setBackground(new java.awt.Color(255, 255, 255));
        thirdItem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        thirdItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                thirdItemMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                thirdItemMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                thirdItemMouseExited(evt);
            }
        });

        fourthItem.setBackground(new java.awt.Color(255, 255, 255));
        fourthItem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        fourthItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fourthItemMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                fourthItemMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                fourthItemMouseExited(evt);
            }
        });

        descriptionLabel.setText("Seleziona un elemento per conoscerne la descrizione.");
        descriptionLabel.setOpaque(true);
        descriptionLabel.setBackground(new Color(0xF3E28B));
        //descriptionLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        descriptionLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        //descriptionLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        //descriptionLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        descriptionLabel.setFocusable(false);
        descriptionLabel.setEditable(false);
        descriptionLabel.setLineWrap(true);
        descriptionLabel.setWrapStyleWord(true);
        scrollPane.setViewportView(descriptionLabel);

        qtyFirstItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        qtyFirstItem.setOpaque(true);
        qtyFirstItem.setBackground(new Color(0xF3E28B));
        qtyFirstItem.setText("0");
        qtyFirstItem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        qtyFirstItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        qtySecondItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        qtySecondItem.setOpaque(true);
        qtySecondItem.setBackground(new Color(0xF3E28B));
        qtySecondItem.setText("0");
        qtySecondItem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        qtySecondItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        qtyThirdItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        qtyThirdItem.setOpaque(true);
        qtyThirdItem.setBackground(new Color(0xF3E28B));
        qtyThirdItem.setText("0");
        qtyThirdItem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        qtyThirdItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        qtyFourthItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        qtyFourthItem.setOpaque(true);
        qtyFourthItem.setBackground(new Color(0xF3E28B));
        qtyFourthItem.setText("0");
        qtyFourthItem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        qtyFourthItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(qtyFirstItem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(qtySecondItem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(qtyFourthItem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(qtyThirdItem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(thirdItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(firstItem, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(secondItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fourthItem, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                .addGap(60, 60, 60))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(secondItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(firstItem, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(qtyFirstItem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(qtySecondItem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(thirdItem, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .addComponent(fourthItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(qtyFourthItem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(qtyThirdItem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        this.setResizable(false);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void firstItemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_firstItemMouseEntered
        descriptionLabel.append("Oggetto 1:\n Fa questo, quello e quell'altro ancora. \n MOUSE CLICKED");
    }//GEN-LAST:event_firstItemMouseEntered

    private void firstItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_firstItemMouseClicked
        descriptionLabel.append("Oggetto 1:\n Fa questo, quello e quell'altro ancora. \n MOUSE CLICKED");
    }//GEN-LAST:event_firstItemMouseClicked

    private void firstItemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_firstItemMouseExited
        //descriptionLabel.setText("");
    }//GEN-LAST:event_firstItemMouseExited

    private void secondItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_secondItemMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_secondItemMouseClicked

    private void secondItemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_secondItemMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_secondItemMouseEntered

    private void secondItemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_secondItemMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_secondItemMouseExited

    private void thirdItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_thirdItemMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_thirdItemMouseClicked

    private void thirdItemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_thirdItemMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_thirdItemMouseEntered

    private void thirdItemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_thirdItemMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_thirdItemMouseExited

    private void fourthItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fourthItemMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_fourthItemMouseClicked

    private void fourthItemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fourthItemMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_fourthItemMouseEntered

    private void fourthItemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fourthItemMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_fourthItemMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextArea descriptionLabel;
    private javax.swing.JLabel firstItem;
    private javax.swing.JLabel fourthItem;
    private javax.swing.JPanel panel;
    private javax.swing.JLabel qtyFirstItem;
    private javax.swing.JLabel qtySecondItem;
    private javax.swing.JLabel qtyThirdItem;
    private javax.swing.JLabel qtyFourthItem;
    private javax.swing.JLabel secondItem;
    private javax.swing.JLabel thirdItem;
    // End of variables declaration//GEN-END:variables
}
