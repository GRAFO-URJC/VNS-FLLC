package es.urjc.etsii.grafo.Vehicles.constructives;

import es.urjc.etsii.grafo.Vehicles.moves.AbstractBaseMove;
import es.urjc.etsii.grafo.Vehicles.model.VehiclesInstance;
import es.urjc.etsii.grafo.Vehicles.model.VehiclesSolution;
import es.urjc.etsii.grafo.Vehicles.neighborhoods.AddNeighborhood;
import es.urjc.etsii.grafo.util.random.RandomManager;

import java.util.Optional;

public class Constructive extends es.urjc.etsii.grafo.create.Constructive<VehiclesSolution, VehiclesInstance> {
    double alpha;
    boolean random = false;

    public Constructive(double alpha) {
        this.alpha = alpha;
    }

    public Constructive(double alpha, boolean random) {
        this(alpha);
        this.random = random;
    }

    @Override
    public VehiclesSolution construct(VehiclesSolution solution) {
        solution.recalculateScore();
        var add = new AddNeighborhood();

        Optional<AbstractBaseMove> move;
        boolean improve;
        do {
            improve = false;
            if (random) {
                var random = RandomManager.getRandom();
                alpha = random.nextDouble(1);
            }
            move = add.getRandomGreedyMovement(solution, alpha);
            if (move.isPresent() && move.get().improves()) {
                improve = true;
                move.get().execute(solution);
            }
        } while (improve);

        return solution;
    }
}
