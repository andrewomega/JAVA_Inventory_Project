package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Add Product Controller Class
 * This controller handles the processing and functionality of the "Add Product" stage.
 * @author Andrew Burk
 */

public class AddProduct implements Initializable {
    public TextField searchPart;
    public TableColumn partTablePartID;
    public TableColumn partTablePartName;
    public TableColumn partTableInvLevel;
    public TableColumn partTableCost;
    public TableView listOfParts;
    public TableColumn associatedPartTablePartID;
    public TableColumn associatedPartTablePartName;
    public TableColumn associatedPartTableInvLevel;
    public TableColumn associatedPartTableCost;
    public TableView listOfAssociatedParts;
    public TextField productName;
    public TextField productInv;
    public TextField productCost;
    public TextField productMax;
    public TextField productMin;
    private Inventory inv;
    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * Overrides JavaFX initialize class.
     * Sets up the Part Table and Associated Parts Table between tableview and class.
     * @param url Standard URL object received from JavaFX.
     * @param resourceBundle Standard ResourceBundle object received from JavaFX.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Links FXML Parts table to Part Class
        listOfParts.setItems(partInventory);
        partTablePartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partTablePartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partTableInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partTableCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Links FXML Associated Parts table to Part Class
        listOfAssociatedParts.setItems(associatedParts);
        associatedPartTablePartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartTablePartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartTableInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartTableCost.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * This will close the "Add Product" stage and reopen the "Main Screen" stage.
     * This will also pass the Inventory inv object back to Main Controller.
     * @param actionEvent Event object passed by JavaFX
     */
    public void openMainScreen(ActionEvent actionEvent) {
        try {
            FXMLLoader loader= new FXMLLoader(getClass().getResource("/Views/Main.fxml"));
            Parent root = loader.load();

            MainController mainControllerObject = loader.getController();
            mainControllerObject.passInInventory(inv);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {

        }
    }


    /**
     * This handles the passing the Inventory inv object from the Main Controller.
     * Sends a call to populate the Parts Tables.
     * @param inventoryObject Main Inventory Object.
     */
    public void passInInventory(Inventory inventoryObject){
        inv = inventoryObject;
        populatePartsTable();
    }

    /**
     * Populated the parts inventory observable list which is linked to parts table view.
     * This will in turn populate the parts table.
     */
    private void populatePartsTable() {
        //Fills in the table with Inventory Data
        partInventory.setAll(inv.getAllParts());
        partInventory = removeAssociatedPartsFromList(partInventory);
    }

    /**
     * Search Box Typing Event: User types in SearchBox Parts (Auto updates as user types)
     * @param keyEvent keyEvent object passed from JavaFX.
     */
    public void searchPart(KeyEvent keyEvent) {
        if(searchPart.getText().trim().isEmpty()){
            //Empty load default list
            populatePartsTable();
        } else {
            //Is text Int or String?
            String searchText = searchPart.getText().trim();
            try {
                int num=Integer.parseInt(searchText);
                partInventory.setAll(inv.lookupPart(num));
            } catch(NumberFormatException ex) {
                partInventory.setAll(inv.lookupPart(searchText));
            }
            partInventory = removeAssociatedPartsFromList(partInventory);
        }
    }

    /**
     * Filters part list to remove any parts that is associated with product.
     * @param list Part list to be filtered.
     * @returns Modified part list without associated parts already bind to product.
    */

    private ObservableList<Part> removeAssociatedPartsFromList(ObservableList<Part> list){
        for (Part iterationPart : associatedParts){
            list.remove(iterationPart);
        }
        return list;
    }

    /**
     * Called when the user clicks the "Add Part" button.
     * Will move part from part list to associated part list.
     * @param actionEvent Event object passed by JavaFX
     */
    public void addPart(ActionEvent actionEvent) {
        Part selectedPart = (Part)listOfParts.getSelectionModel().getSelectedItem();
        if (selectedPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You must select a part to add.");
            alert.showAndWait();
            return;
        }
        partInventory.remove(selectedPart);
        associatedParts.add(selectedPart);

    }

    /**
     * Called when the user clicks the "Remove associated Part" button.
     * Will move part from associated part list to part list.
     * @param actionEvent Event object passed by JavaFX
     */
    public void removeAssociatedPart(ActionEvent actionEvent) {
        Part selectedPart = (Part)listOfAssociatedParts.getSelectionModel().getSelectedItem();
        if (selectedPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You must select a part to remove.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to remove this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            partInventory.add(selectedPart);
            associatedParts.remove(selectedPart);
        }
    }

    /**
     * Used to generate the New Product ID.
     * Finds the maximum then adds 1.
     * @return int
     */
    public int generateProductId(){
        ObservableList<Product> allProducts = inv.getAllProducts();
        int maxNumber = 0;
        for (Product myProduct : allProducts){
            if (maxNumber < myProduct.getId()){
                maxNumber = myProduct.getId();
            }
        }
        return ++maxNumber; //Increments before returns
    }

    /**
     * This function is called when the user clicks the "Save" button.
     * This will check the text boxes for valid input from user and alert user if any errors are found.
     * This will then save the product to the Inventory Object, then adds all associated parts to object.
     * This will close the "Add Product" stage and reopen the "Main Screen" stage.
     * @param actionEvent Event object passed by JavaFX
     */
    public void buttonClickSave(ActionEvent actionEvent) {
        //Error checking
        String errorMessage = "";
        if (productName.getText().trim().isEmpty()) {
            errorMessage = "Must include Part Name";
        } else if (!productInv.getText().trim().matches("\\d+")){
            errorMessage = "Stock must be greater than 0";
        } else if (!productCost.getText().trim().matches("\\d+(\\.\\d+)?")){
            errorMessage = "Must Enter a Cost";
        } else if (!productMax.getText().trim().matches("\\d+")) {
            errorMessage = "Maximum must be greater than 0";
        } else if (!productMin.getText().trim().matches("\\d+")){
            errorMessage = "Minimum must be greater than 0";
        } else if (Integer.parseInt(productMax.getText().trim()) < Integer.parseInt(productMin.getText().trim())) {
            errorMessage = "Maximum must be greater than Minimum";
        } else if (Integer.parseInt(productInv.getText().trim()) > Integer.parseInt(productMax.getText().trim())) {
            errorMessage = "Inventory Level Must Be Less Than Max.";
        } else if (Integer.parseInt(productInv.getText().trim()) < Integer.parseInt(productMin.getText().trim())) {
            errorMessage = "Inventory Level Must Be More Than Min.";
        }

        if (errorMessage.contentEquals("")) {
            //No Errors was found.

            Product newProduct = new Product(generateProductId(),productName.getText().trim(),Double.parseDouble(productCost.getText().trim()),Integer.parseInt(productInv.getText().trim()),Integer.parseInt(productMin.getText().trim()),Integer.parseInt(productMax.getText().trim()));
            inv.addProduct(newProduct);
            for (Part myPart : associatedParts) {
                newProduct.addAssociatedPart(myPart);
            }
            openMainScreen(actionEvent);
        } else {
            //Error was found - Display Error Dialog Box
            Alert alert = new Alert(Alert.AlertType.ERROR,errorMessage);
            alert.showAndWait();
        }
    }
}
