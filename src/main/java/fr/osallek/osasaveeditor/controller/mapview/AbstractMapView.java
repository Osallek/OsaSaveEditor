package fr.osallek.osasaveeditor.controller.mapview;

import fr.osallek.eu4parser.model.save.province.SaveProvince;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;

public abstract class AbstractMapView {

    protected final MapViewType type;

    protected final MapViewContainer mapViewContainer;

    protected final Property<Boolean> selected;

    protected AbstractMapView(MapViewContainer mapViewContainer, MapViewType type) {
        this.type = type;
        this.mapViewContainer = mapViewContainer;
        this.selected = new SimpleBooleanProperty(false);
        this.selected.addListener((observable, oldValue, newValue) -> {
            if (Boolean.FALSE.equals(oldValue) && Boolean.TRUE.equals(newValue)) {
                onSelected();
            }
        });
    }

    public MapViewType getType() {
        return this.type;
    }

    public void setSelected(boolean selected) {
        this.selected.setValue(selected);
    }

    public abstract void draw() throws IOException;

    public abstract void onProvinceSelected(SaveProvince province);

    public abstract String updateTitle(SaveProvince selectedProvince);

    public abstract void onSelected();
}
