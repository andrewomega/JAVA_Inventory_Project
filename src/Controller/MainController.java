package Controller;

import Model.*;
import javafx.application.Platform;
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
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Main Screen Controller Class
 * This controller handles the processing and functionality of the "Main Screen" stage.
 * @author Andrew Burk
 */

public class MainController implements Initializable {
    public TextField searchPart;
    public TextField searchProduct;
    public TableColumn productTableProductID;
    public TableColumn productTableProductName;
    public TableColumn productTableInvLevel;
    public TableColumn productTableCost;
    public TableView listOfProducts;
    private Inventory inv;
    public TableView listOfParts;
    public TableColumn partTablePartID;
    public TableColumn partTablePartName;
    public TableColumn partTableInvLevel;
    public TableColumn partTableCost;

    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Product> productInventory = FXCollections.observableArrayList();

    /**
     * Overrides JavaFX initialize class.
     * Sets up the Part Table and Product Table between tableview and class.
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

        //Links FXML Product table to Product Class
        listOfProducts.setItems(productInventory);
        productTableProductID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productTableProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productTableInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productTableCost.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Sets Inventory Field passed in from other controllers or main loader.
     * This also calls the populate parts and product methods.
     * @param inventoryObject Main Inventory Object Passed in from Main Class.
     */
    public void passInInventory(Inventory inventoryObject){
        inv = inventoryObject;
        populatePartsTable();
        populateProductTable();
    }

    /**
     * Populated the parts inventory observable list which is linked to parts table view.
     * This will in turn populate the parts table.
     */
    private void populatePartsTable() {
        //Fills in the table with Inventory Data
        partInventory.setAll(inv.getAllParts());
    }

    /**
     * Populated the product inventory observable list which is linked to product table view.
     * This will in turn populate the product table.
     */
    private void populateProductTable() {
        //Fills in the table with Inventory Data
        productInventory.setAll(inv.getAllProducts());
    }

    /**
     Controller for Exit Button on Main Screen. Exits the program.
     */
    public void exitProgram(){
        Platform.exit();
    }

    /**
     * Main Screen Event: User clicks Add Part Button.
     * Closes main stage and opens the Add Part stage.
     * Then passes the Inventory object into the new stage.
     * @param actionEvent actionEvent passed in from JavaFX.
     */
    public void addPart(ActionEvent actionEvent) {
        try {
            FXMLLoader loader= new FXMLLoader(getClass().getResource("/Views/AddPart.fxml"));
            Parent root = loader.load();
            AddPart AddPartObject = loader.getController();
            AddPartObject.passInInventory(inv);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Main Screen Event: User clicks Modify Part Button.
     * Closes main stage and opens the modify part stage.
     * Then passes the Inventory object and Selected Part into the new stage.
     * @param actionEvent actionEvent passed in from JavaFX.
     */
    public void modifyPart(ActionEvent actionEvent) {
        Part selectedPart = (Part)listOfParts.getSelectionModel().getSelectedItem();
        if (selectedPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You must select a record to modify.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader= new FXMLLoader(getClass().getResource("/Views/ModifyPart.fxml"));
            Parent root = loader.load();
            ModifyPart ModifyPartObject = loader.getController();
            ModifyPartObject.passInInventory(inv,selectedPart);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Main Screen Event: User clicks Delete Part Button.
     * Confirms delete of product.
     * Removes object from Inventory Product List.
     * @param actionEvent actionEvent passed in from JavaFX.
     */
    public void deletePart(ActionEvent actionEvent) {
        Part selectedPart = (Part)listOfParts.getSelectionModel().getSelectedItem();
        if (selectedPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You must select a record to delete.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to delete this record?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            partInventory.remove(selectedPart);
            inv.deletePart(selectedPart);
        }
    }

    /**
     * Main Screen Event: User clicks Delete Product Button.
     * Confirms delete of product.
     * Removes object from Inventory Product List.
     * @param actionEvent actionEvent passed in from JavaFX.
     */
    public void deleteProduct(ActionEvent actionEvent) {
        Product selectedProduct = (Product) listOfProducts.getSelectionModel().getSelectedItem();
        if (selectedProduct == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You must select a record to delete.");
            alert.showAndWait();
            return;
        } else if (!selectedProduct.getAllAssociatedParts().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You cannot delete Products that has parts associated with it.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to delete this record?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            productInventory.remove(selectedProduct);
            inv.deleteProduct(selectedProduct);
        }
    }

    /**
     * Main Screen Event: User clicks Add Product Button.
     * Closes main stage and opens the add product stage.
     * Then passes the Inventory object into the new stage.
     * @param actionEvent actionEvent passed in from JavaFX.
     */
    public void addProduct(ActionEvent actionEvent) {
        try {
            FXMLLoader loader= new FXMLLoader(getClass().getResource("/Views/AddProduct.fxml"));
            Parent root = loader.load();
            AddProduct AddProductObject = loader.getController();
            AddProductObject.passInInventory(inv);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Main Screen Event: User clicks Modify Product Button.
     * Closes main stage and opens the modify product stage.
     * Then passes the Inventory object and Selected Product into the new stage.
     * @param actionEvent actionEvent passed in from JavaFX.
     */
    public void modifyProduct(ActionEvent actionEvent) {
            Product selectedProduct = (Product) listOfProducts.getSelectionModel().getSelectedItem();
            if (selectedProduct == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("You must select a record to modify.");
                alert.showAndWait();
                return;
            }

            try {
                FXMLLoader loader= new FXMLLoader(getClass().getResource("/Views/ModifyProduct.fxml"));
                Parent root = loader.load();
                ModifyProduct ModifyProductObject = loader.getController();
                ModifyProductObject.passInInventory(inv,selectedProduct);
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        }
     }

    /**
     * Main Screen Event: User types in SearchBox Products (Auto updates as user types)
     * @param keyEvent keyEvent object passed in from JavaFX
     */
    public void searchProduct(KeyEvent keyEvent) {
        if(searchProduct.getText().trim().isEmpty()){
            //Empty load default list
            populateProductTable();
        } else {
            //Is text Int or String?
            String searchText = searchProduct.getText().trim();
            try {
                int num=Integer.parseInt(searchText);
                productInventory.setAll(inv.lookupProduct(num));
            } catch(NumberFormatException ex) {
                productInventory.setAll(inv.lookupProduct(searchText));
            }
        }
    }

}
