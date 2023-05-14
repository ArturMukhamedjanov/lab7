package model;

import changingClasses.Arguments;

import java.io.*;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.util.Objects;

public class Flat implements Comparable<Flat>, Serializable{
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long area; //Значение поля должно быть больше 0
    private long numberOfRooms; //Максимальное значение поля: 10, Значение поля должно быть больше 0
    private boolean furniture;
    private Furnish furnish; //Поле не может быть null
    private View view; //Поле может быть null
    private House house; //Поле может быть null

    public Flat(){

    }

    public Flat(long id, String name, Coordinates coordinates, LocalDate creationDate, Long area, long numberOfRooms, boolean furniture, Furnish furnish, View view, House house) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.furniture = furniture;
        this.furnish = furnish;
        this.view = view;
        this.house = house;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Long getArea() {
        return area;
    }

    public void setArea(Long area) {
        this.area = area;
    }

    public long getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(long numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public boolean isFurniture() {
        return furniture;
    }

    public void setFurniture(boolean furniture) {
        this.furniture = furniture;
    }

    public Furnish getFurnish() {
        return furnish;
    }

    public void setFurnish(Furnish furnish) {
        this.furnish = furnish;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }
    @Override
    public int compareTo(Flat flat){
        return (int)this.getNumberOfRooms() - (int)flat.getNumberOfRooms();
    }

    @Override
    public String toString() {
        return "Flat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", area=" + area +
                ", numberOfRooms=" + numberOfRooms +
                ", furniture=" + furniture +
                ", furnish=" + furnish +
                ", view=" + view +
                ", house=" + house +
                '}';
    }

    public static ByteBuffer toBuffer(Flat obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();
        ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
        oos.close();
        baos.close();
        return buffer;
    }

    public static Flat toFlat(ByteBuffer buffer) throws IOException, ClassNotFoundException {
        byte[] arr = new byte[buffer.remaining()];
        buffer.get(arr);
        ByteArrayInputStream byteStream = new ByteArrayInputStream(arr);
        ObjectInputStream objStream = new ObjectInputStream(byteStream);
        return (Flat)objStream.readObject();
    }

    /*public String createString(){
        String s = "";
        s = s + this.getName() + ",";
        s = s + this.getCoordinates().getX() + ",";
        s = s + this.getCoordinates().getY() + ",";
        s = s + this.getCreationDate() + ",";
        s = s + this.getArea() + ",";
        s = s + this.getNumberOfRooms() + ",";
        s = s + this.isFurniture() + ",";
        s = s + this.getFurnish().toString() + ",";
        s = s + this.getView().toString() + ",";
        s = s + this.getHouse().getName() + ",";
        s = s + this.getHouse().getYear() + ",";
        s = s + this.getHouse().getNumberOfFlatsOnFloor();
        return s;
    }

    public static Flat createFlat(String s){
        String[] flat_info = s.split(",");
        String name = flat_info[0];
        System.out.println("name:" + name);
        float coordinate_x = Float.parseFloat(flat_info[1]);
        System.out.println("x:" + coordinate_x);
        Integer coordinate_y = Integer.parseInt(flat_info[2]);
        System.out.println("y:" + coordinate_y);
        LocalDate creationDate = LocalDate.parse(flat_info[3]);
        System.out.println("ld:" + creationDate);
        Long area = Long.parseLong(flat_info[4]);
        System.out.println("area:" + area);
        long numberOfRooms = Long.parseLong(flat_info[5]);
        System.out.println("nor" + numberOfRooms);
        boolean furniture = Boolean.parseBoolean(flat_info[6]);
        System.out.println("furniture" + furniture);
        Furnish furnish = switch (flat_info[7]) {
            case ("DESIGNER") -> Furnish.DESIGNER;
            case ("FINE") -> Furnish.FINE;
            case ("BAD") -> Furnish.BAD;
            case ("LITTLE") -> Furnish.LITTLE;
            default -> throw new NumberFormatException();
        };
        System.out.println("furnish" + furnish);
        View view = switch (flat_info[8]) {
            case ("BAD") -> View.BAD;
            case ("GOOD") -> View.GOOD;
            case ("PARK") -> View.PARK;
            case ("TERRIBLE") -> View.TERRIBLE;
            default -> throw new NumberFormatException();
        };
        System.out.println("view" + view);
        String name_of_house = flat_info[9];
        System.out.println("nameofhouse" + name_of_house);
        long year_of_house = Long.parseLong(flat_info[10]);
        System.out.println("yof" + year_of_house);
        int numbers_of_flats = Integer.parseInt(flat_info[11]);
        System.out.println("nof" + numbers_of_flats);
        Coordinates coordinates = new Coordinates(coordinate_x, coordinate_y);
        House house = new House(name_of_house, year_of_house, numbers_of_flats);
        Flat flat = new Flat(-1, name, coordinates, creationDate, area, numberOfRooms, furniture, furnish, view, house);
        return flat;
    }*/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flat flat)) return false;
        return getId() == flat.getId() && getNumberOfRooms() == flat.getNumberOfRooms() && isFurniture() == flat.isFurniture() && Objects.equals(getName(), flat.getName()) && Objects.equals(getCoordinates(), flat.getCoordinates()) && Objects.equals(getCreationDate(), flat.getCreationDate()) && Objects.equals(getArea(), flat.getArea()) && getFurnish() == flat.getFurnish() && getView() == flat.getView() && Objects.equals(getHouse(), flat.getHouse());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCoordinates(), getCreationDate(), getArea(), getNumberOfRooms(), isFurniture(), getFurnish(), getView(), getHouse());
    }

}
