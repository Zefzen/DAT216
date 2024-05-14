package imat;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import java.io.IOException;

public class productItem extends AnchorPane {

    @FXML private Label productTitle;
    @FXML private ImageView productImage;

    public productItem(String name){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("product_item.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.productTitle.setText(name);
        this.productImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("imat/resources/logo.png")));

    }


}
