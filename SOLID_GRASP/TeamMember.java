class TeamMember implements Role {
    private String name;
    private String email;

    public TeamMember(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public void defineResponsibilities() {
        System.out.println("No specific responosibilities yet.");
    }

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }

}