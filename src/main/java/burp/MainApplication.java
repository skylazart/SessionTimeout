package burp;


import burp.controller.RootController;
import burp.logappender.BurpLogWriterSingleton;
import burp.uifactory.LayoutFactory;
import burp.uifactory.UITypes;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Burp extension - session timeout verifier
 * Created by FSantos@trustwave.com on 3/29/17.
 */
public class MainApplication extends Application {
    private static final Logger logger = LogManager.getLogger();

    private Stage primaryStage;
    private BorderPane rootLayout;
    private AnchorPane mainLayout;

    public static void main(String[] args) {
        BurpLogWriterSingleton.getInstance().setWriter(new PrintWriter(System.out));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Testando");

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Test application");

        try {
            initRootLayout();
            showMainLayout();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showMainLayout() throws IOException {
        LayoutFactory<RootController> layoutFactory = new LayoutFactory<>();
        mainLayout = layoutFactory.makeLayout(UITypes.MAIN_LAYOUT);
        rootLayout.setCenter(mainLayout);
    }

    private void initRootLayout() throws IOException {
        LayoutFactory<RootController> layoutFactory = new LayoutFactory<>();
        rootLayout = layoutFactory.makeLayout(UITypes.ROOT_LAYOUT);

        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
