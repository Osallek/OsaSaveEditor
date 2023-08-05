package fr.osallek.osasaveeditor.controller.mapview;

import fr.osallek.eu4parser.model.game.localisation.Eu4Language;
import fr.osallek.eu4parser.model.save.Save;
import fr.osallek.eu4parser.model.save.SaveReligion;
import fr.osallek.eu4parser.model.save.country.SaveCountry;
import fr.osallek.eu4parser.model.save.province.SaveProvince;
import fr.osallek.osasaveeditor.common.Constants;
import fr.osallek.osasaveeditor.controller.EditorController;
import fr.osallek.osasaveeditor.controller.control.ClearableComboBox;
import fr.osallek.osasaveeditor.controller.control.ClearableSpinnerDouble;
import fr.osallek.osasaveeditor.controller.control.TableView2ReformationCenter;
import fr.osallek.osasaveeditor.controller.converter.CountryStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.CountryStringConverter;
import fr.osallek.osasaveeditor.controller.converter.GoldenBullStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.GoldenBullStringConverter;
import fr.osallek.osasaveeditor.controller.converter.SaveReligionStringConverter;
import fr.osallek.osasaveeditor.controller.object.GoldenBull;
import fr.osallek.osasaveeditor.controller.object.ReformationCenter;
import fr.osallek.osasaveeditor.controller.pane.CustomPropertySheet;
import fr.osallek.osasaveeditor.controller.pane.TableViewDialog;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ButtonItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.CheckBoxItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableComboBoxItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableSliderItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableSpinnerItem;
import fr.osallek.osasaveeditor.controller.validator.CustomGraphicValidationDecoration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.apache.commons.collections4.CollectionUtils;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.decoration.CompoundValidationDecoration;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class ReligionPropertySheet extends PropertySheet<SaveReligion> {

    private final ValidationSupport validationSupport;

    private CheckBoxItem enableField;

    private ClearableComboBoxItem<SaveCountry> defenderOfFaithField;

    private ClearableComboBoxItem<SaveCountry> papalControllerField;

    private ClearableComboBoxItem<SaveCountry> crusadeTargetField;

    private ClearableSliderItem reformDesireField;

    private ClearableSpinnerItem<Double> curiaTreasuryField;

    private ClearableComboBoxItem<GoldenBull> goldenBullField;

    private ObservableList<ReformationCenter> reformationCenters;

    public ReligionPropertySheet(Save save, SaveReligion religion, ObservableList<SaveCountry> countriesAlive, ObservableList<SaveProvince> provinces) {
        super(save, religion);
        String category = SaveReligionStringConverter.INSTANCE.toString(religion);

        List<CustomPropertySheet.Item<?>> items = new ArrayList<>();

        this.validationSupport = new ValidationSupport();
        this.validationSupport.setValidationDecorator(
                new CompoundValidationDecoration(new CustomGraphicValidationDecoration(), new StyleClassValidationDecoration("validation-error", null)));

        if (this.t.hasDate() && this.t.getEnable() == null) {
            this.enableField = new CheckBoxItem(category,
                                                this.save.getGame().getLocalisationClean("ENABLE", Eu4Language.getByLocale(Constants.LOCALE)),
                                                this.t.getEnable() != null);
            items.add(this.enableField);
        }

        ObservableList<SaveCountry> countries = FXCollections.observableArrayList(countriesAlive.stream()
                                                                                                .filter(country -> religion.equals(country.getReligion()))
                                                                                                .toList());
        countries.add(0, EditorController.dummyCountry);

        ObservableList<SaveCountry> otherReligionGroupCountries =
                FXCollections.observableArrayList(countriesAlive.stream()
                                                                .filter(country -> country.getReligion() == null
                                                                                   || !country.getReligion()
                                                                                              .getReligionGroup()
                                                                                              .equals(religion.getReligionGroup()))
                                                                .toList());
        otherReligionGroupCountries.add(0, EditorController.dummyCountry);

        FilteredList<SaveCountry> possibleDefenders = countries.filtered(c -> c.getOverlord() == null && !EditorController.dummyCountry.equals(c));
        if (this.t.hasDefenderOfFaith() && !possibleDefenders.isEmpty()) {
            this.defenderOfFaithField = new ClearableComboBoxItem<>(category,
                                                                    this.save.getGame()
                                                                             .getLocalisationClean("defender_of_faith",
                                                                                                   Eu4Language.getByLocale(Constants.LOCALE)),
                                                                    possibleDefenders,
                                                                    this.t.getDefender() == null ? EditorController.dummyCountry :
                                                                    this.t.getDefender(),
                                                                    new ClearableComboBox<>(new SearchableComboBox<>(), this.t::getDefender));
            this.defenderOfFaithField.setConverter(new CountryStringConverter());
            this.defenderOfFaithField.setCellFactory(new CountryStringCellFactory());
            items.add(this.defenderOfFaithField);
        }

        if (this.t.hasPapacy() && Boolean.TRUE.equals(this.t.getPapacy().getPapacyActive())) {
            FilteredList<SaveCountry> possibleControllers = countries.filtered(country -> country.getCapital() != null
                                                                                          && country.getCapital().getContinent() != null
                                                                                          && !EditorController.dummyCountry.equals(country));

            if (!possibleControllers.isEmpty()) {
                this.papalControllerField = new ClearableComboBoxItem<>(category,
                                                                        this.save.getGame()
                                                                                 .getLocalisationClean("HINT_PAPALCONTROLLER_TITLE",
                                                                                                       Eu4Language.getByLocale(Constants.LOCALE)),
                                                                        possibleControllers,
                                                                        this.t.getPapacy().getController(),
                                                                        new ClearableComboBox<>(new SearchableComboBox<>(),
                                                                                                () -> this.t.getPapacy().getController()));
                this.papalControllerField.setConverter(new CountryStringConverter());
                this.papalControllerField.setCellFactory(new CountryStringCellFactory());
                items.add(this.papalControllerField);
            }

            if (otherReligionGroupCountries.contains(EditorController.dummyCountry) ?
                otherReligionGroupCountries.size() > 1 : !otherReligionGroupCountries.isEmpty()) {

                this.crusadeTargetField = new ClearableComboBoxItem<>(category,
                                                                      this.save.getGame()
                                                                               .getLocalisationClean("IS_CRUSADE_TARGET",
                                                                                                     Eu4Language.getByLocale(Constants.LOCALE)),
                                                                      otherReligionGroupCountries,
                                                                      this.t.getPapacy().getCrusadeTarget(),
                                                                      new ClearableComboBox<>(new SearchableComboBox<>(),
                                                                                              () -> this.t.getPapacy().getCrusadeTarget()));
                this.crusadeTargetField.setConverter(new CountryStringConverter());
                this.crusadeTargetField.setCellFactory(new CountryStringCellFactory());
                items.add(this.crusadeTargetField);
            }

            this.reformDesireField = new ClearableSliderItem(category,
                                                             save.getGame()
                                                                 .getLocalisationClean("HINT_REFORMDESIRE_TITLE", Eu4Language.getByLocale(Constants.LOCALE)),
                                                             0, 200,
                                                             this.t.getPapacy().getReformDesire(),
                                                             () -> this.t.getPapacy().getReformDesire());
            items.add(this.reformDesireField);

            this.curiaTreasuryField = new ClearableSpinnerItem<>(category,
                                                                 save.getGame()
                                                                     .getLocalisationCleanNoPunctuation("CURIA_TREASURY",
                                                                                                        Eu4Language.getByLocale(Constants.LOCALE)),
                                                                 new ClearableSpinnerDouble(0, Math.max(this.t.getPapacy().getCuriaTreasury(), 1_000_000),
                                                                                            this.t.getPapacy().getCuriaTreasury(),
                                                                                            100,
                                                                                            () -> this.t.getPapacy().getCuriaTreasury()));
            items.add(this.curiaTreasuryField);

            ObservableList<GoldenBull> goldenBulls = FXCollections.observableArrayList(this.save.getGame()
                                                                                                .getGoldenBulls()
                                                                                                .stream()
                                                                                                .map(g -> new GoldenBull(g, this.save))
                                                                                                .toList());
            goldenBulls.add(0, new GoldenBull(this.save));

            this.goldenBullField = new ClearableComboBoxItem<>(category,
                                                               this.save.getGame()
                                                                        .getLocalisationClean("GOLDEN_BULL_PICKED_TITLE",
                                                                                              Eu4Language.getByLocale(Constants.LOCALE)),
                                                               goldenBulls,
                                                               new GoldenBull(this.t.getPapacy().getGoldenBull(), this.save),
                                                               new ClearableComboBox<>(new SearchableComboBox<>(),
                                                                                       () -> new GoldenBull(this.t.getPapacy().getGoldenBull(),
                                                                                                            this.save)));
            this.goldenBullField.setConverter(new GoldenBullStringConverter());
            this.goldenBullField.setCellFactory(new GoldenBullStringCellFactory());
            items.add(this.goldenBullField);
        }

        if (CollectionUtils.isNotEmpty(provinces) && this.t.getGameReligion() != null
            && CollectionUtils.isNotEmpty(this.t.getGameReligion().getAllowedCenterConversion())) {
            this.reformationCenters = FXCollections.observableArrayList();
            this.reformationCenters.setAll(this.t.getReformationCenters().stream().map(ReformationCenter::new).toList());
            ButtonItem reformationCentersButton = new ButtonItem(category, null,
                                                                 save.getGame()
                                                                     .getLocalisationClean("protestant_center_of_reformation",
                                                                                           Eu4Language.getByLocale(Constants.LOCALE)),
                                                                 items.isEmpty() ? 1 : 2);
            reformationCentersButton.getButton().setOnAction(event -> {
                TableView2ReformationCenter tableView2 = new TableView2ReformationCenter(this.save, this.reformationCenters, provinces);
                TableViewDialog<ReformationCenter> dialog = new TableViewDialog<>(this.save,
                                                                                  tableView2,
                                                                                  this.save.getGame()
                                                                                           .getLocalisationClean("protestant_center_of_reformation",
                                                                                                                 Eu4Language.getByLocale(Constants.LOCALE)),
                                                                                  list -> new ReformationCenter(this.t,
                                                                                                                provinces.stream()
                                                                                                                         .filter(Predicate.not(
                                                                                                                                 SaveProvince::centerOfReligion))
                                                                                                                         .findFirst()
                                                                                                                         .get()),
                                                                                  () -> this.reformationCenters);
                dialog.setDisableAddProperty(tableView2.disableAddPropertyProperty());
                Optional<List<ReformationCenter>> modifierList = dialog.showAndWait();

                modifierList.ifPresent(this.reformationCenters::setAll);
            });
            items.add(reformationCentersButton);
        }

        this.propertySheet.getItems().setAll(items);
    }

    public Set<CustomPropertySheet.Item<?>> internalUpdate(SaveReligion saveReligion) {
        if (this.enableField != null) {
            this.enableField.setValue(this.t.getEnable() != null);
        }

        if (this.defenderOfFaithField != null) {
            this.defenderOfFaithField.setValue(this.t.getDefender() == null ? EditorController.dummyCountry : this.t.getDefender());
        }

        if (this.papalControllerField != null) {
            this.papalControllerField.setValue(this.t.getPapacy().getController());
        }

        if (this.crusadeTargetField != null) {
            this.crusadeTargetField.setValue(this.t.getPapacy().getCrusadeTarget());
        }

        if (this.reformDesireField != null) {
            this.reformDesireField.setValue(this.t.getPapacy().getReformDesire());
        }

        if (this.curiaTreasuryField != null) {
            this.curiaTreasuryField.setValue(this.t.getPapacy().getCuriaTreasury());
        }

        if (this.goldenBullField != null) {
            this.goldenBullField.setValue(new GoldenBull(this.t.getPapacy().getGoldenBull(), this.save));
        }

        if (this.reformationCenters != null) {
            this.reformationCenters.setAll(this.t.getReformationCenters().stream().map(ReformationCenter::new).toList());
        }

        return new HashSet<>(this.propertySheet.getItems());
    }

    public void validate() {
        if (this.enableField != null) {
            if (this.enableField.isSelected()) {
                this.t.setEnable(this.save.getDate());
            }
        }

        if (this.defenderOfFaithField != null) {
            if (!Objects.equals(this.t.getDefender(), this.defenderOfFaithField.getValue())) {
                this.t.setDefender(this.defenderOfFaithField.getValue(), this.save.getDate());
            }
        }

        if (this.papalControllerField != null) {
            if (!Objects.equals(this.t.getPapacy().getController(), this.papalControllerField.getValue())) {
                this.t.getPapacy().setController(this.papalControllerField.getValue());
            }
        }

        if (this.crusadeTargetField != null) {
            if (!Objects.equals(this.t.getPapacy().getCrusadeTarget(), this.crusadeTargetField.getValue())) {
                this.t.getPapacy().setCrusadeTarget(this.crusadeTargetField.getValue());
            }
        }

        if (this.reformDesireField != null) {
            if (!Objects.equals(this.t.getPapacy().getReformDesire(), this.reformDesireField.getDoubleValue())) {
                this.t.getPapacy().setReformDesire(this.reformDesireField.getDoubleValue());
            }
        }

        if (this.curiaTreasuryField != null) {
            if (!Objects.equals(this.t.getPapacy().getCuriaTreasury(), this.curiaTreasuryField.getValue())) {
                this.t.getPapacy().setCuriaTreasury(this.curiaTreasuryField.getValue());
            }
        }

        if (this.goldenBullField != null) {
            if (this.goldenBullField.getValue() == null) {
                if (this.t.getPapacy().getGoldenBull() != null) {
                    this.t.getPapacy().setGoldenBull(null);
                }
            } else {
                if (this.t.getPapacy().getGoldenBull() == null ||
                    !Objects.equals(this.t.getPapacy().getGoldenBull().getName(), this.goldenBullField.getValue().getGoldenBull())) {
                    this.t.getPapacy().setGoldenBull(this.save.getGame().getGoldenBull(this.goldenBullField.getValue().getGoldenBull()));
                }
            }
        }

        if (this.reformationCenters != null) {
            if (this.t.getReformationCenters().size() != this.reformationCenters.size()
                || this.reformationCenters.stream().anyMatch(ReformationCenter::isChanged)) {
                this.t.getReformationCenters().forEach(center -> this.reformationCenters.stream()
                                                                                        .filter(r -> center.getProvince().equals(r.getProvince()))
                                                                                        .findFirst()
                                                                                        .ifPresentOrElse(r -> this.reformationCenters.remove(r),
                                                                                                         () -> this.t.removeReformationCenter(center)));
                this.reformationCenters.forEach(reformationCenter -> this.t.addReformationCenter(reformationCenter.getProvince()));
            }
        }

    }

    public ValidationSupport getValidationSupport() {
        return validationSupport;
    }
}
