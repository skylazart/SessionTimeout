package burp.uifactory;

import javafx.fxml.FXMLLoader;

import java.io.IOException;


/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 3/31/17.
 */
public class LayoutFactory<C> {
    private C controller;

    public LayoutFactory() {

    }

    public <T> T makeLayout(UITypes uiType) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setClassLoader(LayoutFactory.class.getClassLoader());
        loader.setLocation(uiType.getResource());
        T ui = loader.load();
        controller = loader.getController();
        return ui;
    }

    public C getController() {
        return controller;
    }
}
