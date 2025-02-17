package fr.osallek.osasaveeditor.controller.control;

import fr.osallek.eu4parser.model.UnitType;
import fr.osallek.eu4parser.model.game.Unit;
import fr.osallek.eu4parser.model.save.country.SaveCountry;
import fr.osallek.osasaveeditor.common.Constants;
import fr.osallek.osasaveeditor.controller.converter.UnitStringConverter;
import fr.osallek.osasaveeditor.controller.converter.UnitTypeStringConverter;
import fr.osallek.osasaveeditor.controller.object.Regiment;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.converter.DoubleStringConverter;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.stream.Collectors;

public class TableView2Regiment extends CustomTableView<Regiment> {

    public TableView2Regiment(SaveCountry country, List<Regiment> regiments, MessageSource messageSource, UnitType[] unitTypes) {
        TableColumn<Regiment, String> name = new TableColumn<>(messageSource.getMessage("ose.name", null, Constants.LOCALE));
        name.setCellValueFactory(p -> p.getValue() == null ? null : new ReadOnlyObjectWrapper<>(p.getValue().getName()));
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(event -> event.getRowValue().setName(event.getNewValue()));
        name.setStyle("-fx-alignment: CENTER-LEFT");

        TableColumn<Regiment, UnitType> unitType = new TableColumn<>(messageSource.getMessage("country.unitType", null, Constants.LOCALE));
        unitType.setCellValueFactory(p -> p.getValue() == null ? null : new ReadOnlyObjectWrapper<>(p.getValue().getUnitType()));
        unitType.setCellFactory(ComboBoxTableCell.forTableColumn(new UnitTypeStringConverter(country.getSave().getGame()), unitTypes));
        unitType.setOnEditCommit(event -> event.getRowValue().setUnitType(event.getNewValue()));
        unitType.setEditable(true);
        unitType.setStyle("-fx-alignment: CENTER-LEFT");

        TableColumn<Regiment, Unit> unit = new TableColumn<>(messageSource.getMessage("ose.type", null, Constants.LOCALE));
        unit.setCellValueFactory(p -> p.getValue() == null ? null : p.getValue().getType());
        unit.setCellFactory(ComboBoxTableCell.forTableColumn(new UnitStringConverter(country.getSave().getGame())));
        unit.setOnEditCommit(event -> event.getRowValue().setType(event.getNewValue()));
        unit.setEditable(false);
        unit.setStyle("-fx-alignment: CENTER-LEFT");

        TableColumn<Regiment, Double> morale = new TableColumn<>(messageSource.getMessage("country.morale", null, Constants.LOCALE));
        morale.setCellValueFactory(p -> p.getValue() == null ? null : new ReadOnlyObjectWrapper<>(p.getValue().getMorale()));
        morale.setCellFactory(SpinnerTableCell.forTableColumn(0, country.getLandMorale(), 0d, new DoubleStringConverter()));
        morale.setOnEditCommit(event -> event.getRowValue().setMorale(event.getNewValue()));
        morale.setPrefWidth(50);
        morale.setEditable(true);
        morale.setStyle("-fx-alignment: CENTER-LEFT");

        TableColumn<Regiment, Void> remove = new TableColumn<>();
        remove.setPrefWidth(48);
        remove.setEditable(false);
        remove.setCellFactory(ClearCellFactory.forTableColumn());

        setPrefWidth(name.getPrefWidth() + unitType.getPrefWidth() + unit.getPrefWidth() + morale.getPrefWidth() + remove.getPrefWidth());
        setFixedCellSize(40);
        setEditable(true);

        getColumns().clear();
        getColumns().add(name);
        getColumns().add(unitType);
        getColumns().add(unit);
        getColumns().add(morale);
        getColumns().add(remove);
        setItems(regiments.stream().map(Regiment::new).collect(Collectors.toCollection(FXCollections::observableArrayList)));

        prepare();
    }
}
