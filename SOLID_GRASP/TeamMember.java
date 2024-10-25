import java.util.List;

class TeamMember implements Role {
    private String name;
    private String email;
    private List<Project> projects; //Track which projects the member is in

    public TeamMember(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public void defineResponsibilities() {
        System.out.println("No specific responsibilities yet.");
    }

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public List<Project> getProjects() {
        return projects;
    }

    public void joinProject(Project project) {
        if (projects.contains(project)) {
            projects.add(project);
            project.addTeamMember(this);
        } else {
            System.out.println(name + "has already join the project");
        }
    }

    public void leaveProject(Project project) {
        if (projects.contains(project)) {
            projects.remove(project);
            project.removeTeamMember(this);
        } else {
            System.out.println(name + "is not in this project");
        }
    }

}