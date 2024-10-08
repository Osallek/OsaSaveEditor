package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.osasaveeditor.controller.object.GoldenBull;
import javafx.util.StringConverter;

public class GoldenBullStringConverter extends StringConverter<GoldenBull> {

    @Override
    public String toString(GoldenBull goldenBull) {
        return goldenBull == null ? "" : goldenBull.toString();
    }

    @Override
    public GoldenBull fromString(String goldenBull) {
        return null;
    }
}
