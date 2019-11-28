
package model;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

import java.util.List;
import java.util.Map;

public class IssueWithCF {
    @SerializedName("issue")
    @Expose
    private Issue issue;

    @SerializedName("customFieldListWithValues")
    @Expose
    private List<CfWithValue> customFieldListWithValues;

    public IssueWithCF(Issue issue, List<CfWithValue> customFieldListWithValues) {
        this.issue = issue;
        this.customFieldListWithValues = customFieldListWithValues;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public List<CfWithValue> getCustomFieldListWithValues() {
        return customFieldListWithValues;
    }

    public void setCustomFieldListWithValues(List<CfWithValue> customFieldListWithValues) {
        this.customFieldListWithValues = customFieldListWithValues;
    }
}
