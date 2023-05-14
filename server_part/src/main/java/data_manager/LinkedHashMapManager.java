package data_manager;

import model.Flat;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class that change collection
 */
public class LinkedHashMapManager {
    LinkedHashMap<Integer, Flat> linkedHashMap;
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public LinkedHashMapManager(LinkedHashMap<Integer, Flat> linkedHashMap){
        this.linkedHashMap = linkedHashMap;
    }

    public LinkedHashMap<Integer, Flat> getLinkedHashMap() {
        return linkedHashMap;
    }

    public void setLinkedHashMap(LinkedHashMap<Integer, Flat> linkedHashMap) {
        this.linkedHashMap = linkedHashMap;
    }


    /**
     * @return free id for new element
     */
    public long get_new_id(){
        lock.readLock().lock();
        try {
            ArrayList<Long> id_list = new ArrayList<Long>();
            for(int key : linkedHashMap.keySet()){
                id_list.add(linkedHashMap.get(key).getId());
            }
            for(long i = 1; i < Long.MAX_VALUE; i++){
                if (!id_list.contains(i)){
                    return i;
                }
            }
            return 0;
        }finally {
            lock.readLock().unlock();
        }

    }

    /**
     * @param id
     * @return is id free or not
     */
    public boolean checkId(long id){
        lock.readLock().lock();
        try{
            ArrayList<Long> id_list = new ArrayList<Long>();
            for(int key : linkedHashMap.keySet()){
                id_list.add(linkedHashMap.get(key).getId());
            }
            if(id_list.contains(id)) {
                return true;
            }
            return false;
        }finally {
            lock.readLock().unlock();
        }

    }

    /**
     * @param new_key
     * @return whether element with this key in collection or not
     */
    public boolean checkKey(int new_key){
        lock.readLock().lock();
        try{

            if (linkedHashMap.keySet().contains(new_key)){
                return false;
            }
            return true;
        }finally {
            lock.readLock().unlock();
        }
    }

    /**
     * @param name
     * @return check correctness of value of name
     */
    public boolean checkName(String name){
        return !name.equals("");
    }

    /**
     * @param x
     * @return check correctness of value of coordinate_x
     */
    public boolean checkCoordinateX(float x){
        return !(x > 2);
    }
    /**
     * @param y
     * @return check correctness of value of coordinate_y
     */
    public boolean checkCoordinateY(Integer y){
        return y != null;
    }

    /**
     * @param area
     * @return check correctness of value of area
     */
    public boolean checkArea(Long area){
        return area >= 0;
    }

    /**
     * @param number_of_rooms
     * * @return check correctness of value of number of rooms
     */
    public boolean checkNumberOfRooms(long number_of_rooms){
        return !(number_of_rooms > 10 | number_of_rooms < 0);
    }

    /**
     * @param year_of_house
     * @return check correctness of value of year of house
     */
    public boolean checkYearOfHouse(Long year_of_house){
        return year_of_house >= 0;
    }


    /**
     * @param number_of_flats
     * @return check correctness of value of number of flat
     */
    public boolean checkNumberOfFlats(int number_of_flats){
        return number_of_flats >= 0;
    }

    /**
     * add element in collection
     * @param key
     * @param flat
     */
    public void addElement(int key, Flat flat){
        lock.writeLock().lock();
        try {
            linkedHashMap.put(key, flat);
        }finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * remove element from collection
     * @param key
     */
    public void removeElement(int key, ArrayList<Long> id_to_remove){
        lock.writeLock().lock();
        try{
            if(id_to_remove.contains(linkedHashMap.get(key).getId())) {
                linkedHashMap.remove(key);
            }
        }finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * update elements with given id
     * @param id
     * @param flat
     */
    public void updateElement(long id, Flat flat, ArrayList<Long> id_to_remove){
        lock.writeLock().lock();
        try {
            if (!id_to_remove.contains(id)) {
                return;
            }
            for (int key : linkedHashMap.keySet()) {
                if (id == linkedHashMap.get(key).getId()) {
                    linkedHashMap.replace(key, flat);
                    return;
                }
            }
        }finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * clear collection
     */
    public void clearCollection(ArrayList<Long> id_to_remove){
        lock.writeLock().lock();
        try {
            for (int key : linkedHashMap.keySet()) {
                if (id_to_remove.contains(linkedHashMap.get(key).getId())) {
                    linkedHashMap.remove(key);
                }
            }
        }finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * remove element with lower id
     * @param flat
     */
    public void removeLowerElements(Flat flat, ArrayList<Long> id_to_remove){
        lock.writeLock().lock();
        try {
            ArrayList<Integer> keys = new ArrayList<Integer>();
            keys.addAll(linkedHashMap.keySet());
            for (int key : keys) {
                if (flat.compareTo(linkedHashMap.get(key)) > 0 && id_to_remove.contains(linkedHashMap.get(key).getId())) {
                    linkedHashMap.remove(key);
                }
            }
        }finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * remove elements with lower keys
     * @param delete_key
     */
    public void removeLowerElementsByKey(int delete_key){
        lock.writeLock().lock();
        try {
            ArrayList<Integer> keys = new ArrayList<Integer>();
            keys.addAll(linkedHashMap.keySet());
            for(int key : keys){
                if(delete_key > key){
                    linkedHashMap.remove(key);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * remove elements with given number of rooms
     * @param number_of_rooms
     */
    public void removeElementByNumberOfRooms(long number_of_rooms, ArrayList<Long> id_to_remove){
        lock.writeLock().lock();
        try {
            ArrayList<Integer> keys = new ArrayList<Integer>();
            keys.addAll(linkedHashMap.keySet());
            for(int key : keys){
                if(number_of_rooms == linkedHashMap.get(key).getNumberOfRooms() && id_to_remove.contains(linkedHashMap.get(key).getId())){
                    linkedHashMap.remove(key);
                }
            }
        }finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * @return element with the lowest value of coordinates
     */
    public Flat giveSmallestCoordinates(){
        lock.readLock().lock();
        try {
            float min = Float.MAX_VALUE;
            Flat min_flat = new Flat();
            ArrayList<Integer> keys = new ArrayList<Integer>();
            keys.addAll(linkedHashMap.keySet());
            for(int key : keys){
                if(min > linkedHashMap.get(key).getCoordinates().getX() + linkedHashMap.get(key).getCoordinates().getY()){
                    min = linkedHashMap.get(key).getCoordinates().getX() + linkedHashMap.get(key).getCoordinates().getY();
                    min_flat = linkedHashMap.get(key);
                }
            }
            return min_flat;
        }finally {
            lock.readLock().unlock();
        }
    }

    /**
     * @param number_of_rooms
     * @return number of elements with given number of rooms
     */
    public String countElementsLessThanNumberOfRooms(long number_of_rooms){
        lock.readLock().lock();
        try {
            int amount = 0;
            ArrayList<Integer> keys = new ArrayList<Integer>();
            keys.addAll(linkedHashMap.keySet());
            for (int key : keys) {
                if (linkedHashMap.get(key).getNumberOfRooms() < number_of_rooms) {
                    amount++;
                }
            }
            return "There are " + amount + " elements with value of number of rooms lower than " + number_of_rooms;
        }finally {
            lock.readLock().unlock();
        }
    }

    /**
     * sort collection by id
     */
    public void sortCollection(){
        linkedHashMap = linkedHashMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors
                        .toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new));
    }
}
