package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Object Class for Inventory
 *
 * #######################################################
 * ### Grading Rubric (G: JavaDoc: Compatible Feature) ###
 * #######################################################
 * Future version of this application, the Inventory
 * class will extend the mySQL class to including saving
 * reading from a database for persistent data storage.
 *
 * @author Andrew Burk
 */

public class Inventory {

    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * @param newPart The Part to add
     */
    public void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     * @param newProduct The Product to add
     */
    public void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     * @param partId the part id to lookup
     * @return the Part object
     */
    public Part lookupPart(int partId){
        if (!allParts.isEmpty()) {
            for (int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getId() == partId) {
                    return allParts.get(i);
                }
            }
        }
        return null;
    }
    public ObservableList<Part> lookupPart(String partName) {
        if (!allParts.isEmpty()) {
            ObservableList searchPartsList = FXCollections.observableArrayList();
            for (Part p : getAllParts()) {
                if (p.getName().contains(partName)) {
                    searchPartsList.add(p);
                }
            }
            return searchPartsList;
        }
        return null;
    }

    /**
     * @param productId the product id to lookup
     * @return the Product object
     */
    public Product lookupProduct(int productId){
        if (!allProducts.isEmpty()) {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getId() == productId) {
                    return allProducts.get(i);
                }
            }
        }
        return null;
    }
    public ObservableList<Product> lookupProduct(String productName) {
        if (!allProducts.isEmpty()) {
            ObservableList searchProductList = FXCollections.observableArrayList();
            for (Product p : getAllProducts()) {
                if (p.getName().contains(productName)) {
                    searchProductList.add(p);
                }
            }
            return searchProductList;
        }
        return null;
    }

    /**
     * @param index part list index to update
     * @param selectedPart part object to update to
     */
    public void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    /**
     * @param index product list index to update
     * @param selectedProduct product object to update to
     */
    public void updateProduct(int index, Product selectedProduct){
        allProducts.set(index, selectedProduct);
    }

    /**
     * @param selectedPart part object to remove from part list
     * @    return true or false if successful
     */
    public boolean deletePart(Part selectedPart) {
        if (selectedPart == null)
            return false;
        allParts.remove(selectedPart);
        return true;
    }

    /**
     * @param selectedProduct product object to remove from product list
     * @return true or false if successful
     */
    public boolean deleteProduct(Product selectedProduct) {
        if (selectedProduct == null)
            return false;
        allProducts.remove(selectedProduct);
        return true;
    }

    /**
     * @return list of all parts
     */
    public ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * @return list of all products
     */
    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
