package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.clausewitzparser.common.ClausewitzUtils;
import fr.osallek.eu4parser.model.save.country.SaveCountry;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import fr.osallek.osasaveeditor.controller.EditorController;
import javafx.util.StringConverter;
import org.apache.commons.lang3.ObjectUtils;

public class CountryStringConverter extends StringConverter<SaveCountry> {

    public static final CountryStringConverter INSTANCE = new CountryStringConverter();

    @Override
    public String toString(SaveCountry country) {
        return (country == null || EditorController.dummyCountry.equals(country)) ? "" :
               ClausewitzUtils.removeQuotes(ObjectUtils.firstNonNull(country.getCustomName(),
                                                                     country.getName(),
                                                                     country.getLocalizedName(),
                                                                     OsaSaveEditorUtils.localize(country.getTag(), country.getSave().getGame())));
    }

    @Override
    public SaveCountry fromString(String tag) {
        return null;
    }
}
