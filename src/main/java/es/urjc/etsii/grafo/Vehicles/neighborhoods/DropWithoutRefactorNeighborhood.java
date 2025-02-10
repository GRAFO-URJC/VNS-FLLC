package es.urjc.etsii.grafo.Vehicles.neighborhoods;

import es.urjc.etsii.grafo.Vehicles.moves.AbstractBaseMove;
import es.urjc.etsii.grafo.Vehicles.model.VehiclesSolution;
import es.urjc.etsii.grafo.Vehicles.moves.DropWithoutRefactor;
import es.urjc.etsii.grafo.util.random.RandomManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DropWithoutRefactorNeighborhood extends AbstractNeighborhood {
    @Override
    public List<AbstractBaseMove> getMovements(VehiclesSolution vehiclesSolution) {
        var list = new ArrayList<AbstractBaseMove>();
        var j = vehiclesSolution.getX();
        for (int i = 0; i < j.length; i++) {
            if (j[i]) {
                list.add(new DropWithoutRefactor(vehiclesSolution, i));
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
            if (j[i]) {
                list.add(i);
            }
        }
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new DropWithoutRefactor(vehiclesSolution, list.get(random.nextInt(list.size()))));
    }
}
