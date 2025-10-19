package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.ScatterChart;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    public static boolean atCli=true;
    public CliController cliController;
    public ModelViewController modelViewController;
    public viewAllController viewAllController;
    public Scene cli, model, view;
    public static Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader cliLoader = new FXMLLoader(MainApplication.class.getResource("cliView.fxml"));
        FXMLLoader modelLoader = new FXMLLoader(MainApplication.class.getResource("modelView.fxml"));
        FXMLLoader viewAllLoader= new FXMLLoader(MainApplication.class.getResource("viewAll.fxml"));

        Parent cliView=cliLoader.load();
        Parent modelView=modelLoader.load();
        Parent viewAll=viewAllLoader.load();

        cliController=cliLoader.getController();
        modelViewController=modelLoader.getController();
        viewAllController=viewAllLoader.getController();

        cliController.setMainApp(this);
        modelViewController.setMainApp(this);

        cli = new Scene(cliView, 1080, 720);
        model = new Scene(modelView, 1080, 720);
        view = new Scene(viewAll, 1080, 720);
        MainApplication.stage = stage;
        stage.setTitle("CLI VIEW");
        stage.setFullScreen(true);
        stage.setScene(cli);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void switchScene(String destination) {
        if (destination.equals("model")) {
            stage.setScene(model);
            stage.setFullScreen(true);
        }
        else if (destination.equals("view")) {
            stage.setScene(view);
            stage.setFullScreen(true);
        }
        else if (destination.equals("cli")) {
            stage.setScene(cli);
            stage.setFullScreen(true);
        }
    }
}