package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.game.Game;
import fr.osallek.eu4parser.model.game.PersonalDeity;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.util.StringConverter;

public class PersonalDeityStringConverter extends StringConverter<PersonalDeity> {

    private final Game game;

    public PersonalDeityStringConverter(Game game) {
        this.game = game;
    }

    @Override
    public String toString(PersonalDeity personalDeity) {
        return personalDeity == null ? "" : OsaSaveEditorUtils.localize(personalDeity.getName(), this.game);
    }

    @Override
    public PersonalDeity fromString(String s) {
        return null;
    }
}
