package fr.osallek.osasaveeditor.controller.mapview;

import fr.osallek.eu4parser.model.save.Save;
import fr.osallek.osasaveeditor.controller.pane.CustomPropertySheet;
import fr.osallek.osasaveeditor.controller.pane.CustomPropertySheetSkin;
import fr.osallek.osasaveeditor.controller.propertyeditor.CustomPropertyEditorFactory;
import javafx.scene.layout.VBox;

import java.util.Set;

public abstract class PropertySheet<T> extends VBox {

    protected final Save save;

    protected T t;

    protected final CustomPropertySheet propertySheet;

    protected final CustomPropertySheetSkin propertySheetSkin;

    protected PropertySheet(Save save, T t) {
        this.save = save;
        this.t = t;
        this.propertySheet = new CustomPropertySheet();
        this.propertySheet.setPropertyEditorFactory(new CustomPropertyEditorFactory());
        this.propertySheet.setMode(CustomPropertySheet.Mode.CATEGORY);
        this.propertySheet.setModeSwitcherVisible(false);
        this.propertySheet.setSearchBoxVisible(false);

        this.propertySheetSkin = new CustomPropertySheetSkin(this.propertySheet);
        this.propertySheet.setSkin(this.propertySheetSkin);
    }

    public void update(T t) {
        this.t = t;
        String expandedPaneName = this.propertySheetSkin.getAccordion().getExpandedPane() == null ? null :
                                  this.propertySheetSkin.getAccordion().getExpandedPane().getText();
        Set<CustomPropertySheet.Item<?>> items = internalUpdate(t);

        this.propertySheet.getItems().forEach(item -> {
            if (!item.isVisible().isBound()) {
                if (items.contains(item)) {
                    if (!item.isVisible().get()) {
                        item.isVisible().set(true);
                    }
                } else {
                    if (item.isVisible().get()) {
                        item.isVisible().set(false);
                    }
                }
            }
        });

        this.propertySheetSkin.updateAccordionVisibility();

        if (expandedPaneName != null) {
            this.propertySheetSkin.getAccordion()
                                  .getPanes()
                                  .stream()
                                  .filter(titledPane -> titledPane.getText().equals(expandedPaneName))
                                  .findFirst()
                                  .ifPresent(titledPane -> this.propertySheetSkin.getAccordion().setExpandedPane(titledPane));
        }
    }

    protected abstract Set<CustomPropertySheet.Item<?>> internalUpdate(T t);

    public void validate() {
        internalValidate();
        update(this.t);
    }

    protected abstract void internalValidate();

    public Save getSave() {
        return save;
    }

    public CustomPropertySheet getPropertySheet() {
        return propertySheet;
    }

    public T getT() {
        return t;
    }
}
