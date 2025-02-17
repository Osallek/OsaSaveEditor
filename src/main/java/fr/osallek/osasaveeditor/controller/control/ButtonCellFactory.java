package fr.osallek.osasaveeditor.controller.control;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class ButtonCellFactory<T> extends TableCell<T, Void> {

    public static <T> Callback<TableColumn<T, Void>, TableCell<T, Void>> forTableColumn(FontAwesome.Glyph glyph,
                                                                                        BiConsumer<TableCell<T, Void>, ActionEvent> handler) {
        return list -> new ButtonCellFactory<>(glyph, handler, null);
    }

    public static <T> Callback<TableColumn<T, Void>, TableCell<T, Void>> forTableColumn(FontAwesome.Glyph glyph,
                                                                                        BiConsumer<TableCell<T, Void>, ActionEvent> handler,
                                                                                        Function<T, Boolean> disableSupplier) {
        return list -> new ButtonCellFactory<>(glyph, handler, disableSupplier);
    }

    private final Function<T, Boolean> disableSupplier;

    private final HBox hBox = new HBox();

    private final Button button;

    public ButtonCellFactory(FontAwesome.Glyph glyph, BiConsumer<TableCell<T, Void>, ActionEvent> handler, Function<T, Boolean> disableSupplier) {
        this.button = new Button(String.valueOf(glyph.getChar()));
        this.button.setStyle("-fx-font-family: FontAwesome");
        this.button.setTextAlignment(TextAlignment.CENTER);
        this.button.setOnAction(event -> handler.accept(this, event));
        this.hBox.setAlignment(Pos.CENTER);
        this.hBox.getChildren().add(this.button);
        this.disableSupplier = disableSupplier;
    }

    @Override
    public void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(this.hBox);

            if (this.disableSupplier != null) {
                this.button.disableProperty().setValue(this.disableSupplier.apply(getTableRow().getItem()));
            }
        }
    }
}
