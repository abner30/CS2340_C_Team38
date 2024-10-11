abstract class BaseTask implements Task {
    protected String title;
    protected String description;
    protected String dueDate;
    protected TaskStatus status;
    protected int priority;

    public BaseTask(String title, String description, String dueDate, int priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = TaskStatus.PENDING;
    }

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getDueDate() {
        return dueDate;
    }
    public TaskStatus getStatus() {
        return status;
    }
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void complete() {
        setStatus(TaskStatus.COMPLETED);
    }

    public boolean isComplete() {
        return status.compareTo(TaskStatus.COMPLETED) == 0;
    }

    // other methods can be overridden by subclasses
}