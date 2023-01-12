package fr.osallek.osasaveeditor.common;

import fr.osallek.eu4parser.common.Eu4Utils;

import java.nio.file.Path;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Locale;

public final class Constants {

    private Constants() {
    }

    //Args
    public static final String GAME_FOLDER_ARG = "game_folder";
    public static final String SAVE_FILE_ARG = "save_file";
    public static final String OVERRIDE_ARG = "override";

    //Templates

    //Images
    public static final String IMAGE_ICON = "/images/favicon.ico";

    public static final Path EDITOR_FOLDER = Eu4Utils.OSALLEK_DOCUMENTS_FOLDER.resolve("OsaSaveEditor");

    public static final Locale LOCALE = Locale.getDefault();

    //To get full years
    public static final DateTimeFormatter PRETTY_DATE_FORMAT = DateTimeFormatter.ofPattern(DateTimeFormatterBuilder.getLocalizedDateTimePattern(FormatStyle.SHORT, null, IsoChronology.INSTANCE, LOCALE)
                                                                                                                   .replace("yyyy", "yy")
                                                                                                                   .replace("yy", "yyyy")
                                                                                                                   .replace("uuuu", "uu")
                                                                                                                   .replace("uu", "uuuu"));
}
