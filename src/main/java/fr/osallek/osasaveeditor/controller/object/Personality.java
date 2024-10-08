package fr.osallek.osasaveeditor.controller.object;

import fr.osallek.eu4parser.model.game.RulerPersonality;
import fr.osallek.osasaveeditor.common.Copy;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;

import java.util.Objects;

public class Personality extends Copy<Personality> {

    private RulerPersonality rulerPersonality;

    private boolean changed;

    public Personality(RulerPersonality rulerPersonality) {
        this.rulerPersonality = rulerPersonality;
        this.changed = false;
    }

    public Personality(Personality other) {
        this.rulerPersonality = other.rulerPersonality;
        this.changed = other.changed;
    }

    @Override
    public Personality copy() {
        return new Personality(this);
    }

    public RulerPersonality getRulerPersonality() {
        return rulerPersonality;
    }

    public void setRulerPersonality(RulerPersonality rulerPersonality) {
        if (!rulerPersonality.equals(this.rulerPersonality)) {
            this.rulerPersonality = rulerPersonality;
            this.changed = true;
        }
    }

    public boolean isChanged() {
        return changed;
    }

    @Override
    public String toString() {
        return OsaSaveEditorUtils.localize(this.rulerPersonality.getName(), this.rulerPersonality.getGame());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Personality that)) {
            return false;
        }

        return Objects.equals(rulerPersonality, that.rulerPersonality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rulerPersonality);
    }
}
