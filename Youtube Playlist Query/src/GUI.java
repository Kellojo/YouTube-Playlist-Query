import java.io.File;
import java.util.List;

import Runnables.PlaylistListViewFillerRunnable;
import Runnables.PlaylistQueryRunnable;
import Utility.Format;
import Utility.GuiUtility;
import Utility.VideoListCell;
import Youtube.Playlist;
import Youtube.Video;
import Youtube.YouTubeAPIKey;
import Youtube.YoutubeUtility;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GUI {

	public static final String author = "Kellojo";
	public static final String appName = "YouTube Playlist Query";
	public static final String defaultFont = "Roboto";
	public static final String version = "v1.1";
	public static final int appWidth = 700;
	public static final int appHeight = 400;

	public static final Insets insets_trbl = new Insets(10, 10, 10, 10);
	public static final Insets insets_half_trbl = new Insets(5, 5, 5, 5);

	private Stage primaryStage;
	private String path = "";
	private YouTubeAPIKey APIKey;
	private Playlist currentPlaylist;

	private Label lbl_heading;
	private ListView<Video> lv_videos;
	private TextField tf_PlaylistID;
	private TextField tf_APIKey;
	private ComboBox<Format> cb_FileFormat;

	private Button btn_ExtractVideos;
	private Button btn_renameVideos;
	private Button btn_ExtractMissingVideos;

	private ProgressBar pb_status;
	private boolean isInProgress = false;


	public GUI() {
		primaryStage = new Stage();

		BorderPane bp = buildGUI();
		primaryStage.setTitle(appName + " by " + author + " (" + version + ")");
		primaryStage.setScene(new Scene( bp, appWidth, appHeight));
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("Save.png")));
		primaryStage.show();
	}

	private BorderPane buildGUI() {
		BorderPane bp = new BorderPane();

		/* Heading */
		lbl_heading = new Label(appName);
		lbl_heading.setFont(new Font(defaultFont, 26));
		lbl_heading.setPadding(insets_trbl);
		bp.setTop(lbl_heading);


		/* Center */
		HBox center = new HBox();
		center.setPadding(insets_trbl);
		bp.setCenter(center);

			/* Left Pane */
			GridPane gp = new GridPane();
			gp.setMaxWidth(600);
			gp.setMinWidth(300);
			HBox.setHgrow(gp, Priority.ALWAYS);
			center.getChildren().add(gp);


			Label lbl_PlaylistID = new Label("Playlist ID:");
			GridPane.setHgrow(lbl_PlaylistID, Priority.SOMETIMES);
			gp.add(lbl_PlaylistID, 0, 0);

			tf_PlaylistID = new TextField();
			tf_PlaylistID.setTooltip(new Tooltip("The id of the YouTube playlist you want to query."));
			tf_PlaylistID.setOnKeyReleased(new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent arg0) {
					validateInput(tf_PlaylistID, tf_APIKey);
				}
			});
			GridPane.setColumnSpan(tf_PlaylistID, 2);
			GridPane.setHgrow(tf_PlaylistID, Priority.ALWAYS);
			tf_PlaylistID.setPadding(insets_half_trbl);
			gp.add(tf_PlaylistID, 1, 0);

			Label lbl_APIKey = new Label("YouTube API Key:");
			GridPane.setHgrow(lbl_APIKey, Priority.SOMETIMES);
			gp.add(lbl_APIKey, 0, 1);

			tf_APIKey = new TextField("AIzaSyDSs6VwJdOUUyoSYN4ZZcd0PlzsHobqfDE");
			tf_APIKey.setTooltip(new Tooltip("The api key for the YouTube api v3. If you don't have one, just stick with the default api key. However if to many people use this key at the same time it may be temporarily disabled by YouTube."));
			tf_APIKey.setOnKeyReleased(new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent arg0) {
					validateInput(tf_PlaylistID, tf_APIKey);
				}
			});
			GridPane.setColumnSpan(tf_APIKey, 2);
			GridPane.setHgrow(tf_APIKey, Priority.ALWAYS);
			tf_APIKey.setPadding(insets_half_trbl);
			gp.add(tf_APIKey, 1, 1);

			Label lbl_Path = new Label("Path:");
			lbl_Path.setTooltip(new Tooltip("The path to save the ids or look for already downloaded videos."));
			gp.add(lbl_Path, 0, 2);

			Label lbl_curPath = new Label("No Directory selected");
			GridPane.setHgrow(lbl_curPath, Priority.ALWAYS);
			gp.add(lbl_curPath, 1, 2);

			Button btn_Path = new Button("...");
			btn_Path.setTooltip(new Tooltip("The path to save the ids or look for already downloaded videos."));
			gp.add(btn_Path, 2, 2);
			btn_Path.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (event.getEventType().toString().equals("ACTION")) {
						DirectoryChooser directoryChooser = new DirectoryChooser();
		                File selectedDirectory = directoryChooser.showDialog(primaryStage);

		                if(selectedDirectory == null && path.trim().equals("")){
		                	lbl_curPath.setText("No Directory selected");
		                	path = "";
		                }else{
		                	lbl_curPath.setText(selectedDirectory.getAbsolutePath());
		                	path = selectedDirectory.getAbsolutePath() + "\\";
		                	tryVideoFileQuery();
		                }
					}
				}
			});

			Label lbl_FileFormat = new Label("Rename format: ");
			GridPane.setHgrow( lbl_FileFormat, Priority.ALWAYS);
			gp.add( lbl_FileFormat, 0, 3);

			cb_FileFormat = new ComboBox<Format>();
			cb_FileFormat.setTooltip(new Tooltip("Select the format that fits your needs. This format is applied to the files when you click the 'Rename Videos in Folder (adds Playlist Index)' button."));
			cb_FileFormat.getItems().addAll(
				new Format("%PIND - %T", "'Playlist Index' - 'Title'"),
				new Format("%T - %PIND", "'Title' - 'Playlist Index'"),
				new Format("%ID", "'Video id'"),
				new Format("%PIND - %ID", "'Playlist Index' - 'Video id'"),
				new Format("%ID - %CID", "'Video id' - 'Chanel id'"),
				new Format("%PIND - %ID - %CID", "'Playlist Index' - 'Video id' - 'Chanel id'")
			);

			cb_FileFormat.getSelectionModel().select(0);
			GridPane.setColumnSpan(cb_FileFormat, 2);
			GuiUtility.SetNodeToFillGridPaneHorizontally( cb_FileFormat);
			gp.add(cb_FileFormat, 1, 3);

			btn_ExtractVideos = new Button("Save Video IDs");
			btn_ExtractVideos.setTooltip(new Tooltip("Save all video ids in this playlist to the selected path."));
			btn_ExtractVideos.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (event.getEventType().toString().equals("ACTION")) {
						showVideoIDPopup();
					}
				}
			});
			GuiUtility.SetNodeToFillGridPaneHorizontally( btn_ExtractVideos);
			gp.add(btn_ExtractVideos, 0, 4);

			btn_ExtractMissingVideos = new Button("Save Missing Video IDs");
			btn_ExtractMissingVideos.setTooltip(new Tooltip("Save all video ids in this playlist to the selected path that are not already downloaded to the selected path."));
			btn_ExtractMissingVideos.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (event.getEventType().toString().equals("ACTION")) {
						showMissingVideoPopup();
					}
				}
			});
			GuiUtility.SetNodeToFillGridPaneHorizontally( btn_ExtractMissingVideos);
			gp.add(btn_ExtractMissingVideos, 0, 5);

			btn_renameVideos = new Button("Rename Videos in Folder (adds Playlist Index)");
			btn_renameVideos.setTooltip(new Tooltip("Rename all videos in the given path to this format: '*playlist index* - *video title*', that are in the given playlist. In order for this to work you have to select a path where videos of this playlist are located in."));
			btn_renameVideos.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (event.getEventType().toString().equals("ACTION")) {
						tryMusicRenamerStart();
					}
				}
			});
			GuiUtility.SetNodeToFillGridPaneHorizontally(btn_renameVideos);
			gp.add(btn_renameVideos, 0, 6);


			pb_status = new ProgressBar();
			GuiUtility.SetNodeToFillGridPaneHorizontally(pb_status);
			gp.add(pb_status, 0, 7);


			GuiUtility.AddMarginToAllGridPaneChildren(gp, insets_half_trbl);




			/* Right Pane */
			lv_videos = new ListView<Video>();
			lv_videos.setTooltip(new Tooltip("All videos in the playlist are shown here. This includes their icon, title and path to the file on your computer, if they are downloaded to your computer and the selected path is the path where they are located."));
			lv_videos.setCellFactory(param -> new VideoListCell());
			lv_videos.setMaxWidth(Double.MAX_VALUE);
			lv_videos.setMinWidth(300);
			HBox.setHgrow( lv_videos, Priority.ALWAYS);
			center.getChildren().add(lv_videos);

		startUpdateTimeline();
		return bp;
	}


	/* Tries to start the query if all params are correctly given/not empty */
	private void tryMusicRenamerStart() {
		if (currentPlaylist == null || isInProgress) {
			return;
		}
			MusicRenamer mr = new MusicRenamer(currentPlaylist, cb_FileFormat.getValue());
			isInProgress = true;

			pb_status.progressProperty().bind(mr.progressProperty());
			pb_status.progressProperty().unbind();
			pb_status.setProgress(0);
			mr.setOnSucceeded(e -> {
				pb_status.progressProperty().unbind();
				pb_status.setProgress(1);
				pb_status.setDisable(false);
				isInProgress = false;
				tryVideoFileQuery();
			});

			Thread t = new Thread(mr);
			t.start();
	}
	

	/* Show the popup with the missing video ids */
	private void showMissingVideoPopup() {
		List<String> missingVideos = YoutubeUtility.getMissingVideosOnDisk(currentPlaylist);
		new TextPopup(primaryStage, missingVideos, currentPlaylist, "Missing Videos");
	}
	/* Show the popup with the missing video ids */
	private void showVideoIDPopup() {
		List<String> allVideos = YoutubeUtility.getVideoIDs(currentPlaylist);
		new TextPopup(primaryStage, allVideos, currentPlaylist, "All Video IDs");
	}
	
	/* tries to query the selected directory on disk for videos in the selected playlist */
	private void tryVideoFileQuery() {
		if (APIKey == null || currentPlaylist == null || isInProgress) {
			return;
		}
		if (path.trim().equals("") || !APIKey.isValid() || !currentPlaylist.isValidated()) {
			return;
		}

		File dir = new File(path);
		currentPlaylist.FindVideosOnDisk(dir);

		//update list view to show the found paths too
		lv_videos.getItems().clear();
		PlaylistListViewFillerRunnable plwfr = new PlaylistListViewFillerRunnable( currentPlaylist, lv_videos.getItems());
		isInProgress = true;

		pb_status.progressProperty().bind(plwfr.progressProperty());
		pb_status.progressProperty().unbind();
		pb_status.setProgress(0);
		plwfr.setOnSucceeded(e -> {
			pb_status.progressProperty().unbind();
			pb_status.setProgress(1);
			pb_status.setDisable(false);
			isInProgress = false;
		});

		Thread t = new Thread(plwfr);
		t.start();
	}
	/* Fills the listView with the videos of the given playlist/api key */
	private void FillListViewWithVideos(Playlist playlist, YouTubeAPIKey APIKey) {
		if (!playlist.isValidated())
			return;

		lv_videos.getItems().clear();
		PlaylistQueryRunnable pqr = new PlaylistQueryRunnable(APIKey, playlist, lv_videos.getItems(), true);
		isInProgress = true;

		pb_status.progressProperty().bind(pqr.progressProperty());
		pb_status.progressProperty().unbind();
		pb_status.setProgress(0);
		pqr.setOnSucceeded(e -> {
			pb_status.progressProperty().unbind();
			pb_status.setProgress(1);
			pb_status.setDisable(false);
			isInProgress = false;
		});

		Thread t = new Thread(pqr);
		t.start();
	}


	/* Validates the playlistID input */
	private void validateInput(TextField tf_PlaylistID, TextField tf_APIKey) {

		//validate api key
		YouTubeAPIKey apiKey = new YouTubeAPIKey(tf_APIKey.getText());
		if (!apiKey.isValid()) {
			return;
		}
		APIKey = apiKey;

		//validate playlist
		String playlistID = tf_PlaylistID.getText().trim();
		if (playlistID.length() < 33) {
			lv_videos.getItems().clear();
			currentPlaylist = null;
			return;
		}

		//fill playlist list view if we have a valid playlist.
		Playlist playlist = new Playlist(tf_PlaylistID.getText());
		playlist.isValidPlaylist(APIKey);
		if (playlist.isValidated()) {
			currentPlaylist = playlist;
			FillListViewWithVideos(playlist, APIKey);
		}
	}


	/* Applies the progress and text to the status indicators */
	private void startUpdateTimeline() {
		Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
				refreshUI();
		    }
		}));
		fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
		fiveSecondsWonder.play();
	}
	/* Refreshes the ui to show the info of the current playlist and displays loading info while fetching playlist data*/
	private void refreshUI() {
		if (currentPlaylist != null) {
			if (currentPlaylist.getTitle().equals("")) {
				lbl_heading.setText("Loading... (" + currentPlaylist.getVideos().size() + " Videos)");
			} else {
				lbl_heading.setText(currentPlaylist.getTitle() + " (" + currentPlaylist.getVideos().size() + " Videos)");
			}
		} else {
			lbl_heading.setText(appName);
		}

		//we got a valid playlist and a valid path or are currently proceccing something. Allow the buttons to be pressable!
		SetDisableForActions(currentPlaylist == null || path.trim().equals("") || isInProgress);
		btn_ExtractVideos.setDisable(currentPlaylist == null || isInProgress);
	}
	/* Sets the disabled property for all buttons that pefrorm a certain action that should not be done in paralell */
	private void SetDisableForActions(boolean isDisabled) {
		btn_renameVideos.setDisable(isDisabled);
		btn_ExtractMissingVideos.setDisable(isDisabled);
	}
}
