package to.dialogs;

import javafx.scene.control.TextField;
import to.model.Employee;

public class EmployeeUpdateDialog extends BaseDialog<Employee> {
    public EmployeeUpdateDialog(Employee emp) {
        super("Update Employee");
        TextField name = new DialogNotNullTextField();
        addTextField(name, "Name");

        TextField surname = new DialogNotNullTextField();
        addTextField(surname, "Surname");

        TextField email = new DialogNotNullTextField();
        addTextField(email, "Email Address");

        TextField phone = new DialogPhoneNumberTextField();
        addTextField(phone, "Phone Number");

        name.setText(emp.getName());
        surname.setText(emp.getSurname());
        email.setText(emp.getEmailAddress());
        phone.setText(emp.getPhoneNumber() != null ? emp.getPhoneNumber() : "" );

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                Employee e = new Employee();
                e.setId(emp.getId());
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
