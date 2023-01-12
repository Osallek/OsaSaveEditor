package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.game.Culture;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

public class CultureStringConverter extends StringConverter<Culture> {

    public static final CultureStringConverter INSTANCE = new CultureStringConverter();

    @Override
    public String toString(Culture culture) {
        return culture == null ? "" : StringUtils.capitalize(OsaSaveEditorUtils.localize(culture.getName(), culture.getGame()));
    }

    @Override
    public Culture fromString(String culture) {
        return null;
    }
}
