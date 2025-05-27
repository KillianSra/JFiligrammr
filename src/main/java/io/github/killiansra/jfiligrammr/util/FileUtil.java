package io.github.killiansra.jfiligrammr.util;

import java.io.File;

import static io.github.killiansra.jfiligrammr.config.AppConstants.UPLOAD_DIR_NAME;

public class FileUtil
{
    //Private constructor to prevent accidental instantiation
    private FileUtil(){}

    /**
     * Returns the file extension from a given filename.
     *
     * @param filename the name of the targeted file
     * @return the file extension in lowercase, or {@code null} if none is found
     */
    public static String getExtension(String filename)
    {
        String extension = null;

        int i = filename.lastIndexOf('.');
        if(i > 0 && i < filename.length() - 1)
        {
            extension = filename.substring(i + 1).toLowerCase();
        }

        return extension;
    }

    /**
     * Deletes all files in the uploads directory located at the root of the application.
     */
    public static void cleanUpUploadsDirectory()
    {
        File dir = new File(UPLOAD_DIR_NAME);
        if(dir.exists() && dir.list().length > 0)
        {
            for(File file : dir.listFiles())
            {
                file.delete();
            }
        }
    }
}
