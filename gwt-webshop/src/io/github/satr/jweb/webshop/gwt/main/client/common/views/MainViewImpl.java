package io.github.satr.jweb.webshop.gwt.main.client.common.views;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.MainView;
import io.github.satr.jweb.webshop.gwt.main.client.common.interfaces.ComponentView;

public class MainViewImpl implements MainView {

    private final RootPanel menuPanel;
    private final RootPanel mainPanel;

    public MainViewImpl() {
        RootPanel.get("headerSlot").add(new HeaderView());
        RootPanel.get("footerSlot").add(new FooterView());
        menuPanel = RootPanel.get("menuSlot");
        mainPanel = RootPanel.get("mainSlot");
    }

    @Override
    public void show(ComponentView view) {
        mainPanel.clear();
        mainPanel.add((Widget) view);
    }

    @Override
    public void addMenu(ComponentView view) {
        menuPanel.add((Widget)view);
    }
}
