
package imat;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import se.chalmers.cse.dat216.project.*;

public class MainViewController implements Initializable {

    @FXML
    Label pathLabel;
    @FXML
    private FlowPane productGrid;
    @FXML
    private TextField searchBar;

    IMatDataHandler iMatDataHandler = IMatDataHandler.getInstance();
    ShoppingCart shoppingCart = iMatDataHandler.getShoppingCart();
    HashMap<Product, ProductItem> productMap = new HashMap<>();

    public void initialize(URL url, ResourceBundle rb) {


        int n = 0;
        List<Product> productList = iMatDataHandler.getProducts();
        for (Product product : productList) {
            ProductItem productItem = new ProductItem(product, iMatDataHandler);
            productMap.put(product, productItem);
            productGrid.getChildren().add(productItem);
        }


        shoppingCart.addShoppingCartListener(new ShoppingCartListener() {
            @Override
            public void shoppingCartChanged(CartEvent cartEvent) {updateAmountLabels();}
        });


    }
    public void updateSearchResult() {
        List<Product> searchResult = iMatDataHandler.findProducts(searchBar.getText());
        populateGrid(searchResult);
    }

    public void populateGrid(List<Product> productList) {
        productGrid.getChildren().clear();
        for (Product p: productList) {
            productGrid.getChildren().add(productMap.get(p));
        }
    }

    public void updateAmountLabels() {
        for (ProductItem productItem: productMap.values()) {
            productItem.resetAmount();
        }
        List<ShoppingItem> shoppingCartList = iMatDataHandler.getShoppingCart().getItems();
        for (ShoppingItem shoppingItem: shoppingCartList) {
            Product product = shoppingItem.getProduct();
            ProductItem productItem = productMap.get(product);
            productItem.setAmount(shoppingItem.getAmount());
        }
    }

}