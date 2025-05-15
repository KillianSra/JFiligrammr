package io.github.killiansra.jfiligrammr.controller;

import io.github.killiansra.jfiligrammr.util.FileUtil;
import io.github.killiansra.jfiligrammr.util.PdfUtil;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.ResourceBundle;

import static io.github.killiansra.jfiligrammr.config.AppConstants.*;

public class MainController extends BaseController implements Initializable
{
    @FXML
    public BorderPane rootPane;

    @FXML
    public ImageView dropTarget;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        //Deletes pdf files if there are any in the uploads folder
        FileUtil.cleanUpUploadsDirectory();

        //Drag the window across the screen
        super.enableWindowDrag(this.rootPane);

        //Drag PDF file into the ImageView
        this.dropTarget.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();

            //Check if the dragged file is a pdf file
            if(db.hasFiles() && Objects.equals(FileUtil.getExtension(String.valueOf(db.getFiles().get(0))), VALID_EXTENSION))
            {
                //Allow copying
                event.acceptTransferModes(TransferMode.COPY);
            }
        });

        //Drop PDF file into the ImageView
        this.dropTarget.setOnDragDropped(event -> {
            if(event.getDragboard().hasFiles())
            {
                File droppedFile = event.getDragboard().getFiles().get(0);

                //Upload directory
                File dir = new File(System.getProperty("user.dir"), UPLOAD_DIR_NAME);

                //Creates the folder if it does not exist
                if(!dir.exists())
                {
                    dir.mkdirs();
                }

                File destFile = new File(dir, droppedFile.getName());

                try
                {
                    Files.copy(droppedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    PdfUtil.setFilename(droppedFile.getName());
                }
                catch(IOException e)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred while uploading the document");
                    alert.show();

                    event.setDropCompleted(false);
                    return;
                }

                event.setDropCompleted(true);

                try
                {
                    //Shows the next scene
                    changeScene(event);
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Minimizes the application window.
     */
    public void reduceWindow()
    {
        super.reduce(this.rootPane);
    }

    /**
     * Loads the edit view from an FXML file and switches the current scene to it.
     *
     * @param e the event that triggered the scene change
     * @throws IOException if the FXML file cannot be loaded
     */
    private void changeScene(Event e) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RESOURCE_BASE_PATH + "view/edit.fxml"));

        this.root = loader.load();

        EditController editController = loader.getController();

        this.stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        this.scene = new Scene(root);
        this.stage.setScene(scene);
        this.stage.show();
    }
}
