package com.osallek.eu4saveeditor.controller.mapview;

import com.osallek.clausewitzparser.common.ClausewitzUtils;
import com.osallek.eu4parser.model.Power;
import com.osallek.eu4parser.model.game.Culture;
import com.osallek.eu4parser.model.game.GovernmentReform;
import com.osallek.eu4parser.model.game.ImperialReform;
import com.osallek.eu4parser.model.save.Save;
import com.osallek.eu4parser.model.save.SaveReligion;
import com.osallek.eu4parser.model.save.country.Country;
import com.osallek.eu4saveeditor.controller.control.ClearableComboBox;
import com.osallek.eu4saveeditor.controller.control.ClearableSpinnerInt;
import com.osallek.eu4saveeditor.controller.control.ListSelectionViewImperialReform;
import com.osallek.eu4saveeditor.controller.control.RequiredComboBox;
import com.osallek.eu4saveeditor.controller.converter.PairCellFactory;
import com.osallek.eu4saveeditor.controller.converter.PairConverter;
import com.osallek.eu4saveeditor.controller.pane.CustomPropertySheet;
import com.osallek.eu4saveeditor.controller.pane.CustomPropertySheetSkin;
import com.osallek.eu4saveeditor.controller.pane.GovernmentReformsDialog;
import com.osallek.eu4saveeditor.controller.pane.ListSelectionViewDialog;
import com.osallek.eu4saveeditor.controller.propertyeditor.CustomPropertyEditorFactory;
import com.osallek.eu4saveeditor.controller.propertyeditor.item.ButtonItem;
import com.osallek.eu4saveeditor.controller.propertyeditor.item.CheckBoxItem;
import com.osallek.eu4saveeditor.controller.propertyeditor.item.ClearableComboBoxItem;
import com.osallek.eu4saveeditor.controller.propertyeditor.item.ClearableSpinnerItem;
import com.osallek.eu4saveeditor.controller.propertyeditor.item.ClearableTextItem;
import com.osallek.eu4saveeditor.controller.validator.CustomGraphicValidationDecoration;
import com.osallek.eu4saveeditor.i18n.SheetCategory;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.tuple.Pair;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.controlsfx.validation.decoration.CompoundValidationDecoration;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CountryPropertySheet extends VBox {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryPropertySheet.class);

    private Country country;

    private final CustomPropertySheet propertySheet;

    private final ValidationSupport validationSupport;

    private final ClearableTextItem nameField;

    private final CheckBoxItem wasPlayerField;

    private final ClearableSpinnerItem<Integer> admPointField;

    private final ClearableSpinnerItem<Integer> dipPointField;

    private final ClearableSpinnerItem<Integer> milPointField;

    private final ClearableComboBoxItem<Pair<String, String>> governmentRankField;

    private final ButtonItem governmentReformsButton;

    private final ObservableList<GovernmentReform> governmentReformsField;

    private final CustomPropertySheetSkin propertySheetSkin;

    public CountryPropertySheet(Save save, ObservableList<Country> playableCountries, ObservableList<Culture> cultures,
                                ObservableList<SaveReligion> religions) {
        this.propertySheet = new CustomPropertySheet();
        this.propertySheet.setPropertyEditorFactory(new CustomPropertyEditorFactory());
        this.propertySheet.setMode(CustomPropertySheet.Mode.CATEGORY);
        this.propertySheet.setCategoryComparator(Comparator.comparing(SheetCategory::getByLocale));
        this.propertySheet.setModeSwitcherVisible(false);
        this.propertySheet.setSearchBoxVisible(false);

        this.propertySheetSkin = new CustomPropertySheetSkin(this.propertySheet);
        this.propertySheet.setSkin(this.propertySheetSkin);

        //GENERAL
        this.nameField = new ClearableTextItem(SheetCategory.GENERAL, save.getGame().getLocalisation("LEDGER_NAME"));
        this.nameField.getTextField()
                      .getStylesheets()
                      .add(getClass().getClassLoader().getResource("styles/propertySheetsStyle.css").toExternalForm());

        this.wasPlayerField = new CheckBoxItem(SheetCategory.GENERAL, save.getGame().getLocalisationClean("WAS_PLAYER"), false);

        this.admPointField = new ClearableSpinnerItem<>(SheetCategory.GENERAL,
                                                        save.getGame().getLocalisationClean("ADM_POWER"),
                                                        new ClearableSpinnerInt(0, 999, 1));

        this.dipPointField = new ClearableSpinnerItem<>(SheetCategory.GENERAL,
                                                        save.getGame().getLocalisationClean("DIP_POWER"),
                                                        new ClearableSpinnerInt(0, 999, 1));

        this.milPointField = new ClearableSpinnerItem<>(SheetCategory.GENERAL,
                                                        save.getGame().getLocalisationClean("MIL_POWER"),
                                                        new ClearableSpinnerInt(0, 999, 1));

        //LEDGER_GOVERNMENT_NAME
        this.governmentRankField = new ClearableComboBoxItem<>(SheetCategory.COUNTRY_GOVERNMENT,
                                                               save.getGame().getLocalisation("GOV_RANK"),
                                                               FXCollections.observableArrayList(),
                                                               new ClearableComboBox<>(new RequiredComboBox<>()));
        this.governmentRankField.setConverter(new PairConverter());
        this.governmentRankField.setCellFactory(new PairCellFactory());

        this.governmentReformsButton = new ButtonItem(SheetCategory.COUNTRY_GOVERNMENT,
                                                      null,
                                                      save.getGame().getLocalisationClean("governmental_reforms"),
                                                      2);
        this.governmentReformsField = new ObservableListWrapper<>(new ArrayList<>());

        this.validationSupport = new ValidationSupport();
        this.validationSupport.registerValidator(this.nameField.getTextField(), Validator.createEmptyValidator("Text is required"));
        this.validationSupport.setValidationDecorator(new CompoundValidationDecoration(new CustomGraphicValidationDecoration(),
                                                                                       new StyleClassValidationDecoration("validation-error", null)));
    }

    public void update(Country country) {
        update(country, false);
    }

    public void update(Country country, boolean force) {
        if (force || this.country == null || !this.country.equals(country)) {
            this.country = country;

            if (this.country == null) {
                this.propertySheet.getItems().clear();
            } else {
                String expandedPaneName = this.propertySheetSkin.getAccordion().getExpandedPane() == null ? null :
                                          this.propertySheetSkin.getAccordion().getExpandedPane().getText();

                List<CustomPropertySheet.Item> items = new ArrayList<>();

                //GENERAL
                this.nameField.setValue(ClausewitzUtils.removeQuotes(this.country.getLocalizedName()));
                this.nameField.setSupplier(() -> ClausewitzUtils.removeQuotes(this.country.getLocalizedName()));
                this.nameField.setEditable(this.country.isNameEditable());
                items.add(this.nameField);

                this.wasPlayerField.setValue(this.country.wasPlayer());
                items.add(this.wasPlayerField);

                this.governmentRankField.getChoices().setAll(this.country.getGovernmentName().getRanks().values());
                this.governmentRankField.setValue(this.country.getGovernmentName().getRank(this.country.getGovernmentLevel()));
                this.governmentRankField.setSupplier(() -> this.country.getGovernmentName().getRank(this.country.getGovernmentLevel()));
                items.add(this.governmentRankField);

                this.governmentReformsField.setAll(this.country.getGovernment().getReforms());
                this.governmentReformsButton.getButton().setOnAction(event -> {
                    GovernmentReformsDialog dialog = new GovernmentReformsDialog(this.country, this.governmentReformsField);

                    Optional<List<GovernmentReform>> reforms = dialog.showAndWait();

                    reforms.ifPresent(this.governmentReformsField::setAll);
                });
                items.add(this.governmentReformsButton);

                this.admPointField.setSupplier(() -> this.country.getPowers().get(Power.ADM));
                this.admPointField.setMax(Math.max(this.country.getPowers().get(Power.ADM), 999));
                this.admPointField.setValue(this.country.getPowers().get(Power.ADM));
                items.add(this.admPointField);

                this.dipPointField.setSupplier(() -> this.country.getPowers().get(Power.DIP));
                this.dipPointField.setMax(Math.max(this.country.getPowers().get(Power.DIP), 999));
                this.dipPointField.setValue(this.country.getPowers().get(Power.DIP));
                items.add(this.dipPointField);

                this.milPointField.setSupplier(() -> this.country.getPowers().get(Power.MIL));
                this.milPointField.setMax(Math.max(this.country.getPowers().get(Power.MIL), 999));
                this.milPointField.setValue(this.country.getPowers().get(Power.MIL));
                items.add(this.milPointField);

                this.propertySheet.getItems().setAll(items);

                if (expandedPaneName != null) {
                    this.propertySheetSkin.getAccordion()
                                          .getPanes()
                                          .stream()
                                          .filter(titledPane -> titledPane.getText().equals(expandedPaneName))
                                          .findFirst()
                                          .ifPresent(titledPane -> this.propertySheetSkin.getAccordion().setExpandedPane(titledPane));
                }
            }
        }
    }

    public void validate(ActionEvent actionEvent) {
        if (!ClausewitzUtils.removeQuotes(this.country.getLocalizedName()).equals(this.nameField.getText())) {
            this.country.setLocalizedName(this.nameField.getText());
        }

        if (!Objects.equals(this.country.wasPlayer(), this.wasPlayerField.isSelected())) {
            this.country.setWasPlayer(this.wasPlayerField.isSelected());
        }

        if (!Objects.equals(this.country.getGovernmentName().getRank(this.country.getGovernmentLevel()), this.governmentRankField.getSelectedValue())) {
            this.country.setGovernmentRank(this.governmentRankField.getSelectedValue().getKey());
        }

        if (!Objects.equals(this.country.getGovernment().getReforms(), this.governmentReformsField)) {
            this.country.getGovernment().setReforms(this.governmentReformsField);
        }

        if (!Objects.equals(this.country.getPowers().get(Power.ADM), this.admPointField.getTrueValue())) {
            this.country.setPower(Power.ADM, this.admPointField.getTrueValue());
        }

        if (!Objects.equals(this.country.getPowers().get(Power.DIP), this.dipPointField.getTrueValue())) {
            this.country.setPower(Power.DIP, this.dipPointField.getTrueValue());
        }

        if (!Objects.equals(this.country.getPowers().get(Power.MIL), this.milPointField.getTrueValue())) {
            this.country.setPower(Power.MIL, this.milPointField.getTrueValue());
        }

        update(this.country, true);
    }

    public Country getCountry() {
        return country;
    }

    public ValidationSupport getValidationSupport() {
        return validationSupport;
    }

    public CustomPropertySheet getPropertySheet() {
        return propertySheet;
    }
}
