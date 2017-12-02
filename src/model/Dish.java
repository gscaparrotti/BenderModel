package model;

import com.google.common.base.Preconditions;

/**
 *
 */
public class Dish implements IDish {

    /**
     * 
     */
    private static final long serialVersionUID = 562063817725787601L;
    /**
     * The number of fields that defines a dish.
     */
    public static final int FIELDS = 3;
    private final String name;
    private final double price;
    private final int filter;

    /**
     * Creates a new dish with the given name and price.
     * 
     * @param newName
     *            The name of this dish
     * @param newPrice
     *            The price of this dish
     * @param filter
     *            Must be zero if this dish represents a beverage or a positive
     *            integer otherwise
     */
    public Dish(final String newName, final double newPrice, final int filter) {
        Preconditions.checkNotNull(newName);
        this.name = newName;
        this.price = newPrice;
        this.filter = filter;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double getPrice() {
        return this.price;
    }
    
    @Override
    public int getFilterValue() {
        return filter;
    }

    @Override
    public int hashCode() {
        final int prime = 31; // NOPMD
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        long temp;
        temp = Double.doubleToLongBits(price);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Dish)) {
            return false;
        }
        final Dish other = (Dish) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price);
    }

    @Override
    public String toString() {
        return name;
    }
}
