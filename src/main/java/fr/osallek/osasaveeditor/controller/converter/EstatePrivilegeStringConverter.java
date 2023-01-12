package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.game.EstatePrivilege;
import fr.osallek.eu4parser.model.game.Game;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.util.StringConverter;

public class EstatePrivilegeStringConverter extends StringConverter<EstatePrivilege> {

    private final Game game;

    public EstatePrivilegeStringConverter(Game game) {
        this.game = game;
    }

    @Override
    public String toString(EstatePrivilege privilege) {
        return privilege == null ? "" : OsaSaveEditorUtils.localize(privilege.getName(), this.game);
    }

    @Override
    public EstatePrivilege fromString(String s) {
        return null;
    }
}
