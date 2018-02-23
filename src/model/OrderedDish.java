package model;

import java.util.Date;

/* 
 * Nota: anche se estendere Dish non e' l'opzione migliore
 * per avere degli oggetti che mantengano le informazioni sul momento
 * in cui sono stati creati (un'opzione migliore potrebbe essere creare
 * una classe Ordine, piu' basilare di quella gia' esistente, che contenga
 * come campi un normale Dish e un Date), bisogna considerare che l'implementazione
 * di Bender fa affidamento piu' volte sull'esistenza di questa classe, come conseguenza
 * del fatto che e' ovviamente nata prima questa classe rispetto al codice che ne fa uso,
 * percio', anche se chiaramente sarebbe possibile modificare tutto per rendere il codice
 * piu' "corretto", questo comporterebbe cambiamenti importanti, 
 * come del resto qualsiasi modifica alle interfacce (getOrders non dovrebbe
 * piu' ritornare dei semplici Dish ma degli oggetti della nuova classe che andrebbe 
 * creata) che al momento potrebbe non valere la pena sostenere. 
 */

/**
 * A IDish which has been ordered by a customer. 
 * Generically speaking, an ordered dish is a dish which 
 * also keeps information about the fact that it has been 
 * ordered by a certain customer. 
 * In this specific case, an OrderedDish keeps informations about 
 * the moment it has been ordered at.
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
