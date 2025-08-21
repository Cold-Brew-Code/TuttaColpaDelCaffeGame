package it.tutta.colpa.del.caffe.game.exception;

/**
 * Eccezione personalizzata che viene lanciata in caso di errori relativi
 * all'inventario.
 * <p>
 * Estende {@link RuntimeException}, quindi è un'eccezione non controllata.
 * </p>
 *
 * @author giova
 * @since 20/08/25
 */
public class InventoryException extends RuntimeException {

    /**
     * Costruisce una nuova InventoryException con un messaggio descrittivo.
     *
     * @param message il messaggio che descrive l'errore relativo all'inventario
     */
    public InventoryException(String message) {
        super(message);
    }
}
