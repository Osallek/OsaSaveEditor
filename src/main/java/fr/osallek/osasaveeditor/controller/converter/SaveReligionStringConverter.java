package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.save.SaveReligion;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

public class SaveReligionStringConverter extends StringConverter<SaveReligion> {

    public static final SaveReligionStringConverter INSTANCE = new SaveReligionStringConverter();

    @Override
    public String toString(SaveReligion religion) {
        return religion == null ? "" : StringUtils.capitalize(OsaSaveEditorUtils.localize(religion.getName(), religion.getSave().getGame()));
    }

    @Override
    public SaveReligion fromString(String religion) {
        return null;
    }
}
