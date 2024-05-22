package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingCart;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class CartListItem extends AnchorPane {

    private final ShoppingCart shoppingCart;
    private IMatDataHandler controller;
    private Product product;
    private ShoppingItem shoppingItem;
    @FXML private Label cartProductTitle;
    @FXML private ImageView cartProductImage;
    @FXML private Label cartProductPrice;
    @FXML private Button cartIncrementButton;
    @FXML private Button cartDecrementButton;
    @FXML private Label cartAmountLabel;


    public CartListItem(Product product, IMatDataHandler controller, MainViewController mainViewController){
        this.product = product;
        this.controller = controller;
        this.shoppingCart = controller.getShoppingCart();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CartListItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
//
        this.cartProductTitle.setText(product.getName());
        this.cartProductImage.setImage(controller.getFXImage(product));
        this.cartIncrementButton.setOnAction(event -> {increment();});
        this.cartDecrementButton.setOnAction(event -> {decrement(mainViewController);});
        this.cartProductPrice.setText((product.getPrice() )+" "+product.getUnit());


    }

    public void increment() {
        shoppingCart.addProduct(product, true);


    }
    public void decrement(MainViewController mainController) {
        if (shoppingCart.getItems().isEmpty()) {return;}
        boolean removeItem = false;
        for (ShoppingItem si : shoppingCart.getItems()) {
            if (si.getProduct().equals(product)) {
                if (si.getAmount() > 1) {
                    Double amount = si.getAmount() - 1;
                    si.setAmount(amount);
                    shoppingCart.fireShoppingCartChanged(si, true);
                }
                else if (si.getAmount() == 1) {
                     removeItem= true;

                }
            }
        }
        if (removeItem) {shoppingCart.removeProduct(product);}

    }

    public void resetAmount() {
        cartAmountLabel.setText(String.valueOf(0));
    }
    public void setAmount(double amount) {
        cartAmountLabel.setText(String.valueOf((int) amount));
    }


}
