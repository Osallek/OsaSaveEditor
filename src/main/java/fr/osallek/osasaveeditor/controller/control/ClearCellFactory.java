package fr.osallek.osasaveeditor.controller.control;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;

import java.util.function.Function;

public class ClearCellFactory<T> extends TableCell<T, Void> {

    public static <T> Callback<TableColumn<T, Void>, TableCell<T, Void>> forTableColumn() {
        return forTableColumn(null);
    }

    public static <T> Callback<TableColumn<T, Void>, TableCell<T, Void>> forTableColumn(Function<T, Boolean> disableSupplier) {
        return ButtonCellFactory.forTableColumn(FontAwesome.Glyph.REMOVE, (cell, actionEvent) -> cell.getTableView().getItems().remove(cell.getIndex()),
                                                disableSupplier);
    }
}
