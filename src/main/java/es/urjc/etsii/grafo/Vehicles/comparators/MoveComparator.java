package es.urjc.etsii.grafo.Vehicles.comparators;

import es.urjc.etsii.grafo.Vehicles.moves.AbstractBaseMove;
import es.urjc.etsii.grafo.algorithms.FMode;

public class MoveComparator {
    public static AbstractBaseMove getBest(AbstractBaseMove a, AbstractBaseMove b, FMode ofMode) {
        return (ofMode.isBetter(a.getValue(), b.getValue())) ? a : b;
    }
}
