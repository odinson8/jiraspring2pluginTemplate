package com.veniture.rest;


import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.index.IndexException;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.workflow.TransitionOptions;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.net.RequestFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import model.pojo.ProjectsDetails;
import com.veniture.RemoteSearcher;
import com.veniture.constants.Constants;
import org.apache.commons.httpclient.URIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Path("/rest")
public class rest {
    @JiraImport
    private RequestFactory requestFactory;
    @JiraImport
    private ApplicationProperties applicationProperties;
    private static final Logger logger = LoggerFactory.getLogger(rest.class);// The transition ID
    private static final Gson GSON = new Gson();
    private static final IssueService ISSUE_SERVICE = ComponentAccessor.getIssueService();
    private static final ApplicationUser CURRENT_USER = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();

    public rest(RequestFactory requestFactory){
        this.requestFactory = requestFactory;
    }

    @GET
    @Path("/transitionissues")
    public String transitionIssues(@Context HttpServletRequest req, @Context HttpServletResponse resp) {
        IssueService issueService = ComponentAccessor.getIssueService();
        String[] issues = req.getParameterValues("issues");
        String[] action = req.getParameterValues("action");
        ApplicationUser currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        if (action[0].equals("approve")){
            Arrays.stream(issues).forEach(issue->transitionIssue(issueService, currentUser, issueService.getIssue(currentUser, issue).getIssue(), Constants.ApproveWorkflowTransitionId));
        }
        else if (action[0].equals("decline")){
            Arrays.stream(issues).forEach(issue->transitionIssue(issueService, currentUser, issueService.getIssue(currentUser, issue).getIssue(), Constants.DeclineWorkflowTransitionId));
        }
        return "true";
    }

    @POST
    @Path("/getCfValueFromIssue")
    public String getCfValueFromIssue(@Context HttpServletRequest req, @Context HttpServletResponse resp) throws URIException {
        CustomField customField= ComponentAccessor.getCustomFieldManager().getCustomFieldObject(req.getParameterValues("customFieldId")[0]);
        return ISSUE_SERVICE.getIssue(CURRENT_USER,req.getParameterValues("issueKey")[0]).getIssue().getCustomFieldValue(customField).toString();
    }

    @POST
    @Path("/bulkGetCfValueFromIssue")
    public String bulkGetCfValueFromIssue(@Context HttpServletRequest req, @Context HttpServletResponse resp) throws URIException {
        CustomField customField= ComponentAccessor.getCustomFieldManager().getCustomFieldObject(req.getParameterValues("customFieldId")[0]);
        String issueKeysJoined = req.getParameterValues("issueKey")[0];
        String[] issueKeys = issueKeysJoined.split(",");
        List<String> cfValues = new ArrayList<String>();
        for (String issueKey:issueKeys){
            String cfValue= null;
            try {
                Issue issue= ISSUE_SERVICE.getIssue(CURRENT_USER,issueKey).getIssue();
                if (customField.getCustomFieldType().getName().equals("User Picker (single user)")){
                    ApplicationUser applicationUser=(ApplicationUser) issue.getCustomFieldValue(customField);
                    cfValue=applicationUser.getDisplayName();
                }else {
                    cfValue = issue.getCustomFieldValue(customField).toString();
                }
            } catch (Exception e) {
                cfValue = "-";
                e.printStackTrace();
            }
            cfValues.add(cfValue);
        }
        return String.join(",,,", cfValues); // "foo and bar and baz";
    }

    @POST
    @Path("/setPriorityCfValuesInJira")
    public String setPriorityCfValuesInJira(@Context HttpServletRequest req, @Context HttpServletResponse resp) throws URIException, IndexException {
        String[] jsontableString = req.getParameterValues("jsontable");
        JsonArray tableAsJsonArray = jsonString2JsonArray(jsontableString[0]);
        for (JsonElement jsonElement:tableAsJsonArray){
            ProjectsDetails projectsDetails = GSON.fromJson(jsonElement, ProjectsDetails.class);
            MutableIssue issue = ISSUE_SERVICE.getIssue(CURRENT_USER, projectsDetails.getIssueKey()).getIssue();
            com.veniture.util.functions.updateCustomFieldValue(issue,Constants.BIRIM_ONCELIK_ID,Double.valueOf(projectsDetails.getDepartmentPriority()),CURRENT_USER);
            com.veniture.util.functions.updateCustomFieldValue(issue,Constants.GMY_ONCELIK_ID,Double.valueOf(projectsDetails.getGMYPriority()),CURRENT_USER);
            com.veniture.util.functions.updateCfValueForSelectList(issue,Constants.onceliklendirildiMiId, Constants.TRUE_OPTION_ID_CanliVeniture,CURRENT_USER);
        }
        return null;
    }

    @GET
    @Path("/test")
    public String test() throws URIException {
        RemoteSearcher remoteSearcher =  new RemoteSearcher(requestFactory);
        return remoteSearcher.getTotalRemainingTimeInYearForTeam(7).toString();
    }

    private JsonArray jsonString2JsonArray(String responseString) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(responseString);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        return jsonArray;
    }

    private void transitionIssue(IssueService issueService, ApplicationUser currentUser, Issue issue, Integer workflowTransitionId) {

        final TransitionOptions transitionOptions = new TransitionOptions.Builder().skipPermissions().skipValidators().setAutomaticTransition().skipConditions().build();

        IssueService.TransitionValidationResult result = issueService.validateTransition(currentUser,
                issue.getId(),
                workflowTransitionId,
                issueService.newIssueInputParameters(),
                transitionOptions);

        if (result.isValid()) {
            issueService.transition(currentUser, result);
        } else {
            logger.error(result.getErrorCollection().toString());
        }
    }
}
