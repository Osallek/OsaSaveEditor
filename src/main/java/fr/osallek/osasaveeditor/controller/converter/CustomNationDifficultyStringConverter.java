package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.save.Save;
import fr.osallek.eu4parser.model.save.gameplayoptions.CustomNationDifficulty;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.util.StringConverter;

public class CustomNationDifficultyStringConverter extends StringConverter<CustomNationDifficulty> {

    private final Save save;

    public CustomNationDifficultyStringConverter(Save save) {
        this.save = save;
    }

    @Override
    public String toString(CustomNationDifficulty difficulty) {
        return difficulty == null ? "" : OsaSaveEditorUtils.localize(difficulty.name(), this.save.getGame());
    }

    @Override
    public CustomNationDifficulty fromString(String difficulty) {
        return difficulty == null ? null : CustomNationDifficulty.valueOf(difficulty);
    }
}
