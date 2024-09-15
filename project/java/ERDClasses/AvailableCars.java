package ERDClasses;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Objects;

public class AvailableCars {
    private IntegerProperty car_id;
    private IntegerProperty rent_id;

    // Constructors
    public AvailableCars() {
        this.car_id = new SimpleIntegerProperty();
        this.rent_id = new SimpleIntegerProperty();
    }

    public AvailableCars(int car_id, int rent_id) {
        this.car_id = new SimpleIntegerProperty(car_id);
        this.rent_id = new SimpleIntegerProperty(rent_id);
    }

    // Getters
    public int getCar_id() {
        return car_id.get();
    }

    public int getRent_id() {
        return rent_id.get();
    }

    // Setters
    public void setCar_id(int car_id) {
        this.car_id.set(car_id);
    }

    public void setRent_id(int rent_id) {
        this.rent_id.set(rent_id);
    }

    // Property getters
    public IntegerProperty car_idProperty() {
        return car_id;
    }

    public IntegerProperty rent_idProperty() {
        return rent_id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        AvailableCars that = (AvailableCars) object;
        return Objects.equals(car_id, that.car_id) && Objects.equals(rent_id, that.rent_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(car_id, rent_id);
    }
// Other methods if needed...
}
