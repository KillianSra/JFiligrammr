package io.github.killiansra.jfiligrammr.controller;

import io.github.killiansra.jfiligrammr.util.FileUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

import static io.github.killiansra.jfiligrammr.config.AppConstants.UPLOAD_DIR_NAME;
import static io.github.killiansra.jfiligrammr.config.AppConstants.VALID_EXTENSION;

public class MainController implements Initializable
{
    @FXML
    public BorderPane rootPane;

    @FXML
    public ImageView dropTarget;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        //Deletes pdf files if there are any in the uploads folder
        FileUtil.cleanUpUploadsDirectory();

        //Drag the window across the screen
        this.rootPane.setOnMousePressed(pressEvent -> {
            this.rootPane.setOnMouseDragged(dragEvent -> {
                Stage stage = (Stage) this.rootPane.getScene().getWindow();
                stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        });

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
                }
                catch(IOException e)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred while uploading the document");
                    alert.show();

                    event.setDropCompleted(false);
                    return;
                }

                event.setDropCompleted(true);
            }
        });
    }

    /**
     * Closes the application by terminating the JavaFX platform.
     */
    public void close()
    {
        //Delete the uploaded PDF file
        FileUtil.cleanUpUploadsDirectory();

        Platform.exit();
    }

    /**
     * Minimizes the application window.
     */
    public void reduce()
    {
        Stage stage = (Stage) this.rootPane.getScene().getWindow();
        stage.setIconified(true);
    }
}
