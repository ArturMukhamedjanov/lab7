package db_helper;

import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

//ssh -p 2222 s367422@se.ifmo.ru -L 5432:pg:5432
public class DBManager {
    private final Properties properties = new Properties();
    private InputStream fis = DBManager.class.getResourceAsStream("/config.properties");
    private String dbUrl;
    private String user;
    private String pass;
    private final int KEY_POSITION = 1;
    private final int ID_POSITION = 2;
    private final int NAME_POSITION = 3;
    private final int COORDX_POSITION = 4;
    private final int COORDY_POSITION = 5;
    private final int CREATIONDATE_POSITION = 6;
    private final int AREA_POSITION = 7;
    private final int NUMBERSOFROOMS_POSITION = 8;
    private final int FURNITURE_POSITION = 9;
    private final int FURNISH_POSITION = 10;
    private final int VIEW_POSITION = 11;
    private final int NAMEOFHOUSE_POSITION = 12;
    private final int YEAROFHOUSE_NOT_POSITION = 13;
    private final int NUMBEROFFLATSONFLOOR_POSITION = 14;
    private final int OWNERID_POSITION = 15;
    private LinkedHashMap<String, ArrayList<Long>> loginFlatsMap;
    private Connection connection;

    private static Logger LOGGER = LoggerFactory.getLogger(DBManager.class);

    public DBManager() throws SQLException, IOException {
        properties.load(fis);
        dbUrl = properties.getProperty("DB.URL");
        user = properties.getProperty("DB.USER");
        pass = properties.getProperty("DB.PASSWORD");
        connection = DriverManager.getConnection(dbUrl, user, pass);
        loginFlatsMap = new LinkedHashMap<String, ArrayList<Long>>();
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS s367422users "
                + "("
                + "id serial primary key,"
                + "login varchar(255) NOT NULL UNIQUE CHECK(login<>''),"
                + "password varchar(255) NOT NULL CHECK(password<>'')"
                + ");");
        statement.execute("CREATE TABLE IF NOT EXISTS s367422flats"
                + "("
                + "fkey integer NOT NULL,"
                + "id int primary key,"
                + "name varchar(100) NOT NULL CHECK(name<>''),"
                + "coordX FLOAT NOT NULL,"
                + "coordY integer NOT NULL ,"
                + "creationDate date NOT NULL,"
                + "area bigint NOT NULL ,"
                + "number_of_rooms bigint NOT NULL ,"
                + "furniture boolean NOT NULL,"
                + "furnish varchar(100) NOT NULL,"
                + "vieww varchar(100) NOT NULL,"
                + "name_of_house varchar(382) NOT NULL,"
                + "year_of_house bigint NOT NULL,"
                + "number_of_flats_on_floor bigint NOT NULL,"
                + "owner_id integer NOT NULL REFERENCES s367422users (id)"
                + ");");
        updateLoginFlatsMap();
    }

    public void updateLoginFlatsMap() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet collectionSet = statement.executeQuery("SELECT * FROM s367422users");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM s367422flats WHERE owner_id = ?");
        while (collectionSet.next()){
            int id = Integer.parseInt(collectionSet.getString("id"));
            String login = collectionSet.getString("login");
            preparedStatement.setInt(1, id);
            ResultSet id_set = preparedStatement.executeQuery();
            ArrayList<Long> flats_ids = new ArrayList<>();
            while (id_set.next()){
                flats_ids.add(Long.parseLong(id_set.getString("id")));
            }
            loginFlatsMap.put(login, flats_ids);
        }
    }

    public void addElement(int key, Flat flat, String login) throws SQLException {
        Statement statement = connection.createStatement();
        PreparedStatement get_owner_id = connection.prepareStatement("SELECT id FROM s367422users  WHERE login = ?");
        get_owner_id.setString(1, login);
        ResultSet user_id = get_owner_id.executeQuery();
        int owner_id = 0;
        while (user_id.next()){
            owner_id = Integer.parseInt(user_id.getString("id"));
        }
        PreparedStatement preparedStatement = connection.prepareStatement("insert into s367422flats values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        preparedStatement.setInt(KEY_POSITION, key);
        preparedStatement.setInt(ID_POSITION, (int)flat.getId());
        preparedStatement.setString(NAME_POSITION, flat.getName());
        preparedStatement.setFloat(COORDX_POSITION, flat.getCoordinates().getX());
        preparedStatement.setInt(COORDY_POSITION, flat.getCoordinates().getY());
        preparedStatement.setDate(CREATIONDATE_POSITION, Date.valueOf(flat.getCreationDate()));
        preparedStatement.setLong(AREA_POSITION, flat.getArea());
        preparedStatement.setLong(NUMBERSOFROOMS_POSITION, flat.getNumberOfRooms());
        preparedStatement.setBoolean(FURNITURE_POSITION, flat.isFurniture());
        preparedStatement.setString(FURNISH_POSITION, flat.getFurnish().toString());
        preparedStatement.setString(VIEW_POSITION, flat.getView().toString());
        preparedStatement.setString(NAMEOFHOUSE_POSITION, flat.getHouse().getName());
        preparedStatement.setLong(YEAROFHOUSE_NOT_POSITION, flat.getHouse().getYear());
        preparedStatement.setLong(NUMBEROFFLATSONFLOOR_POSITION, flat.getHouse().getNumberOfFlatsOnFloor());
        preparedStatement.setInt(OWNERID_POSITION, owner_id);
        preparedStatement.execute();
        ArrayList<Long> list = loginFlatsMap.get(login);
        list.add(flat.getId());
        loginFlatsMap.put(login, list);
        LOGGER.info("===Element was added successfully===");
    }

    public LinkedHashMap<Integer, Flat> loadCollection() throws SQLException {
        LinkedHashMap<Integer, Flat> result_map = new LinkedHashMap<>();
        String selectCollectionQuery = "SELECT * FROM s367422flats;";
        Statement statement = connection.createStatement();
        ResultSet collectionSet = statement.executeQuery(selectCollectionQuery);
        while (collectionSet.next()) {
            int key = Integer.parseInt(collectionSet.getString("fkey"));
            long id  = Long.parseLong(collectionSet.getString("id"));
            String name = collectionSet.getString("name");
            float coordinate_x = Float.parseFloat(collectionSet.getString("coordX"));
            Integer coordinate_y = Integer.parseInt(collectionSet.getString("coordY"));
            LocalDate creationDate = LocalDate.parse(collectionSet.getString("creationDate"));
            Long area = Long.parseLong(collectionSet.getString("area"));
            long numberOfRooms = Long.parseLong(collectionSet.getString("number_of_rooms"));
            boolean furniture = Boolean.parseBoolean(collectionSet.getString("furniture"));
            Furnish furnish = switch (collectionSet.getString("furnish")) {
                case ("DESIGNER") -> Furnish.DESIGNER;
                case ("FINE") -> Furnish.FINE;
                case ("BAD") -> Furnish.BAD;
                case ("LITTLE") -> Furnish.LITTLE;
                default -> throw new NumberFormatException();
            };
            View view = switch (collectionSet.getString("vieww")) {
                case ("BAD") -> View.BAD;
                case ("GOOD") -> View.GOOD;
                case ("PARK") -> View.PARK;
                case ("TERRIBLE") -> View.TERRIBLE;
                default -> throw new NumberFormatException();
            };
            String name_of_house = collectionSet.getString("name_of_house");
            long year_of_house = Long.parseLong(collectionSet.getString("year_of_house"));
            int numbers_of_flats = Integer.parseInt(collectionSet.getString("number_of_flats_on_floor"));
            Coordinates coordinates = new Coordinates(coordinate_x, coordinate_y);
            House house = new House(name_of_house, year_of_house, numbers_of_flats);
            Flat flat = new Flat(id, name, coordinates, creationDate, area, numberOfRooms, furniture, furnish, view, house);
            result_map.put(key, flat);
        }
        LOGGER.info("Collection loaded successfully. Loaded " + result_map.size() + " elements===");
        return result_map;
    }

    public ArrayList<Long> getFlatsId(String login){
        return loginFlatsMap.get(login);
    }

    public String checkLogin(String login) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT password FROM s367422users WHERE login = ?");
        preparedStatement.setString(1, login);
        ResultSet resultSet = preparedStatement.executeQuery();
        String password = null;
        while (resultSet.next()){
            password = resultSet.getString("password");
            password = org.apache.commons.codec.digest.DigestUtils.sha1Hex(password);
        }
        return password;
    }

    public boolean checkPassword(String login, String password) throws SQLException {
        password = org.apache.commons.codec.digest.DigestUtils.sha1Hex(password);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT password FROM s367422users WHERE login = ?");
        preparedStatement.setString(1, login);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            if(resultSet.getString("password").equals(password)){
                return true;
            }
        }
        return false;
    }
    public void registerNewUser(String login, String password) throws SQLException {
        password = org.apache.commons.codec.digest.DigestUtils.sha1Hex(password);
        PreparedStatement preparedStatement = connection.prepareStatement("Insert into s367422users(login, password) values(?,?)");
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        preparedStatement.execute();
        ArrayList<Long> empty_list = new ArrayList<Long>();
        loginFlatsMap.put(login, empty_list);
        LOGGER.info("===User was added===");
    }

    public ArrayList<Long> clearCollection(String login) throws SQLException {
        ArrayList<Long> id_to_remove = loginFlatsMap.get(login);
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM s367422flats WHERE id = ?");
        for (Long i : id_to_remove){
            preparedStatement.setLong(1, i);
            preparedStatement.execute();
        }
        ArrayList<Long> empty_list  = new ArrayList<>();
        loginFlatsMap.put(login, empty_list);
        LOGGER.info("===Elements of user " + login + " were removed===");
        return id_to_remove;
    }

    public ArrayList<Long> removeElementsByNumberOfRooms(long numbers_of_rooms, String login) throws SQLException {
        ArrayList<Long> id_to_remove = loginFlatsMap.get(login);
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM s367422flats WHERE number_of_rooms = ? and id = ?");
        preparedStatement.setLong(1, numbers_of_rooms);
        for (Long i : id_to_remove){
            preparedStatement.setLong(2, i);
            preparedStatement.execute();
        }
        ArrayList<Long> empty_list  = new ArrayList<>();
        LOGGER.info("===Elements of user " + login + " were removed===");
        updateLoginFlatsMap();
        return id_to_remove;
    }

    public ArrayList<Long> removeElement(int key, String login) throws SQLException {
        ArrayList<Long> id_to_remove = loginFlatsMap.get(login);
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM s367422flats WHERE fkey = ? and id = ?");
        preparedStatement.setInt(1, key);
        for (Long i : id_to_remove){
            preparedStatement.setLong(2, i);
            preparedStatement.execute();
        }
        ArrayList<Long> empty_list  = new ArrayList<>();
        loginFlatsMap.put(login, empty_list);
        updateLoginFlatsMap();
        return id_to_remove;
    }

    public ArrayList<Long> removeLowerElements(Flat flat, String login) throws SQLException {
        ArrayList<Long> id_to_remove = loginFlatsMap.get(login);
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM s367422flats WHERE number_of_rooms = ? and id = ?");
        preparedStatement.setLong(1, flat.getNumberOfRooms());
        for (Long i : id_to_remove){
            preparedStatement.setLong(2, i);
            preparedStatement.execute();
        }
        ArrayList<Long> empty_list  = new ArrayList<>();
        loginFlatsMap.put(login, empty_list);
        updateLoginFlatsMap();
        return id_to_remove;
    }

    public ArrayList<Long> removeLowerKeyElements(int key, String login) throws SQLException {
        ArrayList<Long> id_to_remove = loginFlatsMap.get(login);
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM s367422flats WHERE fkey < ? and id = ?");
        preparedStatement.setInt(1, key);
        for (Long i : id_to_remove){
            preparedStatement.setLong(2, i);
            preparedStatement.execute();
        }
        ArrayList<Long> empty_list  = new ArrayList<>();
        loginFlatsMap.put(login, empty_list);
        updateLoginFlatsMap();
        return id_to_remove;
    }

    public ArrayList<Long> updateElement(Long id, Flat flat, String login) throws SQLException {
        if(!getFlatsId(login).contains(id)){
            return new ArrayList<Long>();
        }
        PreparedStatement preparedStatement = connection.prepareStatement("update s367422flats set " +
                "name = ?, " +
                "coord_x = ?," +
                "coordX = ?," +
                "coordY = ?," +
                "creationDate date = ?," +
                "area = ?," +
                "number_of_rooms =?," +
                "furniture =?," +
                "furnish =?," +
                "vieww = ?," +
                "name_of_house = ?," +
                "year_of_house = ?," +
                "number_of_flats_on_floor = ?" +
                "where id = ?");
        preparedStatement.setString(NAME_POSITION - 2, flat.getName());
        preparedStatement.setFloat(COORDX_POSITION - 2, flat.getCoordinates().getX());
        preparedStatement.setInt(COORDY_POSITION - 2, flat.getCoordinates().getY());
        preparedStatement.setDate(CREATIONDATE_POSITION - 2, Date.valueOf(flat.getCreationDate()));
        preparedStatement.setLong(AREA_POSITION - 2, flat.getArea());
        preparedStatement.setLong(NUMBERSOFROOMS_POSITION - 2, flat.getNumberOfRooms());
        preparedStatement.setBoolean(FURNITURE_POSITION - 2, flat.isFurniture());
        preparedStatement.setString(FURNISH_POSITION - 2, flat.getFurnish().toString());
        preparedStatement.setString(VIEW_POSITION - 2, flat.getView().toString());
        preparedStatement.setString(NAMEOFHOUSE_POSITION - 2, flat.getHouse().getName());
        preparedStatement.setLong(YEAROFHOUSE_NOT_POSITION - 2, flat.getHouse().getYear());
        preparedStatement.setLong(NUMBEROFFLATSONFLOOR_POSITION - 2,flat.getHouse().getNumberOfFlatsOnFloor());
        preparedStatement.setLong(13, id);
        preparedStatement.execute();
        LOGGER.info("===Element was added successfully===");
        return loginFlatsMap.get(login);
    }
}
