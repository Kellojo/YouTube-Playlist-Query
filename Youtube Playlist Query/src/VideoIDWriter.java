import java.util.ArrayList;
import java.util.List;

import Utility.FileWriter;
import Youtube.Playlist;
import Youtube.Video;
import javafx.concurrent.Task;

public class VideoIDWriter extends Task<Object> {
	
	private String savePath = "";
	private Playlist playlist;
	
	
	public VideoIDWriter(Playlist playlist, String savePath) {
		this.savePath = savePath;
		this.playlist = playlist;
	}
	
	@Override
	protected Object call() throws Exception {
		if (playlist == null)
			return null;
		
		List<Video> videos = playlist.getVideos();
		List<String> allVideoIDs = new ArrayList<String>();
		
		int i = 0;
		int videoCount = videos.size();
		
		for (Video v : videos) {
			allVideoIDs.add(v.getVideoID());
			
			i++;
			updateMessage("Writing " + i + "/" + videoCount);
			updateProgress(i, videoCount);
		}
		
		FileWriter.writeLinesToFile(allVideoIDs, savePath, "Videos in " + playlist.getTitle() + ".txt");
		
		return null;
	}
}
