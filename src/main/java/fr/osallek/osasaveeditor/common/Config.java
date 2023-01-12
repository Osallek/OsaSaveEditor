package fr.osallek.osasaveeditor.common;

import fr.osallek.eu4parser.Eu4Parser;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

public class Config {

    private Config() {}

    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    private static boolean loaded = false;

    private static final Properties PROPERTIES = new Properties();

    private static final Path CONFIG_FILE_PATH = Constants.EDITOR_FOLDER.resolve("config").resolve("config.txt");
    private static final String GAME_FOLDER_PROP = "game_folder";

    public static Optional<Path> getGameFolder() {
        if (!loaded) {
            load();
        }

        String gameFolderPath = PROPERTIES.getProperty(GAME_FOLDER_PROP);

        if (gameFolderPath == null) {
            return Optional.empty();
        }

        Path path = Path.of(gameFolderPath);

        if (Files.exists(path) && Files.isReadable(path)) {
            return Optional.of(path);
        }

        return Optional.empty();
    }

    public static void setGameFolder(File newValue) {
        if (!loaded) {
            load();
        }

        if (newValue != null && newValue.exists()) {
            PROPERTIES.setProperty(GAME_FOLDER_PROP, newValue.getAbsolutePath());
        }

        store();
    }

    private static void load() {
        File file = CONFIG_FILE_PATH.toFile();

        if (!file.exists()) {
            tryDetectGameFolder();
            store();
        } else {
            try {
                try (InputStream inputStream = Files.newInputStream(file.toPath())) {
                    PROPERTIES.load(inputStream);
                }

                if (!PROPERTIES.containsKey(GAME_FOLDER_PROP)) {
                    tryDetectGameFolder();
                    store();
                }
            } catch (IOException e) {
                LOGGER.error("An error occurred while reading config file: {}", e.getMessage());
            }
        }

        loaded = true;
    }

    private static void store() {
        File file = CONFIG_FILE_PATH.toFile();

        try {
            if (!file.exists()) {
                FileUtils.forceMkdirParent(file);
            }

            try (OutputStream outputStream = Files.newOutputStream(file.toPath())) {
                PROPERTIES.store(outputStream, null);
            }
        } catch (IOException e) {
            LOGGER.error("An error occurred while writing config file: {}", e.getMessage());
        }
    }

    private static void tryDetectGameFolder() {
        Eu4Parser.detectInstallationFolder().ifPresent(path -> PROPERTIES.setProperty(GAME_FOLDER_PROP, path.toAbsolutePath().toString()));
    }
}
