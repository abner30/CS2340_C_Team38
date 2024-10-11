import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class Project {
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private List<Task> tasks;
    private List<TeamMember> teamMembers;

    public Project(String name, String description, String startDate, String endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tasks = new ArrayList<Task>();
        this.teamMembers = new ArrayList<TeamMember>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }
    public void removeTask(Task task) {
        tasks.remove(task);
    }
    public void addTeamMember(TeamMember member) {
        teamMembers.add(member);
    }
    public void removeTeamMember(TeamMember member) {
        teamMembers.remove(member);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public boolean isComplete() {
        boolean allComplete = true;
        for (int i = 0; i < tasks.size(); i++) {
            if (!tasks.get(i).isComplete()) {
                allComplete = false;
            }
        }
        return allComplete;
    }
    // other getters and setters
}