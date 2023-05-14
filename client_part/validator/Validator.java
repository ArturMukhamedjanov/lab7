package validator;

import exceptions.BrokenScript;
import model.*;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class that change collection
 */
public class Validator {

    /**
     * @param name
     * @return check correctness of value of name
     */
    public static boolean checkName(String name){
        return !name.equals("");
    }

    /**
     * @param x
     * @return check correctness of value of coordinate_x
     */
    public static boolean checkCoordinateX(float x){
        return !(x > 2);
    }
    /**
     * @param y
     * @return check correctness of value of coordinate_y
     */
    public static boolean checkCoordinateY(Integer y){
        return y != null;
    }

    /**
     * @param area
     * @return check correctness of value of area
     */
    public static boolean checkArea(Long area){
        return area >= 0;
    }

    /**
     * @param number_of_rooms
     * * @return check correctness of value of number of rooms
     */
    public static boolean checkNumberOfRooms(long number_of_rooms){
        return !(number_of_rooms > 10 | number_of_rooms < 0);
    }

    /**
     * @param year_of_house
     * @return check correctness of value of year of house
     */
    public static boolean checkYearOfHouse(Long year_of_house){
        return year_of_house >= 0;
    }


    /**
     * @param number_of_flats
     * @return check correctness of value of number of flat
     */
    public static boolean checkNumberOfFlats(int number_of_flats){
        return number_of_flats >= 0;
    }

    public static Flat getElement(InputStream input) throws BrokenScript{
        Scanner scanner = new Scanner(input);
        if(input == System.in) {
            System.out.println("Enter name value:");
            System.out.print("$");
        }
        String name = scanner.nextLine();
        boolean isEntered = false;
        Float coordinate_x = (float) 0;
        while(!isEntered) {
            try {
                if(input == System.in) {
                    System.out.println("Enter coordinate_x value:");
                    System.out.print("$");
                }
                coordinate_x = Float.parseFloat(scanner.nextLine());
                if(!checkCoordinateX(coordinate_x)) {
                    throw new InputMismatchException();
                }
                isEntered = true;
            }catch(InputMismatchException | NumberFormatException e){
                if(input != System.in){
                    throw new BrokenScript();
                }
                System.out.println("Entered value is incorrect. Enter another one, please. (higher than zero, lower than 2)");
                System.out.print("$");
            }
        }
        isEntered = false;
        Integer coordinate_y = 0;
        while(!isEntered) {
            try {
                if(input == System.in) {
                    System.out.println("Enter coordinate_y value:");
                    System.out.print("$");
                }
                coordinate_y = Integer.parseInt(scanner.nextLine());
                if (!checkCoordinateY(coordinate_y)) {
                    throw new InputMismatchException();
                }
                isEntered = true;
            } catch (InputMismatchException | NumberFormatException e) {
                if(input != System.in){
                    throw new BrokenScript();
                }
                System.out.println("Entered value is incorrect. Enter another one, please. (can't be nell)");
                System.out.print("$");
            }
        }
        LocalDate localDate = LocalDate.now();
        isEntered = false;
        Long area = (long)0;
        while(!isEntered) {
            try {
                if(input == System.in) {
                    System.out.println("Enter area value:");
                    System.out.print("$");
                }
                area = Long.parseLong(scanner.nextLine());
                if(!checkArea(area)) {
                    throw new InputMismatchException();
                }
                isEntered = true;
            }catch (InputMismatchException | NumberFormatException e) {
                if(input != System.in){
                    throw new BrokenScript();
                }
                System.out.println("Entered value is incorrect. Enter another one, please. (higher than zero)");
                System.out.print("$");
            }
        }
        isEntered = false;
        long numbers_of_rooms = 0;
        while(!isEntered) {
            try {
                if(input == System.in) {
                    System.out.println("Enter number of rooms value:");
                    System.out.print("$");
                }
                numbers_of_rooms = Long.parseLong(scanner.nextLine());
                if(!checkNumberOfRooms(numbers_of_rooms)) {
                    throw new InputMismatchException();
                }
                isEntered = true;
            }catch (InputMismatchException | NumberFormatException e) {
                if(input != System.in){
                    throw new BrokenScript();
                }
                System.out.println("Entered value is incorrect. Enter another one, please. (higher than zero, lower than 10)");
                System.out.print("$");
            }
        }
        isEntered = false;
        boolean furniture = false;
        while(!isEntered) {
            try {
                if(input == System.in) {
                    System.out.println("Enter furniture value:");
                    System.out.print("$");
                }
                String dummy = scanner.nextLine();
                furniture = Boolean.parseBoolean(dummy);
                isEntered = true;
            } catch (InputMismatchException | NumberFormatException e) {
                if(input != System.in){
                    throw new BrokenScript();
                }
                System.out.println("Entered value is incorrect. Enter another one, please.");
                System.out.print("$");
            }
        }
        isEntered = false;
        Furnish furnish = Furnish.DESIGNER;
        while(!isEntered) {
            try {
                if(input == System.in) {
                    System.out.println("Enter furnish value[DESIGNER, FINE, BAD, LITTLE]");
                    System.out.print("$");
                }
                String dummy = scanner.nextLine();
                furnish = switch (dummy) {
                    case ("DESIGNER") -> Furnish.DESIGNER;
                    case ("FINE") -> Furnish.FINE;
                    case ("BAD") -> Furnish.BAD;
                    case ("LITTLE") -> Furnish.LITTLE;
                    default -> throw new InputMismatchException();
                };
                isEntered = true;
            }catch (InputMismatchException | NumberFormatException e) {
                if(input != System.in){
                    throw new BrokenScript();
                }
                System.out.println("Entered value is incorrect. Enter another one, please.");
                System.out.print("$");
            }
        }
        isEntered = false;
        View view = View.BAD;
        while(!isEntered) {
            try {
                if(input == System.in) {
                    System.out.println("Enter view value[BAD, GOOD, PARK, TERRIBLE]");
                    System.out.print("$");
                }
                String dummy = scanner.nextLine();
                view = switch (dummy) {
                    case ("BAD") -> View.BAD;
                    case ("GOOD") -> View.GOOD;
                    case ("PARK") -> View.PARK;
                    case ("TERRIBLE") -> View.TERRIBLE;
                    default -> throw new NumberFormatException();
                };
                isEntered = true;
            }catch (InputMismatchException | NumberFormatException e) {
                if(input != System.in){
                    throw new BrokenScript();
                }
                System.out.println("Entered value is incorrect. Enter another one, please.");
                System.out.print("$");
            }
        }
        isEntered = false;
        String name_of_house = "";
        while(!isEntered) {
            try {
                if(input == System.in){
                    System.out.println("Enter name of house value:");
                    System.out.print("$");
                }
                name_of_house = scanner.nextLine();
                if(name_of_house.equals("\s")) {
                    throw new InputMismatchException();
                }
                isEntered = true;
            }catch (InputMismatchException | NumberFormatException e) {
                if(input != System.in){
                    throw new BrokenScript();
                }
                System.out.println("Entered value is incorrect. Enter another one, please. (can't be null)");
                System.out.print("$");
            }
        }
        long year_of_house = 0;
        isEntered = false;
        while(!isEntered) {
            try {
                if(input == System.in) {
                    System.out.println("Enter year of house value:");
                    System.out.print("$");
                }
                year_of_house = Long.parseLong(scanner.nextLine());
                if(!checkYearOfHouse(year_of_house)) {
                    throw new InputMismatchException();
                }
                isEntered = true;
            }catch (InputMismatchException | NumberFormatException e) {
                if(input != System.in){
                    throw new BrokenScript();
                }
                System.out.println("Entered value is incorrect. Enter another one, please. (higher than zero)");
                System.out.print("$");
            }
        }
        int number_of_flat = 0;
        isEntered = false;
        while(!isEntered) {
            try {
                System.out.println("Enter number of flats value:");
                System.out.print("$");
                number_of_flat = Integer.parseInt(scanner.nextLine());
                while (!checkNumberOfFlats(number_of_flat)) {
                    throw new InputMismatchException();
                }
                isEntered = true;
            }catch (InputMismatchException | NumberFormatException e) {
                if(input != System.in){
                    throw new BrokenScript();
                }
                System.out.println("Entered value is incorrect. Enter another one, please. (higher than zero)");
                System.out.print("$");
            }
        }
        return new Flat(-1 ,name, new Coordinates(coordinate_x,coordinate_y), localDate, area, numbers_of_rooms, furniture, furnish, view, new House(name_of_house, year_of_house, number_of_flat));
    }
}
