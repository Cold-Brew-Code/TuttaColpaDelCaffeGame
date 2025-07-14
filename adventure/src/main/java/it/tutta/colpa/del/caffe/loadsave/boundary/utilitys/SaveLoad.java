/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.loadsave.boundary.utilitys;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;


/**
 *
 * @author giova
 */
public class SaveLoad {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public static void saveGame(GameDescription description) throws  IOException{


        String timestamp = LocalDateTime.now().format(FORMATTER);
        String nameFile =  timestamp + ".save";
        String path = System.getProperty("user.dir")+ File.separator + "resources"+ File.separator + "saves"
                    + File.separator + nameFile;


        
        FileOutputStream outFile= new FileOutputStream(path);
        ObjectOutputStream objFile= new ObjectOutputStream(outFile);
        try{
            objFile.writeObject(description);
            System.out.println("Salvataggio completato in: " + path);
        }catch(IOException e){
            e.getStackTrace();
        }
        outFile.close();
        objFile.close();
    }

    
}
