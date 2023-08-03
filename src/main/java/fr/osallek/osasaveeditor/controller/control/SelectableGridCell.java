package fr.osallek.osasaveeditor.controller.control;

import fr.osallek.eu4parser.common.ImageReader;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.collections.SetChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.PopupWindow;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

public class SelectableGridCell<T> extends GridCell<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelectableGridCell.class);

    private final SelectableGridView<T> gridView;

    private final ColorAdjust notSelectedEffect;

    private ImageView imageView;

    private Tooltip tooltip;

    private Function<T, String> textFunction;

    private Function<T, File> imageFunction;

    private final int size;

    private final File defaultFile;

    private boolean init = false;

    public SelectableGridCell(GridView<T> gridView, Function<T, String> textFunction, Function<T, File> imageFunction, int size, File defaultFile) {
        this.gridView = (SelectableGridView<T>) gridView;
        this.size = size;
        this.defaultFile = defaultFile;
        this.notSelectedEffect = new ColorAdjust();
        this.notSelectedEffect.setSaturation(-1);

        if (imageFunction != null) {
            this.imageFunction = imageFunction;
            this.imageView = new ImageView();
            this.imageView.setPickOnBounds(true);
        }

        if (textFunction != null) {
            this.textFunction = textFunction;
            this.tooltip = new Tooltip();
            this.tooltip.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_TOP_RIGHT);
            Tooltip.install(this, this.tooltip);
        }

        setAlignment(Pos.CENTER);
        updateGridView(gridView);
        setOnMouseClicked(event -> {
            if (MouseButton.PRIMARY.equals(event.getButton())) {
                SelectableGridCell<T> source = ((SelectableGridCell<T>) event.getSource());

                if (this.gridView.isSelected(source.getItem())) {
                    this.gridView.unSelect(source.getItem());
                } else {
                    this.gridView.select(source.getItem());
                }
            }
        });
        this.gridView.getSelectedItems().addListener((SetChangeListener<T>) change -> updateItem(gridView.getItems().get(getIndex()), false));
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty) {
            updateSelected(this.gridView.isSelected(item));
            this.imageView.setEffect(this.gridView.isSelected(item) ? null : this.notSelectedEffect);

            if (!this.init) {
                if (this.tooltip != null) {
                    this.tooltip.setText(this.textFunction.apply(item));
                }

                if (this.imageFunction != null) {
                    try {
                        if (this.imageView.getImage() == null) {
                            BufferedImage image = ImageReader.convertFileToImage(this.imageFunction.apply(item));

                            if (image == null) {
                                image = ImageReader.convertFileToImage(this.defaultFile);
                            }

                            if (image != null) {
                                this.imageView.setImage(OsaSaveEditorUtils.bufferedToView(image).getImage());

                                setGraphic(this.imageView);
                                setMaxWidth(this.size);
                                setMaxHeight(this.size);
                                this.imageView.setFitHeight(this.size);
                                this.imageView.setFitWidth(this.size);
                            }
                        }
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }

                this.init = true;
            }
        }
    }
}
