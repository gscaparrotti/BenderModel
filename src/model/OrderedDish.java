package model;

import java.util.Date;

/**
 * A IDish which keeps infos about the moment when the instance was created.
 *
 */
public class OrderedDish extends Dish {

    private static final long serialVersionUID = 1637868088291903941L;
    private final Date time;
    
    /**
     * Creates a new OrderedDish given a name and a price and keeping a
     * reference about the moment when the object was instantiated.
     * 
     * @param newName
     *            the name of the dish
     * @param newPrice
     *            the price of the dish
     * @param moment
     *            If NOW then this OrderedDish will effectively keep a reference to the 
     *            moment it was istantiated, otherwise it will keep a reference to
     *            to a default moment.
     * @param filter
     *            Must be zero if this dish represents a beverage or a positive
     *            integer otherwise
     */
    
    public OrderedDish(final String newName, final double newPrice, final Moments moment, final int filter) {
        super(newName, newPrice, filter);
        if (moment == Moments.NOW) {
            time = new Date();
        } else {
            time = new Date(0);
        }
    }
    
    /**
     * Creates a new OrderedDish given a name and a price and keeping a
     * reference about the moment when the object was instantiated.
     * 
     * @param newName
     *            the name of the dish
     * @param newPrice
     *            the price of the dish
     * @param filter
     *            Must be zero if this dish represents a beverage or a positive
     *            integer otherwise
     */
    public OrderedDish(final String newName, final double newPrice, final int filter) {
        this(newName, newPrice, Moments.NOW, filter);
    }
    
    /**
     * Creates a new OrderedDish given a name and a price and keeping a
     * reference about the moment when the object was instantiated.
     * 
     * @param dish
     *            an existing dish which will be used to get a name and a price
     * @param moment
     *            If NOW then this OrderedDish will effectively keep a reference to the 
     *            moment it was istantiated, otherwise it will keep a reference to
     *            to a default moment.
     */
    public OrderedDish(final IDish dish, final Moments moment) {
        super(dish.getName(), dish.getPrice(), dish.getFilterValue());
        if (moment == Moments.NOW) {
            time = new Date();
        } else {
            time = new Date(0);
        }
    }

    /**
     * Creates a new OrderedDish given a name and a price and keeping a
     * reference about the moment when the object was instantiated.
     * 
     * @param dish
     *            an existing dish which will be used to get a name and a price
     */
    public OrderedDish(final IDish dish) {
        this(dish.getName(), dish.getPrice(), Moments.NOW, dish.getFilterValue());
    }

    /**
     * Creates a new OrderedDish setting its creation time to a moment provided
     * by another OrderedDish.
     * 
     * @param dish
     *            an existing dish which will be used to get the moment it was
     *            created at. Its name and its price will be ignored.
     * @param name
     *            the name of the new dish
     * @param price
     *            the price of the new dish
     * @param filter
     *            Must be zero if this dish represents a beverage or a positive
     *            integer otherwise
     */
    public OrderedDish(final String name, final double price, final int filter, final OrderedDish dish) {
        super(name, price, filter);
        time = dish.getTime();
    }

    /**
     * @return the moment when this object was created
     */
    public Date getTime() {
        return new Date(time.getTime());
    }
    
    public enum Moments {
        NOW, ZERO;
    }
}
