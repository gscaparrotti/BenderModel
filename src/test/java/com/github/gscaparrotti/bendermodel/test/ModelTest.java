package com.github.gscaparrotti.bendermodel.test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.github.gscaparrotti.bendermodel.model.Dish;
import com.github.gscaparrotti.bendermodel.model.IRestaurant;
import com.github.gscaparrotti.bendermodel.model.Restaurant;
import org.junit.Test;

public class ModelTest {

    @Test
    public void test() {
        final IRestaurant r = new Restaurant();
        for (int i = 0; i < 3; i++) {
            assertEquals(r.addTable(), i + 1);
        }
        r.addOrder(0, new Dish("piatto1", 1, 0), 1);
        assertEquals(0, r.getOrders(0).size());
        r.addOrder(1, new Dish("piatto1", 1, 0), 1);
        assertEquals(1, r.getOrders(1).size());
        assertEquals(new Dish("piatto1", 1, 0), r.getOrders(1).keySet().toArray()[0]);
    }
    
    @Test
    public void concurrencyTest() {
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
                try {
                    f.get();
                } catch (ExecutionException e1) {
                    fail();
                }
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
                    r.addOrder(t, new Dish("piatto" + t, 1, 0), 2);      
                    return null;
                }
            });
        }
        try {
            final List<Future<Void>> l = e.invokeAll(s);
            for (final Future<Void> f : l) {
                try {
                    f.get();
                } catch (ExecutionException e1) {
                    fail();
                }
                assertTrue(f.isDone());
            }
            for (int i = 0; i < 30; i++) {
                assertTrue(r.getOrders(i+1).containsKey(new Dish("piatto" + (i+1), 1, 0)));
            }
        } catch (final InterruptedException e1) {
            fail();
        }
        
        final Set<Callable<Void>> s2 = new HashSet<Callable<Void>>();
        for (int i = 0; i < 60; i++) {
            final int t = (i % 30) + 1;
            s2.add(new Callable<Void>() {           
                @Override
                public Void call() {
                    r.removeOrder(t, new Dish("piatto" + t, 1, 0), 1);      
                    return null;
                }
            });
        }
        try {
            final List<Future<Void>> l = e.invokeAll(s2);
            for (final Future<Void> f : l) {
                try {
                    f.get();
                } catch (ExecutionException e1) {
                    fail();
                }
                assertTrue(f.isDone());
            }
            for (int i = 0; i < 30; i++) {
                //System.out.println(r.getOrders(i+1));
                assertTrue(r.getOrders(i+1).isEmpty());
            }
        } catch (final InterruptedException e1) {
            fail();
        }
     }

}
