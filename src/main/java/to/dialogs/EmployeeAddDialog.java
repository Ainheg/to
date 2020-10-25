package to.dialogs;

import javafx.scene.control.TextField;
import to.model.Employee;

public class EmployeeAddDialog extends BaseDialog<Employee> {

    public EmployeeAddDialog(){
        super("Add Employee");
        TextField name = new DialogNotNullTextField();
        addTextField(name, "Name");

        TextField surname = new DialogNotNullTextField();
        addTextField(surname, "Surname");

        TextField email = new DialogNotNullTextField();
        addTextField(email, "Email Address");

        TextField phone = new DialogPhoneNumberTextField();
        addTextField(phone, "Phone Number");

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                Employee e = new Employee();
                e.setName(name.getText());
                e.setSurname(surname.getText());
                e.setEmailAddress(email.getText());
                e.setPhoneNumber(phone.getText());
                return e;
            }
            return null;
        });
    }
}
