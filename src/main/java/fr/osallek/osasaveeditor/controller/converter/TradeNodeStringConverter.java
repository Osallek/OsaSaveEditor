package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.game.TradeNode;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.util.StringConverter;

public class TradeNodeStringConverter extends StringConverter<TradeNode> {

    public static final TradeNodeStringConverter INSTANCE = new TradeNodeStringConverter();

    @Override
    public String toString(TradeNode tradeNode) {
        return tradeNode == null ? "" : OsaSaveEditorUtils.localize(tradeNode.getName(), tradeNode.getGame());
    }

    @Override
    public TradeNode fromString(String tradeNode) {
        return null;
    }
}
