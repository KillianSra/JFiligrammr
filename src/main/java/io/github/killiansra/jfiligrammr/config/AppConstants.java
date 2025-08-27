package io.github.killiansra.jfiligrammr.config;

import io.github.killiansra.jfiligrammr.util.FileUtil;

public class AppConstants
{
    public static final String VALID_EXTENSION = "pdf";
    public static final String UPLOAD_DIR_NAME = FileUtil.getUserConfigPath();
    public static final String RESOURCE_BASE_PATH = "/io/github/killiansra/jfiligrammr/";
    public static final float DPI = 150.0F;
    public static final int SCALE = (int) DPI / 72;
}