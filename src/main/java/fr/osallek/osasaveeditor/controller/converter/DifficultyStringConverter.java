package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.save.Save;
import fr.osallek.eu4parser.model.save.gameplayoptions.Difficulty;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.util.StringConverter;

public class DifficultyStringConverter extends StringConverter<Difficulty> {

    private final Save save;

    public DifficultyStringConverter(Save save) {
        this.save = save;
    }

    @Override
    public String toString(Difficulty difficulty) {
        return difficulty == null ? "" : OsaSaveEditorUtils.localize(difficulty.name(), this.save.getGame());
    }

    @Override
    public Difficulty fromString(String difficulty) {
        return difficulty == null ? null : Difficulty.valueOf(difficulty);
    }
}
