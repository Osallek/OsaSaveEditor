package fr.osallek.osasaveeditor.controller.mapview;

import fr.osallek.eu4parser.common.Eu4Utils;
import fr.osallek.eu4parser.common.ImageReader;
import fr.osallek.eu4parser.model.game.Event;
import fr.osallek.eu4parser.model.game.Government;
import fr.osallek.eu4parser.model.game.GovernmentReform;
import fr.osallek.eu4parser.model.game.Hegemon;
import fr.osallek.eu4parser.model.game.ImperialReform;
import fr.osallek.eu4parser.model.game.localisation.Eu4Language;
import fr.osallek.eu4parser.model.save.Save;
import fr.osallek.eu4parser.model.save.SaveReligion;
import fr.osallek.eu4parser.model.save.changeprices.ChangePriceGood;
import fr.osallek.eu4parser.model.save.country.SaveCountry;
import fr.osallek.eu4parser.model.save.country.SaveGovernment;
import fr.osallek.eu4parser.model.save.country.SaveHegemon;
import fr.osallek.eu4parser.model.save.empire.HreReligionStatus;
import fr.osallek.eu4parser.model.save.gameplayoptions.CustomNationDifficulty;
import fr.osallek.eu4parser.model.save.gameplayoptions.Difficulty;
import fr.osallek.eu4parser.model.save.province.SaveProvince;
import fr.osallek.osasaveeditor.common.Constants;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import fr.osallek.osasaveeditor.controller.control.ClearableComboBox;
import fr.osallek.osasaveeditor.controller.control.CustomListSelectionView;
import fr.osallek.osasaveeditor.controller.control.ListSelectionViewImperialReform;
import fr.osallek.osasaveeditor.controller.control.RequiredComboBox;
import fr.osallek.osasaveeditor.controller.control.TableView2PriceChange;
import fr.osallek.osasaveeditor.controller.converter.CountryStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.CountryStringConverter;
import fr.osallek.osasaveeditor.controller.converter.CustomNationDifficultyStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.CustomNationDifficultyStringConverter;
import fr.osallek.osasaveeditor.controller.converter.DecreeStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.DecreeStringConverter;
import fr.osallek.osasaveeditor.controller.converter.DifficultyStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.DifficultyStringConverter;
import fr.osallek.osasaveeditor.controller.converter.EventStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.HreReligionStatusStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.HreReligionStatusStringConverter;
import fr.osallek.osasaveeditor.controller.converter.ProvinceStringCellFactory;
import fr.osallek.osasaveeditor.controller.converter.ProvinceStringConverter;
import fr.osallek.osasaveeditor.controller.object.Decree;
import fr.osallek.osasaveeditor.controller.object.PriceChange;
import fr.osallek.osasaveeditor.controller.pane.CustomPropertySheet;
import fr.osallek.osasaveeditor.controller.pane.CustomPropertySheetSkin;
import fr.osallek.osasaveeditor.controller.pane.ListSelectionViewDialog;
import fr.osallek.osasaveeditor.controller.pane.TableViewDialog;
import fr.osallek.osasaveeditor.controller.propertyeditor.CustomPropertyEditorFactory;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ButtonItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.CheckBoxItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableComboBoxItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableSliderItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.HBoxItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.PropertySheetItem;
import fr.osallek.osasaveeditor.controller.validator.CustomGraphicValidationDecoration;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.decoration.CompoundValidationDecoration;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SavePropertySheet extends PropertySheet<Save> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SavePropertySheet.class);

    private final ValidationSupport validationSupport;

    private final ClearableComboBoxItem<Difficulty> difficultyField;

    private final CheckBoxItem allowHotJoinField;

    private final CheckBoxItem allowCoopField;

    private final CheckBoxItem terraIncognitaField;

    private final CheckBoxItem saveEditableField;

    private final CheckBoxItem lockedLedgerField;

    private final CheckBoxItem limitedLedgerField;

    private final CheckBoxItem dynamicProvinceNamesField;

    private final ClearableComboBoxItem<CustomNationDifficulty> customNationDifficultyField;

    private final CheckBoxItem addNationsToGameField;

    private final CheckBoxItem showMonthlyTaxIncomeField;

    private final CheckBoxItem colorWastelandsField;

    private final CheckBoxItem exclavesRegionNameField;

    private final CheckBoxItem blockNationRuiningField;

    private final CheckBoxItem unlimitedIdeaGroupsField;

    private final CheckBoxItem allowNameChangeField;

    private final CheckBoxItem onlyHostCanPauseField;

    private final CheckBoxItem onlyHostAndObserversCanSaveField;

    private final CheckBoxItem allowTeamsField;

    private final CheckBoxItem allowFreeTeamCreationField;

    private final List<CheckBoxItem> institutionAvailableFields;

    private final List<ClearableComboBoxItem<SaveProvince>> institutionOriginFields;

    private final Map<String, List<PriceChange>> goodsChangePrices;

    private ClearableComboBoxItem<SaveCountry> hreEmperor;

    private ObservableList<SaveCountry> hreElectors;

    private ClearableSliderItem hreInfluenceField;

    private ObservableList<ImperialReform> passedHreMainLineReforms;

    private ObservableList<ImperialReform> notPassedHreMainLineReforms;

    private ObservableList<ImperialReform> passedHreLeftBranchReforms;

    private ObservableList<ImperialReform> notPassedHreLeftBranchReforms;

    private ObservableList<ImperialReform> passedHreRightBranchReforms;

    private ObservableList<ImperialReform> notPassedHreRightBranchReforms;

    private CheckBoxItem hreLeaguesActives;

    private ClearableComboBoxItem<HreReligionStatus> hreReligionStatusField;

    private ClearableComboBoxItem<SaveCountry> celestialEmperor;

    private ClearableSliderItem celestialInfluenceField;

    private ObservableList<ImperialReform> passedCelestialReforms;

    private ObservableList<ImperialReform> notPassedCelestialReforms;

    private ClearableComboBoxItem<Decree> decreeField;

    private final CustomPropertySheet religionPropertySheet;

    private final List<ReligionPropertySheet> religionPropertySheets;

    private final ObservableList<Event> firedEvents;

    private final ObservableList<Event> notFiredEvents;

    private final List<ClearableComboBoxItem<SaveCountry>> hegemoniesCountryFields;

    private final List<ClearableSliderItem> hegemoniesProgressesFields;

    public SavePropertySheet(Save save, ObservableList<SaveCountry> countriesAlive, ObservableList<SaveProvince> cities, MessageSource messageSource) {
        super(save, save);

        List<CustomPropertySheet.Item<?>> items = new ArrayList<>();

        this.validationSupport = new ValidationSupport();
        this.validationSupport.setValidationDecorator(
                new CompoundValidationDecoration(new CustomGraphicValidationDecoration(),
                                                 new StyleClassValidationDecoration("validation-error", null)));

        //GAME OPTIONS
        this.difficultyField = new ClearableComboBoxItem<>(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                                           this.save.getGame().getLocalisationClean("FE_DIFFICULTY", Eu4Language.getByLocale(Constants.LOCALE)),
                                                           FXCollections.observableArrayList(Difficulty.values()),
                                                           this.save.getGameplayOptions().getDifficulty(),
                                                           this.save.getGame()
                                                                    .getLocalisationClean("FE_BONUSES_DESC", Eu4Language.getByLocale(Constants.LOCALE)),
                                                           new ClearableComboBox<>(new RequiredComboBox<>()));
        this.difficultyField.setConverter(new DifficultyStringConverter(this.save));
        this.difficultyField.setCellFactory(new DifficultyStringCellFactory(this.save));
        this.difficultyField.setSupplier(() -> this.save.getGameplayOptions().getDifficulty());

        this.allowHotJoinField = new CheckBoxItem(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                                  this.save.getGame().getLocalisationClean("ALLOW_HOTJOIN", Eu4Language.getByLocale(Constants.LOCALE)),
                                                  this.save.getGameplayOptions().getAllowHotjoin(),
                                                  this.save.getGame().getLocalisationClean("FE_HOTJOIN_DESC", Eu4Language.getByLocale(Constants.LOCALE)));

        this.allowCoopField = new CheckBoxItem(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                               this.save.getGame().getLocalisationClean("ALLOW_COOP_MP", Eu4Language.getByLocale(Constants.LOCALE)),
                                               this.save.getGameplayOptions().getAllowCoop(),
                                               this.save.getGame().getLocalisationClean("FE_COOP_DESC", Eu4Language.getByLocale(Constants.LOCALE)));

        this.terraIncognitaField = new CheckBoxItem(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                                    this.save.getGame().getLocalisationClean("FE_USE_TI", Eu4Language.getByLocale(Constants.LOCALE)),
                                                    this.save.getGameplayOptions().getTerraIncognita(),
                                                    this.save.getGame().getLocalisationClean("FE_USETI_DESC", Eu4Language.getByLocale(Constants.LOCALE)));

        this.saveEditableField = new CheckBoxItem(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                                  this.save.getGame().getLocalisationClean("FE_EDIT_SAVE", Eu4Language.getByLocale(Constants.LOCALE)),
                                                  this.save.getGameplayOptions().getSaveEditable(),
                                                  this.save.getGame().getLocalisationClean("FE_EDIT_SAVE_DESC", Eu4Language.getByLocale(Constants.LOCALE)));

        this.lockedLedgerField = new CheckBoxItem(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                                  this.save.getGame().getLocalisationClean("FE_LOCK_LEDGER", Eu4Language.getByLocale(Constants.LOCALE)),
                                                  this.save.getGameplayOptions().getLockedLedger(),
                                                  this.save.getGame().getLocalisationClean("FE_LOCKED_LEDGER_DESC", Eu4Language.getByLocale(Constants.LOCALE)));

        this.limitedLedgerField = new CheckBoxItem(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                                   this.save.getGame().getLocalisationClean("FE_LIMITED_LEDGER", Eu4Language.getByLocale(Constants.LOCALE)),
                                                   this.save.getGameplayOptions().getLimitedLedger(),
                                                   this.save.getGame()
                                                            .getLocalisationClean("FE_LIMITED_LEDGER_DESC", Eu4Language.getByLocale(Constants.LOCALE)));

        this.dynamicProvinceNamesField = new CheckBoxItem(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                                          this.save.getGame()
                                                                   .getLocalisationClean("FE_USE_DYNAMIC_PROVINCE_NAMES",
                                                                                         Eu4Language.getByLocale(Constants.LOCALE)),
                                                          this.save.getGameplayOptions().getDynamicProvinceNames(),
                                                          this.save.getGame()
                                                                   .getLocalisationClean("FE_DYNAMIC_PROVINCE_DESC",
                                                                                         Eu4Language.getByLocale(Constants.LOCALE)));

        this.customNationDifficultyField = new ClearableComboBoxItem<>(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                                                       this.save.getGame()
                                                                                .getLocalisationClean("FE_CUSTOM_NATION_DIFFICULTY",
                                                                                                      Eu4Language.getByLocale(Constants.LOCALE)),
                                                                       FXCollections.observableArrayList(CustomNationDifficulty.values()),
                                                                       this.save.getGameplayOptions().getCustomNationDifficulty(),
                                                                       this.save.getGame()
                                                                                .getLocalisationClean("CN_DIFFICULTY_TOOLTIP",
                                                                                                      Eu4Language.getByLocale(Constants.LOCALE)),
                                                                       new ClearableComboBox<>(new RequiredComboBox<>()));
        this.customNationDifficultyField.setConverter(new CustomNationDifficultyStringConverter(this.save));
        this.customNationDifficultyField.setCellFactory(new CustomNationDifficultyStringCellFactory(this.save));
        this.customNationDifficultyField.setSupplier(() -> this.save.getGameplayOptions().getCustomNationDifficulty());

        this.addNationsToGameField = new CheckBoxItem(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                                      this.save.getGame()
                                                               .getLocalisationClean("FE_CUSTOM_NATION_ADD_TO_SAVE", Eu4Language.getByLocale(Constants.LOCALE)),
                                                      this.save.getGameplayOptions().getAddNationsToGame(),
                                                      this.save.getGame()
                                                               .getLocalisationClean("CN_ADD_TO_SAVE_TOOLTIP1", Eu4Language.getByLocale(Constants.LOCALE)));

        this.showMonthlyTaxIncomeField = new CheckBoxItem(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                                          this.save.getGame()
                                                                   .getLocalisationClean("SHOW_MONTHLY_TAX_INCOME", Eu4Language.getByLocale(Constants.LOCALE)),
                                                          this.save.getGameplayOptions().getShowMonthlyTaxIncome(),
                                                          this.save.getGame()
                                                                   .getLocalisationClean("SHOW_MONTHLY_TAX_INCOME_TOOLTIP",
                                                                                         Eu4Language.getByLocale(Constants.LOCALE)));

        this.colorWastelandsField = new CheckBoxItem(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                                     this.save.getGame().getLocalisationClean("COLOR_WASTELANDS", Eu4Language.getByLocale(Constants.LOCALE)),
                                                     this.save.getGameplayOptions().getColorWastelands(),
                                                     this.save.getGame()
                                                              .getLocalisationClean("COLOR_WASTELANDS_TOOLTIP", Eu4Language.getByLocale(Constants.LOCALE)));

        this.exclavesRegionNameField = new CheckBoxItem(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                                        this.save.getGame().getLocalisationClean("USE_REGION_NAMES", Eu4Language.getByLocale(Constants.LOCALE)),
                                                        this.save.getGameplayOptions().getExclavesRegionName(),
                                                        this.save.getGame()
                                                                 .getLocalisationClean("USE_REGION_NAMES_TOOLTIP", Eu4Language.getByLocale(Constants.LOCALE)));

        this.blockNationRuiningField = new CheckBoxItem(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                                        this.save.getGame()
                                                                 .getLocalisationClean("FE_BLOCK_NATION_RUINING", Eu4Language.getByLocale(Constants.LOCALE)),
                                                        this.save.getGameplayOptions().getBlockNationRuining(),
                                                        this.save.getGame()
                                                                 .getLocalisationClean("FE_BLOCK_NATION_RUINING_DESC",
                                                                                       Eu4Language.getByLocale(Constants.LOCALE)));

        this.unlimitedIdeaGroupsField = new CheckBoxItem(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                                         this.save.getGame()
                                                                  .getLocalisationClean("USE_ANY_IDEAGROUP", Eu4Language.getByLocale(Constants.LOCALE)),
                                                         this.save.getGameplayOptions().getUnlimitedIdeaGroups(),
                                                         this.save.getGame()
                                                                  .getLocalisationClean("FE_NO_LIMITS_ON_IDEAS_DESC",
                                                                                        Eu4Language.getByLocale(Constants.LOCALE)));

        this.allowNameChangeField = new CheckBoxItem(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                                     this.save.getGame()
                                                              .getLocalisationClean("FE_ALLOW_NAME_CHANGE", Eu4Language.getByLocale(Constants.LOCALE)),
                                                     this.save.getGameplayOptions().getAllowNameChange(),
                                                     this.save.getGame()
                                                              .getLocalisationClean("FE_ALLOW_NAME_CHANGE_DESC", Eu4Language.getByLocale(Constants.LOCALE)));

        this.onlyHostCanPauseField = new CheckBoxItem(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                                      this.save.getGame().getLocalisationClean("FE_ONLY_HOST_PAUSE", Eu4Language.getByLocale(Constants.LOCALE)),
                                                      this.save.getGameplayOptions().getOnlyHostCanPause(),
                                                      this.save.getGame()
                                                               .getLocalisationClean("FE_ONLY_HOST_PAUSE_DESC", Eu4Language.getByLocale(Constants.LOCALE)));

        this.onlyHostAndObserversCanSaveField = new CheckBoxItem(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                                                 this.save.getGame()
                                                                          .getLocalisationClean("FE_ONLY_HOST_SAVE", Eu4Language.getByLocale(Constants.LOCALE)),
                                                                 this.save.getGameplayOptions().getOnlyHostAndObserversCanSave(),
                                                                 this.save.getGame()
                                                                          .getLocalisationClean("FE_ONLY_HOST_SAVE_DESC",
                                                                                                Eu4Language.getByLocale(Constants.LOCALE)));

        this.allowTeamsField = new CheckBoxItem(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                                this.save.getGame().getLocalisationClean("FE_USE_TEAMS", Eu4Language.getByLocale(Constants.LOCALE)),
                                                this.save.getGameplayOptions().getAllowTeams(),
                                                this.save.getGame().getLocalisationClean("USE_TEAMS_TOOLTIP", Eu4Language.getByLocale(Constants.LOCALE)));

        this.allowFreeTeamCreationField = new CheckBoxItem(messageSource.getMessage("ose.category.options", null, Constants.LOCALE),
                                                           this.save.getGame()
                                                                    .getLocalisationClean("FE_ALLOW_FREE_TEAM_CREATION",
                                                                                          Eu4Language.getByLocale(Constants.LOCALE)),
                                                           this.save.getGameplayOptions().getAllowFreeTeamCreation(),
                                                           this.save.getGame()
                                                                    .getLocalisationClean("ALLOW_FREE_TEAM_CREATION_TOOLTIP",
                                                                                          Eu4Language.getByLocale(Constants.LOCALE)));
        items.add(this.difficultyField);
        items.add(this.terraIncognitaField);
        items.add(this.dynamicProvinceNamesField);
        items.add(this.showMonthlyTaxIncomeField);
        items.add(this.colorWastelandsField);
        items.add(this.exclavesRegionNameField);
        items.add(this.unlimitedIdeaGroupsField);
        items.add(this.allowHotJoinField);
        items.add(this.allowCoopField);
        items.add(this.onlyHostAndObserversCanSaveField);
        items.add(this.onlyHostCanPauseField);
        items.add(this.saveEditableField);
        items.add(this.lockedLedgerField);
        items.add(this.limitedLedgerField);
        items.add(this.allowTeamsField);
        items.add(this.allowFreeTeamCreationField);
        items.add(this.blockNationRuiningField);
        items.add(this.allowNameChangeField);
        items.add(this.customNationDifficultyField);
        items.add(this.addNationsToGameField);

        //INSTITUTIONS
        this.institutionOriginFields = new ArrayList<>();
        this.institutionAvailableFields = new ArrayList<>();
        for (int i = 0; i < this.save.getInstitutions().getNbInstitutions(); i++) {
            CheckBoxItem checkBoxItem = new CheckBoxItem(messageSource.getMessage("ose.category.institutions", null, Constants.LOCALE),
                                                         OsaSaveEditorUtils.localize(this.save.getGame().getInstitution(i).getName(),
                                                                                     this.save.getGame()),
                                                         this.save.getInstitutions().isAvailable(i));

            int finalI = i;
            ClearableComboBoxItem<SaveProvince> comboBoxItem = new ClearableComboBoxItem<>(
                    messageSource.getMessage("ose.category.institutions", null, Constants.LOCALE),
                    OsaSaveEditorUtils.localize(this.save.getGame().getInstitution(i).getName(), this.save.getGame()),
                    cities,
                    this.save.getInstitutions().getOrigin(i),
                    new ClearableComboBox<>(new SearchableComboBox<>(), () -> this.save.getInstitutions().getOrigin(finalI)));
            comboBoxItem.setConverter(new ProvinceStringConverter());
            comboBoxItem.setCellFactory(new ProvinceStringCellFactory());

            this.institutionAvailableFields.add(checkBoxItem);
            this.institutionOriginFields.add(comboBoxItem);
            items.add(checkBoxItem);
            items.add(comboBoxItem);
        }

        //GOODS
        this.goodsChangePrices = new HashMap<>();
        for (int i = 0; i < this.save.getChangePrices().getGoods().size(); i++) {
            ChangePriceGood good = this.save.getChangePrices().getGood(i);
            this.goodsChangePrices.put(good.getName(), new ArrayList<>(good.getChangePrices()
                                                                           .stream()
                                                                           .map(c -> new PriceChange(c, this.save))
                                                                           .collect(Collectors.toList())));
            Text priceText = new Text(goodToPrice(good));
            Text modifsText = new Text(goodToModifs(good));

            ButtonItem buttonItem = new ButtonItem(messageSource.getMessage("ose.category.goods", null, Constants.LOCALE),
                                                   " ",
                                                   save.getGame().getLocalisationClean("TSI_CURR_MOD_BY", Eu4Language.getByLocale(Constants.LOCALE)));

            buttonItem.getButton().setOnAction(event -> {
                TableViewDialog<PriceChange> dialog = new TableViewDialog<>(this.save,
                                                                            new TableView2PriceChange(this.goodsChangePrices.get(good.getName()), this.save),
                                                                            this.save.getGame()
                                                                                     .getLocalisationClean("TSI_CURR_MOD_BY",
                                                                                                           Eu4Language.getByLocale(Constants.LOCALE)),
                                                                            null,
                                                                            () -> good.getChangePrices()
                                                                                      .stream()
                                                                                      .map(c -> new PriceChange(c, this.save))
                                                                                      .collect(Collectors.toList()));
                dialog.setDisableAddProperty(new SimpleBooleanProperty(true));
                Optional<List<PriceChange>> changePrices = dialog.showAndWait();

                if (changePrices.isPresent()) {
                    this.goodsChangePrices.put(good.getName(), changePrices.get());
                } else {
                    this.goodsChangePrices.put(good.getName(), good.getChangePrices()
                                                                   .stream()
                                                                   .map(c -> new PriceChange(c, this.save))
                                                                   .collect(Collectors.toList()));
                }

                priceText.setText(goodToPrice(good));
                modifsText.setText(goodToModifs(good));
            });

            HBox hBox = new HBox(0);
            HBox.setHgrow(priceText, Priority.ALWAYS);
            HBox.setHgrow(modifsText, Priority.ALWAYS);
            hBox.getChildren().add(priceText);

            try {
                ImageView imageView = OsaSaveEditorUtils.bufferedToView(ImageReader.convertFileToImage(this.save.getGame().getGoldImage()));
                imageView.setFitWidth(17);
                imageView.setFitHeight(17);
                HBox.setHgrow(imageView, Priority.ALWAYS);
                hBox.getChildren().add(imageView);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }

            hBox.getChildren().add(modifsText);

            items.add(new HBoxItem<>(messageSource.getMessage("ose.category.goods", null, Constants.LOCALE),
                                     OsaSaveEditorUtils.localize(good.getName(), this.save.getGame()), hBox));
            items.add(buttonItem);
        }

        //HRE
        if (!this.save.getHre().dismantled()) {
            this.hreEmperor = new ClearableComboBoxItem<>(messageSource.getMessage("ose.category.hre", null, Constants.LOCALE),
                                                          save.getGame().getLocalisationClean("HINT_EMPEROR_TITLE", Eu4Language.getByLocale(Constants.LOCALE)),
                                                          countriesAlive.filtered(
                                                                  c -> c.getCapital().getContinent().equals(this.save.getHre().getContinent())
                                                                       && Optional.ofNullable(c.getGovernment())
                                                                                  .map(SaveGovernment::getType)
                                                                                  .map(Government::getBasicGovernmentReform)
                                                                                  .map(GovernmentReform::isMonarchy)
                                                                                  .map(Pair::getKey)
                                                                                  .map(BooleanUtils::isTrue).orElse(true)
                                                                       && c.getOverlord() == null
                                                                       && c.getReligion()
                                                                           .getReligionGroup()
                                                                           .equals(this.save.getHre().getEmperor().getReligion().getReligionGroup())),
                                                          this.save.getHre().getEmperor(),
                                                          new ClearableComboBox<>(new SearchableComboBox<>(), () -> this.save.getHre().getEmperor()));
            this.hreEmperor.setConverter(new CountryStringConverter());
            this.hreEmperor.setCellFactory(new CountryStringCellFactory());

            ButtonItem hreElectorsButtonItem = new ButtonItem(messageSource.getMessage("ose.category.hre", null, Constants.LOCALE),
                                                              null,
                                                              save.getGame()
                                                                  .getLocalisationClean("HINT_ELECTOR_TITLE", Eu4Language.getByLocale(Constants.LOCALE)),
                                                              2);

            this.hreElectors = FXCollections.observableArrayList(new ArrayList<>(this.save.getHre().getElectors()));
            ObservableList<SaveCountry> members = FXCollections.observableArrayList(countriesAlive.stream()
                                                                                                  .filter(country -> country.getCapital().inHre())
                                                                                                  .filter(country -> !this.hreElectors.contains(country))
                                                                                                  .collect(Collectors.toList()));
            hreElectorsButtonItem.getButton().setOnAction(event -> {
                ListSelectionViewDialog<SaveCountry> dialog = new ListSelectionViewDialog<>(this.save,
                                                                                            new CustomListSelectionView<>(members, this.hreElectors,
                                                                                                                          new CountryStringCellFactory(),
                                                                                                                          750, 600),
                                                                                            this.save.getGame()
                                                                                                     .getLocalisationClean("HINT_ELECTOR_TITLE",
                                                                                                                           Eu4Language.getByLocale(
                                                                                                                                   Constants.LOCALE)),
                                                                                            () -> countriesAlive.stream()
                                                                                                                .filter(country -> country.getCapital().inHre())
                                                                                                                .filter(country -> !this.hreElectors.contains(
                                                                                                                        country))
                                                                                                                .collect(Collectors.toList()),
                                                                                            () -> this.save.getHre()
                                                                                                           .getElectors());
                Optional<List<SaveCountry>> newElectors = dialog.showAndWait();

                if (newElectors.isEmpty()) {
                    this.hreElectors = FXCollections.observableArrayList(new ArrayList<>(this.save.getHre()
                                                                                                  .getElectors()));
                }
            });

            this.hreInfluenceField = new ClearableSliderItem(messageSource.getMessage("ose.category.hre", null, Constants.LOCALE),
                                                             save.getGame().getLocalisationClean("HRE_INFLUENCE", Eu4Language.getByLocale(Constants.LOCALE)),
                                                             0, 100,
                                                             this.save.getHre().getImperialInfluence(),
                                                             () -> this.save.getHre().getImperialInfluence());

            ButtonItem hreMainLineReformsButtonItem = new ButtonItem(messageSource.getMessage("ose.category.hre", null, Constants.LOCALE),
                                                                     null,
                                                                     save.getGame()
                                                                         .getLocalisationClean("HRE_REFORMS", Eu4Language.getByLocale(Constants.LOCALE)),
                                                                     2);

            this.passedHreMainLineReforms = FXCollections.observableArrayList(this.save.getHre().getMainLinePassedReforms());

            this.notPassedHreMainLineReforms = FXCollections.observableArrayList(this.save.getHre().getMainLineNotPassedReforms());
            hreMainLineReformsButtonItem.getButton().setOnAction(event -> {
                ListSelectionViewImperialReform listSelectionView = new ListSelectionViewImperialReform(this.notPassedHreMainLineReforms,
                                                                                                        this.passedHreMainLineReforms, this.save.getGame());

                ObservableList<ImperialReform> tmpPassedHreMainLineReforms = FXCollections.observableArrayList(this.passedHreMainLineReforms);
                ObservableList<ImperialReform> tmpNotPassedHreMainLineReforms = FXCollections.observableArrayList(this.notPassedHreMainLineReforms);

                ListSelectionViewDialog<ImperialReform> dialog = new ListSelectionViewDialog<>(this.save,
                                                                                               listSelectionView,
                                                                                               this.save.getGame()
                                                                                                        .getLocalisationClean("HRE_REFORMS",
                                                                                                                              Eu4Language.getByLocale(
                                                                                                                                      Constants.LOCALE)),
                                                                                               () -> this.save.getHre().getMainLineNotPassedReforms(),
                                                                                               () -> this.save.getHre().getMainLinePassedReforms());

                Optional<List<ImperialReform>> newMainLineReforms = dialog.showAndWait();

                if (newMainLineReforms.isEmpty()) {
                    this.passedHreMainLineReforms.setAll(tmpPassedHreMainLineReforms);
                    this.notPassedHreMainLineReforms.setAll(tmpNotPassedHreMainLineReforms);
                }
            });

            ButtonItem hreLeftBranchReformsButtonItem = new ButtonItem(messageSource.getMessage("ose.category.hre", null, Constants.LOCALE), null,
                                                                       this.save.getGame()
                                                                                .getLocalisationClean("HRE_LEFTBRANCH",
                                                                                                      Eu4Language.getByLocale(Constants.LOCALE)), 2);

            this.passedHreLeftBranchReforms = FXCollections.observableArrayList(this.save.getHre().getLeftBranchPassedReforms());
            this.notPassedHreLeftBranchReforms = FXCollections.observableArrayList(this.save.getHre().getLeftBranchNotPassedReforms());
            hreLeftBranchReformsButtonItem.setOnAction(event -> {
                ListSelectionViewImperialReform listSelectionView = new ListSelectionViewImperialReform(this.notPassedHreLeftBranchReforms,
                                                                                                        this.passedHreLeftBranchReforms, this.save.getGame());

                ObservableList<ImperialReform> tmpPassedHreLeftBranchReforms = FXCollections.observableArrayList(this.passedHreLeftBranchReforms);
                ObservableList<ImperialReform> tmpNotPassedHreLeftBranchReforms = FXCollections.observableArrayList(this.notPassedHreLeftBranchReforms);

                ListSelectionViewDialog<ImperialReform> dialog = new ListSelectionViewDialog<>(this.save,
                                                                                               listSelectionView,
                                                                                               this.save.getGame()
                                                                                                        .getLocalisationClean("HRE_LEFTBRANCH",
                                                                                                                              Eu4Language.getByLocale(
                                                                                                                                      Constants.LOCALE)),
                                                                                               () -> this.save.getHre().getLeftBranchNotPassedReforms(),
                                                                                               () -> this.save.getHre().getLeftBranchPassedReforms());
                Optional<List<ImperialReform>> newLeftBranchReforms = dialog.showAndWait();

                if (newLeftBranchReforms.isPresent()) {
                    if (!newLeftBranchReforms.get().isEmpty()) {
                        this.passedHreRightBranchReforms.clear();
                        this.notPassedHreRightBranchReforms.setAll(this.save.getHre().getRightBranchReforms());
                    }
                } else {
                    this.passedHreLeftBranchReforms.setAll(tmpPassedHreLeftBranchReforms);
                    this.notPassedHreLeftBranchReforms.setAll(tmpNotPassedHreLeftBranchReforms);
                }
            });

            ButtonItem hreRightBranchReformsButtonItem = new ButtonItem(messageSource.getMessage("ose.category.hre", null, Constants.LOCALE),
                                                                        null,
                                                                        this.save.getGame()
                                                                                 .getLocalisationClean("HRE_RIGHTBRANCH",
                                                                                                       Eu4Language.getByLocale(Constants.LOCALE)),
                                                                        2);

            this.passedHreRightBranchReforms = FXCollections.observableArrayList(this.save.getHre().getRightBranchPassedReforms());

            this.notPassedHreRightBranchReforms = FXCollections.observableArrayList(this.save.getHre().getRightBranchNotPassedReforms());
            hreRightBranchReformsButtonItem.setOnAction(event -> {
                ObservableList<ImperialReform> tmpPassedHreRightBranchReforms = FXCollections.observableArrayList(this.passedHreRightBranchReforms);
                ObservableList<ImperialReform> tmpNotPassedHreRightBranchReforms = FXCollections.observableArrayList(this.notPassedHreRightBranchReforms);

                ListSelectionViewDialog<ImperialReform> dialog = new ListSelectionViewDialog<>(this.save,
                                                                                               new ListSelectionViewImperialReform(
                                                                                                       this.notPassedHreRightBranchReforms,
                                                                                                       this.passedHreRightBranchReforms, this.save.getGame()),
                                                                                               this.save.getGame()
                                                                                                        .getLocalisationClean("HRE_RIGHTBRANCH",
                                                                                                                              Eu4Language.getByLocale(
                                                                                                                                      Constants.LOCALE)),
                                                                                               () -> this.save
                                                                                                       .getHre()
                                                                                                       .getRightBranchNotPassedReforms(),
                                                                                               () -> this.save
                                                                                                       .getHre()
                                                                                                       .getRightBranchPassedReforms());
                Optional<List<ImperialReform>> newRightBranchReforms = dialog.showAndWait();

                if (newRightBranchReforms.isPresent()) {
                    if (!newRightBranchReforms.get().isEmpty()) {
                        this.passedHreLeftBranchReforms.setAll(this.save.getHre().getLeftBranchReforms());
                        this.notPassedHreLeftBranchReforms.setAll(this.save.getHre().getLeftBranchReforms());
                    }
                } else {
                    this.passedHreRightBranchReforms.setAll(tmpPassedHreRightBranchReforms);
                    this.notPassedHreRightBranchReforms.setAll(tmpNotPassedHreRightBranchReforms);
                }
            });

            this.hreLeaguesActives = new CheckBoxItem(messageSource.getMessage("ose.category.hre", null, Constants.LOCALE),
                                                      this.save.getGame()
                                                               .getLocalisationClean("HRE_RELIGIOUS_WAR", Eu4Language.getByLocale(Constants.LOCALE)),
                                                      this.save.getHreLeaguesActive());

            this.hreReligionStatusField = new ClearableComboBoxItem<>(messageSource.getMessage("ose.category.hre", null, Constants.LOCALE),
                                                                      save.getGame()
                                                                          .getLocalisationClean("HRE_DOMINANTFAITH", Eu4Language.getByLocale(Constants.LOCALE)),
                                                                      FXCollections.observableArrayList(
                                                                              HreReligionStatus
                                                                                      .values()),
                                                                      this.save.getHreReligionStatus(),
                                                                      new ClearableComboBox<>(new ComboBox<>(),
                                                                                              this.save::getHreReligionStatus));
            this.hreReligionStatusField.setConverter(new HreReligionStatusStringConverter(this.save));
            this.hreReligionStatusField.setCellFactory(new HreReligionStatusStringCellFactory(this.save));

            items.add(this.hreEmperor);
            items.add(hreElectorsButtonItem);
            items.add(this.hreInfluenceField);
            items.add(hreMainLineReformsButtonItem);
            items.add(hreLeftBranchReformsButtonItem);
            items.add(hreRightBranchReformsButtonItem);
            items.add(this.hreLeaguesActives);
            items.add(this.hreReligionStatusField);
        }

        //CELESTIAL EMPIRE
        if (!this.save.getCelestialEmpire().dismantled()) {
            this.celestialEmperor = new ClearableComboBoxItem<>(messageSource.getMessage("ose.category.celestial-empire", null, Constants.LOCALE),
                                                                save.getGame()
                                                                    .getLocalisationClean("HINT_EMPEROR_TITLE", Eu4Language.getByLocale(Constants.LOCALE)),
                                                                new FilteredList<>(countriesAlive,
                                                                                   country -> country.getReligion() != null &&
                                                                                              ("pagan".equals(country.getReligion()
                                                                                                                     .getReligionGroup()
                                                                                                                     .getName())
                                                                                               || "eastern".equals(country.getReligion()
                                                                                                                          .getReligionGroup()
                                                                                                                          .getName()))),
                                                                this.save.getCelestialEmpire().getEmperor(),
                                                                new ClearableComboBox<>(new SearchableComboBox<>(),
                                                                                        () -> this.save.getCelestialEmpire().getEmperor()));
            this.celestialEmperor.setConverter(new CountryStringConverter());
            this.celestialEmperor.setCellFactory(new CountryStringCellFactory());

            this.celestialInfluenceField = new ClearableSliderItem(messageSource.getMessage("ose.category.celestial-empire", null, Constants.LOCALE),
                                                                   save.getGame()
                                                                       .getLocalisationClean("CELESTIAL_MANDATE", Eu4Language.getByLocale(Constants.LOCALE)),
                                                                   0, 100,
                                                                   this.save.getCelestialEmpire().getImperialInfluence(),
                                                                   () -> this.save.getCelestialEmpire().getImperialInfluence());

            ButtonItem celestialMainLineReformsButtonItem = new ButtonItem(messageSource.getMessage("ose.category.celestial-empire", null, Constants.LOCALE),
                                                                           null,
                                                                           save.getGame()
                                                                               .getLocalisationClean("CELESTIAL_DECISIONS",
                                                                                                     Eu4Language.getByLocale(Constants.LOCALE)),
                                                                           2);

            this.passedCelestialReforms = FXCollections.observableArrayList(this.save.getCelestialEmpire().getMainLinePassedReforms());

            this.notPassedCelestialReforms = FXCollections.observableArrayList(this.save.getCelestialEmpire().getMainLineNotPassedReforms());
            celestialMainLineReformsButtonItem.getButton().setOnAction(event -> {
                ListSelectionViewImperialReform listSelectionView = new ListSelectionViewImperialReform(this.notPassedCelestialReforms,
                                                                                                        this.passedCelestialReforms, this.save.getGame());

                ObservableList<ImperialReform> tmpPassedCelestialMainLineReforms = FXCollections.observableArrayList(this.passedCelestialReforms);
                ObservableList<ImperialReform> tmpNotPassedCelestialMainLineReforms = FXCollections.observableArrayList(this.notPassedCelestialReforms);

                ListSelectionViewDialog<ImperialReform> dialog = new ListSelectionViewDialog<>(this.save,
                                                                                               listSelectionView,
                                                                                               this.save.getGame()
                                                                                                        .getLocalisationClean("CELESTIAL_DECISIONS",
                                                                                                                              Eu4Language.getByLocale(
                                                                                                                                      Constants.LOCALE)),
                                                                                               () -> this.save
                                                                                                       .getCelestialEmpire()
                                                                                                       .getMainLineNotPassedReforms(),
                                                                                               () -> this.save
                                                                                                       .getCelestialEmpire()
                                                                                                       .getMainLinePassedReforms());

                Optional<List<ImperialReform>> newMainLineReforms = dialog.showAndWait();

                if (newMainLineReforms.isEmpty()) {
                    this.passedCelestialReforms.setAll(tmpPassedCelestialMainLineReforms);
                    this.notPassedCelestialReforms.setAll(tmpNotPassedCelestialMainLineReforms);
                }
            });


            List<Decree> decrees = this.save.getGame().getDecrees().stream().map(d -> new Decree(d, this.save)).collect(Collectors.toList());
            decrees.add(0, new Decree(this.save));

            this.decreeField = new ClearableComboBoxItem<>(messageSource.getMessage("ose.category.celestial-empire", null, Constants.LOCALE),
                                                           this.save.getGame()
                                                                    .getLocalisationClean("CELESTIAL_DECREES", Eu4Language.getByLocale(Constants.LOCALE)),
                                                           FXCollections.observableList(decrees),
                                                           Optional.ofNullable(this.save.getCelestialEmpire().getDecree().getDecree())
                                                                   .map(d -> new Decree(d, this.save))
                                                                   .orElse(null),
                                                           new ClearableComboBox<>(new ComboBox<>()));
            this.decreeField.setConverter(new DecreeStringConverter());
            this.decreeField.setCellFactory(new DecreeStringCellFactory());
            this.decreeField.setSupplier(() -> new Decree(this.save.getCelestialEmpire().getDecree().getDecree(), this.save));

            items.add(this.celestialEmperor);
            items.add(this.celestialInfluenceField);
            items.add(celestialMainLineReformsButtonItem);
            items.add(this.decreeField);
        }

        //RELIGIONS
        this.religionPropertySheet = new CustomPropertySheet();
        this.religionPropertySheet.setPropertyEditorFactory(new CustomPropertyEditorFactory());
        this.religionPropertySheet.setMode(CustomPropertySheet.Mode.CATEGORY);
        this.religionPropertySheet.setModeSwitcherVisible(false);
        this.religionPropertySheet.setSearchBoxVisible(false);
        CustomPropertySheetSkin religionPropertySheetSkin = new CustomPropertySheetSkin(this.religionPropertySheet);
        this.religionPropertySheet.setSkin(religionPropertySheetSkin);

        this.religionPropertySheets = new ArrayList<>();

        this.save.getReligions()
                 .getReligions()
                 .values()
                 .stream()
                 .filter(SaveReligion::hasSpecialAttribute)
                 .sorted(Comparator.comparing(r -> OsaSaveEditorUtils.localize(r.getName(), this.save.getGame()), Eu4Utils.COLLATOR))
                 .forEach(religion -> {
                     ReligionPropertySheet relPropertySheet = new ReligionPropertySheet(this.save, religion, countriesAlive,
                                                                                        cities.filtered(province -> religion.equals(province.getReligion())));

                     if (!relPropertySheet.getPropertySheet().getItems().isEmpty()) {
                         this.religionPropertySheets.add(relPropertySheet);
                         this.religionPropertySheet.getItems().addAll(relPropertySheet.getPropertySheet().getItems());
                     }
                 });

        if (!this.religionPropertySheet.getItems().isEmpty()) {
            items.add(new PropertySheetItem(this.save.getGame().getLocalisationClean("LEDGER_RELIGIONS", Eu4Language.getByLocale(Constants.LOCALE)),
                                            this.religionPropertySheet));
        }

        //EVENTS
        ButtonItem firedEventsButtonItem = new ButtonItem(
                this.save.getGame().getLocalisationClean("MENU_MESSAGES_EVENTS", Eu4Language.getByLocale(Constants.LOCALE)),
                null, messageSource.getMessage("ose.fired-events", null, Constants.LOCALE));

        this.firedEvents = FXCollections.observableArrayList(this.save.getFiredEvents().getEvents());
        this.notFiredEvents = FXCollections.observableArrayList(this.save.getGame().getFireOnlyOnceEvents());
        this.notFiredEvents.removeIf(this.firedEvents::contains);

        firedEventsButtonItem.setOnAction(event -> {
            CustomListSelectionView<Event> selectionView = new CustomListSelectionView<>(this.notFiredEvents, this.firedEvents,
                                                                                         new EventStringCellFactory(this.save.getGame()),
                                                                                         1400, 600);

            ObservableList<Event> tmpFiredEvents = FXCollections.observableArrayList(this.firedEvents);
            ObservableList<Event> tmpNotFiredEvents = FXCollections.observableArrayList(this.notFiredEvents);

            ListSelectionViewDialog<Event> dialog = new ListSelectionViewDialog<>(this.save,
                                                                                  selectionView,
                                                                                  this.save.getGame()
                                                                                           .getLocalisationClean("MENU_MESSAGES_EVENTS",
                                                                                                                 Eu4Language.getByLocale(Constants.LOCALE)),
                                                                                  () -> tmpNotFiredEvents,
                                                                                  () -> tmpFiredEvents);
            Optional<List<Event>> newLeftBranchReforms = dialog.showAndWait();

            if (newLeftBranchReforms.isEmpty()) {
                this.firedEvents.setAll(tmpFiredEvents);
                this.notFiredEvents.setAll(tmpNotFiredEvents);
            }
        });

        items.add(firedEventsButtonItem);

        //HEGEMONIES
        this.hegemoniesCountryFields = new ArrayList<>();
        this.hegemoniesProgressesFields = new ArrayList<>();

        for (Hegemon hegemon : this.save.getGame().getHegemons()) {
            Optional<SaveHegemon> saveHegemon = this.save.getHegemons().stream().filter(h -> hegemon.equals(h.hegemon())).findFirst();
            ClearableComboBoxItem<SaveCountry> comboBoxItem = new ClearableComboBoxItem<>(
                    messageSource.getMessage("ose.category.hegemonies", null, Constants.LOCALE),
                    OsaSaveEditorUtils.localize(hegemon.getName(), this.save.getGame()),
                    countriesAlive.filtered(c -> c.getOverlord() == null),
                    saveHegemon.map(SaveHegemon::getCountry).orElse(null),
                    new ClearableComboBox<>(new SearchableComboBox<>(), () -> saveHegemon.map(SaveHegemon::getCountry).orElse(null)));
            comboBoxItem.setConverter(CountryStringConverter.INSTANCE);
            comboBoxItem.setCellFactory(CountryStringCellFactory.INSTANCE);

            ClearableSliderItem sliderItem = new ClearableSliderItem(messageSource.getMessage("ose.category.hegemonies", null, Constants.LOCALE),
                                                                     OsaSaveEditorUtils.localize(hegemon.getName(), this.save.getGame()),
                                                                     0, 100,
                                                                     saveHegemon.map(SaveHegemon::getProgress).orElse(0d),
                                                                     () -> saveHegemon.map(SaveHegemon::getProgress).orElse(0d));
            sliderItem.isEditable().bind(comboBoxItem.getComboBox().valueProperty().isNotNull());

            this.hegemoniesCountryFields.add(comboBoxItem);
            this.hegemoniesProgressesFields.add(sliderItem);

            items.add(comboBoxItem);
            items.add(sliderItem);
        }

        this.propertySheet.getItems().setAll(items);
    }

    @Override
    public Set<CustomPropertySheet.Item<?>> internalUpdate(Save save) {
        //GAME OPTIONS
        this.difficultyField.setValue(this.save.getGameplayOptions().getDifficulty());
        this.allowHotJoinField.setValue(this.save.getGameplayOptions().getAllowHotjoin());
        this.allowCoopField.setValue(this.save.getGameplayOptions().getAllowCoop());
        this.terraIncognitaField.setValue(this.save.getGameplayOptions().getTerraIncognita());
        this.saveEditableField.setValue(this.save.getGameplayOptions().getSaveEditable());
        this.lockedLedgerField.setValue(this.save.getGameplayOptions().getLockedLedger());
        this.limitedLedgerField.setValue(this.save.getGameplayOptions().getLimitedLedger());
        this.dynamicProvinceNamesField.setValue(this.save.getGameplayOptions().getDynamicProvinceNames());
        this.customNationDifficultyField.setValue(this.save.getGameplayOptions().getCustomNationDifficulty());
        this.addNationsToGameField.setValue(this.save.getGameplayOptions().getAddNationsToGame());
        this.showMonthlyTaxIncomeField.setValue(this.save.getGameplayOptions().getShowMonthlyTaxIncome());
        this.colorWastelandsField.setValue(this.save.getGameplayOptions().getColorWastelands());
        this.exclavesRegionNameField.setValue(this.save.getGameplayOptions().getExclavesRegionName());
        this.blockNationRuiningField.setValue(this.save.getGameplayOptions().getBlockNationRuining());
        this.unlimitedIdeaGroupsField.setValue(this.save.getGameplayOptions().getUnlimitedIdeaGroups());
        this.allowNameChangeField.setValue(this.save.getGameplayOptions().getAllowNameChange());
        this.onlyHostCanPauseField.setValue(this.save.getGameplayOptions().getOnlyHostCanPause());
        this.onlyHostAndObserversCanSaveField.setValue(this.save.getGameplayOptions().getOnlyHostAndObserversCanSave());
        this.allowTeamsField.setValue(this.save.getGameplayOptions().getAllowTeams());
        this.allowFreeTeamCreationField.setValue(this.save.getGameplayOptions().getAllowFreeTeamCreation());

        //INSTITUTIONS
        for (int i = 0; i < this.institutionOriginFields.size(); i++) {
            this.institutionAvailableFields.get(i).setValue(this.save.getInstitutions().isAvailable(i));
            this.institutionOriginFields.get(i).setValue(this.save.getInstitutions().getOrigin(i));
        }

        //GOODS
        this.goodsChangePrices.clear();
        this.save.getChangePrices()
                 .getGoods()
                 .forEach((name, changePriceGood) -> this.goodsChangePrices.put(name, new ArrayList<>(changePriceGood.getChangePrices()
                                                                                                                     .stream()
                                                                                                                     .map(c -> new PriceChange(c, this.save))
                                                                                                                     .collect(Collectors.toList()))));

        //HRE
        if (!this.save.getHre().dismantled()) {
            this.hreEmperor.setValue(this.save.getHre().getEmperor());
            this.hreElectors = FXCollections.observableArrayList(new ArrayList<>(this.save.getHre().getElectors()));
            this.hreInfluenceField.setValue(this.save.getHre().getImperialInfluence());
            this.passedHreMainLineReforms.setAll(this.save.getHre().getMainLinePassedReforms());
            this.notPassedHreMainLineReforms.setAll(this.save.getHre().getMainLineNotPassedReforms());
            this.passedHreLeftBranchReforms.setAll(this.save.getHre().getLeftBranchPassedReforms());
            this.notPassedHreLeftBranchReforms.setAll(this.save.getHre().getLeftBranchNotPassedReforms());
            this.passedHreRightBranchReforms.setAll(this.save.getHre().getRightBranchPassedReforms());
            this.notPassedHreRightBranchReforms.setAll(this.save.getHre().getRightBranchNotPassedReforms());
            this.hreLeaguesActives.setValue(this.save.getHreLeaguesActive());
            this.hreReligionStatusField.setValue(this.save.getHreReligionStatus());
        }

        //CELESTIAL EMPIRE
        if (!this.save.getCelestialEmpire().dismantled()) {
            this.celestialEmperor.setValue(this.save.getCelestialEmpire().getEmperor());
            this.celestialInfluenceField.setValue(this.save.getCelestialEmpire().getImperialInfluence());
            this.passedCelestialReforms.setAll(this.save.getCelestialEmpire().getMainLinePassedReforms());
            this.notPassedCelestialReforms.setAll(this.save.getCelestialEmpire().getMainLineNotPassedReforms());

            if (this.save.getCelestialEmpire().getDecree().getDecree() != null) {
                this.decreeField.setValue(new Decree(this.save.getCelestialEmpire().getDecree().getDecree(), this.save));
            } else {
                this.decreeField.setValue(null);
            }
        }

        //RELIGIONS
        this.religionPropertySheets.forEach(sheet -> sheet.update(sheet.t));

        //EVENTS
        this.firedEvents.setAll(this.save.getFiredEvents().getEvents());
        this.notFiredEvents.setAll(this.save.getGame().getFireOnlyOnceEvents());
        this.notFiredEvents.removeIf(this.firedEvents::contains);

        return new HashSet<>(this.propertySheet.getItems());
    }

    public void internalValidate() {
        //GAME OPTIONS
        if (!this.save.getGameplayOptions().getDifficulty().equals(this.difficultyField.getValue())) {
            this.save.getGameplayOptions().setDifficulty(this.difficultyField.getValue());
        }

        if (this.save.getGameplayOptions().getAllowHotjoin() != this.allowHotJoinField.isSelected()) {
            this.save.getGameplayOptions().setAllowHotjoin(this.allowHotJoinField.isSelected());
        }

        if (this.save.getGameplayOptions().getAllowCoop() != this.allowCoopField.isSelected()) {
            this.save.getGameplayOptions().setAllowCoop(this.allowCoopField.isSelected());
        }

        if (this.save.getGameplayOptions().getTerraIncognita() != this.terraIncognitaField.isSelected()) {
            this.save.getGameplayOptions().setTerraIncognita(this.terraIncognitaField.isSelected());
        }

        if (this.save.getGameplayOptions().getSaveEditable() != this.saveEditableField.isSelected()) {
            this.save.getGameplayOptions().setSaveEditable(this.saveEditableField.isSelected());
        }

        if (this.save.getGameplayOptions().getLockedLedger() != this.lockedLedgerField.isSelected()) {
            this.save.getGameplayOptions().setLockedLedger(this.lockedLedgerField.isSelected());
        }

        if (this.save.getGameplayOptions().getLimitedLedger() != this.limitedLedgerField.isSelected()) {
            this.save.getGameplayOptions().setLimitedLedger(this.limitedLedgerField.isSelected());
        }

        if (this.save.getGameplayOptions().getDynamicProvinceNames() != this.dynamicProvinceNamesField.isSelected()) {
            this.save.getGameplayOptions().setDynamicProvinceNames(this.dynamicProvinceNamesField.isSelected());
        }

        if (!this.save.getGameplayOptions()
                      .getCustomNationDifficulty()
                      .equals(this.customNationDifficultyField.getValue())) {
            this.save.getGameplayOptions()
                     .setCustomNationDifficulty(this.customNationDifficultyField.getValue());
        }

        if (this.save.getGameplayOptions().getAddNationsToGame() != this.addNationsToGameField.isSelected()) {
            this.save.getGameplayOptions().setAddNationsToGame(this.addNationsToGameField.isSelected());
        }

        if (this.save.getGameplayOptions().getShowMonthlyTaxIncome() != this.showMonthlyTaxIncomeField.isSelected()) {
            this.save.getGameplayOptions().setShowMonthlyTaxIncome(this.showMonthlyTaxIncomeField.isSelected());
        }

        if (this.save.getGameplayOptions().getColorWastelands() != this.colorWastelandsField.isSelected()) {
            this.save.getGameplayOptions().setColorWastelands(this.colorWastelandsField.isSelected());
        }

        if (this.save.getGameplayOptions().getExclavesRegionName() != this.exclavesRegionNameField.isSelected()) {
            this.save.getGameplayOptions().setExclavesRegionName(this.exclavesRegionNameField.isSelected());
        }

        if (this.save.getGameplayOptions().getBlockNationRuining() != this.blockNationRuiningField.isSelected()) {
            this.save.getGameplayOptions().setBlockNationRuining(this.blockNationRuiningField.isSelected());
        }

        if (this.save.getGameplayOptions().getUnlimitedIdeaGroups() != this.unlimitedIdeaGroupsField.isSelected()) {
            this.save.getGameplayOptions().setUnlimitedIdeaGroups(this.unlimitedIdeaGroupsField.isSelected());
        }

        if (this.save.getGameplayOptions().getAllowNameChange() != this.allowNameChangeField.isSelected()) {
            this.save.getGameplayOptions().setAllowNameChange(this.allowNameChangeField.isSelected());
        }

        if (this.save.getGameplayOptions().getOnlyHostCanPause() != this.onlyHostCanPauseField.isSelected()) {
            this.save.getGameplayOptions().setOnlyHostCanPause(this.onlyHostCanPauseField.isSelected());
        }

        if (this.save.getGameplayOptions().getOnlyHostAndObserversCanSave()
            != this.onlyHostAndObserversCanSaveField.isSelected()) {
            this.save.getGameplayOptions()
                     .setOnlyHostAndObserversCanSave(this.onlyHostAndObserversCanSaveField.isSelected());
        }

        if (this.save.getGameplayOptions().getAllowTeams() != this.allowTeamsField.isSelected()) {
            this.save.getGameplayOptions().setAllowFreeTeamCreation(this.allowTeamsField.isSelected());
        }

        if (this.save.getGameplayOptions().getAllowFreeTeamCreation() != this.allowFreeTeamCreationField.isSelected()) {
            this.save.getGameplayOptions().setAllowFreeTeamCreation(this.allowFreeTeamCreationField.isSelected());
        }

        //INSTITUTIONS
        if (!this.institutionAvailableFields.isEmpty()) {
            for (int i = 0; i < this.institutionAvailableFields.size(); i++) {
                if ((this.institutionAvailableFields.get(i).isSelected() !=
                     this.save.getInstitutions().isAvailable(i))
                    || this.institutionOriginFields.get(i).getValue() != this.save.getInstitutions()
                                                                                  .getOrigin(i)) {
                    if (this.institutionAvailableFields.get(i).isSelected()) {
                        this.save.getInstitutions()
                                 .availableIn(i, this.institutionOriginFields.get(i).getValue());
                    } else {
                        this.save.getInstitutions().disable(i);
                        this.institutionOriginFields.get(i).getComboBox().reset();
                    }
                }
            }
        }

        //GOODS
        if (!this.goodsChangePrices.isEmpty()) {
            this.goodsChangePrices.forEach((name, changePrices) -> this.save.getChangePrices()
                                                                            .getGood(name)
                                                                            .setChangePrices(changePrices.stream()
                                                                                                         .map(PriceChange::toChangePrice)
                                                                                                         .collect(Collectors.toList())));
        }

        //HRE
        if (!this.save.getHre().dismantled()) {
            if (this.save.getHre().getEmperor() != this.hreEmperor.getValue()) {
                this.save.getHre().setEmperor(this.hreEmperor.getValue());
            }

            if (!this.save.getHre().getElectors().equals(this.hreElectors)) {
                this.save.getHre().setElectors(this.hreElectors);
            }

            if (!this.save.getHre().getImperialInfluence().equals(this.hreInfluenceField.getDoubleValue())) {
                this.save.getHre().setImperialInfluence(this.hreInfluenceField.getDoubleValue());
            }

            if (!this.save.getHre().getMainLinePassedReforms().equals(this.passedHreMainLineReforms)
                || !this.save.getHre().getLeftBranchPassedReforms().equals(this.passedHreLeftBranchReforms)
                || !this.save.getHre().getRightBranchPassedReforms().equals(this.passedHreRightBranchReforms)) {
                this.save.getHre().setPassedReforms(
                        Stream.of(this.passedHreMainLineReforms, this.passedHreLeftBranchReforms,
                                  this.passedHreRightBranchReforms)
                              .flatMap(Collection::stream)
                              .collect(Collectors.toList()));
            }
        }

        //CELESTIAL EMPIRE
        if (!this.save.getCelestialEmpire().dismantled()) {
            if (this.save.getCelestialEmpire().getEmperor() != this.celestialEmperor.getValue()) {
                this.save.getCelestialEmpire().setEmperor(this.celestialEmperor.getValue());
            }

            if (!this.save.getCelestialEmpire()
                          .getImperialInfluence()
                          .equals(this.celestialInfluenceField.getDoubleValue())) {
                this.save.getCelestialEmpire().setImperialInfluence(this.celestialInfluenceField.getDoubleValue());
            }

            if (!this.save.getCelestialEmpire().getMainLinePassedReforms().equals(this.passedCelestialReforms)) {
                this.save.getCelestialEmpire().setPassedReforms(this.passedCelestialReforms);
            }

            if ((this.decreeField.getValue() == null && this.save.getCelestialEmpire().getDecree().getDecree() != null)
                || (this.decreeField.getValue() != null
                    && !this.decreeField.getValue().getDecree().equals(this.save.getCelestialEmpire().getDecree().getDecree().getName()))) {
                this.save.getCelestialEmpire().setDecree(this.save.getGame().getDecree(this.decreeField.getValue().getDecree()));
            }

            if (this.hreLeaguesActives != null && !this.save.getHreLeaguesActive().equals(this.hreLeaguesActives.isSelected())) {
                this.save.setHreLeaguesActive(this.hreLeaguesActives.isSelected());
            }

            if (this.hreReligionStatusField != null && !this.save.getHreReligionStatus().equals(this.hreReligionStatusField.getValue())) {
                this.save.setHreReligionStatus(this.hreReligionStatusField.getValue());
            }
        }

        //RELIGIONS
        this.religionPropertySheets.forEach(ReligionPropertySheet::validate);

        //EVENTS

        //HEGEMONIES
        List<Hegemon> hegemons = this.save.getGame().getHegemons();
        for (int i = 0; i < hegemons.size(); i++) {
            Hegemon hegemon = hegemons.get(i);
            Optional<SaveHegemon> saveHegemon = this.save.getHegemons().stream().filter(h -> hegemon.equals(h.hegemon())).findFirst();

            if (!Objects.equals(this.hegemoniesCountryFields.get(i).getValue(), saveHegemon.map(SaveHegemon::getCountry).orElse(null))
                || !Objects.equals(this.hegemoniesProgressesFields.get(i).getDoubleValue(), saveHegemon.map(SaveHegemon::getProgress).orElse(0d))) {
                this.save.setHegemon(hegemon, this.hegemoniesCountryFields.get(i).getValue(), this.hegemoniesProgressesFields.get(i).getDoubleValue());
            }
        }
    }

    private String goodToModifs(ChangePriceGood good) {
        double modifiersSum = this.goodsChangePrices.get(good.getName())
                                                    .stream()
                                                    .mapToDouble(PriceChange::getValue)
                                                    .sum();

        return "(" + good.getBasicPrice() + (modifiersSum < 0 ? " " : " +") + modifiersSum + "%)";
    }

    private String goodToPrice(ChangePriceGood good) {
        double modifiersSum = this.goodsChangePrices.get(good.getName())
                                                    .stream()
                                                    .mapToDouble(PriceChange::getValue)
                                                    .sum();

        return BigDecimal.valueOf(good.getBasicPrice())
                         .multiply(BigDecimal.valueOf(100).add(BigDecimal.valueOf(modifiersSum)))
                         .divide(BigDecimal.valueOf(100), 5, RoundingMode.HALF_EVEN)
                         .setScale(3, RoundingMode.HALF_EVEN)
                         .toPlainString();
    }

    public ValidationSupport getValidationSupport() {
        return validationSupport;
    }
}
