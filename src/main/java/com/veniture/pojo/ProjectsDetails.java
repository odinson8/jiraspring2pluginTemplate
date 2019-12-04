
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

    public String getIssueKey() {
        return issueKey;
    }
    public String getDepartmentPriority() {
        return departmentPriority;
    }
}
