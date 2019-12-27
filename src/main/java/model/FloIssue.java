
package model;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

import java.util.List;
import java.util.Map;

public class FloIssue {
    @SerializedName("issue")
    @Expose
    private Issue issue;

    @SerializedName("customFieldListWithValues")
    @Expose
    private List<CfWithValue> customFieldListWithValues;

    @SerializedName("projeYili")
    @Expose
    private Integer projeYili;

    @SerializedName("departmanOnceligi")
    @Expose
    private Integer departmanOnceligi;

    @SerializedName("gmyOnceligi")
    @Expose
    private Integer gmyOnceligi;

    public FloIssue(Issue issue, List<CfWithValue> customFieldListWithValues) {
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

    public Integer getProjeYili() {
        return projeYili;
    }

    public void setProjeYili(Integer projeYili) {
        this.projeYili = projeYili;
    }

    public Integer getDepartmanOnceligi() {
        return departmanOnceligi;
    }

    public void setDepartmanOnceligi(Integer departmanOnceligi) {
        this.departmanOnceligi = departmanOnceligi;
    }

    public Integer getGmyOnceligi() {
        return gmyOnceligi;
    }

    public void setGmyOnceligi(Integer gmyOnceligi) {
        this.gmyOnceligi = gmyOnceligi;
    }

    public void setCustomFieldListWithValues(List<CfWithValue> customFieldListWithValues) {
        this.customFieldListWithValues = customFieldListWithValues;
    }
}
