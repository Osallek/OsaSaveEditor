package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.game.FetishistCult;
import fr.osallek.eu4parser.model.game.Game;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class FetishistCultStringCellFactory implements Callback<ListView<FetishistCult>, ListCell<FetishistCult>> {

    private final Game game;

    public FetishistCultStringCellFactory(Game game) {
        this.game = game;
    }

    @Override
    public ListCell<FetishistCult> call(ListView<FetishistCult> param) {
        return new ListCell<>() {

            @Override
            protected void updateItem(FetishistCult value, boolean empty) {
                super.updateItem(value, empty);
                setText(value == null ? null : OsaSaveEditorUtils.localize(value.getName(), game));
            }
        };
    }
}
