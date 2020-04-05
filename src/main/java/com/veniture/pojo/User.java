package com.veniture.pojo;

import java.util.Collection;
import java.util.List;

public class User {

    public User(String fullName, String username, String email, Collection<String> groups, List<String> directory, Boolean isActive) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.groups = groups;
        this.directory = directory;
        this.isActive = isActive;
    }

    public User() {

    }
    private String fullName;
    private String username;
    private String email;
    private Collection<String> groups;
    private List<String> directory;
    private Boolean isActive;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<String> getGroups() {
        return groups;
    }

    public void setGroups(Collection<String> groups) {
        this.groups = groups;
    }

    public List<String> getDirectory() {
        return directory;
    }

    public void setDirectory(List<String> directory) {
        this.directory = directory;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
