package es.urjc.etsii.grafo.Vehicles.neighborhoods;

import es.urjc.etsii.grafo.Vehicles.moves.AbstractBaseMove;
import es.urjc.etsii.grafo.Vehicles.model.VehiclesSolution;
import es.urjc.etsii.grafo.util.random.RandomManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddNeighborhood extends AbstractNeighborhood {

    @Override
    public List<AbstractBaseMove> getMovements(VehiclesSolution vehiclesSolution) {
        var list = new ArrayList<AbstractBaseMove>();
        var j = vehiclesSolution.getX();
        for (int i = 0; i < j.length; i++) {
            if (!j[i]) {
                list.add(vehiclesSolution.addLocation(i));
            }
        }
        return list;
    }

    @Override
    public Optional<AbstractBaseMove> getRandomMove(VehiclesSolution vehiclesSolution) {
        var random = RandomManager.getRandom();
        var j = vehiclesSolution.getX();
        var list = new ArrayList<Integer>(j.length);
        for (int i = 0; i < j.length; i++) {
            if (!j[i]) {
                list.add(i);
            }
        }
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(vehiclesSolution.addLocation(list.get(random.nextInt(list.size()))));
    }

    public Optional<AbstractBaseMove> getRandomGreedyMovement(VehiclesSolution vehiclesSolution, Double alpha) {
        List<Integer> candidates = new ArrayList<Integer>();
        var j = vehiclesSolution.getX();
        for (int i = 0; i < j.length; i++) {
            if (!j[i]) {
                candidates.add(i);
            }
        }
        if (candidates.isEmpty()) return Optional.empty();

        var random = RandomManager.getRandom();
        AbstractBaseMove best = null;
        var index = candidates.size() * alpha;
        do {
            index--;
            var move = vehiclesSolution.addLocation(candidates.remove(random.nextInt(candidates.size())));
            if (best == null) {
                best = move;
            } else if (move.getValue() > best.getValue()) {
                best = move;
            }
        } while (index > 0);
        return Optional.of(best);
    }

    @Override
    public int neighborhoodSize(VehiclesSolution solution) {
        return solution.getInstance().getJ() - solution.getXSet().size();
    }
}
