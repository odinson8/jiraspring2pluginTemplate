package com.veniture.servlet;

import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.bc.project.ProjectService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
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
import com.veniture.util.AddPrograms;
import com.veniture.util.GetCustomFieldsInExcel;
import com.veniture.util.ProgramEforCfs;
import com.veniture.util.TeamsWithRemainingTimes;
import model.CfWithValue;
import model.IssueWithCF;
import model.pojo.TempoTeams.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.veniture.constants.Constants.*;
import static com.veniture.util.functions.getCustomFieldValueFromIssue;

@Scanned
public class ProjectApprove extends HttpServlet {

    @JiraImport
    private IssueManager issueManager;
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
    public RequestFactory requestFactory;
    String action;

    private static final String LIST_ISSUES_TEMPLATE = "/templates/projectApprove.vm";
    public static final Logger logger = LoggerFactory.getLogger(ProjectApprove.class);
    // The transition ID

    public ProjectApprove(IssueManager issueManager, ProjectService projectService,
                          SearchService searchService,
                          TemplateRenderer templateRenderer,
                          JiraAuthenticationContext authenticationContext,
                          ConstantsManager constantsManager,
                          RequestFactory requestFactory) {
        this.issueManager = issueManager;
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
       // Query projectApproveQuery = ComponentAccessor.getComponent(JqlQueryParser.class).parseQuery(DEVORTAMI_TEST_SORGUSU);
        Query projectApproveQuery = ComponentAccessor.getComponent(JqlQueryParser.class).parseQuery(ProjectApproveJQL);

        SearchResults<Issue> IssueResults = searchService.search(authenticationContext.getLoggedInUser(),projectApproveQuery , PagerFilter.getUnlimitedFilter());
        List<CustomField> customFieldsInProject = new GetCustomFieldsInExcel().invoke();
        List<Team> teams= new TeamsWithRemainingTimes(this).invoke();
        context.put("issuesWithCF",getIssueWithCFS(IssueResults, customFieldsInProject));
        context.put("customFieldsInProject", customFieldsInProject);
        context.put("baseUrl", JIRA_BASE_URL);
        context.put("teams", teams);
        context = new ProgramEforCfs(context).invoke();
        context = new AddPrograms(context, teams).invoke();
        //context.put("programs", programsWithCapacities);
        //context.put("projectCFs",getCustomFieldsInProject(Constants.ProjectId));
        return context;
    }

    private List<IssueWithCF> getIssueWithCFS(SearchResults<Issue> results, List<CustomField> customFieldsInProject) {
        List<IssueWithCF> issuesWithCF= new ArrayList<>();
        for (Issue issue : results.getResults()) {
            MutableIssue issueFull = issueManager.getIssueByKeyIgnoreCase(issue.getKey());
            ArrayList<CfWithValue> customFieldsWithValues= new ArrayList<>();
            for (CustomField customField:customFieldsInProject){
                try{
//                    ofBizCustomFieldValuePersister.getValues(cfStable,issueFull.getId(), PersistenceFieldType.TYPE_UNLIMITED_TEXT);
                    customFieldsWithValues.add(new CfWithValue(customField,getCustomFieldValueFromIssue(issueFull,customField.getIdAsLong())));
                }
                catch (Exception e){
                    customFieldsWithValues.add(new CfWithValue(customField," "));
                    //Bu satırı commentledim,çünkü çok fazla log basiyor.Log dosyasini çöp yaptı.Her boş cf için log atiyor sanırım.... logger.error("Error at getIssueWithCFS= " +e.getMessage());
                }
            }
            IssueWithCF issueWithCF= new IssueWithCF(issueFull,customFieldsWithValues);
            int departmanOnceligi = 0;
            try {
                departmanOnceligi = Integer.parseInt(getCustomFieldValueFromIssue(issueFull, 11403L));
            } catch (Exception e) {
                logger.error("Cannot get and set  departmanOnceligi ");
                e.printStackTrace();
            }
            issueWithCF.setDepartmanOnceligi(departmanOnceligi);
            int gmyOnceligi = 0;
            try {
                gmyOnceligi = Integer.parseInt(getCustomFieldValueFromIssue(issueFull, 11501L));
            } catch (Exception e) {
                logger.error("Cannot get and set gmyOnceligi ");
            }
            issueWithCF.setGmyOnceligi(gmyOnceligi);
            issueWithCF.setProjeYili(2019);
            issuesWithCF.add(issueWithCF);
        }
        return issuesWithCF;
    }

    //    private Map<String, Object> addIssuesToTheContext(Map<String, Object> context, String JQL, JqlQueryParser jqlQueryParser, CustomField kapasiteAbapCf,CustomField kapasiteSapCf,CustomField gerekliAbapEforCf,CustomField gerekliSapEforCf) throws SearchException, JqlParseException {
//        try {
//            Query conditionQuery;
//                    switch (action) {
//                case "WFA":
//                    conditionQuery = jqlQueryParser.parseQuery(Constants.WFA);
//                    break;
//                case "PLANLAMA":
//                    conditionQuery = jqlQueryParser.parseQuery(Constants.PLANLAMA);
//                    break;
//                case "SATISARTTIRAN":
//                    conditionQuery = jqlQueryParser.parseQuery(Constants.SATISARTTIRAN);
//                    break;
//                default:
//                     //conditionQuery = jqlQueryParser.parseQuery(ProjectApproveJQL);
//                   // conditionQuery = jqlQueryParser.parseQuery(DEVORTAMI_TEST_SORGUSU);
//            }
//
////            for (Issue issue : issues) {
////                Object kapasiteABAPvalue = kapasiteAbapCf.getValue(issue);
////            }
////            IssueService.IssueResult kapasiteIssue = issueService.getIssue(authenticationContext.getLoggedInUser(),"FP-17");
//
//
//            //List<CustomField> customFieldsInProject = new ArrayList<>();
////            customFieldsInProject.add(kapasiteAbapCf);
////            customFieldsInProject.add(kapasiteSapCf);
//
//
//
//
////            context.put("kapasiteAbap",kapasiteIssue.getIssue().getCustomFieldValue(kapasiteAbapCf));
////            context.put("kapasiteSap",kapasiteIssue.getIssue().getCustomFieldValue(kapasiteSapCf));
////            context.put("gerekliAbapEforCf", gerekliAbapEforCf);
////         // context.put("issueService", issueService);
////         // context.put("user", authenticationContext.getLoggedInUser());
////            context.put("gerekliSapEforCf", gerekliSapEforCf);
//
//            return context;
//
//        } catch (JqlParseException e) {
//            logger.error("JqlParseException error at project approve" + e.getParseErrorMessage());
//            e.printStackTrace();
//            throw e;
//        }
//    }
}
