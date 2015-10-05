package my.cool.demo;

public class CreateRepositoryDTO  {
    
    private String name;
    private String description;
    private boolean create_empty_commit;

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

    public boolean isCreate_empty_commit() {
        return create_empty_commit;
    }

    public void setCreate_empty_commit(boolean create_empty_commit) {
        this.create_empty_commit = create_empty_commit;
    }

}