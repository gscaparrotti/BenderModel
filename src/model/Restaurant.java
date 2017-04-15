package model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    private transient Lock mapLock = new ReentrantLock(true);
    private int tablesAmount;
    private static final String ERROR_MESSAGE = "Dati inseriti non corretti. Controllare.";
    
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException { //NOPMD
        in.defaultReadObject();
        tablesAmountLock = new FastReadWriteLock();
        mapLock = new ReentrantLock(true);
    }

    @Override
    public int addTable() {
        tablesAmountLock.write();
        this.tablesAmount++;
        tablesAmountLock.release();
        return this.tablesAmount;
    }

    @Override
    public int removeTable() {
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
    public void addOrder(final int table, final IDish item, final int quantity) {
        Preconditions.checkNotNull(item);
        if (!checkIfCorrect(table, item, quantity)) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        final Map<IDish, Pair<Integer, Integer>> temp;
        mapLock.lock();
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
        mapLock.unlock();
    }

    @Override
    public void removeOrder(final int table, final IDish item, final int quantity) {
        Preconditions.checkNotNull(item);
        mapLock.lock();
        if (!checkIfCorrect(table, item, quantity) || !tables.containsKey(table) || !tables.get(table).containsKey(item)
                || tables.get(table).get(item).getX() - quantity < 0) {
            mapLock.unlock();
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
        mapLock.unlock();
    }

    @Override
    public void setOrderAsProcessed(final int table, final IDish item) {
        Preconditions.checkNotNull(item);
        mapLock.lock();
        if (!checkIfCorrect(table, item, 1) || !tables.containsKey(table) || tables.get(table).get(item) == null) {
            mapLock.unlock();
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        tables.get(table).get(item).setY(tables.get(table).get(item).getX());
        mapLock.unlock();
    }

    @Override
    public void resetTable(final int table) {
        if (table <= 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        mapLock.lock();
        tables.remove(table);
        mapLock.unlock();
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
    public Map<IDish, Pair<Integer, Integer>> getOrders(final int table) {
        mapLock.lock();
        if (table > 0 && tables.containsKey(table)) {
            final Map<IDish, Pair<Integer, Integer>> temp = tables.get(table);
            mapLock.unlock();
            return temp;
        } else {
            mapLock.unlock();
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
