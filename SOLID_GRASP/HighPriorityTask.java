
class HighPriorityTask extends BaseTask {

    public HighPriorityTask(String title, String description, String dueDate) {
        super(title, description, dueDate, 1); // highest priority
    }

    @Override
    public void setPriority(int priority) {
        System.out.println("This is a high priority task,so you cannot change its priorit");
    }
    // You can add behavior unique to high-priority tasks, if needed
}