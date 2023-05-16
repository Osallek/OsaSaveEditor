package fr.osallek.osasaveeditor.controller.converter;

import fr.osallek.eu4parser.model.save.country.SaveCountry;
import fr.osallek.osasaveeditor.common.OsaSaveEditorUtils;
import javafx.util.StringConverter;

public class GovernmentRankConverter extends StringConverter<Integer> {

    private SaveCountry country;

    public SaveCountry getCountry() {
        return country;
    }

    public void setCountry(SaveCountry country) {
        this.country = country;
    }

    @Override
    public String toString(Integer rank) {
        return rank == null ? "" : OsaSaveEditorUtils.localize(this.country.getGovernmentName().getRank(rank), this.country.getSave().getGame());
    }

    @Override
    public Integer fromString(String s) {
        return null;
    }
}
