package JavaFXClasses;

import java.time.LocalDate;
import java.time.Period;

public class Manager {
    public static int calculateAge(LocalDate birthDate) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        Period period = Period.between(birthDate, currentDate);
        System.out.println("Age: " + period.getYears() );
        // Return the years from the period
        return period.getYears();
    }
    public static boolean isValidAge(int age){
        return age >= 18;
    }
    public static boolean isNum(String n){
        try{
            double num = Double.parseDouble(n);
            return true;
        }catch (NumberFormatException ex){
            return false;
        }
    }
    public static boolean isStrongPassword(String password) {
        // Check length
        if (password.length() < 8) {
            return false;
        }

        // Check for at least one uppercase letter, one lowercase letter, and one digit
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }

        // Check if all conditions are met
        return hasUppercase && hasLowercase && hasDigit;
    }
    public static boolean idCheck(long number) {
        // Convert the number to a string to count its digits
        String numberString = String.valueOf(number);

        // Check if the length of the string is 10
        return numberString.length() == 10;
    }

    public static boolean phoneCheck(long number) {
        String numberString = String.valueOf(number);
        return numberString.length() == 7;
    }



}
