package ERDClasses;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.sql.Date;
import java.util.Objects;

public class DailyRental extends Rental {
    private IntegerProperty noOfDays;

    public DailyRental(Date rentDate, Date returnDate, int rentID, String cancellation, int car_num, int emp_id, int cus_ID, int noOfDays) {
        super(rentDate, returnDate, rentID, cancellation, car_num, emp_id, cus_ID);
        this.noOfDays = new SimpleIntegerProperty(noOfDays);
    }

    public int getNoOfDays() {
        return noOfDays.get();
    }

    public IntegerProperty noOfDaysProperty() {
        return noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays.set(noOfDays);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        DailyRental that = (DailyRental) object;
        return Objects.equals(noOfDays, that.noOfDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noOfDays);
    }
}
