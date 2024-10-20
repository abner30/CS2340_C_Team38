interface Completable {
    void setStatus(TaskStatus status);
    void complete();
    boolean isComplete();
}