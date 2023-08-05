package fr.osallek.osasaveeditor.controller.mapview;

import fr.osallek.eu4parser.model.game.localisation.Eu4Language;
import fr.osallek.eu4parser.model.save.country.SaveCountry;
import fr.osallek.eu4parser.model.save.country.SaveEstate;
import fr.osallek.osasaveeditor.common.Constants;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import fr.osallek.osasaveeditor.controller.control.TableView2Privilege;
import fr.osallek.osasaveeditor.controller.object.Privilege;
import fr.osallek.osasaveeditor.controller.pane.CustomPropertySheet;
import fr.osallek.osasaveeditor.controller.pane.TableViewDialog;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ButtonItem;
import fr.osallek.osasaveeditor.controller.propertyeditor.item.ClearableSliderItem;
import fr.osallek.osasaveeditor.controller.validator.CustomGraphicValidationDecoration;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.decoration.CompoundValidationDecoration;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class EstatePropertySheet extends PropertySheet<SaveEstate> {

    private final SaveCountry country;

    private final ClearableSliderItem loyaltyField;

    private final ClearableSliderItem territoryField;

    private final ObservableList<Privilege> privileges;

    private final ValidationSupport validationSupport;

    private final List<DoubleProperty> countryEstatesTerritory = FXCollections.observableArrayList();

    public EstatePropertySheet(SaveCountry country, SaveEstate estate) {
        super(country.getSave(), estate);
        this.country = country;

        List<CustomPropertySheet.Item<?>> items = new ArrayList<>();

        this.loyaltyField = new ClearableSliderItem(OsaSaveEditorUtils.localize(this.t.getEstateGame().getName(), this.country.getSave().getGame()),
                                                    this.country.getSave().getGame().getLocalisationClean("LOYALTY", Eu4Language.getByLocale(Constants.LOCALE)),
                                                    0, 100, this.t.getLoyalty(), this.t::getLoyalty);
        items.add(this.loyaltyField);

        this.territoryField = new ClearableSliderItem(OsaSaveEditorUtils.localize(this.t.getEstateGame().getName(), this.country.getSave().getGame()),
                                                      this.country.getSave()
                                                                  .getGame()
                                                                  .getLocalisationClean("TERRITORY", Eu4Language.getByLocale(Constants.LOCALE)),
                                                      0, 100, this.t.getTerritory(), this.t::getTerritory);
        this.territoryField.getObservableDoubleValue().addListener((observable, oldValue, newValue) -> {
            if ((oldValue.doubleValue() == 100d || newValue.doubleValue() == 100d || !Objects.equals(oldValue, newValue))
                && (this.countryEstatesTerritory.stream().mapToDouble(DoubleExpression::doubleValue).sum() + newValue.doubleValue()) > 100d) {
                this.territoryField.getObservableDoubleValue().set(100d - this.countryEstatesTerritory.stream().mapToDouble(DoubleExpression::getValue).sum());
            }
        });
        items.add(this.territoryField);

        this.privileges = FXCollections.observableArrayList(this.t.getGrantedPrivileges().stream().map(Privilege::new).collect(Collectors.toList()));
        ButtonItem privilegeButton = new ButtonItem(OsaSaveEditorUtils.localize(this.t.getEstateGame().getName(), this.country.getSave().getGame()), null,
                                                    country.getSave().getGame().getLocalisationClean("PRIVILEGE_PICKER_TITLE", Eu4Language.getByLocale(
                                                            Constants.LOCALE)), 2);
        privilegeButton.getButton().setOnAction(event -> {
            TableView2Privilege tableView2Privilege = new TableView2Privilege(this.country, this.t, this.privileges,
                                                                              FXCollections.observableArrayList(
                                                                                      this.t.getEstateGame().getPrivileges().values()));
            TableViewDialog<Privilege> dialog =
                    new TableViewDialog<>(this.country.getSave(),
                                          tableView2Privilege,
                                          this.country.getSave()
                                                      .getGame()
                                                      .getLocalisationClean("PRIVILEGE_PICKER_TITLE", Eu4Language.getByLocale(Constants.LOCALE)),
                                          list -> new Privilege(this.t.getEstateGame()
                                                                      .getPrivileges()
                                                                      .values()
                                                                      .stream()
                                                                      .filter(p -> list.stream().noneMatch(p2 -> p2.getPrivilege().equals(p)))
                                                                      .findFirst()
                                                                      .get(),
                                                                this.country.getSave().getDate()),
                                          () -> this.privileges.stream()
                                                               .map(Privilege::new)
                                                               .collect(Collectors.toCollection(FXCollections::observableArrayList)));
            dialog.setDisableAddProperty(tableView2Privilege.disableAddPropertyProperty());
            Optional<List<Privilege>> privilegeList = dialog.showAndWait();

            privilegeList.ifPresent(this.privileges::setAll);
        });
        items.add(privilegeButton);

        this.validationSupport = new ValidationSupport();
        this.validationSupport.setValidationDecorator(
                new CompoundValidationDecoration(new CustomGraphicValidationDecoration(), new StyleClassValidationDecoration("validation-error", null)));

        this.propertySheet.getItems().setAll(items);
    }

    @Override
    public Set<CustomPropertySheet.Item<?>> internalUpdate(SaveEstate saveEstate) {
        return new HashSet<>(this.propertySheet.getItems());
    }

    public void internalValidate() {
        if (!Objects.equals(this.t.getLoyalty(), this.loyaltyField.getDoubleValue())) {
            this.t.setLoyalty(this.loyaltyField.getDoubleValue());
        }

        if (!Objects.equals(this.t.getTerritory(), this.territoryField.getDoubleValue())) {
            this.t.setTerritory(this.territoryField.getDoubleValue());
        }

        if (this.t.getGrantedPrivileges().size() != this.privileges.size() || this.privileges.stream().anyMatch(Privilege::isChanged)) {
            this.t.getGrantedPrivileges().forEach(i -> this.privileges.stream()
                                                                      .filter(p -> i.getPrivilege().equals(p.getPrivilege()))
                                                                      .findFirst()
                                                                      .ifPresentOrElse(p -> {
                                                                                           if (!Objects.equals(p.getStartDate(), i.getDate())) {
                                                                                               i.setDate(p.getStartDate());
                                                                                           }

                                                                                           this.privileges.remove(p);
                                                                                       },
                                                                                       () -> this.t.removeGrantedPrivilege(i.getPrivilege())));
            this.privileges.forEach(p -> this.t.addGrantedPrivilege(p.getPrivilege(), p.getStartDate()));
        }
    }

    public DoubleProperty territoryValue() {
        return this.territoryField.getObservableDoubleValue();
    }

    public ValidationSupport getValidationSupport() {
        return validationSupport;
    }

    public List<DoubleProperty> getCountryEstatesTerritory() {
        return countryEstatesTerritory;
    }
}
