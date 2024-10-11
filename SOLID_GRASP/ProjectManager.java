class ProjectManager extends TeamMember implements Role {
    public ProjectManager(String name, String email) {
        super(name, email);
    }

    @Override
    public void defineResponsibilities() {
        System.out.println("Oversee project, manage resources, etc.");
    }
}