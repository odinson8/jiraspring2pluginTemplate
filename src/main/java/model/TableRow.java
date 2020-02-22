
package model;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.util.json.JSONException;
import com.atlassian.jira.util.json.JSONObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.veniture.util.ProgramEforCfs;

import java.util.List;

import static com.veniture.util.team2Program.getTotalRemainingCapacityOfAllPrograms;
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
        jo.put("isSelected", "--");
        jo.put("checkbox", "");
        jo.put("issue", issue.getKey());
        jo.put("summary", issue.getSummary());
        addEforJson(jo);
        //jo.put("totalCapacity",com.veniture.util.functions.calculateEmployeeCountFromHour(getTotalRemainingCapacityOfAllPrograms()));
        addExcelCfs(jo);
        jo.put("status",issue.getStatus().getName());
        return jo;
    }

    private void addExcelCfs(JSONObject jo) throws JSONException {
        for (CfWithValue cfWV:customFieldListWithValues){
            String cfName = cfWV.getCustomField().getName();
            String cfValue = cfWV.getValue();
            if (cfName.equals("Departman")){
                try {
//                    jo.put(cfName, cfValue.substring(cfValue.indexOf("{null=")+6 ,cfValue.lastIndexOf("}")).replace(", 1="," - "));
                    jo.put(cfName, cfValue.substring(cfValue.indexOf("{null=")+6 ,cfValue.lastIndexOf(", 1")));
                } catch (Exception e) {
                    jo.put(cfName, "-");
                }
            }else {
                jo.put(cfName, cfValue);
            }
        }
    }

    private void addEforJson(JSONObject jo) throws JSONException {
        CustomFieldManager customFieldManager= ComponentAccessor.getCustomFieldManager();
        for (CustomField cf:new ProgramEforCfs().eforCFs()) {
            String customFieldValueFromIssue;
            try {
                customFieldValueFromIssue = getCustomFieldValueFromIssue(issue, cf.getIdAsLong(),customFieldManager);
                //requiredMan = Double.valueOf(customFieldValueFromIssue);
                //requiredMan=com.veniture.util.functions.calculateEmployeeCountFromManDay(requiredMan);
            
            } catch (Exception e) {
                customFieldValueFromIssue="0";
                e.printStackTrace();
            }
            jo.put(cf.getId(), customFieldValueFromIssue);
        }
    }
}