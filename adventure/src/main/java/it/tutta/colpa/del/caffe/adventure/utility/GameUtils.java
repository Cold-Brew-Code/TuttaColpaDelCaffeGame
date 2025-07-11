/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.utility;

import it.tutta.colpa.del.caffe.adventure.entity.AdvObject;
import it.tutta.colpa.del.caffe.adventure.entity.Inventory;
import java.util.List;

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
    public static AdvObject getObjectFromInventory(Inventory inventory, int id) {
    for (AdvObject o : inventory.getInventory().keySet()) {
        if (o.getId() == id) {
            return o;
        }
    }
    return null;
}


}
