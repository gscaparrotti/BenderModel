package test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import model.*;

public class ModelTest {

    @Test
    public void test() {
        final IRestaurant r = new Restaurant();
        for (int i = 0; i < 3; i++) {
            assertSame(r.addTable(), i+1);
        }
        try {
            r.addOrder(0, new Dish("piatto1", 1), 1);
            fail();
        } catch (final IllegalArgumentException e) { } //NOPMD
        r.addOrder(1, new Dish("piatto1", 1), 1);
        assertEquals(1, r.getOrders(1).size());
        assertEquals(new Dish("piatto1", 1), r.getOrders(1).keySet().toArray()[0]);
    }
    
    @Test
    public void testConcorrenza1() {
        final IRestaurant r = new Restaurant();
        final ExecutorService e = Executors.newFixedThreadPool(30);
        final Set<Callable<Void>> s1 = new HashSet<Callable<Void>>();
        for (int i = 0; i < 30; i++) {
            s1.add(new Callable<Void>() {           
                @Override
                public Void call() {
                    r.addTable();      
                    return null;
                }
            });
        }
        try {
            final List<Future<Void>> l1 = e.invokeAll(s1);
            for (final Future<Void> f : l1) {
                assertTrue(f.isDone());
            }
            assertSame(r.getTablesAmount(), 30);
        } catch (final InterruptedException e2) {
            fail();
        }
        final Set<Callable<Void>> s = new HashSet<Callable<Void>>();
        for (int i = 0; i < 30; i++) {
            final int t = i+1;
            s.add(new Callable<Void>() {           
                @Override
                public Void call() {
                    r.addOrder(t, new Dish("piatto" + t, 1), 1);      
                    return null;
                }
            });
        }
        try {
            final List<Future<Void>> l = e.invokeAll(s);
            for (final Future<Void> f : l) {
                assertTrue(f.isDone());
            }
            for (int i = 0; i < 30; i++) {
                assertTrue(r.getOrders(i+1).containsKey(new Dish("piatto" + (i+1), 1)));
            }
        } catch (final InterruptedException e1) {
            fail();
        }
     }

}
