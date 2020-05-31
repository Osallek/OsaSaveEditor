package com.osallek.eu4saveeditor.controller.mapview;

import com.osallek.clausewitzparser.common.ClausewitzUtils;
import com.osallek.eu4parser.model.game.Culture;
import com.osallek.eu4parser.model.game.Religion;
import com.osallek.eu4parser.model.game.TradeGood;
import com.osallek.eu4parser.model.save.Save;
import com.osallek.eu4parser.model.save.country.Country;
import com.osallek.eu4parser.model.save.province.SaveProvince;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class CountriesMapView extends AbstractMapView {

    private SaveProvince selectedProvince;

    private final ToggleButton countryButton;

    private final ToggleButton provinceButton;

    private final ProvincePropertySheet provinceSheet;

    private final Button submitButton;

    public CountriesMapView(SaveProvince[][] provincesMap, Canvas canvas, VBox editPane, Save save,
                            ObservableList<Country> playableCountries, ObservableList<Culture> cultures,
                            ObservableList<Religion> religions, ObservableList<TradeGood> tradeGoods) {
        super(provincesMap, canvas, editPane, save, MapViewType.COUNTRIES_MAP_VIEW, playableCountries, cultures, religions, tradeGoods);
        this.provinceSheet = new ProvincePropertySheet(save, this.playableCountries, this.cultures, this.religions, this.tradeGoods);

        this.countryButton = new ToggleButton(save.getGame().getLocalisation("TRIGGER_COUNTRY"));
        this.countryButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (Boolean.FALSE.equals(oldValue) && Boolean.TRUE.equals(newValue)) {
                this.editPane.getChildren().remove(this.provinceSheet.getPropertySheet());
                this.titleLabel.setText(this.selectedProvince.getCountry().getLocalizedName());
            }
        });
        this.countryButton.disableProperty().bind(this.countryButton.selectedProperty());

        this.provinceButton = new ToggleButton(save.getGame().getLocalisation("ANY_ALL_PROVINCE"));
        this.provinceButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (Boolean.FALSE.equals(oldValue) && Boolean.TRUE.equals(newValue)
                && !this.editPane.getChildren().contains(this.provinceSheet.getPropertySheet())) {
                this.editPane.getChildren().add(2, this.provinceSheet.getPropertySheet());
                this.titleLabel.setText(getTitle(this.selectedProvince));
            }
        });
        this.provinceButton.disableProperty().bind(this.provinceButton.selectedProperty());

        this.submitButton = new Button(save.getGame().getLocalisation("DONE"));
        this.submitButton.setOnAction(e -> {
            this.provinceSheet.validate(e);
            this.provinceSheet.update(this.provinceSheet.getProvince());
            this.titleLabel.setText(getTitle(this.provinceSheet.getProvince()));
        });
        this.submitButton.disableProperty().bind(this.provinceSheet.getValidationSupport().invalidProperty());
    }

    @Override
    public void draw() {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        for (int x = 0; x < this.provincesMap.length; x++) {
            for (int y = 0; y < this.provincesMap[x].length; y++) {
                SaveProvince province = this.provincesMap[x][y];
                int startY = y;
                while (y < this.provincesMap[x].length && this.provincesMap[x][y].equals(province)) {
                    y++;
                }

                gc.setFill(getOwnerColor(province));
                gc.fillRect(x, startY, 1, (double) y - startY);
            }
        }

        drawProvincesBorders();
    }

    @Override
    public void onProvinceSelected(SaveProvince province) {
        this.selectedProvince = province;

        if (this.selected) {
            this.titleLabel.setText(getTitle(province));
            this.provinceSheet.update(province);
            this.submitButton.disableProperty().bind(this.provinceSheet.getValidationSupport().invalidProperty());
        } else {
            this.editPane.getChildren().clear();
            this.clearTabsSegmentedButton();

            this.tabsSegmentedButton.getButtons().add(this.countryButton);
            this.tabsSegmentedButton.getButtons().add(this.provinceButton);

            this.tabsSegmentedButton.getButtons()
                                    .forEach(toggleButton ->
                                                     toggleButton.prefWidthProperty()
                                                                 .bind(this.tabsSegmentedButton.widthProperty()
                                                                                               .divide(this.tabsSegmentedButton
                                                                                                               .getButtons()
                                                                                                               .size())));

            this.editPane.getChildren().add(this.tabsSegmentedButton);

            this.titleLabel.setText(getTitle(province));
            this.titleLabel.setVisible(true);

            this.editPane.getChildren().add(this.titleLabel);

            this.editPane.getChildren().add(this.provinceSheet.update(province));

            this.editPane.getChildren().add(this.submitButton);
        }
    }

    @Override
    public void removeSheets() {
        this.editPane.getChildren().remove(this.provinceSheet.getPropertySheet());
    }

    private String getTitle(SaveProvince saveProvince) {
        String title = ClausewitzUtils.removeQuotes(saveProvince.getName());

        if (saveProvince.getCountry() != null) {
            title += " (" + saveProvince.getCountry().getLocalizedName() + ")";
        }

        return title;
    }

    private Color getOwnerColor(SaveProvince province) {
        if (province.getCountry() != null) {
            return countryToMapColor(province.getCountry());
        } else {
            if (province.isOcean()) {
                return Color.rgb(68, 107, 163);
            } else if (province.isImpassable()) {
                return Color.rgb(94, 94, 94);
            } else {
                return Color.rgb(148, 146, 149);
            }
        }
    }

    private Color countryToMapColor(Country country) {
        return Color.rgb(country.getColors().getCountryColor().getRed(),
                         country.getColors().getCountryColor().getGreen(),
                         country.getColors().getCountryColor().getBlue());
    }
}
