package es.urjc.etsii.grafo.Vehicles.moves.changes;

public class ChangeRecords {
    public int changedCustomer;
    public int removedFacility;
    public int[] newFacilites;
    public double newPartialValue;

    public ChangeRecords(int changedCustomer, int removedFacility, int[] newFacilites, double newPartialValue) {
        this.changedCustomer = changedCustomer;
        this.removedFacility = removedFacility;
        this.newFacilites = newFacilites;
        this.newPartialValue = newPartialValue;
    }
}
