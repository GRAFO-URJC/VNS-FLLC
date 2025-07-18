package es.urjc.etsii.grafo.Vehicles.algorithms;

import es.urjc.etsii.grafo.algorithms.Algorithm;
import es.urjc.etsii.grafo.create.Constructive;
import es.urjc.etsii.grafo.improve.Improver;
import es.urjc.etsii.grafo.io.Instance;
import es.urjc.etsii.grafo.shake.Shake;
import es.urjc.etsii.grafo.solution.Solution;
import es.urjc.etsii.grafo.util.TimeControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Iterated greedy is a search method that iterates through applications of construction
 * heuristics using the repeated execution of two main phases, the partial
 * destruction of a complete candidate solution and a subsequent reconstruction of
 * a complete candidate solution.
 * <p>
 * Algorithmic outline of the simplest version of Iterated Greedy (IG)
 * <p>
 * s = GenerateInitialSolution
 * do {
 * s' = Destruction(s)
 * s'' = Reconstruction (s'')
 * s = AcceptanceCriterion(s,s'')
 * } while (Termination criteria is not met)
 * return s
 * <p>
 * Iterated greedy algorithms natural extension is to improve the generated solutions by
 * the application of an improvement algorithm, such as local search procedures.
 * <p>
 * Algorithmic outline of an IG with an additional local search step
 * <p>
 * For further information about Iterated Greedy Algorithms see:
 * Stützle T., Ruiz R. (2018) Iterated Greedy.
 * In: Martí R., Pardalos P., Resende M. (eds) Handbook of Heuristics.
 * Springer, Cham. <a href="https://doi.org/10.1007/978-3-319-07124-4_10">...</a>
 *
 * @param <S> the type of the problem solution
 * @param <I> the type of problem instances
 */
public class ILS<S extends Solution<S, I>, I extends Instance> extends Algorithm<S, I> {

    private static final Logger logger = LoggerFactory.getLogger(ILS.class);

    /**
     * Constructive procedure
     */
    private Constructive<S, I> constructive;

    /**
     * Destructive and reconstructive procedure
     */
    private Shake<S, I> destructionReconstruction;

    /**
     * Improving procedures
     */
    private Improver<S, I>[] improvers;

    /**
     * Maximum number of iterations the algorithm could be executed.
     */
    private int maxIterations;

    /**
     * Maximum number of iterations without improving the algorithm could be executed.
     */
    private int stopIfNotImprovedIn;


    /**
     * Protected constructor, for serialization, use public one when creating a new IteratedGreedy
     */
    protected ILS(String name) {
        super(name);
    }

    /**
     * Iterated Greedy Algorithm constructor
     *
     * @param maxIterations             maximum number of iterations the algorithm could be executed.
     * @param stopIfNotImprovedIn       maximum number of iterations without improving the algorithm could be executed.
     * @param constructive              constructive procedure to generate the initial solution of the algorithm
     * @param destructionReconstruction destruction and reconstruction procedures
     * @param improvers                 improving procedures. Could be 0 or more.
     */
    @SafeVarargs
    public ILS(String name, int maxIterations, int stopIfNotImprovedIn, Constructive<S, I> constructive, Shake<S, I> destructionReconstruction, Improver<S, I>... improvers) {
        this(name);
        if (stopIfNotImprovedIn < 1) {
            throw new IllegalArgumentException("stopIfNotImprovedIn must be greater than 0");
        }
        this.maxIterations = maxIterations;
        this.stopIfNotImprovedIn = stopIfNotImprovedIn;
        this.constructive = constructive;
        this.destructionReconstruction = destructionReconstruction;
        this.improvers = improvers;
    }

    public String getShortName() {
        return this.getName();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Iterated greedy algorithm procedure
     */
    @Override
    public S algorithm(I instance) {
        S solution = this.newSolution(instance);
        solution = this.constructive.construct(solution);
        if (TimeControl.isTimeUp()) {
            return solution;
        }
        solution = ls(solution);
        logger.debug("Initial solution: {} - {}", solution.getScore(), solution);
        int iterationsWithoutImprovement = 0;
        for (int i = 0; i < maxIterations; i++) {
            if (TimeControl.isTimeUp()) {
                return solution;
            }
            S copy = solution.cloneSolution();
            copy = this.destructionReconstruction.shake(copy, 1);
            if (copy != null) {
                copy = ls(copy);
            }

            // Analyze result
            if (copy == null || !copy.isBetterThan(solution)) {
                iterationsWithoutImprovement++;
                if (iterationsWithoutImprovement >= this.stopIfNotImprovedIn) {
                    logger.debug("Not improved after {} iterations, stopping in iteration {}. Current score {} - {}", stopIfNotImprovedIn, i, solution.getScore(), solution);
                    break;
                }
            } else {
                solution = copy;
                logger.debug("Improved at iteration {}: {} - {}", i, solution.getScore(), solution);
                iterationsWithoutImprovement = 0;
            }
        }
        solution.recalculateScore();
        return solution;
    }

    /**
     * Improving method. Given a solution, this method execute sequentially the improvement procedures.
     * If no improvement procedure is defined, the solution returned is the same as the one given as a parameter of the method.
     *
     * @param solution initial solution  of the procedure
     * @return the improved solution
     */
    private S ls(S solution) {
        if (improvers == null) return solution;

        for (Improver<S, I> improver : improvers) {
            solution = improver.improve(solution);
        }
        return solution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "IteratedGreedy{" +
                "constructive=" + constructive +
                ", destructionReconstruction=" + destructionReconstruction +
                ", improvers=" + Arrays.toString(improvers) +
                ", maxIterations=" + maxIterations +
                ", stopIfNotImprovedIn=" + stopIfNotImprovedIn +
                ", algorithmName='" + this.getName() + '\'' +
                '}';
    }
}
