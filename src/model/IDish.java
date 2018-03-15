package model;

import java.io.Serializable;

/**
 * 
 *         An interface which models the basic method for managing a dish.
 *
 */
public interface IDish extends Serializable, Cloneable {

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
    
    /**
     * @return A deep copy of this IDish
     */
    IDish clone();

}
