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
import javafx.scene.control.TextField;
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

public class Downloader extends Application {

	TextField youtubeurl, downloadurl, path ;
	Label Size, fileName;
	ComboBox<String> Select;
	Button Start, Cancel, Browse;
	HBox h1, h2, h3, h4;
	String url, title, Information;
	static String[] arg;
	DirectoryChooser browse;
	String lastpath = System.getProperty("user.dir");
	static Stage newstage = new Stage();
	ArrayList<String> list = new ArrayList<String>();
	
	public Downloader(String u, String t) {
		url = "https://www.youtube.com/watch?v=" + u;
		title = t;
	}

    public void start(Stage stage) {
    	
    	
    	newstage = stage;
    	
    	browse = new DirectoryChooser();

    	h1 = new HBox(); h2 = new HBox(); h3 = new HBox(); h4 = new HBox();
    	youtubeurl = new TextField(url);
    	downloadurl = new TextField("N/A");
    	
    	path = new TextField("C:\\Users\\BILAL AHMAD\\Downloads\\Video");
    	Size = new Label("File Size:  0 Byte"); fileName = new Label(title);
    	fileName.setMaxWidth(525);
    	fileName.setWrapText(true);
    	Size.setTextFill(Color.GREY);
    	fileName.setStyle("-fx-font-family:\"Open Sans\";-fx-font-weight:bold;-fx-font-size:9pt;-fx-background-radius: 6;-fx-background-color:rgb(226, 226, 226);-fx-text-inner-color:white;-fx-padding:3 10 3 10;");
    	Select = new ComboBox<String>();
    	Select.setMinWidth(190);
    	Select.setMaxWidth(190);
    	
    	youtubeurl.setEditable(false);
    	downloadurl.setEditable(false);
    	path.setEditable(false);
    	Start = new Button("Start"); Cancel = new Button("Cancel"); Browse = new Button("  ...  ");
    	Start.setText("Fetch");
    	Start.setMinWidth(90);Cancel.setMinWidth(90);
    	
    	VBox Main = new VBox();
    	VBox V1 = new VBox();
    	VBox V2 = new VBox();
    	
    	V2.getChildren().add(new Label("YouTube URL:"));
    	V2.getChildren().add(new Label("Select type:"));
    	V2.getChildren().add(new Label("Download address:"));
    	V2.getChildren().add(new Label("Download path:"));
    	
    	V1.getChildren().add( youtubeurl );
    	h3.getChildren().add(Select);
    	h3.getChildren().add( Size );
    	h3.setAlignment(Pos.BASELINE_LEFT);
    	h3.setSpacing(20);
    	V1.getChildren().add( h3 );
    	V1.getChildren().add( downloadurl );
    	HBox Path = new HBox();
    	Path.getChildren().add(path);
    	Path.getChildren().add(Browse);
    	Path.setSpacing(8);
    	HBox.setHgrow(path, Priority.ALWAYS);
    	V1.getChildren().add( Path );
    	V1.setMinWidth(400);
    	
    	
    	
    	V1.setSpacing(10);
    	V2.setSpacing(20);
    	
    	h1.getChildren().add(V2);
    	h1.getChildren().add(V1);
    	h1.setSpacing(20);
    	
    	h4.getChildren().add( Start );
    	h4.getChildren().add( Cancel );
    	h4.setAlignment(Pos.BASELINE_RIGHT);
    	h4.setSpacing(10);
    	
    	V1.styleProperty().bind(Bindings.concat("-fx-font-family:\"Open Sans Semibold\";-fx-font-size:9pt"));
    	V2.styleProperty().bind(Bindings.concat("-fx-font-family:\"Open Sans Semibold\";-fx-font-size:9pt"));
    	h1.styleProperty().bind(Bindings.concat("-fx-font-family:\"Open Sans Semibold\";-fx-font-size:9pt"));
    	h4.styleProperty().bind(Bindings.concat("-fx-font-family:\"Open Sans Semibold\";-fx-font-size:9pt"));
    	
    	
    	Main.getChildren().add(fileName);
    	Main.getChildren().add(h1);
    	Main.getChildren().add(h4);
//    	h1.setVisible(false);
    	
    	
    	
    	Main.setSpacing(12);
    	Main.setPadding(new Insets(20, 10, 10, 20));
    	
    	Select.valueProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				try {
					Call( String.valueOf( Select.getSelectionModel().getSelectedIndex() ));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}	
        });
    	
       	Browse.setOnAction(new EventHandler<ActionEvent>(){
    		public void handle(final ActionEvent e) {
    			String showd = showdialog();
    			if (!showd.equals(null))
    				path.setText( showd );
    		}
    	});
    	Start.setOnAction(new EventHandler<ActionEvent>(){
    		public void handle(final ActionEvent e) {
    			try {
					Started();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
    		}
    	});
    	Cancel.setOnAction(new EventHandler<ActionEvent>(){
    		public void handle(final ActionEvent e) {
    			Cancel();
    		}
    	});
    	
        Scene scene = new Scene(Main);
        
        Cancel.setCancelButton(true);
        Start.setDefaultButton(true);
        ON(true);

        stage.setMaximized(true);
        stage.setTitle("Downloader");
        stage.setMaximized(false);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public void Started() throws Exception {
    	if (Start.getText().equals("Fetch")) {
    		Task<Void> task = new Task<Void>() {		
    			public void run() {
    				try {
						Information =  new PythonCaller().Execute(url) ;
						Platform.runLater(new Runnable() {
	                        @Override
	                        public void run() {
	                        	try {
	                        		Complete();	
	                        	} catch (Exception e) { }
	                        }});
					} catch (Exception e) { }
    			}
				protected Void call() throws Exception {
					return null;
				}
    		};
    		new Thread(task).start();
    		Start.setDisable(true);
    		
    	}
    	else if (Start.getText().equals("Download")) {
//    		(String NAME, String URL, long SIZE, String PATH)
    		if ( Select.getSelectionModel().getSelectedIndex() != -1 ) {
	    		JSONParser parser = new JSONParser();
	    		JSONObject ob = (JSONObject)  parser.parse(Information);
	    		JSONObject array = (JSONObject) ob.get( String.valueOf( Select.getSelectionModel().getSelectedIndex() ));
	    		IDM d = new IDM(fileName.getText(), (String)array.get("url"), Long.parseLong( (String) array.get("size") ), path.getText());
	    		newstage.close();
	    		d.start(IDM.newtage);
				
    		}
    		
    	}
    	
    }
    
    public void Cancel() {
    	newstage.close();
    }
    
    public void Call(String c) throws Exception {
		JSONParser parser = new JSONParser();
		JSONObject ob = (JSONObject)  parser.parse(Information);
		JSONObject array = (JSONObject) ob.get( String.valueOf(c) );
    	this.downloadurl.setText( (String) array.get("url") );
    	this.Size.setText("File Size:  " + Youtube.Size( Long.parseLong( (String) array.get("size") )));
    }
    
    public void Complete() throws Exception {
    	Start.setMinWidth(100);
    	ON(false);
    	Start.setDisable(false);
    	Start.setText("Download");
//    	{\"0\" : {\"type\" : \"a\", \"rate\" : \"x128k\", \"ext\" : \"m4a\", \"size\" : \"2597387\", \"url\" : 
		JSONParser parser = new JSONParser();
		JSONObject ob = (JSONObject)  parser.parse(Information);
		for (int i = 0; i < ob.size(); i++ ) {
			JSONObject array = (JSONObject) ob.get( String.valueOf(i) );
			if ( array.get("type").equals("a" )) {
				Select.getItems().add("Extract audio only [" + ((String) array.get("rate")).split("x")[1] + "]");
			} else if (array.get("type").equals("v")) {
				Select.getItems().add( array.get("ext").toString().toUpperCase() + "  " + array.get("rate").toString().split("@")[0]);
				
			}
		}
    }
    public void ON(boolean t) {
    	Browse.setDisable(t);
    	Select.setDisable(t);
    	
    }
    
    public String showdialog() {
    	String ret = null;
    	try {
    	browse.setInitialDirectory(new File(lastpath));
    	browse.setTitle("Select Download Path");
    	
    	lastpath = browse.showDialog(newstage).getAbsolutePath();
    	System.out.println( lastpath );
    	} catch (Exception e) {
    		lastpath = null;
    	}
    	if (lastpath.equals(null)) {
    		ret = null;
    	} else {
    		ret = lastpath;
    	}
    	return ret;
    	
    }
}