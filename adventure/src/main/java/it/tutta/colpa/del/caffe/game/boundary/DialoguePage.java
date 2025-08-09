package it.tutta.colpa.del.caffe.game.boundary;

import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.game.control.DialogueController;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.*;
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

    private DialogueController controller;
    private boolean closable;

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

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowClosingActionPerformed();
            }
        });
    }

    /**
     * Metodo eseguito dopo la chiusura della finestra.
     * Termina l'applicazione.
     */
    private void onWindowClosingActionPerformed() {
        if (closable) {
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "<html>Non potrai uscire dalla finestra <br> fino a quando il dialogo <br> non sarà terminato.</html>",
                    "Attenzione!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTextArea createBubbleTextArea(String text, Color bgColor, Color textColor, Border border) {
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(BUBBLE_FONT);
        textArea.setForeground(textColor);
        textArea.setBackground(bgColor);
        textArea.setBorder(border);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(false);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        return textArea;
    }

    private JTextArea createAnswerTextArea(String text) {
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(ANSWER_FONT);
        textArea.setForeground(ANSWER_TEXT_COLOR);
        textArea.setBackground(ANSWER_BG_COLOR);
        textArea.setBorder(ANSWER_BORDER);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        textArea.setHighlighter(null);
        return textArea;
    }

    @Override
    public void addNPCStatement(String npcName, String statement) {
        JTextArea bubble = createBubbleTextArea(npcName + ":\n" + statement.translateEscapes(), NPC_BUBBLE_COLOR, NPC_TEXT_COLOR, NPC_BUBBLE_BORDER);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(bubble, BorderLayout.CENTER);
        gbc.gridy = gridY++;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 15, 80);
        dialogueContentPanel.add(wrapper, gbc);
        updateLayout();
    }

    @Override
    public void setPageClosable(boolean intention) {
        this.closable = intention;
    }

    @Override
    public void addUserStatement(String userName, String statement) {
        JTextArea bubble = createBubbleTextArea(userName + ":\n" + statement.translateEscapes(), USER_BUBBLE_COLOR, USER_TEXT_COLOR, USER_BUBBLE_BORDER);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(bubble, BorderLayout.CENTER);
        gbc.gridy = gridY++;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 80, 15, 10);
        dialogueContentPanel.add(wrapper, gbc);
        updateLayout();
    }

    @Override
    public void addUserPossibleAnswers(List<String> statements) {
        JPanel answersPanel = new JPanel();
        answersPanel.setLayout(new BoxLayout(answersPanel, BoxLayout.Y_AXIS));
        answersPanel.setOpaque(false);

        for (String statement : statements) {
            JTextArea answerArea = createAnswerTextArea(statement);
            answerArea.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    answerArea.setBackground(ANSWER_BG_HOVER_COLOR);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    answerArea.setBackground(ANSWER_BG_COLOR);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    for (MouseListener ml : answerArea.getMouseListeners()) {
                        answerArea.removeMouseListener(ml);
                    }
                    dialogueContentPanel.remove(answersPanel);
                    addUserStatement("Tu", statement);
                    controller.answerChosen(statement);
                }
            });
            answersPanel.add(answerArea);
            answersPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        gbc.gridy = gridY++;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);
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
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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
            this.controller = (DialogueController) c;
        } catch (Exception e) {
            throw new RuntimeException("Controller non valido: richiesto DialogueController", e);
        }
    }
}