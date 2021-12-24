import java.io.*;

import java.net.*;
import java.nio.charset.Charset;
import java.text.*;
import java.util.*;
import javafx.application.*;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class Youtube extends Application {

	StackPane contentPane;
	ComboBox<String> let = new ComboBox<>();
	TextField search, path;
	Label results, logo, type, size, resume, status, ERROR = new Label("404 NOT FOUND");
	Button Search, Browse;
	String Query = "http://suggestqueries.google.com/complete/search?client=firefox&ds=yt&q=--",
			
			Ids = "https://www.googleapis.com/youtube/v3/search?part=id,snippet&q=%s&type=video&key=AIzaSyB-0Oyydd0NFDACLBaFTLGSAUFlK6fX3BE&maxResults=%d",
			Info = "https://www.googleapis.com/youtube/v3/videos?part=statistics,contentDetails&id=%s&key=AIzaSyB-0Oyydd0NFDACLBaFTLGSAUFlK6fX3BE";
	VBox main;
	HBox H1, H2;
	URL req = null;
	int over = 0;
	long TimeStart, TotalResults;
	HashMap<String, HashMap> Record = new HashMap<String, HashMap>();
	ArrayList<Thread> RunningThreads = new ArrayList<Thread>();

	public void start(Stage stage) {

		let.getItems().addAll("A", "B", "C", "D", "E");
		let.setEditable(true);
		let.getEditor().focusedProperty().addListener(observable -> {
			if (let.getSelectionModel().getSelectedIndex() < 0) {
				let.getEditor().setText(null);
			}
		});
		ERROR.setFont(Font.font("Roboto Medium", FontWeight.BLACK, 40));

		MenuBar menuBar = new MenuBar();
		// --- Menu File
		Menu menuFile = new Menu("File");
		Menu menuEdit = new Menu("Edit");
		Menu menuView = new Menu("View");
		Menu menuAbout = new Menu("Help");
		MenuItem Bilal = new MenuItem("About Developer");
		Bilal.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(final ActionEvent e) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("Developer Information");
				alert.getDialogPane().setContentText(
						"This program is developed by:\n\t\tBILAL AHMAD\n\t\t17-TE-21, Alpha Section\n\nSubmitted to:\n\t\tSir Jameel Khan\n\n\tTELECOMMUNICATION ENGINEERING DEPARTMENT");
				alert.showAndWait();
			}
		});
		menuAbout.getItems().add(Bilal);

		menuBar.getMenus().addAll(menuFile, menuEdit, menuView, menuAbout);

		logo = new Label();
		Image image = new Image(getClass().getResourceAsStream("youtube.png"));
		ImageView Image = new ImageView(image);
		Image.setFitHeight(24);
		Image.setFitWidth(140);
		Image.setPreserveRatio(true);
		logo.setGraphic(Image);

		search = new TextField();
		search.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

		Search = new Button();
		Image image1 = new Image(getClass().getResourceAsStream("search.png"));

		ImageView Image1 = new ImageView(image1);
		Search.setGraphic(Image1);
		Search.setMinWidth(70);
		search.setPromptText("Search Youtube");
		search.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					if (!(search.getText().replace(" ", "").equals(""))) {
						Fetch();
					}

				}
			}
		});
		Search.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(final ActionEvent e) {
				if (!(search.getText().replace(" ", "").equals(""))) {
					System.out.println("Pressed");
					Fetch();
				}
			}
		});

		results = new Label();

		H1 = new HBox();
		H1.getChildren().add(logo);
		H1.getChildren().add(new Label());
		H1.getChildren().add(search);
		HBox.setHgrow(search, Priority.ALWAYS);
		H1.getChildren().add(Search);
		H1.setSpacing(5);
		H1.setAlignment(Pos.CENTER_LEFT);
		H1.setPadding(new Insets(20, 10, 20, 10));

		H2 = new HBox();
		H2.getChildren().add(results);
		Label n = new Label("...");

		H2.getChildren().add(n);
		HBox.setHgrow(n, Priority.ALWAYS);
		H2.setSpacing(5);

		main = new VBox();
		main.setSpacing(10);

		ScrollPane scrollPane = new ScrollPane(main);

		main.setPadding(new Insets(5, 0, 10, 10));
		scrollPane.setHmax(300);
		status = new Label();
		status.setPadding(new Insets(4, 0, 4, 10));
		VBox Main = new VBox();
		Main.getChildren().addAll(menuBar);
		Main.getChildren().add(H1);
		Main.getChildren().add(scrollPane);

		Main.styleProperty().bind(Bindings.concat("-fx-font-family:\"Roboto Medium\";-fx-font-size:10pt"));
		Main.setSpacing(2);
		VBox.setVgrow(scrollPane, Priority.ALWAYS);

		Main.getChildren().add(status);

		Scene scene = new Scene(Main);
		stage.setMaximized(true);
		stage.setTitle("YOUTUBE");
		stage.setScene(scene);
		stage.show();
	}

	public static String Size(long size) {
		String hrSize = null;
		double b = size;
		double k = size / 1024.0;
		double m = ((size / 1024.0) / 1024.0);
		double g = (((size / 1024.0) / 1024.0) / 1024.0);
		double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);
		DecimalFormat dec = new DecimalFormat("0.00");
		if (t >= 1)
			hrSize = dec.format(t).concat(" TB");
		else if (g >= 1)
			hrSize = dec.format(g).concat(" GB");
		else if (m >= 1)
			hrSize = dec.format(m).concat(" MB");
		else if (k >= 1)
			hrSize = dec.format(k).concat(" kB");
		else
			hrSize = b + " Bytes";
		return hrSize;
	}

	public static String Short(String get) {
		String ret = "";
		String att = "";
		DecimalFormat df2 = new DecimalFormat("0.00");
		DecimalFormat df1 = new DecimalFormat("0.0");
		boolean err = false;
		if (get.length() > 9) {
			ret = "" + df2.format((Long.parseLong(get) / 1000000000.));
			att = "B";
		} else if (get.length() > 6) {
			ret = "" + df1.format((Long.parseLong(get) / 1000000.));
			att = "M";
		} else if (get.length() > 3) {
			ret = "" + df1.format((Long.parseLong(get) / 1000.));
			att = "K";
		} else
			ret = get;
		return (ret + att);

	}

	public void setERROR(String err) {
		ERROR.setText(err);
		// ERROR
	}

	public static String getDate(String get) {
		String Date = get.split("T")[0];
		String Time = get.split("T")[1].split(".")[0];
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()));
		return get;
	}

	public String read(String pass) {
		final int BUFFER_SIZE = 1024 * 8;
		final int ZERO = 0;
		final byte[] dataBuffer = new byte[BUFFER_SIZE];
		final StringBuilder sb = new StringBuilder();
		while (true) {
			try {
				URL urlObject = new URL(pass);
				HttpURLConnection huc = (HttpURLConnection) urlObject.openConnection();
				HttpURLConnection.setFollowRedirects(false);
				huc.setRequestMethod("GET");
				huc.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
				huc.connect();
				final BufferedInputStream in = new BufferedInputStream(huc.getInputStream());

				int bytesRead = ZERO;
				while ((bytesRead = in.read(dataBuffer, ZERO, BUFFER_SIZE)) >= ZERO) {
					sb.append(new String(dataBuffer, ZERO, bytesRead));
				}
				break;
			} catch (Exception e) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						try {
							status.setText("No Internet Connection!");
						} catch (Exception e) {
						}
					}
				});
			}
		}
		return sb.toString();
	}

	public void Requestforinfo(String id, String live) {
		Task<Void> task = new Task<Void>() {
			public void run() {
				try {
					JSONParser parser = new JSONParser();
					JSONObject ob = (JSONObject) parser.parse(read(Info.replace("%s", id)));
					JSONArray array = (JSONArray) ob.get("items");

					JSONObject y1 = (JSONObject) array.get(0);
					JSONObject y2 = (JSONObject) y1.get("statistics");
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (!(live.equals("live"))) {
								String views = y2.get("viewCount").toString();
								Label v_p = ((ArrayList<Label>) (Record.get(id).get("1"))).get(3);

								Label c = ((ArrayList<Label>) (Record.get(id).get("1"))).get(6);
								String com = "";
								try {
									String comments = y2.get("commentCount").toString();
									com = Short(comments) + ":"
											+ NumberFormat.getIntegerInstance().format(Long.parseLong(comments));
								} catch (Exception e) {
									com = " COMMENTS:HIDDEN";
								}
								c.setText(" " + com.split(":")[0]);
								c.setTooltip(new Tooltip("Comments: " + com.split(":")[1]));

								Label l = ((ArrayList<Label>) (Record.get(id).get("1"))).get(4);
								String lik = "";
								try {
									String likes = y2.get("likeCount").toString();
									lik = Short(likes) + ":"
											+ NumberFormat.getIntegerInstance().format(Long.parseLong(likes));
								} catch (Exception e) {
									lik = " LIKES:HIDDEN";
								}
								l.setText(" " + lik.split(":")[0]);
								l.setTooltip(new Tooltip("Likes: " + lik.split(":")[1]));

								Label d = ((ArrayList<Label>) (Record.get(id).get("1"))).get(5);
								String dis = "";
								try {
									String dislikes = y2.get("dislikeCount").toString();
									dis = Short(dislikes) + ":"
											+ NumberFormat.getIntegerInstance().format(Long.parseLong(dislikes));
								} catch (Exception e) {
									dis = " DISLIKES:HIDDEN";
								}
								d.setText(" " + dis.split(":")[0]);
								d.setTooltip(new Tooltip("Dislikes: " + dis.split(":")[1]));

								String date = ((ArrayList<String>) (Record.get(id).get("2"))).get(3);

								v_p.setText(Short(views) + " views  |  " + date);
								v_p.setTooltip(new Tooltip(
										"views: " + NumberFormat.getIntegerInstance().format(Long.parseLong(views))
												+ "\nPublished on: " + date));

							} else {
								Label v_p = ((ArrayList<Label>) (Record.get(id).get("1"))).get(3);
								String views = y2.get("viewCount").toString();
								v_p.setText(Short(views) + " views  |  LIVE NOW");
								v_p.setTooltip(new Tooltip(
										"views: " + NumberFormat.getIntegerInstance().format(Long.parseLong(views))));

								Label c = ((ArrayList<Label>) (Record.get(id).get("1"))).get(6);
								String com = "";
								try {
									String comments = y2.get("commentCount").toString();
									com = Short(comments) + ":"
											+ NumberFormat.getIntegerInstance().format(Long.parseLong(comments));
								} catch (Exception e) {
									com = " COMMENTS:HIDDEN";
								}
								c.setText(" " + com.split(":")[0]);
								c.setTooltip(new Tooltip("Comments: " + com.split(":")[1]));

								Label l = ((ArrayList<Label>) (Record.get(id).get("1"))).get(4);
								String lik = "";
								try {
									String likes = y2.get("likeCount").toString();
									lik = Short(likes) + ":"
											+ NumberFormat.getIntegerInstance().format(Long.parseLong(likes));
								} catch (Exception e) {
									lik = " LIKES:HIDDEN";
								}
								l.setText(" " + lik.split(":")[0]);
								l.setTooltip(new Tooltip("Likes: " + lik.split(":")[1]));

								Label d = ((ArrayList<Label>) (Record.get(id).get("1"))).get(5);
								String dis = "";
								try {
									String dislikes = y2.get("dislikeCount").toString();
									dis = Short(dislikes) + ":"
											+ NumberFormat.getIntegerInstance().format(Long.parseLong(dislikes));
								} catch (Exception e) {
									dis = " DISLIKES:HIDDEN";
								}
								d.setText(" " + dis.split(":")[0]);
								d.setTooltip(new Tooltip("Dislikes: " + dis.split(":")[1]));
							}

							TotalResults = 1 + TotalResults;
							float end = (float) ((System.currentTimeMillis() - TimeStart) / 1000.);
							status.setText("Total " + TotalResults + " results in "
									+ (end < 1 ? (end + "m sec") : (end + " sec")));

							if (TotalResults == 10)
								System.out.println("10");

							// main.clearConstraints(main.getParent().getChildrenUnmodifiable().get(1));

						}
					});

				} catch (Exception e) {
					System.out.println("Error line no. :" + e.getStackTrace()[0].getLineNumber());
				}
			}

			protected Void call() throws Exception {
				return null;
			}
		};
		Thread sd = new Thread(task);
		sd.start();
		RunningThreads.add(sd);
	}

	public void Requestforthumb(String url) {
		Task<Void> task = new Task<Void>() {
			public void run() {
				try {
					Image load = new Image("https://img.youtube.com/vi/" + url + "/mqdefault.jpg");
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							try {

								Label v_p = ((ArrayList<Label>) (Record.get(url).get("1"))).get(0);

								// v_p.setText("12:00:01");
								// v_p.setTextOverrun(null);
								ImageView Like = new ImageView(load);
								Like.setFitHeight(300);
								Like.setFitWidth(230);
								Like.setPreserveRatio(true);
								v_p.setGraphic(Like);

							} catch (Exception e) {
							}
						}
					});
				} catch (Exception e) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							try {
								status.setText("Error in requesting thumbnail for youtube.com/watch?v=");
							} catch (Exception e) {
							}
						}
					});
				}
			}

			protected Void call() throws Exception {
				return null;
			}
		};
		Thread sd = new Thread(task);
		sd.start();
		RunningThreads.add(sd);

	}

	public void Fetch() {
		System.out.println("I'm running...");
		for (int c = 0; c < RunningThreads.size(); c++) {
			try {
				RunningThreads.get(c).interrupt();
				RunningThreads.remove(c);
			} catch (Exception e) {
			}
		}
		TimeStart = System.currentTimeMillis();
		TotalResults = 0;
		results.setText("Request generated for \"" + search.getText() + "\" ");
		Task<Void> task = new Task<Void>() {
			public void run() {
				try {
					String ret = read(
							Ids.replace("%s", URLEncoder.encode(search.getText(), "UTF-8")).replace("%d", "10"));
					JSONParser parser = new JSONParser();

					JSONObject ob = (JSONObject) parser.parse(ret);
					ret = null;
					JSONArray array = (JSONArray) ob.get("items");

					JSONObject result = (JSONObject) ob.get("pageInfo");
					for (over = 0; over < Integer.parseInt(result.get("resultsPerPage").toString()); over++) {
						String title = ((JSONObject) ((JSONObject) array.get(over)).get("snippet")).get("title")
								.toString();
						String url = ((JSONObject) ((JSONObject) array.get(over)).get("id")).get("videoId").toString();
						String channel = ((JSONObject) ((JSONObject) array.get(over)).get("snippet"))
								.get("channelTitle").toString();

						String description = ((JSONObject) ((JSONObject) array.get(over)).get("snippet"))
								.get("description").toString();
						String isLive = ((JSONObject) ((JSONObject) array.get(over)).get("snippet"))
								.get("liveBroadcastContent").toString();
						String date = ((JSONObject) ((JSONObject) array.get(over)).get("snippet")).get("publishedAt")
								.toString();

						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								try {

									String retValue = new String(title.getBytes(), Charset.forName("Windows-1252"));
									String convertValue = new String(retValue.getBytes("Windows-1252"),
											Charset.forName("UTF-8"));
									Label s = new Label();
									Label t = new Label(convertValue);
									Hyperlink tt = new Hyperlink(convertValue);
									tt.setCursor(Cursor.HAND);
									tt.setOnAction(new EventHandler<ActionEvent>() {
										public void handle(final ActionEvent e) {
											Downloader d = new Downloader(url, convertValue);
											// Downloader.newstage.initModality(Modality.APPLICATION_MODAL);
											d.start(Downloader.newstage);

											// d.initModality(Modality.WINDOW_MODAL);
											// d.initOwner(Downloader.newstage);
										}
									});
									t.setMaxWidth(600);
									t.setFont(Font.font("Roboto Medium", FontWeight.BOLD, 14));
									t.setWrapText(true);
									t.setPadding(new Insets(0, 0, 3, 0));
									Label v_p = new Label();
									Label c = new Label(channel);
									c.styleProperty().bind(Bindings.concat(
											"-fx-background-radius: 6;-fx-background-color:grey;-fx-text-inner-color:white;-fx-padding:0 8 0 8;"));
									c.setTextFill(Color.WHITE);

									ImageView Image = new ImageView(
											new Image(getClass().getResourceAsStream("default.jpg")));
									Image.setFitHeight(300);
									Image.setFitWidth(230);
									Image.setPreserveRatio(true);
									s.setGraphic(Image);
									s.setCursor(Cursor.HAND);
									ContextMenu contextMenu = new ContextMenu();
									MenuItem cut = new MenuItem("Copy Image");
									MenuItem copy = new MenuItem("Save");
									MenuItem paste = new MenuItem("Save Image as");
									MenuItem pate = new MenuItem("Copy Image address");
									contextMenu.getItems().addAll(cut, copy, paste, pate);
									s.setContextMenu(contextMenu);

									// s.getParent().getChildrenUnmodifiable().remove(s) ;

									HBox hori = new HBox();

									HBox det = new HBox();
									Label like = new Label(" LIKE");
									ImageView Like = new ImageView(
											new Image(getClass().getResourceAsStream("like.png")));
									Like.setFitHeight(14);
									Like.setFitWidth(14);
									Like.setPreserveRatio(true);
									like.setGraphic(Like);
									Label dislike = new Label(" DISLIKES");

									ImageView disLike = new ImageView(
											new Image(getClass().getResourceAsStream("dislike.png")));
									disLike.setFitHeight(14);
									disLike.setFitWidth(14);
									disLike.setPreserveRatio(true);
									dislike.setGraphic(disLike);

									Label comment = new Label(" COMMENTS");
									ImageView Comment = new ImageView(
											new Image(getClass().getResourceAsStream("comment.png")));
									Comment.setFitHeight(15);
									Comment.setFitWidth(15);
									Comment.setPreserveRatio(true);
									comment.setGraphic(Comment);

									det.getChildren().add(like);
									det.getChildren().add(dislike);
									det.getChildren().add(comment);
									det.setSpacing(20);
									det.setAlignment(Pos.CENTER_LEFT);

									hori.setSpacing(10);
									VBox vert = new VBox();

									Label ds = new Label();
									ds.setFont(Font.font("Arial", FontWeight.LIGHT, 10));
									ds.setText(description);

									ds.setMaxWidth(600);
									ds.setWrapText(true);

									vert.getChildren().add(tt);
									vert.getChildren().add(c);
									vert.getChildren().add(v_p);
									vert.getChildren().add(det);
									vert.getChildren().add(ds);
									vert.setSpacing(3);

									hori.getChildren().add(s);
									hori.getChildren().add(vert);
									ArrayList<Label> subsubRecord = new ArrayList<Label>();
									subsubRecord.add(s); // Thumbnail
									subsubRecord.add(t); // Video Title
									subsubRecord.add(c); // Channel Title
									subsubRecord.add(v_p); // Published Date || Views
									subsubRecord.add(like); // Likes
									subsubRecord.add(dislike); // Dislikes
									subsubRecord.add(comment); // Comments

									ArrayList<String> subsubRecord1 = new ArrayList<String>();
									subsubRecord1.add(title);
									subsubRecord1.add(channel);
									subsubRecord1.add(description);
									subsubRecord1.add(date);

									ArrayList<HBox> HLayouts = new ArrayList<HBox>();
									HLayouts.add(hori);

									ArrayList<VBox> VLayouts = new ArrayList<VBox>();
									VLayouts.add(vert);

									HashMap<String, ArrayList> subRecord = new HashMap<String, ArrayList>();
									subRecord.put("1", subsubRecord);
									subRecord.put("2", subsubRecord1);
									subRecord.put("3", HLayouts);
									subRecord.put("4", VLayouts);

									// d.launch(d, "");

									Record.put(url, subRecord);
									Requestforinfo(url, isLive);

									// hori.setStyle("-fx-padding: 4;" + "-fx-border-style: solid inside;"
									// + "-fx-border-width: 1;"
									// + "-fx-border-radius: 5;" + "-fx-border-color: silver;");

									main.getChildren().add(hori);

									Requestforthumb(url);

								} catch (Exception e) {
								}
							}
						});
					}
				} catch (Exception e) {
					status.setText("Error");
				}
			}

			protected Void call() throws Exception {
				return null;
			}
		};
		Thread sd = new Thread(task);
		sd.start();
		RunningThreads.add(sd);

	}

	public void Refresh() {
		// subRecord.put("1", subsubRecord);
		// subRecord.put("2", subsubRecord1);
		// subRecord.put("3", HLayouts);
		// subRecord.put("4", VLayouts);

		// ((HBox) vert.getParent()).getChildren().remove(vert);
		// for (HashMap iter : Record.values()) {
		((VBox) main.getParent().getChildrenUnmodifiable().get(1).getParent()).getChildren()
				.remove(main.getParent().getChildrenUnmodifiable().get(1));

		// ( (HBox) ((HBox)iter.get("3")).getParent()
		// ).getChildren().remove(iter.get("3"));

		// }
		Record = null;

	}

	public void oneTime() {

	}

	public static void main(String[] args) {
		launch(args);
	}

}
