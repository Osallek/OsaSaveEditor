package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.game.Religion;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

public class ReligionStringConverter extends StringConverter<Religion> {

    public static final ReligionStringConverter INSTANCE = new ReligionStringConverter();

    @Override
    public String toString(Religion religion) {
        return religion == null ? "" : StringUtils.capitalize(OsaSaveEditorUtils.localize(religion.getName(), religion.getReligionGroup().getGame()));
    }

    @Override
    public Religion fromString(String religion) {
        return null;
    }
}
