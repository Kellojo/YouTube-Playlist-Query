package Youtube;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Playlist {
	private String id;
	private String title = "";
	private String author = "";
	private String authorID = "";
	private String publishedAt = "";
	private boolean isValidated = false;
	private List<Video> videos;
	
	
	public Playlist(String id) {
		super();
		this.id = id.trim();
		videos = new ArrayList<Video>();
	}

	/* validate playlist */
	public boolean isValidPlaylist(YouTubeAPIKey APIKey) {
		boolean isValid = false;
		String query = "https://www.googleapis.com/youtube/v3/playlists?part=id&id=" + id + "&key=" + APIKey;
		try {
			String result = WebClient.getHTML(query);
			JSONObject obj = new JSONObject(result);
			JSONObject foundPlaylist = (JSONObject) obj.getJSONArray("items").get(0);
			if (foundPlaylist != null && foundPlaylist.getString("id").equals(id)) {
				isValid = true;
				isValidated = true;
			}
		} catch (Exception e) {
			System.err.println("Exception caught while validating the playlist '" + id + "'. (Should not be a problem)");
		}
		
		
		return isValid;
	}
	
	/* Querys the youtube api for this playlist and fills the properties of this playlist object */
	public void QueryPlaylistData(YouTubeAPIKey APIKey) {
		queryAllVideos(APIKey);
		fetchPlaylistInfo(APIKey);
	}
	
	/* Querys all videos in the playlist */
	private void queryAllVideos(YouTubeAPIKey APIKey) {
		videos = new ArrayList<Video>();
		String nextPageToken = "";
		boolean isFirstQuery = true;
		
		while (nextPageToken != "" || isFirstQuery) {
			try {
				isFirstQuery = false;
				String query = "https://www.googleapis.com/youtube/v3/playlistItems?playlistId=" + id + "&pageToken=" + nextPageToken + "&part=snippet&maxResults=50&key=" + APIKey;
				String response = WebClient.getHTML(query);
				JSONObject obj = new JSONObject(response);
				
				/* Get Videos in response and check for nextPageToken */
				try {
					nextPageToken = obj.getString("nextPageToken");
				} catch (Exception e) {
					nextPageToken = "";
				}
				
				/* Get all videos contained in the answer and add them to the list */
				JSONArray arr = obj.getJSONArray("items");
				for (int i = 0; i < arr.length(); i++)
				{
					JSONObject result = arr.getJSONObject(i);
					JSONObject videoInfo = result.getJSONObject("snippet");
					
				    String videoID = videoInfo.getJSONObject("resourceId").getString("videoId");
				    String videoTitle = videoInfo.getString("title");
				    String publishedAt = videoInfo.getString("publishedAt");
				    String channelId = videoInfo.getString("channelId");
				    
				    String previewImage_default = "";
				    try {
				    	previewImage_default = videoInfo.getJSONObject("thumbnails").getJSONObject("default").getString("url");
				    } catch(Exception e) {
				    	System.err.println("An error occured while querying the image of video with the id '" + videoID + "'. This probably is not a problem at all and is usually caused by a video being set to 'private'.");
				    }
				    
				    int position = videoInfo.getInt("position") + 1;
				    videos.add(new Video(videoID, videoTitle, channelId, publishedAt, position, previewImage_default));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/* Gets the name of the playlist */
	private void fetchPlaylistInfo(YouTubeAPIKey APIKey) {
		String query = "https://www.googleapis.com/youtube/v3/playlists?part=snippet%2Clocalizations&id=" + id + "&key=" + APIKey;
		String response;
		try {
			response = WebClient.getHTML(query);
			JSONObject obj = new JSONObject(response);
			JSONArray arr = obj.getJSONArray("items");
			JSONObject snippet = arr.getJSONObject(0).getJSONObject("snippet");
			title = snippet.getString("title");
			author = snippet.getString("channelTitle");
			authorID = snippet.getString("channelId");
			publishedAt = snippet.getString("publishedAt");
			
		} catch (Exception e) {
			System.err.println("Could not retrieve playlist name. Using id as playlistname instead.");
		}
	}
	
	/* Finds the videos in this playlist on disk given a directory to search in */
	public void FindVideosOnDisk(File searchDirectory) {
		for (Video v : videos) {
			v.FindVideoOnDisk(searchDirectory);
		}
	}
	
	public String toString() { 
	    return "Title: '" + this.title + "' (" + this.id + ") by " + this.author + " (" + this.authorID + ") published at: " + this.publishedAt + " contains " + videos.size() + " Videos and is validated: " + this.isValidated;
	} 
	
	
	public String getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getAuthor() {
		return author;
	}
	public String getAuthorID() {
		return authorID;
	}

	public String getPublishedAt() {
		return publishedAt;
	}
	public boolean isValidated() {
		return isValidated;
	}
	public List<Video> getVideos() {
		return videos;
	}

}
