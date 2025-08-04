/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.control;

import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.Item;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.GameUtils;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;

/**
 *
 * @author giova
 */
public class BuildObserver implements GameObserver {

    /**
     *
     * @param description
     * @param parserOutput
     * @return
     */
    @Override
    public String update(GameDescription description, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        // controllo se l'oggetto è stato specificato
        Object obj = parserOutput.getObject();

        if (parserOutput.getCommand().getType() == CommandType.CREATE) {
            boolean isRoomCurrent1 = false;
            boolean isRoomCurrent2 = false;
            if (obj == null) {
                msg.append("Non hai specificato gli oggetto da combinare. (scrivi 'combina nome oggetto nome oggetto')");
                return msg.toString();
            } else if (obj instanceof GeneralItem) {
                GeneralItem pippo = (GeneralItem) obj;
                boolean labda = pippo.getAlias().stream().anyMatch(alias -> alias.equalsIgnoreCase("carta magica"));
                if (pippo.getName().equalsIgnoreCase("carta magica") || labda) {

                    if (GameUtils.getObjectFromInventory(description.getInventory(), 14) != null) {
                        msg.append("non puoi avere 200 mila tessere magiche, la vita sarebbe troppo bella. Hai già la tessera magica nell'inventario");
                    } else {
                        boolean objInv1 = GameUtils.getObjectFromInventory(description.getInventory(), 12) != null; // carta 
                        boolean objInv2 = GameUtils.getObjectFromInventory(description.getInventory(), 6) != null; //scheda madre
                        int IdRoomCurrent = description.getCurrentRoom().getId();
                        if (objInv1 && objInv2) {
                            /**IteamCombinable cartaMagica= new IteamCombinable()
                             * quantity= sarà 1;
                             * description.getInventory().add(picartaMagicappo, quantity);
                             * */
                            GeneralItem obj1 = GameUtils.getObjectFromInventory(description.getInventory(), 12);
                            GeneralItem obj2 = GameUtils.getObjectFromInventory(description.getInventory(), 6);
                            Item objInvR1 = (Item) obj1;
                            Item objInvR2 = (Item) obj2;

                            description.getInventory().remove(objInvR1);
                            description.getInventory().remove(objInvR2);
                            msg.append("Fabbricatum Objectum!");
                        } else if (objInv1 == false && objInv2) {
                            isRoomCurrent1 = description.getCurrentRoom().getId() == 19;
                            if (isRoomCurrent1) {
                                msg.append("devi prima raccogliere l'oggetto ").append(description.getCurrentRoom().getObject(12).getName()).append("prima di combinare i due oggetti");;
                            }// non puoi costrurie
                        } else if (objInv2 == false && objInv1) {
                            isRoomCurrent2 = description.getCurrentRoom().getId() == 30;
                            if (isRoomCurrent2) {
                                msg.append("devi prima raccogliere l'oggetto ").append(description.getCurrentRoom().getObject(6).getName()).append("prima di combinare i due oggetti");
                                // else non puoi costrurie   
                            } else if (IdRoomCurrent != 19 || IdRoomCurrent != 30) {
                                msg.append("Non hai gli oggetti necessari per scostruire la carta magica");
                            }
                        }

                    }
                }

            } else {// se non è un oggetto allora da errore ( devo considerare il caso in cui abbia indicato i due oggetti che possono essere combinati che danno la magia
                msg.append("non puoi fare ste magie");
            }

        }
        return msg.toString();
    }
}
