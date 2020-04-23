package com.github.gscaparrotti.bendermodel.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

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
    private static final long serialVersionUID = -1893050222250754776L;
    private final Map<String, Map<IDish, Pair<Integer, Integer>>> tables = new HashMap<>();
    private final Map<Integer, String> names = new HashMap<>();
    private int tablesAmount;

    @Override
    public synchronized int addTable() {
        this.tablesAmount++;
        return this.tablesAmount;
    }

    @Override
    public synchronized int removeTable() {
        if (tablesAmount > 0) {
            resetTable(tablesAmount);
            setTableName(tablesAmount, null);
            tablesAmount--;
        }
        return tablesAmount;
    }
    
    @Override
    public synchronized int getTablesAmount() {
        return this.tablesAmount;
    }

    @Override
    public synchronized void addOrder(final int table, final IDish item, final int quantity) {
        Preconditions.checkNotNull(item);
        if (!checkIfCorrect(table, item, quantity)) {
            return;
        }
        final Map<IDish, Pair<Integer, Integer>> temp;
        if (tables.containsKey(table + names.getOrDefault(table, ""))) {
            temp = tables.get(table + names.getOrDefault(table, ""));
        } else {
            temp = new HashMap<>();
        }
        if (temp.containsKey(item)) {
            temp.get(item).setX(temp.get(item).getX() + quantity);
        } else {
            temp.put(item, new Pair<>(quantity, 0));
        }
        tables.put(table + names.getOrDefault(table, ""), temp);
    }

    @Override
    public synchronized void removeOrder(final int table, final IDish item, final int quantity) {
        Preconditions.checkNotNull(item);
        if (!checkIfCorrect(table, item, quantity) || !tables.containsKey(table + names.getOrDefault(table, "")) || !tables.get(table + names.getOrDefault(table, "")).containsKey(item)
                || tables.get(table + names.getOrDefault(table, "")).get(item).getX() - quantity < 0) {
            return;
        }
        if (tables.get(table + names.getOrDefault(table, "")).get(item).getX() - quantity == 0) {
            tables.get(table + names.getOrDefault(table, "")).remove(item);
        } else {
            tables.get(table + names.getOrDefault(table, "")).get(item).setX(tables.get(table + names.getOrDefault(table, "")).get(item).getX() - quantity);
            if (tables.get(table + names.getOrDefault(table, "")).get(item).getY() > tables.get(table + names.getOrDefault(table, "")).get(item).getX()) {
                setOrderAsProcessed(table, item);
            }
        }
        if (tables.get(table + names.getOrDefault(table, "")).isEmpty()) {
            resetTable(table);
        }
    }

    @Override
    public synchronized void setOrderAsProcessed(final int table, final IDish item) {
        Preconditions.checkNotNull(item);
        if (!checkIfCorrect(table, item, 1) || !tables.containsKey(table + names.getOrDefault(table, "")) || tables.get(table + names.getOrDefault(table, "")).get(item) == null) {
            return;
        }
        tables.get(table + names.getOrDefault(table, "")).get(item).setY(tables.get(table + names.getOrDefault(table, "")).get(item).getX());
    }

    @Override
    public synchronized void resetTable(final int table) {
        if (table <= 0) {
            return;
        }
        tables.remove(table + names.getOrDefault(table, ""));
        names.remove(table);
    }

    @Override
    public synchronized  Map<IDish, Pair<Integer, Integer>> getOrders(final int table) {
        if (table > 0) {
            if (names.containsKey(table)) {
                if (tables.containsKey(table + names.getOrDefault(table, ""))) {
                    return tables.get(table + names.getOrDefault(table, ""));
                } else {
                    return new HashMap<>();
                }
            } else {
                final Map<IDish, Pair<Integer, Integer>> result = new HashMap<>();
                tables.entrySet()
                        .stream()
                        .filter(entry -> entry.getKey().startsWith(Integer.toString(table)))
                        .map(Map.Entry::getValue)
                        .forEach(map -> map.forEach((key, value) -> {
                            if (result.containsKey(key)) {
                                result.get(key).setX(result.get(key).getX() + value.getX());
                                result.get(key).setY(result.get(key).getY() + value.getY());
                            } else {
                                result.put(key, new Pair<>(value.getX(), value.getY()));
                            }
                        }));
                return result;
            }
        } else {
            return new HashMap<>();
        }
    }

    @Override
    public synchronized void setTableName(final int tableNumber, final String name) {
        if (tableNumber > 0 && tableNumber <= getTablesAmount()) {
            if (name == null || name.length() == 0) {
                names.remove(tableNumber);
            } else {
                names.put(tableNumber, name);
            }
        }
    }

    @Override
    public synchronized String getTableName(final int tableNumber) {
        return names.getOrDefault(tableNumber, "");
    }

    @Override
    public synchronized Map<Integer, String> getAllNames() {
        return ImmutableMap.copyOf(names);
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

}
