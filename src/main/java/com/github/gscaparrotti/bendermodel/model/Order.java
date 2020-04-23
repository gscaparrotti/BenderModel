package com.github.gscaparrotti.bendermodel.model;

import java.io.Serializable;

/**
 * Created by gscap_000 on 25/04/2016.
 */
public class Order implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5209160795122798772L;
    private final int table;
    private final IDish dish;
    private final Pair<Integer, Integer> amounts;

    /**
     * Creates a new Order. An Order is identified by the table which requested
     * a certain dish, an ordered dish and a couple of integer values,
     * representing how many dishes have been ordered and how many dishes have
     * been already served.
     * 
     * @param table
     *            the table who made this order
     * @param dish
     *            the desired dish
     * @param amounts
     *            the quantity of the desired dish and the already served
     *            quantity
     */
    public Order(final int table, final IDish dish, final Pair<Integer, Integer> amounts) {
        this.table = table;
        this.dish = dish;
        this.amounts = amounts;
    }

    /**
     * @return the table number
     */
    public int getTable() {
        return table;
    }

    /**
     * @return the wanted dish
     */
    public IDish getDish() {
        return dish;
    }

    /**
     * @return the quantity of the desired dish and the already served quantity
     */
    public Pair<Integer, Integer> getAmounts() {
        return amounts;
    }
}
