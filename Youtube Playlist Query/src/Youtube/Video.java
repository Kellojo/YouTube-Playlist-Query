package Youtube;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Video {
	private String videoID = "none";
	private String title = "none";
	private String channelId = "none";
	private String publishedAt = "none";
	private int positionInPlaylist;
	private String previewImage_default = "";
	
	private File videoOnDisk;
	
	public Video(String videoID, String title, String channelId, String publishedAt, int positionInPlaylist) {
		this.videoID = videoID;
		this.title = title;
		this.channelId = channelId;
		this.publishedAt = publishedAt;
		this.positionInPlaylist = positionInPlaylist;
	}
	public Video(String videoID, String title, String channelId, String publishedAt, int positionInPlaylist, String previewImage_default) {
		this.videoID = videoID;
		this.title = title;
		this.channelId = channelId;
		this.publishedAt = publishedAt;
		this.positionInPlaylist = positionInPlaylist;
		this.previewImage_default = previewImage_default;
	}
	public Video(String videoID) {
		this.videoID = videoID;
	}
	
	/* Finds the video on disk given a directory to search in */
	public void FindVideoOnDisk(File directory) {
		List<File> directoryContents = Arrays.asList(directory.listFiles());
		
		videoOnDisk = null;
		for (File file : directoryContents) {
			
			String fileName = file.getName().toLowerCase();
			String vTitle = title.toLowerCase();

			if (fileName.contains(vTitle) || file.getName().contains(videoID)) {
				this.videoOnDisk = file;
				return;
			}
		}
	}
	
	public String getVideoID() {
		return videoID;
	}
	public String getTitle() {
		return title;
	}
	public String getChannelId() {
		return channelId;
	}
	public String getPublishedAt() {
		return publishedAt;
	}
	public int getPositionInPlaylist() {
		return positionInPlaylist;
	}
	public String getPreviewImage_default() {
		return previewImage_default;
	}
	public File getVideoOnDisk() {
		return videoOnDisk;
	}
}
