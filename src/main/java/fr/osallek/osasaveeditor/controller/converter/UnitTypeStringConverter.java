package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.UnitType;
import fr.osallek.eu4parser.model.game.Game;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

public class UnitTypeStringConverter extends StringConverter<UnitType> {

    private final Game game;

    public UnitTypeStringConverter(Game game) {
        this.game = game;
    }

    @Override
    public String toString(UnitType unitType) {
        return unitType == null ? "" : StringUtils.capitalize(OsaSaveEditorUtils.localize(unitType.name(), this.game));
    }

    @Override
    public UnitType fromString(String tag) {
        return null;
    }
}
