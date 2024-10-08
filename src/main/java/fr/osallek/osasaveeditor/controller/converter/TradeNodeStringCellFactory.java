package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.game.Game;
import fr.osallek.eu4parser.model.game.TradeNode;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class TradeNodeStringCellFactory implements Callback<ListView<TradeNode>, ListCell<TradeNode>> {

    private final Game game;

    public TradeNodeStringCellFactory(Game game) {
        this.game = game;
    }

    @Override
    public ListCell<TradeNode> call(ListView<TradeNode> param) {
        return new ListCell<>() {

            @Override
            protected void updateItem(TradeNode value, boolean empty) {
                super.updateItem(value, empty);
                setText(value == null ? null : OsaSaveEditorUtils.localize(value.getName(), game));
            }
        };
    }
}
