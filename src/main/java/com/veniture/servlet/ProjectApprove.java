package com.veniture.servlet;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.bc.project.ProjectService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.issue.search.SearchResults;
import com.atlassian.jira.jql.parser.JqlParseException;
import com.atlassian.jira.jql.parser.JqlQueryParser;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.web.bean.PagerFilter;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.query.Query;
import com.atlassian.sal.api.net.RequestFactory;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.veniture.RemoteSearcher;
import com.veniture.constants.Constants;
import model.pojo.TempoTeams.Team;
import com.veniture.util.JiraUtilClasses;
import model.CfWithValue;
import model.IssueWithCF;
import org.apache.commons.httpclient.URIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.veniture.constants.Constants.*;
import static com.veniture.util.functions.getCustomFieldsInProject;

@Scanned
public class ProjectApprove extends HttpServlet {

    @JiraImport
    private IssueService issueService;
    @JiraImport
    private ProjectService projectService;
    @JiraImport
    private SearchService searchService;
    @JiraImport
    private TemplateRenderer templateRenderer;
    @JiraImport
    private JiraAuthenticationContext authenticationContext;
    @JiraImport
    private ConstantsManager constantsManager;
    @JiraImport
    private RequestFactory requestFactory;
    String action;

    private static final String LIST_ISSUES_TEMPLATE = "/templates/projectApprove.vm";
    private static final Logger logger = LoggerFactory.getLogger(ProjectApprove.class);// The transition ID

    public ProjectApprove(IssueService issueService, ProjectService projectService,
                          SearchService searchService,
                          TemplateRenderer templateRenderer,
                          JiraAuthenticationContext authenticationContext,
                          ConstantsManager constantsManager,
                          RequestFactory requestFactory) {
        this.issueService = issueService;
        this.projectService = projectService;
        this.searchService = searchService;
        this.templateRenderer = templateRenderer;
        this.authenticationContext = authenticationContext;
        this.constantsManager = constantsManager;
        this.requestFactory = requestFactory;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        action = Optional.ofNullable(req.getParameter("action")).orElse("");
        Map<String, Object> context = null;
        try {
            context = createContext();
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace().toString());
        }
        resp.setContentType("text/html;charset=utf-8");
        templateRenderer.render(LIST_ISSUES_TEMPLATE, context, resp.getWriter());
    }

    private Map<String, Object> createContext() throws JqlParseException, SearchException {

        Map<String, Object> context = new HashMap<String, Object>();

        SearchResults<Issue> IssueResults = searchService.search(authenticationContext.getLoggedInUser(), ComponentAccessor.getComponent(JqlQueryParser.class).parseQuery(TEST_SORGUSU), PagerFilter.getUnlimitedFilter());
        List<CustomField> customFieldsInProject = new JiraUtilClasses.GetCustomFieldsInSearchContext().invoke();
        List<Team> teams= getTeamsAndSetRemaining();
        context.put("issuesWithCF",getIssueWithCFS(IssueResults, customFieldsInProject));
        context.put("customFieldsInProject", customFieldsInProject);
        context.put("baseUrl",ComponentAccessor.getApplicationProperties().getString("jira.baseurl"));
        context.put("teams", teams);
        context = addEforCfs(context);
    //        context.put("programs", teams.stream().map(Team::getProgram).collect(Collectors.toSet()));
        context.put("projectCFs",getCustomFieldsInProject(Constants.ProjectId));
        return context;
    }

    private Map<String, Object> addEforCfs (Map<String, Object> context){
        context.put("ABAPeforCf",ComponentAccessor.getCustomFieldManager().getCustomFieldObject(ABAPeforCfId));
        context.put("ANeforCfId",ComponentAccessor.getCustomFieldManager().getCustomFieldObject(ANeforCfId));
        context.put("BIeforCfId",ComponentAccessor.getCustomFieldManager().getCustomFieldObject(BIeforCfId));
        context.put("ECeforCfId",ComponentAccessor.getCustomFieldManager().getCustomFieldObject(ECeforCfId));
        context.put("NSeforCfId",ComponentAccessor.getCustomFieldManager().getCustomFieldObject(NSeforCfId));
        context.put("OPeforCfId",ComponentAccessor.getCustomFieldManager().getCustomFieldObject(OPeforCfId));
        context.put("PMOeforCfId",ComponentAccessor.getCustomFieldManager().getCustomFieldObject(PMOeforCfId));
        context.put("SapModEforCfId",ComponentAccessor.getCustomFieldManager().getCustomFieldObject(SapModEforCfId));
        context.put("SDeforCfId",ComponentAccessor.getCustomFieldManager().getCustomFieldObject(SDeforCfId));
        context.put("UDeforCfId",ComponentAccessor.getCustomFieldManager().getCustomFieldObject(UDeforCfId));
        context.put("YGeforCfId",ComponentAccessor.getCustomFieldManager().getCustomFieldObject(YGeforCfId));

        return context;
    }

    private List<Team> getTeamsAndSetRemaining() {
        List<Team> teams2 = new ArrayList<>();

        try {
            RemoteSearcher remoteSearcher =  new RemoteSearcher(requestFactory);
            List<Team> teams=remoteSearcher.getAllTeams();
            for (Team team:teams){
                if (!team.getName().contains("Team")){
                    //Gereksiz takimlar vardı onları silmek için yaptım
                    team.setRemainingInAYear(remoteSearcher.getTotalRemainingTimeInYearForTeam(team.getId()));
                    teams2.add(team);
                }
            }
        } catch (Exception e) {
            logger.error("Error at "+"getTeamsAndSetRemaining");
        }
        return teams2;
    }

    private List<IssueWithCF> getIssueWithCFS(SearchResults<Issue> results, List<CustomField> customFieldsInProject) {
        List<IssueWithCF> issuesWithCF= new ArrayList<>();
        for (Issue issue : results.getResults()){
            Issue issueFull = issueService.getIssue(authenticationContext.getLoggedInUser(),issue.getId()).getIssue();
            ArrayList<CfWithValue> customFieldsWithValues= new ArrayList<>();
            for (CustomField customField:customFieldsInProject){
                try{
                    customFieldsWithValues.add(new CfWithValue(customField,issueFull.getCustomFieldValue(customField).toString()));
                }
                catch (Exception e){
                    customFieldsWithValues.add(new CfWithValue(customField," "));
                    logger.error(e.getMessage());
                }
            }
            IssueWithCF issueWithCF= new IssueWithCF(issueFull,customFieldsWithValues);
            issuesWithCF.add(issueWithCF);
        }
        return issuesWithCF;
    }

    private Map<String, Object> addIssuesToTheContext(Map<String, Object> context, String JQL, JqlQueryParser jqlQueryParser, CustomField kapasiteAbapCf,CustomField kapasiteSapCf,CustomField gerekliAbapEforCf,CustomField gerekliSapEforCf) throws SearchException, JqlParseException {
        try {
            Query conditionQuery;
                    switch (action) {
                case "WFA":
                    conditionQuery = jqlQueryParser.parseQuery(Constants.WFA);
                    break;
                case "PLANLAMA":
                    conditionQuery = jqlQueryParser.parseQuery(Constants.PLANLAMA);
                    break;
                case "SATISARTTIRAN":
                    conditionQuery = jqlQueryParser.parseQuery(Constants.SATISARTTIRAN);
                    break;
                default:
                    conditionQuery = jqlQueryParser.parseQuery(TEST_SORGUSU);
            }

//            for (Issue issue : issues) {
//                Object kapasiteABAPvalue = kapasiteAbapCf.getValue(issue);
//            }
//            IssueService.IssueResult kapasiteIssue = issueService.getIssue(authenticationContext.getLoggedInUser(),"FP-17");


            //List<CustomField> customFieldsInProject = new ArrayList<>();
//            customFieldsInProject.add(kapasiteAbapCf);
//            customFieldsInProject.add(kapasiteSapCf);




//            context.put("kapasiteAbap",kapasiteIssue.getIssue().getCustomFieldValue(kapasiteAbapCf));
//            context.put("kapasiteSap",kapasiteIssue.getIssue().getCustomFieldValue(kapasiteSapCf));
//            context.put("gerekliAbapEforCf", gerekliAbapEforCf);
//         // context.put("issueService", issueService);
//         // context.put("user", authenticationContext.getLoggedInUser());
//            context.put("gerekliSapEforCf", gerekliSapEforCf);

            return context;

        } catch (JqlParseException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
