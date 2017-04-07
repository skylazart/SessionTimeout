package burp.uifactory;

import java.net.URL;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 3/31/17.
 */
public enum UITypes {
    MAIN_LAYOUT("/views/mainLayout.fxml"),
    ROOT_LAYOUT("/views/rootLayout.fxml"),
    TAB_LAYOUT("/views/tabLayoutNew.fxml");

    private String resourceName;
    private URL resource;

    UITypes(String s) {
        this.resourceName = s;
        resource = UITypes.class.getResource(resourceName);
    }

    public URL getResource() {
        return resource;
    }

    public String toString() {
        return this.resourceName;
    }
}
