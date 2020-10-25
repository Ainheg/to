package to.dialogs;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import to.model.Product;

public class ProductPickDialog extends BaseDialog<Product>{
    public ProductPickDialog(ObservableList<Product> prodList){
        super("Pick Product for Item");
        TableView<Product> prodTable = new TableView<>();
        TableColumn<Product, Integer> prodIDColumn = new TableColumn<>("ID");
        TableColumn<Product, String> prodNameColumn = new TableColumn<>("Name");
        prodTable.getColumns().add(prodIDColumn);
        prodTable.getColumns().add(prodNameColumn);
        prodIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodTable.setItems(prodList);
        grid.add(prodTable,0, 0 );

        prodTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setOkButtonDisable(newValue == null));

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return prodTable.getSelectionModel().getSelectedItem();
            }
            return null;
        });
    }

}
