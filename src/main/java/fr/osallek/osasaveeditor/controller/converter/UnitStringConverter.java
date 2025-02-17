package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.game.Game;
import fr.osallek.eu4parser.model.game.Unit;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

public class UnitStringConverter extends StringConverter<Unit> {

    private final Game game;

    public UnitStringConverter(Game game) {
        this.game = game;
    }

    @Override
    public String toString(Unit unit) {
        return unit == null ? "" : StringUtils.capitalize(OsaSaveEditorUtils.localize(unit.getName(), this.game));
    }

    @Override
    public Unit fromString(String tag) {
        return null;
    }
}
