import java.io.File;
import java.util.List;

import Utility.FileWriter;
import Youtube.Playlist;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TextPopup {
	
	List<String> lines;
	TextArea ta;
	
	public TextPopup(Stage primaryStage, List<String>  lines, Playlist playlist, String title) {
		this.lines = lines;
		
		
		Stage s = new Stage();
		s.initModality(Modality.APPLICATION_MODAL);
		s.initOwner(primaryStage);
		s.setTitle(title);
		
		
		
		BorderPane bp = new BorderPane();
		bp.setPadding(GUI.insets_trbl);
		
		
		ta = new TextArea();
		ta.setEditable(false);
		ta.prefHeight(Double.MAX_VALUE);
		ta.setMaxHeight(Double.MAX_VALUE);
		for(String line : lines) {
			ta.appendText(line + "\n");
		}
		
		HBox hb = new HBox();
		hb.prefWidth(Double.MAX_VALUE);
		
		
		VBox vb = new VBox(ta, hb);
		vb.prefWidth(Double.MAX_VALUE);
		VBox.setVgrow(ta, Priority.ALWAYS);
		vb.setFillWidth(true);
		vb.setPrefHeight(Double.MAX_VALUE);
		vb.setPrefWidth(Double.MAX_VALUE);
		bp.setCenter(vb);
		

		
		Button btn_saveToFile = new Button("Save to Disk");
		btn_saveToFile.setTooltip(new Tooltip("Saves the displayed text to disk."));
		btn_saveToFile.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (event.getEventType().toString().equals("ACTION")) {
					
					//get directory to save results to
					DirectoryChooser directoryChooser = new DirectoryChooser();
					File selectedDirectory = directoryChooser.showDialog(primaryStage);

					//return if path is invalid
					if(selectedDirectory == null || !selectedDirectory.exists()){
						return;
					}
					
					//write lines to disk
					FileWriter.writeLinesToFile(lines, selectedDirectory.getPath(), title + " (" + playlist.getTitle() + ").txt");
				}
			}
		});

		
		Button btn_close = new Button("Close");
		btn_close.setTooltip(new Tooltip("Closes this window."));
		btn_close.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (event.getEventType().toString().equals("ACTION")) {
					s.close();
				}
			}
		});

		
		HBox.setHgrow(btn_saveToFile, Priority.ALWAYS);
		HBox.setHgrow(btn_close, Priority.ALWAYS);
		btn_saveToFile.setMaxWidth(Double.MAX_VALUE);
		btn_close.setMaxWidth(Double.MAX_VALUE);
		HBox.setMargin(btn_saveToFile, GUI.insets_half_trbl);
		HBox.setMargin(btn_close, GUI.insets_half_trbl);
		hb.getChildren().addAll(btn_close, btn_saveToFile);

		

		Scene scene = new Scene(bp, 250, 300);
		s.setScene(scene);
		s.show();
	}
}
