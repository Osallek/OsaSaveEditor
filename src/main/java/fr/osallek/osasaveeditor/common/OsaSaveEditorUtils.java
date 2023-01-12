package fr.osallek.osasaveeditor.common;

import fr.osallek.clausewitzparser.common.ClausewitzUtils;
import fr.osallek.eu4parser.Eu4Parser;
import fr.osallek.eu4parser.model.game.Game;
import fr.osallek.eu4parser.model.game.localisation.Eu4Language;
import fr.osallek.eu4parser.model.game.localisation.Localisation;
import fr.osallek.eu4parser.model.save.country.SaveCountry;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class OsaSaveEditorUtils {

    private OsaSaveEditorUtils() {}

    public static Color countryToMapColor(SaveCountry country) {
        return Color.rgb(country.getColors().getMapColor().getRed(), country.getColors().getMapColor().getGreen(), country.getColors().getMapColor().getBlue());
    }

    public static String readArg(String[] args, String arg) {
        for (String s : args) {
            if (s.matches("^-?-?" + arg + "=.*$")) {
                return s.substring(s.indexOf(arg + "=") + arg.length() + 1).replace("\"", "");
            }
        }

        return null;
    }

    public static String localize(String s, Game game) {
        return Optional.ofNullable(game.getLocalisation(ClausewitzUtils.removeQuotes(s), Eu4Language.getByLocale(Constants.LOCALE)))
                       .or(() -> Optional.ofNullable(game.getLocalisation(ClausewitzUtils.removeQuotes(s), Eu4Language.ENGLISH)))
                       .map(Localisation::getValue)
                       .orElse(s);
    }

    public static ImageView bufferedToView(BufferedImage buffered) {
        WritableImage wr = null;

        if (buffered != null) {
            wr = new WritableImage(buffered.getWidth(), buffered.getHeight());
            PixelWriter pw = wr.getPixelWriter();

            for (int x = 0; x < buffered.getWidth(); x++) {
                for (int y = 0; y < buffered.getHeight(); y++) {
                    pw.setArgb(x, y, buffered.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr);
    }

    public static Path getSaveFolder(Path gameFolder) {
        try {
            return Eu4Parser.loadSettings(gameFolder).getSavesFolder();
        } catch (IOException e) {
            return null;
        }
    }

    public static List<Path> getSaves(Path saveFolder) {
        try {
            if (saveFolder != null && Files.exists(saveFolder) && Files.isDirectory(saveFolder)) {
                try (Stream<Path> stream = Files.walk(saveFolder)) {
                    return stream.filter(path -> path.getFileName().toString().endsWith(".eu4"))
                                 .filter(Eu4Parser::isValid)
                                 .filter(Predicate.not(Eu4Parser::isIronman))
                                 .sorted(Comparator.comparing(t -> t.toFile().lastModified(), Comparator.reverseOrder()))
                                 .toList();
                }
            }
        } catch (Exception ignored) {
        }

        return new ArrayList<>();
    }
}
