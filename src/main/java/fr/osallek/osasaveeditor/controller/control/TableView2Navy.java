package fr.osallek.osasaveeditor.controller.control;

import fr.osallek.eu4parser.model.UnitType;
import fr.osallek.eu4parser.model.save.country.SaveCountry;
import fr.osallek.eu4parser.model.save.province.SaveProvince;
import fr.osallek.osasaveeditor.common.Constants;
import fr.osallek.osasaveeditor.controller.converter.ProvinceStringConverter;
import fr.osallek.osasaveeditor.controller.object.Navy;
import fr.osallek.osasaveeditor.controller.object.Regiment;
import fr.osallek.osasaveeditor.controller.pane.TableViewDialog;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ComboBoxTableCell;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.stream.Collectors;

public class TableView2Navy extends CustomTableView<Navy> {

    public TableView2Navy(SaveCountry country, ObservableList<Navy> navies, ObservableList<SaveProvince> provinces, MessageSource messageSource) {
        TableColumn<Navy, String> name = new TableColumn<>(messageSource.getMessage("ose.name", null, Constants.LOCALE));
        name.setCellValueFactory(p -> p.getValue() == null ? null : new ReadOnlyObjectWrapper<>(p.getValue().getName()));
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(event -> event.getRowValue().setName(event.getNewValue()));
        name.setStyle("-fx-alignment: CENTER-LEFT");

        TableColumn<Navy, SaveProvince> location = new TableColumn<>(messageSource.getMessage("ose.location", null, Constants.LOCALE));
        location.setMinWidth(90);
        location.setCellValueFactory(p -> p.getValue() == null ? null : new ReadOnlyObjectWrapper<>(country.getSave().getProvince(p.getValue().getLocation())));
        List<Integer> owned = country.getOwnedProvincesIds();
        location.setCellFactory(ComboBoxTableCell.forTableColumn(ProvinceStringConverter.INSTANCE, provinces.filtered(p -> owned.contains(p.getId()))));
        location.setOnEditCommit(event -> event.getRowValue().setLocation(event.getNewValue().getId()));
        location.setEditable(true);
        location.setStyle("-fx-alignment: CENTER-LEFT");

        TableColumn<Navy, Void> regiments = new TableColumn<>(messageSource.getMessage("country.regiments", null, Constants.LOCALE));
        regiments.setPrefWidth(70);
        regiments.setMinWidth(70);
        regiments.setEditable(false);
        regiments.setCellFactory(
                DialogCellFactory.forTableColumn(
                        navy -> new TableViewDialog<>(country.getSave(), new TableView2Regiment(country, navy.getRegiments(), messageSource, UnitType.navy()),
                                                      messageSource.getMessage("country.regiments", null, Constants.LOCALE),
                                                      list -> new Regiment(country, "Ship " + (navy.getRegiments().size() + 1),
                                                                           UnitType.HEAVY_SHIP,
                                                                           country.getSave()
                                                                                  .getGame()
                                                                                  .getUnit(country.getSubUnit()
                                                                                                  .getSubUnit(UnitType.HEAVY_SHIP)),
                                                                           navy.getRegiments().isEmpty() ? 0d : navy.getRegiments().getFirst().getMorale()),
                                                      navy::getRegiments),
                        (r, navy) -> navy.setRegiments(r)));

        TableColumn<Navy, Void> remove = new TableColumn<>();
        remove.setPrefWidth(48);
        remove.setEditable(false);
        remove.setCellFactory(ClearCellFactory.forTableColumn());

        setPrefWidth(name.getPrefWidth() + location.getPrefWidth() + regiments.getPrefWidth() + remove.getPrefWidth());
        setFixedCellSize(40);
        setEditable(true);

        getColumns().clear();
        getColumns().add(name);
        getColumns().add(location);
        getColumns().add(regiments);
        getColumns().add(remove);
        setItems(navies.stream().map(Navy::new).collect(Collectors.toCollection(FXCollections::observableArrayList)));

        prepare();
    }
}
