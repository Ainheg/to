package to.dialogs;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDialog<T> extends Dialog<T> {

    protected final GridPane grid;
    protected int gridRows=0;
    private final Node okButton;
    protected ButtonType okButtonType;
    protected List<Requirement> requirements = new ArrayList<>();

    public BaseDialog(String title){
        super();
        setTitle(title);
        setHeaderText(null);
        okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        okButton = getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        getDialogPane().setContent(grid);
    }

    protected void setOkButtonDisable(boolean value){okButton.setDisable(value);}

    protected void addTextField(TextField textField, String name){
        textField.setPromptText(name);
        grid.add(new Label(name), 0, gridRows );
        grid.add(textField, 1, gridRows );
        gridRows++;
    }

    private boolean areRequirementsNotMet(){
        for(Requirement r : requirements){
            if(!r.getIsMet()) {
                return true;
            }
        }
        return false;
    }

    protected class DialogNotNullTextField extends TextField{
        public DialogNotNullTextField(){
            super();
            Requirement req = new Requirement();
            requirements.add(req);
            this.textProperty().addListener((observable, oldValue, newValue) ->  {
                req.setIsMet(!newValue.trim().isEmpty());
                okButton.setDisable((areRequirementsNotMet()));
            });
        }
    }

    protected class DialogNotNullIntTextField extends TextField{
        public DialogNotNullIntTextField(){
            super();
            Requirement req = new Requirement();
            requirements.add(req);
            this.textProperty().addListener((observable, oldValue, newValue) ->  {
                if (!newValue.matches("\\d*")) {
                    this.setText(newValue.replaceAll("[^\\d]", ""));
                }
                req.setIsMet(!newValue.trim().isEmpty());
                okButton.setDisable(areRequirementsNotMet());
            });
        }

        public DialogNotNullIntTextField(int length){
            super();
            Requirement req = new Requirement();
            requirements.add(req);
            this.textProperty().addListener((observable, oldValue, newValue) ->  {
                if (!newValue.matches("\\d{" + length + "}")) {
                    this.setText(newValue.replaceAll("[^\\d]", ""));
                }
                req.setIsMet(!newValue.trim().isEmpty());
                okButton.setDisable(areRequirementsNotMet());
            });
        }
    }

    protected static class DialogHourTextField extends TextField {
        public DialogHourTextField() {
            super();
            this.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d{0,2}")) {
                    this.setText(newValue.substring(0,Math.min(newValue.length(),2)).replaceAll("[^\\d]", ""));
                }
                if(!this.getText().isEmpty() && Integer.parseInt(this.getText()) > 23){
                    this.setText(oldValue);
                }
            });
        }
    }

    protected static class DialogMinSecTextField extends TextField {
        public DialogMinSecTextField() {
            super();
            this.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d{0,2}")) {
                    this.setText(newValue.substring(0,Math.min(newValue.length(),2)).replaceAll("[^\\d]", ""));
                }
                if(!this.getText().isEmpty() && Integer.parseInt(this.getText()) > 60){
                    this.setText(oldValue);
                }
            });
        }
    }

    protected class DialogPhoneNumberTextField extends TextField{
        public DialogPhoneNumberTextField(){
            super();
            Requirement req = new Requirement(true);
            requirements.add(req);
            this.textProperty().addListener((observable, oldValue, newValue) ->  {
                if (!newValue.matches("\\d*")) {
                    this.setText(newValue.replaceAll("[^\\d]", ""));
                }
                req.setIsMet(newValue.length() == 9 || newValue.length() == 0);
                okButton.setDisable(areRequirementsNotMet());
            });
        }
    }

    private static class Requirement{
        private boolean value;

        public Requirement(){
            this.value = false;
        }

        public Requirement(boolean value){
            this.value = value;
        }

        public void setIsMet(boolean value){
            this.value = value;
        }

        public boolean getIsMet(){
            return value;
        }
    }

}
