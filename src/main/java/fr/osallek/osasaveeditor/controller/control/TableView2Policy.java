package fr.osallek.osasaveeditor.controller.control;

import fr.osallek.eu4parser.common.Eu4Utils;
import fr.osallek.eu4parser.model.game.ModifiersUtils;
import fr.osallek.eu4parser.model.game.Policy;
import fr.osallek.eu4parser.model.game.localisation.Eu4Language;
import fr.osallek.eu4parser.model.save.country.SaveCountry;
import fr.osallek.osasaveeditor.common.Constants;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import fr.osallek.osasaveeditor.controller.converter.PolicyStringConverter;
import fr.osallek.osasaveeditor.controller.object.ActivePolicy;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TableView2Policy extends TableView<ActivePolicy> {

    private final SaveCountry country;

    private final String modifier;

    private final BooleanProperty disableAddProperty;

    private final ObservableList<Policy> availablePolicies;

    private final ObservableMap<ActivePolicy, ObservableList<Policy>> policiesMap = FXCollections.observableHashMap();

    public TableView2Policy(SaveCountry country, ObservableList<ActivePolicy> activePolicies, ObservableList<Policy> availablePolicies, String modifier) {
        this.country = country;
        this.availablePolicies = availablePolicies;
        this.modifier = modifier;

        TableColumn<ActivePolicy, Policy> target = new TableColumn<>(country.getSave()
                                                                            .getGame()
                                                                            .getLocalisationClean("POLICY_ENACTED_TITLE", Eu4Language.getByLocale(
                                                                                    Constants.LOCALE)));
        target.setCellValueFactory(p -> p.getValue() == null ? null : new ReadOnlyObjectWrapper<>(p.getValue().getPolicy()));
        target.setCellFactory(UniqueComboBoxTableCell.forTableColumn(new PolicyStringConverter(country.getSave().getGame()),
                                                                     this.policiesMap,
                                                                     Comparator.comparing(p -> OsaSaveEditorUtils.localize(p.getName(), country.getSave()
                                                                                                                                               .getGame()),
                                                                                          Eu4Utils.COLLATOR),
                                                                     getItems(),
                                                                     this::getNewList
                                                                    ));
        target.setOnEditCommit(event -> event.getRowValue().setPolicy(event.getNewValue()));
        target.setPrefWidth(250);
        target.setStyle("-fx-alignment: CENTER-LEFT");

        TableColumn<ActivePolicy, LocalDate> date = new TableColumn<>(country.getSave()
                                                                             .getGame()
                                                                             .getLocalisationCleanNoPunctuation("FE_STARTING_DATE", Eu4Language.getByLocale(Constants.LOCALE)));
        date.setCellValueFactory(p -> p.getValue() == null ? null : new ReadOnlyObjectWrapper<>(p.getValue().getDate()));
        date.setCellFactory(DatePickerCell.forTableColumn(null, country.getSave().getDate()));
        date.setOnEditCommit(event -> event.getRowValue().setDate(event.getNewValue()));
        date.setPrefWidth(150);
        date.setStyle("-fx-alignment: CENTER-LEFT");

        TableColumn<ActivePolicy, Void> remove = new TableColumn<>();
        remove.setPrefWidth(48);
        remove.setEditable(false);
        remove.setCellFactory(ClearCellFactory.forTableColumn());

        setFixedCellSize(40);
        setPrefWidth(450);
        setEditable(true);

        getColumns().clear();
        getColumns().add(target);
        getColumns().add(date);
        getColumns().add(remove);
        setItems(activePolicies.stream().map(ActivePolicy::new).collect(Collectors.toCollection(FXCollections::observableArrayList)));
        getItems().forEach(activePolicy -> this.policiesMap.put(activePolicy, getNewList()));

        this.disableAddProperty = new SimpleBooleanProperty(disableAddButton(getItems()));

        getItems().addListener((ListChangeListener<? super ActivePolicy>) c -> this.disableAddProperty.setValue(disableAddButton(c.getList())));
    }

    private ObservableList<Policy> getNewList() {
        return this.availablePolicies.stream()
                                     .filter(c -> getItems().stream().noneMatch(activePolicy -> activePolicy.getPolicy().equals(c)))
                                     .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    private boolean disableAddButton(List<? extends ActivePolicy> policies) {
        return policies.size() >= this.country.getSave().getGame().getMaxActivePolicies()
               || policies.size() >= (this.country.getSave().getGame().getBasePossiblePolicies() +
                                      this.country.getModifier(ModifiersUtils.getModifier("POSSIBLE_POLICY")) +
                                      this.country.getModifier(ModifiersUtils.getModifier(this.modifier)))
               || policies.size() >= this.availablePolicies.size();
    }

    public boolean isDisableAddProperty() {
        return disableAddProperty.get();
    }

    public BooleanProperty disableAddPropertyProperty() {
        return disableAddProperty;
    }
}
