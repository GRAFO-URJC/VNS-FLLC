package es.urjc.etsii.grafo.Vehicles;

import es.urjc.etsii.grafo.algorithms.FMode;
import es.urjc.etsii.grafo.solver.Mork;

public class Main {
    public static void main(String[] args) {
        Mork.start(args, FMode.MAXIMIZE);
    }
}
