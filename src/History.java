import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class History extends Application {
	static Stage newstage = new Stage();
	private TableView table = new TableView();

    public void start(Stage stage) {
        TableColumn no = new TableColumn("  #  ");
        TableColumn query = new TableColumn("Query");
        TableColumn down = new TableColumn("Data downloaded");
        TableColumn comp = new TableColumn("Time to complete");
        TableColumn date = new TableColumn("Date");
        TableColumn select = new TableColumn("Select");
        
        
        
        table.getColumns().addAll(no, query, down, comp, date, select);
    	newstage = stage;
    	
    	ScrollPane scrollPane = new ScrollPane(table);
    	VBox Main = new VBox(scrollPane);
    	Main.setVgrow(scrollPane, Priority.ALWAYS);
        Scene scene = new Scene(Main);
        stage.setMaximized(true);
        stage.setTitle("Downloader");
//        stage.setMaximized(false);
        stage.setScene(scene);
//        stage.setResizable(false);
        stage.show();
    }
    public static void main(String[] args) {
    	launch(args);
    }
}