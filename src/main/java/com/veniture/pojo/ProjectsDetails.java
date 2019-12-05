
package com.veniture.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjectsDetails {

    @SerializedName("key")
    @Expose
    private String issueKey;

    @SerializedName("DP")
    @Expose
    private String departmentPriority;

    @SerializedName("GM")
    @Expose
    private String GMYPriority;

    public String getIssueKey() {
        return issueKey;
    }
    public String getDepartmentPriority() {
        return departmentPriority;
    }
    public String getGMYPriority() {  return GMYPriority; }
}
