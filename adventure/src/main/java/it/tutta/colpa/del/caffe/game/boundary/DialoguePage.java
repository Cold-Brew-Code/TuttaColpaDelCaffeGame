package it.tutta.colpa.del.caffe.game.boundary;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class DialoguePage extends javax.swing.JFrame {

    private static final Font BUBBLE_FONT = new Font("Segoe UI", Font.PLAIN, 18);
    private static final Color BACKGROUND_COLOR = new Color(217, 236, 251);

    private static final Color NPC_BUBBLE_COLOR = new Color(255, 255, 255);
    private static final Color NPC_TEXT_COLOR = new Color(20, 20, 20);
    private static final Border NPC_BUBBLE_BORDER = javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(220, 220, 220)),
            javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15)
    );

    private static final Color USER_BUBBLE_COLOR = new Color(0, 82, 128);
    private static final Color USER_TEXT_COLOR = Color.WHITE;
    private static final Border USER_BUBBLE_BORDER = javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15);

    private static final Font ANSWER_FONT = new Font("Segoe UI", Font.ITALIC, 18);
    private static final Color ANSWER_BG_COLOR = new Color(45, 45, 45);
    private static final Color ANSWER_BG_HOVER_COLOR = new Color(80, 80, 80);
    private static final Color ANSWER_TEXT_COLOR = Color.WHITE;
    private static final Border ANSWER_BORDER = javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(Color.WHITE, 1),
            javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15)
    );

    private final JPanel dialogueContentPanel;
    private final GridBagConstraints gbc;
    private int gridY = 0;

    public DialoguePage() {
        initComponents();
        setTitle("Tutta Colpa del Caffè");

        mainContainer.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        dialogueContentPanel = new JPanel(new GridBagLayout());
        dialogueContentPanel.setOpaque(false);
        scrollPane.setViewportView(dialogueContentPanel);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        setResizable(false);
        setLocationRelativeTo(null);
    }

    private JTextArea createBubbleTextArea(String text, Color bgColor, Color textColor, Border border) {
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(BUBBLE_FONT);
        textArea.setForeground(textColor);
        textArea.setBackground(bgColor);
        textArea.setBorder(border);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        return textArea;
    }

    public void addNPCStatement(String npcName, String statement) {
        JTextArea bubble = createBubbleTextArea(npcName + ":\n" + statement, NPC_BUBBLE_COLOR, NPC_TEXT_COLOR, NPC_BUBBLE_BORDER);
        gbc.gridy = gridY++;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 10, 15, 80);
        dialogueContentPanel.add(bubble, gbc);
        updateLayout();
    }

    public void addUserStatement(String userName, String statement) {
        JTextArea bubble = createBubbleTextArea(userName + ":\n" + statement, USER_BUBBLE_COLOR, USER_TEXT_COLOR, USER_BUBBLE_BORDER);
        gbc.gridy = gridY++;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 80, 15, 10);
        dialogueContentPanel.add(bubble, gbc);
        updateLayout();
    }

    public void addUserPossibleAnswers(List<String> statements) {
        JPanel answersPanel = new JPanel();
        answersPanel.setLayout(new javax.swing.BoxLayout(answersPanel, javax.swing.BoxLayout.Y_AXIS));
        answersPanel.setOpaque(false);

        for (String statement : statements) {
            JLabel answerLabel = new JLabel(statement, JLabel.CENTER);
            answerLabel.setFont(ANSWER_FONT);
            answerLabel.setForeground(ANSWER_TEXT_COLOR);
            answerLabel.setBorder(ANSWER_BORDER);
            answerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            answerLabel.setOpaque(true);
            answerLabel.setBackground(ANSWER_BG_COLOR);
            answerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            answerLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    answerLabel.setBackground(ANSWER_BG_HOVER_COLOR);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    answerLabel.setBackground(ANSWER_BG_COLOR);
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    dialogueContentPanel.remove(answersPanel);
                    addUserStatement("Tu", statement);
                }
            });
            answersPanel.add(answerLabel);
            answersPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));
        }
        gbc.gridy = gridY++;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 0, 0);
        dialogueContentPanel.add(answersPanel, gbc);
        updateLayout();
    }

    private void updateLayout() {
        dialogueContentPanel.revalidate();
        dialogueContentPanel.repaint();
        javax.swing.SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        });
    }

    private void initComponents() {

        mainContainer = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 600));

        javax.swing.GroupLayout mainContainerLayout = new javax.swing.GroupLayout(mainContainer);
        mainContainer.setLayout(mainContainerLayout);
        mainContainerLayout.setHorizontalGroup(
                mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainContainerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
                                .addContainerGap())
        );
        mainContainerLayout.setVerticalGroup(
                mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainContainerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                                .addContainerGap())
        );

        getContentPane().add(mainContainer, java.awt.BorderLayout.CENTER);

        pack();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(DialoguePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            DialoguePage dialoguePage = new DialoguePage();
            dialoguePage.setVisible(true);

            dialoguePage.addNPCStatement("Mastro Programmatore", "Bentornato! Vedo che sei pronto per il prossimo passo. La nostra interfaccia di dialogo ora è robusta e flessibile. Ma capisci *perché* questa soluzione funziona dove le altre hanno fallito?");
            dialoguePage.addUserStatement("Tu", "Credo di sì. Abbiamo sostituito JLabel con JTextArea perché quest'ultimo gestisce il 'word wrap' in modo nativo, senza bisogno di trucchi con l'HTML, giusto?");
            dialoguePage.addNPCStatement("Mastro Programmatore", "Esattamente! Ma c'è di più. Il vero eroe qui è il GridBagLayout. A differenza di BoxLayout o FlowLayout, che sono più semplici, GridBagLayout ci dà un controllo chirurgico su ogni cella della griglia. L'uso degli 'insets' è stato il trucco finale per dare l'effetto della bolla di chat.");
            dialoguePage.addUserStatement("Tu", "Quindi gli insets creano quello spazio vuoto a destra o a sinistra, facendo sembrare il JTextArea una bolla più stretta del suo contenitore, pur permettendogli di adattarsi in larghezza?");
            dialoguePage.addNPCStatement("Mastro Programmatore", "Hai colto perfettamente nel segno! Stiamo dicendo al layout: 'Caro GridBagLayout, fai in modo che questo componente occupi la sua cella, ma lascia un margine interno di 80 pixel a sinistra (o a destra)'. Questo crea l'illusione di una bolla fluttuante, ma con un layout che si adatta sempre perfettamente allo spazio disponibile. È l'unione di un componente adatto (JTextArea) e di un layout potente (GridBagLayout).");
            dialoguePage.addUserStatement("Tu", "Fantastico, ora è tutto molto più chiaro. Non è solo 'fare le cose', ma capire gli strumenti giusti per il lavoro. Grazie mille per la spiegazione dettagliata!");
            dialoguePage.addNPCStatement("Mastro Programmatore", "Proprio così. Ogni Layout Manager ha il suo scopo preciso. Conoscerli a fondo è il marchio di un vero artigiano del software. Ora, cosa vuoi fare?");

            List<String> possibleAnswers = new ArrayList<>();
            possibleAnswers.add("Ripassiamo di nuovo il GridBagLayout.");
            possibleAnswers.add("Parliamo di come aggiungere immagini o altri componenti nelle bolle.");
            possibleAnswers.add("Per oggi basta così, grazie!");
            dialoguePage.addUserPossibleAnswers(possibleAnswers);
        });
    }

    private javax.swing.JPanel mainContainer;
    private javax.swing.JScrollPane scrollPane;
}