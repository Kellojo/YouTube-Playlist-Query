import java.io.File;
import java.util.List;

import Utility.FileWriter;
import Youtube.Playlist;
import Youtube.Video;
import javafx.concurrent.Task;

public class MusicRenamer extends Task<Object> {
	
	private Playlist playlist;
	
	public MusicRenamer(Playlist playlist) {
		this.playlist = playlist;
	}
	
	/* Add playlist index to the files */
	private void renameFiles() {	
		List<Video> videos = playlist.getVideos();
		int leadingZeros = Integer.toString(videos.size()).toString().length();
		
		for (Video video : videos) {
			
			File fileOnDisk = video.getVideoOnDisk();
			//System.out.println((fileOnDisk == null) + " - " + video.getVideoOnDisk());
			
			if (fileOnDisk == null) {
				continue;
			}
			
			//Some funky code to make sure we get the right extension even when we dont have one or have a lot of dots in out extension
			String extension = FileWriter.getFileExtension(fileOnDisk);
			if (extension.trim().equals("") || extension.length() > 10)
				extension = "mp3";
			extension = extension.replace(".", "");
			extension = "." + extension;
			
			
			String finalName = String.format("%0" + leadingZeros + "d", video.getPositionInPlaylist()) + " - " + video.getTitle() + extension;
			fileOnDisk.renameTo(new File(fileOnDisk.getParentFile().getAbsolutePath() + "\\" + finalName));
		}
		
	}

	@Override
	protected Object call() throws Exception {
		renameFiles();
		return null;
	}
	
	
	
	
	
}
