package io.github.killiansra.jfiligrammr.controller;

import io.github.killiansra.jfiligrammr.util.PdfUtil;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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

    private List<BufferedImage> pdfPages;
    private int currentPageIndex = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        enableWindowDrag(this.rootPane);
        //Convert the loaded PDF into a list of image pages
        this.pdfPages = PdfUtil.convertPdfToImages();

        //Display the first page of the PDF in the image viewer
        this.pageViewer.setImage(SwingFXUtils.toFXImage(this.pdfPages.get(currentPageIndex), null));

        this.updatePageNumber();

        //hide buttons if there is only one page
        if(this.pdfPages.size() == 1)
        {
            this.rightArrow.setVisible(false);
            this.leftArrow.setVisible(false);
        }
    }

    /**
     * Minimizes the application window.
     */
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
    public void nextPage()
    {
        if(currentPageIndex < this.pdfPages.size() - 1)
        {
            this.pageViewer.setImage(SwingFXUtils.toFXImage(this.pdfPages.get(++currentPageIndex), null));
            this.updatePageNumber();
        }
    }

    /**
     * Goes back to the previous page of the PDF if available.
     */
    public void previousPage()
    {
        if(currentPageIndex > 0)
        {
            this.pageViewer.setImage(SwingFXUtils.toFXImage(this.pdfPages.get(--currentPageIndex), null));
            this.updatePageNumber();
        }
    }
}
