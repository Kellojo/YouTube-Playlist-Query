import java.util.ArrayList;
import java.util.List;

import Utility.Format;
import javafx.application.Application;
import javafx.stage.Stage;

public class YoutubePlaylistQuery extends Application {
	
	public static final String author = "Kellojo";
	public static final String appName = "YouTube Playlist Query";
	public static final String version = "v3.1";
	public static final String defaultFont = "Roboto";
	public static final int appWidth = 700;
	public static final int appHeight = 400;
	
	public static final Format[] renameFormats = {
			new Format("%PIND - %T", "'Playlist Index' - 'Title'"),
			new Format("%T - %PIND", "'Title' - 'Playlist Index'"),
			new Format("%ID", "'Video id'"),
			new Format("%PIND - %ID", "'Playlist Index' - 'Video id'"),
			new Format("%ID - %CID", "'Video id' - 'Chanel id'"),
			new Format("%PIND - %ID - %CID", "'Playlist Index' - 'Video id' - 'Chanel id'")
	};
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		new GUI(author, appName, defaultFont, version, appWidth, appHeight, renameFormats);
	}
}
