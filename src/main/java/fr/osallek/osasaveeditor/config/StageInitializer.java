package fr.osallek.osasaveeditor.config;

import fr.osallek.osasaveeditor.common.Constants;
import fr.osallek.osasaveeditor.controller.HomeController;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StageInitializer.class);

    private final HomeController mainController;

    public StageInitializer(HomeController homeController) {
        this.mainController = homeController;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            Font.loadFont(FontAwesome.class.getResourceAsStream("fontawesome-webfont.ttf"), -1);

            Scene scene = new Scene(this.mainController.getScene(event.getSavePath()));
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

            Stage stage = event.getStage();
            stage.setMaximized(true);
            stage.setScene(scene);
            stage.setTitle("Osa Save Editor");
            stage.getIcons().add(new Image(getClass().getResourceAsStream(Constants.IMAGE_ICON)));
            stage.show();
        } catch (Exception e) {
            LOGGER.error("{}", e.getMessage(), e);
        }
    }
}
