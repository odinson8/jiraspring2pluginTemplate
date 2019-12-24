package model.pojo;

public class Program {
    String name;
    Integer capacity = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void addMoreCapacity(Integer capacity) {
        this.capacity = this.capacity + capacity;
    }

    public Program(String name, Integer capacity) {
        this.name = name;
        this.capacity = capacity;
    }
}
