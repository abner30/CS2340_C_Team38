import java.util.ArrayList;
import java.util.List;

class Project {
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private List<Task> tasks;
    private List<TeamMember> teamMembers;

    public Project(String name, String description,
                   String startDate, String endDate) {
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

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    //Potentially add getter for tasks and team members
    //Add to Strings to other classes to make it return something meaningful.
    public boolean isComplete() {
        boolean allComplete = true;
        for (Task task : tasks) {
            if (!task.isComplete()) {
                allComplete = false;
            }
        }
        return allComplete;
    }
    // other getters and setters
}
