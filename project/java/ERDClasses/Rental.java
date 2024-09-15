package ERDClasses;

import javafx.beans.property.*;
import java.sql.Date;
import java.util.Objects;

public class Rental {
    private ObjectProperty<Date> rentDate;
    private ObjectProperty<Date> returnDate;
    private IntegerProperty rentID;
    private StringProperty cancellation;
    private IntegerProperty car_num;
    private IntegerProperty emp_id;


    public int getCus_ID() {
        return cus_ID.get();
    }

    public IntegerProperty cus_IDProperty() {
        return cus_ID;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Rental rental = (Rental) object;
        return Objects.equals(rentDate, rental.rentDate) && Objects.equals(returnDate, rental.returnDate) && Objects.equals(rentID, rental.rentID) && Objects.equals(cancellation, rental.cancellation) && Objects.equals(car_num, rental.car_num) && Objects.equals(cus_ID, rental.cus_ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rentDate, returnDate, rentID, cancellation, car_num,emp_id, cus_ID);
    }

    public void setCus_ID(int cus_ID) {
        this.cus_ID.set(cus_ID);
    }

    private IntegerProperty cus_ID;

    public Rental(java.sql.Date rentDate, java.sql.Date returnDate, int rentID, String cancellation, int car_num,int emp_id,int cus_ID) {
        this.rentDate = new SimpleObjectProperty<>(rentDate);
        this.returnDate = new SimpleObjectProperty<>(returnDate);
        this.rentID = new SimpleIntegerProperty(rentID);
        this.cancellation = new SimpleStringProperty(cancellation);
        this.car_num = new SimpleIntegerProperty(car_num);
        this.emp_id= new SimpleIntegerProperty(emp_id);
        this.cus_ID= new SimpleIntegerProperty(cus_ID);

    }

    public int getCar_num() {
        return car_num.get();
    }

    public IntegerProperty car_numProperty() {
        return car_num;
    }

    public void setCar_num(int car_num) {
        this.car_num.set(car_num);
    }

    public java.sql.Date getRentDate() {
        return rentDate.get();
    }

    public ObjectProperty<Date> rentDateProperty() {
        return rentDate;
    }

    public void setRentDate(Date rentDate) {
        this.rentDate.set(rentDate);
    }

    public java.sql.Date getReturnDate() {
        return returnDate.get();
    }

    public ObjectProperty<Date> returnDateProperty() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate.set(returnDate);
    }

    public int getRentID() {
        return rentID.get();
    }

    public IntegerProperty rentIDProperty() {
        return rentID;
    }

    public void setRentID(int rentID) {
        this.rentID.set(rentID);
    }

    public String getCancellation() {
        return cancellation.get();
    }

    public StringProperty cancellationProperty() {
        return cancellation;
    }

    public void setCancellation(String cancellation) {
        this.cancellation.set(cancellation);
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
}
