package model;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 */
public interface IRestaurant extends Serializable {

    /**
     * @return the new amount of tables
     * 
     *         Adds a new table
     */
    int addTable();

    /**
     * @return the new amount of tables
     * 
     *         Removes the last added table
     */
    int removeTable();

    /**
     * @param table
     *            The table whose orders you want to update
     * @param item
     *            the {@link IDish} you want to add
     * @param quantity
     *            to amount of item to add
     * 
     *            Adds a new order to the provived table
     */

    void addOrder(int table, IDish item, int quantity);

    /**
     * @param table
     *            The table whose orders you want to update
     * @param item
     *            the {@link IDish} you want to remove
     * @param quantity
     *            to amount of item to remove
     * 
     *            Removes a order from the provived table
     */
    void removeOrder(int table, IDish item, int quantity);

    /**
     * @param table
     *            The table whose orders you want to update
     * @param item
     *            The item you want to update
     * 
     *            Set the provided order as processed. More formally, this
     *            method sets the number of processed items to the value of the
     *            ordered items. You cannot specify the amount of processed
     *            items.
     */
    void setOrderAsProcessed(int table, IDish item);

    /**
     * @param table
     *            the table of whih you want the orders
     * @return a map representing all the orders
     * 
     *         Provides all the orders of the selected table
     */
    Map<IDish, Pair<Integer, Integer>> getOrders(int table);

    /**
     * @param table
     *            The table whose orders you want to update
     * 
     *            Deletes all the orders for the selected table
     */
    void resetTable(int table);

    /**
     * @return the number of present tables
     */
    int getTablesAmount();
    
    /**
     * @param tableNumber the number of the table
     * @param name the name you want to give to the selected table
     */
    void setTableName(int tableNumber, String name);
    
    /**
     * @param tableNumber the number of the table whose name you want to know
     */
    String getTableName(int tableNumber);
    
    Map<Integer, String> getAllNames();
}
