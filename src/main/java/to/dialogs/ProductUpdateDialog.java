package to.dialogs;

import javafx.scene.control.TextField;
import to.model.Product;

public class ProductUpdateDialog extends BaseDialog<Product> {
    public ProductUpdateDialog(Product prd){
        super("Update Product");
        TextField name = new DialogNotNullTextField();
        addTextField(name, "Name");

        name.setText(prd.getName());

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                Product p = new Product();
                p.setId(prd.getId());
                p.setName(name.getText());
                return p;
            }
            return null;
        });
    }
}
