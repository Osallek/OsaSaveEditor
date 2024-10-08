package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.game.Game;
import fr.osallek.eu4parser.model.game.ImperialReform;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ImperialReformStringCellFactory implements Callback<ListView<ImperialReform>, ListCell<ImperialReform>> {

    private final Game game;

    public ImperialReformStringCellFactory(Game game) {
        this.game = game;
    }

    @Override
    public ListCell<ImperialReform> call(ListView<ImperialReform> param) {
        return new ListCell<>() {

            @Override
            protected void updateItem(ImperialReform value, boolean empty) {
                super.updateItem(value, empty);
                setText(value == null ? null : OsaSaveEditorUtils.localize(value.getName() + "_title", game));
            }
        };
    }
}
