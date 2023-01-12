package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.game.Game;
import fr.osallek.eu4parser.model.save.country.LeaderType;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.util.StringConverter;

public class LeaderTypeStringConverter extends StringConverter<LeaderType> {

    private final Game game;

    public LeaderTypeStringConverter(Game game) {
        this.game = game;
    }

    @Override
    public String toString(LeaderType leaderType) {
        return leaderType == null ? "" : OsaSaveEditorUtils.localize(leaderType.name(), this.game);
    }

    @Override
    public LeaderType fromString(String tag) {
        return LeaderType.value(tag);
    }
}
