package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.game.Game;
import fr.osallek.eu4parser.model.game.TradeGood;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class TradeGoodStringCellFactory implements Callback<ListView<TradeGood>, ListCell<TradeGood>> {

    private final Game game;

    public TradeGoodStringCellFactory(Game game) {
        this.game = game;
    }

    @Override
    public ListCell<TradeGood> call(ListView<TradeGood> param) {
        return new ListCell<>() {

            @Override
            protected void updateItem(TradeGood value, boolean empty) {
                super.updateItem(value, empty);
                setText(value == null ? null : OsaSaveEditorUtils.localize(value.getName(), game));
            }
        };
    }
}
