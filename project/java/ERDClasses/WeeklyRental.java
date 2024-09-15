package ERDClasses;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.sql.Date;
import java.util.Objects;

public class WeeklyRental extends Rental {
    private IntegerProperty noOfWeeks;

    public int getNoOfWeeks() {
        return noOfWeeks.get();
    }

    public WeeklyRental(Date rentDate, Date returnDate, int rentID, String cancellation, int car_num, int emp_id, int cus_ID, int noOfWeeks) {
        super(rentDate, returnDate, rentID, cancellation, car_num, emp_id, cus_ID);
        this.noOfWeeks = new SimpleIntegerProperty(noOfWeeks);
    }

    public IntegerProperty getNoOfWeeksProperty() {
        return noOfWeeks;
    }

    public void setNoOfWeeks(int noOfDays) {
        this.noOfWeeks.set(noOfDays);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        WeeklyRental that = (WeeklyRental) object;
        return Objects.equals(noOfWeeks, that.noOfWeeks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noOfWeeks);
    }
}
