package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.clausewitzparser.common.ClausewitzUtils;
import fr.osallek.eu4parser.model.save.province.SaveProvince;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ProvinceStringCellFactory implements Callback<ListView<SaveProvince>, ListCell<SaveProvince>> {

    @Override
    public ListCell<SaveProvince> call(ListView<SaveProvince> param) {
        return new ListCell<>() {

            @Override
            protected void updateItem(SaveProvince value, boolean empty) {
                super.updateItem(value, empty);
                setText(value == null ? null : ClausewitzUtils.removeQuotes(value.getName()));
            }
        };
    }
}
