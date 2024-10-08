package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.game.Game;
import fr.osallek.eu4parser.model.game.LeaderPersonality;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.util.StringConverter;

public class LeaderPersonalityStringConverter extends StringConverter<LeaderPersonality> {

    private final Game game;

    public LeaderPersonalityStringConverter(Game game) {
        this.game = game;
    }

    @Override
    public String toString(LeaderPersonality leaderPersonality) {
        return leaderPersonality == null ? "" : OsaSaveEditorUtils.localize(leaderPersonality.getName(), this.game);
    }

    @Override
    public LeaderPersonality fromString(String s) {
        return null;
    }
}
