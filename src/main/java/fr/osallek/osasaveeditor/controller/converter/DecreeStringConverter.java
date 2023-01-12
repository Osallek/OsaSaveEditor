package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.osasaveeditor.controller.object.Decree;
import javafx.util.StringConverter;

public class DecreeStringConverter extends StringConverter<Decree> {

    @Override
    public String toString(Decree decree) {
        return decree == null ? "" : decree.toString();
    }

    @Override
    public Decree fromString(String tag) {
        return null;
    }
}
