public class Schedule {
    public String getSchedule(int day) {
        return switch (day) {
            case 1, 2, 3, 4, 5, 6 -> Report.NO_SCHEDULE; // TODO change Report
            default -> Report.NO_SCHEDULE;
        };
    }
}
