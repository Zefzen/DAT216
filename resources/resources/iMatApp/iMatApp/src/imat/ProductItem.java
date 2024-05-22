package imat;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingCart;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class ProductItem extends AnchorPane {

    private final ShoppingCart shoppingCart;
    private double amount;
    private IMatDataHandler controller;
    private Product product;
    private ShoppingItem shoppingItem;
    @FXML private Label productTitle;
    @FXML private ImageView productImage;
    @FXML private Label priceTitle;
    @FXML private Button incrementButton;
    @FXML private Button decrementButton;
    @FXML private Button favoriteButton;
    @FXML private Label amountTitle;


    public ProductItem(Product product, IMatDataHandler controller, MainViewController mainViewController){
        this.amount = 0;
        this.product = product;
        this.controller = controller;
        this.shoppingCart = controller.getShoppingCart();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProductItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.productTitle.setText(product.getName());
        this.productImage.setImage(controller.getFXImage(product, 200, 137));
        this.productImage.setOnMouseClicked(EventHandler -> {mainViewController.populateDetailView(product);});
        this.incrementButton.setOnAction(event -> {increment();});
        this.decrementButton.setOnAction(event -> {decrement();});
        this.favoriteButton.setOnAction(event -> {mainViewController.toggleFavorite(product);});
        this.priceTitle.setText(String.valueOf(product.getPrice()) + " " + product.getUnit());

    }

    public void increment() {
        shoppingCart.addProduct(product, true);


    }
    public void decrement() {
        if (shoppingCart.getItems().isEmpty()) {return;}
        boolean removeItem = false;
        for (ShoppingItem si : shoppingCart.getItems()) {
            if (si.getProduct().equals(product)) {
                if (si.getAmount() > 1) {
                    Double amount = si.getAmount() - 1;
                    shoppingCart.removeProduct(product);
                    shoppingCart.addProduct(product, amount);
                }
                else if (si.getAmount() == 1) {
                     removeItem= true;

                }
            }
        }
        if (removeItem) {shoppingCart.removeProduct(product);}

    }
    public void setFavorite() {
        this.favoriteButton.setText("❤");
    }
    public void removeFavorite() {
            this.favoriteButton.setText("♡");}

    public void resetAmount() {
        amountTitle.setText(String.valueOf(0));
    }
    public void setAmount(double amount) {
        amountTitle.setText(String.valueOf((int) amount));
    }


}
