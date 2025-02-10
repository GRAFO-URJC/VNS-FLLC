package es.urjc.etsii.grafo.Vehicles.moves;

import es.urjc.etsii.grafo.Vehicles.model.VehiclesSolution;

import java.util.Objects;

public abstract class AbstractMove extends AbstractBaseMove {
    double oldScore;

    double newScore;

    public AbstractMove(VehiclesSolution solution) {
        super(solution);
        oldScore = solution.getScore();
    }

    @Override
    protected abstract VehiclesSolution _execute(VehiclesSolution s);

    @Override
    public double getValue() {
        return newScore;
    }

    @Override
    public boolean improves() {
        return newScore > oldScore;
    }

    @Override
    public String toString() {
        return "FastMove{" + "oldScore=" + oldScore + ", newScore=" + newScore + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        // No super
//        if (!super.equals(o)) return false;
        AbstractMove slowMove = (AbstractMove) o;
        return Double.compare(slowMove.oldScore, oldScore) == 0 && Double.compare(slowMove.newScore, newScore) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), oldScore, newScore);
    }

    public double getNewScore() {
        return newScore;
    }
}

