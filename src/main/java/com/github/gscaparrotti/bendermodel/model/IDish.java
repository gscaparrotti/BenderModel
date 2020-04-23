package com.github.gscaparrotti.bendermodel.model;

import java.io.Serializable;

/**
 * 
 *         An interface which models the basic method for managing a dish.
 *
 */
public interface IDish extends Serializable {

    /**
     * @return The name of this dish.
     */
    String getName();

    /**
     * @return The price of this dish.
     */
    double getPrice();
    
    /**
     * @return the value of the filter for this dish
     */
    int getFilterValue();

}
