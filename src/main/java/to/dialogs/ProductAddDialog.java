package to.dialogs;

import javafx.scene.control.TextField;
import to.model.Product;

public class ProductAddDialog extends BaseDialog<Product> {

    public ProductAddDialog(){
        super("Add Product");
        TextField name = new DialogNotNullTextField();
        addTextField(name, "Name");

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                Product p = new Product();
                p.setName(name.getText());
                return p;
            }
            return null;
        });
    }
}
