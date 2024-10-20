class Developer extends TeamMember implements Role {
    public Developer(String name, String email) {
        super(name, email);
    }

    @Override
    public void defineResponsibilities() {
        System.out.println("Write code, fix bugs, etc.");
    }
}