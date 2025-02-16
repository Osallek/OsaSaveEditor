package fr.osallek.osasaveeditor.controller.control;

import fr.osallek.eu4parser.model.save.country.SaveCountry;
import fr.osallek.eu4parser.model.save.province.SaveProvince;
import fr.osallek.osasaveeditor.common.Constants;
import fr.osallek.osasaveeditor.controller.converter.ProvinceStringConverter;
import fr.osallek.osasaveeditor.controller.object.Army;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ComboBoxTableCell;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.stream.Collectors;

public class TableView2Army extends CustomTableView<Army> {

    public TableView2Army(SaveCountry country, ObservableList<Army> armies, ObservableList<SaveProvince> provinces, MessageSource messageSource) {
        TableColumn<Army, String> name = new TableColumn<>(messageSource.getMessage("ose.name", null, Constants.LOCALE));
        name.setCellValueFactory(p -> p.getValue() == null ? null : new ReadOnlyObjectWrapper<>(p.getValue().getName()));
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(event -> event.getRowValue().setName(event.getNewValue()));
        name.setStyle("-fx-alignment: CENTER-LEFT");

        TableColumn<Army, SaveProvince> location = new TableColumn<>(messageSource.getMessage("ose.location", null, Constants.LOCALE));
        location.setMinWidth(90);
        location.setCellValueFactory(p -> p.getValue() == null ? null : new ReadOnlyObjectWrapper<>(country.getSave().getProvince(p.getValue().getLocation())));
        List<Integer> owned = country.getOwnedProvincesIds();
        location.setCellFactory(ComboBoxTableCell.forTableColumn(ProvinceStringConverter.INSTANCE, provinces.filtered(p -> owned.contains(p.getId()))));
        location.setOnEditCommit(event -> event.getRowValue().setLocation(event.getNewValue().getId()));
        location.setEditable(true);
        location.setStyle("-fx-alignment: CENTER-LEFT");

        TableColumn<Army, Void> remove = new TableColumn<>();
        remove.setPrefWidth(48);
        remove.setEditable(false);
        remove.setCellFactory(ClearCellFactory.forTableColumn());

        setPrefWidth(name.getPrefWidth() + location.getPrefWidth() + remove.getPrefWidth());
        setFixedCellSize(40);
        setEditable(true);

        getColumns().clear();
        getColumns().add(name);
        getColumns().add(location);
        getColumns().add(remove);
        setItems(armies.stream().map(Army::new).collect(Collectors.toCollection(FXCollections::observableArrayList)));

        prepare();
    }
}
