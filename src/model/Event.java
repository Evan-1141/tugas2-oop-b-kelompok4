package model;

public abstract class Event {

    protected String id;
    protected String title;
    protected String location;
    protected double price;
    protected int capacity;

    public Event(String id, String title, String location,
            double price, int capacity) {

        if (price < 0) {
            throw new IllegalArgumentException(
                    "Harga tidak boleh negatif");
        }

        if (capacity < 0) {
            throw new IllegalArgumentException(
                    "Kapasitas tidak boleh negatif");
        }

        this.id = id;
        this.title = title;
        this.location = location;
        this.price = price;
        this.capacity = capacity;
    }

    public abstract String getEventType();

    public abstract double calculateTicketPrice(String category);

    public void displayInfo() {

        System.out.println("ID: " + id);
        System.out.println("Title: " + title);
        System.out.println("Location: " + location);
        System.out.println("Price: " + price);
        System.out.println("Capacity: " + capacity);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public double getPrice() {
        return price;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return title + " - " + location;
    }
}