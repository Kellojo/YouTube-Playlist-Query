package Runnables;

import java.util.List;

import Youtube.Playlist;
import Youtube.Video;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class PlaylistListViewFillerRunnable extends Task<Object> {
	
	private Playlist playlist;
	private boolean isFinished = false;
	private ObservableList<Video> listView;
	
	public PlaylistListViewFillerRunnable(Playlist playlist, ObservableList<Video> lv_ov) {
		this.playlist = playlist;
		this.listView = lv_ov;
	}

	public boolean isFinished() {
		return isFinished;
	}

	@Override
	protected Object call() throws Exception {
		updateMessage("Filling listView...");
		List<Video> videos = playlist.getVideos();
		
		int i = 0;
		int videoCount = videos.size();
		
		for (Video v : videos) {
			try {
				listView.add(v);
			} catch(IllegalStateException e) {
				//catching Exception in thread "Thread-6" java.lang.IllegalStateException: Not on FX application thread; currentThread = Thread-6
			}
			
			i++;
			updateMessage(i + "/" + videoCount);
			updateProgress(i, videoCount);
		}
		isFinished = true;
		updateMessage("Done");
		
		return null;
	}
}
