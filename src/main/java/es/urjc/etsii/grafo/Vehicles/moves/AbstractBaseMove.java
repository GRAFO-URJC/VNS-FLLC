package es.urjc.etsii.grafo.Vehicles.moves;

import es.urjc.etsii.grafo.Vehicles.model.VehiclesInstance;
import es.urjc.etsii.grafo.Vehicles.model.VehiclesSolution;
import es.urjc.etsii.grafo.solution.EagerMove;

public class AbstractBaseMove extends EagerMove<VehiclesSolution, VehiclesInstance> {
    public AbstractBaseMove(VehiclesSolution solution) {
        super(solution);
    }

    @Override
    protected VehiclesSolution _execute(VehiclesSolution s) {
        // TODO Apply changes to solution if movement is executed
        // this.s --> reference to current solution
        throw new UnsupportedOperationException("_execute() in VehiclesListManager not implemented yet");

    }

    @Override
    public double getValue() {
        // TODO How much does o.f. value change if we apply this movement?
        throw new UnsupportedOperationException("getValue() in VehiclesListManager not implemented yet");
    }

    public boolean improves() {
        // TODO: Choose implementation
        // If maximizing: return DoubleComparator.isPositive(this.score)
        // If minimizing: return DoubleComparator.isNegative(this.score)
        throw new UnsupportedOperationException("improves() in VehiclesListManager not implemented yet");
    }

    @Override
    public String toString() {
        // TODO Use IDE to generate this method after all properties are defined
        throw new UnsupportedOperationException("toString() in Vehicles not implemented yet");
    }

    @Override
    public boolean equals(Object o) {
        // TODO Use IDE to generate this method after all properties are defined
        throw new UnsupportedOperationException("equals() in Vehicles not implemented yet");
    }

    @Override
    public int hashCode() {
        // TODO Use IDE to generate this method after all properties are defined
        throw new UnsupportedOperationException("hashCode() in Vehicles not implemented yet");
    }
}
