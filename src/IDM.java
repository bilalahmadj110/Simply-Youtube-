import java.io.BufferedInputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
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
import javafx.scene.control.ProgressBar;
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

public class IDM extends Application {

	Label fileSize, fileName, Status, Downloaded, transferRate, timeLeft, resumeCapability;
	Button Start, Cancel;
	ProgressBar bar;
	String name,  url, path;
	long size;
	double p;
	static Stage newtage = new Stage();

	public IDM(String NAME, String URL, long SIZE, String PATH) {
		name = NAME; size = SIZE; url = URL; path = PATH;
	}
	
	@Override
    public void start(Stage stage) {
    	
    	Start = new Button("Start");
    	Cancel = new Button("Cancel");
    	Start.setMinWidth(90);
    	Cancel.setMinWidth(90);
    	
    	fileName = new Label(name);
    	fileName.setMaxWidth(580);
    	fileName.setWrapText(true);
    	fileName.setStyle("-fx-font-family:\"Open Sans Semibold\";-fx-font-weight:bold;-fx-font-size:9pt;-fx-background-radius: 6;-fx-background-color:rgb(226, 226, 226);-fx-text-inner-color:white;-fx-padding:3 10 3 10;");
    	
    	Status = new Label("NEW");
    	
    	fileSize = new Label( Youtube.Size(size) );
    	Downloaded = new Label("not started yet");
    	transferRate = new Label("0 Bytes/sec");
    	timeLeft = new Label("(unknown)");
    	resumeCapability = new Label("unknown");
    	bar = new ProgressBar(0);
    	bar.setProgress(0);
    	bar.setMinWidth(580);
    	
    	
    	VBox Main = new VBox();
    	Main.setStyle("-fx-font-family:\"Open Sans Semibold\";");
    	Main.setSpacing(10);
    	Main.setPadding(new Insets(15, 10, 10, 15));
    	VBox h = new VBox();
    	VBox hh = new VBox();
    	HBox h1 = new HBox();
    	HBox h2 = new HBox();
    	h2.getChildren().add(Start);
    	h2.getChildren().add(Cancel);
    	h2.setAlignment(Pos.BASELINE_RIGHT);
    	h2.setSpacing(10);
    	
    	Main.getChildren().add(fileName);
    	
    	
    	h1.getChildren().add(h);
    	h1.getChildren().add(hh);
    	h1.setSpacing(20);
    	
    	
    	Main.getChildren().add(h1);
    	Main.getChildren().add(bar);
    	Main.getChildren().add(h2);
    	
    	
    	h.getChildren().add(new Label("Status: "));
    	h.getChildren().add(new Label("File Size: "));
    	h.getChildren().add(new Label("Downloaded: "));
    	h.getChildren().add(new Label("Tranfer rate: "));
    	h.getChildren().add(new Label("Time left: "));
    	h.getChildren().add(new Label("Resume capability: "));
    	h.setSpacing(4);
    	
    	hh.getChildren().add( Status );
    	hh.getChildren().add( fileSize );
    	hh.getChildren().add( Downloaded );
    	hh.getChildren().add( transferRate );
    	hh.getChildren().add( timeLeft );
    	hh.getChildren().add( resumeCapability );
    	hh.setSpacing(4);
    	newtage = stage;
    	
    	Start.setOnAction(new EventHandler<ActionEvent>(){
    		public void handle(final ActionEvent e) {
    			Pressed();
    		}
    	});
    	Cancel.setOnAction(new EventHandler<ActionEvent>(){
    		public void handle(final ActionEvent e) {
    			newtage.close();
    		}
    	});
    	
    	
    	
    	
    	Scene scene = new Scene(Main);
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.setTitle("Downloader");
        stage.setMaximized(false);
        stage.setScene(scene);
        stage.show();
    }
    
    public void Pressed() {
//		Task<Void> task = new Task<Void>() {		
//			public void run() {
				try {
					final int BUFFER_SIZE = 1024 * 8;
			        final int ZERO = 0;
			        final byte[] dataBuffer = new byte[BUFFER_SIZE];
			        final StringBuilder sb = new StringBuilder();
		        	URL urlObject = new URL(url);
		            HttpURLConnection huc = (HttpURLConnection) urlObject.openConnection();
		            HttpURLConnection.setFollowRedirects(false);
		            huc.setRequestMethod("GET");
		            huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
		            huc.connect();
		            System.out.println(Youtube.Size( huc.getContentLengthLong() ));
				} catch (Exception e) {
					System.out.println("Something bad happened on our end.");
				}
    }
}
//		            final BufferedInputStream in = new BufferedInputStream( huc.getInputStream() );
//		        	
//		            long bytesRead = ZERO;
//		            long read = 0;
//		            while ((bytesRead = in.read(dataBuffer, ZERO, BUFFER_SIZE)) >= ZERO) {
//		                read += bytesRead;
//		                p = (read * 10) / size;
//			        	Platform.runLater(new Runnable() {
//		                    @Override
//		                    public void run() {
//		                    	try {
//		                    		bar.setProgress( p );
//		                    		System.out.println(p);
//		                    	}
//		                    	catch(Exception e) {}
//		                    	}});
//		            }
//		            System.out.println("Completed");
//		        } catch(Exception e) {	}
//			}
//			@Override
//			protected Void call() throws Exception {
//				return null;
//			}
//		};
//		Thread sd = new Thread(task) ;
//		sd.start();
//    }
