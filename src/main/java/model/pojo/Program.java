package model.pojo;

public class Program {
    String name;
    Integer remainingCapacity = 0;
    Integer totalCapacity = 0;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getRemainingCapacity() { return remainingCapacity;}
    public Integer getTotalCapacity() { return totalCapacity; }

    public void addRemainingCapacity(Integer capacity) { this.remainingCapacity = this.remainingCapacity + capacity; }

    public void addTotalCapacity(Integer capacity) { this.totalCapacity = this.totalCapacity + capacity; }

    public Program(String name, Integer remaining) {
        this.name = name;
        this.remainingCapacity = remaining;
    }
}
