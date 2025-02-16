package fr.osallek.osasaveeditor.controller.control;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.TableColumnHeader;

import java.lang.reflect.Method;
import java.util.Objects;

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
        this.widthBinding = Bindings.createDoubleBinding(() -> getColumns().stream().mapToDouble(TableColumn::getWidth).sum() + 2,
                                                         getColumns().stream().map(TableColumn::widthProperty).toArray(Observable[]::new));
        this.widthBinding.addListener((observable, oldValue, newValue) -> {
            setWidth(newValue.doubleValue());
            setMinWidth(newValue.doubleValue());
            setMaxWidth(newValue.doubleValue());
        });
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        skinProperty().addListener((observable, oldValue, newValue) -> {
            if (!Objects.equals(oldValue, newValue) && newValue != null) {
                for (TableColumn<T, ?> column : getColumns()) {
                    TableColumnHeader header = (TableColumnHeader) column.getStyleableNode();
                    try {
                        columnToFitMethod.invoke(header, -1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
