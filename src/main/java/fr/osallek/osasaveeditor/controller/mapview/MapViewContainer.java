package fr.osallek.osasaveeditor.controller.mapview;

import fr.osallek.eu4parser.model.game.Culture;
import fr.osallek.eu4parser.model.game.TradeGood;
import fr.osallek.eu4parser.model.game.TradeNode;
import fr.osallek.eu4parser.model.game.localisation.Eu4Language;
import fr.osallek.eu4parser.model.save.Save;
import fr.osallek.eu4parser.model.save.SaveReligion;
import fr.osallek.eu4parser.model.save.country.SaveCountry;
import fr.osallek.eu4parser.model.save.province.SaveProvince;
import fr.osallek.osasaveeditor.OsaSaveEditorApplication;
import fr.osallek.osasaveeditor.common.Constants;
import fr.osallek.osasaveeditor.controller.pane.CustomPropertySheet;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SegmentedButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MapViewContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapViewContainer.class);

    private final SaveProvince[][] provincesMap;

    private final Canvas canvas;

    private final Save save;

    private final VBox editPane;

    private final Map<MapViewType, AbstractMapView> mapViews;

    private AbstractMapView selectedMapView;

    private SaveProvince selectedProvince;

    private final ObservableList<SaveCountry> playableCountries;

    private final ObservableList<SaveCountry> countriesAlive;

    private final ObservableList<Culture> cultures;

    private final ObservableList<SaveReligion> religions;

    private final ObservableList<SaveReligion> playableReligions;

    private final ObservableList<TradeGood> tradeGoods;

    private final ObservableList<TradeNode> tradeNodes;

    private final ObservableList<SaveProvince> cities;

    private final Label titleLabel;

    private final SegmentedButton tabsSegmentedButton;

    private final ToggleButton saveButton;

    private final SavePropertySheet saveSheet;

    private final Button submitButton;

    private final List<CustomPropertySheet> sheets = new ArrayList<>();

    public MapViewContainer(MessageSource messageSource, SaveProvince[][] provincesMap, Canvas canvas, VBox editPane, Save save,
                            ObservableList<SaveCountry> playableCountries, ObservableList<SaveCountry> countriesAlive, ObservableList<Culture> cultures,
                            ObservableList<SaveReligion> religions, ObservableList<SaveReligion> playableReligions, ObservableList<TradeGood> tradeGoods,
                            ObservableList<TradeNode> tradeNodes, ObservableList<SaveProvince> cities) {
        this.provincesMap = provincesMap;
        this.canvas = canvas;
        this.editPane = editPane;
        this.save = save;
        this.cities = cities;
        this.playableCountries = playableCountries;
        this.countriesAlive = countriesAlive;
        this.cultures = cultures;
        this.religions = religions;
        this.playableReligions = playableReligions;
        this.tradeGoods = tradeGoods;
        this.tradeNodes = tradeNodes;
        this.saveSheet = new SavePropertySheet(this.save, this.countriesAlive, this.cities, messageSource);
        this.mapViews = new EnumMap<>(MapViewType.class);

        this.titleLabel = new Label();
        this.titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px");

        this.submitButton = new Button(save.getGame().getLocalisationClean("SM_APPLY", Eu4Language.getByLocale(Constants.LOCALE)));
        this.submitButton.setStyle("-fx-font-weight: bold");

        this.saveButton = new ToggleButton(save.getGame().getLocalisationClean("SM_GAME", Eu4Language.getByLocale(Constants.LOCALE)));
        this.saveButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (Boolean.FALSE.equals(oldValue) && Boolean.TRUE.equals(newValue)) {
                selectSaveButton();
            }
        });
        this.saveButton.disableProperty().bind(this.saveButton.selectedProperty());

        this.tabsSegmentedButton = new SegmentedButton();
        this.tabsSegmentedButton.getStyleClass().add(SegmentedButton.STYLE_CLASS_DARK);
        this.tabsSegmentedButton.setMaxWidth(Double.MAX_VALUE);
        this.tabsSegmentedButton.getStylesheets().add(OsaSaveEditorApplication.class.getResource("/styles/style.css").toExternalForm());
        addTabsSegmentedButtons(this.saveButton);

        this.editPane.getChildren().clear();
        this.editPane.getChildren().add(this.tabsSegmentedButton);
        this.editPane.getChildren().add(this.titleLabel);
        this.editPane.getChildren().add(this.submitButton);

        addSheet(this.saveSheet.getPropertySheet());

        this.saveButton.setSelected(true);
    }

    public void draw() {
        if (this.selectedMapView != null) {
            this.selectedMapView.draw();
        }
    }

    public void registerMapView(MapViewType mapViewType, AbstractMapView mapView) {
        this.mapViews.put(mapViewType, mapView);
    }

    public void selectMapView(MapViewType mapViewType) {
        if (this.mapViews.containsKey(mapViewType)) {
            if (this.selectedMapView != null) {
                this.selectedMapView.setSelected(false);
            }

            clearTabsSegmentedButton();
            this.selectedMapView = this.mapViews.get(mapViewType);
            this.selectedMapView.setSelected(true);
        }
    }

    public void onProvinceSelected(SaveProvince province) {
        this.selectedProvince = province;

        if (this.selectedMapView != null) {
            this.selectedMapView.onProvinceSelected(this.selectedProvince);
        }
    }

    public void updateTitle() {
        if (this.saveButton.isSelected()) {
            this.titleLabel.setText(this.save.getName());
        } else {
            this.titleLabel.setText(this.selectedMapView.updateTitle(this.selectedProvince));
        }
    }

    public void selectSaveButton() {
        updateTitle();
        showSheet(this.saveSheet.getPropertySheet());
        setSubmitButtonOnAction(event -> {
            this.saveSheet.validate();
            this.saveSheet.update(this.save);
            updateTitle();
        });
        bindSubmitButtonDisableProperty(this.saveSheet.getValidationSupport().invalidProperty());
    }

    public void showSheet(CustomPropertySheet toShow) {
        for (CustomPropertySheet sheet : this.sheets) {
            sheet.visibleProperty().set(sheet.equals(toShow));
        }
    }

    public void hideSaveSheet() {
        hideSheet(this.saveSheet.getPropertySheet());
    }

    public void hideSheet(CustomPropertySheet propertySheet) {
        propertySheet.visibleProperty().set(false);
    }

    public void setSubmitButtonOnAction(EventHandler<ActionEvent> value) {
        this.submitButton.setOnAction(value);
    }

    public void bindSubmitButtonDisableProperty(ReadOnlyBooleanProperty observable) {
        this.submitButton.disableProperty().unbind();
        this.submitButton.disableProperty().bind(observable);
    }

    public void clearTabsSegmentedButton() {
        this.tabsSegmentedButton.getButtons().subList(1, this.tabsSegmentedButton.getButtons().size()).clear();
    }

    public void addTabsSegmentedButtons(ToggleButton... buttons) {
        this.tabsSegmentedButton.getButtons().addAll(buttons);
        this.tabsSegmentedButton.getButtons()
                                .forEach(toggleButton -> toggleButton.prefWidthProperty()
                                                                     .bind(this.tabsSegmentedButton.widthProperty()
                                                                                                   .divide(this.tabsSegmentedButton.getButtons().size())));
        this.tabsSegmentedButton.getButtons().forEach(button -> button.disableProperty().bind(button.selectedProperty()));
    }

    public void addSheet(CustomPropertySheet sheet) {
        this.editPane.getChildren().add(2 + this.sheets.size(), sheet); //+2 for buttons and title
        this.sheets.add(sheet);
        sheet.managedProperty().bind(sheet.visibleProperty());
    }

    public SaveProvince[][] getProvincesMap() {
        return provincesMap;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Save getSave() {
        return save;
    }

    public VBox getEditPane() {
        return editPane;
    }

    public ObservableList<SaveCountry> getPlayableCountries() {
        return playableCountries;
    }

    public ObservableList<SaveCountry> getCountriesAlive() {
        return countriesAlive;
    }

    public ObservableList<Culture> getCultures() {
        return cultures;
    }

    public ObservableList<SaveReligion> getReligions() {
        return religions;
    }

    public ObservableList<SaveReligion> getPlayableReligions() {
        return playableReligions;
    }

    public ObservableList<TradeGood> getTradeGoods() {
        return tradeGoods;
    }

    public ObservableList<TradeNode> getTradeNodes() {
        return tradeNodes;
    }

    public ObservableList<SaveProvince> getCities() {
        return cities;
    }

    public SegmentedButton getTabsSegmentedButton() {
        return tabsSegmentedButton;
    }

    public SaveProvince getSelectedProvince() {
        return selectedProvince;
    }
}
