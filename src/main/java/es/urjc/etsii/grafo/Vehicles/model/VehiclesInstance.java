package es.urjc.etsii.grafo.Vehicles.model;

import es.urjc.etsii.grafo.io.Instance;
import es.urjc.etsii.grafo.util.CollectionUtil;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class VehiclesInstance extends Instance {

    private final int i, j, seed;
    private final int[] gamma;
    private final double[] cost;
    private final double u0;
    private final double[] bp;
    private final double[][] customers, facilities;
    String nombre;
    private double[][] pis;
    private ArrayList<Integer>[] searchOrder;
    private int[][] searchOrderImproved;
    private int[][] searchOrderImprovedIndex;

    public VehiclesInstance(String filename, int i, int j, double[][] customers, double[][] facilities, double[] cost, int[] gamma, double[] bp, double u0, int seed) {
        super(filename);
        this.nombre = filename;
        this.i = i;
        this.j = j;
        this.customers = customers;
        this.facilities = facilities;
        this.cost = cost;
        this.gamma = gamma;
        this.seed = seed;
        this.u0 = u0;
        this.bp = bp;
        generateHelpers(null);
    }

    public VehiclesInstance(String filename, int i, int j, double[][] pi, double[] cost, int[] gamma, double[] bp, double u0, int seed) {
        super(filename);
        this.nombre = filename;
        this.i = i;
        this.j = j;
        this.customers = null;
        this.facilities = null;
        this.cost = cost;
        this.gamma = gamma;
        this.seed = seed;
        this.u0 = u0;
        this.bp = bp;
        generateHelpers(pi);
    }

    protected void generateHelpers(double[][] pi) {
        if (pi == null) {
            pis = new double[i][j];
            // Calculate everything
            for (int i = 0; i < this.i; i++) {
                for (int j = 0; j < this.j; j++) {
                    piCalculate(i, j);
                }
            }
        } else {
            pis = pi;
        }
        var jotas = IntStream.range(0, this.j).boxed().toList();
        searchOrder = new ArrayList[this.i];
        searchOrderImproved = new int[this.i][this.j];
        searchOrderImprovedIndex = new int[this.i][this.j];
        for (int i = 0; i < this.i; i++) {
            // Sort by value
            int finalI = i;
            searchOrder[i] = new ArrayList<>(jotas);
            searchOrder[i].sort((a, b) -> Double.compare(pis[finalI][b], pis[finalI][a]));
            var index = 0;
            for (int i1 = 0; i1 < searchOrder[i].size(); i1++) {
                searchOrderImprovedIndex[i][searchOrder[i].get(i1)] = index++;
            }

            // Copy to the new way of working
            System.arraycopy(CollectionUtil.toIntArray(searchOrder[i]), 0, searchOrderImproved[i], 0, this.j);
        }
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public int getSeed() {
        return seed;
    }

    public double[][] getCustomers() {
        return customers;
    }

    public double[][] getFacilities() {
        return facilities;
    }

    public int[] getSearchOrderImproved(int i) {
        return searchOrderImproved[i];
    }

    public int[] getSearchOrderImprovedIndex(int i) {
        return searchOrderImprovedIndex[i];
    }


    public double pi(int i, int j) {
        return pis[i][j];
    }

    public void piCalculate(int i, int j) {
        pis[i][j] = this.u(i, j) / this.u(i, -1);
    }

    public double d(int i, int j) {
        var x1 = this.facilities[j][0];
        var y1 = this.facilities[j][1];
        var x2 = this.customers[i][0];
        var y2 = this.customers[i][1];
        var minusX = x2 - x1;
        var minusY = y2 - y1;
        return Math.sqrt(minusY * minusY + minusX * minusX);
    }

    public double A(int j) {
        // Always one: First paragraph, Numerical Experiments
        return 1;
    }

    public int gamma(int i) {
        return this.gamma[i];
    }

    public double b(int i) {
        return this.bp[i];
    }

    public double cost(int j) {
        return this.cost[j];
    }

    public double u(int i, int j) {
        if (j == -1) return this.u0;
        double distance;
        distance = this.d(i, j);
        distance = distance * distance;
        return A(j) / distance;
    }

    public String getNombre() {
        return nombre;
    }
}
