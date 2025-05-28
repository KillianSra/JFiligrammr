package io.github.killiansra.jfiligrammr.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImageUtil
{
    //Private constructor to prevent accidental instantiation
    private ImageUtil(){}

    /**
     * Creates a clone of the given {@link List} objects.
     *
     * @param originalPages the list of {@link BufferedImage} to clone.
     * @return a new list containing independent copies of each image.
     */
    public static List<BufferedImage> cloneList(List<BufferedImage> originalPages)
    {
        List<BufferedImage> copy = new ArrayList<>();
        for (BufferedImage image : originalPages)
        {
            BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
            Graphics2D g = newImage.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            copy.add(newImage);
        }
        return copy;
    }
}
