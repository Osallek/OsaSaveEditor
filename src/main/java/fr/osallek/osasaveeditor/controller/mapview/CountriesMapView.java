package fr.osallek.osasaveeditor.controller.mapview;

import fr.osallek.eu4parser.common.Eu4MapUtils;
import fr.osallek.eu4parser.model.save.Save;
import fr.osallek.eu4parser.model.save.province.SaveProvince;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import fr.osallek.osasaveeditor.controller.converter.CountryStringConverter;
import fr.osallek.osasaveeditor.controller.converter.ProvinceIdStringConverter;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import org.springframework.context.MessageSource;

import java.io.IOException;
import java.util.Objects;

public class CountriesMapView extends AbstractMapView {

    private final Save save;

    private final ToggleButton countryButton;

    private final ToggleButton provinceButton;

    private final ProvincePropertySheet provinceSheet;

    private final CountryPropertySheet countrySheet;

    public CountriesMapView(MapViewContainer mapViewContainer, Save save, MessageSource messageSource) {
        super(mapViewContainer, MapViewType.COUNTRIES_MAP_VIEW);
        this.save = save;
        this.provinceSheet = new ProvincePropertySheet(messageSource, this.mapViewContainer.getSave(),
                                                       this.mapViewContainer.getPlayableCountries(),
                                                       this.mapViewContainer.getCultures(),
                                                       this.mapViewContainer.getPlayableReligions(),
                                                       this.mapViewContainer.getTradeGoods(),
                                                       this.mapViewContainer.getTradeNodes());
        this.provinceSheet.countryChangedProperty().addListener((observable, oldValue, newValue) -> {
            if (Boolean.FALSE.equals(oldValue) && Boolean.TRUE.equals(newValue)) {
                try {
                    draw();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        this.provinceSheet.getPropertySheet().visibleProperty().set(false);

        this.countrySheet = new CountryPropertySheet(messageSource, this.mapViewContainer.getSave(),
                                                     this.mapViewContainer.getCountriesAlive(),
                                                     this.mapViewContainer.getCultures(),
                                                     this.mapViewContainer.getPlayableReligions());
        this.countrySheet.colorChangedProperty().addListener((observable, oldValue, newValue) -> {
            if (Boolean.FALSE.equals(oldValue) && Boolean.TRUE.equals(newValue)) {
                try {
                    draw();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        this.countrySheet.getPropertySheet().visibleProperty().set(false);

        this.countryButton = new ToggleButton(OsaSaveEditorUtils.localize("TRIGGER_COUNTRY", this.mapViewContainer.getSave().getGame()));
        this.countryButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (Boolean.FALSE.equals(oldValue) && Boolean.TRUE.equals(newValue)) {
                selectCountryButton();
            }
        });
        this.countryButton.disableProperty().bind(this.countryButton.selectedProperty());

        this.provinceButton = new ToggleButton(OsaSaveEditorUtils.localize("UNKNOWN_LOC", this.mapViewContainer.getSave().getGame()));
        this.provinceButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (Boolean.FALSE.equals(oldValue) && Boolean.TRUE.equals(newValue)) {
                selectProvinceButton();
            }
        });
        this.provinceButton.disableProperty().bind(this.provinceButton.selectedProperty());

        this.mapViewContainer.addSheet(this.countrySheet.getPropertySheet());
        this.mapViewContainer.addSheet(this.provinceSheet.getPropertySheet());
    }

    @Override
    public void draw() throws IOException {
        GraphicsContext graphicsContext = this.mapViewContainer.getCanvas().getGraphicsContext2D();
        graphicsContext.drawImage(OsaSaveEditorUtils.bufferedToView(Eu4MapUtils.generateMapPng(this.save.getGame(), province -> {
            Color color = getOwnerColor(this.save.getProvince(province.getId()));

            return new java.awt.Color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getOpacity());
        })).getImage(), 0, 0);
    }

    @Override
    public void onProvinceSelected(SaveProvince province) {
        if (!Objects.equals(this.provinceSheet.getT(), province)) {
            this.provinceSheet.update(province);
        }

        if (!Objects.equals(this.countrySheet.getT(), province.getOwner())) {
            this.countrySheet.update(province.getOwner());
        }

        if (Boolean.FALSE.equals(this.selected.getValue())) {
            this.mapViewContainer.addTabsSegmentedButtons(this.countryButton, this.provinceButton);

            if (this.countryButton.isSelected()) {
                selectCountryButton();
            } else if (this.provinceButton.isSelected()) {
                selectProvinceButton();
            }
        } else {
            this.mapViewContainer.updateTitle();

            if (this.countryButton.isSelected()) {
                if (this.countrySheet.getT() == null) {
                    this.mapViewContainer.bindSubmitButtonDisableProperty(new ReadOnlyBooleanWrapper(true));
                } else {
                    this.mapViewContainer.bindSubmitButtonDisableProperty(this.countrySheet.getValidationSupport().invalidProperty());
                }
            }
        }
    }

    @Override
    public String updateTitle(SaveProvince selectedProvince) {
        if (this.countryButton.isSelected()) {
            return selectedProvince.getOwner() == null ? getTitle(selectedProvince)
                                                       :
                   (CountryStringConverter.INSTANCE.toString(selectedProvince.getOwner()) + " (" + selectedProvince.getOwner().getTag() + ")");
        } else if (this.provinceButton.isSelected()) {
            return getTitle(selectedProvince);
        }

        return null;
    }

    @Override
    public void onSelected() {
        this.mapViewContainer.addTabsSegmentedButtons(this.countryButton, this.provinceButton);
    }

    private void selectCountryButton() {
        this.mapViewContainer.hideSaveSheet();
        this.mapViewContainer.hideSheet(this.provinceSheet.getPropertySheet());
        this.mapViewContainer.showSheet(this.countrySheet.getPropertySheet());
        this.mapViewContainer.updateTitle();
        this.mapViewContainer.setSubmitButtonOnAction(e -> {
            this.countrySheet.validate();
            this.mapViewContainer.updateTitle();
        });

        if (this.countrySheet.getT() == null) {
            this.mapViewContainer.bindSubmitButtonDisableProperty(new ReadOnlyBooleanWrapper(true));
        } else {
            this.mapViewContainer.bindSubmitButtonDisableProperty(this.countrySheet.getValidationSupport().invalidProperty());
        }
    }

    private void selectProvinceButton() {
        this.mapViewContainer.hideSaveSheet();
        this.mapViewContainer.hideSheet(this.countrySheet.getPropertySheet());
        this.mapViewContainer.showSheet(this.provinceSheet.getPropertySheet());
        this.mapViewContainer.updateTitle();
        this.mapViewContainer.setSubmitButtonOnAction(e -> {
            this.provinceSheet.validate();
            this.provinceSheet.update(this.provinceSheet.getT());
            this.mapViewContainer.updateTitle();
        });

        this.mapViewContainer.bindSubmitButtonDisableProperty(this.provinceSheet.getValidationSupport().invalidProperty());
    }

    private String getTitle(SaveProvince saveProvince) {
        String title = ProvinceIdStringConverter.INSTANCE.toString(saveProvince);

        if (saveProvince.getOwner() != null) {
            title += " - " + CountryStringConverter.INSTANCE.toString(saveProvince.getOwner());
        }

        return title;
    }

    private Color getOwnerColor(SaveProvince province) {
        if (province == null) {
            return Color.BLACK;
        } else if (province.getOwner() != null) {
            return OsaSaveEditorUtils.countryToMapColor(province.getOwner());
        } else {
            if (province.isOcean() || province.isLake()) {
                return Color.rgb(Eu4MapUtils.OCEAN_COLOR.getRed(), Eu4MapUtils.OCEAN_COLOR.getGreen(), Eu4MapUtils.OCEAN_COLOR.getBlue());
            } else if (province.isImpassable()) {
                return Color.rgb(Eu4MapUtils.IMPASSABLE_COLOR.getRed(), Eu4MapUtils.IMPASSABLE_COLOR.getGreen(), Eu4MapUtils.IMPASSABLE_COLOR.getBlue());
            } else {
                return Color.rgb(Eu4MapUtils.EMPTY_COLOR.getRed(), Eu4MapUtils.EMPTY_COLOR.getGreen(), Eu4MapUtils.EMPTY_COLOR.getBlue());
            }
        }
    }
}
