package fr.osallek.osasaveeditor;

import fr.osallek.eu4parser.common.Eu4Utils;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OsaSaveEditorApplication {

    public static void main(String[] args) {
        System.setProperty("OSALLEK_DOCUMENTS", Eu4Utils.OSALLEK_DOCUMENTS_FOLDER.toString());
        Application.launch(OsaSaveEditorUiApplication.class, args);
    }

}
