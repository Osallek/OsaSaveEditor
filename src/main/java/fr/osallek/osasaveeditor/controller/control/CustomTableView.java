package fr.osallek.osasaveeditor.controller.control;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.TableColumnHeader;

import java.lang.reflect.Method;

public class CustomTableView<T> extends TableView<T> {

    private static Method columnToFitMethod;

    static {
        try {
            columnToFitMethod = TableColumnHeader.class.getDeclaredMethod("resizeColumnToFitContent", int.class);
            columnToFitMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
        }
    }

    private DoubleBinding widthBinding;

    protected void prepare() {
        this.widthBinding = Bindings.createDoubleBinding(
                () -> getColumns().stream().mapToDouble(TableColumn::getWidth).sum() + 3 + (getItems().size() > 9 ? 17 : 0),
                getColumns().stream().map(TableColumn::widthProperty).toArray(Observable[]::new));
        this.widthBinding.addListener((observable, oldValue, newValue) -> {
            setWidth(newValue.doubleValue());
            setMinWidth(newValue.doubleValue());
            setMaxWidth(newValue.doubleValue());
        });
    }
}
