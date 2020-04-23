package com.github.gscaparrotti.bendermodel.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import com.google.common.base.Preconditions;

/**
 *
 */
public class Menu extends LinkedList<IDish> implements IMenu {

    /**
     * 
     */
    private static final long serialVersionUID = -1111129590390041868L;

    @Override
    public void addItems(final IDish... items) {
        for (final IDish i : items) {
            Preconditions.checkNotNull(i);
        }
        this.addAll(Arrays.asList(items));
    }

    @Override
    public IDish[] getDishesArray() {
        final IDish[] temp = this.toArray(new Dish[this.size()]);
        Arrays.sort(temp, new Comparator<IDish>() {
            @Override
            public int compare(final IDish t1, final IDish t2) {
                return t1.getName().compareTo(t2.getName());
            }
        });
        return temp;
    }

}
