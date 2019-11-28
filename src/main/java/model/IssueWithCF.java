
package model;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class IssueWithCF {

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public IssueWithCF(Issue issue, Map<CustomField, String> customFieldListWithValues) {
        this.issue = issue;
        this.customFieldListWithValues = customFieldListWithValues;
    }

    @SerializedName("issue")
    @Expose
    private Issue issue;

    public Map<CustomField, String> getCustomFieldListWithValues() {
        return customFieldListWithValues;
    }

    public void setCustomFieldListWithValues(Map<CustomField, String> customFieldListWithValues) {
        this.customFieldListWithValues = customFieldListWithValues;
    }

    @SerializedName("customFieldListWithValues")
    @Expose
    private Map<CustomField,String> customFieldListWithValues;

}
