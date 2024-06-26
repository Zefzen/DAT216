
package imat;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import se.chalmers.cse.dat216.project.*;

import javax.swing.*;

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
    @FXML private Label searchResultLabel;

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
    @FXML private Button cartDeleteAllButton;

    @FXML private FlowPane receiptListFlowPane;
    @FXML private FlowPane receiptDetailFlowPane;
    @FXML private Label receiptTotalLabel;
    @FXML private Button buyAgainButton;

    @FXML private TextField infoFirstName;
    @FXML private TextField infoLastName;
    @FXML private TextField infoAdress;
    @FXML private TextField infoPostNumber;
    @FXML private TextField infoCity;
    @FXML private TextField infoPhone;
    @FXML private TextField infoMail;

    @FXML private TextField updateFirstNameTextField;
    @FXML private TextField updateLastNameTextField;
    @FXML private TextField updateAdressTextField;
    @FXML private TextField updatePostNumberTextField;
    @FXML private TextField updateCityTextField;
    @FXML private TextField updatePhoneTextField;
    @FXML private TextField updateMailTextField;
    @FXML private TextField updateCardNumberTextField;
    @FXML private TextField updateExpMonthTextField;
    @FXML private TextField updateExpYearTextField;
    @FXML private TextField updateCVCTextField;

    @FXML private Label totalPaymentView;
    @FXML private Label leveransPaymentView;
    @FXML private TextField kortnummerPaymentView;
    @FXML private TextField EXPMonthPaymentView;
    @FXML private TextField EXPYearPaymentView;
    @FXML private TextField CVCPaymentView;

    @FXML private Label confirmedOrderNumberLabel;
    @FXML private Label confirmedOrderDeliveryLabel;

    @FXML private ToggleGroup dateToggleGroup;
    @FXML private RadioButton optionDate1;
    @FXML private RadioButton optionDate2;
    @FXML private RadioButton optionDate3;
    @FXML private RadioButton optionDate4;
    @FXML private RadioButton optionDate5;
    @FXML private RadioButton optionDate6;
    private String stringDate1;
    private String stringDate2;
    private String stringDate3;
    private String stringDate4;
    private String stringDate5;
    private String stringDate6;


    IMatDataHandler iMatDataHandler = IMatDataHandler.getInstance();
    ShoppingCart shoppingCart = iMatDataHandler.getShoppingCart();
    CreditCard creditCard = iMatDataHandler.getCreditCard();
    HashMap<Product, ProductItem> productMap = new HashMap<>();
    HashMap<Product, CartListItem> cartProductMap = new HashMap<>();
    HashMap<Product, ReceiptDetailListItem> receiptDetailListItemMap = new HashMap<>();
    List<List<String>> subCategories;
    @FXML private ImageView detailFavoriteImage;


    public void initialize(URL url, ResourceBundle rb) {


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

        shoppingCart.clear();
        populateMain();
        updateCartTotal();
        updateAmountLabels();


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
            TitledPane pane = panes.get(main + 2);
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
        panes.get(0).setOnMouseClicked(EventHandler -> {populateMain();});
        panes.get(1).setOnMouseClicked(EventHandler -> {populateGrid(iMatDataHandler.favorites());
        searchResultLabel.setText("");
        if (iMatDataHandler.favorites().isEmpty()) {
        searchResultLabel.setText("Du har inga favoriter än\n\nTryck på hjärtat för att lägga till den i favoriter");}});


        productDetailView.setOnMouseClicked(EventHandler -> {searchView.toFront();});  // Return when clicking on black border
        detailCloseButton.setOnMouseClicked(EventHandler -> {searchView.toFront();});

        cartDeleteAllButton.setOnAction(EventHandler -> {shoppingCart.clear();});

        logoImageView.setOnMouseClicked(EventHandler -> {populateMain();});
    }

    public void updateSearchResult() {
        List<Product> searchResult = iMatDataHandler.findProducts(searchBar.getText());
        populateGrid(searchResult);
        if (searchResult.isEmpty()) {
            searchResultLabel.setText("Din sökning matchade ingen vara i vårat system");
            } else {searchResultLabel.setText("");}
        searchView.toFront();
        mainView.toFront();

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
        searchResultLabel.setText("");
        List<Product> mainCategory = new ArrayList<>();
        List<String> subCategoryNames = subCategories.get(index);
        for (String name : subCategoryNames) {
            List<Product> tempCategory = iMatDataHandler.getProducts(ProductCategory.valueOf(name));
            mainCategory.addAll(tempCategory);
        }
        populateGrid(mainCategory);
    }


    public void clickSubCategory(String name) {
        searchResultLabel.setText("");
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
        cartTotalLabel.setText("Total: " + String.format ("%.2f",shoppingCart.getTotal() ) + " kr");
        int n = 0;
        for (ShoppingItem si: shoppingCart.getItems()) {
            n += si.getAmount();
        }
        cartAmountLabel.setText("Antal: " + valueOf(n));
    }

    public void populateMain() {
        searchView.toFront();
        mainView.toFront();
        searchResultLabel.setText("");
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
        int i = 0;
        for (Order order: iMatDataHandler.getOrders().reversed()) {
            ReceiptItem ri = new ReceiptItem(order, iMatDataHandler, this);
            if (i % 2 == 1) {
            ri.setStyle("-fx-background-color: #e4cbfc;"); }
            else {ri.setStyle("-fx-background-color: #e7d5f8;");}
            receiptListFlowPane.getChildren().add(ri);
            i ++;
        }
    }

    public void populateReceiptDetailList(Order order) {
        receiptDetailFlowPane.getChildren().clear();
        buyAgainButton.setOnAction(EventHandler -> {purchaseAgain(order);});
        double total = 0;
        for (ShoppingItem si: order.getItems()) {
            ReceiptDetailListItem rd = receiptDetailListItemMap.get(si.getProduct());
            rd.setAmount(si.getAmount());
            total += si.getTotal();
            receiptDetailFlowPane.getChildren().add(rd);
        }
        receiptTotalLabel.setText(String.format ("%.2f",total) + " kr");
    }

    public void populateDeliveryTime() {
        LocalDate futureDate1 = LocalDate.now().plusDays(1);
        LocalDate futureDate2 = LocalDate.now().plusDays(2);
        LocalDate futureDate3 = LocalDate.now().plusDays(3);
        Locale locale = new Locale("sv", "SE");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d MMMM y");
        formatter.localizedBy(locale);
        String date1 = futureDate1.format(formatter);
        String date2 = futureDate2.format(formatter);
        String date3 = futureDate3.format(formatter);

        stringDate1 = date1 + " kl 12:00";
        stringDate2 = date1 + " kl 17:00";
        stringDate3 = date2 + " kl 12:00";
        stringDate4 = date2 + " kl 17:00";
        stringDate5 = date3 + " kl 12:00";
        stringDate6 = date3 + " kl 17:00";

        optionDate1.setText(stringDate1);
        optionDate2.setText(stringDate2);
        optionDate3.setText(stringDate3);
        optionDate4.setText(stringDate4);
        optionDate5.setText(stringDate5);
        optionDate6.setText(stringDate6);

        deliveryTimeView.toFront();
    }
    public String getSelectedDate(){
        if(optionDate1.isSelected()){
            return stringDate1;
        }
        if(optionDate2.isSelected()){
            return stringDate2;
        }
        if(optionDate3.isSelected()){
            return stringDate3;
        }
        if(optionDate4.isSelected()){
            return stringDate4;
        }
        if(optionDate5.isSelected()){
            return stringDate5;
        }
        if(optionDate6.isSelected()){
            return stringDate6;
        }
        return stringDate1;
    }


    public void PressDeliveryTime() {
        saveDeliveryInfo();
        populateDeliveryTime();
        }
    public void PressDeliveryTimeBackwards() {
        savePayment();
        populateDeliveryTime();
        }
   public void populateDeliveryInfo() {
       Customer c = iMatDataHandler.getCustomer();
       infoFirstName.setText(c.getFirstName());
       infoLastName.setText(c.getLastName());
       infoAdress.setText(c.getAddress());
       infoPostNumber.setText(c.getPostCode());
       infoPhone.setText(c.getPhoneNumber());
       infoMail.setText(c.getEmail());
       infoCity.setText(c.getPostAddress());

       deliveryInfoView.toFront();

   }
    public void populateOrderConfirmed() {
       confirmedOrderNumberLabel.setText("Ordernummer: " + iMatDataHandler.placeOrder().getOrderNumber());
       confirmedOrderDeliveryLabel.setText("Din beställning levereras " + getSelectedDate());
       orderConfirmedView.toFront();


    }

    public void pressOrderConfirmation() {
        savePayment();
        populateOrderConfirmed();
        iMatDataHandler.shutDown();
    }

    public void populatePayment(){
        paymentView.toFront();
        CreditCard cc = iMatDataHandler.getCreditCard();
        totalPaymentView.setText("Total: " + String.format("%.2f", shoppingCart.getTotal())  +" kr");
        leveransPaymentView.setText("Leverans: "+ getSelectedDate());
        kortnummerPaymentView.setText(cc.getCardNumber());
        EXPMonthPaymentView.setText(valueOf(cc.getValidMonth()));
        CVCPaymentView.setText(String.valueOf(cc.getVerificationCode()));
        EXPYearPaymentView.setText(valueOf(cc.getValidYear()));
    }

    public void populateUpdateInfo(){
        updateInfoView.toFront();
        CreditCard cc = iMatDataHandler.getCreditCard();
        Customer customer = iMatDataHandler.getCustomer();
        updateFirstNameTextField.setText(customer.getFirstName());
        updateLastNameTextField.setText(customer.getLastName());
        updateAdressTextField.setText(customer.getAddress());
        updatePostNumberTextField.setText(customer.getPostCode());
        updateCityTextField.setText(customer.getPostAddress());
        updatePhoneTextField.setText(customer.getPhoneNumber());
        updateMailTextField.setText(customer.getEmail());
        updateCardNumberTextField.setText(cc.getCardNumber());
        updateExpMonthTextField.setText(String.valueOf(cc.getValidMonth()));
        updateExpYearTextField.setText(String.valueOf(cc.getValidYear()));
        updateCVCTextField.setText(String.valueOf(cc.getVerificationCode()));
    }

    public void saveUpdateInfo(){
        CreditCard cc = iMatDataHandler.getCreditCard();
        Customer customer = iMatDataHandler.getCustomer();
        customer.setFirstName(updateFirstNameTextField.getText());
        customer.setLastName(updateLastNameTextField.getText());
        customer.setAddress(updateAdressTextField.getText());
        customer.setPostCode(updatePostNumberTextField.getText());
        customer.setPostAddress(updateCityTextField.getText());
        customer.setPhoneNumber(updatePhoneTextField.getText());
        customer.setEmail(updateMailTextField.getText());
        cc.setCardNumber(updateCardNumberTextField.getText());
        cc.setValidMonth(Integer.parseInt(updateExpMonthTextField.getText()));
        cc.setValidYear(Integer.parseInt(updateExpYearTextField.getText()));
        cc.setVerificationCode(Integer.parseInt(updateCVCTextField.getText()));
        iMatDataHandler.shutDown();
        populateMain();
    }

    public void savePayment() {
        CreditCard cc = iMatDataHandler.getCreditCard();
        cc.setCardNumber(kortnummerPaymentView.getText());
        cc.setValidMonth(Integer.parseInt(EXPMonthPaymentView.getText()));
        cc.setValidYear(Integer.parseInt(EXPYearPaymentView.getText()));
        cc.setVerificationCode(Integer.parseInt(CVCPaymentView.getText()));
        iMatDataHandler.shutDown();
    }

    public void pressShoppingCart() {
        cartView.toFront();
        populateCart();
    }

    public void pressShoppingCartBackwards() {
        saveDeliveryInfo();
        cartView.toFront();
        populateCart();
        }

    public void pressReceipt() {
        receiptView.toFront();
        populateReceiptList();
    }

    public void pressProfile(){
        updateInfoView.toFront();
    }

    public void saveDeliveryInfo() {
        Customer c = iMatDataHandler.getCustomer();
        c.setFirstName(infoFirstName.getText());
        c.setLastName(infoLastName.getText());
        c.setAddress(infoAdress.getText());
        c.setPostAddress(infoCity.getText());
        c.setPostCode(infoPostNumber.getText());
        c.setPhoneNumber(infoPhone.getText());
        c.setEmail(infoMail.getText());
        iMatDataHandler.shutDown();
    }

    public void purchaseAgain(Order order) {
        for(ShoppingItem si: order.getItems()) {
            shoppingCart.addProduct(si.getProduct(),si.getAmount());
        }

    }

    public void toggleFavorite(Product product) {
        if (iMatDataHandler.isFavorite(product)) {
            String iconPath = "imat/resources/heart_empty.png";
            iMatDataHandler.removeFavorite(product);
            this.detailFavoriteImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream(iconPath)));
            productMap.get(product).removeFavorite();
        } else {
            iMatDataHandler.addFavorite(product);
            String iconPath = "imat/resources/heart_full.png";
            this.detailFavoriteImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream(iconPath)));
            productMap.get(product).setFavorite();
        }
        iMatDataHandler.shutDown();
    }

    @FXML
    public void mouseTrap(Event event) {
        event.consume();
    }
}