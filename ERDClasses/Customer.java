package ERDClasses;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.sql.Date; // Import java.sql.Date instead of java.util.Date

public class Customer extends Person {

    private StringProperty licenseNum;
    private IntegerProperty id;

    public Customer(String firstName, String lastName, int age, String address, String phoneNumber, Date birthDate, String licenseNum, int id,char gen) {
        super(firstName, lastName, age, address, phoneNumber, birthDate, id, gen); // Pass java.sql.Date object
        this.licenseNum = new SimpleStringProperty(licenseNum);
        this.id = new SimpleIntegerProperty(id);
    }

    public Customer() {
        this("", "", 0, "", "", null, "", 0, 'N'); // Default constructor with empty values
    }

    public String getLicenseNum() {
        return licenseNum.get();
    }

    public StringProperty licenseNumProperty() {
        return licenseNum;
    }

    public void setLicenseNum(String licenseNum) {
        this.licenseNum.set(licenseNum);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }
}
