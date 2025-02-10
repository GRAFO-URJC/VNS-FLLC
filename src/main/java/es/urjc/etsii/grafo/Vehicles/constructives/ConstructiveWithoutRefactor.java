package es.urjc.etsii.grafo.Vehicles.constructives;

import es.urjc.etsii.grafo.Vehicles.moves.AbstractBaseMove;
import es.urjc.etsii.grafo.Vehicles.model.VehiclesInstance;
import es.urjc.etsii.grafo.Vehicles.model.VehiclesSolution;
import es.urjc.etsii.grafo.Vehicles.neighborhoods.AddWithoutRefactorNeighborhood;
import es.urjc.etsii.grafo.create.Constructive;

import java.util.Optional;

public class ConstructiveWithoutRefactor extends Constructive<VehiclesSolution, VehiclesInstance> {
    double alpha;

    public ConstructiveWithoutRefactor(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public VehiclesSolution construct(VehiclesSolution solution) {
        solution.recalculateScore();
        var remove = new AddWithoutRefactorNeighborhood();

        Optional<AbstractBaseMove> move;
        boolean improve;
        do {
            improve = false;
            move = remove.getRandomGreedyMovement(solution, alpha);
            if (move.isPresent() && move.get().improves()) {
                improve = true;
                move.get().execute(solution);
            }
        } while (improve);

        return solution;
    }
}
