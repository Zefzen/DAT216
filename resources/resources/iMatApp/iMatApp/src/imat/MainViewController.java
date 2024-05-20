
package imat;

import java.beans.EventHandler;
import java.net.URL;
import java.util.*;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import se.chalmers.cse.dat216.project.*;

public class MainViewController implements Initializable {

    @FXML
    Label pathLabel;
    @FXML
    private FlowPane productGrid;
    @FXML
    private TextField searchBar;
    @FXML
    private Accordion category;
    @FXML private AnchorPane productDetailView;
    @FXML private AnchorPane searchView;
    @FXML private Button detailCloseButton;
    @FXML private Button detailIncrementButton;
    @FXML private Button detailDecrementButton;
    @FXML private Button detailFavoriteButton;
    @FXML private Label detailAmount;
    @FXML private Label detailTitle;
    @FXML private Label detailPrice;
    @FXML private Label detailNumber;
    @FXML private Label detailOrigin;
    @FXML private Label detailBrand;
    @FXML private Label detailDescription;
    @FXML private ImageView detailImageView;
    @FXML private AnchorPane detailAnchorPane;

    IMatDataHandler iMatDataHandler = IMatDataHandler.getInstance();
    ShoppingCart shoppingCart = iMatDataHandler.getShoppingCart();
    HashMap<Product, ProductItem> productMap = new HashMap<>();
    List<List<String>> subCategories;


    public void initialize(URL url, ResourceBundle rb) {


        int n = 0;
        List<Product> productList = iMatDataHandler.getProducts();
        for (Product product : productList) {
            ProductItem productItem = new ProductItem(product, iMatDataHandler, this);
            productMap.put(product, productItem);
            productGrid.getChildren().add(productItem);
        }


        shoppingCart.addShoppingCartListener(new ShoppingCartListener() {
            @Override
            public void shoppingCartChanged(CartEvent cartEvent) {
                updateAmountLabels();
            }
        });


        this.subCategories = Arrays.asList(
                Arrays.asList("FRUIT",
                        "VEGETABLE_FRUIT",
                        "ROOT_VEGETABLE",
                        "CITRUS_FRUIT",
                        "EXOTIC_FRUIT",
                        "MELONS",
                        "CABBAGE",
                        "BERRY",
                        "HERB"),
                Arrays.asList("BREAD"),
                Arrays.asList("DAIRIES"),
                Arrays.asList("MEAT",
                        "FISH"),
                Arrays.asList("PASTA",
                        "POTATO_RICE",
                        "POD",
                        "NUTS_AND_SEEDS",
                        "FLOUR_SUGAR_SALT"),
                Arrays.asList("COLD_DRINKS",
                        "HOT_DRINKS"),
                Arrays.asList("SWEET")
        );
        ObservableList<TitledPane> panes = category.getPanes();
        for (int main = 0; main < 7; main++) {
            TitledPane pane = panes.get(main + 1);
            int finalMain = main;
            pane.setOnMouseClicked(mouseEvent -> clickMainCategory(finalMain));
            Node content = pane.getContent();
            if (content instanceof VBox) {
                VBox vBox = (VBox) content;
                List<Button> buttons = vBox.getChildren().stream()
                        .filter(node -> node instanceof Button)
                        .map(node -> (Button) node)
                        .toList();
                for (int sub = 0; sub < buttons.size(); sub++) {
                    int finalSub = sub;
                    buttons.get(sub).setOnAction(actionEvent -> {clickSubCategory(
                        subCategories.get(finalMain).get(finalSub));
                    });
                }
            }
        }

        panes.get(0).setOnMouseClicked(EventHandler -> {populateGrid(iMatDataHandler.favorites());});


        productDetailView.setOnMouseClicked(EventHandler -> {searchView.toFront();});  // Return when clicking on black border
        detailCloseButton.setOnMouseClicked(EventHandler -> {searchView.toFront();});
    }

    public void updateSearchResult() {
        List<Product> searchResult = iMatDataHandler.findProducts(searchBar.getText());
        populateGrid(searchResult);
    }

    public void populateGrid(List<Product> productList) {
        productGrid.getChildren().clear();
        for (Product p : productList) {
            productGrid.getChildren().add(productMap.get(p));
        }
    }

    public void updateAmountLabels() {
        for (ProductItem productItem : productMap.values()) {
            productItem.resetAmount();
        }
        List<ShoppingItem> shoppingCartList = iMatDataHandler.getShoppingCart().getItems();
        for (ShoppingItem shoppingItem : shoppingCartList) {
            Product product = shoppingItem.getProduct();
            ProductItem productItem = productMap.get(product);
            productItem.setAmount(shoppingItem.getAmount());
        }
    }

    public void clickMainCategory(int index) {
        List<Product> mainCategory = new ArrayList<>();
        List<String> subCategoryNames = subCategories.get(index);
        for (String name : subCategoryNames) {
            List<Product> tempCategory = iMatDataHandler.getProducts(ProductCategory.valueOf(name));
            mainCategory.addAll(tempCategory);
        }
        populateGrid(mainCategory);
    }


    public void clickSubCategory(String name) {
        populateGrid(iMatDataHandler.getProducts(ProductCategory.valueOf(name)));
    }

    public void populateDetailView(Product product) {
        productDetailView.toFront();
        detailTitle.setText(product.getName());
        detailImageView.setImage(iMatDataHandler.getFXImage(product));
        detailPrice.setText(product.getPrice() + " " + product.getUnit());
        detailNumber.setText("Product ID: " + product.getProductId());
        detailIncrementButton.setOnAction(EventHandler -> {shoppingCart.addProduct(product, true);
        updateDetailAmount(product);});
        detailDecrementButton.setOnAction(EventHandler -> {productMap.get(product).decrement();
        updateDetailAmount(product);});
        detailFavoriteButton.setOnAction(EventHandler -> {toggleFavorite(product);});
        toggleFavorite(product); toggleFavorite(product);

        ProductDetail productDetail = iMatDataHandler.getDetail(product);
        detailBrand.setText(productDetail.getBrand());
        detailOrigin.setText(productDetail.getOrigin());
        detailDescription.setText(productDetail.getDescription());

    }
    public void updateDetailAmount(Product product) {
        for (ShoppingItem shoppingItem : shoppingCart.getItems()) {
            if (product == shoppingItem.getProduct()) {
                detailAmount.setText(String.valueOf((int) shoppingItem.getAmount()));
                return;
            }
        }
        detailAmount.setText("0");
    }
    public void toggleFavorite(Product product) {
        if (iMatDataHandler.isFavorite(product)) {
            iMatDataHandler.removeFavorite(product);
            this.detailFavoriteButton.setText("♡");
        } else {
            iMatDataHandler.addFavorite(product);
            this.detailFavoriteButton.setText("❤");
        }
    }

    @FXML
    public void mouseTrap(Event event) {
        event.consume();
    }
}