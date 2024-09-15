package ERDClasses;

import javafx.beans.property.*;
import java.sql.Date;
import java.util.Objects;

public class Payment {
    private DoubleProperty amount; // Change from IntegerProperty to DoubleProperty
    private IntegerProperty paymentID;
    private DoubleProperty downPay; // Change from IntegerProperty to DoubleProperty
    private StringProperty paymentMethod;
    private ObjectProperty<Date> paydate; // Change to java.sql.Date
    private IntegerProperty customerId;
    private IntegerProperty rental_Id;
    private DoubleProperty totalCost; // Change from IntegerProperty to DoubleProperty
    int counter = 0;



    public Payment(double amount, double downPay,double totalCost, String paymentMethod, Date paydate,int customerID,int rental_Id) {
        this.amount = new SimpleDoubleProperty(amount); // Use SimpleDoubleProperty instead of SimpleIntegerProperty
        this.paymentID = new SimpleIntegerProperty(++counter);
        this.downPay = new SimpleDoubleProperty(downPay); // Use SimpleDoubleProperty instead of SimpleIntegerProperty
        this.paymentMethod = new SimpleStringProperty(paymentMethod);
        this.paydate = new SimpleObjectProperty<>(paydate);
        this.customerId=new SimpleIntegerProperty(customerID);
        this.rental_Id=new SimpleIntegerProperty(rental_Id);
        this.totalCost= new SimpleDoubleProperty(totalCost);
    }

    public int getRental_Id() {
        return rental_Id.get();
    }

    public IntegerProperty rental_IdProperty() {
        return rental_Id;
    }

    public void setRental_Id(int rental_Id) {
        this.rental_Id.set(rental_Id);
    }

    public double getTotalCost() {
        return totalCost.get();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Payment payment = (Payment) object;
        return Objects.equals(amount, payment.amount) && Objects.equals(paymentID, payment.paymentID) && Objects.equals(downPay, payment.downPay) && Objects.equals(paymentMethod, payment.paymentMethod) && Objects.equals(paydate, payment.paydate) && Objects.equals(customerId, payment.customerId) && Objects.equals(rental_Id, payment.rental_Id) && Objects.equals(totalCost, payment.totalCost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, paymentID, downPay, paymentMethod, paydate, customerId, rental_Id, totalCost);
    }

    public DoubleProperty totalCostProperty() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost.set(totalCost);
    }

    public double getAmount() {
        return amount.get();
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public int getPaymentID() {
        return paymentID.get();
    }

    public IntegerProperty paymentIDProperty() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID.set(paymentID);
    }

    public double getDownPay() {
        return downPay.get();
    }

    public DoubleProperty downPayProperty() {
        return downPay;
    }

    public void setDownPay(double downPay) {
        this.downPay.set(downPay);
    }

    public String getPaymentMethod() {
        return paymentMethod.get();
    }

    public StringProperty paymentMethodProperty() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod.set(paymentMethod);
    }

    public Date getPaydate() {
        return paydate.get();
    }

    public ObjectProperty<Date> paydateProperty() {
        return paydate;
    }

    public int getCustomerId() {
        return customerId.get();
    }

    public IntegerProperty customerIdProperty() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }

    public void setPaydate(Date paydate) {
        this.paydate.set(paydate);
    }
}
