/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.utility;

import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.Inventory;

/**
 * Classe di utilità per operazioni relative al gioco.
 * Contiene metodi statici per facilitare la gestione dell'inventario e altri oggetti di gioco.
 * 
 * @author giova
 */
public class GameUtils {

    /**
     * Recupera un oggetto dall'inventario in base al suo ID.
     * 
     * @param inventory l'inventario da cui cercare l'oggetto
     * @param id l'identificatore dell'oggetto da cercare
     * @return l'oggetto {@link GeneralItem} corrispondente all'ID, oppure {@code null} se non trovato
     */
    public static GeneralItem getObjectFromInventory(Inventory inventory, int id) {
        for (GeneralItem o : inventory.getInventory().keySet()) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }
}

