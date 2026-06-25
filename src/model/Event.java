package model;

public abstract class Event {

    private String id;
    private String name;
    private String venueId;
    private String organizerId;
    private String date;
    private double basePrice;
    private String createdAt;
    private String updatedAt;

    public Event(String id,
            String name,
            String venueId,
            String organizerId,
            String date,
            double basePrice,
            String createdAt,
            String updatedAt) {

        if (basePrice < 0) {
            throw new IllegalArgumentException(
                    "Harga tidak boleh negatif");
        }

        this.id = id;
        this.name = name;
        this.venueId = venueId;
        this.organizerId = organizerId;
        this.date = date;
        this.basePrice = basePrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public abstract String getEventType();

    public abstract double calculateTicketPrice(String category);

    public void displayInfo() {

        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Venue ID: " + venueId);
        System.out.println("Organizer ID: " + organizerId);
        System.out.println("Date: " + date);
        System.out.println("Base Price: " + basePrice);
        System.out.println("Created At: " + createdAt);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVenueId() {
        return venueId;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public String getDate() {
        return date;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return name + " [" + getEventType() + "]";
    }
}