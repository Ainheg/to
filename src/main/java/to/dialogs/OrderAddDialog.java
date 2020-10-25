package to.dialogs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import to.DataManager;
import to.model.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public class OrderAddDialog extends BaseDialog<Order> {

    public OrderAddDialog(){
        super("Add Order");
        setOkButtonDisable(false);
        TextField shipment = new TextField();
        addTextField(shipment, "Shipment");

        DatePicker date = new DatePicker();
        grid.add(new Label("Placing Date"), 0, gridRows );
        grid.add(date, 1, gridRows );
        gridRows++;

        TextField hour = new DialogHourTextField();
        addTextField(hour, "Hour");

        TextField minute = new DialogMinSecTextField();
        addTextField(minute, "Minute");

        TextField second = new DialogMinSecTextField();
        addTextField(second, "Second");

        List<Employee> emp = DataManager.getDatabaseTableAsList("Employee");
        ObservableList<Integer> empIds = FXCollections.observableArrayList();
        for(Employee e : emp){
            empIds.add(e.getId());
        }
        ChoiceBox<Integer> empChoiceBox = new ChoiceBox<>();
        empChoiceBox.setItems(empIds);
        empChoiceBox.setValue(empIds.get(0));
        grid.add(new Label("Employee ID"), 0, gridRows );
        grid.add(empChoiceBox, 1, gridRows );
        gridRows++;

        List<Customer> cus = DataManager.getDatabaseTableAsList("Customer");
        ObservableList<Integer> cusIds = FXCollections.observableArrayList();
        for(Customer c : cus){
            cusIds.add(c.getId());
        }
        ChoiceBox<Integer> cusChoiceBox = new ChoiceBox<>();
        cusChoiceBox.setItems(cusIds);
        cusChoiceBox.setValue(cusIds.get(0));
        grid.add(new Label("Customer ID"), 0, gridRows );
        grid.add(cusChoiceBox, 1, gridRows );
        gridRows++;

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                Order o = new Order();
                Shipment s = new Shipment();
                s.setAddress(shipment.getText());
                s.setOrder(o);
                o.setShipment(s);

                Customer customer = null;
                for(Customer c : cus){
                    if(c.getId() == cusChoiceBox.getValue()){
                        customer = c;
                        break;
                    }
                }
                if(customer == null)
                    return null;
                o.setCustomer(customer);

                Employee employee = null;
                for(Employee e : emp){
                    if(e.getId() == empChoiceBox.getValue()){
                        employee = e;
                        break;
                    }
                }
                if(employee == null)
                    return null;
                o.setEmployee(employee);

                if(!(date.getValue() == null || hour.getText().isEmpty() || minute.getText().isEmpty() || second.getText().isEmpty())){
                    LocalDate d = date.getValue();
                    Timestamp timestamp = new Timestamp(d.getYear(), d.getMonthValue(), d.getDayOfMonth(),
                            Integer.parseInt(hour.getText()), Integer.parseInt(minute.getText()),
                            Integer.parseInt(second.getText()), 0);
                    o.setPlacingDate(timestamp);
                }

                return o;
            }
            return null;
        });
    }
}
