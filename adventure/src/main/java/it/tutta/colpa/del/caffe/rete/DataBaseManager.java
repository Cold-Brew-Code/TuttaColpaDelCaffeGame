package it.tutta.colpa.del.caffe.rete;

import it.tutta.colpa.del.caffe.game.entity.*;
import it.tutta.colpa.del.caffe.game.utility.Direzione;

import java.sql.*;
import java.util.*;

/**
 * Classe che gestisce il database.
 * Contiene tutte le query necessarie per interrogare le relazioni e fornire
 * una risposta corretta alle richieste ricevute dal server.
 *
 * @author giovav
 */
public class DataBaseManager {
    private Connection connection;
    private final String dataBasePath = "jdbc:h2:./database;INIT=RUNSCRIPT FROM 'classpath:inizioDB.sql'";
    private final String username = "cacca";
    private final String password = "12345";

    /**
     * @throws SQLException
     */
    private void establishConnection() throws SQLException {
        Properties dbProperties = new Properties();
        dbProperties.setProperty("user", username);
        dbProperties.setProperty("pw", password);
        connection = DriverManager.getConnection(dataBasePath,
                dbProperties);
    }

    /**
     * @throws SQLException
     */
    public DataBaseManager() throws SQLException {
        establishConnection();
    }

    /**
     * @throws SQLException
     */
    public void closeConnection() throws SQLException {
        connection.close();
        connection = null;
    }

    // Commands

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
     * @param commandID
     * @return
     * @throws SQLException
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
     * @return
     * @throws SQLException
     */
    public GameMap askForGameMap() throws SQLException {
        GameMap gameMap = new GameMap();
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM Rooms;");
        Map<Integer, Room> nodes = new HashMap<>();
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
     * @param room
     * @return
     * @throws SQLException
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
     * @param map
     * @param nodes
     * @return
     * @throws SQLException
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
     * @param roomID
     * @return
     * @throws SQLException
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
     * @param rsNPC
     * @return
     * @throws SQLException
     */
    private NPC generateNPC(ResultSet rsNPC) throws SQLException {
        return new NPC(
                rsNPC.getInt("id"),
                rsNPC.getString("name"),
                askForDialogues(rsNPC.getInt("id"))
        );
    }

    /**
     * @param npcID
     * @return
     * @throws SQLException
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
     * @param dialogue
     * @param nodes
     * @return
     * @throws SQLException
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
     * @param roomID
     * @return
     * @throws SQLException
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
     * @param rsItem
     * @return
     * @throws SQLException
     */
    private Item generateItem(ResultSet rsItem) throws SQLException {
        Item i;
        if (rsItem.getBoolean("i_is_composable")) {
            i = (Item) generateComposableItem(rsItem);
        } else if (rsItem.getBoolean("i_is_readable")) {
            i = (Item) generateReadableItem(rsItem);
        } else {
            i = assembleItem(rsItem);
        }
        return i;
    }

    /**
     * @param rsItem
     * @return
     * @throws SQLException
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
     * @param rsItem
     * @return
     * @throws SQLException
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
     * @param composableItemID
     * @return
     * @throws SQLException
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
     * @param rsItem
     * @return
     * @throws SQLException
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
     * @param readableItemID
     * @return
     * @throws SQLException
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
     * @param rsContainer
     * @return
     * @throws SQLException
     */
    private ItemContainer generateContainerItem(ResultSet rsContainer) throws SQLException {
        return new ItemContainer(
                rsContainer.getInt("i_id"),
                rsContainer.getString("i_name"),
                rsContainer.getString("i_description"),
                askForItemAlias(rsContainer.getInt("i_id")),
                rsContainer.getString("i_image_path"),
                askForContainedItems(rsContainer.getInt("i_id")),
                false
        );
    }

    /**
     * @param containerID
     * @return
     * @throws SQLException
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
     * @param itemID
     * @return
     * @throws SQLException
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
     *
     * @param eventID
     * @return
     * @throws SQLException
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


}
