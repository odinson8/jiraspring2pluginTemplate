package com.veniture.rest;


import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.json.JSONArray;
import com.atlassian.jira.util.json.JSONException;
import com.atlassian.jira.util.json.JSONObject;
import com.atlassian.jira.workflow.TransitionOptions;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.net.RequestFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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
import java.util.stream.Collectors;

import static com.veniture.util.functions.updateCfValueForSelectList;
import static com.veniture.util.functions.updateCustomFieldValue;


@Path("/rest")
public class rest {
    @JiraImport
    private RequestFactory requestFactory;
    @JiraImport
//    private ApplicationProperties applicationProperties;
    private IssueManager issueManager;
    private static final Logger logger = LoggerFactory.getLogger(rest.class);// The transition ID
//    private static final Gson GSON = new Gson();
    private static final IssueService ISSUE_SERVICE = ComponentAccessor.getIssueService();
    private static final ApplicationUser CURRENT_USER = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();

    public rest(RequestFactory requestFactory){
        this.requestFactory = requestFactory;
        this.issueManager= ComponentAccessor.getIssueManager();
    }

    @GET
    @Path("/transitionissues")
    public String transitionIssues(@Context HttpServletRequest req, @Context HttpServletResponse resp) {
        IssueService issueService = ComponentAccessor.getIssueService();
        String[] issueHtml = req.getParameterValues("issues");
        logger.error(issueHtml.toString());
        ArrayList<String> issues = (ArrayList<String>) Arrays.stream(issueHtml)
                .map(element -> element.substring(element.indexOf(">")+1,element.indexOf("<",7)))
                .collect(Collectors.toList());
        String[] action = req.getParameterValues("action");
        ApplicationUser currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        if (action[0].equals("approve")){
            issues.stream()
                    .forEach(issue->transitionIssue(issueService, currentUser, issueService.getIssue(currentUser, issue).getIssue(), Constants.ApproveWorkflowTransitionId));
        }
        else if (action[0].equals("decline")){
            issues.stream()
                    .forEach(issue->transitionIssue(issueService, currentUser, issueService.getIssue(currentUser, issue).getIssue(), Constants.DeclineWorkflowTransitionId));
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
        List<String> cfValues = new ArrayList<>();
        for (String issueKey:issueKeys){
            String cfValue;
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
//              This means Could not get CF value for this CF
            }
            cfValues.add(cfValue);
        }
        return String.join(",,,", cfValues);
    }

    @POST
    @Path("/setPriorityCfValuesInJira")
    public String setPriorityCfValuesInJira(@Context HttpServletRequest req, @Context HttpServletResponse resp) throws Exception {
        String[] jsonTable = req.getParameterValues("jsontable");
        String gmyOrBirim = null;
        try {
            gmyOrBirim = req.getParameterValues("gmyOrBirim")[0];
        } catch (Exception e) {
            gmyOrBirim = "";
            logger.error("gmyOrBirim parameter is null");
        }
        //logger.debug("jsonTable  " +jsonTable[0]);
        JSONArray tableAsJSONarray = jsonString2JsonArray(jsonTable[0]);
        ObjectMapper mapper = new ObjectMapper();

        //logger.debug(tableAsJSONarray.toString());
        int failTryCount=4;
        for (int i = 0; i < tableAsJSONarray.length(); i++) {
            try {
                parseJsonAndSetPriorityCFs(gmyOrBirim, tableAsJSONarray, mapper, i);
            } catch (Exception e) {
                failTryCount--;
                if (failTryCount>0){
                    parseJsonAndSetPriorityCFs(gmyOrBirim, tableAsJSONarray, mapper, i);
                }
                logger.error("JSONObject'i POJO'ya çevirirken hata oldu: " +e.getMessage());
            }
        }
        return null;
    }

    private void parseJsonAndSetPriorityCFs(String gmyOrBirim, JSONArray tableAsJSONarray, ObjectMapper mapper, int i) throws Exception {
        JSONObject jsonObj = tableAsJSONarray.getJSONObject(i);
        //ProjectsDetails projectsDetails = mapper.readValue(jsonObj.toString(),ProjectsDetails.class);

        //MutableIssue issue = ISSUE_SERVICE.getIssue(CURRENT_USER, jsonObj.getString("key")).getIssue();
        MutableIssue issue = issueManager.getIssueByKeyIgnoreCase(jsonObj.getString("key"));

        if (issue==null){
            logger.error("CANNOT GET ISSUE "+jsonObj.getString("key")+" CRITICAL ERROR");
            throw new Exception();
        }

        if(gmyOrBirim.equalsIgnoreCase("gmy")){
            if (jsonObj.getString("GM")==null){
                logger.error("GM priority is null");
                throw new Exception();
            }
            updateCustomFieldValue(issue, Constants.GMY_ONCELIK_ID,Double.valueOf(jsonObj.getString("GM")),CURRENT_USER);
            updateCfValueForSelectList(issue,Constants.genelOnceliklendirildiMiId, Constants.GENEL_TRUE_OPTION_ID_CanliVeniture,CURRENT_USER);
        }
        else if (gmyOrBirim.equalsIgnoreCase("dp")){
            if (jsonObj.getString("DP")==null){
                logger.error("DP priority is null");
                throw new Exception();
            }
            updateCustomFieldValue(issue,Constants.BIRIM_ONCELIK_ID,Double.valueOf(jsonObj.getString("DP")),CURRENT_USER);
            updateCfValueForSelectList(issue,Constants.onceliklendirildiMiId, Constants.TRUE_OPTION_ID_CanliVeniture,CURRENT_USER);
        }
        else {
            logger.error("Neither gmy nor dp restriction set");
        }
    }

    @GET
    @Path("/test")
    public String test() throws URIException {
        RemoteSearcher remoteSearcher =  new RemoteSearcher(requestFactory);
        return remoteSearcher.getTotalRemainingTimeInYearForTeam(7).toString();
    }

    private JSONArray jsonString2JsonArray(String responseString) {
        try {
            JSONArray jsonArr = new JSONArray(responseString);
            return jsonArr;
        } catch (JSONException e) {
            return null;
        }
//        JsonParser parser = new JsonParser();
//        JsonElement jsonElement = parser.parse(responseString);
//        JsonArray jsonArray = jsonElement.getAsJsonArray();
//        return jsonArray;
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
            logger.error("Issue transition is successful");

        } else {
            logger.error(result.getErrorCollection().toString());
        }
    }
}
