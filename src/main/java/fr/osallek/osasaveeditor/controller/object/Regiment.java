package fr.osallek.osasaveeditor.controller.object;

import fr.osallek.clausewitzparser.common.ClausewitzUtils;
import fr.osallek.eu4parser.model.UnitType;
import fr.osallek.eu4parser.model.game.Unit;
import fr.osallek.eu4parser.model.save.country.SaveCountry;
import fr.osallek.osasaveeditor.common.Copy;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

public class Regiment extends Copy<Regiment> {

    private final SaveCountry country;

    private final Integer id;

    private UnitType unitType;

    private String name;

    private final SimpleObjectProperty<Unit> type;

    private Double morale;

    private boolean changed;

    public Regiment(SaveCountry country, fr.osallek.eu4parser.model.save.country.AbstractRegiment regiment) {
        this.country = country;
        this.id = regiment.getId().getId();
        this.name = ClausewitzUtils.removeQuotes(regiment.getName());
        this.unitType = regiment.getUnitType();
        this.type = new SimpleObjectProperty<>(regiment.getType());
        this.morale = regiment.getMorale();
    }

    public Regiment(SaveCountry country, String name, UnitType unitType, Unit type, Double morale) {
        this.country = country;
        this.id = null;
        this.name = name;
        this.unitType = unitType;
        this.type = new SimpleObjectProperty<>(type);
        this.morale = morale;
        this.changed = true;
    }

    public Regiment(Regiment other) {
        this.country = other.country;
        this.id = other.id;
        this.name = other.name;
        this.unitType = other.unitType;
        this.type = other.type;
        this.morale = other.morale;
        this.changed = other.changed;
    }

    @Override
    public Regiment copy() {
        return new Regiment(this);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!name.equals(this.name)) {
            this.name = name;
            this.changed = true;
        }
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        if (!unitType.equals(this.unitType)) {
            this.unitType = unitType;
            setType(this.country.getSave().getGame().getUnit(this.country.getSubUnit().getSubUnit(unitType)));
            this.changed = true;
        }
    }

    public SimpleObjectProperty<Unit> getType() {
        return type;
    }

    public void setType(Unit type) {
        if (!type.equals(this.type.get())) {
            this.type.set(type);
            this.changed = true;
        }
    }

    public Double getMorale() {
        return morale;
    }

    public void setMorale(Double morale) {
        if (!morale.equals(this.morale)) {
            this.morale = morale;
            this.changed = true;
        }
    }

    public boolean isChanged() {
        return changed;
    }
}
