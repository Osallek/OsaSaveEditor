package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.game.Game;
import fr.osallek.eu4parser.model.game.SubjectType;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

public class SubjectTypeStringConverter extends StringConverter<SubjectType> {

    private final Game game;

    public SubjectTypeStringConverter(Game game) {
        this.game = game;
    }

    @Override
    public String toString(SubjectType subjectType) {
        return subjectType == null ? "" : StringUtils.capitalize(OsaSaveEditorUtils.localize(subjectType.getName() + "_title", this.game));
    }

    @Override
    public SubjectType fromString(String tag) {
        return null;
    }
}
