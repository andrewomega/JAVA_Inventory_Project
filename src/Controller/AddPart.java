package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Add Part Controller Class
 * This controller handles the processing and functionality of the "Add Part" stage.
 * @author Andrew Burk
 */

public class AddPart {
    public TextField partIdentifier;
    public TextField partMax;
    public TextField partMin;
    public TextField partCost;
    public TextField partInv;
    public TextField partName;
    public Label partIdentifierLabel;
    public TextField partID;
    public RadioButton toggleInHouse;
    public RadioButton toggleOutsourced;
    private Inventory inv;

    /**
     * This function is called when the user clicks the "Cancel" button.
     * This will close the "Add Part" stage and reopen the "Main Screen" stage.
     * @param actionEvent Event object passed by JavaFX
     */
    public void buttonClickCancel(ActionEvent actionEvent) {
        openMainScreen(actionEvent);
    }

    /**
     * This function is called when the user clicks the "Save" button.
     * This will check the text boxes for valid input from user and alert user if any errors are found.
     * This will then save the part to the Inventory Object.
     * This will close the "Add Part" stage and reopen the "Main Screen" stage.
     * @param actionEvent Event object passed by JavaFX
     */
    public void buttonClickSave(ActionEvent actionEvent) {
        //Error checking
        String errorMessage = "";
        if (partName.getText().trim().isEmpty()) {
            errorMessage = "Must include Part Name";
        } else if (!partInv.getText().trim().matches("\\d+")){
            errorMessage = "Stock must be greater than 0";
        } else if (!partCost.getText().trim().matches("\\d+(\\.\\d+)?")){
            errorMessage = "Must Enter a Cost";
        } else if (!partMax.getText().trim().matches("\\d+")) {
            errorMessage = "Maximum must be greater than 0";
        } else if (!partMin.getText().trim().matches("\\d+")){
            errorMessage = "Minimum must be greater than 0";
        } else if (Integer.parseInt(partMax.getText().trim()) < Integer.parseInt(partMin.getText().trim())) {
            errorMessage = "Maximum must be greater than Minimum";
        } else if (Integer.parseInt(partInv.getText().trim()) > Integer.parseInt(partMax.getText().trim())) {
            errorMessage = "Inventory Level Must Be Less Than Max.";
        } else if (Integer.parseInt(partInv.getText().trim()) < Integer.parseInt(partMin.getText().trim())) {
            errorMessage = "Inventory Level Must Be More Than Min.";
        } else if (toggleOutsourced.isSelected() && partIdentifier.getText().trim().isEmpty()) {
            errorMessage = "Must Enter a Company Name";
        } else if (toggleInHouse.isSelected() && !partIdentifier.getText().trim().matches("\\d+")) {
            errorMessage = "Invalid Machine Id";
        }

        if (errorMessage.contentEquals("")) {
            //No Errors was found.
            if (toggleInHouse.isSelected()) {
                Part newPart = new InHouse(generatePartId(),partName.getText().trim(),Double.parseDouble(partCost.getText().trim()),Integer.parseInt(partInv.getText().trim()),Integer.parseInt(partMin.getText().trim()),Integer.parseInt(partMax.getText().trim()),Integer.parseInt(partIdentifier.getText().trim()));
                inv.addPart(newPart);
            } else {
                Part newPart = new Outsourced(generatePartId(),partName.getText().trim(),Double.parseDouble(partCost.getText().trim()),Integer.parseInt(partInv.getText().trim()),Integer.parseInt(partMin.getText().trim()),Integer.parseInt(partMax.getText().trim()),partIdentifier.getText().trim());
                inv.addPart(newPart);
            }
            openMainScreen(actionEvent);
        } else {
            //Error was found - Display Error Dialog Box
            Alert alert = new Alert(Alert.AlertType.ERROR,errorMessage);
            alert.showAndWait();
        }
    }

    /**
     * This will close the "Add Part" stage and reopen the "Main Screen" stage.
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
     * @param inventoryObject Main Inventory Object.
     */
    public void passInInventory(Inventory inventoryObject){
        inv = inventoryObject;
    }

    /**
     * User Clicks the radio button, "InHouse."
     * Updates identifier label From Company Name to Machine Id.
     * @param actionEvent Event object passed by JavaFX.
     */
    public void userCheckedInHouse(ActionEvent actionEvent) {
        partIdentifierLabel.setText("Machine Id");
    }

    /**
     * User Clicks the radio button, "Outsourced."
     * Updates identifier label From Machine Id to Company Name.
     * @param actionEvent Event object passed by JavaFX.
     */
    public void userCheckedOutsourced(ActionEvent actionEvent) {
        partIdentifierLabel.setText("Company Name");
    }

    /**
     * Generates a new part id for new parts.
     * Finds the existing maximum part id then adds 1.
     *
     * *******************************
     *
     * #####################################################
     * ### Grading Rubric (G: JavaDoc: Documented Error) ###
     * #####################################################
     * Found and corrected a logical error (bug).
     * Part id was generating the same id as the maximum part
     * instead of a new part id.
     * Max Number was incrementing after return.
     * Changed maxNumber++ to ++maxNumber to correct this error.
     *
     * ********************************
     * @return New part id (int)
     */
    public int generatePartId(){
        ObservableList<Part> allParts = inv.getAllParts();
        int maxNumber = 0;
        for (Part myPart : allParts){
            if (maxNumber < myPart.getId()){
                maxNumber = myPart.getId();
            }
        }

        //Corrected maxNumber to increment before return.
        return ++maxNumber; //Increments before returns
    }
}
