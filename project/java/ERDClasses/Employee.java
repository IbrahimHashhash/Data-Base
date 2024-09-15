package ERDClasses;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.sql.Date; // Import java.sql.Date (instead of java.util.Date)

public class Employee extends Person {

    public Employee(String firstName, String lastName, int age, String address, String phoneNumber, Date birthDate, int id,char gen) {
        super(firstName, lastName, age, address, phoneNumber, birthDate, id, gen); // Pass java.sql.Date object
    }

    public Employee() {
        this("", "", 0, "", "", null, 0, 'N'); // Default constructor with empty values
    }
}
