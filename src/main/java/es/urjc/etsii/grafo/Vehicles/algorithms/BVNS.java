package es.urjc.etsii.grafo.Vehicles.algorithms;

import es.urjc.etsii.grafo.Vehicles.model.VehiclesInstance;
import es.urjc.etsii.grafo.Vehicles.model.VehiclesSolution;
import es.urjc.etsii.grafo.algorithms.Algorithm;
import es.urjc.etsii.grafo.create.Constructive;
import es.urjc.etsii.grafo.improve.Improver;
import es.urjc.etsii.grafo.io.Instance;
import es.urjc.etsii.grafo.shake.Shake;
import es.urjc.etsii.grafo.solution.Solution;
import es.urjc.etsii.grafo.util.TimeControl;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Variable neighborhood search (VNS) is a metaheuristic for solving combinatorial
 * and global optimization problems. Its basic idea is the systematic change of
 * neighborhood both in a descent phase to find a local optimum and in a perturbation
 * phase to exit the corresponding local optimum
 * <p>
 * Algorithmic outline of the simplest version of VNS
 * </p>
 * <pre>
 * s = GenerateInitialSolution
 * while (Termination criteria is not met){
 *      k = 1
 *      while (k != kmax){
 *          s' = Shake(s,k)
 *          s'' = Improve (s')
 *          NeighborhoodChange(s,s'',k)
 *      }
 * }
 * </pre>
 * <p>
 * More information can be found in:
 * Hansen P., Mladenović N. (2018) Variable Neighborhood Search.
 * In: Martí R., Pardalos P., Resende M. (eds) Handbook of Heuristics.
 * Springer, Cham. <a href="https://doi.org/10.1007/978-3-319-07124-4_19">...</a>
 */
public class BVNS extends Algorithm<VehiclesSolution, VehiclesInstance> {

    /**
     * Default KMapper, maps identity, stops when k = 5
     */
    protected static final es.urjc.etsii.grafo.algorithms.VNS.KMapper<VehiclesSolution, VehiclesInstance> DEFAULT_KMAPPER = (solution, originalK) -> originalK >= 5 ? es.urjc.etsii.grafo.algorithms.VNS.KMapper.STOPNOW : originalK;
    private static final Logger log = Logger.getLogger(es.urjc.etsii.grafo.algorithms.VNS.class.getName());
    protected List<Improver<VehiclesSolution, VehiclesInstance>> improvers;
    /**
     * Constructive procedure
     */
    protected Constructive<VehiclesSolution, VehiclesInstance> constructive;
    /**
     * Shake procedure
     */
    protected List<Shake<VehiclesSolution, VehiclesInstance>> shakes;
    /**
     * Calculates K value for each VNS step. {@see KMapper}
     */
    protected es.urjc.etsii.grafo.algorithms.VNS.KMapper<VehiclesSolution, VehiclesInstance> kMapper;

    /**
     * Execute VNS until finished
     *
     * @param algorithmName Algorithm name, example: "VNSWithRandomConstructive"
     * @param kMapper       k value provider, @see VNS.KMapper
     * @param shake         Perturbation method
     * @param constructive  Constructive method
     * @param improvers     List of improvers/local searches
     */
    public BVNS(String algorithmName, es.urjc.etsii.grafo.algorithms.VNS.KMapper<VehiclesSolution, VehiclesInstance> kMapper, Constructive<VehiclesSolution, VehiclesInstance> constructive, Shake<VehiclesSolution, VehiclesInstance> shake, Improver<VehiclesSolution, VehiclesInstance> improvers) {
        this(algorithmName, kMapper, constructive, Collections.singletonList(shake), improvers);
    }

    /**
     * VNS with default KMapper, which starts at 0 and increments by 1 each time the solution does not improve.
     * Stops when k >= 5. Behaviour can be customized passing a custom kMapper, such as:
     * <pre>
     * {@code
     * (solution, originalK) -> originalK >= 10 ? KMapper.STOPNOW : originalK
     * }
     * </pre>
     *
     * @param algorithmName Algorithm name, example: "VNSWithRandomConstructive"
     * @param shake         Perturbation method
     * @param constructive  Constructive method
     * @param improvers     List of improvers/local searches
     */
    @SuppressWarnings("unchecked")
    public BVNS(String algorithmName, Constructive<VehiclesSolution, VehiclesInstance> constructive, Shake<VehiclesSolution, VehiclesInstance> shake, Improver<VehiclesSolution, VehiclesInstance> improvers) {
        this(algorithmName, DEFAULT_KMAPPER, constructive, Collections.singletonList(shake), improvers);
    }


    /**
     * Execute VNS until finished
     *
     * @param algorithmName Algorithm name, example: "VNSWithRandomConstructive"
     * @param kMapper       k value provider, @see VNS.KMapper
     * @param shakes        Perturbation method
     * @param constructive  Constructive method
     * @param improvers     List of improvers/local searches
     */
    public BVNS(String algorithmName, es.urjc.etsii.grafo.algorithms.VNS.KMapper<VehiclesSolution, VehiclesInstance> kMapper, Constructive<VehiclesSolution, VehiclesInstance> constructive, List<Shake<VehiclesSolution, VehiclesInstance>> shakes, Improver<VehiclesSolution, VehiclesInstance> improvers) {
        super(algorithmName);
        this.kMapper = kMapper;

        // Ensure Ks are sorted, maxK is the last element
        this.shakes = shakes;
        this.constructive = constructive;
        this.improvers = Collections.singletonList(improvers);
    }

    /**
     * VNS algorithm. This procedure follows this schema:
     * <pre>
     *     s = GenerateInitial solution
     *     k = 1
     *     while (k != kmax){
     *     s' = Shake(s,k)
     *     s'' = Improve (s')
     *     NeighborhoodChange(s,s'',k)
     *     }
     * </pre>
     * <p>
     * To run the VNS procedure multiples time consider use MultiStart algorithm class {@see es.urjc.etsii.grafo.solver.algorithms.multistart.MultiStartAlgorithm}
     *
     * @param instance Instance to solve
     * @return best solution found when the VNS procedure ends
     */
    public VehiclesSolution algorithm(VehiclesInstance instance) {
        var solution = this.newSolution(instance);
        solution = constructive.construct(solution);
        solution = localSearch(solution);

        int internalK = 0;
        // While stop not request OR k in range. k check is done and breaks inside loop
        while (!TimeControl.isTimeUp()) {
            int userK = kMapper.apply(solution, internalK);
            if (userK == es.urjc.etsii.grafo.algorithms.VNS.KMapper.STOPNOW) {
                printStatus(internalK + ":STOPNOW", solution);
                break;
            }
            printStatus(internalK + ":" + userK, solution);
            VehiclesSolution bestSolution = solution;

            for (var shake : shakes) {
                VehiclesSolution copy = bestSolution.cloneSolution();
                copy = shake.shake(copy, userK);
                copy = localSearch(copy);
                if (copy.isBetterThan(bestSolution)) {
                    bestSolution = copy;
                }
            }
            if (bestSolution == solution) {
                internalK++;
            } else {
                solution = bestSolution;
                internalK = 0;
            }
        }
        solution.recalculateScore();
        return solution;
    }

    /**
     * Improving method. Given a solution, this method execute sequentially the improvement procedures.
     *
     * @param solution initial solution of the procedure
     * @return the improved solution
     */
    protected VehiclesSolution localSearch(VehiclesSolution solution) {
        for (Improver<VehiclesSolution, VehiclesInstance> ls : improvers) {
            solution = ls.improve(solution);
        }
        return solution;
    }

    /**
     * Print the current status of the VNS procedure, i.e., the current iteration the best solution.
     *
     * @param phase            current state of the vns procedure
     * @param vehiclesSolution solution
     */
    protected void printStatus(String phase, VehiclesSolution vehiclesSolution) {
        log.fine(String.format("%s: \t%s", phase, vehiclesSolution));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "VNS{" + "improvers=" + improvers + ", constructive=" + constructive + ", shakes=" + shakes + ", kmap=" + kMapper + '}';
    }

    /**
     * Calculates K value for each VNS step.
     */
    @FunctionalInterface
    public interface KMapper<S extends Solution<S, I>, I extends Instance> {
        int STOPNOW = -1;

        /**
         * Map internal K value to custom values during VNS execution.
         * VNS by default uses an internal K which starts in 0 and is incremented by 1 each time the solution does not improve.
         * This K can be mapped to any other number using this interface. Example: multiply by 5, stop if result would be greater than 100.
         * <pre>
         * {@code
         * (solution, originalK) -> originalK >= 20 ? KMapper.STOPNOW : originalK * 5
         * }
         * </pre>
         * would generate the following mapping
         * <pre>
         * 0 —> 0
         * 1 —> 5
         * 2 —> 10
         * 3 —> 15
         * etc.
         * </pre>
         *
         * @param solution  Current instance, provided as a parameter so K can be adapted or scaled to instance size.
         * @param originalK Current k strength. Starts in 0 and increments by 1 each time the solution does not improve.
         * @return K value to use. Return KMapper.STOPNOW to stop when the VNS should terminate
         */
        int mapK(S solution, int originalK);
    }

}
