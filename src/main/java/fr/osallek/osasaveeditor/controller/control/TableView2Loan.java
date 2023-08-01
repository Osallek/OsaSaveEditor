package fr.osallek.osasaveeditor.controller.control;

import fr.osallek.eu4parser.model.game.localisation.Eu4Language;
import fr.osallek.eu4parser.model.save.country.SaveCountry;
import fr.osallek.osasaveeditor.common.Constants;
import fr.osallek.osasaveeditor.controller.converter.DoubleStringConverter;
import fr.osallek.osasaveeditor.controller.object.Loan;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class TableView2Loan extends TableView<Loan> {

    public TableView2Loan(SaveCountry country, ObservableList<Loan> loans) {
        TableColumn<Loan, Integer> amount = new TableColumn<>(country.getSave().getGame().getLocalisationClean("LOAN_MONEY", Eu4Language.getByLocale(Constants.LOCALE)));
        amount.setCellValueFactory(p -> p.getValue() == null ? null : new ReadOnlyObjectWrapper<>(p.getValue().getAmount()));
        amount.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        amount.setOnEditCommit(event -> event.getRowValue().setAmount(event.getNewValue()));
        amount.setPrefWidth(150);
        amount.setStyle("-fx-alignment: CENTER-LEFT");

        TableColumn<Loan, Double> interest = new TableColumn<>(country.getSave().getGame().getLocalisationCleanNoPunctuation("EXPENSEINTEREST", Eu4Language.getByLocale(
                Constants.LOCALE)));
        interest.setCellValueFactory(p -> p.getValue() == null ? null : new ReadOnlyObjectWrapper<>(p.getValue().getInterest()));
        interest.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        interest.setOnEditCommit(event -> event.getRowValue().setInterest(event.getNewValue()));
        interest.setPrefWidth(150);
        interest.setStyle("-fx-alignment: CENTER-LEFT");

        TableColumn<Loan, LocalDate> expiryDate = new TableColumn<>(country.getSave().getGame().getLocalisationCleanNoPunctuation("EXPIRES_ON", Eu4Language.getByLocale(Constants.LOCALE)));
        expiryDate.setCellValueFactory(p -> p.getValue() == null ? null : new ReadOnlyObjectWrapper<>(p.getValue().getExpiryDate()));
        expiryDate.setCellFactory(column -> new DatePickerCell<>(country.getSave().getDate(), null));
        expiryDate.setOnEditCommit(event -> event.getRowValue().setExpiryDate(event.getNewValue()));
        expiryDate.setPrefWidth(150);
        expiryDate.setStyle("-fx-alignment: CENTER-LEFT");

        TableColumn<Loan, Void> remove = new TableColumn<>();
        remove.setPrefWidth(48);
        remove.setEditable(false);
        remove.setCellFactory(ClearCellFactory.forTableColumn());

        setFixedCellSize(40);
        setPrefWidth(500);
        setEditable(true);

        getColumns().clear();
        getColumns().add(amount);
        getColumns().add(interest);
        getColumns().add(expiryDate);
        getColumns().add(remove);
        setItems(loans.stream().map(Loan::new).collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }
}
