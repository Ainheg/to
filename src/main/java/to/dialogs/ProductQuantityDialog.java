package to.dialogs;

import javafx.scene.control.TextField;
import to.model.Item;
import to.model.Product;

public class ProductQuantityDialog extends BaseDialog<Item>{
    public ProductQuantityDialog(Product p){
        super("Type Product quantity");
        TextField quantity = new DialogNotNullIntTextField();
        addTextField(quantity, "Quantity of " + p.getName());

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                Item i = new Item();
                i.setProduct(p);
                i.setQuantity(Double.parseDouble(quantity.getText()));
                return i;
            }
            return null;
        });
    }

    public ProductQuantityDialog(Item item){
        super("Type Product quantity");
        TextField quantity = new DialogNotNullIntTextField();
        addTextField(quantity, "New quantity of " + item.getProduct().getName());
        int intValue = (int) item.getQuantity().doubleValue();
        quantity.setText(Integer.toString(intValue));

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                Item i = new Item();
                i.setOrder(item.getOrder());
                i.setId(item.getId());
                i.setProduct(item.getProduct());
                i.setQuantity(Double.parseDouble(quantity.getText()));
                return i;
            }
            return null;
        });
    }

}
