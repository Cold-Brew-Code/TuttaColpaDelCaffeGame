package it.tutta.colpa.del.caffe.rete;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import it.tutta.colpa.del.caffe.game.entity.Command;
import it.tutta.colpa.del.caffe.game.entity.Dialogo;
import it.tutta.colpa.del.caffe.game.entity.GameMap;
import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.IteamCombinable;
import it.tutta.colpa.del.caffe.game.entity.Item;
import it.tutta.colpa.del.caffe.game.entity.ItemContainer;
import it.tutta.colpa.del.caffe.game.entity.ItemRead;
import it.tutta.colpa.del.caffe.game.entity.NPC;
import it.tutta.colpa.del.caffe.game.entity.Room;
import it.tutta.colpa.del.caffe.game.utility.Direzione;

/**
 * Classe che gestisce il database.
 * Contiene tutte le query necessarie per interrogare le relazioni e fornire
 * una risposta corretta alle richieste ricevute dal server.
 *
 * @author giovav
 */
public class DataBaseManager {
    private Connection connection;

    /**
     * Stabilisce la connessione con il database utilizzando le credenziali fornite.
     *
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    private void establishConnection() throws SQLException {
        Properties dbProperties = new Properties();
        String username = "cacca";
        dbProperties.setProperty("user", username);
        String password = "12345";
        dbProperties.setProperty("pw", password);
        String dataBasePath = "jdbc:h2:./database;INIT=RUNSCRIPT FROM 'classpath:inizioDB.sql'";
        connection = DriverManager.getConnection(dataBasePath,
                dbProperties);
    }

    /**
     * Costruttore della classe DataBaseManager.
     * Inizializza e stabilisce la connessione al database.
     *
     * @throws SQLException se si verifica un errore durante la connessione al database.
     */
    public DataBaseManager() throws SQLException {
        establishConnection();
    }

    /**
     * Chiude la connessione al database.
     *
     * @throws SQLException se si verifica un errore durante la chiusura della connessione.
     */
    public void closeConnection() throws SQLException {
        connection.close();
        connection = null;
    }

    // Commands

    /**
     * Recupera tutti i comandi disponibili dal database, inclusi i loro alias.
     *
     * @return una lista di oggetti Command.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    public List<Command> askForCommands() throws SQLException {
        List<Command> commands = new ArrayList<>();
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Commands;");
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            Command c = new Command(rs.getString("name"));
            c.setAlias(askForCommandAlias(rs.getInt("id")).toArray(new String[0]));
            commands.add(c);
        }
        rs.close();
        pstm.close();
        return commands;
    }

    /**
     * Recupera gli alias di un comando specifico dal database.
     *
     * @param commandID l'ID del comando.
     * @return un insieme di stringhe contenente gli alias del comando.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    private Set<String> askForCommandAlias(int commandID) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM CommandAlias WHERE id = ?");
        pstm.setInt(1, commandID);
        ResultSet rs = pstm.executeQuery();

        Set<String> alias = new HashSet<>();
        while (rs.next()) {
            alias.add(rs.getString("command_alias"));
        }

        rs.close();
        pstm.close();
        return alias;
    }

    /**
     * Costruisce e restituisce la mappa di gioco completa, con stanze e collegamenti.
     *
     * @return l'oggetto GameMap inizializzato.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    public GameMap askForGameMap() throws SQLException {
        GameMap gameMap = new GameMap();
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM Rooms ORDER BY id ASC;");
        Map<Integer, Room> nodes = new HashMap<>();
        if (rs.next()) {
            Room room = generateRoom(rs);
            nodes.put(room.getId(), room);
            gameMap.aggiungiStanza(room, true);
        }
        while (rs.next()) {
            Room room = generateRoom(rs);
            nodes.put(room.getId(), room);
            gameMap.aggiungiStanza(room);
        }
        rs.close();
        stm.close();
        return getLinkedMap(gameMap, nodes);
    }


    // Rooms

    /**
     * Genera un oggetto Room a partire dai dati di un ResultSet.
     *
     * @param room il ResultSet contenente i dati della stanza.
     * @return un nuovo oggetto Room.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    private Room generateRoom(ResultSet room) throws SQLException {
        return new Room(
                room.getInt("id"),
                room.getString("name"),
                room.getString("description"),
                room.getString("look"),
                room.getBoolean("is_visible"),
                !room.getBoolean("allowed_entry"),
                room.getString("image_path"),
                askForInRoomItems(room.getInt("id")),
                askForNPCs(room.getInt("id"))
        );
    }

    /**
     * Collega le stanze nella mappa di gioco in base alle connessioni definite nel database.
     *
     * @param map   la mappa di gioco da popolare.
     * @param nodes una mappa di stanze indicizzate per ID.
     * @return la mappa di gioco con le stanze collegate.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    private GameMap getLinkedMap(GameMap map, Map<Integer, Room> nodes) throws SQLException {
        Room initialRoomId;
        Room targetRoomId;
        Direzione direction;
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM RoomConnections;");
        while (rs.next()) {
            initialRoomId = nodes.get(rs.getInt("initial_room_id"));
            targetRoomId = nodes.get(rs.getInt("target_room_id"));
            if (rs.getString("direction").equals("n"))
                direction = Direzione.NORD;
            else if (rs.getString("direction").equals("s"))
                direction = Direzione.SUD;
            else if (rs.getString("direction").equals("e"))
                direction = Direzione.EST;
            else if (rs.getString("direction").equals("o"))
                direction = Direzione.OVEST;
            else if (rs.getString("direction").equals("sopra"))
                direction = Direzione.SOPRA;
            else if (rs.getString("direction").equals("sotto"))
                direction = Direzione.SOTTO;
            else
                direction = null;
            map.collegaStanze(initialRoomId, targetRoomId, direction);
        }
        rs.close();
        stm.close();
        return map;
    }

    // NPCs

    /**
     * Recupera gli NPC presenti in una specifica stanza.
     *
     * @param roomID l'ID della stanza.
     * @return una lista di oggetti NPC.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    private List<NPC> askForNPCs(int roomID) throws SQLException {
        List<NPC> NPCs = new ArrayList<>();
        PreparedStatement pstm = connection.prepareStatement(
                "SELECT * " +
                        "FROM NonPlayerCharacters " +
                        "WHERE room_id=?;");
        pstm.setInt(1, roomID);
        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            NPCs.add(generateNPC(rs));
        }

        rs.close();
        pstm.close();
        return NPCs;
    }

    /**
     * Genera un oggetto NPC a partire dai dati di un ResultSet.
     *
     * @param rsNPC il ResultSet contenente i dati dell'NPC.
     * @return un nuovo oggetto NPC.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    private NPC generateNPC(ResultSet rsNPC) throws SQLException {
        return new NPC(
                rsNPC.getInt("id"),
                rsNPC.getString("name"),
                askForDialogues(rsNPC.getInt("id"))
        );
    }

    /**
     * Recupera i dialoghi associati a un NPC.
     *
     * @param npcID l'ID dell'NPC.
     * @return una lista di oggetti Dialogo.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    private List<Dialogo> askForDialogues(int npcID) throws SQLException {
        List<Dialogo> dialogues = new ArrayList<>();
        PreparedStatement pstm = connection.prepareStatement(
                "SELECT d.id as d_id, ds.id AS ds_id, ds.dialogue_id AS ds_dialogue_id, " +
                        "ds.dialog_statement AS ds_dialog_statement " +
                        "FROM DialoguesStatements AS ds " +
                        "INNER JOIN Dialogues AS d " +
                        "ON d.id=ds.dialogue_id " +
                        "WHERE d.NPC=? " +
                        "ORDER BY ds.id;");
        pstm.setInt(1, npcID);
        ResultSet rsDialoghi = pstm.executeQuery();

        Map<Integer, String> nodes = new HashMap<>();
        int di = -1;
        Dialogo dialogue = null;
        boolean firstNode = true;
        while (rsDialoghi.next()) {
            if (di == -1 || di != rsDialoghi.getInt("d_id")) {
                if (di != -1) dialogues.add(generateDialogue(dialogue, nodes));
                di = rsDialoghi.getInt("d_id");
                dialogue = new Dialogo(di);
                firstNode = true;
            }
            nodes.put(rsDialoghi.getInt("ds_id"), rsDialoghi.getString("ds_dialog_statement"));
            dialogue.addDialogo(rsDialoghi.getString("ds_dialog_statement"), firstNode);
            firstNode = false;
        }
        if (di != -1) dialogues.add(generateDialogue(dialogue, nodes));
        rsDialoghi.close();
        pstm.close();
        return dialogues;
    }

    /**
     * Popola un oggetto Dialogo con le possibili risposte e collegamenti.
     *
     * @param dialogue il dialogo da completare.
     * @param nodes    una mappa di frasi del dialogo indicizzate per ID.
     * @return l'oggetto Dialogo completo.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    private Dialogo generateDialogue(Dialogo dialogue, Map<Integer, String> nodes) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM DialoguesPossibleAnswers WHERE dialogue_id=?;");
        pstm.setInt(1, dialogue.getId());
        ResultSet rsArchi = pstm.executeQuery();

        while (rsArchi.next()) {
            dialogue.addRisposta(nodes.get(rsArchi.getInt("first_statement")),
                    nodes.get(rsArchi.getInt("related_answer_statement")),
                    rsArchi.getString("answer"));
        }

        rsArchi.close();
        pstm.close();
        return dialogue;
    }


    // Items

    /**
     * Recupera gli oggetti presenti in una stanza specifica, con le loro quantità.
     *
     * @param roomID l'ID della stanza.
     * @return una mappa di GeneralItem e le loro quantità.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    private Map<GeneralItem, Integer> askForInRoomItems(int roomID) throws SQLException {
        Map<GeneralItem, Integer> items = new HashMap<>();
        PreparedStatement pstm = connection.prepareStatement("SELECT " +
                "    iro.room_id        AS iro_room_id, " +
                "    iro.object_id      AS iro_object_id, " +
                "    iro.quantity       AS iro_quantity, " +
                "    i.id               AS i_id, " +
                "    i.name             AS i_name, " +
                "    i.description      AS i_description, " +
                "    i.is_container     AS i_is_container, " +
                "    i.is_readable      AS i_is_readable, " +
                "    i.is_visible       AS i_is_visible, " +
                "    i.is_composable    AS i_is_composable, " +
                "    i.is_pickable      AS i_is_pickable, " +
                "    i.uses             AS i_uses, " +
                "    i.image_path       AS i_image_path " +
                "FROM InRoomObjects     AS iro " +
                "INNER JOIN Items AS i ON i.id = iro.object_id " +
                "WHERE iro.room_id = ?;");
        pstm.setInt(1, roomID);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            if (rs.getBoolean("i_is_container")) {
                items.put(generateContainerItem(rs), rs.getInt("iro_quantity"));
            } else {
                items.put(generateItem(rs), rs.getInt("iro_quantity"));
            }
        }
        rs.close();
        pstm.close();
        return items;
    }

    /**
     * Genera un oggetto di tipo Item (o una sua sottoclasse) a partire da un ResultSet.
     *
     * @param rsItem il ResultSet contenente i dati dell'oggetto.
     * @return un'istanza di Item o una delle sue sottoclassi (IteamCombinable, ItemRead).
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    private Item generateItem(ResultSet rsItem) throws SQLException {
        Item i;
        if (rsItem.getBoolean("i_is_composable")) {
            i = generateComposableItem(rsItem);
        } else if (rsItem.getBoolean("i_is_readable")) {
            i = generateReadableItem(rsItem);
        } else {
            i = assembleItem(rsItem);
        }
        //System.out.println(" VALORE visibile " +rsItem.getBoolean("i_is_visible")+ i.getName());
        if(rsItem.getBoolean("i_is_visible")){
            i.setVisibile(rsItem.getBoolean("i_is_visible"));
        }

        //System.out.println(" VALORE PRENDIBILE " +rsItem.getBoolean("i_is_pickable")+ i.getName());
        if(rsItem.getBoolean("i_is_pickable")){
            i.setPickupable(rsItem.getBoolean("i_is_pickable"));
        }
        return i;
    }

    /**
     * Assembla un oggetto Item semplice a partire da un ResultSet.
     *
     * @param rsItem il ResultSet contenente i dati dell'oggetto.
     * @return un nuovo oggetto Item.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    private Item assembleItem(ResultSet rsItem) throws SQLException {
        return new Item(
                rsItem.getInt("i_id"),
                rsItem.getString("i_name"),
                rsItem.getString("i_description"),
                askForItemAlias(rsItem.getInt("i_id")),
                rsItem.getInt("i_uses"),
                rsItem.getString("i_image_path")
        );
    }

    /**
     * Genera un oggetto IteamCombinable a partire da un ResultSet.
     *
     * @param rsItem il ResultSet contenente i dati dell'oggetto.
     * @return un nuovo oggetto IteamCombinable.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    private IteamCombinable generateComposableItem(ResultSet rsItem) throws SQLException {
        return new IteamCombinable(
                rsItem.getInt("i_id"),
                rsItem.getString("i_name"),
                rsItem.getString("i_description"),
                askForItemAlias(rsItem.getInt("i_id")),
                rsItem.getInt("i_uses"),
                rsItem.getString("i_image_path"),
                askForComponentsOf(rsItem.getInt("i_id"))
        );
    }

    /**
     * Recupera i componenti di un oggetto combinabile.
     *
     * @param composableItemID l'ID dell'oggetto combinabile.
     * @return una lista di Item che compongono l'oggetto.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    private List<Item> askForComponentsOf(int composableItemID) throws SQLException {
        List<Item> list = new ArrayList<>();
        PreparedStatement pstm = connection.prepareStatement(
                "SELECT  c.composing_item_id AS iro_object_id, " +
                        "    i.id                AS i_id, " +
                        "    i.name              AS i_name, " +
                        "    i.description       AS i_description, " +
                        "    i.is_container      AS i_is_container, " +
                        "    i.is_readable       AS i_is_readable, " +
                        "    i.is_visible        AS i_is_visible, " +
                        "    i.is_composable     AS i_is_composable, " +
                        "    i.is_pickable       AS i_is_pickable, " +
                        "    i.uses              AS i_uses, " +
                        "    i.image_path        AS i_image_path " +
                        "FROM ComposedOf AS c " +
                        "INNER JOIN Items AS i " +
                        "ON c.composing_item_id=i.id " +
                        "WHERE c.composed_item_id=?;");
        pstm.setInt(1, composableItemID);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            list.add(generateItem(rs));
        }
        rs.close();
        pstm.close();
        return list;
    }

    /**
     * Genera un oggetto ItemRead a partire da un ResultSet.
     *
     * @param rsItem il ResultSet contenente i dati dell'oggetto.
     * @return un nuovo oggetto ItemRead.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    private ItemRead generateReadableItem(ResultSet rsItem) throws SQLException {
        return new ItemRead(
                rsItem.getInt("i_id"),
                rsItem.getString("i_name"),
                rsItem.getString("i_description"),
                askForItemAlias(rsItem.getInt("i_id")),
                rsItem.getInt("i_uses"),
                rsItem.getString("i_image_path"),
                askForReadableContent(rsItem.getInt("i_id"))
        );
    }

    /**
     * Recupera il contenuto testuale di un oggetto leggibile.
     *
     * @param readableItemID l'ID dell'oggetto leggibile.
     * @return il contenuto testuale come stringa.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    private String askForReadableContent(int readableItemID) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM ReadableContent WHERE readable_item_id=?;");
        pstm.setInt(1, readableItemID);
        ResultSet rs = pstm.executeQuery();
        String content = "Scemo chi legge, marameo. Prrrr...";
        if (rs.next()) {
            content = rs.getString("content");
        }
        rs.close();
        pstm.close();
        return content;
    }

    /**
     * Genera un oggetto ItemContainer a partire da un ResultSet.
     *
     * @param rsContainer il ResultSet contenente i dati del contenitore.
     * @return un nuovo oggetto ItemContainer.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
   private ItemContainer generateContainerItem(ResultSet rsContainer) throws SQLException {
    ItemContainer container = new ItemContainer(
        rsContainer.getInt("i_id"),
        rsContainer.getString("i_name"),
        rsContainer.getString("i_description"),
        askForItemAlias(rsContainer.getInt("i_id")),
        rsContainer.getString("i_image_path"),
        askForContainedItems(rsContainer.getInt("i_id")),
        false // oppure metti un valore corretto per 'isOpen' se viene dal DB
    );
    //System.out.println("VALORE visibile: " + rsContainer.getBoolean("i_is_visible") + " " + container.getName());
    container.setVisibile(rsContainer.getBoolean("i_is_visible"));
    //System.out.println("VALORE prendibile: " + rsContainer.getBoolean("i_is_pickable") + " " + container.getName());
    container.setPickupable(rsContainer.getBoolean("i_is_pickable"));
    return container;
}


    /**
     * Recupera gli oggetti contenuti all'interno di un contenitore.
     *
     * @param containerID l'ID del contenitore.
     * @return una mappa di GeneralItem e le loro quantità.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    private Map<GeneralItem, Integer> askForContainedItems(int containerID) throws SQLException {
        Map<GeneralItem, Integer> containedItems = new HashMap<>();
        PreparedStatement pstm = connection.prepareStatement("SELECT " +
                "    cc.container_id    AS cc_container_id, " +
                "    cc.content_id      AS cc_content_id, " +
                "    cc.quantity        AS cc_quantity, " +
                "    i.id               AS i_id, " +
                "    i.name             AS i_name, " +
                "    i.description      AS i_description, " +
                "    i.is_container     AS i_is_container, " +
                "    i.is_readable      AS i_is_readable, " +
                "    i.is_visible       AS i_is_visible, " +
                "    i.is_composable    AS i_is_composable, " +
                "    i.is_pickable      AS i_is_pickable, " +
                "    i.uses             AS i_uses, " +
                "    i.image_path       AS i_image_path " +
                "FROM ContainerContents AS cc " +
                "INNER JOIN Items AS i ON i.id = cc.content_id " +
                "WHERE cc.container_id = ?;");
        pstm.setInt(1, containerID);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            containedItems.put(generateItem(rs), rs.getInt("cc_quantity"));
        }
        rs.close();
        pstm.close();
        return containedItems;
    }

    /**
     * Recupera gli alias di un oggetto specifico dal database.
     *
     * @param itemID l'ID dell'oggetto.
     * @return un insieme di stringhe contenente gli alias dell'oggetto.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    private Set<String> askForItemAlias(int itemID) throws SQLException {
        Set<String> alias = new HashSet<>();
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM ItemAlias WHERE id = ?");
        pstm.setInt(1, itemID);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            alias.add(rs.getString("item_alias"));
        }
        rs.close();
        pstm.close();
        return alias;
    }

    // Room's look refresh

    /**
     * Recupera la nuova descrizione 'look' di una stanza a seguito di un evento.
     *
     * @param eventID l'ID dell'evento che ha modificato la stanza.
     * @return la nuova stringa 'look' per la stanza, o null se non trovata.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    public String askForNewRoomLook(int eventID) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Evento where id=?");
        pstm.setInt(1, eventID);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            return rs.getString("updated_room_look");
        }
        rs.close();
        pstm.close();
        return null;
    }

    /**
     * Recupera un singolo oggetto (GeneralItem) dal database usando il suo ID.
     *
     * @param itemID l'ID dell'oggetto da recuperare.
     * @return l'oggetto GeneralItem corrispondente, o null se non trovato.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    public GeneralItem askForItem(int itemID) throws SQLException {
        GeneralItem item = null;
        PreparedStatement pstm = connection.prepareStatement("SELECT " +
                "    i.id               AS i_id, " +
                "    i.name             AS i_name, " +
                "    i.description      AS i_description, " +
                "    i.is_container     AS i_is_container, " +
                "    i.is_readable      AS i_is_readable, " +
                "    i.is_visible       AS i_is_visible, " +
                "    i.is_composable    AS i_is_composable, " +
                "    i.is_pickable      AS i_is_pickable, " +
                "    i.uses             AS i_uses, " +
                "    i.image_path       AS i_image_path " +
                "FROM Items AS i " +
                "WHERE i.id = ?;");
        pstm.setInt(1, itemID);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            if (rs.getBoolean("i_is_container")) {
                item = generateContainerItem(rs);
            } else {
                item = generateItem(rs);
            }
        }
        rs.close();
        pstm.close();
        return item;
    }

    /**
     * Recupera tutti gli oggetti presenti nel database.
     *
     * @return una lista di tutti gli oggetti GeneralItem.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    public List<GeneralItem> askForItems() throws SQLException {
        List<GeneralItem> items = new ArrayList<>();
        PreparedStatement pstm = connection.prepareStatement("SELECT " +
                "    i.id               AS i_id, " +
                "    i.name             AS i_name, " +
                "    i.description      AS i_description, " +
                "    i.is_container     AS i_is_container, " +
                "    i.is_readable      AS i_is_readable, " +
                "    i.is_visible       AS i_is_visible, " +
                "    i.is_composable    AS i_is_composable, " +
                "    i.is_pickable      AS i_is_pickable, " +
                "    i.uses             AS i_uses, " +
                "    i.image_path       AS i_image_path " +
                "FROM Items AS i;");
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            if (rs.getBoolean("i_is_container")) {
                items.add(generateContainerItem(rs));
            } else {
                items.add(generateItem(rs));
            }
        }
        rs.close();
        pstm.close();
        return items;
    }

    /**
     * Recupera tutti gli NPC (Non-Player Characters) dal database.
     *
     * @return una lista di tutti gli NPC.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    public List<NPC> askForNonPlayerCharacters() throws SQLException{
        List<NPC> NPCs = new ArrayList<>();
        PreparedStatement pstm = connection.prepareStatement(
                "SELECT * " +
                        "FROM NonPlayerCharacters;");
        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            NPCs.add(generateNPC(rs));
        }

        rs.close();
        pstm.close();

        return NPCs;
    }
}