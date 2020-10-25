package to.dialogs;

import javafx.scene.control.TextField;
import to.model.Customer;

public class CustomerAddDialog extends BaseDialog<Customer> {

    public CustomerAddDialog(){
        super("Add Customer");
        TextField name = new DialogNotNullTextField();
        addTextField(name, "Name");

        TextField surname = new DialogNotNullTextField();
        addTextField(surname, "Surname");

        TextField email = new DialogNotNullTextField();
        addTextField(email, "Email Address");

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                Customer c = new Customer();
                c.setName(name.getText());
                c.setSurname(surname.getText());
                c.setEmailAddress(email.getText());
                return c;
            }
            return null;
        });
    }
}
