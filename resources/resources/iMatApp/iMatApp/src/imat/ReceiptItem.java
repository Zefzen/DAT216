package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.*;

import java.io.IOException;

public class ReceiptItem extends AnchorPane {

    private final ShoppingCart shoppingCart;
    private IMatDataHandler controller;
    private Order order;
    @FXML private Label receiptDateLabel;
    @FXML private Label receiptPriceLabel;
    @FXML private AnchorPane receiptAnchorPane;


    public ReceiptItem(Order order, IMatDataHandler controller, MainViewController mainViewController){

        this.controller = controller;
        this.shoppingCart = controller.getShoppingCart();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ReceiptItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
//
        this.receiptDateLabel.setText(order.getDate().toString());
        double priceSum = 0;
        for (ShoppingItem si: order.getItems()) {
            priceSum += si.getTotal();
        }
        this.receiptPriceLabel.setText(String.valueOf(priceSum) + " kr");
        this.receiptAnchorPane.setOnMouseClicked(EventHandler -> {
            mainViewController.populateReceiptDetailList(order);});


    }


}


