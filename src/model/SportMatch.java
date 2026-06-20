package model;

public class SportMatch extends Event {

    private String team;

    public SportMatch(String id, String title,
            String location, double price,
            int capacity, String team) {

        super(id, title, location, price, capacity);

        this.team = team;
    }

    @Override
    public String getEventType() {
        return "Sport Match";
    }

    @Override
    public double calculateTicketPrice(String category) {

        if (category.equalsIgnoreCase("Tribune")) {
            return getPrice() * 1.0;
        }

        if (category.equalsIgnoreCase("VIP")) {
            return getPrice() * 2.0;
        }

        if (category.equalsIgnoreCase("VVIP")) {
            return getPrice() * 5.0;
        }

        return getPrice();
    }

    public String getTeam() {
        return team;
    }
}