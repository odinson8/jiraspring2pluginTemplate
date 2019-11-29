
package com.veniture.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjectsDetails {

    @SerializedName("Issue key")
    @Expose
    private String issueKey;
    @SerializedName("Summary")
    @Expose
    private String summary;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Department")
    @Expose
    private String department;
    @SerializedName("GMYPriority")
    @Expose
    private String gMYPriority;
    @SerializedName("DepartmentPriority")
    @Expose
    private String departmentPriority;

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getGMYPriority() {
        return gMYPriority;
    }

    public void setGMYPriority(String gMYPriority) {
        this.gMYPriority = gMYPriority;
    }

    public String getDepartmentPriority() {
        return departmentPriority;
    }

    public void setDepartmentPriority(String departmentPriority) {
        this.departmentPriority = departmentPriority;
    }

}
