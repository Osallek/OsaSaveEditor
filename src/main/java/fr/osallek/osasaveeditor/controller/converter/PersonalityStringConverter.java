package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.game.Game;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import fr.osallek.osasaveeditor.controller.object.Personality;
import javafx.util.StringConverter;

public class PersonalityStringConverter extends StringConverter<Personality> {

    private final Game game;

    public PersonalityStringConverter(Game game) {
        this.game = game;
    }

    @Override
    public String toString(Personality rulerPersonality) {
        return rulerPersonality == null ? "" : OsaSaveEditorUtils.localize(rulerPersonality.getRulerPersonality().getName(), this.game);
    }

    @Override
    public Personality fromString(String s) {
        return null;
    }
}
