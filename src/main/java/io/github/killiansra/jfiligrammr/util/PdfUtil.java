package io.github.killiansra.jfiligrammr.util;

import java.awt.*;
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

    /**
     * Adds a semi-transparent text watermark on the given {@link BufferedImage}.
     *
     * @param image the {@link BufferedImage} to which the text will be added.
     * @param text the text string to draw on the image.
     * @return the modified {@link BufferedImage} with the text watermark
     */
    public static BufferedImage addTextOnImage(BufferedImage image, String text)
    {
        Graphics2D g2 = image.createGraphics();

        //Text settings
        Font font = new Font("Arial", Font.BOLD, 65);
        g2.setFont(font);
        g2.setPaint(Color.BLACK);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

        //Center text horizontally and vertically
        FontMetrics metrics = g2.getFontMetrics(font);
        int x = (image.getWidth() - metrics.stringWidth(text)) / 2;
        int y = (image.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();

        //Draw the text on the BufferedImage
        g2.drawString(text, x, y);

        g2.dispose();

        return image;
    }
}
