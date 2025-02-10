package es.urjc.etsii.grafo.Vehicles.moves;

import es.urjc.etsii.grafo.Vehicles.model.VehiclesSolution;

public class DropWithoutRefactor extends AbstractMoveWithoutRefactor {
    private final int location;

    public DropWithoutRefactor(VehiclesSolution solution, int location) {
        super(solution);
        this.location = location;
        newSolution = solution.cloneSolution();
        newSolution.clearX(location);
        newScore = newSolution.getScore();
        newSolution.notifyUpdate();
    }

    @Override
    public String toString() {
        return "Slow %s".formatted(location);
    }
}
