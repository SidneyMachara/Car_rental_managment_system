package carRentalSystem;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Class Used to change the applications Scenes.
 */
public class Scene_Changer {

    /**
     * Changes the layout of the pane passed.
     * @param path  name of the new layout file
     * @param mainPane  the pane whose layout is to be changed
     * @throws IOException throws an exception if the layout file is not found
     */
    public void changeScene(String path, AnchorPane mainPane) throws IOException {

        AnchorPane pane = new AnchorPane();
        pane = FXMLLoader.load(getClass().getResource("../view/"+path+".fxml"));
        mainPane.getChildren().setAll(pane);
    }
}
