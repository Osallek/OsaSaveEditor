package fr.osallek.osasaveeditor.controller.mapview;

import fr.osallek.clausewitzparser.common.ClausewitzUtils;
import fr.osallek.eu4parser.common.Eu4Utils;
import fr.osallek.eu4parser.model.Power;
import fr.osallek.eu4parser.model.game.Culture;
import fr.osallek.eu4parser.model.game.FetishistCult;
import fr.osallek.eu4parser.model.game.GovernmentReform;
import fr.osallek.eu4parser.model.game.PersonalDeity;
import fr.osallek.eu4parser.model.game.Policy;
import fr.osallek.eu4parser.model.game.Religion;
import fr.osallek.eu4parser.model.game.ReligionGroup;
import fr.osallek.eu4parser.model.game.ReligiousReform;
import fr.osallek.eu4parser.model.game.SubjectType;
import fr.osallek.eu4parser.model.game.localisation.Eu4Language;
import fr.osallek.eu4parser.model.save.Save;
import fr.osallek.eu4parser.model.save.SaveReligion;
import fr.osallek.eu4parser.model.save.country.LeaderType;
import fr.osallek.eu4parser.model.save.country.SaveCountry;
import fr.osallek.eu4parser.model.save.province.SaveProvince;
import fr.osallek.osasaveeditor.OsaSaveEditorApplication;
import fr.osallek.osasaveeditor.common.Constants;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import fr.osallek.osasaveeditor.controller.control.ClearableCheckComboBox;
import fr.osallek.osasaveeditor.controller.control.ClearableColorPicker;
import fr.osallek.osasaveeditor.controller.control.ClearableComboBox;
import fr.osallek.osasaveeditor.controller.control.ClearableSpinnerDouble;
import fr.osallek.osasaveeditor.controller.control.ClearableSpinnerInt;
import fr.osallek.osasaveeditor.controller.control.CustomListSelectionView;
import fr.osallek.osasaveeditor.controller.control.RequiredComboBox;
import fr.osallek.osasaveeditor.controller.control.TableView2CountrySubject;
import fr.osallek.osasaveeditor.controller.control.TableView2Ideas;
import fr.osallek.osasaveeditor.controller.control.TableView2Leader;
import fr.osallek.osasaveeditor.controller.control.TableView2Loan;
import fr.osallek.osasaveeditor.controller.control.TableView2Modifier;
import fr.osallek.osasaveeditor.controller.control.TableView2Policy;
import fr.osallek.osasaveeditor.controller.control.TableView2Rival;
import fr.osallek.osasaveeditor.controller.control.TableView2StringDate;
import fr.osallek.osasaveeditor.controller.converter.CountryStringConverter;
import fr.osallek.osasaveeditor.controller.converter.CultureStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.CultureStringConverter;
import fr.osallek.osasaveeditor.controller.converter.FetishistCultStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.FetishistCultStringConverter;
import fr.osallek.osasaveeditor.controller.converter.GovernmentRankCellFactory;
import fr.osallek.osasaveeditor.controller.converter.GovernmentRankConverter;
import fr.osallek.osasaveeditor.controller.converter.PersonalDeityStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.PersonalDeityStringConverter;
import fr.osallek.osasaveeditor.controller.converter.ProvinceStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.ProvinceStringConverter;
import fr.osallek.osasaveeditor.controller.converter.ReligionGroupStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.ReligionStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.ReligiousReformStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.SaveReligionStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.SaveReligionStringConverter;
import fr.osallek.osasaveeditor.controller.object.ActivePolicy;
import fr.osallek.osasaveeditor.controller.object.CountrySubject;
import fr.osallek.osasaveeditor.controller.object.Idea;
import fr.osallek.osasaveeditor.controller.object.Leader;
import fr.osallek.osasaveeditor.controller.object.Loan;
import fr.osallek.osasaveeditor.controller.object.Modifier;
import fr.osallek.osasaveeditor.controller.object.Rival;
import fr.osallek.osasaveeditor.controller.object.StringDate;
import fr.osallek.osasaveeditor.controller.pane.CustomPropertySheet;
import fr.osallek.osasaveeditor.controller.pane.CustomPropertySheetSkin;
import fr.osallek.osasaveeditor.controller.pane.GovernmentReformsDialog;
import fr.osallek.osasaveeditor.controller.pane.ListSelectionViewDialog;
import fr.osallek.osasaveeditor.controller.pane.TableViewDialog;
import fr.osallek.osasaveeditor.controller.propertyeditor.CustomPropertyEditorFactory;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ButtonItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.CheckBoxItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableCheckComboBoxItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableColorPickerItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableComboBoxItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableSliderIntItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableSliderItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableSpinnerItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableTextItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.PropertySheetItem;
import fr.osallek.osasaveeditor.controller.validator.CustomGraphicValidationDecoration;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.controlsfx.validation.decoration.CompoundValidationDecoration;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CountryPropertySheet extends PropertySheet<SaveCountry> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryPropertySheet.class);

    private final MessageSource messageSource;

    private final ObservableList<Culture> cultures;

    private final ObservableList<SaveReligion> religions;

    private final ObservableList<SaveCountry> countriesAlive;

    private final ObservableList<SubjectType> subjectTypes;

    private final ValidationSupport validationSupport;

    private final ClearableTextItem nameField;

    private final CheckBoxItem wasPlayerField;

    private final ClearableSpinnerItem<Integer> admPointField;

    private final ClearableSpinnerItem<Integer> dipPointField;

    private final ClearableSpinnerItem<Integer> milPointField;

    private final ClearableSpinnerItem<Integer> stabilityField;

    private final ClearableSpinnerItem<Double> prestigeField;

    private final ClearableSpinnerItem<Integer> absolutismField;

    private final ClearableComboBoxItem<SaveProvince> capitalField;

    private final ClearableColorPickerItem mapColorField;

    private final ClearableComboBoxItem<Culture> cultureField;

    private final ClearableCheckComboBoxItem<Culture> acceptedCulturesField;

    private final ClearableComboBoxItem<SaveReligion> religionField;

    private final ClearableSliderItem authorityField;

    private final ClearableSliderItem churchPowerField;

    private final ClearableSliderItem fervorField;

    private final ButtonItem religiousReformsButton;

    private ObservableList<ReligiousReform> passedReligiousReforms;

    private ObservableList<ReligiousReform> notPassedReligiousReforms;

    private final ClearableSliderItem patriarchAuthorityField;

    private final ClearableComboBoxItem<FetishistCult> fetishistCultField;

    private final ClearableSpinnerItem<Integer> isolationismField;

    private final ClearableSliderIntItem karmaField;

    private final ClearableSliderItem pietyField;

    private final ClearableSliderItem harmonyField;

    private final ClearableComboBoxItem<PersonalDeity> personalDeityField;

    private final ButtonItem harmonizedReligionGroupsButton;

    private ObservableList<ReligionGroup> harmonizedReligionGroups;

    private ObservableList<ReligionGroup> notHarmonizedReligionGroups;

    private final ButtonItem harmonizedReligionsButton;

    private ObservableList<Religion> harmonizedReligions;

    private ObservableList<Religion> notHarmonizedReligions;

    private final ClearableSliderItem doomField;

    private final ClearableComboBoxItem<SaveReligion> secondaryReligionsField;

    private final ClearableSpinnerItem<Double> governmentReformProgressField;

    private final ClearableComboBoxItem<Integer> governmentRankField;

    private final ButtonItem governmentReformsButton;

    private final ObservableList<GovernmentReform> governmentReformsField;

    private final CustomPropertySheet courtPropertySheet;

    private final CustomPropertySheetSkin courtPropertySheetSkin;

    private MonarchPropertySheet monarchPropertySheet;

    private MonarchPropertySheet heirPropertySheet;

    private MonarchPropertySheet queenPropertySheet;

    private final ButtonItem countrySubjectsButton;

    private Map<SaveCountry, CountrySubject> countrySubjectsField;

    private final ClearableComboBoxItem<SaveCountry> overlordField;

    private final ButtonItem rivalsButton;

    private final ObservableList<Rival> rivals;

    private final ClearableSpinnerItem<Double> treasuryField;

    private final ClearableSpinnerItem<Double> inflationField;

    private final ClearableSliderItem corruptionField;

    private final ClearableSliderIntItem mercantilismField;

    private final List<CheckBoxItem> institutionsEmbracedFields;

    private final ClearableSpinnerItem<Double> manpowerField;

    private final ClearableSpinnerItem<Double> sailorsField;

    private final ClearableSliderItem armyTraditionField;

    private final ClearableSliderItem navyTraditionField;

    private final ClearableSliderItem armyProfessionalismField;

    private final ClearableSliderItem warEhaustionField;

    private final ButtonItem leadersButton;

    private final ObservableList<Leader> leaders;

    private final ButtonItem loansButton;

    private final ObservableList<Loan> loans;

    private final CustomPropertySheet estatesPropertySheet;

    private final List<EstatePropertySheet> estatePropertySheets;

    private final CustomPropertySheetSkin estatesPropertySheetSkin;

    private final ClearableSpinnerItem<Integer> admTechField;

    private final ClearableSpinnerItem<Integer> dipTechField;

    private final ClearableSpinnerItem<Integer> milTechField;

    private final ClearableSliderItem innovativenessField;

    private final ButtonItem ideasButton;

    private final ObservableList<Idea> ideas;

    private final ButtonItem admPoliciesButton;

    private ObservableList<Policy> availableAdmPolicies;

    private final ObservableList<ActivePolicy> admPolicies;

    private final ButtonItem dipPoliciesButton;

    private ObservableList<Policy> availableDipPolicies;

    private final ObservableList<ActivePolicy> dipPolicies;

    private final ButtonItem milPoliciesButton;

    private ObservableList<Policy> availableMilPolicies;

    private final ObservableList<ActivePolicy> milPolicies;

    private final ButtonItem modifiersButton;

    private final ObservableList<Modifier> modifiers;

    private final ObservableList<StringDate> flags;

    private final ButtonItem flagsButton;

    private final ObservableList<StringDate> hiddenFlags;

    private final ButtonItem hiddenFlagsButton;

    private final CheckBoxItem removeAe;

    private final CheckBoxItem removeAnnexPartHre;

    private BooleanProperty colorChanged;

    public CountryPropertySheet(MessageSource messageSource, Save save, ObservableList<SaveCountry> countriesAlive, ObservableList<Culture> cultures,
                                ObservableList<SaveReligion> religions) {
        super(save, null);
        this.messageSource = messageSource;
        this.countriesAlive = countriesAlive;
        this.cultures = cultures;
        this.religions = religions;
        this.subjectTypes = save.getGame()
                                .getSubjectTypes()
                                .stream()
                                .filter(type -> !"default".equals(type.getName()))
                                .sorted(Comparator.comparing(t -> OsaSaveEditorUtils.localize(t.getName(), save.getGame()), Eu4Utils.COLLATOR))
                                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        //GENERAL
        this.nameField = new ClearableTextItem(this.messageSource.getMessage("ose.category.general", null, Constants.LOCALE),
                                               OsaSaveEditorUtils.localize("LEDGER_NAME", save.getGame()));
        this.nameField.getTextField().getStylesheets().add(OsaSaveEditorApplication.class.getResource("/styles/style.css").toExternalForm());
        this.propertySheet.getItems().add(this.nameField);

        this.wasPlayerField = new CheckBoxItem(this.messageSource.getMessage("ose.category.general", null, Constants.LOCALE),
                                               OsaSaveEditorUtils.localize("WAS_PLAYER", save.getGame()), false);
        this.propertySheet.getItems().add(this.wasPlayerField);

        this.admPointField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.general", null, Constants.LOCALE),
                                                        OsaSaveEditorUtils.localize("ADM_POWER", save.getGame()),
                                                        new ClearableSpinnerInt(0, 999, 1));
        this.propertySheet.getItems().add(this.admPointField);

        this.dipPointField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.general", null, Constants.LOCALE),
                                                        OsaSaveEditorUtils.localize("DIP_POWER", save.getGame()),
                                                        new ClearableSpinnerInt(0, 999, 1));
        this.propertySheet.getItems().add(this.dipPointField);

        this.milPointField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.general", null, Constants.LOCALE),
                                                        OsaSaveEditorUtils.localize("MIL_POWER", save.getGame()),
                                                        new ClearableSpinnerInt(0, 999, 1));
        this.propertySheet.getItems().add(this.milPointField);

        this.stabilityField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.general", null, Constants.LOCALE),
                                                         OsaSaveEditorUtils.localize("stability", save.getGame()),
                                                         new ClearableSpinnerInt(-3, 3, 1));
        this.propertySheet.getItems().add(this.stabilityField);

        this.prestigeField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.general", null, Constants.LOCALE),
                                                        OsaSaveEditorUtils.localize("prestige", save.getGame()),
                                                        new ClearableSpinnerDouble(-100, 100, 1));
        this.propertySheet.getItems().add(this.prestigeField);

        this.absolutismField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.general", null, Constants.LOCALE),
                                                          OsaSaveEditorUtils.localize("absolutism", save.getGame()),
                                                          new ClearableSpinnerInt(0, 100, 1));
        this.propertySheet.getItems().add(this.absolutismField);

        this.capitalField = new ClearableComboBoxItem<>(this.messageSource.getMessage("ose.category.general", null, Constants.LOCALE),
                                                        OsaSaveEditorUtils.localize("TRIGGER_CAPITAL", save.getGame()),
                                                        FXCollections.observableArrayList(),
                                                        new ClearableComboBox<>(new SearchableComboBox<>()));
        this.capitalField.setConverter(new ProvinceStringConverter());
        this.capitalField.setCellFactory(new ProvinceStringCellFactory());
        this.propertySheet.getItems().add(this.capitalField);

        this.mapColorField = new ClearableColorPickerItem(this.messageSource.getMessage("ose.category.general", null, Constants.LOCALE),
                                                          OsaSaveEditorUtils.localize("ND_MAP_COLOR", save.getGame()),
                                                          new ClearableColorPicker(new ColorPicker()));
        this.propertySheet.getItems().add(this.mapColorField);

        //Culture
        this.cultureField = new ClearableComboBoxItem<>(OsaSaveEditorUtils.localize("LEDGER_CULTURE", save.getGame()),
                                                        OsaSaveEditorUtils.localize("LEDGER_CULTURE", save.getGame()),
                                                        cultures,
                                                        new ClearableComboBox<>(new SearchableComboBox<>()));
        this.cultureField.setConverter(CultureStringConverter.INSTANCE);
        this.cultureField.setCellFactory(CultureStringCellFactory.INSTANCE);
        this.propertySheet.getItems().add(this.cultureField);

        this.acceptedCulturesField = new ClearableCheckComboBoxItem<>(OsaSaveEditorUtils.localize("LEDGER_CULTURE", save.getGame()),
                                                                      OsaSaveEditorUtils.localize("MAPMODE_ACCEPTEDCULTURES", save.getGame()),
                                                                      cultures,
                                                                      new ClearableCheckComboBox<>());
        this.acceptedCulturesField.setConverter(CultureStringConverter.INSTANCE);
        this.propertySheet.getItems().add(this.acceptedCulturesField);

        //Religion
        this.religionField = new ClearableComboBoxItem<>(OsaSaveEditorUtils.localize("LEDGER_RELIGION", save.getGame()),
                                                         OsaSaveEditorUtils.localize("LEDGER_RELIGION", save.getGame()),
                                                         this.religions,
                                                         new ClearableComboBox<>(new SearchableComboBox<>()));
        this.religionField.setConverter(SaveReligionStringConverter.INSTANCE);
        this.religionField.setCellFactory(SaveReligionStringCellFactory.INSTANCE);
        this.propertySheet.getItems().add(this.religionField);

        this.authorityField = new ClearableSliderItem(OsaSaveEditorUtils.localize("LEDGER_RELIGION", save.getGame()),
                                                      OsaSaveEditorUtils.localize("authority", save.getGame()),
                                                      0, 100);
        this.propertySheet.getItems().add(this.authorityField);

        this.churchPowerField = new ClearableSliderItem(OsaSaveEditorUtils.localize("LEDGER_RELIGION", save.getGame()),
                                                        OsaSaveEditorUtils.localize("MODIFIER_CHURCH_POWER", save.getGame()),
                                                        0, 200);
        this.propertySheet.getItems().add(this.churchPowerField);

        this.fervorField = new ClearableSliderItem(OsaSaveEditorUtils.localize("LEDGER_RELIGION", save.getGame()),
                                                   OsaSaveEditorUtils.localize("FERVOR_VALUE2", save.getGame()),
                                                   0, 100);
        this.propertySheet.getItems().add(this.fervorField);

        this.religiousReformsButton = new ButtonItem(OsaSaveEditorUtils.localize("LEDGER_RELIGION", save.getGame()),
                                                     null,
                                                     OsaSaveEditorUtils.localize("HEADER_RELIGIOUS_REFORMS", save.getGame()),
                                                     2);
        this.propertySheet.getItems().add(this.religiousReformsButton);
        this.passedReligiousReforms = FXCollections.observableArrayList();
        this.notPassedReligiousReforms = FXCollections.observableArrayList();

        this.patriarchAuthorityField = new ClearableSliderItem(OsaSaveEditorUtils.localize("LEDGER_RELIGION", save.getGame()),
                                                               OsaSaveEditorUtils.localize("patriarch_authority_global", save.getGame()),
                                                               0, 100);
        this.propertySheet.getItems().add(this.patriarchAuthorityField);

        this.fetishistCultField = new ClearableComboBoxItem<>(OsaSaveEditorUtils.localize("LEDGER_RELIGION", save.getGame()),
                                                              OsaSaveEditorUtils.localize("HAS_ADOPTED_CULT", save.getGame()),
                                                              FXCollections.observableArrayList(),
                                                              new ClearableComboBox<>(new SearchableComboBox<>()));
        this.fetishistCultField.setConverter(new FetishistCultStringConverter(save.getGame()));
        this.fetishistCultField.setCellFactory(new FetishistCultStringCellFactory(save.getGame()));
        this.propertySheet.getItems().add(this.fetishistCultField);

        this.isolationismField = new ClearableSpinnerItem<>(OsaSaveEditorUtils.localize("LEDGER_RELIGION", save.getGame()),
                                                            OsaSaveEditorUtils.localize("ISOLATIONISM", save.getGame()),
                                                            new ClearableSpinnerInt(0, 4, 1));
        this.propertySheet.getItems().add(this.isolationismField);

        this.karmaField = new ClearableSliderIntItem(OsaSaveEditorUtils.localize("LEDGER_RELIGION", save.getGame()),
                                                     OsaSaveEditorUtils.localize("CURRENT_KARMA", save.getGame()),
                                                     -100, 100);
        this.propertySheet.getItems().add(this.karmaField);

        this.pietyField = new ClearableSliderItem(OsaSaveEditorUtils.localize("LEDGER_RELIGION", save.getGame()),
                                                  OsaSaveEditorUtils.localize("CURRENT_PIETY", save.getGame()),
                                                  0, 100);
        this.propertySheet.getItems().add(this.pietyField);

        this.harmonyField = new ClearableSliderItem(OsaSaveEditorUtils.localize("LEDGER_RELIGION", save.getGame()),
                                                    OsaSaveEditorUtils.localize("CURRENT_HARMONY", save.getGame()),
                                                    0, 100);
        this.propertySheet.getItems().add(this.harmonyField);

        this.harmonizedReligionGroupsButton = new ButtonItem(OsaSaveEditorUtils.localize("LEDGER_RELIGION", save.getGame()),
                                                             null,
                                                             OsaSaveEditorUtils.localize("HARMONIZED_RELIGION_GROUP", save.getGame()),
                                                             2);
        this.propertySheet.getItems().add(this.harmonizedReligionGroupsButton);
        this.harmonizedReligionGroups = FXCollections.observableArrayList();
        this.notHarmonizedReligionGroups = FXCollections.observableArrayList();

        this.harmonizedReligionsButton = new ButtonItem(OsaSaveEditorUtils.localize("LEDGER_RELIGION", save.getGame()),
                                                        null,
                                                        OsaSaveEditorUtils.localize("HARMONIZED_RELIGION", save.getGame()),
                                                        2);
        this.propertySheet.getItems().add(this.harmonizedReligionsButton);
        this.harmonizedReligions = FXCollections.observableArrayList();
        this.notHarmonizedReligions = FXCollections.observableArrayList();

        this.personalDeityField = new ClearableComboBoxItem<>(OsaSaveEditorUtils.localize("LEDGER_RELIGION", save.getGame()),
                                                              OsaSaveEditorUtils.localize("HAS_DEITY", save.getGame()),
                                                              FXCollections.observableArrayList(),
                                                              new ClearableComboBox<>(new SearchableComboBox<>()));
        this.personalDeityField.setConverter(new PersonalDeityStringConverter(save.getGame()));
        this.personalDeityField.setCellFactory(new PersonalDeityStringCellFactory(save.getGame()));
        this.propertySheet.getItems().add(this.personalDeityField);

        this.doomField = new ClearableSliderItem(OsaSaveEditorUtils.localize("LEDGER_RELIGION", save.getGame()),
                                                 OsaSaveEditorUtils.localize("doom", save.getGame()),
                                                 0, 100);
        this.propertySheet.getItems().add(this.doomField);

        this.secondaryReligionsField = new ClearableComboBoxItem<>(OsaSaveEditorUtils.localize("LEDGER_RELIGION", save.getGame()),
                                                                   OsaSaveEditorUtils.localize("SECONDARY_RELIGION", save.getGame()),
                                                                   FXCollections.observableArrayList(this.religions),
                                                                   new ClearableComboBox<>(new SearchableComboBox<>()));
        this.secondaryReligionsField.setConverter(SaveReligionStringConverter.INSTANCE);
        this.secondaryReligionsField.setCellFactory(SaveReligionStringCellFactory.INSTANCE);
        this.propertySheet.getItems().add(this.secondaryReligionsField);

        //Economy
        this.treasuryField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.economy", null, Constants.LOCALE),
                                                        OsaSaveEditorUtils.localize("TECH_TRESURY_TITLE", save.getGame()),
                                                        new ClearableSpinnerDouble(-1000000, 1000000, 100));
        this.propertySheet.getItems().add(this.treasuryField);

        this.inflationField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.economy", null, Constants.LOCALE),
                                                         OsaSaveEditorUtils.localize("inflation", save.getGame()),
                                                         new ClearableSpinnerDouble(0, Integer.MAX_VALUE / 1000.0, 1));
        this.propertySheet.getItems().add(this.inflationField);

        this.corruptionField = new ClearableSliderItem(this.messageSource.getMessage("ose.category.economy", null, Constants.LOCALE),
                                                       OsaSaveEditorUtils.localize("corruption", save.getGame()),
                                                       0, 100);
        this.propertySheet.getItems().add(this.corruptionField);

        this.mercantilismField = new ClearableSliderIntItem(this.messageSource.getMessage("ose.category.economy", null, Constants.LOCALE),
                                                            OsaSaveEditorUtils.localize("MERCANTILISM_LABEL", save.getGame()),
                                                            0, 100);
        this.propertySheet.getItems().add(this.mercantilismField);

        this.loansButton = new ButtonItem(this.messageSource.getMessage("ose.category.economy", null, Constants.LOCALE), null, save.getGame()
                                                                                                                                   .getLocalisationClean(
                                                                                                                                           "AI_LOANS",
                                                                                                                                           Eu4Language.getByLocale(
                                                                                                                                                   Constants.LOCALE)),
                                          2);
        this.propertySheet.getItems().add(this.loansButton);
        this.loans = FXCollections.observableArrayList();

        //Institutions
        this.institutionsEmbracedFields = new ArrayList<>();

        //GOVERNMENT
        this.governmentRankField = new ClearableComboBoxItem<>(this.messageSource.getMessage("ose.category.government", null, Constants.LOCALE),
                                                               save.getGame().getLocalisationClean("GOV_RANK", Eu4Language.getByLocale(Constants.LOCALE)),
                                                               FXCollections.observableArrayList(),
                                                               new ClearableComboBox<>(new RequiredComboBox<>()));
        this.governmentRankField.setConverter(new GovernmentRankConverter());
        this.governmentRankField.setCellFactory(new GovernmentRankCellFactory());
        this.propertySheet.getItems().add(this.governmentRankField);

        this.governmentReformProgressField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.government", null, Constants.LOCALE),
                                                                        save.getGame()
                                                                            .getLocalisationCleanNoPunctuation("CHANGE_GOVERNMENT_REFORM_PROGRESS",
                                                                                                               Eu4Language.getByLocale(Constants.LOCALE)),
                                                                        new ClearableSpinnerDouble(0, Double.MAX_VALUE, 10));
        this.propertySheet.getItems().add(this.governmentReformProgressField);

        this.governmentReformsButton = new ButtonItem(this.messageSource.getMessage("ose.category.government", null, Constants.LOCALE),
                                                      null,
                                                      save.getGame().getLocalisationClean("governmental_reforms", Eu4Language.getByLocale(Constants.LOCALE)),
                                                      2);
        this.propertySheet.getItems().add(this.governmentReformsButton);
        this.governmentReformsField = FXCollections.observableArrayList();

        //CATEGORY_COURT
        this.courtPropertySheet = new CustomPropertySheet();
        this.courtPropertySheet.setPropertyEditorFactory(new CustomPropertyEditorFactory());
        this.courtPropertySheet.setMode(CustomPropertySheet.Mode.CATEGORY);
        this.courtPropertySheet.setModeSwitcherVisible(false);
        this.courtPropertySheet.setSearchBoxVisible(false);
        this.courtPropertySheetSkin = new CustomPropertySheetSkin(this.courtPropertySheet);
        this.courtPropertySheet.setSkin(this.courtPropertySheetSkin);
        this.propertySheet.getItems()
                          .add(new PropertySheetItem(this.save.getGame().getLocalisationClean("CATEGORY_COURT", Eu4Language.getByLocale(Constants.LOCALE)),
                                                     this.courtPropertySheet));

        //Diplomacy
        this.overlordField = new ClearableComboBoxItem<>(save.getGame().getLocalisationClean("HEADER_DIPLOMACY", Eu4Language.getByLocale(Constants.LOCALE)),
                                                         save.getGame().getLocalisationClean("HEADER_OVERLORD", Eu4Language.getByLocale(Constants.LOCALE)),
                                                         FXCollections.observableArrayList(new ArrayList<>()),
                                                         new ClearableComboBox<>(new ComboBox<>()));
        this.overlordField.setEditable(false);
        this.propertySheet.getItems().add(this.overlordField);

        this.countrySubjectsButton = new ButtonItem(save.getGame().getLocalisationClean("HEADER_DIPLOMACY", Eu4Language.getByLocale(Constants.LOCALE)),
                                                    null,
                                                    save.getGame().getLocalisationClean("HEADER_SUBJECTS", Eu4Language.getByLocale(Constants.LOCALE)),
                                                    2);
        this.propertySheet.getItems().add(this.countrySubjectsButton);

        this.rivalsButton = new ButtonItem(save.getGame().getLocalisationClean("HEADER_DIPLOMACY", Eu4Language.getByLocale(Constants.LOCALE)), null,
                                           save.getGame().getLocalisationClean("RIVALS", Eu4Language.getByLocale(Constants.LOCALE)), 2);
        this.propertySheet.getItems().add(this.rivalsButton);
        this.rivals = FXCollections.observableArrayList();

        //Military
        this.manpowerField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.military", null, Constants.LOCALE),
                                                        save.getGame()
                                                            .getLocalisationCleanNoPunctuation("HINT_MANPOWER_TITLE",
                                                                                               Eu4Language.getByLocale(Constants.LOCALE)),
                                                        new ClearableSpinnerDouble(0, Double.MAX_VALUE, 1000));
        this.propertySheet.getItems().add(this.manpowerField);

        this.sailorsField = new ClearableSpinnerItem<>(this.messageSource.getMessage("ose.category.military", null, Constants.LOCALE),
                                                       save.getGame()
                                                           .getLocalisationCleanNoPunctuation("LEDGER_SAILORS", Eu4Language.getByLocale(Constants.LOCALE)),
                                                       new ClearableSpinnerDouble(0, Double.MAX_VALUE, 1000));
        this.propertySheet.getItems().add(this.sailorsField);

        this.armyTraditionField = new ClearableSliderItem(this.messageSource.getMessage("ose.category.military", null, Constants.LOCALE),
                                                          OsaSaveEditorUtils.localize("army_tradition", save.getGame()),
                                                          0, 100);
        this.propertySheet.getItems().add(this.armyTraditionField);

        this.navyTraditionField = new ClearableSliderItem(this.messageSource.getMessage("ose.category.military", null, Constants.LOCALE),
                                                          OsaSaveEditorUtils.localize("navy_tradition", save.getGame()),
                                                          0, 100);
        this.propertySheet.getItems().add(this.navyTraditionField);

        this.armyProfessionalismField = new ClearableSliderItem(this.messageSource.getMessage("ose.category.military", null, Constants.LOCALE),
                                                                OsaSaveEditorUtils.localize("army_professionalism", save.getGame()),
                                                                0, save.getGame().getMaxArmyProfessionalism());
        this.propertySheet.getItems().add(this.armyProfessionalismField);

        this.warEhaustionField = new ClearableSliderItem(this.messageSource.getMessage("ose.category.military", null, Constants.LOCALE),
                                                         OsaSaveEditorUtils.localize("WAR_EXHAUSTION", save.getGame()),
                                                         0, 20);
        this.propertySheet.getItems().add(this.warEhaustionField);

        this.leadersButton = new ButtonItem(this.messageSource.getMessage("ose.category.military", null, Constants.LOCALE), null, save.getGame()
                                                                                                                                      .getLocalisationClean(
                                                                                                                                              "HEADER_LEADER",
                                                                                                                                              Eu4Language.getByLocale(
                                                                                                                                                      Constants.LOCALE)),
                                            2);
        this.propertySheet.getItems().add(this.leadersButton);
        this.leaders = FXCollections.observableArrayList();

        //ESTATES
        this.estatesPropertySheet = new CustomPropertySheet();
        this.estatesPropertySheet.setPropertyEditorFactory(new CustomPropertyEditorFactory());
        this.estatesPropertySheet.setMode(CustomPropertySheet.Mode.CATEGORY);
        this.estatesPropertySheet.setModeSwitcherVisible(false);
        this.estatesPropertySheet.setSearchBoxVisible(false);
        this.estatesPropertySheetSkin = new CustomPropertySheetSkin(this.estatesPropertySheet);
        this.estatesPropertySheet.setSkin(this.estatesPropertySheetSkin);
        this.propertySheet.getItems()
                          .add(new PropertySheetItem(this.save.getGame().getLocalisationClean("HEADER_ESTATES", Eu4Language.getByLocale(Constants.LOCALE)),
                                                     this.estatesPropertySheet));

        this.estatePropertySheets = new ArrayList<>();

        //TECHNOLOGY
        this.admTechField = new ClearableSpinnerItem<>(save.getGame().getLocalisationClean("HEADER_TECHNOLOGY", Eu4Language.getByLocale(Constants.LOCALE)),
                                                       save.getGame().getLocalisationClean("adm_tech", Eu4Language.getByLocale(Constants.LOCALE)),
                                                       new ClearableSpinnerInt(0, save.getGame().getTechnologies(Power.ADM).size(), 1));
        this.propertySheet.getItems().add(this.admTechField);

        this.dipTechField = new ClearableSpinnerItem<>(save.getGame().getLocalisationClean("HEADER_TECHNOLOGY", Eu4Language.getByLocale(Constants.LOCALE)),
                                                       save.getGame().getLocalisationClean("dip_tech", Eu4Language.getByLocale(Constants.LOCALE)),
                                                       new ClearableSpinnerInt(0, save.getGame().getTechnologies(Power.DIP).size(), 1));
        this.propertySheet.getItems().add(this.dipTechField);

        this.milTechField = new ClearableSpinnerItem<>(save.getGame().getLocalisationClean("HEADER_TECHNOLOGY", Eu4Language.getByLocale(Constants.LOCALE)),
                                                       save.getGame().getLocalisationClean("mil_tech", Eu4Language.getByLocale(Constants.LOCALE)),
                                                       new ClearableSpinnerInt(0, save.getGame().getTechnologies(Power.MIL).size(), 1));
        this.propertySheet.getItems().add(this.milTechField);

        this.innovativenessField = new ClearableSliderItem(save.getGame().getLocalisationClean("HEADER_TECHNOLOGY", Eu4Language.getByLocale(Constants.LOCALE)),
                                                           save.getGame().getLocalisationClean("innovativeness", Eu4Language.getByLocale(Constants.LOCALE)),
                                                           0, save.getGame().getInnovativenessMax());
        this.propertySheet.getItems().add(this.innovativenessField);

        this.ideas = FXCollections.observableArrayList();
        this.ideasButton = new ButtonItem(save.getGame().getLocalisationClean("HEADER_TECHNOLOGY", Eu4Language.getByLocale(Constants.LOCALE)), null,
                                          save.getGame().getLocalisationClean("HEADER_IDEAS", Eu4Language.getByLocale(Constants.LOCALE)), 2);
        this.propertySheet.getItems().add(this.ideasButton);

        this.admPolicies = FXCollections.observableArrayList();
        this.admPoliciesButton = new ButtonItem(save.getGame().getLocalisationClean("HEADER_TECHNOLOGY", Eu4Language.getByLocale(Constants.LOCALE)), null,
                                                save.getGame().getLocalisationClean("POLICYVIEW_ADMINISTRATIVE", Eu4Language.getByLocale(Constants.LOCALE)), 2);
        this.propertySheet.getItems().add(this.admPoliciesButton);

        this.dipPolicies = FXCollections.observableArrayList();
        this.dipPoliciesButton = new ButtonItem(save.getGame().getLocalisationClean("HEADER_TECHNOLOGY", Eu4Language.getByLocale(Constants.LOCALE)), null,
                                                save.getGame().getLocalisationClean("POLICYVIEW_DIPLOMATIC", Eu4Language.getByLocale(Constants.LOCALE)), 2);
        this.propertySheet.getItems().add(this.dipPoliciesButton);

        this.milPolicies = FXCollections.observableArrayList();
        this.milPoliciesButton = new ButtonItem(save.getGame().getLocalisationClean("HEADER_TECHNOLOGY", Eu4Language.getByLocale(Constants.LOCALE)), null,
                                                save.getGame().getLocalisationClean("POLICYVIEW_MILITARY", Eu4Language.getByLocale(Constants.LOCALE)), 2);
        this.propertySheet.getItems().add(this.milPoliciesButton);

        //Modifiers
        this.modifiers = FXCollections.observableArrayList();
        this.modifiersButton = new ButtonItem(save.getGame().getLocalisationClean("DOMESTIC_MODIFIERS", Eu4Language.getByLocale(Constants.LOCALE)), null,
                                              save.getGame().getLocalisationClean("DOMESTIC_MODIFIERS", Eu4Language.getByLocale(Constants.LOCALE)));
        this.propertySheet.getItems().add(this.modifiersButton);

        //Flags
        this.flags = FXCollections.observableArrayList();
        this.flagsButton = new ButtonItem(this.messageSource.getMessage("ose.category.flags", null, Constants.LOCALE), null,
                                          this.messageSource.getMessage("ose.category.flags", null, Constants.LOCALE));
        this.propertySheet.getItems().add(this.flagsButton);

        this.hiddenFlags = FXCollections.observableArrayList();
        this.hiddenFlagsButton = new ButtonItem(this.messageSource.getMessage("ose.category.flags", null, Constants.LOCALE), null,
                                                this.messageSource.getMessage("ose.category.hidden-flags", null, Constants.LOCALE));
        this.propertySheet.getItems().add(this.hiddenFlagsButton);

        //Other
        this.removeAe = new CheckBoxItem(this.messageSource.getMessage("ose.category.other", null, Constants.LOCALE),
                                         this.messageSource.getMessage("country.removeAe", null, Constants.LOCALE), false);
        this.propertySheet.getItems().add(this.removeAe);

        this.removeAnnexPartHre = new CheckBoxItem(this.messageSource.getMessage("ose.category.other", null, Constants.LOCALE),
                                                   this.messageSource.getMessage("country.removeAnnexPartHre", null, Constants.LOCALE), false);
        this.propertySheet.getItems().add(this.removeAnnexPartHre);

        this.validationSupport = new ValidationSupport();
        this.validationSupport.registerValidator(this.nameField.getTextField(), Validator.createEmptyValidator("Text is required"));
        this.validationSupport.setValidationDecorator(new CompoundValidationDecoration(new CustomGraphicValidationDecoration(),
                                                                                       new StyleClassValidationDecoration("validation-error", null)));
    }

    @Override
    public Set<CustomPropertySheet.Item<?>> internalUpdate(SaveCountry country) {
        this.colorChanged.set(false);
        if (this.t == null) {
            return new HashSet<>();
        } else {
            String estateExpandedPaneName = this.estatesPropertySheetSkin.getAccordion().getExpandedPane() == null ? null :
                                            this.estatesPropertySheetSkin.getAccordion().getExpandedPane().getText();

            String courtExpandedPaneName = this.courtPropertySheetSkin.getAccordion().getExpandedPane() == null ? null :
                                           this.courtPropertySheetSkin.getAccordion().getExpandedPane().getText();

            Set<CustomPropertySheet.Item<?>> items = new HashSet<>();

            //GENERAL
            this.nameField.setValue(CountryStringConverter.INSTANCE.toString(this.t));
            this.nameField.setSupplier(() -> CountryStringConverter.INSTANCE.toString(this.t));
            this.nameField.setEditable(this.t.isNameEditable());
            items.add(this.nameField);

            this.wasPlayerField.setValue(this.t.wasPlayer());
            items.add(this.wasPlayerField);

            this.admPointField.setSupplier(() -> this.t.getPowers().get(Power.ADM));
            this.admPointField.setMax(Math.max(this.t.getPowers().get(Power.ADM), 999));
            this.admPointField.setValue(this.t.getPowers().get(Power.ADM));
            items.add(this.admPointField);

            this.dipPointField.setSupplier(() -> this.t.getPowers().get(Power.DIP));
            this.dipPointField.setMax(Math.max(this.t.getPowers().get(Power.DIP), 999));
            this.dipPointField.setValue(this.t.getPowers().get(Power.DIP));
            items.add(this.dipPointField);

            this.milPointField.setSupplier(() -> this.t.getPowers().get(Power.MIL));
            this.milPointField.setMax(Math.max(this.t.getPowers().get(Power.MIL), 999));
            this.milPointField.setValue(this.t.getPowers().get(Power.MIL));
            items.add(this.milPointField);

            this.stabilityField.setSupplier(this.t::getStability);
            this.stabilityField.setValue(this.t.getStability());
            items.add(this.stabilityField);

            this.prestigeField.setValue(this.t.getPrestige());
            this.prestigeField.setSupplier(this.t::getPrestige);
            items.add(this.prestigeField);

            this.absolutismField.setValue(this.t.getAbsolutism());
            this.absolutismField.setSupplier(this.t::getAbsolutism);

            if (MapUtils.isNotEmpty(this.t.getSave().getCurrentAge().getAbsolutism())) {
                items.add(this.absolutismField);
            }

            this.capitalField.setValues(FXCollections.observableArrayList(this.t.getOwnedProvinces()
                                                                                .stream()
                                                                                .sorted(Comparator.comparing(SaveProvince::getName,
                                                                                                             Eu4Utils.COLLATOR))
                                                                                .toList()));
            this.capitalField.setValue(this.t.getCapital());
            this.capitalField.setSupplier(this.t::getCapital);
            items.add(this.capitalField);

            this.mapColorField.setValue(OsaSaveEditorUtils.countryToMapColor(country));
            items.add(this.mapColorField);

            //Culture
            this.cultureField.setValue(this.t.getPrimaryCulture());
            this.cultureField.setSupplier(this.t::getPrimaryCulture);
            items.add(this.cultureField);

            this.acceptedCulturesField.setValue(FXCollections.observableList(this.t.getAcceptedCultures()));
            this.acceptedCulturesField.setSupplier(this.t::getAcceptedCultures);
            items.add(this.acceptedCulturesField);

            //Religion
            this.religionField.setValue(this.t.getReligion());
            this.religionField.setSupplier(this.t::getReligion);
            items.add(this.religionField);

            if (this.t.getReligion() != null && this.t.getReligion().getGameReligion().isUseAuthority()) {
                this.authorityField.setValue(this.t.getAuthority());
                this.authorityField.setSupplier(this.t::getAuthority);
                items.add(this.authorityField);
            }

            if (this.t.getReligion() != null && this.t.getReligion().getGameReligion().isUseAuthority()) {
                this.authorityField.setValue(this.t.getAuthority());
                this.authorityField.setSupplier(this.t::getAuthority);
                items.add(this.authorityField);
            }

            if (this.t.getReligion() != null && this.t.getReligion().getGameReligion().useReligiousReforms()) {
                this.passedReligiousReforms = FXCollections.observableArrayList(this.t.getReligiousReforms().getAdoptedReforms());
                this.notPassedReligiousReforms = this.t.getSave()
                                                       .getGame()
                                                       .getReligiousReforms(this.t.getReligiousReforms().getReligiousReforms().getName())
                                                       .getReforms()
                                                       .stream()
                                                       .filter(Predicate.not(this.passedReligiousReforms::contains))
                                                       .collect(Collectors.toCollection(FXCollections::observableArrayList));
                this.religiousReformsButton.getButton().setOnAction(event -> {
                    CustomListSelectionView<ReligiousReform> listSelectionView = new CustomListSelectionView<>(
                            FXCollections.observableArrayList(this.notPassedReligiousReforms),
                            FXCollections.observableArrayList(this.passedReligiousReforms),
                            new ReligiousReformStringCellFactory(this.t.getSave().getGame()),
                            750, 600);

                    ListSelectionViewDialog<ReligiousReform> dialog = new ListSelectionViewDialog<>(this.t.getSave(),
                                                                                                    listSelectionView,
                                                                                                    this.t.getSave()
                                                                                                          .getGame()
                                                                                                          .getLocalisationClean("CELESTIAL_DECISIONS",
                                                                                                                                Eu4Language.getByLocale(
                                                                                                                                        Constants.LOCALE)),
                                                                                                    () -> this.notPassedReligiousReforms,
                                                                                                    () -> this.passedReligiousReforms);

                    Optional<List<ReligiousReform>> reforms = dialog.showAndWait();

                    reforms.ifPresent(religiousReforms -> {
                        this.passedReligiousReforms.setAll(religiousReforms);
                        this.notPassedReligiousReforms.setAll(this.t.getSave()
                                                                    .getGame()
                                                                    .getReligiousReforms(
                                                                            this.t.getReligiousReforms().getReligiousReforms().getName())
                                                                    .getReforms()
                                                                    .stream()
                                                                    .filter(Predicate.not(this.passedReligiousReforms::contains))
                                                                    .toArray(ReligiousReform[]::new));
                    });
                });

                items.add(this.religiousReformsButton);
            }

            if (this.t.getReligion() != null &&
                (this.t.getReligion().getGameReligion().usesAnglicanPower()
                 || this.t.getReligion().getGameReligion().usesHussitePower()
                 || this.t.getReligion().getGameReligion().usesChurchPower())) {
                this.churchPowerField.setValue(this.t.getChurch().getPower());
                this.churchPowerField.setSupplier(() -> this.t.getChurch().getPower());
                items.add(this.churchPowerField);
            }

            if (this.t.getReligion() != null && CollectionUtils.isNotEmpty(this.t.getReligion().getGameReligion().getAspects())) {
                //Todo
            }

            if (this.t.getReligion() != null && this.t.getReligion().getGameReligion().useFervor()) {
                this.fervorField.setValue(this.t.getFervor().getValue());
                this.fervorField.setSupplier(() -> this.t.getFervor().getValue());
                items.add(this.fervorField);
            }

            if (this.t.getReligion() != null && this.t.getReligion().getGameReligion().hasPatriarchs()) {
                this.patriarchAuthorityField.setValue(this.t.getPatriarchAuthority());
                this.patriarchAuthorityField.setSupplier(() -> this.t.getPatriarchAuthority());
                items.add(this.patriarchAuthorityField);
            }

            if (this.t.getReligion() != null && this.t.getReligion().getGameReligion().useFetishistCult()) {
                this.fetishistCultField.getChoices()
                                       .setAll(Stream.concat(Stream.of((FetishistCult) null), this.t.getUnlockedFetishistCults().stream())
                                                     .sorted(Comparator.nullsFirst(
                                                             Comparator.comparing(f -> OsaSaveEditorUtils.localize(f.getName(),
                                                                                                                   this.t.getSave().getGame()),
                                                                                  Eu4Utils.COLLATOR)))
                                                     .toArray(FetishistCult[]::new));
                this.fetishistCultField.setValue(this.t.getFetishistCult());
                this.fetishistCultField.setSupplier(this.t::getFetishistCult);
                items.add(this.fetishistCultField);
            }

            if (this.t.getReligion() != null && this.t.getReligion().getGameReligion().usesIsolationism()) {
                this.isolationismField.setSupplier(this.t::getIsolationismLevel);
                this.isolationismField.setValue(this.t.getIsolationismLevel());
                items.add(this.isolationismField);
            }

            if (this.t.getReligion() != null && this.t.getReligion().getGameReligion().usesKarma()) {
                this.karmaField.setValue(this.t.getKarma());
                this.karmaField.setSupplier(() -> this.t.getKarma());
                items.add(this.karmaField);
            }

            if (this.t.getReligion() != null && this.t.getReligion().getGameReligion().usePersonalDeity()) {
                this.personalDeityField.getChoices()
                                       .setAll(Stream.concat(Stream.of((PersonalDeity) null), this.t.getUnlockedPersonalDeities().stream())
                                                     .sorted(Comparator.nullsFirst(
                                                             Comparator.comparing(p -> OsaSaveEditorUtils.localize(p.getName(),
                                                                                                                   this.t.getSave().getGame()),
                                                                                  Eu4Utils.COLLATOR)))
                                                     .toArray(PersonalDeity[]::new));
                this.personalDeityField.setValue(this.t.getPersonalDeity());
                this.personalDeityField.setSupplier(this.t::getPersonalDeity);
                items.add(this.personalDeityField);
            }

            if (this.t.getReligion() != null && this.t.getReligion().getGameReligion().usesPiety()) {
                this.pietyField.setValue(this.t.getPiety());
                this.pietyField.setSupplier(() -> this.t.getPiety());
                items.add(this.pietyField);
            }

            if (this.t.getReligion() != null && this.t.getReligion().getGameReligion().usesHarmony()) {
                this.harmonyField.setValue(this.t.getHarmony());
                this.harmonyField.setSupplier(() -> this.t.getHarmony());
                items.add(this.harmonyField);

                this.harmonizedReligionGroups = FXCollections.observableArrayList(this.t.getHarmonizedReligionGroups());
                this.notHarmonizedReligionGroups = this.t.getSave()
                                                         .getGame()
                                                         .getReligionGroups()
                                                         .stream()
                                                         .filter(religionGroup -> StringUtils.isNotBlank(religionGroup.getHarmonizedModifier()))
                                                         .filter(Predicate.not(this.harmonizedReligionGroups::contains))
                                                         .collect(Collectors.toCollection(FXCollections::observableArrayList));
                this.harmonizedReligionGroupsButton.getButton().setOnAction(event -> {
                    CustomListSelectionView<ReligionGroup> listSelectionView = new CustomListSelectionView<>(
                            FXCollections.observableArrayList(this.notHarmonizedReligionGroups),
                            FXCollections.observableArrayList(this.harmonizedReligionGroups),
                            new ReligionGroupStringCellFactory(this.t.getSave().getGame()),
                            750, 600);

                    ListSelectionViewDialog<ReligionGroup> dialog =
                            new ListSelectionViewDialog<>(this.t.getSave(),
                                                          listSelectionView,
                                                          this.t.getSave()
                                                                .getGame()
                                                                .getLocalisationClean("HARMONIZED_RELIGION_GROUP",
                                                                                      Eu4Language.getByLocale(Constants.LOCALE)),
                                                          () -> this.notHarmonizedReligionGroups,
                                                          () -> this.harmonizedReligionGroups);

                    Optional<List<ReligionGroup>> reforms = dialog.showAndWait();

                    reforms.ifPresent(religiousReforms -> {
                        this.harmonizedReligionGroups.setAll(religiousReforms);
                        this.notHarmonizedReligionGroups.setAll(this.t.getSave()
                                                                      .getGame()
                                                                      .getReligionGroups()
                                                                      .stream()
                                                                      .filter(religionGroup -> StringUtils.isNotBlank(
                                                                              religionGroup.getHarmonizedModifier()))
                                                                      .filter(Predicate.not(this.harmonizedReligionGroups::contains))
                                                                      .toArray(ReligionGroup[]::new));
                    });
                });

                items.add(this.harmonizedReligionGroupsButton);

                this.harmonizedReligions = FXCollections.observableArrayList(this.t.getHarmonizedReligions()
                                                                                   .stream()
                                                                                   .map(SaveReligion::getGameReligion)
                                                                                   .collect(Collectors.toList()));
                this.notHarmonizedReligions = this.t.getSave()
                                                    .getGame()
                                                    .getReligions()
                                                    .stream()
                                                    .filter(religion -> StringUtils.isNotBlank(religion.getHarmonizedModifier()))
                                                    .filter(Predicate.not(this.harmonizedReligions::contains))
                                                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
                this.harmonizedReligionsButton.getButton().setOnAction(event -> {
                    CustomListSelectionView<Religion> listSelectionView = new CustomListSelectionView<>(
                            FXCollections.observableArrayList(this.notHarmonizedReligions),
                            FXCollections.observableArrayList(this.harmonizedReligions),
                            ReligionStringCellFactory.INSTANCE,
                            750, 600);

                    ListSelectionViewDialog<Religion> dialog =
                            new ListSelectionViewDialog<>(this.t.getSave(),
                                                          listSelectionView,
                                                          this.t.getSave()
                                                                .getGame()
                                                                .getLocalisationClean("HARMONIZED_RELIGION_",
                                                                                      Eu4Language.getByLocale(Constants.LOCALE)),
                                                          () -> this.notHarmonizedReligions,
                                                          () -> this.harmonizedReligions);

                    Optional<List<Religion>> reforms = dialog.showAndWait();

                    reforms.ifPresent(religiousReforms -> {
                        this.harmonizedReligions.setAll(religiousReforms);
                        this.notHarmonizedReligions.setAll(this.t.getSave()
                                                                 .getGame()
                                                                 .getReligions()
                                                                 .stream()
                                                                 .filter(religion -> StringUtils.isNotBlank(religion.getHarmonizedModifier()))
                                                                 .filter(Predicate.not(this.harmonizedReligions::contains))
                                                                 .toArray(Religion[]::new));
                    });
                });

                items.add(this.harmonizedReligionsButton);
            }

            if (this.t.getReligion() != null && this.t.getReligion().getGameReligion().useDoom()) {
                this.doomField.setValue(this.t.getDoom());
                this.doomField.setSupplier(() -> this.t.getDoom());
                items.add(this.doomField);
            }

            if (this.t.getReligion() != null && this.t.getReligion().getGameReligion().canHaveSecondaryReligion()) {
                this.secondaryReligionsField.getChoices()
                                            .setAll(Stream.concat(Stream.of((SaveReligion) null),
                                                                  this.t.getAvailableSecondaryReligions()
                                                                        .stream()
                                                                        .filter(religion -> !religion.equals(this.t.getReligion())))
                                                          .sorted(Comparator.nullsFirst(
                                                                  Comparator.comparing(r -> OsaSaveEditorUtils.localize(r.getName(),
                                                                                                                        this.t.getSave().getGame()),
                                                                                       Eu4Utils.COLLATOR)))
                                                          .toArray(SaveReligion[]::new));
                this.secondaryReligionsField.setValue(this.t.getSecondaryReligion());
                this.secondaryReligionsField.setSupplier(this.t::getSecondaryReligion);
                items.add(this.secondaryReligionsField);
            }

            if (this.t.getReligion() != null && CollectionUtils.isNotEmpty(this.t.getReligion().getGameReligion().getIcons())) {
                //Todo
            }

            //Economy
            this.treasuryField.setValue(this.t.getTreasury());
            this.treasuryField.setSupplier(this.t::getTreasury);
            items.add(this.treasuryField);

            this.inflationField.setValue(this.t.getInflation());
            this.inflationField.setSupplier(this.t::getInflation);
            items.add(this.inflationField);

            this.corruptionField.setValue(this.t.getCorruption());
            this.corruptionField.setSupplier(this.t::getCorruption);
            items.add(this.corruptionField);

            this.mercantilismField.setValue(this.t.getMercantilism());
            this.mercantilismField.setSupplier(this.t::getMercantilism);
            items.add(this.mercantilismField);

            this.loans.setAll(this.t.getLoans().stream().map(Loan::new).collect(Collectors.toList()));
            this.loansButton.getButton().setOnAction(event -> {
                TableViewDialog<Loan> dialog = new TableViewDialog<>(this.t.getSave(),
                                                                     new TableView2Loan(this.t, this.loans),
                                                                     this.t.getSave()
                                                                           .getGame()
                                                                           .getLocalisationClean("AI_LOANS",
                                                                                                 Eu4Language.getByLocale(Constants.LOCALE)),
                                                                     (list) -> new Loan(300, 4, this.t.getSave().getDate().plusYears(5)),
                                                                     () -> this.loans);
                Optional<List<Loan>> countrySubjects = dialog.showAndWait();

                countrySubjects.ifPresent(this.loans::setAll);
            });
            items.add(this.loansButton);

            //Institutions
            this.institutionsEmbracedFields.clear();
            for (int i = 0; i < this.t.getSave().getInstitutions().getNbInstitutions(); i++) {
                CheckBoxItem checkBoxItem = new CheckBoxItem(OsaSaveEditorUtils.localize("INSTITUTIONS", this.t.getSave().getGame()),
                                                             OsaSaveEditorUtils.localize(this.t.getSave().getGame().getInstitution(i).getName(),
                                                                                         this.t.getSave().getGame()),
                                                             this.t.getEmbracedInstitution(i));
                this.institutionsEmbracedFields.add(checkBoxItem);
            }
            items.addAll(this.institutionsEmbracedFields);

            //Government
            ((GovernmentRankConverter) this.governmentRankField.getConverter()).setCountry(this.t);
            ((GovernmentRankCellFactory) this.governmentRankField.getCellFactory()).setCountry(this.t);
            this.governmentRankField.setValues(FXCollections.observableArrayList(this.t.getGovernmentName().getRanks().keySet()));
            this.governmentRankField.setSupplier(() -> this.t.getGovernmentLevel());
            this.governmentRankField.setValue(this.t.getGovernmentLevel());
            items.add(this.governmentRankField);

            this.governmentReformProgressField.setSupplier(this.t::getGovernmentReformProgress);
            this.governmentReformProgressField.setValue(this.t.getGovernmentReformProgress());
            items.add(this.governmentReformProgressField);

            this.governmentReformsField.setAll(this.t.getGovernment().getReforms());
            this.governmentReformsButton.getButton().setOnAction(event -> {
                GovernmentReformsDialog dialog = new GovernmentReformsDialog(this.t, this.governmentReformsField);

                Optional<List<GovernmentReform>> reforms = dialog.showAndWait();

                reforms.ifPresent(this.governmentReformsField::setAll);
            });
            items.add(this.governmentReformsButton);

            //Court
            this.courtPropertySheet.getItems().clear();
            this.monarchPropertySheet = null;

            if (this.t.getMonarch() != null) {
                MonarchPropertySheet sheet = new MonarchPropertySheet(this.t, this.t.getMonarch(),
                                                                      this.t.getSave()
                                                                            .getGame()
                                                                            .getLocalisationClean("CURRENT_MONARCH",
                                                                                                  Eu4Language.getByLocale(Constants.LOCALE)),
                                                                      this.cultures, this.religions);

                if (!sheet.getPropertySheet().getItems().isEmpty()) {
                    this.monarchPropertySheet = sheet;
                    this.courtPropertySheet.getItems().addAll(sheet.getPropertySheet().getItems());
                }
            }

            this.heirPropertySheet = null;

            if (this.t.getHeir() != null) {
                MonarchPropertySheet sheet = new MonarchPropertySheet(this.t, this.t.getHeir(),
                                                                      this.t.getSave()
                                                                            .getGame()
                                                                            .getLocalisationClean("HEIR", Eu4Language.getByLocale(Constants.LOCALE)),
                                                                      this.cultures, this.religions);

                if (!sheet.getPropertySheet().getItems().isEmpty()) {
                    this.heirPropertySheet = sheet;
                    this.courtPropertySheet.getItems().addAll(sheet.getPropertySheet().getItems());
                }
            }

            this.queenPropertySheet = null;

            if (this.t.getQueen() != null) {
                MonarchPropertySheet sheet = new MonarchPropertySheet(this.t, this.t.getQueen(),
                                                                      this.t.getSave()
                                                                            .getGame()
                                                                            .getLocalisationClean("CONSORT",
                                                                                                  Eu4Language.getByLocale(Constants.LOCALE)),
                                                                      this.cultures, this.religions);

                if (!sheet.getPropertySheet().getItems().isEmpty()) {
                    this.queenPropertySheet = sheet;
                    this.courtPropertySheet.getItems().addAll(sheet.getPropertySheet().getItems());
                }
            }

            if (!this.courtPropertySheet.getItems().isEmpty()) {
                items.add(new PropertySheetItem(
                        this.t.getSave().getGame().getLocalisationClean("CATEGORY_COURT", Eu4Language.getByLocale(Constants.LOCALE)),
                        this.courtPropertySheet));
            }

            //Diplomacy
            this.overlordField.setValues(FXCollections.observableArrayList(this.t.getOverlord()));
            this.overlordField.setValue(this.t.getOverlord());
            items.add(this.overlordField);

            this.countrySubjectsField = this.t.getSubjects()
                                              .stream()
                                              .map(CountrySubject::new)
                                              .collect(Collectors.toMap(CountrySubject::getSubject, Function.identity()));
            this.countrySubjectsButton.getButton().setOnAction(event -> {
                TableViewDialog<CountrySubject> dialog =
                        new TableViewDialog<>(this.t.getSave(),
                                              new TableView2CountrySubject(this.t,
                                                                           this.countrySubjectsField.values()
                                                                                                    .stream()
                                                                                                    .map(CountrySubject::new)
                                                                                                    .collect(Collectors.toCollection(
                                                                                                            FXCollections::observableArrayList)),
                                                                           this.countriesAlive,
                                                                           this.subjectTypes),
                                              this.t.getSave()
                                                    .getGame()
                                                    .getLocalisationClean("HEADER_SUBJECTS", Eu4Language.getByLocale(Constants.LOCALE)),
                                              list -> new CountrySubject(this.t,
                                                                         this.countriesAlive.get(0),
                                                                         this.subjectTypes.get(0),
                                                                         this.t.getSave().getDate()),
                                              () -> this.t.getSubjects()
                                                          .stream()
                                                          .map(CountrySubject::new)
                                                          .collect(Collectors.toList()));
                Optional<List<CountrySubject>> countrySubjects = dialog.showAndWait();

                countrySubjects.ifPresent(list -> this.countrySubjectsField = list.stream()
                                                                                  .map(CountrySubject::new)
                                                                                  .collect(Collectors.toMap(CountrySubject::getSubject,
                                                                                                            Function.identity())));
            });
            items.add(this.countrySubjectsButton);

            this.rivals.setAll(this.t.getRivals().values().stream().map(rival -> new Rival(this.t, rival)).collect(Collectors.toList()));
            this.rivalsButton.getButton().setOnAction(event -> {
                TableView2Rival tableView2Rival = new TableView2Rival(this.t, this.rivals, this.countriesAlive);
                TableViewDialog<Rival> dialog = new TableViewDialog<>(this.t.getSave(),
                                                                      tableView2Rival,
                                                                      this.t.getSave()
                                                                            .getGame()
                                                                            .getLocalisationClean("RIVALS",
                                                                                                  Eu4Language.getByLocale(Constants.LOCALE)),
                                                                      list -> new Rival(this.t,
                                                                                        this.countriesAlive.stream()
                                                                                                           .filter(c -> list.stream()
                                                                                                                            .noneMatch(r -> c.equals(
                                                                                                                                    r.getTarget())))
                                                                                                           .findFirst()
                                                                                                           .get(),
                                                                                        this.t.getSave().getDate()),

                                                                      () -> this.rivals);
                dialog.setDisableAddProperty(tableView2Rival.disableAddPropertyProperty());
                Optional<List<Rival>> rivalList = dialog.showAndWait();

                rivalList.ifPresent(this.rivals::setAll);
            });
            items.add(this.rivalsButton);

            //Military
            this.manpowerField.setSupplier(() -> this.t.getManpower() * 1000);
            this.manpowerField.setValue(this.t.getManpower() * 1000);
            items.add(this.manpowerField);

            this.sailorsField.setSupplier(this.t::getSailors);
            this.sailorsField.setValue(this.t.getSailors());
            items.add(this.sailorsField);

            this.armyTraditionField.setValue(this.t.getArmyTradition());
            this.armyTraditionField.setSupplier(this.t::getArmyTradition);
            items.add(this.armyTraditionField);

            this.navyTraditionField.setValue(this.t.getNavyTradition());
            this.navyTraditionField.setSupplier(this.t::getNavyTradition);
            items.add(this.navyTraditionField);

            this.armyProfessionalismField.setValue(this.t.getArmyProfessionalism());
            this.armyProfessionalismField.setSupplier(this.t::getArmyProfessionalism);
            items.add(this.armyProfessionalismField);

            this.warEhaustionField.setValue(this.t.getWarExhaustion());
            this.warEhaustionField.setSupplier(this.t::getWarExhaustion);
            items.add(this.warEhaustionField);

            this.leaders.setAll(this.t.getLeaders().values().stream().map(Leader::new).collect(Collectors.toList()));
            this.leadersButton.getButton().setOnAction(event -> {
                TableViewDialog<Leader> dialog = new TableViewDialog<>(this.t.getSave(),
                                                                       new TableView2Leader(this.t, this.leaders),
                                                                       this.t.getSave()
                                                                             .getGame()
                                                                             .getLocalisationClean("HEADER_LEADER",
                                                                                                   Eu4Language.getByLocale(Constants.LOCALE)),
                                                                       list -> new Leader(this.t,
                                                                                          "New one",
                                                                                          LeaderType.GENERAL,
                                                                                          0,
                                                                                          0,
                                                                                          0,
                                                                                          0,
                                                                                          null,
                                                                                          this.t.getSave()
                                                                                                .getDate()
                                                                                                .minusYears(this.t.getSave()
                                                                                                                  .getGame()
                                                                                                                  .getAgeOfAdulthood())),
                                                                       () -> this.leaders);
                Optional<List<Leader>> countrySubjects = dialog.showAndWait();

                countrySubjects.ifPresent(this.leaders::setAll);
            });
            items.add(this.leadersButton);

            //Estates
            this.estatePropertySheets.clear();
            this.estatesPropertySheet.getItems().clear();
            this.t.getEstates()
                  .stream()
                  .sorted(Comparator.comparing(e -> OsaSaveEditorUtils.localize(e.getEstateGame().getName(), this.t.getSave().getGame()),
                                               Eu4Utils.COLLATOR))
                  .forEach(estate -> {
                      EstatePropertySheet estatePropertySheet = new EstatePropertySheet(this.t, estate);

                      if (!estatePropertySheet.getPropertySheet().getItems().isEmpty()) {
                          this.estatePropertySheets.add(estatePropertySheet);
                          this.estatesPropertySheet.getItems().addAll(estatePropertySheet.getPropertySheet().getItems());
                      }
                  });
            this.estatePropertySheets.forEach(sheet -> this.estatePropertySheets.stream()
                                                                                .filter(otherSheet -> !otherSheet.equals(sheet))
                                                                                .forEach(otherSheet -> sheet.getCountryEstatesTerritory()
                                                                                                            .add(otherSheet.territoryValue())));

            if (!this.estatesPropertySheet.getItems().isEmpty()) {
                items.add(new PropertySheetItem(
                        this.t.getSave().getGame().getLocalisationClean("HEADER_ESTATES", Eu4Language.getByLocale(Constants.LOCALE)),
                        this.estatesPropertySheet));
            }

            //Technology
            this.admTechField.setSupplier(() -> this.t.getTech().getAdm());
            this.admTechField.setValue(this.t.getTech().getAdm());
            items.add(this.admTechField);

            this.dipTechField.setSupplier(() -> this.t.getTech().getDip());
            this.dipTechField.setValue(this.t.getTech().getDip());
            items.add(this.dipTechField);

            this.milTechField.setSupplier(() -> this.t.getTech().getMil());
            this.milTechField.setValue(this.t.getTech().getMil());
            items.add(this.milTechField);

            this.innovativenessField.setValue(this.t.getInnovativeness());
            this.innovativenessField.setSupplier(this.t::getInnovativeness);
            items.add(this.innovativenessField);

            this.ideas.setAll(this.t.getIdeaGroups().getIdeaGroups().entrySet().stream().map(Idea::new).collect(Collectors.toList()));
            this.ideasButton.getButton().setOnAction(event -> {
                TableView2Ideas tableView2Ideas = new TableView2Ideas(this.t, this.ideas, this.t.getSave()
                                                                                                .getGame()
                                                                                                .getIdeaGroups()
                                                                                                .stream()
                                                                                                .collect(Collectors.toCollection(
                                                                                                        FXCollections::observableArrayList)));
                TableViewDialog<Idea> dialog =
                        new TableViewDialog<>(this.t.getSave(),
                                              tableView2Ideas,
                                              this.t.getSave()
                                                    .getGame()
                                                    .getLocalisationClean("HEADER_IDEAS", Eu4Language.getByLocale(Constants.LOCALE)),
                                              list -> new Idea(this.t.getSave()
                                                                     .getGame()
                                                                     .getIdeaGroups()
                                                                     .stream()
                                                                     .filter(ideaGroup -> list.stream()
                                                                                              .noneMatch(i -> i.getIdeaGroup().equals(ideaGroup)))
                                                                     .filter(ideaGroup -> list.isEmpty() == ideaGroup.isFree())
                                                                     .findFirst()
                                                                     .get(),
                                                               0),
                                              () -> this.ideas);
                dialog.setDisableAddProperty(tableView2Ideas.disableAddPropertyProperty());
                Optional<List<Idea>> ideas = dialog.showAndWait();

                ideas.ifPresent(this.ideas::setAll);
            });
            items.add(this.ideasButton);

            this.availableAdmPolicies = this.t.getSave()
                                              .getGame()
                                              .getPolicies()
                                              .stream()
                                              .filter(policy -> Power.ADM.equals(policy.getCategory()))
                                              .filter(policy -> policy.getAllow().apply(this.t, this.t))
                                              .collect(Collectors.toCollection(FXCollections::observableArrayList));

            if (!availableAdmPolicies.isEmpty()) {
                this.admPolicies.setAll(this.t.getActivePolicies()
                                              .stream()
                                              .filter(policy -> Power.ADM.equals(policy.getPolicy().getCategory()))
                                              .map(ActivePolicy::new)
                                              .toArray(ActivePolicy[]::new));
                this.admPoliciesButton.getButton().setOnAction(event -> {
                    TableView2Policy tableView2Policy = new TableView2Policy(this.t, this.admPolicies, availableAdmPolicies, "POSSIBLE_ADM_POLICY");
                    TableViewDialog<ActivePolicy> dialog =
                            new TableViewDialog<>(this.t.getSave(),
                                                  tableView2Policy,
                                                  this.t.getSave()
                                                        .getGame()
                                                        .getLocalisationClean("POLICYVIEW_ADMINISTRATIVE", Eu4Language.getByLocale(Constants.LOCALE)),
                                                  list -> new ActivePolicy(availableAdmPolicies.stream()
                                                                                               .filter(policy -> list.stream()
                                                                                                                     .noneMatch(
                                                                                                                             activePolicy -> activePolicy.getPolicy()
                                                                                                                                                         .equals(policy)))
                                                                                               .findFirst()
                                                                                               .get(),
                                                                           this.t.getSave().getDate()),
                                                  () -> this.admPolicies);
                    dialog.setDisableAddProperty(tableView2Policy.disableAddPropertyProperty());
                    Optional<List<ActivePolicy>> policyList = dialog.showAndWait();

                    policyList.ifPresent(this.admPolicies::setAll);
                });
                items.add(this.admPoliciesButton);
            }

            this.availableDipPolicies = this.t.getSave()
                                              .getGame()
                                              .getPolicies()
                                              .stream()
                                              .filter(policy -> Power.DIP.equals(policy.getCategory()))
                                              .filter(policy -> policy.getAllow().apply(this.t, this.t))
                                              .collect(Collectors.toCollection(FXCollections::observableArrayList));

            if (!availableDipPolicies.isEmpty()) {
                this.dipPolicies.setAll(this.t.getActivePolicies()
                                              .stream()
                                              .filter(policy -> Power.DIP.equals(policy.getPolicy().getCategory()))
                                              .map(ActivePolicy::new)
                                              .toArray(ActivePolicy[]::new));
                this.dipPoliciesButton.getButton().setOnAction(event -> {
                    TableView2Policy tableView2Policy = new TableView2Policy(this.t, this.dipPolicies, availableDipPolicies, "POSSIBLE_DIP_POLICY");
                    TableViewDialog<ActivePolicy> dialog =
                            new TableViewDialog<>(this.t.getSave(),
                                                  tableView2Policy,
                                                  this.t.getSave()
                                                        .getGame()
                                                        .getLocalisationClean("POLICYVIEW_DIPLOMATIC", Eu4Language.getByLocale(Constants.LOCALE)),
                                                  list -> new ActivePolicy(availableDipPolicies.stream()
                                                                                               .filter(policy -> list.stream()
                                                                                                                     .noneMatch(
                                                                                                                             activePolicy -> activePolicy.getPolicy()
                                                                                                                                                         .equals(policy)))
                                                                                               .findFirst()
                                                                                               .get(),
                                                                           this.t.getSave().getDate()),
                                                  () -> this.dipPolicies);
                    dialog.setDisableAddProperty(tableView2Policy.disableAddPropertyProperty());
                    Optional<List<ActivePolicy>> policyList = dialog.showAndWait();

                    policyList.ifPresent(this.dipPolicies::setAll);
                });
                items.add(this.dipPoliciesButton);
            }

            this.availableMilPolicies = this.t.getSave()
                                              .getGame()
                                              .getPolicies()
                                              .stream()
                                              .filter(policy -> Power.MIL.equals(policy.getCategory()))
                                              .filter(policy -> policy.getAllow().apply(this.t, this.t))
                                              .collect(Collectors.toCollection(FXCollections::observableArrayList));

            if (!availableMilPolicies.isEmpty()) {
                this.milPolicies.setAll(this.t.getActivePolicies()
                                              .stream()
                                              .filter(policy -> Power.MIL.equals(policy.getPolicy().getCategory()))
                                              .map(ActivePolicy::new)
                                              .toArray(ActivePolicy[]::new));
                this.milPoliciesButton.getButton().setOnAction(event -> {
                    TableView2Policy tableView2Policy = new TableView2Policy(this.t, this.milPolicies, availableMilPolicies, "POSSIBLE_MIL_POLICY");
                    TableViewDialog<ActivePolicy> dialog =
                            new TableViewDialog<>(this.t.getSave(),
                                                  tableView2Policy,
                                                  this.t.getSave()
                                                        .getGame()
                                                        .getLocalisationClean("POLICYVIEW_MILITARY", Eu4Language.getByLocale(Constants.LOCALE)),
                                                  list -> new ActivePolicy(availableMilPolicies.stream()
                                                                                               .filter(policy -> list.stream()
                                                                                                                     .noneMatch(
                                                                                                                             activePolicy -> activePolicy.getPolicy()
                                                                                                                                                         .equals(policy)))
                                                                                               .findFirst()
                                                                                               .get(),
                                                                           this.t.getSave().getDate()),
                                                  () -> this.milPolicies);
                    dialog.setDisableAddProperty(tableView2Policy.disableAddPropertyProperty());
                    Optional<List<ActivePolicy>> policyList = dialog.showAndWait();

                    policyList.ifPresent(this.milPolicies::setAll);
                });
                items.add(this.milPoliciesButton);
            }

            //Modifiers
            this.modifiers.setAll(this.t.getModifiers().stream().map(Modifier::new).collect(Collectors.toList()));
            this.modifiersButton.getButton().setOnAction(event -> {
                TableView2Modifier tableView2Modifier = new TableView2Modifier(this.t.getSave(), this.modifiers);
                TableViewDialog<Modifier> dialog = new TableViewDialog<>(this.t.getSave(),
                                                                         tableView2Modifier,
                                                                         this.t.getSave()
                                                                               .getGame()
                                                                               .getLocalisationClean("DOMESTIC_MODIFIERS",
                                                                                                     Eu4Language.getByLocale(Constants.LOCALE)),
                                                                         list -> null,
                                                                         () -> this.modifiers);
                dialog.setDisableAddProperty(new SimpleBooleanProperty(true));
                Optional<List<Modifier>> modifierList = dialog.showAndWait();

                modifierList.ifPresent(this.modifiers::setAll);
            });
            items.add(this.modifiersButton);

            //Flags
            if (this.t.getFlags() != null) {
                this.flags.setAll(this.t.getFlags()
                                        .getAll()
                                        .entrySet()
                                        .stream()
                                        .map(StringDate::new)
                                        .sorted(Comparator.comparing(StringDate::getDate))
                                        .collect(Collectors.toList()));
                this.flagsButton.getButton().setOnAction(event -> {
                    TableView2StringDate tableView2Flag = new TableView2StringDate(this.t.getSave(), this.flags, false, null, null);
                    TableViewDialog<StringDate> dialog = new TableViewDialog<>(this.t.getSave(),
                                                                               tableView2Flag,
                                                                               this.messageSource.getMessage("ose.category.flags", null, Constants.LOCALE),
                                                                               list -> null,
                                                                               () -> this.flags);
                    dialog.setDisableAddProperty(new SimpleBooleanProperty(true));
                    Optional<List<StringDate>> flagList = dialog.showAndWait();

                    flagList.ifPresent(this.flags::setAll);
                });
                items.add(this.flagsButton);
            } else {
                this.flags.clear();
            }

            if (this.t.getHiddenFlags() != null) {
                this.hiddenFlags.setAll(this.t.getHiddenFlags()
                                              .getAll()
                                              .entrySet()
                                              .stream()
                                              .map(StringDate::new)
                                              .sorted(Comparator.comparing(StringDate::getDate))
                                              .collect(Collectors.toList()));
                this.hiddenFlagsButton.getButton().setOnAction(event -> {
                    TableView2StringDate tableView2HiddenFlag = new TableView2StringDate(this.t.getSave(), this.hiddenFlags, false, null, null);
                    TableViewDialog<StringDate> dialog = new TableViewDialog<>(this.t.getSave(),
                                                                               tableView2HiddenFlag,
                                                                               this.messageSource.getMessage("ose.category.hidden-flags", null,
                                                                                                             Constants.LOCALE),
                                                                               list -> null,
                                                                               () -> this.hiddenFlags,
                                                                               new SimpleBooleanProperty(true));
                    Optional<List<StringDate>> hiddenFlagList = dialog.showAndWait();

                    hiddenFlagList.ifPresent(this.hiddenFlags::setAll);
                });
                items.add(this.hiddenFlagsButton);
            } else {
                this.hiddenFlags.clear();
            }

            //Other
            this.removeAe.setValue(false);
            items.add(this.removeAe);

            this.removeAnnexPartHre.setValue(false);
            items.add(this.removeAnnexPartHre);

            if (estateExpandedPaneName != null) {
                this.estatesPropertySheetSkin.getAccordion()
                                             .getPanes()
                                             .stream()
                                             .filter(titledPane -> titledPane.getText().equals(estateExpandedPaneName))
                                             .findFirst()
                                             .ifPresent(titledPane -> this.estatesPropertySheetSkin.getAccordion().setExpandedPane(titledPane));
            }

            if (courtExpandedPaneName != null) {
                this.courtPropertySheetSkin.getAccordion()
                                           .getPanes()
                                           .stream()
                                           .filter(titledPane -> titledPane.getText().equals(courtExpandedPaneName))
                                           .findFirst()
                                           .ifPresent(titledPane -> this.courtPropertySheetSkin.getAccordion().setExpandedPane(titledPane));
            }

            return items;
        }
    }

    public void internalValidate() {
        if (this.nameField.isVisible().get() && !Objects.equals(ClausewitzUtils.removeQuotes(this.t.getLocalizedName()), this.nameField.getValue())) {
            this.t.setLocalizedName(this.nameField.getValue());
        }

        if (this.wasPlayerField.isVisible().get() && !Objects.equals(this.t.wasPlayer(), this.wasPlayerField.isSelected())) {
            this.t.setWasPlayer(this.wasPlayerField.isSelected());
        }

        if (this.treasuryField.isVisible().get() && !Objects.equals(this.t.getTreasury(), this.treasuryField.getValue())) {
            this.t.setTreasury(this.treasuryField.getValue());
        }

        if (this.corruptionField.isVisible().get() && !Objects.equals(this.t.getCorruption(), this.corruptionField.getDoubleValue())) {
            this.t.setCorruption(this.corruptionField.getDoubleValue());
        }

        if (this.inflationField.isVisible().get() && !Objects.equals(this.t.getInflation(), this.inflationField.getValue())) {
            this.t.setInflation(this.inflationField.getValue());
        }

        if (this.mercantilismField.isVisible().get() && !Objects.equals(this.t.getMercantilism(), this.mercantilismField.getValue())) {
            this.t.setMercantilism(this.mercantilismField.getValue());
        }

        if (this.loansButton.isVisible().get() &&
            (CollectionUtils.size(this.t.getLoans()) != this.loans.size() || this.loans.stream().anyMatch(Loan::isChanged))) {
            this.t.getLoans().forEach(loan -> this.loans.stream()
                                                        .filter(l -> loan.getId().getId().equals(l.getId()))
                                                        .findFirst()
                                                        .ifPresentOrElse(l -> {
                                                                             if (l.getAmount() != loan.getAmount()) {
                                                                                 loan.setAmount(l.getAmount());
                                                                             }

                                                                             if (l.getInterest() != loan.getInterest()) {
                                                                                 loan.setInterest(l.getInterest());
                                                                             }

                                                                             if (!Objects.equals(l.getExpiryDate(), loan.getExpiryDate())) {
                                                                                 loan.setExpiryDate(l.getExpiryDate());
                                                                             }
                                                                             this.loans.remove(l);
                                                                         },
                                                                         () -> this.t.removeLoan(loan.getId().getId())));
            this.loans.forEach(l -> this.t.addLoan(l.getInterest(), l.getAmount(), l.getExpiryDate()));
        }

        if (this.governmentRankField.isVisible().get() && !Objects.equals(this.t.getGovernmentLevel(), this.governmentRankField.getValue())) {
            this.t.setGovernmentRank(this.governmentRankField.getValue());
        }

        if (this.governmentReformsButton.isVisible().get() && !Objects.equals(this.t.getGovernment().getReforms(), this.governmentReformsField)) {
            this.t.getGovernment().setReforms(this.governmentReformsField);
        }

        if (this.admPointField.isVisible().get() && !Objects.equals(this.t.getPowers().get(Power.ADM), this.admPointField.getValue())) {
            this.t.setPower(Power.ADM, this.admPointField.getValue());
        }

        if (this.dipPointField.isVisible().get() && !Objects.equals(this.t.getPowers().get(Power.DIP), this.dipPointField.getValue())) {
            this.t.setPower(Power.DIP, this.dipPointField.getValue());
        }

        if (this.milPointField.isVisible().get() && !Objects.equals(this.t.getPowers().get(Power.MIL), this.milPointField.getValue())) {
            this.t.setPower(Power.MIL, this.milPointField.getValue());
        }

        if (this.stabilityField.isVisible().get() && !Objects.equals(this.t.getStability(), this.stabilityField.getValue())) {
            this.t.setStability(this.stabilityField.getValue());
        }

        if (this.prestigeField.isVisible().get() && !Objects.equals(this.t.getPrestige(), this.prestigeField.getValue())) {
            this.t.setPrestige(this.prestigeField.getValue());
        }

        if (this.absolutismField.isVisible().get() && MapUtils.isNotEmpty(this.t.getSave().getCurrentAge().getAbsolutism())) {
            if (!Objects.equals(this.t.getAbsolutism(), this.absolutismField.getValue())) {
                this.t.setAbsolutism(this.absolutismField.getValue());
            }
        }

        if (this.capitalField.isVisible().get() && !Objects.deepEquals(this.t.getCapital(), this.capitalField.getValue())) {
            this.t.setCapital(this.capitalField.getValue());
        }

        if (!Objects.deepEquals(OsaSaveEditorUtils.countryToMapColor(this.t), this.mapColorField.getValue())) {
            this.t.getColors().setMapColor((int) (this.mapColorField.getValue().getRed() * 255),
                                           (int) (this.mapColorField.getValue().getGreen() * 255),
                                           (int) (this.mapColorField.getValue().getBlue() * 255));
            this.colorChanged.set(true);
        }

        if (this.capitalField.isVisible().get() && !Objects.deepEquals(this.t.getPrimaryCulture(), this.cultureField.getValue())) {
            this.t.setPrimaryCulture(this.cultureField.getValue());
        }

        if (this.acceptedCulturesField.isVisible().get() && !CollectionUtils.isEqualCollection(this.t.getAcceptedCultures(),
                                                                                               this.acceptedCulturesField.getValue())) {
            this.t.setAcceptedCulture(new ArrayList<>(this.acceptedCulturesField.getValue()));
        }

        if (this.religionField.isVisible().get() && !Objects.deepEquals(this.t.getReligion(), this.religionField.getValue())) {
            this.t.setReligion(this.religionField.getValue());
        }

        if (this.authorityField.isVisible().get() && this.t.getReligion() != null && BooleanUtils.isTrue(
                this.t.getReligion().getGameReligion().isUseAuthority())) {
            if (!Objects.equals(this.t.getAuthority(), this.authorityField.getDoubleValue())) {
                this.t.setAuthority(this.authorityField.getDoubleValue());
            }
        }

        if (this.churchPowerField.isVisible().get() && this.t.getReligion() != null
            && (this.t.getReligion().getGameReligion().usesAnglicanPower() || this.t.getReligion().getGameReligion().usesHussitePower()
                || this.t.getReligion().getGameReligion().usesChurchPower())) {
            if (!Objects.equals(this.t.getChurch().getPower(), this.churchPowerField.getDoubleValue())) {
                this.t.getChurch().setPower(this.churchPowerField.getDoubleValue());
            }
        }

        if (this.religiousReformsButton.isVisible().get() && this.t.getReligion() != null && this.t.getReligion().getGameReligion().useReligiousReforms()) {
            if (!Objects.deepEquals(this.t.getReligiousReforms().getAdoptedReforms(), this.passedReligiousReforms)) {
                this.t.getReligiousReforms()
                      .getAdoptedReforms()
                      .forEach(reform -> this.passedReligiousReforms.stream()
                                                                    .filter(reform::equals)
                                                                    .findFirst()
                                                                    .ifPresentOrElse(this.passedReligiousReforms::remove,
                                                                                     () -> this.t.getReligiousReforms()
                                                                                                 .removeAdoptedReform(reform)));
                this.passedReligiousReforms.forEach(reform -> this.t.getReligiousReforms().addAdoptedReform(reform));
            }
        }

        if (this.fervorField.isVisible().get() && this.t.getReligion() != null && this.t.getReligion().getGameReligion().useFervor()) {
            if (!Objects.equals(this.t.getFervor().getValue(), this.fervorField.getDoubleValue())) {
                this.t.getFervor().setValue(this.fervorField.getDoubleValue());
            }
        }

        if (this.patriarchAuthorityField.isVisible().get() && this.t.getReligion() != null && this.t.getReligion().getGameReligion().hasPatriarchs()) {
            if (!Objects.equals(this.t.getPatriarchAuthority(), this.patriarchAuthorityField.getDoubleValue())) {
                this.t.setPatriarchAuthority(this.patriarchAuthorityField.getDoubleValue());
            }
        }

        if (this.fetishistCultField.isVisible().get() && this.t.getReligion() != null && this.t.getReligion().getGameReligion().useFetishistCult()) {
            if (!Objects.deepEquals(this.t.getFetishistCult(), this.fetishistCultField.getValue())) {
                this.t.setFetishistCult(this.fetishistCultField.getValue());
            }
        }

        if (this.isolationismField.isVisible().get() && this.t.getReligion() != null && this.t.getReligion().getGameReligion().usesIsolationism()) {
            if (!Objects.equals(this.t.getIsolationismLevel(), this.isolationismField.getValue())) {
                this.t.setIsolationismLevel(this.isolationismField.getValue());
            }
        }

        if (this.karmaField.isVisible().get() && this.t.getReligion() != null && this.t.getReligion().getGameReligion().usesKarma()) {
            if (!Objects.equals(this.t.getKarma(), this.karmaField.getValue())) {
                this.t.setKarma(this.karmaField.getValue());
            }
        }

        if (this.pietyField.isVisible().get() && this.t.getReligion() != null && this.t.getReligion().getGameReligion().usesPiety()) {
            if (!Objects.equals(this.t.getPiety(), this.pietyField.getDoubleValue())) {
                this.t.setPiety(this.pietyField.getDoubleValue());
            }
        }

        if (this.harmonyField.isVisible().get() && this.t.getReligion() != null && this.t.getReligion().getGameReligion().usesHarmony()) {
            if (!Objects.equals(this.t.getHarmony(), this.harmonyField.getDoubleValue())) {
                this.t.setHarmony(this.harmonyField.getDoubleValue());
            }

            if (!Objects.deepEquals(this.t.getHarmonizedReligionGroups(), this.harmonizedReligionGroups)) {
                if (CollectionUtils.isNotEmpty(this.t.getHarmonizedReligionGroups())) {
                    this.t.getHarmonizedReligionGroups()
                          .forEach(group -> this.harmonizedReligionGroups.stream()
                                                                         .filter(group::equals)
                                                                         .findFirst()
                                                                         .ifPresentOrElse(this.harmonizedReligionGroups::remove,
                                                                                          () -> this.t.removeHarmonizedReligionGroup(group)));
                }

                this.harmonizedReligionGroups.forEach(religionGroup -> this.t.addHarmonizedReligionGroup(religionGroup));
            }

            if (!Objects.deepEquals(this.t.getHarmonizedReligions(), this.harmonizedReligions)) {
                if (CollectionUtils.isNotEmpty(this.t.getHarmonizedReligions())) {
                    this.t.getHarmonizedReligions()
                          .stream()
                          .map(SaveReligion::getGameReligion)
                          .forEach(religion -> this.harmonizedReligions.stream()
                                                                       .filter(religion::equals)
                                                                       .findFirst()
                                                                       .ifPresentOrElse(this.harmonizedReligions::remove,
                                                                                        () -> this.t.removeHarmonizedReligion(religion)));
                }

                this.harmonizedReligions.forEach(religion -> this.t.addHarmonizedReligion(religion));
            }
        }

        if (this.personalDeityField.isVisible().get() && this.t.getReligion() != null && this.t.getReligion().getGameReligion().usePersonalDeity()) {
            if (!Objects.deepEquals(this.t.getPersonalDeity(), this.personalDeityField.getValue())) {
                this.t.setPersonalDeity(this.personalDeityField.getValue());
            }
        }

        if (this.doomField.isVisible().get() && this.t.getReligion() != null && this.t.getReligion().getGameReligion().useDoom()) {
            if (!Objects.equals(this.t.getDoom(), this.doomField.getDoubleValue())) {
                this.t.setDoom(this.doomField.getDoubleValue());
            }
        }

        if (this.secondaryReligionsField.isVisible().get() && this.t.getReligion() != null &&
            this.t.getReligion().getGameReligion().canHaveSecondaryReligion()) {
            if (!Objects.deepEquals(this.t.getSecondaryReligion(), this.secondaryReligionsField.getValue())) {
                this.t.setSecondaryReligion(this.secondaryReligionsField.getValue());
            }
        }

        if (this.governmentReformProgressField.isVisible().get() &&
            !Objects.equals(this.t.getGovernmentReformProgress(), this.governmentReformProgressField.getValue())) {
            this.t.setGovernmentReformProgress(this.governmentReformProgressField.getValue());
        }

        for (int i = 0; i < this.t.getSave().getInstitutions().getNbInstitutions(); i++) {
            if (this.t.getEmbracedInstitution(i) != this.institutionsEmbracedFields.get(i).isSelected()) {
                this.t.embracedInstitution(i, this.institutionsEmbracedFields.get(i).isSelected());
            }
        }

        if (this.manpowerField.isVisible().get() && !Objects.equals(this.t.getManpower() * 1000, this.manpowerField.getValue())) {
            this.t.setManpower(this.manpowerField.getValue() / 1000);
        }

        if (this.sailorsField.isVisible().get() && !Objects.equals(this.t.getSailors(), this.sailorsField.getValue())) {
            this.t.setSailors(this.sailorsField.getValue());
        }

        if (this.armyTraditionField.isVisible().get() && !Objects.equals(this.t.getArmyTradition(), this.armyTraditionField.getDoubleValue())) {
            this.t.setArmyTradition(this.armyTraditionField.getDoubleValue());
        }

        if (this.navyTraditionField.isVisible().get() && !Objects.equals(this.t.getNavyTradition(), this.navyTraditionField.getDoubleValue())) {
            this.t.setNavyTradition(this.navyTraditionField.getDoubleValue());
        }

        if (this.armyProfessionalismField.isVisible().get() &&
            !Objects.equals(this.t.getArmyProfessionalism(), this.armyProfessionalismField.getDoubleValue())) {
            this.t.setArmyProfessionalism(this.armyProfessionalismField.getDoubleValue());
        }

        if (this.warEhaustionField.isVisible().get() && !Objects.equals(this.t.getWarExhaustion(), this.warEhaustionField.getDoubleValue())) {
            this.t.setWarExhaustion(this.warEhaustionField.getDoubleValue());
        }

        if (this.leadersButton.isVisible().get() && this.t.getLeaders().size() != this.leaders.size() || this.leaders.stream().anyMatch(Leader::isChanged)) {
            this.t.getLeaders()
                  .values()
                  .forEach(leader -> this.leaders.stream()
                                                 .filter(l -> leader.getId().getId().equals(l.getId()))
                                                 .findFirst()
                                                 .ifPresentOrElse(l -> {
                                                                      if (!Objects.equals(l.getBirthDate(), leader.getBirthDate())) {
                                                                          leader.setBirthDate(l.getBirthDate());
                                                                      }

                                                                      if (!Objects.equals(l.getName(), leader.getName())) {
                                                                          leader.setName(l.getName());
                                                                      }

                                                                      if (!Objects.equals(l.getType(), leader.getType())) {
                                                                          leader.setType(l.getType());
                                                                      }

                                                                      if (!Objects.equals(l.getManuever(), leader.getManuever())) {
                                                                          leader.setManuever(l.getManuever());
                                                                      }

                                                                      if (!Objects.equals(l.getFire(), leader.getFire())) {
                                                                          leader.setFire(l.getFire());
                                                                      }

                                                                      if (!Objects.equals(l.getShock(), leader.getShock())) {
                                                                          leader.setShock(l.getShock());
                                                                      }

                                                                      if (!Objects.equals(l.getSiege(), leader.getSiege())) {
                                                                          leader.setSiege(l.getSiege());
                                                                      }

                                                                      if (!Objects.equals(l.getPersonality(), leader.getPersonality())) {
                                                                          leader.setPersonality(l.getPersonality());
                                                                      }

                                                                      this.leaders.remove(l);
                                                                  },
                                                                  () -> this.t.removeLeader(leader.getId().getId())));
            this.leaders.forEach(l -> this.t.addLeader(this.t.getSave().getDate(), l.getBirthDate(), l.getName(), l.getType(), l.getManuever(),
                                                       l.getFire(), l.getShock(), l.getSiege(), l.getPersonality()));
        }

        Stream.concat(this.t.getSubjects().stream(), this.countrySubjectsField.keySet().stream())
              .distinct()
              .forEach(subject -> this.countrySubjectsField.compute(subject, (c, countrySubject) -> {
                  if (countrySubject == null) {
                      this.t.getSave().getDiplomacy().removeDependency(this.t, subject);
                  } else if (countrySubject.changed()) {
                      subject.setOverlord(countrySubject.getOverlord(), countrySubject.getSubjectType(),
                                          countrySubject.getStartDate());
                  }

                  return countrySubject;
              }));

        if (this.t.getRivals().size() != this.rivals.size() || this.rivals.stream().anyMatch(Rival::isChanged)) {
            this.t.getRivals()
                  .values()
                  .forEach(rival -> this.rivals.stream()
                                               .filter(r -> rival.getRival().equals(r.getTarget()))
                                               .findFirst()
                                               .ifPresentOrElse(r -> {
                                                                    if (!Objects.equals(r.getDate(), rival.getDate())) {
                                                                        rival.setDate(r.getDate());
                                                                    }

                                                                    this.rivals.remove(r);
                                                                },
                                                                () -> this.t.removeRival(rival.getRival())));
            this.rivals.forEach(rival -> this.t.addRival(rival.getTarget(), rival.getDate()));
        }

        this.estatePropertySheets.forEach(EstatePropertySheet::validate);

        if (this.admTechField.isVisible().get() && !Objects.equals(this.t.getTech().getAdm(), this.admTechField.getValue())) {
            this.t.getTech().setAdm(this.admTechField.getValue());
        }

        if (this.dipTechField.isVisible().get() && !Objects.equals(this.t.getTech().getDip(), this.dipTechField.getValue())) {
            this.t.getTech().setDip(this.dipTechField.getValue());
        }

        if (this.milTechField.isVisible().get() && !Objects.equals(this.t.getTech().getMil(), this.milTechField.getValue())) {
            this.t.getTech().setMil(this.milTechField.getValue());
        }

        if (this.innovativenessField.isVisible().get() && !Objects.equals(this.t.getInnovativeness(), this.innovativenessField.getDoubleValue())) {
            this.t.setInnovativeness(this.innovativenessField.getDoubleValue());
        }

        if (this.ideasButton.isVisible().get() &&
            (this.t.getIdeaGroups().getIdeaGroups().size() != this.ideas.size() || this.ideas.stream().anyMatch(Idea::isChanged))) {
            this.t.getIdeaGroups()
                  .getIdeaGroups()
                  .forEach((ideaGroup, integer) -> this.ideas.stream()
                                                             .filter(p -> ideaGroup.equals(p.getIdeaGroup()))
                                                             .findFirst()
                                                             .ifPresentOrElse(p -> {
                                                                                  if (!Objects.equals(p.getLevel(), integer)) {
                                                                                      this.t.getIdeaGroups().setIdeaGroup(ideaGroup, p.getLevel());
                                                                                  }

                                                                                  this.ideas.remove(p);
                                                                              },
                                                                              () -> this.t.getIdeaGroups().removeIdeaGroup(ideaGroup)));
            this.ideas.forEach(idea -> this.t.getIdeaGroups().setIdeaGroup(idea.getIdeaGroup(), idea.getLevel()));
        }

        if (this.monarchPropertySheet != null) {
            this.monarchPropertySheet.validate();
        }

        if (this.heirPropertySheet != null) {
            this.heirPropertySheet.validate();
        }

        if (this.queenPropertySheet != null) {
            this.queenPropertySheet.validate();
        }

        if (this.modifiersButton.isVisible().get() &&
            (this.t.getModifiers().size() != this.modifiers.size() || this.modifiers.stream().anyMatch(Modifier::isChanged))) {
            this.t.getModifiers()
                  .forEach(saveModifier -> this.modifiers.stream()
                                                         .filter(modifier -> saveModifier.getModifier().equals(modifier.getModifier()))
                                                         .findFirst()
                                                         .ifPresentOrElse(modifier -> {
                                                                              if (!Objects.equals(modifier.getDate(), saveModifier.getDate())) {
                                                                                  saveModifier.setDate(modifier.getDate());
                                                                              }

                                                                              this.modifiers.remove(modifier);
                                                                          },
                                                                          () -> this.t.removeModifier(saveModifier.getModifier())));
        }

        if (this.flagsButton.isVisible().get() && (this.t.getFlags() != null && CollectionUtils.isNotEmpty(this.flags))
            || (this.t.getFlags() != null && this.t.getFlags().getAll().size() != this.flags.size())
            || this.flags.stream().anyMatch(StringDate::isChanged)) {
            this.t.getFlags()
                  .getAll()
                  .forEach((s, date) -> this.flags.stream()
                                                  .filter(flag -> s.equals(flag.getName()))
                                                  .findFirst()
                                                  .ifPresentOrElse(flag -> {
                                                                       if (!Objects.equals(flag.getDate(), date)) {
                                                                           this.t.getFlags().set(s, flag.getDate());
                                                                       }

                                                                       this.flags.remove(flag);
                                                                   },
                                                                   () -> this.t.getFlags().remove(s)));
        }

        if (this.hiddenFlagsButton.isVisible().get() && (this.t.getHiddenFlags() != null && CollectionUtils.isNotEmpty(this.hiddenFlags))
            || (this.t.getHiddenFlags() != null && this.t.getHiddenFlags().getAll().size() != this.hiddenFlags.size())
            || this.hiddenFlags.stream().anyMatch(StringDate::isChanged)) {
            this.t.getHiddenFlags()
                  .getAll()
                  .forEach((s, date) -> this.hiddenFlags.stream()
                                                        .filter(hiddenFlag -> s.equals(hiddenFlag.getName()))
                                                        .findFirst()
                                                        .ifPresentOrElse(hiddenFlag -> {
                                                                             if (!Objects.equals(hiddenFlag.getDate(), date)) {
                                                                                 this.t.getHiddenFlags().set(s, hiddenFlag.getDate());
                                                                             }

                                                                             this.hiddenFlags.remove(hiddenFlag);
                                                                         },
                                                                         () -> this.t.getHiddenFlags().remove(s)));
        }

        if (this.admPoliciesButton.isVisible().get() && CollectionUtils.isNotEmpty(this.availableAdmPolicies) &&
            (this.t.getActivePolicies().stream().filter(p -> Power.ADM.equals(p.getPolicy().getCategory())).count() != this.admPolicies.size()
             || this.admPolicies.stream().anyMatch(ActivePolicy::isChanged))) {
            this.t.getActivePolicies()
                  .stream()
                  .filter(p -> Power.ADM.equals(p.getPolicy().getCategory()))
                  .forEach(activePolicy -> this.admPolicies.stream()
                                                           .filter(policy -> activePolicy.getPolicy().equals(policy.getPolicy()))
                                                           .findFirst()
                                                           .ifPresentOrElse(policy -> {
                                                                                if (!Objects.equals(policy.getDate(), activePolicy.getDate())) {
                                                                                    activePolicy.setDate(policy.getDate());
                                                                                }

                                                                                this.admPolicies.remove(policy);
                                                                            },
                                                                            () -> this.t.removeActivePolicy(activePolicy.getPolicy())));
            this.admPolicies.forEach(policy -> this.t.addActivePolicy(policy.getPolicy(), policy.getDate()));
        }

        if (this.dipPoliciesButton.isVisible().get() && CollectionUtils.isNotEmpty(this.availableDipPolicies) &&
            (this.t.getActivePolicies().stream().filter(p -> Power.DIP.equals(p.getPolicy().getCategory())).count() != this.dipPolicies.size()
             || this.dipPolicies.stream().anyMatch(ActivePolicy::isChanged))) {
            this.t.getActivePolicies()
                  .stream()
                  .filter(p -> Power.DIP.equals(p.getPolicy().getCategory()))
                  .forEach(activePolicy -> this.dipPolicies.stream()
                                                           .filter(policy -> activePolicy.getPolicy().equals(policy.getPolicy()))
                                                           .findFirst()
                                                           .ifPresentOrElse(policy -> {
                                                                                if (!Objects.equals(policy.getDate(), activePolicy.getDate())) {
                                                                                    activePolicy.setDate(policy.getDate());
                                                                                }

                                                                                this.dipPolicies.remove(policy);
                                                                            },
                                                                            () -> this.t.removeActivePolicy(activePolicy.getPolicy())));
            this.dipPolicies.forEach(policy -> this.t.addActivePolicy(policy.getPolicy(), policy.getDate()));
        }

        if (this.milPoliciesButton.isVisible().get() && CollectionUtils.isNotEmpty(this.availableMilPolicies) &&
            (this.t.getActivePolicies().stream().filter(p -> Power.MIL.equals(p.getPolicy().getCategory())).count() != this.milPolicies.size()
             || this.milPolicies.stream().anyMatch(ActivePolicy::isChanged))) {
            this.t.getActivePolicies()
                  .stream()
                  .filter(p -> Power.MIL.equals(p.getPolicy().getCategory()))
                  .forEach(activePolicy -> this.milPolicies.stream()
                                                           .filter(policy -> activePolicy.getPolicy().equals(policy.getPolicy()))
                                                           .findFirst()
                                                           .ifPresentOrElse(policy -> {
                                                                                if (!Objects.equals(policy.getDate(), activePolicy.getDate())) {
                                                                                    activePolicy.setDate(policy.getDate());
                                                                                }

                                                                                this.milPolicies.remove(policy);
                                                                            },
                                                                            () -> this.t.removeActivePolicy(activePolicy.getPolicy())));
            this.milPolicies.forEach(policy -> this.t.addActivePolicy(policy.getPolicy(), policy.getDate()));
        }

        if (this.removeAe.isVisible().get() && this.removeAe.isSelected()) {
            this.t.getSave().getCountries().values().forEach(c -> c.removeAeFor(this.t.getTag()));
        }

        if (this.removeAnnexPartHre.isVisible().get() && this.removeAnnexPartHre.isSelected()) { //opinion_annex_part_of_empire
            this.t.getSave().getCountries().values().forEach(c -> c.removeOpinionFor(this.t.getTag(), "\"opinion_annex_part_of_empire\""));
        }
    }

    public final BooleanProperty colorChangedProperty() {
        if (this.colorChanged == null) {
            this.colorChanged = new BooleanPropertyBase() {
                @Override
                public Object getBean() {
                    return CountryPropertySheet.this;
                }

                @Override
                public String getName() {
                    return "colorChanged";
                }
            };
        }

        return colorChanged;
    }

    public ValidationSupport getValidationSupport() {
        return validationSupport;
    }
}
