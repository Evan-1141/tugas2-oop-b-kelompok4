package model;

public class SportMatch extends Event {

    private String team;

    public SportMatch(String id, String name, String venueId,
            String organizerId, String date,
            double basePrice, String createdAt, String updatedAt,
            String team) {

        super(id, name, venueId, organizerId, date,
                basePrice, createdAt, updatedAt);

        this.team = team;
    }

    @Override
    public String getEventType() {
        return "sport_match";
    }

    @Override
    public double calculateTicketPrice(String category) {

        if (category.equalsIgnoreCase("Tribune")) {
            return getBasePrice() * 1.0;
        }

        if (category.equalsIgnoreCase("VIP")) {
            return getBasePrice() * 2.5;
        }

        if (category.equalsIgnoreCase("VVIP")) {
            return getBasePrice() * 5.0;
        }

        return getBasePrice();
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Team: " + team;
    }
}