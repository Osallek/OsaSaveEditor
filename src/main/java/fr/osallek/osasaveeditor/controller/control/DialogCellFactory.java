package fr.osallek.osasaveeditor.controller.control;

import javafx.scene.control.Dialog;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class DialogCellFactory<T, U> extends TableCell<T, Void> {

    public static <T, U> Callback<TableColumn<T, Void>, TableCell<T, Void>> forTableColumn(Function<T, Dialog<List<U>>> dialogSupplier,
                                                                                           BiConsumer<List<U>, T> consumer) {
        return forTableColumn(null, dialogSupplier, consumer);
    }

    public static <T, U> Callback<TableColumn<T, Void>, TableCell<T, Void>> forTableColumn(Function<T, Boolean> disableSupplier,
                                                                                           Function<T, Dialog<List<U>>> dialogSupplier,
                                                                                           BiConsumer<List<U>, T> consumer) {
        return ButtonCellFactory.forTableColumn(FontAwesome.Glyph.EDIT, (cell, actionEvent) -> {
            dialogSupplier.apply(cell.getTableRow().getItem()).showAndWait().ifPresent(us -> consumer.accept(us, cell.getTableRow().getItem()));
        }, disableSupplier);
    }
}
