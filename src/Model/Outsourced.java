package Model;

/**
 * Object Class for Outsourced Object, Child of Part Class
 * @author Andrew Burk
 */
public class Outsourced extends Part{
    private String companyName;

    /**
     * Constructor for Outsourced - Called when new Outsourced object is created.
     * @param id the id
     * @param name the name
     * @param price the price
     * @param stock the stock level
     * @param min the min stock allowed
     * @param max the max stock allowed
     * @param companyName the company name
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName){
        super(id,name,price,stock,min,max);
        this.companyName = companyName;
    }

    /**
     * @param companyName the company name to set
     */
   public void setCompanyName(String companyName) {
        this.companyName = companyName;
   }

    /**
     * @return the company name
     */
   public String getCompanyName(){
        return this.companyName;
   }
}