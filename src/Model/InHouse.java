package Model;

/**
 * Object Class for InHouse Object, Child of Part Class
 * @author Andrew Burk
 */

public class InHouse extends Part{
    private int machineId;

    /**
     * Constructor for InHouse - Called when new InHouse object is created.
     * @param id the id
     * @param name the name
     * @param price the price
     * @param stock the stock level
     * @param min the min stock allowed
     * @param max the max stock allowed
     * @param machineId the machine id
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId){
        super(id,name,price,stock,min,max);
        this.machineId = machineId;
    }

    /**
     * @param machineId the machine id to set
     */
    public void setMachineID(int machineId) {
        this.machineId = machineId;
    }

    /**
     * @return the machine id
     */
    public int getMachineId(){
        return machineId;
    }
}
