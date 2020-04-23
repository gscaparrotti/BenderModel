package com.github.gscaparrotti.bendermodel.model;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 */
public interface IRestaurant extends Serializable {

    /**
     * Adds a new table
     * 
     * @return the new amount of tables
     * 
     */
    int addTable();

    /**
     * Removes the last added table
     * 
     * @return the new amount of tables
     * 
     */
    int removeTable();

    /**
     * Adds a new order to the provived table
     * 
     * @param table
     *            The table whose orders you want to update
     * @param item
     *            the {@link IDish} you want to add
     * @param quantity
     *            to amount of item to add
     * 
     */
    void addOrder(int table, IDish item, int quantity);

    /**
     * Removes a order from the provived table
     * 
     * @param table
     *            The table whose orders you want to update
     * @param item
     *            the {@link IDish} you want to remove
     * @param quantity
     *            to amount of item to remove
     * 
     */
    void removeOrder(int table, IDish item, int quantity);

    /**
     * Set the provided order as processed. More formally, this method sets the
     * number of processed items to the value of the ordered items. You cannot
     * specify the amount of processed items.
     * 
     * @param table
     *            The table whose orders you want to update
     * @param item
     *            The item you want to update
     * 
     */
    void setOrderAsProcessed(int table, IDish item);

    /**
     * Provides all the orders of the selected table
     * 
     * @param table
     *            the table of whih you want the orders
     * @return a map representing all the orders
     * 
     */
    Map<IDish, Pair<Integer, Integer>> getOrders(int table);

    /**
     * Deletes all the orders for the selected table
     * 
     * @param table
     *            The table whose orders you want to update
     * 
     */
    void resetTable(int table);

    /**
     * @return the number of present tables
     */
    int getTablesAmount();
    
    /**
     * Gives a certain table a specific name which can be used as an expressive
     * way to identify a table
     * 
     * @param tableNumber
     *            the number of the table
     * @param name
     *            the name you want to give to the selected table
     */
    void setTableName(int tableNumber, String name);
    
    /**
     * @param tableNumber
     *            the number of the table whose name you want to know
     * @return the table's name
     */
    String getTableName(int tableNumber);
    
    /**
     * @return A map containing every association between a table and a name
     */
    Map<Integer, String> getAllNames();
}
