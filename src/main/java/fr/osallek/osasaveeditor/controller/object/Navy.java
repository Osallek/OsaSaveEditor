package fr.osallek.osasaveeditor.controller.object;

import fr.osallek.clausewitzparser.common.ClausewitzUtils;
import fr.osallek.eu4parser.model.save.country.SaveCountry;
import fr.osallek.eu4parser.model.save.province.SaveProvince;
import fr.osallek.osasaveeditor.common.Copy;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Navy extends Copy<Navy> {

    private final Integer id;

    private String name;

    private int location;

    private List<Regiment> ships;

    private boolean changed;

    public Navy(fr.osallek.eu4parser.model.save.country.Navy navy) {
        this.id = navy.getId().getId();
        this.name = ClausewitzUtils.removeQuotes(navy.getName());
        this.location = navy.getLocation();
        this.ships = navy.getShips().stream().map(r -> new Regiment(navy.getCountry(), r)).collect(Collectors.toList());
    }

    public Navy(SaveCountry country, String name, SaveProvince location) {
        this.id = null;
        this.name = name;
        this.location = location.getId();
        this.ships = new ArrayList<>();
        this.changed = true;
    }

    public Navy(Navy other) {
        this.id = other.id;
        this.name = other.name;
        this.location = other.location;
        this.ships = other.ships.stream().map(Regiment::new).collect(Collectors.toList());
        this.changed = other.changed;
    }

    @Override
    public Navy copy() {
        return new Navy(this);
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

    public List<Regiment> getRegiments() {
        return ships;
    }

    public void setRegiments(List<Regiment> regiments) {
        if (CollectionUtils.size(regiments) != CollectionUtils.size(this.ships) || regiments.stream().anyMatch(Regiment::isChanged)) {
            this.ships = regiments;
            this.changed = true;
        }
    }

    public boolean isChanged() {
        return changed;
    }
}
