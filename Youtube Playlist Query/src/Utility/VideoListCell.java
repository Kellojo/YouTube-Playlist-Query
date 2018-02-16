package Utility;

import Youtube.Video;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class VideoListCell extends ListCell<Video> {

	private GridPane grid = new GridPane();
    private ImageView icon = new ImageView();
    private Label title = new Label();
    private Label path = new Label();

    public VideoListCell() {
        configureGrid();        
        addControlsToGrid();            
    }

    private void configureGrid() {
        grid.setHgap(10);
        grid.setVgap(4);
        grid.setPadding(new Insets(0, 10, 0, 10));
        
        icon.setFitHeight(32);
        icon.setFitWidth(32);
        
        path.setFont(new Font("Roboto", 10));
    }

    private void addControlsToGrid() {
        grid.add(icon, 0, 0, 1, 2);                    
        grid.add(title, 1, 0);        
        grid.add(path, 1, 1);
    }

    @Override
    public void updateItem(Video video, boolean empty) {
        super.updateItem(video, empty);
        if (empty) {
            clearContent();
        } else {
            addContent(video);
        }
    }

    private void clearContent() {
        setText(null);
        setGraphic(null);
    }

    private void addContent(Video video) {
        setText(null);
        
        title.setText(video.getTitle());
        if (video.getPreviewImage_default() != "") {
        	icon.setImage(new Image(video.getPreviewImage_default()));
        } else {
        	icon.setImage(null);
        }
        if (video.getVideoOnDisk() != null) {
        	path.setText(video.getVideoOnDisk().getPath());
        } else {
        	path.setText("Not found.");
        }
        
        setGraphic(grid);
    }
    
    
}
