package io.github.killiansra.jfiligrammr.controller;

import io.github.killiansra.jfiligrammr.util.FileUtil;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.stage.Stage;

public abstract class BaseController
{
    /**
     * Enables window dragging functionality for an undecorated JavaFX window.
     *
     * @param root the window to attach the drag behavior to.
     */
    public void enableWindowDrag(Node root)
    {
        root.setOnMousePressed(pressEvent -> {
            root.setOnMouseDragged(dragEvent -> {
                Stage stage = (Stage) root.getScene().getWindow();
                stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
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
    public void reduce(Node root)
    {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setIconified(true);
    }
}
