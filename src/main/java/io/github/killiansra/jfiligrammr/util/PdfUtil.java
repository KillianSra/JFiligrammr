package io.github.killiansra.jfiligrammr.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import static io.github.killiansra.jfiligrammr.config.AppConstants.UPLOAD_DIR_NAME;

public class PdfUtil
{
    private static String filename;

    /**
     * Sets the name of the PDF file to be used for conversion tu BufferedImage.
     *
     * @param pFilename the name of the PDF file
     */
    public static void setFilename(String pFilename)
    {
        filename = pFilename;
    }

    /**
     * Converts the currently set PDF file into a list of BufferedImages
     *
     * @return a list of images representing each page of the PDF
     * @throws RuntimeException if the PDF file cannot be loaded or rendered
     */
    public static List<BufferedImage> convertPdfToImages()
    {
        List<BufferedImage> images = new ArrayList<>();

        try
        {
            //Load the PDF file
            PDDocument document = Loader.loadPDF(new File(UPLOAD_DIR_NAME + "/" + filename));
            //Create a renderer for converting PDF pages to BufferedImage
            PDFRenderer renderer = new PDFRenderer(document);

            //Render each page of the PDF as a BufferedImage
            for(int i = 0; i < document.getNumberOfPages(); i++)
            {
                images.add(renderer.renderImage(i));
            }

            document.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        return images;
    }
}
