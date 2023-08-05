package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.save.country.SaveCountry;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class GovernmentRankCellFactory implements Callback<ListView<Integer>, ListCell<Integer>> {

    private SaveCountry country;

    public SaveCountry getCountry() {
        return country;
    }

    public void setCountry(SaveCountry country) {
        this.country = country;
    }

    @Override
    public ListCell<Integer> call(ListView<Integer> param) {
        return new ListCell<>() {

            @Override
            protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);

                if (!empty) {
                    setText(value == null ? null : OsaSaveEditorUtils.localize(country.getGovernmentName().getRank(value), country.getSave().getGame()));
                }
            }
        };
    }
}
