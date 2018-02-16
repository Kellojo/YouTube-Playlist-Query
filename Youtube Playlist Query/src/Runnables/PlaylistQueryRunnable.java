package Runnables;
import Youtube.Playlist;
import Youtube.Video;
import Youtube.YouTubeAPIKey;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class PlaylistQueryRunnable extends Task<Object> {
	
	private YouTubeAPIKey APIKey;
	private Playlist playlist;
	private ObservableList<Video> listView;
	private boolean isFinished = false;
	private boolean fillPlaylistAfterCompletion = false;
	
	public PlaylistQueryRunnable(YouTubeAPIKey APIKey, Playlist playlist, ObservableList<Video> lv_ov, boolean fillPlaylistAfterCompletion) {
		this.APIKey = APIKey;
		this.playlist = playlist;
		this.listView = lv_ov;
		this.fillPlaylistAfterCompletion = fillPlaylistAfterCompletion;
	}

	public boolean isFinished() {
		return isFinished;
	}

	@Override
	protected Object call() throws Exception {
		updateMessage("Running playlist query");
		updateProgress(1, 4);
		
		playlist.QueryPlaylistData(APIKey);
		isFinished = true;
		
		updateMessage("Finished playlist query.");
		updateProgress(4, 4);
		
		if (fillPlaylistAfterCompletion) {
			PlaylistListViewFillerRunnable plvfr = new PlaylistListViewFillerRunnable(playlist, listView);
			Thread t = new Thread(plvfr);
			t.start();
		}
		
		return null;
	}
}
