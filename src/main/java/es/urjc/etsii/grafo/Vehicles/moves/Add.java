package es.urjc.etsii.grafo.Vehicles.moves;

import es.urjc.etsii.grafo.Vehicles.model.VehiclesSolution;

public class Add extends AbstractMoveWithoutRefactor {
    public Add(VehiclesSolution solution, int location) {
        super(solution);
        newSolution = solution.cloneSolution();
        newSolution.setX(location);
        newScore = newSolution.getScore();
        newSolution.notifyUpdate();
    }

    @Override
    public String toString() {
        return "Add";
    }

}
