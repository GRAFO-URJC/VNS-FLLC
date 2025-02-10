package es.urjc.etsii.grafo.Vehicles.moves;

import es.urjc.etsii.grafo.Vehicles.model.VehiclesSolution;

import java.util.Objects;

public abstract class AbstractMoveWithoutRefactor extends AbstractBaseMove {
    VehiclesSolution newSolution;
    double oldScore;
    double newScore;

    public AbstractMoveWithoutRefactor(VehiclesSolution solution) {
        super(solution);
        oldScore = solution.getScore();
    }

    @Override
    protected VehiclesSolution _execute(VehiclesSolution s) {
        s.setX(newSolution.getX());
        s.notifyUpdate();
        return s;
    }

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
        return "SlowMove{" + "newSolution=" + newSolution + ", oldScore=" + oldScore + ", newScore=" + newScore + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractMoveWithoutRefactor moveWithRecalculation = (AbstractMoveWithoutRefactor) o;
        return Double.compare(moveWithRecalculation.oldScore, oldScore) == 0 && Double.compare(moveWithRecalculation.newScore, newScore) == 0 && Objects.equals(newSolution, moveWithRecalculation.newSolution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), newSolution, oldScore, newScore);
    }
}
