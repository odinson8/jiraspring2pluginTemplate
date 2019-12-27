package com.veniture.util;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.search.SearchResults;
import com.veniture.servlet.ProjectApprove;
import model.CfWithValue;
import model.FloIssue;

import java.util.ArrayList;
import java.util.List;

import static com.veniture.util.functions.getCustomFieldValueFromIssue;

public class FloIssuesCreator {
    private ProjectApprove projectApprove;
    private SearchResults<Issue> results;
    private List<CustomField> customFieldsInProject;

    public FloIssuesCreator(ProjectApprove projectApprove, SearchResults<Issue> results, List<CustomField> customFieldsInProject) {
        this.projectApprove = projectApprove;
        this.results = results;
        this.customFieldsInProject = customFieldsInProject;
    }

    public List<FloIssue> invoke() {
        List<FloIssue> floIssues= new ArrayList<>();
        for (Issue issue : results.getResults()) {
            MutableIssue issueFull = projectApprove.issueManager.getIssueByKeyIgnoreCase(issue.getKey());
            ArrayList<CfWithValue> customFieldsWithValues= new ArrayList<>();
            for (CustomField customField:customFieldsInProject){
                try{
                    customFieldsWithValues.add(new CfWithValue(customField,getCustomFieldValueFromIssue(issueFull,customField.getIdAsLong())));
                }
                catch (Exception e){
                    customFieldsWithValues.add(new CfWithValue(customField," "));
                    //Bu satırı commentledim,çünkü çok fazla log basiyor.Log dosyasini çöp yaptı.Her boş cf için log atiyor sanırım.... logger.error("Error at getIssueWithCFS= " +e.getMessage());
                }
            }
            floIssues = setParametersForSlider(floIssues, issueFull, customFieldsWithValues);
        }
        return floIssues;
    }

    private List<FloIssue> setParametersForSlider(List<FloIssue> floIssues, MutableIssue issueFull, ArrayList<CfWithValue> customFieldsWithValues) {
        FloIssue floIssue = new FloIssue(issueFull,customFieldsWithValues);

        setDepartmanOnceligi(issueFull, floIssue);

        setGmyOnceligi(issueFull, floIssue);

        floIssue.setProjeYili(2019);
        floIssues.add(floIssue);
        return floIssues;
    }

    private void setDepartmanOnceligi(MutableIssue issueFull, FloIssue floIssue) {
        int departmanOnceligi = 0;
        try {
            departmanOnceligi = Integer.parseInt(getCustomFieldValueFromIssue(issueFull, 11403L));
        } catch (Exception e) {
            ProjectApprove.logger.error("Cannot get and set  departmanOnceligi ");
            e.printStackTrace();
        }
        floIssue.setDepartmanOnceligi(departmanOnceligi);
    }

    private void setGmyOnceligi(MutableIssue issueFull, FloIssue floIssue) {
        int gmyOnceligi = 0;
        try {
            gmyOnceligi = Integer.parseInt(getCustomFieldValueFromIssue(issueFull, 11501L));
        } catch (Exception e) {
            ProjectApprove.logger.error("Cannot get and set gmyOnceligi ");
        }
        floIssue.setGmyOnceligi(gmyOnceligi);
    }
}
