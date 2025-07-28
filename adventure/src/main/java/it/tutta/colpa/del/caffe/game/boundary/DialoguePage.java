/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.boundary;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;

/**
 * @author giovanni
 */
public class DialoguePage extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DialoguePage.class.getName());
    private javax.swing.JPanel dialogueContentPanel;
    private javax.swing.JPanel mainContainer;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JPanel scrollPaneContainer;


    public DialoguePage() {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
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
        //</editor-fold>
        initComponents();

        scrollPaneContainer.setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        dialogueContentPanel = new javax.swing.JPanel();
        dialogueContentPanel.setLayout(new javax.swing.BoxLayout(dialogueContentPanel, javax.swing.BoxLayout.Y_AXIS));
        dialogueContentPanel.setOpaque(false);
        scrollPane.setViewportView(dialogueContentPanel);

        setResizable(false);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainContainer = new javax.swing.JPanel();
        scrollPaneContainer = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout scrollPaneContainerLayout = new javax.swing.GroupLayout(scrollPaneContainer);
        scrollPaneContainer.setLayout(scrollPaneContainerLayout);
        scrollPaneContainerLayout.setHorizontalGroup(
                scrollPaneContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(scrollPaneContainerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)
                                .addContainerGap())
        );
        scrollPaneContainerLayout.setVerticalGroup(
                scrollPaneContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(scrollPaneContainerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
                                .addContainerGap())
        );

        javax.swing.GroupLayout mainContainerLayout = new javax.swing.GroupLayout(mainContainer);
        mainContainer.setLayout(mainContainerLayout);
        mainContainerLayout.setHorizontalGroup(
                mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainContainerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(scrollPaneContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        mainContainerLayout.setVerticalGroup(
                mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainContainerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(scrollPaneContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
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
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DialoguePage dialoguePage = new DialoguePage();
            dialoguePage.addNPCStatement("NPC: Salve, viandante. Cosa cerchi?");
            List<String> possibleAnswers = new ArrayList<>();
            possibleAnswers.add("Cerco solo un posto dove riposare.");
            possibleAnswers.add("Sai dove si trova la gilda dei maghi?");
            possibleAnswers.add("Niente di particolare, sono di passaggio.");
            dialoguePage.addUserPossibleAnswers(possibleAnswers);
            dialoguePage.setVisible(true);
        });
    }

    public void addNPCStatement(String statement) {
        JLabel statementLabel = new JLabel("<html><p style=\"width:500px\">" + statement + "</p></html>");
        statementLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));

        dialogueContentPanel.add(statementLabel);
        dialogueContentPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 5)));

        dialogueContentPanel.revalidate();
        dialogueContentPanel.repaint();
    }

    public void addUserStatement(String statement) {
        JLabel statementLabel = new JLabel(statement);
        statementLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));

        dialogueContentPanel.add(statementLabel);
        dialogueContentPanel.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 5)));

        dialogueContentPanel.revalidate();
        dialogueContentPanel.repaint();
    }

    public void addUserPossibleAnswers(List<String> statements) {
        List<JLabel> answerLabels = new ArrayList<>();

        for (String statement : statements) {
            JLabel answerLabel = new JLabel("<html><i>" + statement + "</i></html>");
            answerLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 10, 2, 10));
            answerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            answerLabels.add(answerLabel);

            answerLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (JLabel labelToRemove : answerLabels) {
                        dialogueContentPanel.remove(labelToRemove);
                    }

                    addUserStatement("Tu: " + statement);

                    dialogueContentPanel.revalidate();
                    dialogueContentPanel.repaint();
                }
            });
            dialogueContentPanel.add(answerLabel);
        }
        dialogueContentPanel.revalidate();
        dialogueContentPanel.repaint();
    }
}