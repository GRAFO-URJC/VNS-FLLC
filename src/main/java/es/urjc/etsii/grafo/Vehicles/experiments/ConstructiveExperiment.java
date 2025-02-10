package es.urjc.etsii.grafo.Vehicles.experiments;

import es.urjc.etsii.grafo.Vehicles.algorithms.BVNS;
import es.urjc.etsii.grafo.Vehicles.algorithms.ILS;
import es.urjc.etsii.grafo.Vehicles.constructives.Constructive;
import es.urjc.etsii.grafo.Vehicles.constructives.ConstructiveWithoutRefactor;
import es.urjc.etsii.grafo.Vehicles.ls.EfficientLocalSearch;
import es.urjc.etsii.grafo.Vehicles.ls.LocalSearch;
import es.urjc.etsii.grafo.Vehicles.model.VehiclesInstance;
import es.urjc.etsii.grafo.Vehicles.model.VehiclesSolution;
import es.urjc.etsii.grafo.Vehicles.neighborhoods.DropNeighborhood;
import es.urjc.etsii.grafo.Vehicles.neighborhoods.DropWithoutRefactorNeighborhood;
import es.urjc.etsii.grafo.algorithms.Algorithm;
import es.urjc.etsii.grafo.algorithms.VNS;
import es.urjc.etsii.grafo.experiment.AbstractExperiment;
import es.urjc.etsii.grafo.shake.RandomMoveShake;
import es.urjc.etsii.grafo.solver.Mork;

import java.util.ArrayList;
import java.util.List;

public class ConstructiveExperiment extends AbstractExperiment<VehiclesSolution, VehiclesInstance> {

    @Override
    public List<Algorithm<VehiclesSolution, VehiclesInstance>> getAlgorithms() {
        var algorithms = new ArrayList<Algorithm<VehiclesSolution, VehiclesInstance>>();
        // This is an optional config to execute the MsILS algorithm
        // this.MsILS(algorithms);
        this.MsBVNS(algorithms);
        return algorithms;
    }

    private void MsILS(List<Algorithm<VehiclesSolution, VehiclesInstance>> lista) {
        var maximazing = Mork.getFMode();
        var alphaValue = 0.25;
        var maxIteration = 50;
        lista.add(new ILS<>("MsILS", maxIteration * 1000, maxIteration,
                new ConstructiveWithoutRefactor(alphaValue),
                new RandomMoveShake<>(new DropWithoutRefactorNeighborhood()),
                new LocalSearch(maximazing)
        ));
    }

    private void MsBVNS(List<Algorithm<VehiclesSolution, VehiclesInstance>> lista) {
        var maximizing = Mork.getFMode();
        lista.add(new BVNS(
                "MsBVNS",
                (solution, kIndex) -> {
                    if (kIndex < (solution.getXSet().size() * 0.35)) return kIndex;
                    return VNS.KMapper.STOPNOW;
                },
                new Constructive(0.39),
                new RandomMoveShake<>(new DropNeighborhood()),
                new EfficientLocalSearch(maximizing, 2, true)
        ));
    }
}
