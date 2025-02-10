package es.urjc.etsii.grafo.Vehicles.moves;

import es.urjc.etsii.grafo.Vehicles.model.VehiclesSolution;
import es.urjc.etsii.grafo.Vehicles.moves.changes.ChangeRecords;

import java.util.List;

public class AddWithoutRefactor extends AbstractMove {
    protected List<ChangeRecords> changesList;
    protected int facility;

    public AddWithoutRefactor(VehiclesSolution solution, int facility, double newScore, List<ChangeRecords> changesList) {
        super(solution);
        this.changesList = changesList;
        this.facility = facility;
        this.newScore = newScore;
    }

    @Override
    protected VehiclesSolution _execute(VehiclesSolution s) {
        s.applyAddLocation(this.facility, this.changesList, this.newScore);
        return s;
    }

    @Override
    public String toString() {
        return "AddLocationFaster";
    }
}
