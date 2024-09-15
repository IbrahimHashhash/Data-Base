package ERDClasses;

import javafx.beans.property.*;
import java.sql.Date;

public class Person {
    // Fields
    private StringProperty firstName;
    private StringProperty lastName;
    private IntegerProperty age;
    private StringProperty address;
    private StringProperty phoneNumber;
    private ObjectProperty<Date> birthDate;
    private ObjectProperty<Character> gender;
    private IntegerProperty id;

    // Constructor
    public Person(String firstName, String lastName, int age, String address, String phoneNumber, Date birthDate, int id,char gender) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.age = new SimpleIntegerProperty(age);
        this.address = new SimpleStringProperty(address);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.birthDate = new SimpleObjectProperty<>(birthDate);
        this.id = new SimpleIntegerProperty(id);
        this.gender=new SimpleObjectProperty<>(gender);
    }

    public Person() {
        this("", "", 0, "", "", null, 0,'N'); // Default constructor with empty values
    }

    // Getters and setters
    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public int getAge() {
        return age.get();
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public java.sql.Date getBirthDate() {
        return birthDate.get();
    }

    public ObjectProperty<Date> birthDateProperty() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate.set(birthDate);
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

    public void setGender(Character gender) {
        this.gender.set(gender);
    }

    public Character getGender() {
        return gender.get();
    }

    // toString method to print the details of the person
    @Override
    public String toString() {
        return "Name: " + firstName.get() + " " + lastName.get() + ", Age: " + age.get() + ", Address: " + address.get() + ", Phone Number: " + phoneNumber.get() + ", Birth Date: " + birthDate.get();
    }
}
