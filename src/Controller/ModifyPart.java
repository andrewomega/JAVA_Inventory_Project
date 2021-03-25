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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Modify Part Controller Class
 * This controller handles the processing and functionality of the "Modify Part" stage.
 * @author Andrew Burk
 */

public class ModifyPart {
    public TextField partID;
    public TextField partName;
    public TextField partInv;
    public TextField partCost;
    public TextField partMin;
    public TextField partMax;
    public TextField partIdentifier;
    public RadioButton toggleOutsourced;
    public RadioButton toggleInHouse;
    public Label partIdentifierLabel;
    private Inventory inv;
    private Part selectedPart;

    /**
     * This function is called when the user clicks the "Cancel" button.
     * This will close the "Modify Part" stage and reopen the "Main Screen" stage.
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
            //No Errors was found, okay to update part.

            //Locate selected part in Inventory Index.
            ObservableList<Part> allParts = inv.getAllParts();
            int arrayIndex = -1; // Declared outside of for loop, used to grab array for modification.

            for (int i = 0; i < allParts.size(); i++){
                if (allParts.get(i).equals(selectedPart)){
                    arrayIndex = i;
                }
            }

            if (toggleInHouse.isSelected()) {
                Part newPart = new InHouse(selectedPart.getId(),partName.getText().trim(),Double.parseDouble(partCost.getText().trim()),Integer.parseInt(partInv.getText().trim()),Integer.parseInt(partMin.getText().trim()),Integer.parseInt(partMax.getText().trim()),Integer.parseInt(partIdentifier.getText().trim()));
                inv.updatePart(arrayIndex,newPart);
            } else {
                Part newPart = new Outsourced(selectedPart.getId(),partName.getText().trim(),Double.parseDouble(partCost.getText().trim()),Integer.parseInt(partInv.getText().trim()),Integer.parseInt(partMin.getText().trim()),Integer.parseInt(partMax.getText().trim()),partIdentifier.getText().trim());
                inv.updatePart(arrayIndex,newPart);
            }
            openMainScreen(actionEvent);
        } else {
            //Error was found - Display Error Dialog Box
            Alert alert = new Alert(Alert.AlertType.ERROR,errorMessage);
            alert.showAndWait();
        }
    }

    /**
     * This will close the "Modift Part" stage and reopen the "Main Screen" stage.
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
     * This handles the passing in the Inventory inv object from the Main Controller.
     * This also, passes in the selected part object used to modify.
     * @param inventoryObject Main Inventory Object
     * @param selectedPart Part Object that is being manipulated and updated.
     */
    public void passInInventory(Inventory inventoryObject, Part selectedPart){
        inv = inventoryObject;
        this.selectedPart = selectedPart;
        populateFields(selectedPart);
    }

    /**
     * Populated the GUI text fields.
     * @param selectedPart
     */
    private void populateFields(Part selectedPart){
        if (selectedPart instanceof Outsourced) {
            Outsourced oPart = (Outsourced) selectedPart;
            partIdentifier.setText(oPart.getCompanyName());
            toggleOutsourced.setSelected(true);
            partIdentifierLabel.setText("Company Name");
        } else {
            InHouse iPart = (InHouse) selectedPart;
            partIdentifier.setText(Integer.toString(iPart.getMachineId()));
            toggleInHouse.setSelected(true);
            partIdentifierLabel.setText("Machine Id");
        }

        partID.setText(Integer.toString(selectedPart.getId()));
        partName.setText(selectedPart.getName());
        partInv.setText(Integer.toString(selectedPart.getStock()));
        partCost.setText(Double.toString(selectedPart.getPrice()));
        partMin.setText(Integer.toString(selectedPart.getMin()));
        partMax.setText(Integer.toString(selectedPart.getMax()));
        partInv.setText(Integer.toString(selectedPart.getStock()));

    }

    /**
     * User Clicks the radio button, "InHouse."
     * Updates identifier label From Company Name to Machine Id.
     * @param actionEvent Event object passed by JavaFX
     */
    public void userCheckedInHouse(ActionEvent actionEvent) {
        partIdentifierLabel.setText("Machine Id");
    }

    /**
     * User Clicks the radio button, "Outsourced."
     * Updates identifier label From Machine Id to Company Name.
     * @param actionEvent Event object passed by JavaFX
     */
    public void userCheckedOutsourced(ActionEvent actionEvent) {
        partIdentifierLabel.setText("Company Name");
    }
}
