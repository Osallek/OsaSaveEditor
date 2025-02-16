package fr.osallek.osasaveeditor.controller.object;

import fr.osallek.clausewitzparser.common.ClausewitzUtils;
import fr.osallek.eu4parser.model.save.country.SaveCountry;
import fr.osallek.eu4parser.model.save.province.SaveProvince;
import fr.osallek.osasaveeditor.common.Copy;

public class Army extends Copy<Army> {

    private final SaveCountry country;

    private final Integer id;

    private String name;

    private int location;

    private boolean changed;

    public Army(fr.osallek.eu4parser.model.save.country.Army army) {
        this.country = army.getCountry();
        this.id = army.getId().getId();
        this.name = ClausewitzUtils.removeQuotes(army.getName());
        this.location = army.getLocation();
    }

    public Army(SaveCountry country, String name, SaveProvince location) {
        this.country = country;
        this.id = null;
        this.name = name;
        this.location = location.getId();
        this.changed = true;
    }

    public Army(Army other) {
        this.country = other.country;
        this.id = other.id;
        this.name = other.name;
        this.location = other.location;
        this.changed = other.changed;
    }

    @Override
    public Army copy() {
        return new Army(this);
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

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        if (location != this.location) {
            this.location = location;
            this.changed = true;
        }
    }

    public boolean isChanged() {
        return changed;
    }
}
