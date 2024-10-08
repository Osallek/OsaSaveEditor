package fr.osallek.osasaveeditor.controller.control;

import fr.osallek.eu4parser.model.game.localisation.Eu4Language;
import fr.osallek.eu4parser.model.save.Save;
import fr.osallek.osasaveeditor.common.Constants;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import fr.osallek.osasaveeditor.controller.object.StringDate;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class TableView2StringDate extends TableView<StringDate> {

    public TableView2StringDate(Save save, ObservableList<StringDate> stringDates, boolean dateEditable, LocalDate startDate, LocalDate endDate) {
        TableColumn<StringDate, String> name = new TableColumn<>(save.getGame().getLocalisationClean("NAME", Eu4Language.getByLocale(Constants.LOCALE)));
        name.setCellValueFactory(p -> p.getValue() == null ? null :
                                      new ReadOnlyObjectWrapper<>(OsaSaveEditorUtils.localize(p.getValue().getName(), save.getGame())));
        name.setEditable(false);
        name.setPrefWidth(450);
        name.setStyle("-fx-alignment: CENTER-LEFT");

        TableColumn<StringDate, LocalDate> date = new TableColumn<>(save.getGame()
                                                                        .getLocalisationCleanNoPunctuation("FE_STARTING_DATE", Eu4Language.getByLocale(Constants.LOCALE)));
        date.setCellValueFactory(p -> p.getValue() == null ? null : new ReadOnlyObjectWrapper<>(p.getValue().getDate()));
        date.setCellFactory(DatePickerCell.forTableColumn(startDate, endDate));
        date.setOnEditCommit(event -> event.getRowValue().setDate(event.getNewValue()));
        date.setPrefWidth(150);
        date.setStyle("-fx-alignment: CENTER-LEFT");
        date.setEditable(dateEditable);

        TableColumn<StringDate, Void> remove = new TableColumn<>();
        remove.setPrefWidth(48);
        remove.setEditable(false);
        remove.setCellFactory(ClearCellFactory.forTableColumn());

        setFixedCellSize(40);
        setPrefWidth(663);
        setEditable(true);

        getColumns().clear();
        getColumns().add(name);
        getColumns().add(date);
        getColumns().add(remove);
        setItems(stringDates.stream().map(StringDate::new).collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }
}
