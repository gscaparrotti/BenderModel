package model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.google.common.base.Preconditions;
import concurrency.FastReadWriteLock;

/**
 *
 */
public class Restaurant implements IRestaurant {

    /**
     * 
     */
    private static final long serialVersionUID = 6813103235280390095L;
    private final Map<Integer, Map<IDish, Pair<Integer, Integer>>> tables = new HashMap<Integer, Map<IDish, Pair<Integer, Integer>>>();
    private transient FastReadWriteLock tablesAmountLock = new FastReadWriteLock();
    private int tablesAmount;
    private static final String ERROR_MESSAGE = "Dati inseriti non corretti. Controllare.";
    
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException { //NOPMD
        in.defaultReadObject();
        tablesAmountLock = new FastReadWriteLock();
    }

    @Override
    public int addTable() {
        tablesAmountLock.write();
        this.tablesAmount++;
        tablesAmountLock.release();
        return this.tablesAmount;
    }

    @Override
    public synchronized int removeTable() {
        tablesAmountLock.write();
        if (tablesAmount > 0 && !tables.containsKey(tablesAmount)) {
            tablesAmount--;
            tablesAmountLock.release();
            return tablesAmount;
        } else {
            tablesAmountLock.release();
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
    }

    /*
     * public double getBill(int table){ if(table<=0 ||
     * !tables.containsKey(table)) { return 0.0; } else { double total = 0;
     * Map<IDish, Pair<Integer, Integer>> m = tables.get(table); for(IDish i :
     * m.keySet()) { total += i.getPrice() * m.get(i).getX(); } return total; }
     * }
     */

    @Override
    public int getTablesAmount() {
        int temp;
        tablesAmountLock.read();
        temp = this.tablesAmount;
        tablesAmountLock.release();
        return temp;
    }

    @Override
    public synchronized  Map<IDish, Pair<Integer, Integer>> getOrders(final int table) {
        if (table > 0 && tables.containsKey(table)) {
            return tables.get(table);
        } else {
            return new HashMap<IDish, Pair<Integer, Integer>>();
        }
    }

    private boolean checkIfCorrect(final int table, final IDish item, final int quantity) {
        tablesAmountLock.read();
        if (table <= 0 || table > tablesAmount || item == null || quantity <= 0) {
            tablesAmountLock.release();
            return false;
        }
        tablesAmountLock.release();
        if (item.getName().length() <= 0 || item.getPrice() <= 0) { // NOPMD
            return false;
        }
        return true;
    }

}
