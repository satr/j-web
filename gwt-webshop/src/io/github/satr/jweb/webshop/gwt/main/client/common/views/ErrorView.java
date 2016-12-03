package io.github.satr.jweb.webshop.gwt.main.client.common.views;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;

import java.util.List;

public class ErrorView extends Composite {
    @UiField
    public VerticalPanel verticalPanel;
    final ListDataProvider<String> dataProvider = new ListDataProvider<String>();

    interface ErrorViewUiBinder extends UiBinder<HTMLPanel, ErrorView> {
    }

    private static ErrorViewUiBinder ourUiBinder = GWT.create(ErrorViewUiBinder.class);

    public ErrorView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        CellList<String> cellList = new CellList<>(new TextCell());
        dataProvider.addDataDisplay(cellList);
        verticalPanel.add(cellList);
    }

    public void setVerticalPanel(List<String> errors) {
        List<String> list = dataProvider.getList();
        for(String item: errors)
            list.add(item);
    }
}