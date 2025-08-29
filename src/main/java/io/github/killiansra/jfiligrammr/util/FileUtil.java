package io.github.killiansra.jfiligrammr.util;

import java.io.File;
import java.nio.file.Path;

import static io.github.killiansra.jfiligrammr.config.AppConstants.UPLOAD_DIR_NAME;

public class FileUtil
{
    //Private constructor to prevent accidental instantiation
    private FileUtil(){}

    /**
     * Returns the platform-specific path to a user configuration directory.
     *
     * @return the absolute path to the config directory as a string
     */
    public static String getUserConfigPath()
    {
        Path baseDir;
        String dirName = "/JFiligrammr";
        String os = System.getProperty("os.name").toLowerCase();

        if(os.contains("win")) baseDir = Path.of(System.getenv("APPDATA"), dirName);
        else if(os.contains("mac"))
            baseDir = Path.of(System.getProperty("user.home"), "Library", "Application Support", dirName);
        //Linux, UNIX-like
        else
        {
            String xdgConfigHome = System.getenv("XDG_CONFIG_HOME");
            if(xdgConfigHome != null && !xdgConfigHome.isBlank()) baseDir = Path.of(xdgConfigHome, dirName);
            else baseDir = Path.of(System.getProperty("user.home"), ".config", dirName);
        }

        return baseDir.toString();
    }

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
