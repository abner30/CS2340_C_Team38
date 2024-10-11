interface Task {
    String getTitle();
    String getDescription();
    String getDueDate();
    TaskStatus getStatus();
    int getPriority();
    void setStatus(TaskStatus status);
    void complete();
    boolean isComplete();
}