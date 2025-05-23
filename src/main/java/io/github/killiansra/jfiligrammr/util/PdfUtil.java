package io.github.killiansra.jfiligrammr.util;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.killiansra.jfiligrammr.util.enums.Orientation;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;

import static io.github.killiansra.jfiligrammr.config.AppConstants.*;

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
                images.add(renderer.renderImageWithDPI(i, DPI));
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
     * @param fontSize the font size of the watermark.
     * @param orientation the orientation of the watermark
     * @return the modified {@link BufferedImage} with the text watermark
     */
    public static BufferedImage addTextOnImage(BufferedImage image, String text, int fontSize, Orientation orientation)
    {
        //adjusts the font size
        fontSize *= SCALE;

        Graphics2D g2 = image.createGraphics();

        //Text settings
        Font font = new Font("Arial", Font.BOLD, fontSize);
        g2.setPaint(Color.BLACK);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

        //Text orientation
        AffineTransform transform = new AffineTransform();
        switch (orientation)
        {
            case Orientation.VERTICAL:
                transform.rotate(Math.PI / 2, image.getWidth() / 2.0, image.getHeight() / 2.0);
                break;
            case Orientation.DIAGONAL:
                transform.rotate(Math.toRadians(-55), image.getWidth() / 2.0, image.getHeight() / 2.0);
                break;
            default: //HORIZONTAL
                break;
        }

        g2.setTransform(transform);
        g2.setFont(font);

        //Center text horizontally and vertically
        FontMetrics metrics = g2.getFontMetrics(font);
        int x = (image.getWidth() - metrics.stringWidth(text)) / 2;
        int y = (image.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();

        //Draw the text on the BufferedImage
        g2.drawString(text, x, y);

        g2.dispose();

        return image;
    }

    /**
     * Converts a list of BufferedImages into a single PDF document.
     *
     * @param images the list of BufferedImages to include in the PDF.
     * @throws RuntimeException if an I/O error occurs during PDF creation.
     */
    public static PDDocument convertImagesToPdf(List<BufferedImage> images)
    {
        PDDocument document = new PDDocument();

        try
        {
            for(BufferedImage image : images)
            {
                //Conversion of pixels to points (1 point = 1/72 inch)
                float width = image.getWidth() * 72f / DPI;
                float height = image.getHeight() * 72f / DPI;

                //Create a new PDF page with the same dimensions as the original file
                PDPage page = new PDPage(new PDRectangle(width, height));
                document.addPage(page);

                //Convert the BufferedImage into a PDFBox image object
                PDImageXObject pdImage = JPEGFactory.createFromImage(document, image, 0.6f);
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page))
                {
                    contentStream.drawImage(pdImage, 0, 0, width, height);
                }
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }

        return document;
    }
}
