package fr.osallek.osasaveeditor.controller.mapview;

import fr.osallek.clausewitzparser.common.ClausewitzUtils;
import fr.osallek.eu4parser.common.Eu4Utils;
import fr.osallek.eu4parser.common.NumbersUtils;
import fr.osallek.eu4parser.model.game.Building;
import fr.osallek.eu4parser.model.game.Culture;
import fr.osallek.eu4parser.model.game.ParliamentBribe;
import fr.osallek.eu4parser.model.game.TradeGood;
import fr.osallek.eu4parser.model.game.TradeNode;
import fr.osallek.eu4parser.model.game.localisation.Eu4Language;
import fr.osallek.eu4parser.model.save.Save;
import fr.osallek.eu4parser.model.save.SaveGreatProject;
import fr.osallek.eu4parser.model.save.SaveReligion;
import fr.osallek.eu4parser.model.save.country.SaveCountry;
import fr.osallek.eu4parser.model.save.province.ProvinceBuilding;
import fr.osallek.eu4parser.model.save.province.SaveProvince;
import fr.osallek.osasaveeditor.OsaSaveEditorApplication;
import fr.osallek.osasaveeditor.common.Constants;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import fr.osallek.osasaveeditor.controller.control.ClearableCheckComboBox;
import fr.osallek.osasaveeditor.controller.control.ClearableComboBox;
import fr.osallek.osasaveeditor.controller.control.ClearableSpinnerDouble;
import fr.osallek.osasaveeditor.controller.control.ClearableSpinnerInt;
import fr.osallek.osasaveeditor.controller.control.SelectableGridView;
import fr.osallek.osasaveeditor.controller.control.TableView2Modifier;
import fr.osallek.osasaveeditor.controller.converter.CountryStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.CountryStringConverter;
import fr.osallek.osasaveeditor.controller.converter.CultureStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.CultureStringConverter;
import fr.osallek.osasaveeditor.controller.converter.ParliamentBribeStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.ParliamentBribeStringConverter;
import fr.osallek.osasaveeditor.controller.converter.SaveReligionStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.SaveReligionStringConverter;
import fr.osallek.osasaveeditor.controller.converter.TradeGoodStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.TradeGoodStringConverter;
import fr.osallek.osasaveeditor.controller.converter.TradeNodeStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.TradeNodeStringConverter;
import fr.osallek.osasaveeditor.controller.object.Modifier;
import fr.osallek.osasaveeditor.controller.pane.CustomPropertySheet;
import fr.osallek.osasaveeditor.controller.pane.TableViewDialog;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ButtonItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableCheckBoxItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableCheckComboBoxItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableComboBoxItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableSliderItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableSpinnerItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableTextItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.HBoxItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.SelectableGridViewItem;
import fr.osallek.osasaveeditor.controller.validator.CustomGraphicValidationDecoration;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.controlsfx.validation.decoration.CompoundValidationDecoration;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ProvincePropertySheet extends PropertySheet<SaveProvince> {

    private final MessageSource messageSource;

    private final ValidationSupport validationSupport;

    private final ClearableTextItem nameField;

    private final ClearableComboBoxItem<Culture> cultureComboBox;

    private final ClearableComboBoxItem<SaveReligion> religionComboBox;

    private final ClearableTextItem capitalField;

    private final ClearableComboBoxItem<SaveCountry> ownerComboBox;

    private final ClearableComboBoxItem<SaveCountry> controllerComboBox;

    private final ClearableCheckComboBoxItem<SaveCountry> coresField;

    private final ClearableCheckComboBoxItem<SaveCountry> claimsField;

    private final ClearableCheckComboBoxItem<SaveCountry> discoveredField;

    private final ClearableCheckBoxItem hreField;

    private final ClearableSpinnerItem<Integer> nationalismField;

    private final ClearableComboBoxItem<SaveCountry> colonizeForField;

    private final ClearableSliderItem colonySizeField;

    private final ClearableSpinnerItem<Integer> nativeHostilenessField;

    private final ClearableSpinnerItem<Integer> nativeFerocityField;

    private final ClearableSpinnerItem<Integer> nativeSizeField;

    private final ClearableSpinnerItem<Double> baseTaxField;

    private final ClearableSpinnerItem<Double> baseProdField;

    private final ClearableSpinnerItem<Double> baseMPField;

    private final ClearableComboBoxItem<TradeGood> tradeGoodField;

    private final ClearableComboBoxItem<TradeGood> latentTradeGoodField;

    private final ClearableSpinnerItem<Integer> cotField;

    private final ClearableSpinnerItem<Integer> infrastructureField;

    private final List<ClearableSliderItem> institutionFields;

    private final ClearableSliderItem autonomyField;

    private final ClearableSliderItem devastationField;

    private final ClearableComboBoxItem<TradeNode> tradeNodeField;

    private final ClearableCheckBoxItem tradeCompanyField;

    private final Map<SelectableGridViewItem<Building>, HBoxItem<Building>> buildingsFields;

    private final File defaultBuildingImage;

    private final ButtonItem modifiersButton;

    private final SimpleListProperty<Modifier> modifiers;

    private Map<SaveGreatProject, ClearableSpinnerItem<Integer>> greatProjectsField;

    private final ClearableCheckBoxItem parliamentField;

    private final ClearableCheckBoxItem parliamentBackingField;

    private final ClearableComboBoxItem<ParliamentBribe> parliamentBribeField;

    private final ChangeListener<? super SaveCountry> ownerChangeListener;

    private BooleanProperty countryChanged;

    public ProvincePropertySheet(MessageSource messageSource, Save save, ObservableList<SaveCountry> playableCountries, ObservableList<Culture> cultures,
                                 ObservableList<SaveReligion> religions, ObservableList<TradeGood> tradeGoods, ObservableList<TradeNode> tradeNodes) {
        super(save, null);
        this.messageSource = messageSource;

        //GENERAL
        this.nameField = new ClearableTextItem(this.messageSource.getMessage("ose.category.general", null, Constants.LOCALE),
                                               save.getGame().getLocalisationClean("LEDGER_NAME", Eu4Language.getByLocale(Constants.LOCALE)));
        this.nameField.getTextField().getStylesheets().add(OsaSaveEditorApplication.class.getResource("/styles/style.css").toExternalForm());

        this.validationSupport = new ValidationSupport();
        this.validationSupport.registerValidator(this.nameField.getTextField(), Validator.createEmptyValidator("Text is required"));
        this.validationSupport.setValidationDecorator(
                new CompoundValidationDecoration(new CustomGraphicValidationDecoration(), new StyleClassValidationDecoration("validation-error", null)));

        this.capitalField = new ClearableTextItem(this.messageSource.getMessage("ose.category.general", null, Constants.LOCALE),
                                                  save.getGame().getLocalisationClean("TRIGGER_CAPITAL", Eu4Language.getByLocale(Constants.LOCALE)));

        this.cultureComboBox = new ClearableComboBoxItem<>(this.messageSource.getMessage("ose.category.general", null, Constants.LOCALE),
                                                           save.getGame().getLocalisationClean("LEDGER_CULTURE", Eu4Language.getByLocale(Constants.LOCALE)),
                                                           cultures, new ClearableComboBox<>(new SearchableComboBox<>()));
        this.cultureComboBox.setConverter(CultureStringConverter.INSTANCE);
        this.cultureComboBox.setCellFactory(CultureStringCellFactory.INSTANCE);

        this.religionComboBox = new ClearableComboBoxItem<>(this.messageSource.getMessage("ose.category.general", null, Constants.LOCALE),
                                                            save.getGame().getLocalisationClean("LEDGER_RELIGION", Eu4Language.getByLocale(Constants.LOCALE)),
                                                            religions, new ClearableComboBox<>(new SearchableComboBox<>()));
        this.religionComboBox.setConverter(SaveReligionStringConverter.INSTANCE);
        this.religionComboBox.setCellFactory(SaveReligionStringCellFactory.INSTANCE);

        this.controllerComboBox = new ClearableComboBoxItem<>(this.messageSource.getMessage("ose.category.political", null, Constants.LOCALE), save.getGame()
                                                                                                                                                   .getLocalisationClean(
                                                                                                                                                           "SUPPLY_CONTROLLER",
                                                                                                                                                           Eu4Language.getByLocale(
                                                                                                                                                                   Constants.LOCALE)),
                                                              playableCountries, new ClearableComboBox<>(new SearchableComboBox<>()));
        this.controllerComboBox.setConverter(CountryStringConverter.INSTANCE);
        this.controllerComboBox.setCellFactory(new CountryStringCellFactory());

        this.ownerComboBox = new ClearableComboBoxItem<>(this.messageSource.getMessage("ose.category.political", null, Constants.LOCALE),
                                                         save.getGame().getLocalisationClean("LEDGER_OWNER", Eu4Language.getByLocale(Constants.LOCALE)),
                                                         playableCountries, new ClearableComboBox<>(new SearchableComboBox<>()));
        this.ownerComboBox.setConverter(CountryStringConverter.INSTANCE);
        this.ownerComboBox.setCellFactory(new CountryStringCellFactory());

        this.coresField = new ClearableCheckComboBoxItem<>(this.messageSource.getMessage("ose.category.political", null, Constants.LOCALE),
                                                           save.getGame().getLocalisationClean("LEDGER_CORE", Eu4Language.getByLocale(Constants.LOCALE)),
                                                           playableCountries, new ClearableCheckComboBox<>());
        this.coresField.setConverter(CountryStringConverter.INSTANCE);

        this.claimsField = new ClearableCheckComboBoxItem<>(this.messageSource.getMessage("ose.category.political", null, Constants.LOCALE),
                                                            save.getGame().getLocalisationClean("HAVE_CLAIM_IN", Eu4Language.getByLocale(Constants.LOCALE)),
                                                            playableCountries, new ClearableCheckComboBox<>());
        this.claimsField.setConverter(CountryStringConverter.INSTANCE);

        this.discoveredField = new ClearableCheckComboBoxItem<>(this.messageSource.getMessage("ose.category.political", null, Constants.LOCALE),
                                                                this.messageSource.getMessage("province.discoveredBy", null, Constants.LOCALE),
                                                                playableCountries, new ClearableCheckComboBox<>());
        this.discoveredField.setConverter(CountryStringConverter.INSTANCE);

        this.hreField = new ClearableCheckBoxItem(this.messageSource.getMessage("ose.category.political", null, Constants.LOCALE),
                                                  save.getGame().getLocalisationClean("IS_PART_OF_HRE", Eu4Language.getByLocale(Constants.LOCALE)));

        this.nationalismField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.political", null, Constants.LOCALE),
                                                           this.messageSource.getMessage("province.nationalism", null, Constants.LOCALE),
                                                           new ClearableSpinnerInt(0, 100, 1));

        this.colonizeForField = new ClearableComboBoxItem<>(this.messageSource.getMessage("ose.category.colony", null, Constants.LOCALE),
                                                            save.getGame().getLocalisationClean("COLONIZE_PROVINCE", Eu4Language.getByLocale(Constants.LOCALE)),
                                                            playableCountries, new ClearableComboBox<>(new SearchableComboBox<>()));
        this.colonizeForField.setConverter(CountryStringConverter.INSTANCE);

        this.nativeHostilenessField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.colony", null, Constants.LOCALE), save.getGame()
                                                                                                                                                   .getLocalisationClean(
                                                                                                                                                           "PP_AGGRESSIVE",
                                                                                                                                                           Eu4Language.getByLocale(
                                                                                                                                                                   Constants.LOCALE)),
                                                                 new ClearableSpinnerInt(0, 10, 1));

        this.nativeFerocityField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.colony", null, Constants.LOCALE),
                                                              save.getGame().getLocalisationClean("PP_FEROCITY", Eu4Language.getByLocale(Constants.LOCALE)),
                                                              new ClearableSpinnerInt(0, 10, 1));

        this.nativeSizeField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.colony", null, Constants.LOCALE),
                                                          save.getGame().getLocalisationClean("PP_NATIVES", Eu4Language.getByLocale(Constants.LOCALE)),
                                                          new ClearableSpinnerInt(0, 10000, 1));

        this.colonySizeField = new ClearableSliderItem(this.messageSource.getMessage("ose.category.colony", null, Constants.LOCALE),
                                                       save.getGame().getLocalisationClean("LEDGER_POPULATION", Eu4Language.getByLocale(Constants.LOCALE)), 0,
                                                       1000);

        this.baseTaxField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.economy", null, Constants.LOCALE),
                                                       save.getGame().getLocalisationClean("LEDGER_TAX", Eu4Language.getByLocale(Constants.LOCALE)),
                                                       new ClearableSpinnerDouble(1, 999, 1));

        this.baseProdField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.economy", null, Constants.LOCALE),
                                                        save.getGame().getLocalisationClean("LEDGER_PRODUCTION", Eu4Language.getByLocale(Constants.LOCALE)),
                                                        new ClearableSpinnerDouble(1, 999, 1));

        this.baseMPField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.economy", null, Constants.LOCALE),
                                                      save.getGame().getLocalisationClean("LEDGER_MANPOWER", Eu4Language.getByLocale(Constants.LOCALE)),
                                                      new ClearableSpinnerDouble(1, 999, 1));

        this.tradeGoodField = new ClearableComboBoxItem<>(this.messageSource.getMessage("ose.category.economy", null, Constants.LOCALE),
                                                          save.getGame().getLocalisationClean("LEDGER_GOODS", Eu4Language.getByLocale(Constants.LOCALE)),
                                                          tradeGoods, new ClearableComboBox<>(new SearchableComboBox<>()));
        this.tradeGoodField.setConverter(TradeGoodStringConverter.INSTANCE);
        this.tradeGoodField.setCellFactory(new TradeGoodStringCellFactory(save.getGame()));

        this.tradeNodeField = new ClearableComboBoxItem<>(this.messageSource.getMessage("ose.category.economy", null, Constants.LOCALE),
                                                          save.getGame().getLocalisationClean("TRADENODE", Eu4Language.getByLocale(Constants.LOCALE)),
                                                          tradeNodes, new ClearableComboBox<>(new SearchableComboBox<>()));
        this.tradeNodeField.setConverter(TradeNodeStringConverter.INSTANCE);
        this.tradeNodeField.setCellFactory(new TradeNodeStringCellFactory(save.getGame()));
        this.tradeCompanyField = new ClearableCheckBoxItem(messageSource.getMessage("ose.category.economy", null, Constants.LOCALE),
                                                           messageSource.getMessage("province.trade-company", null, Constants.LOCALE));

        this.latentTradeGoodField = new ClearableComboBoxItem<>(this.messageSource.getMessage("ose.category.economy", null, Constants.LOCALE), save.getGame()
                                                                                                                                                   .getLocalisationClean(
                                                                                                                                                           "LATENT_TRADE_GOODS_TOOLTIP_HEADER",
                                                                                                                                                           Eu4Language.getByLocale(
                                                                                                                                                                   Constants.LOCALE)),
                                                                tradeGoods, new ClearableComboBox<>(new SearchableComboBox<>()));
        this.latentTradeGoodField.setConverter(TradeGoodStringConverter.INSTANCE);
        this.latentTradeGoodField.setCellFactory(new TradeGoodStringCellFactory(save.getGame()));

        this.cotField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.economy", null, Constants.LOCALE),
                                                   save.getGame().getLocalisationClean("EST_VAL_COT", Eu4Language.getByLocale(Constants.LOCALE)),
                                                   new ClearableSpinnerInt(0, 3, 1));

        this.infrastructureField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.economy", null, Constants.LOCALE),
                                                              this.messageSource.getMessage("province.infrastructure", null, Constants.LOCALE),
                                                              new ClearableSpinnerInt(0, 100, 1));

        this.autonomyField = new ClearableSliderItem(this.messageSource.getMessage("ose.category.economy", null, Constants.LOCALE),
                                                     save.getGame().getLocalisationClean("local_autonomy", Eu4Language.getByLocale(Constants.LOCALE)), 0, 100);

        this.devastationField = new ClearableSliderItem(this.messageSource.getMessage("ose.category.economy", null, Constants.LOCALE),
                                                        save.getGame().getLocalisationClean("LEDGER_DEVASTATION", Eu4Language.getByLocale(Constants.LOCALE)), 0,
                                                        100);

        this.institutionFields = new ArrayList<>();
        for (int i = 0; i < save.getInstitutions().getNbInstitutions(); i++) {
            this.institutionFields.add(new ClearableSliderItem(this.messageSource.getMessage("ose.category.institutions", null, Constants.LOCALE),
                                                               OsaSaveEditorUtils.localize(save.getGame().getInstitution(i).getName(), save.getGame()), 0,
                                                               100));
        }

        File defaultBuildingImage1;
        this.buildingsFields = new LinkedHashMap<>();
        try {
            defaultBuildingImage1 = new ClassPathResource("images/no_building.png").getFile();
        } catch (IOException ignored) {
            defaultBuildingImage1 = null;
        }
        this.defaultBuildingImage = defaultBuildingImage1;

        Eu4Utils.buildingsTree(save.getGame().getBuildings()).forEach(buildings -> {
            buildings.removeIf(b -> "manufactory".equals(b.getName()));

            if (CollectionUtils.isNotEmpty(buildings)) {
                SelectableGridViewItem<Building> grid = new SelectableGridViewItem<>(
                        this.messageSource.getMessage("ose.category.buildings", null, Constants.LOCALE),
                        new SelectableGridView<>(FXCollections.observableList(buildings),
                                                 buildings.stream().anyMatch(b -> CollectionUtils.isNotEmpty(b.getManufactoryFor())),
                                                 FXCollections.observableSet(new HashSet<>())));
                grid.setCellFactory(b -> OsaSaveEditorUtils.localize("building_" + b.getName(), this.t.getSave().getGame()), Building::getImage,
                                    this.defaultBuildingImage);
                this.buildingsFields.put(grid, null);
            }
        });

        //Modifiers
        this.modifiers = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.modifiersButton = new ButtonItem(save.getGame().getLocalisationClean("DOMESTIC_MODIFIERS", Eu4Language.getByLocale(Constants.LOCALE)), null,
                                              save.getGame().getLocalisationClean("DOMESTIC_MODIFIERS", Eu4Language.getByLocale(Constants.LOCALE)));
        this.modifiersButton.isVisible().bind(this.modifiers.emptyProperty().not());

        //Great projects
        this.greatProjectsField = new LinkedHashMap<>();

        //Parliament
        this.parliamentField = new ClearableCheckBoxItem(messageSource.getMessage("ose.category.parliament", null, Constants.LOCALE),
                                                         messageSource.getMessage("province.parliament.seat", null, Constants.LOCALE));
        this.parliamentBackingField = new ClearableCheckBoxItem(messageSource.getMessage("ose.category.parliament", null, Constants.LOCALE),
                                                                messageSource.getMessage("province.parliament.backing", null, Constants.LOCALE));
        this.parliamentBackingField.isVisible().bind(this.parliamentField.selectedProperty().and(this.parliamentField.isVisible()));
        this.parliamentBackingField.isEditable()
                                   .bind(this.parliamentField.isEditable().and(this.parliamentField.selectedProperty()).and(this.parliamentField.isVisible()));
        this.parliamentBribeField = new ClearableComboBoxItem<>(this.messageSource.getMessage("ose.category.parliament", null, Constants.LOCALE),
                                                                this.messageSource.getMessage("province.parliament.bribe", null, Constants.LOCALE),
                                                                FXCollections.observableList(save.getGame().getParliamentBribes())
                                                                             .sorted(Comparator.comparing(ParliamentBribeStringConverter.INSTANCE::toString,
                                                                                                          Eu4Utils.COLLATOR)),
                                                                new ClearableComboBox<>(new SearchableComboBox<>()));
        this.parliamentBribeField.setConverter(ParliamentBribeStringConverter.INSTANCE);
        this.parliamentBribeField.setCellFactory(ParliamentBribeStringCellFactory.INSTANCE);
        this.parliamentBribeField.isVisible().bind(this.parliamentField.selectedProperty().and(this.parliamentField.isVisible()));
        this.parliamentBribeField.editableProperty()
                                 .bind(this.parliamentField.isEditable().and(this.parliamentField.selectedProperty()).and(this.parliamentField.isVisible()));

        this.ownerChangeListener = (observable, oldValue, newValue) -> {
            this.controllerComboBox.select(newValue);

            if (this.t.isCity()) {
                this.coresField.clearCheck(oldValue);
                this.coresField.check(newValue);
            }
        };

        this.propertySheet.getItems().add(this.nameField);
        this.propertySheet.getItems().add(this.capitalField);
        this.propertySheet.getItems().add(this.cultureComboBox);
        this.propertySheet.getItems().add(this.religionComboBox);
        this.propertySheet.getItems().add(this.ownerComboBox);
        this.propertySheet.getItems().add(this.controllerComboBox);
        this.propertySheet.getItems().add(this.coresField);
        this.propertySheet.getItems().add(this.claimsField);
        this.propertySheet.getItems().add(this.hreField);
        this.propertySheet.getItems().add(this.discoveredField);
        this.propertySheet.getItems().add(this.nationalismField);
        this.propertySheet.getItems().add(this.colonizeForField);
        this.propertySheet.getItems().add(this.colonySizeField);
        this.propertySheet.getItems().add(this.nativeSizeField);
        this.propertySheet.getItems().add(this.nativeHostilenessField);
        this.propertySheet.getItems().add(this.nativeFerocityField);
        this.propertySheet.getItems().add(this.baseTaxField);
        this.propertySheet.getItems().add(this.baseProdField);
        this.propertySheet.getItems().add(this.baseMPField);
        this.propertySheet.getItems().add(this.tradeGoodField);
        this.propertySheet.getItems().add(this.latentTradeGoodField);
        this.propertySheet.getItems().add(this.cotField);
        this.propertySheet.getItems().add(this.infrastructureField);
        this.propertySheet.getItems().add(this.autonomyField);
        this.propertySheet.getItems().add(this.devastationField);
        this.propertySheet.getItems().add(this.tradeNodeField);
        this.propertySheet.getItems().add(this.tradeCompanyField);
        this.propertySheet.getItems().addAll(this.institutionFields);

        ListIterator<SelectableGridViewItem<Building>> iterator = new ArrayList<>(this.buildingsFields.keySet()).listIterator();
        while (iterator.hasNext()) {
            SelectableGridViewItem<Building> current = iterator.next();
            HBox hBox = new HBox(13);
            HBox.setHgrow(current.getSelectableGridView(), Priority.ALWAYS);

            if (iterator.hasNext()) {
                SelectableGridViewItem<Building> next = iterator.next();

                if (current.getNbItems() <= 4 && next.getNbItems() <= 4) {
                    HBox.setHgrow(next.getSelectableGridView(), Priority.ALWAYS);
                    hBox.getChildren().addAll(current.getSelectableGridView(), next.getSelectableGridView());

                    this.buildingsFields.put(next, this.buildingsFields.get(current));
                } else {
                    hBox.getChildren().add(current.getSelectableGridView());
                    iterator.previous();
                }
            } else {
                hBox.getChildren().add(current.getSelectableGridView());
            }

            this.buildingsFields.put(current, new HBoxItem<>(this.messageSource.getMessage("ose.category.buildings", null, Constants.LOCALE), hBox));
            this.propertySheet.getItems().add(this.buildingsFields.get(current));
        }

        this.propertySheet.getItems().add(this.modifiersButton);
        this.propertySheet.getItems().addAll(this.greatProjectsField.values());
        this.propertySheet.getItems().add(this.parliamentField);
        this.propertySheet.getItems().add(this.parliamentBackingField);
        this.propertySheet.getItems().add(this.parliamentBribeField);

    }

    public Set<CustomPropertySheet.Item<?>> internalUpdate(SaveProvince province) {
        this.countryChanged.set(false);
        this.ownerComboBox.valueProperty().removeListener(this.ownerChangeListener);

        Set<CustomPropertySheet.Item<?>> items = new HashSet<>();

        //GENERAL
        this.nameField.setValue(ClausewitzUtils.removeQuotes(this.t.getName()));
        this.nameField.setSupplier(() -> ClausewitzUtils.removeQuotes(this.t.getName()));
        items.add(this.nameField);

        if (province.isColonizable()) {
            //GENERAL
            this.capitalField.setValue(ClausewitzUtils.removeQuotes(this.t.getCapital()));
            this.capitalField.setSupplier(() -> ClausewitzUtils.removeQuotes(this.t.getCapital()));
            this.capitalField.setEditable(true);
            items.add(this.capitalField);

            this.cultureComboBox.setValue(this.t.getCulture());
            this.cultureComboBox.setSupplier(this.t::getCulture);
            this.cultureComboBox.setEditable(true);
            items.add(this.cultureComboBox);

            this.religionComboBox.setValue(this.t.getReligion());
            this.religionComboBox.setSupplier(this.t::getReligion);
            this.religionComboBox.setEditable(true);
            items.add(this.religionComboBox);

            //POLITICAL
            if (this.t.isCity() || this.t.getColonySize() != null) {
                this.ownerComboBox.setValue(this.t.getOwner());
                this.ownerComboBox.setSupplier(this.t::getOwner);
                this.ownerComboBox.setEditable(true);
                items.add(this.ownerComboBox);

                this.controllerComboBox.setValue(this.t.getController());
                this.controllerComboBox.setSupplier(this.t::getController);
                this.controllerComboBox.setEditable(true);
                items.add(this.controllerComboBox);
            }

            if (this.t.isCity()) {
                this.coresField.setValue(FXCollections.observableList(this.t.getCores()));
                this.coresField.setSupplier(this.t::getCores);
                this.coresField.setEditable(true);
                items.add(this.coresField);

                this.claimsField.setValue(FXCollections.observableList(this.t.getClaims()));
                this.claimsField.setSupplier(this.t::getClaims);
                this.claimsField.setEditable(true);
                items.add(this.claimsField);

                this.hreField.setValue(this.t.inHre());
                this.hreField.setEditable(true);
                items.add(this.hreField);
            }

            this.discoveredField.setValue(FXCollections.observableList(this.t.getDiscoveredBy()));
            this.discoveredField.setSupplier(this.t::getDiscoveredBy);
            this.discoveredField.setEditable(true);
            items.add(this.discoveredField);

            this.nationalismField.setValue(this.t.getNationalism());
            this.nationalismField.setSupplier(this.t::getNationalism);
            this.nationalismField.setEditable(true);
            items.add(this.nationalismField);

            //COLONY
            if (!this.t.isCity()) {
                if (this.t.getColonySize() == null) {
                    this.colonizeForField.setEditable(true);
                    items.add(this.colonizeForField);
                } else {
                    this.colonySizeField.setValue(this.t.getColonySize());
                    this.colonySizeField.setSupplier(this.t::getColonySize);
                    this.colonySizeField.setEditable(true);
                    items.add(this.colonySizeField);
                }

                this.nativeSizeField.setValue(this.t.getNativeSize());
                this.nativeSizeField.setSupplier(this.t::getNativeSize);
                this.nativeSizeField.setEditable(true);
                items.add(this.nativeSizeField);

                this.nativeHostilenessField.setValue(this.t.getNativeHostileness());
                this.nativeHostilenessField.setSupplier(this.t::getNativeHostileness);
                this.nativeHostilenessField.setEditable(true);
                items.add(this.nativeHostilenessField);

                this.nativeFerocityField.setValue(this.t.getNativeFerocity());
                this.nativeFerocityField.setSupplier(this.t::getNativeFerocity);
                this.nativeFerocityField.setEditable(true);
                items.add(this.nativeFerocityField);
            }

            //ECONOMY
            this.baseTaxField.setValue(this.t.getBaseTax());
            this.baseTaxField.setSupplier(this.t::getBaseTax);
            this.baseTaxField.setEditable(true);
            items.add(this.baseTaxField);

            this.baseProdField.setValue(this.t.getBaseProduction());
            this.baseProdField.setSupplier(this.t::getBaseProduction);
            this.baseProdField.setEditable(true);
            items.add(this.baseProdField);

            this.baseMPField.setValue(this.t.getBaseManpower());
            this.baseMPField.setSupplier(this.t::getBaseManpower);
            this.baseMPField.setEditable(true);
            items.add(this.baseMPField);

            this.tradeGoodField.setValue(this.t.getTradeGood());
            this.tradeGoodField.setSupplier(this.t::getTradeGood);
            this.tradeGoodField.setEditable(true);
            items.add(this.tradeGoodField);

            this.latentTradeGoodField.setValue(this.t.getLatentTradeGood());
            this.latentTradeGoodField.setSupplier(this.t::getLatentTradeGood);
            this.latentTradeGoodField.setEditable(true);
            items.add(this.latentTradeGoodField);

            this.cotField.setValue(this.t.getCenterOfTradeLevel());
            this.cotField.setSupplier(this.t::getCenterOfTradeLevel);
            this.cotField.setEditable(true);
            items.add(this.cotField);

            this.infrastructureField.setValue(this.t.getExpandInfrastructure());
            this.infrastructureField.setSupplier(this.t::getExpandInfrastructure);
            this.infrastructureField.setEditable(true);
            items.add(this.infrastructureField);

            if (this.t.isCity()) {
                this.autonomyField.setValue(this.t.getTrueLocalAutonomy());
                this.autonomyField.setSupplier(this.t::getTrueLocalAutonomy);
                this.autonomyField.setEditable(true);
                items.add(this.autonomyField);

                this.devastationField.setValue(this.t.getDevastation());
                this.devastationField.setSupplier(this.t::getDevastation);
                this.devastationField.setEditable(true);
                items.add(this.devastationField);
            }

            this.tradeNodeField.setValue(this.t.getTradeNode());
            this.tradeNodeField.setSupplier(this.t::getTradeNode);
            this.tradeNodeField.setEditable(true);
            items.add(this.tradeNodeField);

            if (this.t.getOwner() != null && !this.t.inHre() && (this.t.getSaveArea() == null
                                                                 || this.t.getSaveArea().getCountryState(this.t.getOwner()) == null) && this.t.getSave()
                                                                                                                                              .getGame()
                                                                                                                                              .getTradeCompanies()
                                                                                                                                              .stream()
                                                                                                                                              .anyMatch(
                                                                                                                                                      c -> c.getProvinces()
                                                                                                                                                            .contains(
                                                                                                                                                                    province.getId()))
                && !this.t.getArea().getRegion().getSuperRegion().equals(this.t.getOwner().getCapital().getArea().getRegion().getSuperRegion())) {
                this.tradeCompanyField.setEditable(true);
                this.tradeCompanyField.setValue(BooleanUtils.toBoolean(province.activeTradeCompany()));
                this.tradeCompanyField.setSupplier(() -> BooleanUtils.toBoolean(province.activeTradeCompany()));
                items.add(this.tradeCompanyField);
            }

            //INSTITUTIONS
            if (!this.t.getInstitutionsProgress().isEmpty()) {
                for (int i = 0; i < this.t.getInstitutionsProgress().size(); i++) {
                    int finalI = i;
                    this.institutionFields.get(i).setEditable(true);
                    this.institutionFields.get(i).setValue(this.t.getInstitutionsProgress(i));
                    this.institutionFields.get(i).setSupplier(() -> this.t.getInstitutionsProgress(finalI));
                }

                items.addAll(this.institutionFields);
            }

            if (this.t.isCity()) {
                //BUILDINGS
                Set<Building> buildings = this.t.getBuildings().stream().map(ProvinceBuilding::getBuilding).collect(Collectors.toSet());
                this.buildingsFields.keySet().forEach(item -> item.getSelectableGridView().getItems().forEach(b -> {
                    if (buildings.contains(b) && !item.getSelectableGridView().getSelectedItems().contains(b)) {
                        item.select(b);
                    } else if (!buildings.contains(b) && item.getSelectableGridView().getSelectedItems().contains(b)) {
                        item.unSelect(b);
                    }
                }));
                items.addAll(this.buildingsFields.values());
            }
        }

        //Modifiers
        this.modifiers.setAll(this.t.getModifiers().values().stream().map(Modifier::new).toList());
        this.modifiersButton.getButton().setOnAction(event -> {
            TableView2Modifier tableView2Modifier = new TableView2Modifier(this.t.getSave(), this.modifiers);
            TableViewDialog<Modifier> dialog = new TableViewDialog<>(this.t.getSave(), tableView2Modifier, this.t.getSave()
                                                                                                                 .getGame()
                                                                                                                 .getLocalisationClean("DOMESTIC_MODIFIERS",
                                                                                                                                       Eu4Language.getByLocale(
                                                                                                                                               Constants.LOCALE)),
                                                                     list -> null, () -> this.modifiers);
            dialog.setDisableAddProperty(new SimpleBooleanProperty(true));
            Optional<List<Modifier>> modifierList = dialog.showAndWait();

            modifierList.ifPresent(this.modifiers::setAll);
        });
        items.add(this.modifiersButton);

        //Great projects
        this.greatProjectsField = this.t.getGreatProjects()
                                        .stream()
                                        .map(p -> Map.entry(p, new ClearableSpinnerItem<>(
                                                this.messageSource.getMessage("ose.category.great-projects", null, Constants.LOCALE),
                                                OsaSaveEditorUtils.localize(p.getName(), this.t.getSave().getGame()),
                                                new ClearableSpinnerInt(0, p.getGreatProject().getMaxLevel(), p.getDevelopmentTier(), 1,
                                                                        p::getDevelopmentTier))))
                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        items.addAll(this.greatProjectsField.values());

        //Parliament
        if (province.getOwner() != null && province.getOwner().getParliament() != null) {
            this.parliamentField.setEditable(true);
            this.parliamentField.setValue(province.getSeatInParliament() != null);
            this.parliamentField.setSupplier(() -> province.getSeatInParliament() != null);
            items.add(this.parliamentField);

            this.parliamentBribeField.setFilter(bribe -> bribe.getTrigger() == null || bribe.getTrigger().apply(province.getOwner(), province));

            if (province.getSeatInParliament() != null) {
                this.parliamentBackingField.setValue(BooleanUtils.toBoolean(province.getSeatInParliament().getBack()));
                this.parliamentBackingField.setSupplier(() -> BooleanUtils.toBoolean(province.getSeatInParliament().getBack()));
                this.parliamentBribeField.setValue(province.getSeatInParliament().getBribe());
                this.parliamentBribeField.setSupplier(() -> province.getSeatInParliament().getBribe());
            } else {
                this.parliamentBackingField.setValue(false);
                this.parliamentBackingField.setSupplier(() -> false);
                this.parliamentBribeField.setValue(this.parliamentBribeField.getChoices().get(0));
                this.parliamentBribeField.setSupplier(() -> this.parliamentBribeField.getChoices().get(0));
            }

            items.add(this.parliamentBackingField);
            items.add(this.parliamentBribeField);
        }

        this.ownerComboBox.valueProperty().addListener(this.ownerChangeListener);

        return items;
    }

    public void validate() {
        if (!ClausewitzUtils.removeQuotes(this.t.getName()).equals(this.nameField.getValue())) {
            this.t.setName(this.nameField.getValue());
        }

        if (this.capitalField.isVisible().get()) {
            if (!Objects.equals(ClausewitzUtils.removeQuotes(this.t.getCapital()), this.capitalField.getValue())) {
                this.t.setCapital(this.capitalField.getValue());
            }
        }

        if (this.controllerComboBox.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getController(), this.controllerComboBox.getValue())) {
                this.t.setController(this.controllerComboBox.getValue());
            }
        }

        if (this.ownerComboBox.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getOwner(), this.ownerComboBox.getValue())) {
                this.t.setOwner(this.ownerComboBox.getValue());
                this.countryChanged.set(true);
            }
        }

        if (this.coresField.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getCores(), this.coresField.getValue())) {
                this.t.setCores(new ArrayList<>(this.coresField.getValue()));
            }
        }

        if (this.claimsField.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getClaims(), this.claimsField.getValue())) {
                this.t.setClaims(new ArrayList<>(this.claimsField.getValue()));
            }
        }

        if (this.discoveredField.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getDiscoveredBy(), this.discoveredField.getValue())) {
                this.t.setDiscoveredBy(new ArrayList<>(this.discoveredField.getValue()));
            }
        }

        if (this.hreField.isVisible().get()) {
            if (this.t.inHre() != this.hreField.isSelected()) {
                this.t.setInHre(this.hreField.isSelected());
            }
        }

        if (this.nationalismField.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getNationalism(), this.nationalismField.getValue())) {
                this.t.setNationalism(NumbersUtils.intOrDefault(this.nationalismField.getValue()));
            }
        }

        if (this.cultureComboBox.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getCulture(), this.cultureComboBox.getValue())) {
                this.t.setCulture(this.cultureComboBox.getValue());
            }
        }

        if (this.religionComboBox.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getReligion(), this.religionComboBox.getValue())) {
                this.t.setReligion(this.religionComboBox.getValue());
            }
        }

        if (this.nativeHostilenessField.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getNativeHostileness(), this.nativeHostilenessField.getValue())) {
                this.t.setNativeHostileness(this.nativeHostilenessField.getValue());
            }
        }

        if (this.nativeFerocityField.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getNativeFerocity(), this.nativeFerocityField.getValue())) {
                this.t.setNativeFerocity(this.nativeFerocityField.getValue());
            }
        }

        if (this.nativeSizeField.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getNativeSize(), this.nativeSizeField.getValue())) {
                this.t.setNativeSize(this.nativeSizeField.getValue());
            }
        }

        if (this.baseTaxField.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getBaseTax(), this.baseTaxField.getValue())) {
                this.t.setBaseTax(this.baseTaxField.getValue());
            }
        }

        if (this.baseProdField.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getBaseProduction(), this.baseProdField.getValue())) {
                this.t.setBaseProduction(this.baseProdField.getValue());
            }
        }

        if (this.baseMPField.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getBaseManpower(), this.baseMPField.getValue())) {
                this.t.setBaseManpower(this.baseMPField.getValue());
            }
        }

        if (!this.institutionFields.isEmpty()) {
            for (int i = 0; i < this.institutionFields.size(); i++) {
                if (this.institutionFields.get(i).isVisible().get() && !Objects.deepEquals(this.t.getInstitutionsProgress(i),
                                                                                           this.institutionFields.get(i).getDoubleValue())) {
                    this.t.setInstitutionProgress(i, this.institutionFields.get(i).getDoubleValue());
                }
            }
        }

        if (this.tradeGoodField.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getTradeGood(), this.tradeGoodField.getValue())) {
                this.t.setTradeGoods(this.tradeGoodField.getValue().getName());
            }
        }

        if (this.latentTradeGoodField.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getLatentTradeGood(), this.latentTradeGoodField.getValue())) {
                this.t.setLatentTradeGoods(this.latentTradeGoodField.getValue().getName());
            }
        }

        if (this.cotField.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getCenterOfTradeLevel(), this.cotField.getValue())) {
                this.t.setCenterOfTrade(this.cotField.getValue());
            }
        }

        if (this.infrastructureField.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getExpandInfrastructure(), this.infrastructureField.getValue())) {
                this.t.setExpandInfrastructure(this.infrastructureField.getValue());
            }
        }

        if (this.autonomyField.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getTrueLocalAutonomy(), this.autonomyField.getDoubleValue())) {
                this.t.setLocalAutonomy(this.autonomyField.getDoubleValue());
            }
        }

        if (this.devastationField.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getDevastation(), this.devastationField.getDoubleValue())) {
                this.t.setDevastation(this.devastationField.getDoubleValue());
            }
        }

        if (this.tradeNodeField.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getTradeNode(), this.tradeNodeField.getValue())) {
                this.t.setTradeNode(this.tradeNodeField.getValue());
            }
        }

        if (this.tradeCompanyField.isVisible().get()) {
            if (!Objects.deepEquals(BooleanUtils.toBoolean(this.t.activeTradeCompany()), this.tradeCompanyField.isSelected())) {
                this.t.setActiveTradeCompany(this.tradeCompanyField.isSelected(), Eu4Language.getByLocale(Constants.LOCALE));
            }
        }

        if (!this.buildingsFields.isEmpty()) {
            List<Building> buildings = this.buildingsFields.keySet()
                                                           .stream()
                                                           .map(SelectableGridViewItem::getValue)
                                                           .flatMap(Collection::stream)
                                                           .distinct()
                                                           .collect(Collectors.toList());
            if (!CollectionUtils.isEqualCollection(this.t.getBuildings().stream().map(ProvinceBuilding::getBuilding).toList(), buildings)) {
                this.t.setBuildings(buildings);
            }
        }

        if (this.colonySizeField.isVisible().get()) {
            if (!Objects.deepEquals(this.t.getColonySize(), this.colonySizeField.getDoubleValue())) {
                this.t.setColonySize(this.colonySizeField.getDoubleValue());
            }
        }

        if (this.colonizeForField.isVisible().get()) {
            if (this.colonizeForField.getValue() != null) {
                this.t.colonize(this.colonizeForField.getValue());
                this.countryChanged.set(true);
            }
        }

        if (this.t.getModifiers().size() != this.modifiers.size() || this.modifiers.stream().anyMatch(Modifier::isChanged)) {
            this.t.getModifiers()
                  .values()
                  .forEach(saveModifier -> this.modifiers.stream()
                                                         .filter(modifier -> saveModifier.getModifier().equals(modifier.getModifier()))
                                                         .findFirst()
                                                         .ifPresentOrElse(modifier -> {
                                                             if (!Objects.equals(modifier.getDate(), saveModifier.getDate())) {
                                                                 saveModifier.setDate(modifier.getDate());
                                                             }

                                                             this.modifiers.remove(modifier);
                                                         }, () -> this.t.removeModifier(saveModifier.getModifier())));
        }

        if (MapUtils.isNotEmpty(this.greatProjectsField) && CollectionUtils.isNotEmpty(this.t.getGreatProjects())) {
            this.greatProjectsField.forEach((p, item) -> {
                if (!Objects.equals(p.getDevelopmentTier(), item.getValue())) {
                    p.setDevelopmentTier(item.getValue());
                }
            });
        }

        if (this.parliamentField.isVisible().get()) {
            if (this.parliamentField.isSelected() && this.t.getSeatInParliament() == null) {
                this.t.addSeatInParliament(this.parliamentBribeField.getValue());
            } else if (!this.parliamentField.isSelected() && this.t.getSeatInParliament() != null) {
                this.t.removeSeatInParliament();
            }
        }

        if (this.parliamentBackingField.isVisible().get() && this.t.getSeatInParliament() != null) {
            if (this.parliamentBackingField.isSelected() != BooleanUtils.toBoolean(this.t.getSeatInParliament().getBack())) {
                this.t.getSeatInParliament().setBack(this.parliamentBackingField.isSelected());
            }
        }

        if (this.parliamentBribeField.isVisible().get() && this.t.getSeatInParliament() != null) {
            if (!Objects.deepEquals(this.t.getSeatInParliament().getBribe(), this.parliamentBribeField.getValue())) {
                this.t.getSeatInParliament().setBribe(this.parliamentBribeField.getValue());
            }
        }
    }

    public final BooleanProperty countryChangedProperty() {
        if (this.countryChanged == null) {
            this.countryChanged = new BooleanPropertyBase() {
                @Override
                public Object getBean() {
                    return ProvincePropertySheet.this;
                }

                @Override
                public String getName() {
                    return "countryChanged";
                }
            };
        }

        return countryChanged;
    }

    public ValidationSupport getValidationSupport() {
        return validationSupport;
    }
}
