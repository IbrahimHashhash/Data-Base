package ERDClasses;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Login {
    private IntegerProperty emp_id;
    private StringProperty password;

    public Login(int emp_id, String password) {
        this.emp_id = new SimpleIntegerProperty(emp_id);
        this.password = new SimpleStringProperty(password);
    }

    public int getEmp_id() {
        return emp_id.get();
    }

    public IntegerProperty emp_idProperty() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id.set(emp_id);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }
}
