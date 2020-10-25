package to.dialogs;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import to.model.Item;

public class ItemPickDialog extends BaseDialog<Item> {
    public ItemPickDialog(ObservableList<Item> items){
        super("Pick Product for Item");
        TableView<Item> itemTable = new TableView<>();
        TableColumn<Item, String> prodNameColumn = new TableColumn<>("Product");
        TableColumn<Item, Double> productQuantityColumn = new TableColumn<>("Quantity");


        itemTable.getColumns().add(prodNameColumn);
        itemTable.getColumns().add(productQuantityColumn);
        prodNameColumn.setCellValueFactory(param -> {
            Item i = param.getValue();
            return new SimpleStringProperty(i.getProduct().getName());
        });
        productQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemTable.setItems(items);

        grid.add(itemTable,0, 0 );

        itemTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setOkButtonDisable(newValue == null));

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return itemTable.getSelectionModel().getSelectedItem();
            }
            return null;
        });
    }
}
