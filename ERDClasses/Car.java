package ERDClasses;

import javafx.beans.property.*;

public class Car {
    private StringProperty color;
    private StringProperty model;
    private StringProperty condition;
    private IntegerProperty carNo;
    private DoubleProperty rentalPrice;
    private DoubleProperty mileage;

    public Car(String color, String model, String condition, int carNo, double rentalPrice,double mileage ) {
        this.color = new SimpleStringProperty(color);
        this.model = new SimpleStringProperty(model);
        this.condition = new SimpleStringProperty(condition);
        this.carNo = new SimpleIntegerProperty(carNo);
        this.rentalPrice = new SimpleDoubleProperty(rentalPrice);
        this.mileage=new SimpleDoubleProperty(mileage);
    }

    public void setMileage(int mileage) {
        this.mileage.set(mileage);
    }

    public double getMileage() {
        return mileage.get();
    }

    public String getColor() {
        return color.get();
    }

    public void setColor(String color) {
        this.color.set(color);
    }

    public StringProperty colorProperty() {
        return color;
    }

    public String getModel() {
        return model.get();
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public StringProperty modelProperty() {
        return model;
    }

    public String getCondition() {
        return condition.get();
    }

    public void setCondition(String condition) {
        this.condition.set(condition);
    }

    public StringProperty conditionProperty() {
        return condition;
    }

    public int getCarNo() {
        return carNo.get();
    }

    public void setCarNo(int carNo) {
        this.carNo.set(carNo);
    }

    public IntegerProperty carNoProperty() {
        return carNo;
    }

    public double getRentalPrice() {
        return rentalPrice.get();
    }

    public void setRentalPrice(double rentalPrice) {
        this.rentalPrice.set(rentalPrice);
    }

    public DoubleProperty rentalPriceProperty() {
        return rentalPrice;
    }
}
