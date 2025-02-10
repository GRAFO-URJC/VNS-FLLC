package es.urjc.etsii.grafo.Vehicles.ls;

import es.urjc.etsii.grafo.Vehicles.comparators.MoveComparator;
import es.urjc.etsii.grafo.Vehicles.moves.AbstractBaseMove;
import es.urjc.etsii.grafo.Vehicles.model.VehiclesInstance;
import es.urjc.etsii.grafo.Vehicles.model.VehiclesSolution;
import es.urjc.etsii.grafo.Vehicles.neighborhoods.AbstractNeighborhood;
import es.urjc.etsii.grafo.Vehicles.neighborhoods.AddNeighborhood;
import es.urjc.etsii.grafo.Vehicles.neighborhoods.DropNeighborhood;
import es.urjc.etsii.grafo.algorithms.FMode;
import es.urjc.etsii.grafo.improve.Improver;

import java.util.Optional;
import java.util.stream.Stream;

public class EfficientLocalSearch extends Improver<VehiclesSolution, VehiclesInstance> {
    protected int divider;
    protected boolean reversed;

    public EfficientLocalSearch(FMode ofMode) {
        super(ofMode);
        this.divider = 2;
        this.reversed = false;
    }

    public EfficientLocalSearch(FMode ofMode, int divider) {
        this(ofMode);
        this.divider = divider;
    }

    public EfficientLocalSearch(FMode ofMode, int divider, boolean reversed) {
        this(ofMode, divider);
        this.reversed = reversed;
    }

    protected Optional<AbstractBaseMove> getBest(Stream<AbstractBaseMove> stream) {
        return stream.reduce((a, b) -> MoveComparator.getBest(b, a, this.ofmode));
    }

    @Override
    protected VehiclesSolution _improve(VehiclesSolution vehiclesSolution) {
        AbstractNeighborhood fistLS = new AddNeighborhood();
        AbstractNeighborhood secondLS = new DropNeighborhood(this.divider);

        if (this.reversed) {
            AbstractNeighborhood tmp = fistLS;
            fistLS = secondLS;
            secondLS = tmp;
        }

        doesMoveImprove(vehiclesSolution, fistLS);
        do {
            var old_vehiclesSolution = vehiclesSolution;
            if ((vehiclesSolution = doesMoveImprove(vehiclesSolution, secondLS)).getVersion() == old_vehiclesSolution.getVersion()) {
                break;
            }
            old_vehiclesSolution = vehiclesSolution;
            if ((vehiclesSolution = doesMoveImprove(vehiclesSolution, fistLS)).getVersion() == old_vehiclesSolution.getVersion()) {
                break;
            }
        } while (true);

        return vehiclesSolution;
    }

    private VehiclesSolution doesMoveImprove(VehiclesSolution vehiclesSolution, AbstractNeighborhood neighborhood) {
        Optional<AbstractBaseMove> move;
        do {
            move = getBest(neighborhood.getMovements(vehiclesSolution).stream());
            if (move.isPresent() && move.get().improves()) {
                vehiclesSolution = move.get().execute(vehiclesSolution);
            }
        } while (move.isPresent() && move.get().improves());
        return vehiclesSolution;
    }
}
