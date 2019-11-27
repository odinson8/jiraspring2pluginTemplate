package com.veniture.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjectsDetails {

    @SerializedName("Issue key")
    @Expose
    private String issueKey;
    @SerializedName("Department Priority")
    @Expose
    private String departmentPriority;
    @SerializedName("Company Priority")
    @Expose
    private String companyPriority;

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public String getDepartmentPriority() {
        return departmentPriority;
    }

    public void setDepartmentPriority(String departmentPriority) {
        this.departmentPriority = departmentPriority;
    }

    public String getCompanyPriority() {
        return companyPriority;
    }

    public void setCompanyPriority(String companyPriority) {
        this.companyPriority = companyPriority;
    }

}