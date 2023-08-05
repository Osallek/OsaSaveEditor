package fr.osallek.osasaveeditor.controller.mapview;

import fr.osallek.clausewitzparser.common.ClausewitzUtils;
import fr.osallek.eu4parser.model.game.Culture;
import fr.osallek.eu4parser.model.game.localisation.Eu4Language;
import fr.osallek.eu4parser.model.save.SaveReligion;
import fr.osallek.eu4parser.model.save.country.Heir;
import fr.osallek.eu4parser.model.save.country.Monarch;
import fr.osallek.eu4parser.model.save.country.SaveCountry;
import fr.osallek.osasaveeditor.OsaSaveEditorApplication;
import fr.osallek.osasaveeditor.common.Constants;
import fr.osallek.osasaveeditor.controller.control.ClearableComboBox;
import fr.osallek.osasaveeditor.controller.control.ClearableSpinnerInt;
import fr.osallek.osasaveeditor.controller.control.TableView2Personalities;
import fr.osallek.osasaveeditor.controller.converter.CultureStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.CultureStringConverter;
import fr.osallek.osasaveeditor.controller.converter.SaveReligionStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.SaveReligionStringConverter;
import fr.osallek.osasaveeditor.controller.object.Personality;
import fr.osallek.osasaveeditor.controller.pane.CustomPropertySheet;
import fr.osallek.osasaveeditor.controller.pane.TableViewDialog;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ButtonItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableComboBoxItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableDatePickerItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableSliderItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableSpinnerItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableTextItem;
import fr.osallek.osasaveeditor.controller.validator.CustomGraphicValidationDecoration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.collections4.CollectionUtils;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.decoration.CompoundValidationDecoration;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MonarchPropertySheet extends PropertySheet<Monarch> {

    private final SaveCountry country;

    private final ClearableTextItem nameField;

    private final ClearableSpinnerItem<Integer> admPointField;

    private final ClearableSpinnerItem<Integer> dipPointField;

    private final ClearableSpinnerItem<Integer> milPointField;

    private final ClearableComboBoxItem<Culture> cultureField;

    private final ClearableComboBoxItem<SaveReligion> religionField;

    private final ClearableDatePickerItem birthDateField;

    private final ClearableSliderItem claimField;

    private final ObservableList<Personality> personalities = FXCollections.observableArrayList();

    private final ValidationSupport validationSupport;

    public MonarchPropertySheet(SaveCountry country, Monarch monarch, String name, ObservableList<Culture> cultures, ObservableList<SaveReligion> religions) {
        super(country.getSave(), monarch);
        this.country = country;

        List<CustomPropertySheet.Item<?>> items = new ArrayList<>();

        this.nameField = new ClearableTextItem(name, this.country.getSave().getGame().getLocalisationClean("LEDGER_NAME",
                                                                                                           Eu4Language.getByLocale(Constants.LOCALE)));
        this.nameField.getTextField().getStylesheets().add(OsaSaveEditorApplication.class.getResource("/styles/style.css").toExternalForm());
        this.nameField.setValue(ClausewitzUtils.removeQuotes(this.t.getName()));
        this.nameField.setSupplier(() -> ClausewitzUtils.removeQuotes(this.t.getName()));
        items.add(this.nameField);

        this.cultureField = new ClearableComboBoxItem<>(name,
                                                        this.country.getSave()
                                                                    .getGame()
                                                                    .getLocalisationClean("LEDGER_CULTURE", Eu4Language.getByLocale(Constants.LOCALE)),
                                                        cultures,
                                                        new ClearableComboBox<>(new SearchableComboBox<>()));
        this.cultureField.setConverter(CultureStringConverter.INSTANCE);
        this.cultureField.setCellFactory(CultureStringCellFactory.INSTANCE);
        this.cultureField.setValue(this.t.getCulture());
        this.cultureField.setSupplier(this.t::getCulture);
        items.add(this.cultureField);

        this.religionField = new ClearableComboBoxItem<>(name,
                                                         this.country.getSave()
                                                                     .getGame()
                                                                     .getLocalisationClean("LEDGER_RELIGION", Eu4Language.getByLocale(Constants.LOCALE)),
                                                         religions,
                                                         new ClearableComboBox<>(new SearchableComboBox<>()));
        this.religionField.setConverter(SaveReligionStringConverter.INSTANCE);
        this.religionField.setCellFactory(SaveReligionStringCellFactory.INSTANCE);
        this.religionField.setValue(this.t.getCountry().getSave().getReligions().getReligion(this.t.getReligionName()));
        this.religionField.setSupplier(() -> this.t.getCountry().getSave().getReligions().getReligion(this.t.getReligionName()));
        items.add(this.religionField);

        this.admPointField = new ClearableSpinnerItem<>(name,
                                                        this.country.getSave()
                                                                    .getGame()
                                                                    .getLocalisationCleanNoPunctuation("COURT_ADM", Eu4Language.getByLocale(Constants.LOCALE)),
                                                        new ClearableSpinnerInt(this.country.getSave().getGame().getMonarchMinSkill(),
                                                                                this.country.getSave().getGame().getMonarchMaxSkill(),
                                                                                this.t.getAdm(), 1, this.t::getAdm));
        items.add(this.admPointField);

        this.dipPointField = new ClearableSpinnerItem<>(name,
                                                        this.country.getSave()
                                                                    .getGame()
                                                                    .getLocalisationCleanNoPunctuation("COURT_DIP", Eu4Language.getByLocale(Constants.LOCALE)),
                                                        new ClearableSpinnerInt(this.country.getSave().getGame().getMonarchMinSkill(),
                                                                                this.country.getSave().getGame().getMonarchMaxSkill(),
                                                                                this.t.getDip(), 1, this.t::getDip));
        items.add(this.dipPointField);

        this.milPointField = new ClearableSpinnerItem<>(name,
                                                        this.country.getSave()
                                                                    .getGame()
                                                                    .getLocalisationCleanNoPunctuation("COURT_MIL", Eu4Language.getByLocale(Constants.LOCALE)),
                                                        new ClearableSpinnerInt(this.country.getSave().getGame().getMonarchMinSkill(),
                                                                                this.country.getSave().getGame().getMonarchMaxSkill(),
                                                                                this.t.getMil(), 1, this.t::getMil));
        items.add(this.milPointField);

        if (ChronoUnit.YEARS.between(this.t.getBirthDate(), this.country.getSave().getDate()) >= this.country.getSave().getGame().getAgeOfAdulthood()) {
            this.birthDateField = new ClearableDatePickerItem(name, this.country.getSave()
                                                                                .getGame()
                                                                                .getLocalisationClean("DATE_OF_BIRTH_REQUIRED",
                                                                                                      Eu4Language.getByLocale(Constants.LOCALE)),
                                                              this.t.getBirthDate(), this.t::getBirthDate,
                                                              null,
                                                              this.country.getSave()
                                                                          .getDate()
                                                                          .minusYears(this.country.getSave().getGame().getAgeOfAdulthood()));
        } else {
            this.birthDateField = new ClearableDatePickerItem(name, this.country.getSave()
                                                                                .getGame()
                                                                                .getLocalisationClean("DATE_OF_BIRTH_REQUIRED",
                                                                                                      Eu4Language.getByLocale(Constants.LOCALE)),
                                                              this.t.getBirthDate(), this.t::getBirthDate,
                                                              this.country.getSave()
                                                                          .getDate()
                                                                          .minusYears(this.country.getSave().getGame().getAgeOfAdulthood())
                                                                          .plusDays(1), this.country.getSave().getDate());
        }
        items.add(this.birthDateField);

        if (Heir.class.equals(this.t.getClass()) && ((Heir) this.t).getClaim() != null) {
            this.claimField = new ClearableSliderItem(name, this.country.getSave()
                                                                        .getGame()
                                                                        .getLocalisationClean("legitimacy", Eu4Language.getByLocale(Constants.LOCALE)), 0, 100,
                                                      ((Heir) this.t).getClaim(), ((Heir) this.t)::getClaim);
            items.add(this.claimField);
        } else {
            this.claimField = null;
        }

        this.personalities.setAll(this.t.getPersonalities() == null ? new ArrayList<>() : this.t.getPersonalities()
                                                                                                .getPersonalities()
                                                                                                .stream()
                                                                                                .map(Personality::new)
                                                                                                .collect(Collectors.toList()));
        ButtonItem personalitiesButton = new ButtonItem(name, null, this.country.getSave()
                                                                                .getGame()
                                                                                .getLocalisationClean("LEDGER_PERSONALITIES",
                                                                                                      Eu4Language.getByLocale(Constants.LOCALE)), 2);
        personalitiesButton.getButton().setOnAction(event -> {
            TableView2Personalities view2Personalities = new TableView2Personalities(this.country, this.t, this.personalities,
                                                                                     this.country.getSave()
                                                                                                 .getGame()
                                                                                                 .getRulerPersonalities()
                                                                                                 .stream()
                                                                                                 .map(Personality::new)
                                                                                                 .collect(Collectors.toCollection(
                                                                                                         FXCollections::observableArrayList)));
            TableViewDialog<Personality> dialog =
                    new TableViewDialog<>(this.country.getSave(),
                                          view2Personalities,
                                          this.country.getSave()
                                                      .getGame()
                                                      .getLocalisationClean("LEDGER_PERSONALITIES", Eu4Language.getByLocale(Constants.LOCALE)),
                                          list -> this.country.getSave()
                                                              .getGame()
                                                              .getRulerPersonalities()
                                                              .stream()
                                                              .map(Personality::new)
                                                              .filter(personality -> list.stream().noneMatch(i -> i.equals(personality)))
                                                              .filter(personality -> personality.getRulerPersonality().isMonarchValid(monarch))
                                                              .findFirst()
                                                              .get(),
                                          () -> this.personalities);
            dialog.setDisableAddProperty(view2Personalities.disableAddPropertyProperty());
            Optional<List<Personality>> rulerPersonalities = dialog.showAndWait();

            rulerPersonalities.ifPresent(this.personalities::setAll);
        });
        items.add(personalitiesButton);

        this.validationSupport = new ValidationSupport();
        this.validationSupport.setValidationDecorator(new CompoundValidationDecoration(new CustomGraphicValidationDecoration(),
                                                                                       new StyleClassValidationDecoration("validation-error", null)));

        this.propertySheet.getItems().setAll(items);
    }

    @Override
    public Set<CustomPropertySheet.Item<?>> internalUpdate(Monarch monarch) {
        return new HashSet<>(this.propertySheet.getItems());
    }

    public void internalValidate() {
        if (!ClausewitzUtils.removeQuotes(this.t.getName()).equals(this.nameField.getValue())) {
            this.t.setName(ClausewitzUtils.addQuotes(this.nameField.getValue()));
        }

        if (!Objects.deepEquals(this.t.getCulture(), this.cultureField.getValue())) {
            this.t.setCulture(this.cultureField.getValue());
        }

        if (!Objects.deepEquals(this.t.getReligionName(), this.religionField.getValue().getName())) {
            this.t.setReligion(this.religionField.getValue());
        }

        if (!Objects.deepEquals(this.t.getAdm(), this.admPointField.getValue())) {
            this.t.setAdm(this.admPointField.getValue());
        }

        if (!Objects.deepEquals(this.t.getDip(), this.dipPointField.getValue())) {
            this.t.setDip(this.dipPointField.getValue());
        }

        if (!Objects.deepEquals(this.t.getMil(), this.milPointField.getValue())) {
            this.t.setMil(this.milPointField.getValue());
        }

        if (!Objects.deepEquals(this.t.getBirthDate(), this.birthDateField.getValue())) {
            this.t.setBirthDate(this.birthDateField.getValue());
        }

        if (this.claimField != null && !Objects.deepEquals(((Heir) this.t).getClaim(), this.claimField.getDoubleValue())) {
            ((Heir) this.t).setClaim(this.claimField.getDoubleValue());
        }

        if ((this.t.getPersonalities() == null && CollectionUtils.isNotEmpty(this.personalities))
            || (this.t.getPersonalities() != null
                && !Objects.equals(this.t.getPersonalities()
                                         .getPersonalities()
                                         .stream()
                                         .map(Personality::new)
                                         .collect(Collectors.toList()), this.personalities))) {
            if (this.t.getPersonalities() != null) {
                this.t.getPersonalities()
                      .getPersonalities()
                      .forEach(personality -> this.personalities.stream()
                                                                .filter(p -> p.getRulerPersonality().equals(personality))
                                                                .findFirst()
                                                                .ifPresentOrElse(this.personalities::remove,
                                                                                 () -> this.t.removePersonality(personality)));
            }

            this.personalities.stream().map(Personality::getRulerPersonality).forEach(this.t::addPersonality);
        }
    }

    public ValidationSupport getValidationSupport() {
        return validationSupport;
    }
}
