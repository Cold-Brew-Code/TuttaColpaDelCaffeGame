package it.tutta.colpa.del.caffe.game.boundary;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Gestisce l'effetto di scrittura carattere per carattere (TypeWriter)
 * su una JTextArea.
 */
public class TypeWriterEffect {
    // omponenti per l'animazione
    private final Timer timer; // timer per l'effetto a intervalli
    private final JTextArea textArea; // area di testo dove scrivere
    private String textToWrite; // testo completo da visualizzare
    private int characterIndex; // posizione del carattere corrente
    private final int delay; // ritardo tra caratteri (ms)
    private boolean isRunning; // flag animazione in corso
    private Runnable onComplete; // callback al termine animazione

    /**
     * Costruttore: inizializza l'effetto con area di testo e delay.
     */
    public TypeWriterEffect(JTextArea textArea, int delay) {
        this.textArea = textArea;
        this.delay = delay;
        this.timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // aggiunge un carattere alla volta
                if (characterIndex < textToWrite.length()) {
                    try {
                        Document doc = textArea.getDocument();
                        doc.insertString(doc.getLength(),
                                String.valueOf(textToWrite.charAt(characterIndex)),
                                null);
                        characterIndex++;
                        textArea.setCaretPosition(doc.getLength()); // Auto-scroll
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                        timer.stop();
                    }
                } else {
                    stop(); // animazione completata
                    if (onComplete != null) {
                        onComplete.run(); // Esegui callback se presente
                    }
                }
            }
        });
    }

    // avvia l'animazione con un nuovo testo
    public void start(String text) {
        this.textToWrite = text;
        this.characterIndex = 0;
        this.isRunning = true;
        timer.start();
    }

    // interrompe l'animazione
    public void stop() {
        timer.stop();
        isRunning = false;
    }

    // completa immediatamente l'animazione
    public void skip() {
        if (isRunning) {
            timer.stop();
            try {
                // inserisce tutto il testo rimanente
                Document doc = textArea.getDocument();
                doc.insertString(doc.getLength(),
                        textToWrite.substring(characterIndex),
                        null);
                textArea.setCaretPosition(doc.getLength());
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
            isRunning = false;
            if (onComplete != null) {
                onComplete.run();
            }
        }
    }

    // verifica se l'animazione è in corso
    public boolean isRunning() {
        return isRunning;
    }

    // imposta callback al termine animazione
    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
    }
}