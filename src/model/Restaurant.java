package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

/**
 * A concrete implementation of {@link IRestaurant}. This implementation is
 * thread-safe, so all its method can be called concurrently by many threads.
 * Please note that, while adding an element (a table, a order, etc...) will
 * work as expected, the methods which retrieve elements, such as
 * {@link #getOrders(int table)}, although thread-safe, return objects which
 * <b>are not</b>. Referring to getOrders, if you want to iterate over its
 * collections views (or generically get an entry) you should synchronize over
 * the entire Resturant object:
 * 
 * <pre>
* {@code
* IRestaurant r = new Restaurant();
* ...
* synchronized(r) {
*       Iterator i = r.getOrders(tableNumber).entrySet().iterator();
*       while(i.hasNext()) {
*               //do something with the entry returned by i.next()
*       }
* }
* }
 * </pre>
 * 
 * If you don't do so, the iterator might throw a
 * {@link java.util.ConcurrentModificationException}. The exception is thrown
 * following a fail-fast logic, so the iterator should fail as soon as a
 * concurrent modification is detected, but this is done also following a best
 * effort logic, so there is no guarantee that this will actually happen. That
 * means that you should not rely on it, but it should only a way to prevent
 * damages to the data stored in a Resturant object as a consequence of, for
 * instance, a bug or a programming error.
 *
 */
public class Restaurant implements IRestaurant {

    /**
     * 
     */
    private static final long serialVersionUID = 6813103235280390095L;
    private final Map<Integer, Map<IDish, Pair<Integer, Integer>>> tables = new HashMap<Integer, Map<IDish, Pair<Integer, Integer>>>();
    private final Map<Integer, String> names = new HashMap<Integer, String>();
    private int tablesAmount;
    private static final String ERROR_MESSAGE = "Dati inseriti non corretti. Controllare.";

    @Override
    public synchronized int addTable() {
        this.tablesAmount++;
        return this.tablesAmount;
    }

    @Override
    public synchronized int removeTable() {
        if (tablesAmount > 0 && !tables.containsKey(tablesAmount)) {
            setTableName(tablesAmount, null);
            tablesAmount--;
            return tablesAmount;
        } else {
            throw new IllegalStateException("Il tavolo ha ancora piatti da servire");
        }
    }

    @Override
    public synchronized void addOrder(final int table, final IDish item, final int quantity) {
        Preconditions.checkNotNull(item);
        if (!checkIfCorrect(table, item, quantity)) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        final Map<IDish, Pair<Integer, Integer>> temp;
        if (tables.containsKey(table)) {
            temp = tables.get(table);
        } else {
            temp = new HashMap<IDish, Pair<Integer, Integer>>();
        }
        if (temp.containsKey(item)) {
            temp.get(item).setX(temp.get(item).getX() + quantity);
        } else {
            temp.put(item, new Pair<Integer, Integer>(quantity, 0));
        }
        tables.put(table, temp);
    }

    @Override
    public synchronized void removeOrder(final int table, final IDish item, final int quantity) {
        Preconditions.checkNotNull(item);
        if (!checkIfCorrect(table, item, quantity) || !tables.containsKey(table) || !tables.get(table).containsKey(item)
                || tables.get(table).get(item).getX() - quantity < 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        if (tables.get(table).get(item).getX() - quantity == 0) {
            tables.get(table).remove(item);
        } else {
            tables.get(table).get(item).setX(tables.get(table).get(item).getX() - quantity);
            if (tables.get(table).get(item).getY() > tables.get(table).get(item).getX()) {
                setOrderAsProcessed(table, item);
            }
        }
        if (tables.get(table).isEmpty()) {
            resetTable(table);
        }
    }

    @Override
    public synchronized void setOrderAsProcessed(final int table, final IDish item) {
        Preconditions.checkNotNull(item);
        if (!checkIfCorrect(table, item, 1) || !tables.containsKey(table) || tables.get(table).get(item) == null) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        tables.get(table).get(item).setY(tables.get(table).get(item).getX());
    }

    @Override
    public synchronized void resetTable(final int table) {
        if (table <= 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        tables.remove(table);
        names.remove(table);
    }

    /*
     * public double getBill(int table){ if(table<=0 ||
     * !tables.containsKey(table)) { return 0.0; } else { double total = 0;
     * Map<IDish, Pair<Integer, Integer>> m = tables.get(table); for(IDish i :
     * m.keySet()) { total += i.getPrice() * m.get(i).getX(); } return total; }
     * }
     */

    @Override
    public synchronized int getTablesAmount() {
        return this.tablesAmount;
    }

    @Override
    public synchronized  Map<IDish, Pair<Integer, Integer>> getOrders(final int tableNumber) {
        if (tableNumber > 0 && tables.containsKey(tableNumber)) {
            final Map<IDish, Pair<Integer, Integer>> result = new HashMap<IDish, Pair<Integer,Integer>>();
            for (final Entry<IDish, Pair<Integer, Integer>> table : tables.get(tableNumber).entrySet()) {
                final IDish newDish = table.getKey().clone();
                final Pair<Integer, Integer> newPair = new Pair<Integer, Integer>(table.getValue().getX(), table.getValue().getY());
                result.put(newDish, newPair);
            }
            return result;
        } else {
            return new HashMap<IDish, Pair<Integer, Integer>>();
        }
    }

    private synchronized boolean checkIfCorrect(final int table, final IDish item, final int quantity) {
        if (table <= 0 || table > tablesAmount || item == null || quantity <= 0) {
            return false;
        }
        if (item.getName().length() <= 0 || item.getPrice() <= 0) { // NOPMD
            return false;
        }
        return true;
    }

    @Override
    public synchronized void setTableName(final int tableNumber, final String name) {
        if (tableNumber > 0 && tableNumber <= getTablesAmount()) {
            if (name == null) {
                names.remove(tableNumber);
            } else {
                names.put(tableNumber, name);
            }
        }
    }

    @Override
    public synchronized String getTableName(final int tableNumber) {
        return names.containsKey(tableNumber) ? names.get(tableNumber) : "";
    }

    @Override
    public synchronized Map<Integer, String> getAllNames() {
        return names != null ? ImmutableMap.copyOf(names) : new HashMap<Integer, String>();
    }

}
