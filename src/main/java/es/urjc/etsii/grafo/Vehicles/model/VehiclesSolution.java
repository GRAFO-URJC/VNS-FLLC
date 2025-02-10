package es.urjc.etsii.grafo.Vehicles.model;

import es.urjc.etsii.grafo.Vehicles.moves.AddWithoutRefactor;
import es.urjc.etsii.grafo.Vehicles.moves.Drop;
import es.urjc.etsii.grafo.Vehicles.moves.changes.ChangeRecords;
import es.urjc.etsii.grafo.solution.Solution;

import java.util.*;

public class VehiclesSolution extends Solution<VehiclesSolution, VehiclesInstance> {

    public int[][] selectedFacilitesPerConsumers;
    public HashSet<Integer>[] selectedCustomersPerFacility;
    /**
     * Initialize solution from instance
     *
     * @param ins
     */
    boolean[] x;
    HashSet<Integer> xSet;
    double[] partialSum;
    // Score values
    double score = Double.MAX_VALUE;

    public VehiclesSolution(VehiclesInstance ins) {
        this(ins, false);
    }

    public VehiclesSolution(VehiclesInstance ins, boolean fast) {
        super(ins);
        // Initialize data structures if necessary
        generateBasicData(ins, fast);
        score = Double.MAX_VALUE;
        if (!fast) {
            Arrays.fill(this.x, false);
            Arrays.fill(this.partialSum, 0);
        }
    }

    /**
     * Clone constructor
     *
     * @param s Solution to clone
     */
    public VehiclesSolution(VehiclesSolution s) {
        super(s);
        generateBasicData(this.getInstance(), false);
        System.arraycopy(s.x, 0, this.x, 0, s.x.length);
        System.arraycopy(s.partialSum, 0, this.partialSum, 0, s.partialSum.length);

        for (int i = 0; i < selectedFacilitesPerConsumers.length; i++) {
            System.arraycopy(s.selectedFacilitesPerConsumers[i], 0, this.selectedFacilitesPerConsumers[i], 0, s.selectedFacilitesPerConsumers[i].length);
        }

        for (int j = 0; j < selectedCustomersPerFacility.length; j++) {
            this.selectedCustomersPerFacility[j].addAll(s.selectedCustomersPerFacility[j]);
        }

        this.xSet.addAll(s.xSet);

        this.score = s.score;
    }

    protected void generateBasicData(VehiclesInstance ins, boolean fast) {
        x = new boolean[ins.getJ()];
        partialSum = new double[ins.getI()];
        selectedFacilitesPerConsumers = new int[ins.getI()][];
        for (int i = 0; i < ins.getI(); i++) {
            selectedFacilitesPerConsumers[i] = new int[ins.gamma(i)];
        }

        selectedCustomersPerFacility = new HashSet[ins.getJ()];
        for (int i = 0; i < ins.getJ(); i++) {
            selectedCustomersPerFacility[i] = new HashSet<Integer>(ins.getI());
        }
        xSet = new HashSet<>(ins.getJ());
    }

    public boolean[] getX() {
        return x;
    }

    public boolean setX(int j) {
        if (this.x[j]) {
            return false;
        }
        // TODO: calcular el cambio
        this.x[j] = true;
        this.recalculateScore();
        return true;
    }


    public boolean setX(boolean[] js) {
        System.arraycopy(js, 0, this.x, 0, this.x.length);
        // TODO: calcular el cambio
        this.recalculateScore();
        return true;
    }

    public boolean clearX(int j) {
        if (!this.x[j]) {
            return false;
        }
        // TODO: calcular el cambio
        this.x[j] = false;
        this.recalculateScore();
        return true;
    }

    public boolean containsX(int j) {
        return this.x[j];
    }

    @Override
    public VehiclesSolution cloneSolution() {
        // You do not need to modify this method
        // Call clone constructor
        return new VehiclesSolution(this);
    }

    @Override
    protected boolean _isBetterThan(VehiclesSolution other) {
        // TODO given two solutions, is the current solution STRICTLY better than the other?
//        return this.getScore() < other.getScore();
        return this.getScore() > other.getScore();
    }

    public double setScore(double newScore) {
        var aux = this.score;
        this.score = newScore;
        return aux;
    }

    /**
     * Get the current solution score.
     * The difference between this method and recalculateScore is that
     * this result can be a property of the solution, or cached,
     * it does not have to be calculated each time this method is called
     *
     * @return current solution score as double
     */
    @Override
    public double getScore() {
        // Can be as simple as a score property that gets updated when the solution changes
        // Example: return this.score;
        // Another ok start implementation can be: return recalculateScore();

        // TODO: primera parte ignoramos todo
        if (this.score == Double.MAX_VALUE) throw new RuntimeException("No score value provided");
        return this.score;
    }

    /**
     * Recalculate solution score and validate current solution state
     * You must check that no constraints are broken, and that all costs are valid
     * The difference between this method and getScore is that we must recalculate the score from scratch,
     * without using any cache/shortcuts.
     * DO NOT UPDATE CACHES / MAKE SURE THIS METHOD DOES NOT HAVE SIDE EFFECTS
     *
     * @return current solution score as double
     */
    @Override
    public double recalculateScore() {
        // TODO calculate solution score from scratch, without using caches
        //  and without modifying the current solution. Careful with side effects.
        var instance = this.getInstance();

        double costs = 0;
        this.xSet = new HashSet<>(instance.getJ());
        for (int j = 0; j < this.x.length; j++) {
            if (this.containsX(j)) {
                this.xSet.add(j);
                costs = costs + instance.cost(j);
            }
        }

        // Reset customers Facility
        for (int i = 0; i < instance.getJ(); i++) {
            selectedCustomersPerFacility[i] = new HashSet<Integer>(instance.getI());
            // TODO: this could help
            // selectedCustomersPerFacility[i].clear();
        }


        double improvements = 0;
        for (int i = 0; i < this.getInstance().getI(); i++) {
            improvements += calcularDivisonMejoresMax(i);
        }
        this.score = improvements - costs;
        return this.score;
    }

    public double calcularDivisonMejoresMax(int i) {
        var instance = getInstance();
        var gamma = instance.gamma(i);
        var added = 0;
        var list = instance.getSearchOrderImproved(i);
        var listIndex = 0;
        int j;

        // Calculation
        double sumj = 0;
        do {
            // Add only the best
            j = list[listIndex++];
            if (this.containsX(j)) {
                sumj += instance.pi(i, j);
                selectedFacilitesPerConsumers[i][added] = j;
                selectedCustomersPerFacility[j].add(i);
                added++;
            }

        } while (added < gamma && listIndex < list.length);

        // Reset facilities
        Arrays.fill(selectedFacilitesPerConsumers[i], added, selectedFacilitesPerConsumers[i].length, -1);

//        partialSum[i] = sumj;
        var globalsum = instance.b(i) * ((sumj) / (sumj + 1));
        partialSum[i] = globalsum;
        return globalsum;
    }

    /**
     * Generate a string representation of this solution. Used when printing progress to console,
     * show as minimal info as possible
     *
     * @return Small string representing the current solution (Example: id + score)
     */
    @Override
    public String toString() {
        return "VehiclesSolution{" + "x=" + Arrays.toString(x) + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehiclesSolution that = (VehiclesSolution) o;
        //TODO: review
        return Double.compare(that.score, score) == 0 && Arrays.equals(x, that.x);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(x), score);
    }

    public Drop removeLocation(int j) {
        return removeLocationParam(j, 2);
    }

    public Drop removeLocationParam(int j, double divider) {
        var newPartialSum = new double[this.partialSum.length];
        var partialDifferences = 0d;
        var oldValue = 0d;
        ChangeRecords newValue;
        var diff = 0d;
        var changesList = new ArrayList<ChangeRecords>(this.selectedFacilitesPerConsumers[j].length);
        for (Integer customer : this.selectedCustomersPerFacility[j]) {
            oldValue = this.partialSum[customer];
            newValue = calculateMaxRemovingWithoutChanges(customer, j, divider);
            changesList.add(newValue);
            diff = newValue.newPartialValue - oldValue;
            newPartialSum[customer] = newValue.newPartialValue;
            partialDifferences += diff;
        }
        // Check solution
        var newScore = this.score + this.getInstance().cost(j) + partialDifferences;


        // Generate a new movement with the changes
        return new Drop(this, j, newScore, changesList);

    }

    private ChangeRecords calculateMaxRemovingWithoutChanges(int i, int j, double divider) {
        var instance = getInstance();
        var gamma = instance.gamma(i);

        var facilities = this.selectedFacilitesPerConsumers[i];
        var newFacilites = new int[gamma]; // Could also be gamma

        double sumj = 0;
        int offset = 0;
        int facilityIndex = 0;
        int lastPositon = -1;
        var added = 0;

        // Check the actual information
        var order = instance.getSearchOrderImprovedIndex(i);
        for (; facilityIndex + offset < facilities.length; facilityIndex++) {
            var givenFacility = facilities[facilityIndex + offset];
            if (givenFacility == -1) {
                // Copy if -1
                newFacilites[facilityIndex] = -1;
            } else if (givenFacility == j) {
                // Remove and the for will copy the others
                // TODO: check the bug
                offset = 1;
                facilityIndex--;
                lastPositon = order[givenFacility];
            } else {
                // Copy the value and set added
                newFacilites[facilityIndex] = givenFacility;
                lastPositon = order[givenFacility];
                added++;
                sumj += instance.pi(i, givenFacility);
            }
        }

        if (offset != 1) {
            throw new RuntimeException("Trying to remove a facility not in the user i");
        }

        // Set the last city to null, we have to find the next one
        newFacilites[facilityIndex] = -1;

        // Where to search
        int listIndex = lastPositon + 1;
        int listSize = instance.getJ() - listIndex;
        int xSetSize = this.xSet.size();

        if (added < xSetSize) {
            if (divider == 0 || (listSize / divider) > xSet.size()) {
                // this branch will search for the best facility inside the open facilities
                var missing = gamma - added;
                var bestFacilities = new int[missing];
                var bestIndex = new int[missing];
                for (int miss = 0; miss < missing; miss++) {
                    bestFacilities[miss] = -1;
                    bestIndex[miss] = Integer.MAX_VALUE;
                }
                int place;
                int index;

                // Search for the best facilities
                for (Integer facility : xSet) {
                    place = instance.getSearchOrderImprovedIndex(i)[facility];
                    if (place <= lastPositon) {
                        continue;
                    }
                    index = 0;
                    while (index < missing && place > bestIndex[index]) {
                        index++;
                    }
                    for (int i1 = missing - 1; i1 > index; i1--) {
                        bestFacilities[i1 - 1] = bestFacilities[i1];
                        bestIndex[i1 - 1] = bestIndex[i1];

                    }
                    if (index < missing) {
                        bestFacilities[index] = facility;
                        bestIndex[index] = place;
                    }
                }

                for (int bestFacilityIndex = 0; bestFacilityIndex < bestFacilities.length; bestFacilityIndex++) {
                    int bestFacility = bestFacilities[bestFacilityIndex];
                    if (bestFacility != -1) {
                        sumj += instance.pi(i, bestFacility);
                        newFacilites[added] = bestFacility;
                        added++;
                    }
                }
            } else {
                int innerJ;
                int[] list = instance.getSearchOrderImproved(i);

                // Calculation
                do {
                    // Add only the best
                    if (listIndex == list.length) {
                        continue;
                    }
                    innerJ = list[listIndex++];
                    if (this.containsX(innerJ)) {
                        sumj += instance.pi(i, innerJ);
                        newFacilites[added] = innerJ;
                        added++;
                    }

                } while (added < gamma && listIndex < list.length && added < xSetSize);
            }
        }
        return new ChangeRecords(i, j, newFacilites, instance.b(i) * ((sumj) / (sumj + 1)));
    }

    public void applyRemoveLocation(int j, List<ChangeRecords> changes, double score) {
        this.score = score;

        for (ChangeRecords change : changes) {
            var i = change.changedCustomer;
            // Set partial sum
            this.partialSum[i] = change.newPartialValue;
            this.selectedFacilitesPerConsumers[i] = change.newFacilites;

            // TODO: this can be improved more
            for (int newFacility : change.newFacilites) {
                if (newFacility != -1) {
                    this.selectedCustomersPerFacility[newFacility].add(i);
                }
            }
        }
        // Remove the actual list
        this.selectedCustomersPerFacility[j] = new HashSet<>(this.getInstance().getI());
        this.x[j] = false;
        this.xSet.remove(j);
    }

    public void applyAddLocation(int j, List<ChangeRecords> changes, double score) {
        this.score = score;

        for (ChangeRecords change : changes) {
            var i = change.changedCustomer;
            this.partialSum[i] = change.newPartialValue;
            var lastFacilityBefore = this.selectedFacilitesPerConsumers[i][this.selectedFacilitesPerConsumers[i].length - 1];
            this.selectedFacilitesPerConsumers[i] = change.newFacilites;
            // Remove fromt the facility the latest one
            if (lastFacilityBefore != -1) {
                this.selectedCustomersPerFacility[lastFacilityBefore].remove(i);
            }
            this.selectedCustomersPerFacility[change.removedFacility].add(i);
        }
        this.x[j] = true;
        this.xSet.add(j);
    }


    public AddWithoutRefactor addLocation(int j) {
        var newPartialSum = new double[this.partialSum.length];
        var partialDifferences = 0d;
        var oldValue = 0d;
        Optional<ChangeRecords> newValueOpt;
        ChangeRecords newValue;
        var diff = 0d;
        var changesList = new ArrayList<ChangeRecords>(this.selectedFacilitesPerConsumers[j].length);
        for (int customer = 0; customer < this.getInstance().getI(); customer++) {
            oldValue = this.partialSum[customer];
            newValueOpt = calculateMaxAddingWithoutChanges(customer, j);
            if (newValueOpt.isPresent()) {
                newValue = newValueOpt.get();
                changesList.add(newValue);
                diff = newValue.newPartialValue - oldValue;
                newPartialSum[customer] = newValue.newPartialValue;
                partialDifferences += diff;
            }
        }
        // Check the solution
        var newScore = this.score - this.getInstance().cost(j) + partialDifferences;

        // Generate a new movement with the changes
        return new AddWithoutRefactor(this, j, newScore, changesList);

    }

    private Optional<ChangeRecords> calculateMaxAddingWithoutChanges(int i, int j) {
        var instance = getInstance();

        var facilities = this.selectedFacilitesPerConsumers[i];
        var positions = instance.getSearchOrderImprovedIndex(i);
        int newFacilityPosition = positions[j];

        // If is not part of the change
        if (facilities[facilities.length - 1] != -1 && newFacilityPosition > positions[facilities[facilities.length - 1]]) {
            // Skip if this does not change, if it is worse than the last one
            return Optional.empty();
        }

        // Part of the solution, so we have to re-arrange the solution
        int offset = 0;
        int facilityIndex = 0;
        double sumj = 0;
        int givenFacilityPosition = -1;
        boolean added = false;
        var newFacilites = new int[instance.gamma(i)]; // Output
        for (; facilityIndex < facilities.length; facilityIndex++) {
            var givenFacility = facilities[facilityIndex + offset];

            // Get the actual facility at that place
            if (givenFacility != -1) {
                givenFacilityPosition = positions[givenFacility];
            }

            if (givenFacility == -1) { // No facility, so should be added or set to -1
                if (!added) {
                    added = true;
                    newFacilites[facilityIndex] = j;
                    sumj += instance.pi(i, j);
                } else {
                    newFacilites[facilityIndex] = -1;
                }
            } else if (givenFacilityPosition < newFacilityPosition || added) { // Already added or the actualone is better
                newFacilites[facilityIndex] = givenFacility;
                sumj += instance.pi(i, givenFacility);
            } else { // Not added, and we are better than the one at that place
                added = true;
                newFacilites[facilityIndex] = j;
                sumj += instance.pi(i, j);
                offset = -1;
            }
        }

        if (!added) {
            // We should never get this case, because we have checked before that we will add it.
            return Optional.empty();
        }
        return Optional.of(new ChangeRecords(i, j, newFacilites, instance.b(i) * ((sumj) / (sumj + 1))));
    }

    public HashSet<Integer> getXSet() {
        return xSet;
    }
}