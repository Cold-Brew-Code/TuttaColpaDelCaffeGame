/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.utility;

import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.Inventory;


/**
 *
 * @author pierpaolo
 */
public class GameUtils {

    /**
     *
     * @param inventory
     * @param id
     * @return
     */
    public static GeneralItem getObjectFromInventory( Inventory inventory, int id) {
        for (GeneralItem o : inventory.getInventory().keySet()) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }

}
