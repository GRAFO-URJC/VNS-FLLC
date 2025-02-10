package es.urjc.etsii.grafo.Vehicles.ls;

import es.urjc.etsii.grafo.Vehicles.comparators.MoveComparator;
import es.urjc.etsii.grafo.Vehicles.moves.AbstractBaseMove;
import es.urjc.etsii.grafo.Vehicles.model.VehiclesInstance;
import es.urjc.etsii.grafo.Vehicles.model.VehiclesSolution;
import es.urjc.etsii.grafo.Vehicles.neighborhoods.AddWithoutRefactorNeighborhood;
import es.urjc.etsii.grafo.Vehicles.neighborhoods.DropWithoutRefactorNeighborhood;
import es.urjc.etsii.grafo.algorithms.FMode;
import es.urjc.etsii.grafo.improve.Improver;

import java.util.Optional;
import java.util.stream.Stream;

public class LocalSearch extends Improver<VehiclesSolution, VehiclesInstance> {


    /**
     * Initialize common improver fields, to be called by subclasses
     *
     * @param ofmode MAXIMIZE to maximize scores returned by the given move, MINIMIZE for minimizing
     */
    public LocalSearch(FMode ofmode) {
        super(ofmode);
    }

    protected Optional<AbstractBaseMove> getBest(Stream<AbstractBaseMove> stream) {
        return stream.reduce((a, b) -> MoveComparator.getBest(b, a, this.ofmode));
    }

    @Override
    protected VehiclesSolution _improve(VehiclesSolution vehiclesSolution) {
        var add = new AddWithoutRefactorNeighborhood();
        var remove = new DropWithoutRefactorNeighborhood();
        var improve = true;
        while (improve) {
            improve = false;
            Optional<AbstractBaseMove> move = null;

            do {
                move = getBest(add.getMovements(vehiclesSolution).stream());
                if (move.isPresent() && move.get().improves()) {
                    move.get().execute(vehiclesSolution);
                    improve = true;
                }
            } while (move.isPresent() && move.get().improves());

            do {
                move = getBest(remove.getMovements(vehiclesSolution).stream());
                if (move.isPresent() && move.get().improves()) {
                    move.get().execute(vehiclesSolution);
                    improve = true;
                }
            } while (move.isPresent() && move.get().improves());
        }
        return vehiclesSolution;
    }
}
