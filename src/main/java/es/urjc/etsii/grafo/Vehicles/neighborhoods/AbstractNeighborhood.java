package es.urjc.etsii.grafo.Vehicles.neighborhoods;

import es.urjc.etsii.grafo.Vehicles.moves.AbstractBaseMove;
import es.urjc.etsii.grafo.Vehicles.model.VehiclesInstance;
import es.urjc.etsii.grafo.Vehicles.model.VehiclesSolution;
import es.urjc.etsii.grafo.solution.neighborhood.ExploreResult;
import es.urjc.etsii.grafo.solution.neighborhood.RandomizableNeighborhood;
import es.urjc.etsii.grafo.util.random.RandomManager;

import java.util.List;
import java.util.Optional;

public abstract class AbstractNeighborhood extends RandomizableNeighborhood<AbstractBaseMove, VehiclesSolution, VehiclesInstance> {
    @Override
    public Optional<AbstractBaseMove> getRandomMove(VehiclesSolution vehiclesSolution) {
        var random = RandomManager.getRandom();
        var list = this.explore(vehiclesSolution).moves().toList();
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(random.nextInt(list.size())));
    }

    public abstract List<AbstractBaseMove> getMovements(VehiclesSolution vehiclesSolution);

    @Override
    public ExploreResult<AbstractBaseMove, VehiclesSolution, VehiclesInstance> explore(VehiclesSolution solution) {
        return ExploreResult.fromList(this.getMovements(solution));
    }

    @Override
    public int neighborhoodSize(VehiclesSolution solution) {
        return this.explore(solution).size();
    }
}
