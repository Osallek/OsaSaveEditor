package fr.osallek.osasaveeditor.controller.object;

import fr.osallek.eu4parser.model.save.country.SaveCountry;
import fr.osallek.osasaveeditor.common.Copy;

import java.time.LocalDate;
import java.util.Objects;

public class Rival extends Copy<Rival> {

    private final SaveCountry source;

    private SaveCountry target;

    private LocalDate date;

    private boolean changed;

    public Rival(SaveCountry source, SaveCountry target, LocalDate date) {
        this.source = source;
        this.target = target;
        this.date = date;
        this.changed = true;
    }

    public Rival(SaveCountry country, fr.osallek.eu4parser.model.save.country.Rival rival) {
        this.source = country;
        this.target = rival.getRival();
        this.date = rival.getDate();
    }

    public Rival(Rival other) {
        this.source = other.source;
        this.target = other.target;
        this.date = other.date;
        this.changed = other.changed;
    }

    @Override
    public Rival copy() {
        return new Rival(this);
    }

    public SaveCountry getSource() {
        return source;
    }

    public SaveCountry getTarget() {
        return target;
    }

    public void setTarget(SaveCountry target) {
        if (!this.target.equals(target)) {
            this.target = target;
            this.changed = true;
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (!this.date.equals(date)) {
            this.date = date;
            this.changed = true;
        }
    }

    public boolean isChanged() {
        return changed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Rival that = (Rival) o;
        return Objects.equals(source, that.source) &&
               Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}
