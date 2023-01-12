package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.game.Game;
import fr.osallek.eu4parser.model.game.IdeaGroup;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.util.StringConverter;

public class IdeaGroupStringConverter extends StringConverter<IdeaGroup> {

    private final Game game;

    public IdeaGroupStringConverter(Game game) {
        this.game = game;
    }

    @Override
    public String toString(IdeaGroup ideaGroup) {
        return ideaGroup == null ? "" : OsaSaveEditorUtils.localize(ideaGroup.getName(), this.game);
    }

    @Override
    public IdeaGroup fromString(String tag) {
        return null;
    }
}
