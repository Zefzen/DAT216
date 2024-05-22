package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingCart;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class ReceiptDetailListItem extends AnchorPane {

    private final ShoppingCart shoppingCart;
    private IMatDataHandler controller;
    private Product product;
    private ShoppingItem shoppingItem;
    @FXML private Label receipeProductTitle;
    @FXML private ImageView receipeProductImage;
    @FXML private Label receipePriceLabel;
    @FXML private Label receipeAmountLabel;
    @FXML private Label receipeItemTotalLabel;


    public ReceiptDetailListItem(Product product, IMatDataHandler controller){
        this.product = product;
        this.controller = controller;
        this.shoppingCart = controller.getShoppingCart();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ReceiptDetailListItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
//
        this.receipeProductTitle.setText(product.getName());
        this.receipeProductImage.setImage(controller.getFXImage(product));
        this.receipePriceLabel.setText(String.valueOf(product.getPrice()));

    }


    public void setAmount(double amount) {
        receipeAmountLabel.setText(String.valueOf((int) amount));
        receipeItemTotalLabel.setText(String.format ("%.2f",amount*product.getPrice()));
    }


}
