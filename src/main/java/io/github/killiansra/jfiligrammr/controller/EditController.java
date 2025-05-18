package io.github.killiansra.jfiligrammr.controller;

import io.github.killiansra.jfiligrammr.util.FileUtil;
import io.github.killiansra.jfiligrammr.util.ImageUtil;
import io.github.killiansra.jfiligrammr.util.PdfUtil;
import io.github.killiansra.jfiligrammr.util.enums.Orientation;
import io.github.killiansra.jfiligrammr.util.enums.Scope;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static io.github.killiansra.jfiligrammr.config.AppConstants.RESOURCE_BASE_PATH;
import static io.github.killiansra.jfiligrammr.util.PdfUtil.convertImagesToPdf;

public class EditController extends BaseController implements Initializable
{
    @FXML
    public BorderPane rootPane;

    @FXML
    public ImageView pageViewer;

    @FXML
    public Label pageNumber;

    @FXML
    public ImageView rightArrow;

    @FXML
    public ImageView leftArrow;

    @FXML
    public TextField watermark;

    @FXML
    public ChoiceBox<Integer> fontSizes;

    @FXML
    public ImageView loading;

    @FXML
    public RadioButton radioHorizontal, radioVertical, radioDiagonal, radioAllPages, radioFirstPage;

    @FXML
    public Button cancelButton, downloadButton;

    private List<BufferedImage> pdfPages;
    private List<BufferedImage> watermarkedPages;
    private int currentPageIndex = 0;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        enableWindowDrag(this.rootPane);

        //Convert the loaded PDF into a list of image pages
        this.pdfPages = PdfUtil.convertPdfToImages();
        this.watermarkedPages = ImageUtil.cloneList(this.pdfPages);

        //Display the first page of the PDF in the image viewer
        reloadImageView();

        this.updatePageNumber();

        //hide buttons if there is only one page
        if(this.pdfPages.size() == 1)
        {
            this.rightArrow.setVisible(false);
            this.leftArrow.setVisible(false);
        }

        //Fill Choice box
        int[] sizes = {18, 24, 30, 36, 42, 48, 54, 60, 66, 72, 78, 84, 90, 96, 102, 108};
        this.fontSizes.getItems().addAll(Arrays.stream(sizes).boxed().toArray(Integer[]::new));
        //Select a default size
        this.fontSizes.getSelectionModel().select(8);
        //Add listener to reload preview when font size is updated
        this.fontSizes.setOnAction(e -> setWatermark());

        //Group the radio buttons together
        ToggleGroup orientationGroup = new ToggleGroup();
        this.radioHorizontal.setToggleGroup(orientationGroup);
        this.radioVertical.setToggleGroup(orientationGroup);
        this.radioDiagonal.setToggleGroup(orientationGroup);

        ToggleGroup applyToGroup = new ToggleGroup();
        this.radioAllPages.setToggleGroup(applyToGroup);
        this.radioFirstPage.setToggleGroup(applyToGroup);
    }

    /**
     * Minimizes the application window.
     */
    @FXML
    public void reduceWindow()
    {
        super.reduce(this.rootPane);
    }

    /**
     * Updates the displayed page number label based on the current page index.
     */
    private void updatePageNumber()
    {
        this.pageNumber.setText("Page " + (currentPageIndex + 1) + " / " + pdfPages.size());
    }

    /**
     * Advances to the next page of the PDF if available.
     */
    @FXML
    public void nextPage()
    {
        if(currentPageIndex < this.pdfPages.size() - 1)
        {
            this.pageViewer.setImage(SwingFXUtils.toFXImage(this.watermarkedPages.get(++currentPageIndex), null));
            this.updatePageNumber();
        }
    }

    /**
     * Goes back to the previous page of the PDF if available.
     */
    @FXML
    public void previousPage()
    {
        if(currentPageIndex > 0)
        {
            this.pageViewer.setImage(SwingFXUtils.toFXImage(this.watermarkedPages.get(--currentPageIndex), null));
            this.updatePageNumber();
        }
    }

    /**
     * Applies a watermark to one page or all pages in the PDF.
     */
    @FXML
    public void setWatermark()
    {
        //Reset the watermarkedPages list
        this.watermarkedPages = ImageUtil.cloneList(this.pdfPages);

        int limit = 1;
        if(getScope() == Scope.ALL_PAGES)
        {
            //Applies the watermark to all pages
            limit = this.watermarkedPages.size();
        }

        for(int i = 0; i < limit; i++)
        {
            this.watermarkedPages.set(i, PdfUtil.addTextOnImage(this.watermarkedPages.get(i), this.watermark.getText(),
                    this.fontSizes.getSelectionModel().getSelectedItem(), getOrientation()));
        }

        reloadImageView();
    }

    /**
     * Returns the currently selected orientation.
     *
     * @return the selected {@link Orientation}.
     */
    private Orientation getOrientation()
    {
        Orientation orientation = Orientation.DIAGONAL;

        if(radioHorizontal.isSelected())
        {
            orientation = Orientation.HORIZONTAL;
        }
        else if(radioVertical.isSelected())
        {
            orientation = Orientation.VERTICAL;
        }

        return orientation;
    }

    /**
     * Returns the currently selected scope.
     *
     * @return the selected {@link Scope}.
     */
    private Scope getScope()
    {
        Scope scope = Scope.FIRST_PAGE_ONLY;

        if(radioAllPages.isSelected())
        {
            scope = Scope.ALL_PAGES;
        }

        return scope;
    }

    /**
     * Reloads the {@link ImageView} with the current watermarked page.
     */
    private void reloadImageView()
    {
        this.pageViewer.setImage(SwingFXUtils.toFXImage(this.watermarkedPages.get(currentPageIndex), null));
    }

    /**
     * Returns the user to the application's main menu.
     *
     * @throws IOException if the main view FXML file cannot be loaded.
     */
    @FXML
    public void backToMainMenu() throws IOException
    {
        //Deletes pdf files if there are any in the uploads folder
        FileUtil.cleanUpUploadsDirectory();

        //return to main menu
        FXMLLoader loader = new FXMLLoader(getClass().getResource(RESOURCE_BASE_PATH + "view/main.fxml"));

        this.root = loader.load();

        MainController mainController = loader.getController();

        this.stage = (Stage) this.rootPane.getScene().getWindow();
        this.scene = new Scene(root);
        this.stage.setScene(scene);
        this.stage.show();
    }

    /**
     * Opens a save dialog to export the watermarked PDF.
     *
     * @throws IOException if the main view FXML file cannot be loaded.
     */
    @FXML
    public void download() throws IOException
    {
        //Display the loading animation and disable buttons
        this.loading.setVisible(true);
        this.cancelButton.setDisable(true);
        this.downloadButton.setDisable(true);

        //Separate thread because this action can take several seconds
        Task<PDDocument> exportTask = new Task<>()
        {
            @Override
            protected PDDocument call() throws Exception
            {
                return convertImagesToPdf(watermarkedPages);
            }
        };

        exportTask.setOnSucceeded(event -> {
            //Handle success case
            PDDocument document = exportTask.getValue();

            try
            {
                //saving the file to the user's computer
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("JFiligrammr - Save your watermarked PDF file");
                //Default name
                fileChooser.setInitialFileName("watermarked_file.pdf");
                //Extension
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
                File output = fileChooser.showSaveDialog(stage);

                if(output != null)
                {
                    document.save(output);
                    document.close();
                    backToMainMenu();
                }
                else
                {
                    //Reactivate buttons and hide loading if cancelled
                    document.close();
                    this.loading.setVisible(false);
                    this.cancelButton.setDisable(false);
                    this.downloadButton.setDisable(false);
                }
            }
            catch (IOException e)
            {
                try
                {
                    document.close();
                }
                catch (IOException ex)
                {
                    throw new RuntimeException(ex);
                }
                throw new RuntimeException(e);
            }
        });

        exportTask.setOnFailed(event -> {
            //Handle failure case
            loading.setVisible(false);
            cancelButton.setDisable(false);
            downloadButton.setDisable(false);
            Throwable error = exportTask.getException();
            error.printStackTrace();
        });

        //Start the background task
        new Thread(exportTask).start();
    }
}
