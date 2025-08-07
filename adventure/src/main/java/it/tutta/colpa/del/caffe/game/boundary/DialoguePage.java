package it.tutta.colpa.del.caffe.game.boundary;

import it.tutta.colpa.del.caffe.game.control.Controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

public class DialoguePage extends JDialog implements DialogueGUI {

    private static final Font BUBBLE_FONT = new Font("Segoe UI", Font.PLAIN, 18);
    private static final Color BACKGROUND_COLOR = new Color(217, 236, 251);

    private static final Color NPC_BUBBLE_COLOR = new Color(255, 255, 255);
    private static final Color NPC_TEXT_COLOR = new Color(20, 20, 20);
    private static final Border NPC_BUBBLE_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220, 0)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
    );

    private static final Color USER_BUBBLE_COLOR = new Color(0, 82, 128);
    private static final Color USER_TEXT_COLOR = Color.WHITE;
    private static final Border USER_BUBBLE_BORDER = BorderFactory.createEmptyBorder(10, 15, 10, 15);

    private static final Font ANSWER_FONT = new Font("Segoe UI", Font.ITALIC, 18);
    private static final Color ANSWER_BG_COLOR = new Color(45, 45, 45);
    private static final Color ANSWER_BG_HOVER_COLOR = new Color(80, 80, 80);
    private static final Color ANSWER_TEXT_COLOR = Color.WHITE;
    private static final Border ANSWER_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
    );

    private final JPanel dialogueContentPanel;
    private final GridBagConstraints gbc;
    private int gridY = 0;

    private JPanel mainContainer;
    private JScrollPane scrollPane;

    private Controller controller;

    public DialoguePage(Frame owner, boolean modal) {
        super(owner, modal);
        initComponents();
        setTitle("Tutta Colpa del Caffè - Dialogo");

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
        setLocationRelativeTo(owner);
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


    @Override
    public void addNPCStatement(String npcName, String statement) {
        JTextArea bubble = createBubbleTextArea(npcName + ":\n" + statement, NPC_BUBBLE_COLOR, NPC_TEXT_COLOR, NPC_BUBBLE_BORDER);
        gbc.gridy = gridY++;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 10, 15, 80);
        dialogueContentPanel.add(bubble, gbc);
        updateLayout();
    }

    @Override
    public void addUserStatement(String userName, String statement) {
        JTextArea bubble = createBubbleTextArea(userName + ":\n" + statement, USER_BUBBLE_COLOR, USER_TEXT_COLOR, USER_BUBBLE_BORDER);
        gbc.gridy = gridY++;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 80, 15, 10);
        dialogueContentPanel.add(bubble, gbc);
        updateLayout();
    }

    @Override
    public void addUserPossibleAnswers(List<String> statements) {
        JPanel answersPanel = new JPanel();
        answersPanel.setLayout(new BoxLayout(answersPanel, BoxLayout.Y_AXIS));
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
                    System.out.println("Scelta utente: " + statement);
                    dialogueContentPanel.remove(answersPanel);
                    addUserStatement("Tu", statement);
                }
            });
            answersPanel.add(answerLabel);
            answersPanel.add(Box.createRigidArea(new Dimension(0, 10)));
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
        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        });
    }

    private void initComponents() {
        mainContainer = new JPanel();
        scrollPane = new JScrollPane();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));

        GroupLayout mainContainerLayout = new GroupLayout(mainContainer);
        mainContainer.setLayout(mainContainerLayout);
        mainContainerLayout.setHorizontalGroup(
                mainContainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(mainContainerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
                                .addContainerGap())
        );
        mainContainerLayout.setVerticalGroup(
                mainContainerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(mainContainerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                                .addContainerGap())
        );
        getContentPane().add(mainContainer, BorderLayout.CENTER);
        pack();
    }

    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(DialoguePage.class.getName()).log(Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(() -> {
            JFrame mainFrame = new JFrame("Applicazione Principale");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(400, 300);
            mainFrame.setLocationRelativeTo(null);

            JButton openDialogueButton = new JButton("Apri Dialogo");
            mainFrame.add(openDialogueButton);

            openDialogueButton.addActionListener(e -> {
                DialoguePage dialoguePage = new DialoguePage(mainFrame, true);

                dialoguePage.addNPCStatement("Mastro Programmatore", "Ciao! Ora sono un dialogo modale. Non puoi interagire con la finestra principale finché non mi chiudi.");
                List<String> possibleAnswers = new ArrayList<>();
                possibleAnswers.add("Ho capito, grazie!");
                possibleAnswers.add("Chiudi");
                dialoguePage.addUserPossibleAnswers(possibleAnswers);

                dialoguePage.setVisible(true);
            });

            mainFrame.setVisible(true);
        });
    }

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
            this.controller = c;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}