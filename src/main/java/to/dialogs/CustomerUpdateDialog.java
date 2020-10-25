package to.dialogs;

import javafx.scene.control.TextField;
import to.model.Customer;

public class CustomerUpdateDialog extends BaseDialog<Customer> {

    public CustomerUpdateDialog(Customer ctr){
        super("Update Customer");
        TextField name = new DialogNotNullTextField();
        addTextField(name, "Name");

        TextField surname = new DialogNotNullTextField();
        addTextField(surname, "Surname");

        TextField email = new DialogNotNullTextField();
        addTextField(email, "Email Address");

        name.setText(ctr.getName());
        surname.setText(ctr.getSurname());
        email.setText(ctr.getEmailAddress());

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                Customer c = new Customer();
                c.setId(ctr.getId());
                c.setName(name.getText());
                c.setSurname(surname.getText());
                c.setEmailAddress(email.getText());
                return c;
            }
            return null;
        });
    }
}
