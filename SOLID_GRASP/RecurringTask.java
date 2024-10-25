
class RecurringTask extends BaseTask {
    private String recurrenceDate;

    public RecurringTask(String title, String description, String dueDate, int priority, String recurrenceDate) {
        super(title, description, dueDate, priority);
        this.recurrenceDate = recurrenceDate;
    }

    public String getRecurrenceDate() {
        return recurrenceDate;
    }

    // Override complete or other methods as necessary
}