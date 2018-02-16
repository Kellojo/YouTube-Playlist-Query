package Utility;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class GuiUtility {
	
	//Add margin for all gridpane children
	public static void AddMarginToAllGridPaneChildren(GridPane gridPane, Insets margin) {
		for (Node n : gridPane.getChildren()) {
			GridPane.setMargin(n, margin);
		}
	}
	
	//Sets the node/region to fill the gridpane horizontally
	public static void SetNodeToFillGridPaneHorizontally(Region node) {
		node.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		GridPane.setColumnSpan(node, GridPane.REMAINING);
	}
	
}
