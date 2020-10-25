package to;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.StageStyle;
import to.dialogs.*;
import to.model.*;

import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

public class WindowController implements Initializable {

    //Getting data out of Hibernate Table as Observable List
    private <T> ObservableList<T> getDataObservableList(String hibernateTableName) {
        ObservableList<T> dataList = FXCollections.observableArrayList();
        dataList.addAll(DataManager.getDatabaseTableAsList(hibernateTableName));
        return dataList;
    }

    @FXML
    private void handleAddButton(ActionEvent event){
        String type = ((Button) event.getSource()).getId();
        Optional<?> result;
        switch(type){
            case "EmpAdd": {
                EmployeeAddDialog dial = new EmployeeAddDialog();
                result = dial.showAndWait();
                break;
            }
            case "CusAdd": {
                CustomerAddDialog dial = new CustomerAddDialog();
                result = dial.showAndWait();
                break;
            }
            case "ProdAdd": {
                ProductAddDialog dial = new ProductAddDialog();
                result = dial.showAndWait();
                break;
            }
            case "OrdAdd": {
                OrderAddDialog dial = new OrderAddDialog();
                result = dial.showAndWait();
                break;
            }
            case "OrdAddItem":{
                Order o = OrdTable.getSelectionModel().getSelectedItem();
                if(o == null)
                    return;
                ObservableList<Product> products = getDataObservableList("Product");
                ProductPickDialog dial = new ProductPickDialog(products);
                Optional<?> pResult = dial.showAndWait();
                if(!pResult.isPresent())
                    return;

                Product p =(Product) pResult.get();
                ProductQuantityDialog productQuantityDialog = new ProductQuantityDialog(p);
                Optional<?> iResult = productQuantityDialog.showAndWait();
                if(!iResult.isPresent())
                    return;

                Item i = (Item) iResult.get();
                i.setOrder(o);
                DataManager.addItemToOrder(i);
                loadAll();
                return;
            }
            default:{
                return;
            }
        }

        if(result != null && result.isPresent()){
            DataManager.addToDatabase(result.get());
            loadAll();
        }
    }

    @FXML
    private void handleUpdateButton(ActionEvent event){
        String type = ((Button) event.getSource()).getId();
        Object itemToUpdate;
        BaseDialog<?> dial;

        switch(type){
            case "EmpUpdate": {
                itemToUpdate = EmpTable.getSelectionModel().getSelectedItem();
                if(itemToUpdate != null)
                    dial = new EmployeeUpdateDialog((Employee) itemToUpdate);
                else
                    return;
                break;
            }
            case "CusUpdate": {
                itemToUpdate = CusTable.getSelectionModel().getSelectedItem();
                if(itemToUpdate != null)
                    dial = new CustomerUpdateDialog((Customer) itemToUpdate);
                else
                    return;
                break;
            }
            case "ProdUpdate": {
                itemToUpdate = ProdTable.getSelectionModel().getSelectedItem();
                if(itemToUpdate != null)
                    dial = new ProductUpdateDialog((Product) itemToUpdate);
                else
                    return;
                break;
            }
            case "OrdUpdate": {
                itemToUpdate = OrdTable.getSelectionModel().getSelectedItem();
                if(itemToUpdate != null)
                    dial = new OrderUpdateDialog((Order) itemToUpdate);
                else
                    return;
                break;
            }
            case "OrdUpdateItem":{
                Order order = OrdTable.getSelectionModel().getSelectedItem();
                if(order == null)
                    return;
                Set<Item> itemSet = order.getItems();
                ObservableList<Item> itemList = FXCollections.observableArrayList(itemSet);
                dial = new ItemPickDialog(itemList);
                Optional<?> itemResult = dial.showAndWait();
                if(!itemResult.isPresent())
                    return;
                itemToUpdate = itemResult.get();
                dial = new ProductQuantityDialog((Item)itemToUpdate);
                break;
            }
            default:{
                return;
            }
        }
        Optional<?> result = dial.showAndWait();
        result.ifPresent(DataManager::updateInDatabase);
        loadAll();

    }

    @FXML
    private void handleDeleteButton(ActionEvent event) {
        String type = ((Button) event.getSource()).getId();
        Object itemToDelete;

        switch(type){
            case "EmpDelete":
                itemToDelete = EmpTable.getSelectionModel().getSelectedItem();
                break;
            case "CusDelete":
                itemToDelete = CusTable.getSelectionModel().getSelectedItem();
                break;
            case "ProdDelete":
                itemToDelete = ProdTable.getSelectionModel().getSelectedItem();
                break;
            case "OrdDelete":
                itemToDelete = OrdTable.getSelectionModel().getSelectedItem();
                break;
            case "OrdDeleteItem":{
                Order order = OrdTable.getSelectionModel().getSelectedItem();
                if(order == null)
                    return;
                Set<Item> itemSet = order.getItems();
                ObservableList<Item> itemList = FXCollections.observableArrayList(itemSet);
                ItemPickDialog dial = new ItemPickDialog(itemList);
                Optional<?> itemResult = dial.showAndWait();
                if(!itemResult.isPresent())
                    return;
                itemToDelete = itemResult.get();
                break;
            }
            default:{
                return;
            }
        }

        if(itemToDelete != null) {
            if(!DataManager.deleteFromDatabase(itemToDelete)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cannot delete chosen item");
                alert.setHeaderText(null);
                alert.setContentText("Cannot delete chosen item. Please make sure, that item is not associated with items from other tables before deleting it.");
                alert.initStyle(StageStyle.UTILITY);
                alert.showAndWait();
            }
        }
        loadAll();
    }

    //Employees TableView ----------------------------------------------------------------------------------------------
    @FXML
    TableView<Employee> EmpTable;
    @FXML
    TableColumn<Employee, Integer> EmpIDColumn;
    @FXML
    TableColumn<Employee, String> EmpNameColumn;
    @FXML
    TableColumn<Employee, String> EmpSurnameColumn;
    @FXML
    TableColumn<Employee, String> EmpEmailColumn;
    @FXML
    TableColumn<Employee, String> EmpPhoneColumn;

    private void loadEmployees(){
        EmpIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        EmpNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        EmpSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        EmpEmailColumn.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
        EmpPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        EmpTable.setItems(this.getDataObservableList("Employee"));
    }

    //Customers TableView ----------------------------------------------------------------------------------------------
    @FXML
    TableView<Customer> CusTable;
    @FXML
    TableColumn<Customer, Integer> CusIDColumn;
    @FXML
    TableColumn<Customer, String> CusNameColumn;
    @FXML
    TableColumn<Customer, String> CusSurnameColumn;
    @FXML
    TableColumn<Customer, String> CusEmailColumn;

    private void loadCustomers(){
        CusIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        CusNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        CusSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        CusEmailColumn.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
        CusTable.setItems(this.getDataObservableList("Customer"));
    }

    //Products TableView -----------------------------------------------------------------------------------------------
    @FXML
    TableView<Product> ProdTable;
    @FXML
    TableColumn<Product, Integer> ProdIDColumn;
    @FXML
    TableColumn<Product, String> ProdNameColumn;

    private void loadProducts(){
        ProdIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ProdNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ProdTable.setItems(this.getDataObservableList("Product"));
    }

    //Orders TableView -------------------------------------------------------------------------------------------------
    @FXML
    TableView<Order> OrdTable;
    @FXML
    TableColumn<Order, Integer> OrdIdColumn;
    @FXML
    TableColumn<Order, String> OrdCustomerColumn;
    @FXML
    TableColumn<Order, String> OrdEmployeeColumn;
    @FXML
    TableColumn<Order, String> OrdShipmentColumn;
    @FXML
    TableColumn<Order, Timestamp> OrdPlacingDateColumn;
    @FXML
    TableColumn<Order, String> OrdItemsColumn;

    private void loadOrders(){
        OrdIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        OrdCustomerColumn.setCellValueFactory(param -> {
            Customer c = param.getValue().getCustomer();
            return new SimpleStringProperty(c.getName() + " " + c.getSurname() + " id: " + c.getId());
        });
        OrdEmployeeColumn.setCellValueFactory(param -> {
            Employee e = param.getValue().getEmployee();
            return new SimpleStringProperty(e.getName() + " " + e.getSurname() + " id: " + e.getId());
        });
        OrdShipmentColumn.setCellValueFactory(param -> {
            Shipment s = param.getValue().getShipment();
            if(s != null){
                return new SimpleStringProperty(s.getAddress());
            }
            return null;
        });
        OrdPlacingDateColumn.setCellValueFactory(new PropertyValueFactory<>("placingDate"));
        List<Item> items = DataManager.getDatabaseTableAsList("Item");
        OrdItemsColumn.setCellValueFactory(param -> {
            Order o = param.getValue();
            Set<Item> itemsSet = new HashSet<>();
            for(Object i : items){
                if(((Item)i).getOrder().getId() == o.getId() ){
                    itemsSet.add((Item) i);
                }
            }
            StringProperty s = new SimpleStringProperty("");
            for(Item i : itemsSet){
                s.setValue(s.getValue() + i.getProduct().getName() + " x" + i.getQuantity() + "\n");
            }
            return s;
        });
        OrdTable.setItems(this.getDataObservableList("Order"));
    }

    //Init -------------------------------------------------------------------------------------------------------------

    private void loadAll(){
        loadOrders();
        loadCustomers();
        loadEmployees();
        loadProducts();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DataManager.generate();
        loadAll();
    }
}