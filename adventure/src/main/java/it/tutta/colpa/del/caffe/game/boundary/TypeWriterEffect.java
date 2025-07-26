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
    private Timer timer;
    private final JTextArea textArea;
    private String textToWrite;
    private int characterIndex;
    private int delay;
    private boolean isRunning;
    private Runnable onComplete;

    /**
     * Costruttore - inizializza l'effetto con l'area di testo e il delay iniziale
     * 
     * @param textArea     Area di testo su cui applicare l'effetto
     * @param initialDelay Ritardo iniziale tra i caratteri (in ms)
     */
    public TypeWriterEffect(JTextArea textArea, int initialDelay) {
        this.textArea = textArea;
        this.delay = initialDelay;
        this.timer = new Timer(initialDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (characterIndex < textToWrite.length()) {
                    try {
                        Document doc = textArea.getDocument();
                        doc.insertString(doc.getLength(),
                                String.valueOf(textToWrite.charAt(characterIndex)),
                                null);
                        characterIndex++;
                        textArea.setCaretPosition(doc.getLength());
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                        timer.stop();
                    }
                } else {
                    stop();
                    if (onComplete != null) {
                        onComplete.run();
                    }
                }
            }
        });
    }

    /**
     * Imposta un nuovo ritardo per l'animazione
     * 
     * @param newDelay Nuovo ritardo tra caratteri (in ms)
     */
    public void setDelay(int newDelay) {
        this.delay = newDelay;
        timer.setDelay(newDelay);
        if (newDelay == 0 && isRunning) {
            skip(); // Se disattivato, salta l'animazione corrente
        }
    }

    /**
     * @return Il ritardo corrente tra caratteri (in ms)
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Avvia l'animazione con un nuovo testo
     * 
     * @param text Testo da visualizzare
     */
    public void start(String text) {
        this.textToWrite = text;
        this.characterIndex = 0;
        this.isRunning = true;
        timer.start();
    }

    /**
     * Interrompe l'animazione senza completarla
     */
    public void stop() {
        timer.stop();
        isRunning = false;
    }

    /**
     * Completa immediatamente l'animazione corrente
     */
    public void skip() {
        if (isRunning) {
            timer.stop();
            try {
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

    /**
     * @return True se l'animazione è in corso
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Imposta un callback da eseguire al termine dell'animazione
     * 
     * @param onComplete Callback da eseguire
     */
    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
    }
}