package com.github.gscaparrotti.bendermodel.utils;

import com.github.gscaparrotti.bendermodel.model.Order;
import com.github.gscaparrotti.bendermodel.model.OrderedDish;
import java.util.Comparator;

public class BenderModelStaticUtils {

    public static Comparator<Order> getComparatorByTime() {
        return (o1, o2) -> {
            if (o1.getDish() instanceof OrderedDish && o2.getDish() instanceof OrderedDish) {
                return (((OrderedDish) o1.getDish()).getTime().compareTo(((OrderedDish) o2.getDish()).getTime()));
            } else if (o1.getDish() instanceof OrderedDish && !(o2.getDish() instanceof OrderedDish)) {
                return -1;
            } else if (o2.getDish() instanceof OrderedDish && !(o1.getDish() instanceof OrderedDish)){
                return 1;
            } else {
                return 0;
            }
        };
    }

    public static Comparator<Order> getComparatorByQuantity() {
        return (o1, o2) -> (o2.getAmounts().getX() - o2.getAmounts().getY()) - (o1.getAmounts().getX() - o1.getAmounts().getY());
    }

}
