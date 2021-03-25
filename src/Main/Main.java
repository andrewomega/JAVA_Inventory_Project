package Main;

import Controller.MainController;
import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main Class for Inventory Project for WGU Inventory Project.
 * @author Andrew Burk
 */
public class Main extends Application {

    /**
     *  Stage function called my JavaFX, program starts here.
     *  @param primaryStage Primary Stage created and returned from JavaFX.
     *  @exception Exception Failed to load stage.
     */

      @Override
    public void start(Stage primaryStage) throws Exception{
        //Inventory Container
        Inventory inv = new Inventory();
        //Dummy Data
        Part test1 = new InHouse(1, "Part 1", 1.99, 18, 1, 100, 104);
        Part test2 = new InHouse(2, "Part 2", 2.99, 19, 1, 100, 105);
        Part test3 = new Outsourced(3, "Outsourced", 3.99, 10, 5, 100, "Vendor 1");
        inv.addPart(test1);
        inv.addPart(test2);
        inv.addPart(test3);
        Product prod1 = new Product(100, "Product 1", 9.99, 20, 5, 100);
        inv.addProduct(prod1);
        prod1.addAssociatedPart(test1);
        prod1.addAssociatedPart(test2);
        Product prod2 = new Product(200, "Product 2", 9.99, 29, 5, 100);
        inv.addProduct(prod2);
        prod2.addAssociatedPart(test1);
        prod2.addAssociatedPart(test2);
        prod2.addAssociatedPart(test3);
        Product prod3 = new Product(300, "Product 3", 9.99, 30, 5, 100);
        inv.addProduct(prod3);
        prod3.addAssociatedPart(test2);

        FXMLLoader loader= new FXMLLoader(getClass().getResource("/Views/Main.fxml"));
        Parent root = loader.load();

        MainController mainControllerObject = loader.getController();
        mainControllerObject.passInInventory(inv);

        primaryStage.setTitle("Inventory Management System - WGU Assessment - By Andrew Burk");
        primaryStage.setScene(new Scene(root,1040,490));
        primaryStage.show();
    }

    /**
     *  Main function to make call to JavaFX
     *  @param args String array of any parameters passed through from CLI.
     */

    public static void main(String[] args) {
        launch(args);
    }
}
