/**
 * Copyright (c) 2013, 2016 ControlsFX All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. * Redistributions in binary form
 * must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. * Neither the name of ControlsFX, any associated website, nor the names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL CONTROLSFX BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package fr.osallek.osasaveeditor.controller.pane;

import fr.osallek.osasaveeditor.controller.pane.CustomPropertySheet.Item;
import fr.osallek.osasaveeditor.controller.pane.CustomPropertySheet.Mode;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.PopupWindow;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.property.editor.PropertyEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CustomPropertySheetSkin extends SkinBase<CustomPropertySheet> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomPropertySheetSkin.class);
    private static final int MIN_COLUMN_WIDTH = 100;
    private final BorderPane content;
    private final ScrollPane scroller;
    private final ToolBar toolbar;
    private final SegmentedButton modeButton = ActionUtils.createSegmentedButton(new ActionChangeMode(Mode.NAME), new ActionChangeMode(Mode.CATEGORY));
    private final TextField searchField = TextFields.createClearableTextField();
    private Accordion accordion;

    private Map<TitledPane, List<Item<?>>> panes;

    public CustomPropertySheetSkin(final CustomPropertySheet control) {
        super(control);

        this.scroller = new ScrollPane();
        this.scroller.setFitToWidth(true);

        this.toolbar = new ToolBar();
        this.toolbar.managedProperty().bind(this.toolbar.visibleProperty());
        this.toolbar.setFocusTraversable(true);

        // property sheet mode
        this.modeButton.managedProperty().bind(this.modeButton.visibleProperty());
        this.modeButton.getButtons().get(getSkinnable().modeProperty().get().ordinal()).setSelected(true);
        this.toolbar.getItems().add(this.modeButton);

        // property sheet search
        this.searchField.setMinWidth(0);
        HBox.setHgrow(this.searchField, Priority.SOMETIMES);
        this.searchField.managedProperty().bind(this.searchField.visibleProperty());
        this.toolbar.getItems().add(this.searchField);

        // layout controls
        this.content = new BorderPane();
        this.content.setTop(this.toolbar);
        this.content.setCenter(this.scroller);
        getChildren().add(this.content);


        // setup listeners
        registerChangeListener(control.modeProperty(), e -> refreshProperties());
        registerChangeListener(control.propertyEditorFactory(), e -> refreshProperties());
        registerChangeListener(control.titleFilter(), e -> refreshProperties());
        registerChangeListener(this.searchField.textProperty(), e -> getSkinnable().setTitleFilter(this.searchField.getText()));
        registerChangeListener(control.modeSwitcherVisibleProperty(), e -> updateToolbar());
        registerChangeListener(control.searchBoxVisibleProperty(), e -> updateToolbar());
        registerChangeListener(control.categoryComparatorProperty(), e -> refreshProperties());

        control.getItems().addListener((ListChangeListener<CustomPropertySheet.Item>) change -> refreshProperties());

        // initialize properly
        refreshProperties();
        updateToolbar();
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        content.resizeRelocate(x, y, w, h);
    }

    private void updateToolbar() {
        this.modeButton.setVisible(getSkinnable().isModeSwitcherVisible());
        this.searchField.setVisible(getSkinnable().isSearchBoxVisible());
        this.toolbar.setVisible(this.modeButton.isVisible() || this.searchField.isVisible());
    }

    private void refreshProperties() {
        this.scroller.setContent(buildPropertySheetContainer());
    }

    private Node buildPropertySheetContainer() {
        switch (getSkinnable().modeProperty().get()) {
            case CATEGORY: {
                Map<String, List<Item<? extends Object>>> categoryMap = getSkinnable().getItems()
                                                                                      .stream()
                                                                                      .collect(Collectors.groupingBy(Item::category,
                                                                                                                     () -> getSkinnable().getCategoryComparator()
                                                                                                                           == null ? new LinkedHashMap<>() :
                                                                                                                           new TreeMap<>(
                                                                                                                                   getSkinnable().getCategoryComparator()),
                                                                                                                     Collectors.toList()));
                // create category-based accordion
                this.accordion = new Accordion();
                this.panes = new LinkedHashMap<>();
                for (Map.Entry<String, List<Item<?>>> entry : categoryMap.entrySet()) {
                    PropertyPane props = new PropertyPane(entry.getValue());

                    if (!props.getChildrenUnmodifiable().isEmpty()) {
                        TitledPane pane = new TitledPane(entry.getKey(), props);
                        pane.setExpanded(true);
                        pane.managedProperty().bind(pane.visibleProperty());
                        this.accordion.getPanes().add(pane);
                        this.panes.put(pane, entry.getValue());
                    }
                }

                if (!this.accordion.getPanes().isEmpty()) {
                    this.accordion.setExpandedPane(this.accordion.getPanes().get(0));
                }

                return this.accordion;
            }

            default:
                return new PropertyPane(getSkinnable().getItems());
        }

    }

    private class ActionChangeMode extends Action {

        public ActionChangeMode(CustomPropertySheet.Mode mode) {
            super(""); //$NON-NLS-1$
            setEventHandler(ae -> getSkinnable().modeProperty().set(mode));

            //            if (mode == Mode.CATEGORY) {
            //                setGraphic(new ImageView(CATEGORY_IMAGE));
            //                setLongText(localize(asKey("property.sheet.group.mode.bycategory"))); //$NON-NLS-1$
            //            } else if (mode == Mode.NAME) {
            //                setGraphic(new ImageView(NAME_IMAGE));
            //                setLongText(localize(asKey("property.sheet.group.mode.byname"))); //$NON-NLS-1$
            //            } else {
            setText("???"); //$NON-NLS-1$
            //            }
        }

    }

    public void updateAccordionVisibility() {
        this.accordion.getPanes()
                      .setAll(this.panes.entrySet()
                                        .stream()
                                        .filter(e -> e.getValue().stream().map(Item::isVisible).anyMatch(ObservableBooleanValue::get))
                                        .map(Map.Entry::getKey)
                                        .toList());
    }

    private class PropertyPane extends VBox {

        public PropertyPane(List<Item<? extends Object>> properties) {
            this(properties, 0);
        }

        public PropertyPane(List<Item<? extends Object>> properties, int nestingLevel) {
            setSpacing(5);
            setAlignment(Pos.CENTER_LEFT);
            setPadding(new Insets(5, 15, 5, 15 + nestingLevel * 10d));
            getStyleClass().add("property-pane");
            setItems(properties);
            //            setGridLinesVisible(true);
        }

        public void setItems(List<Item<? extends Object>> properties) {
            getChildren().clear();

            String filter = getSkinnable().titleFilter().get();
            filter = filter == null ? "" : filter.trim().toLowerCase();

            for (Item<? extends Object> item : properties) {
                String title = item.name();

                if (!filter.isEmpty() && (title == null || !title.toLowerCase().contains(filter))) {
                    continue;
                }

                Node editor = getEditor(item);

                if (!Objects.equals(editor.visibleProperty(), item.isVisible())) {
                    editor.visibleProperty().bind(item.isVisible());
                }

                if (editor instanceof Region region) {
                    region.setMinWidth(MIN_COLUMN_WIDTH);
                    region.setMaxWidth(Double.MAX_VALUE);
                }

                HBox hBox = new HBox(5);
                hBox.setAlignment(Pos.CENTER_LEFT);
                if (title != null && !title.isEmpty()) {
                    Label label = new Label(title);
                    label.setMinWidth(MIN_COLUMN_WIDTH);
                    label.visibleProperty().bind(item.isVisible());

                    // show description as a tooltip
                    String description = item.description();
                    if (StringUtils.isNotBlank(description)) {
                        Tooltip tooltip = new Tooltip(description);
                        tooltip.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_TOP_RIGHT);
                        label.setTooltip(tooltip);
                    }

                    label.setLabelFor(editor);
                    hBox.getChildren().addAll(label, editor);
                } else {
                    hBox.getChildren().add(editor);
                }

                hBox.visibleProperty().bind(item.isVisible());
                hBox.managedProperty().bind(hBox.visibleProperty());
                HBox.setHgrow(editor, Priority.SOMETIMES);
                getChildren().add(hBox);
            }

        }

        @SuppressWarnings("unchecked")
        private <T> Node getEditor(Item<T> item) {
            @SuppressWarnings("rawtypes")
            PropertyEditor editor = getSkinnable().getPropertyEditorFactory().call(item);

            if (editor == null) {
                editor = new AbstractPropertyEditor<>(item, new TextField()) {
                    {
                        getEditor().setEditable(false);
                        getEditor().setDisable(true);
                    }

                    /**
                     * {@inheritDoc}
                     */
                    @Override
                    protected ObservableValue<T> getObservableValue() {
                        return (ObservableValue<T>) getEditor().textProperty();
                    }

                    /**
                     * {@inheritDoc}
                     */
                    @Override
                    public void setValue(Object value) {
                        getEditor().setText(value == null ? "" : value.toString()); //$NON-NLS-1$
                    }
                };
            } else {
                editor.getEditor().disableProperty().bind(item.isEditable().not());
            }

            editor.setValue(item.getValue());
            return editor.getEditor();
        }
    }

    public BorderPane getContent() {
        return content;
    }

    public ScrollPane getScroller() {
        return scroller;
    }

    public ToolBar getToolbar() {
        return toolbar;
    }

    public SegmentedButton getModeButton() {
        return modeButton;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public Accordion getAccordion() {
        return accordion;
    }
}
