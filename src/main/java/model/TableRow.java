
package model;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.util.json.JSONException;
import com.atlassian.jira.util.json.JSONObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.veniture.util.ProgramEforCfs;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

import java.util.List;
import java.util.Map;

import static com.veniture.util.functions.getCustomFieldValueFromIssue;

public class TableRow {
    @SerializedName("issue")
    @Expose
    private MutableIssue issue;

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

    public TableRow(MutableIssue issue, List<CfWithValue> customFieldListWithValues) {
        this.issue = issue;
        this.customFieldListWithValues = customFieldListWithValues;
    }

    public MutableIssue getIssue() {
        return issue;
    }

    public void setIssue(MutableIssue issue) {
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

    public JSONObject toJSON() throws JSONException {

        JSONObject jo = new JSONObject();
        jo.put("issue", issue.getKey());
        jo.put("summary", issue.getSummary());
        addEforJson(jo);
        addExcelCfs(jo);
        return jo;
    }

    private void addExcelCfs(JSONObject jo) throws JSONException {
        for (CfWithValue cfWV:customFieldListWithValues){
            jo.put(cfWV.getCustomField().getName(), cfWV.getValue());
        }
    }

    private void addEforJson(JSONObject jo) throws JSONException {
        for (CustomField cf:new ProgramEforCfs().berk()) {
            String customFieldValueFromIssue;
            try {
                customFieldValueFromIssue = getCustomFieldValueFromIssue(issue, cf.getIdAsLong());
            } catch (Exception e) {
                customFieldValueFromIssue="";
                e.printStackTrace();
            }
            jo.put(cf.getId(), customFieldValueFromIssue);
        }
    }
}