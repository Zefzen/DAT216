
package imat;

import java.net.URL;
import java.util.*;


import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import se.chalmers.cse.dat216.project.*;

import static java.lang.String.valueOf;

public class MainViewController implements Initializable {

    @FXML private StackPane mainView;
    @FXML private AnchorPane productDetailView;
    @FXML private AnchorPane searchView;
    @FXML private AnchorPane cartView;
    @FXML private AnchorPane receiptView;
    @FXML private AnchorPane deliveryTimeView;
    @FXML private AnchorPane deliveryInfoView;
    @FXML private AnchorPane orderConfirmedView;
    @FXML private AnchorPane paymentView;
    @FXML private AnchorPane updateInfoView;

    @FXML private FlowPane productGrid;
    @FXML private TextField searchBar;
    @FXML private Accordion category;
    @FXML private ImageView logoImageView;
    @FXML private Button cartButton;

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

    @FXML private FlowPane cartFlowPane;
    @FXML private Label cartTotalLabel;
    @FXML private Label cartAmountLabel;
    @FXML private Button cartAddresButton;

    @FXML private FlowPane receiptListFlowPane;
    @FXML private FlowPane receiptDetailFlowPane;
    @FXML private Label receiptTotalLabel;

    @FXML private TextField infoFirstName;
    @FXML private TextField infoLastName;
    @FXML private TextField infoAdress;
    @FXML private TextField infoPostNumber;
    @FXML private TextField infoCity;
    @FXML private TextField infoPhone;
    @FXML private TextField infoMail;
    @FXML private CheckBox infoSaveInfo;

    @FXML private TextField updateNameTextField;
    @FXML private TextField updateAdressTextField;
    @FXML private TextField updatePostNumberTextField;
    @FXML private TextField updateCityTextField;
    @FXML private TextField updatePhoneTextField;
    @FXML private TextField updateMailTextField;
    @FXML private TextField updateCardNumberTextField;
    @FXML private TextField updateExpDateTextField;
    @FXML private TextField updateCVCTextField;

    @FXML private Label totalPaymentView;
    @FXML private Label leveransPaymentView;
    @FXML private TextField kortnummerPaymentView;
    @FXML private TextField EXPMonthPaymentView;
    @FXML private TextField EXPYearPaymentView;
    @FXML private TextField CVCPaymentView;

    @FXML private Label confirmedOrderNumberLabel;
    @FXML private Label confirmedOrderDeliveryLabel;

    @FXML private CheckBox optionDate1;
    @FXML private CheckBox optionDate2;
    @FXML private CheckBox optionDate3;
    @FXML private CheckBox optionDate4;
    @FXML private CheckBox optionDate5;
    @FXML private CheckBox optionDate6;
    private String stringDate1;
    private String stringDate2;
    private String stringDate3;
    private String stringDate4;
    private String stringDate5;
    private String stringDate6;


    IMatDataHandler iMatDataHandler = IMatDataHandler.getInstance();
    ShoppingCart shoppingCart = iMatDataHandler.getShoppingCart();
    HashMap<Product, ProductItem> productMap = new HashMap<>();
    HashMap<Product, CartListItem> cartProductMap = new HashMap<>();
    HashMap<Product, ReceiptDetailListItem> receiptDetailListItemMap = new HashMap<>();
    List<List<String>> subCategories;


    public void initialize(URL url, ResourceBundle rb) {

        shoppingCart.clear();
        for (int i = 0; i < 10; i++) {
            cartFlowPane.getChildren().add(new CartListItem(iMatDataHandler.getProducts().getFirst(),
                    iMatDataHandler, this));
        }



        List<Product> productList = iMatDataHandler.getProducts();
        for (Product product : productList) {
            ProductItem productItem = new ProductItem(product, iMatDataHandler, this);
            productMap.put(product, productItem);
            productGrid.getChildren().add(productItem);
            CartListItem cartListItem = new CartListItem(product, iMatDataHandler, this);
            cartProductMap.put(product, cartListItem);
            ReceiptDetailListItem receipeDetailListItem = new ReceiptDetailListItem(product, iMatDataHandler);
            receiptDetailListItemMap.put(product, receipeDetailListItem);
        }


        shoppingCart.addShoppingCartListener(new ShoppingCartListener() {
            @Override
            public void shoppingCartChanged(CartEvent cartEvent) {
                updateAmountLabels();
                populateCart();
                updateCartTotal();
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

        updateDetailAmount(product);



    }
    public void updateDetailAmount(Product product) {
        for (ShoppingItem shoppingItem : shoppingCart.getItems()) {
            if (product == shoppingItem.getProduct()) {
                detailAmount.setText(valueOf((int) shoppingItem.getAmount()));
                return;
            }
        }
        detailAmount.setText("0");
    }

    public void updateCartTotal() {
        cartTotalLabel.setText("Total: " + valueOf(shoppingCart.getTotal()) + " kr");
        int n = 0;
        for (ShoppingItem si: shoppingCart.getItems()) {
            n += si.getAmount();
        }
        cartAmountLabel.setText("Antal: " + valueOf(n));
        receiptTotalLabel.setText(valueOf(n));
    }

    public void populateMain() {
        searchView.toFront();
        mainView.toFront();
        populateGrid(iMatDataHandler.getProducts());
    }

    public void populateCart() {
        List<ShoppingItem> shoppingCartList = iMatDataHandler.getShoppingCart().getItems();
        cartFlowPane.getChildren().clear();
        for (ShoppingItem shoppingItem : shoppingCartList) {
            Product product = shoppingItem.getProduct();
            CartListItem cartListItem = cartProductMap.get(product);
            cartListItem.setAmount(shoppingItem.getAmount());
            cartFlowPane.getChildren().add(cartListItem);
        }
    }

    public void populateReceiptList() {
        receiptListFlowPane.getChildren().clear();
        for (Order order: iMatDataHandler.getOrders()) {
            receiptListFlowPane.getChildren().add(new ReceiptItem(order, iMatDataHandler, this));
        }
    }

    public void populateReceiptDetailList(Order order) {
        receiptDetailFlowPane.getChildren().clear();
        for (ShoppingItem si: order.getItems()) {
            ReceiptDetailListItem rd = receiptDetailListItemMap.get(si.getProduct());
            rd.setAmount(si.getAmount());
            receiptDetailFlowPane.getChildren().add(rd);
        }
    }

    public void pressShoppingCart() {

        cartView.toFront();
        populateCart();
    }

    public void pressReceipt() {
        receiptView.toFront();
        populateReceiptList();
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