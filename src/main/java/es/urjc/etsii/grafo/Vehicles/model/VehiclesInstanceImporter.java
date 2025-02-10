package es.urjc.etsii.grafo.Vehicles.model;

import es.urjc.etsii.grafo.io.InstanceImporter;

import java.io.BufferedReader;
import java.io.IOException;

public class VehiclesInstanceImporter extends InstanceImporter<VehiclesInstance> {

    @Override
    public VehiclesInstance importInstance(BufferedReader reader, String filename) throws IOException {
        // Create and return an instance object from file data
        // TIP You may use a Scanner if you prefer it to a Buffered Reader:
        // IMPORTANT! Remember that instance data must be immutable from this point
        return readOwnFiles(reader, filename);
    }

    public VehiclesInstance readOwnFiles(BufferedReader reader, String filename) throws IOException {
        String line;
        int i = -1;
        int j = -1;
        int seed = -1;
        double[] scost = null;
        double u0 = -1;
        int[] gamma = null;
        double[] bp = null;
        double[][] pis = null;
        // Read but unused other fields
        // double[][] customers = null;
        // double[][] facilities = null;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("I:")) {
                i = Integer.parseInt(line.split(" ")[1]);
            } else if (line.startsWith("J:")) {
                j = Integer.parseInt(line.split(" ")[1]);
            } else if (line.startsWith("COSTS:")) {
                scost = readDoubleArray(reader, j);
            } else if (line.startsWith("GAMMAS:")) {
                gamma = readIntegerArray(reader, i);
            } else if (line.startsWith("SEED:")) {
                seed = Integer.parseInt(line.split(" ")[1]);
            } else if (line.startsWith("U0:")) {
                u0 = Double.parseDouble(line.split(" ")[1]);
//            } else if (line.startsWith("CUSTOMERS:")) {
//                var customersSize = i;
//                if (customersSize == -1) {
//                    throw new RuntimeException("Customers not available");
//                }
//                customers = readDoubleMatrix(reader, customersSize);
//            } else if (line.startsWith("FACILITIES:")) {
//                var facilitiesSize = j;
//                if (facilitiesSize == -1) {
//                    throw new RuntimeException("Facilities not available");
//                }
//                facilities = readDoubleMatrix(reader, facilitiesSize);
            } else if (line.startsWith("BUYING POWER")) {
                var bpSize = i;
                if (bpSize == -1) {
                    throw new RuntimeException("Buying power not available");
                }
                bp = readDoubleArray(reader, bpSize);
            } else if (line.startsWith("PI")) {
                pis = readDoubleMatrix(reader, i, j);
            }
        }

        return new VehiclesInstance(filename, i, j, pis, scost, gamma, bp, u0, seed);
    }

    private double[][] readDoubleMatrix(BufferedReader reader, int size) throws IOException {
        return readDoubleMatrix(reader, size, 2);
    }

    private double[][] readDoubleMatrix(BufferedReader reader, int xsize, int ysize) throws IOException {
        double[][] out = new double[xsize][ysize];
        for (int i1 = 0; i1 < xsize; i1++) {
            var newLine = reader.readLine().split(" ");
            for (int i2 = 0; i2 < ysize; i2++) {
                var x = Double.parseDouble(newLine[i2]);
                out[i1][i2] = x;
            }
        }
        return out;
    }

    private int[] readIntegerArray(BufferedReader reader, int size) throws IOException {
        int[] out = new int[size];
        for (int i1 = 0; i1 < size; i1++) {
            var newLine = reader.readLine();
            out[i1] = Integer.parseInt(newLine);
        }
        return out;
    }

    private double[] readDoubleArray(BufferedReader reader, int size) throws IOException {
        double[] out = new double[size];
        for (int i1 = 0; i1 < size; i1++) {
            var newLine = reader.readLine();
            out[i1] = Double.parseDouble(newLine);
        }
        return out;
    }
}
