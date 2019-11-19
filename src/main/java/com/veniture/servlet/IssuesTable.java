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
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.net.Request;
import com.atlassian.sal.api.net.RequestFactory;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.veniture.RemoteSearcher;
import com.veniture.constants.Constants;
import org.apache.commons.httpclient.URIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.veniture.constants.Constants.*;

@Scanned
public class IssuesTable extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(IssuesTable.class);

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

    private static final String LIST_ISSUES_TEMPLATE = "/templates/list.vm";

    public IssuesTable(IssueService issueService, ProjectService projectService,
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

        getAllTeamsfromTempo();

        action = Optional.ofNullable(req.getParameter("action")).orElse("");
        Map<String, Object> context = null;
        try {
            context = createContext();
        } catch (SearchException e) {
            e.printStackTrace();
        } catch (JqlParseException e) {
            e.printStackTrace();
        }

        resp.setContentType("text/html;charset=utf-8");

        templateRenderer.render(LIST_ISSUES_TEMPLATE, context, resp.getWriter());

    }


    private void getAllTeamsfromTempo() throws URIException {

        RemoteSearcher remoteSearcher =  new RemoteSearcher("asd",requestFactory);
        remoteSearcher.search("","");
        //Request request = requestFactory.createRequest(Request.MethodType.GET, getCurrentAppBaseUrl());
    }

//    public String getCurrentAppBaseUrl()
//    {
//        return applicationProperties.getBaseUrl();
//    }

    private Map<String, Object> createContext() throws SearchException, JqlParseException {
        // ).orElse("");
        Map<String, Object> context = new HashMap<String, Object>();
        JqlQueryParser jqlQueryParser = ComponentAccessor.getComponent(JqlQueryParser.class);

        CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();
        CustomField kapasiteAbapCf = customFieldManager.getCustomFieldObject(kapasiteAbapCfId);
        CustomField kapasiteSapCf = customFieldManager.getCustomFieldObject(kapasiteSapCfId);
        CustomField gerekliAbapEforCf = customFieldManager.getCustomFieldObject(gerekliAbapEforCfId);
        CustomField gerekliSapEforCf = customFieldManager.getCustomFieldObject(gerekliSapEforCfId);

        try {
            context = addIssuesToTheContext(context,action, jqlQueryParser, kapasiteAbapCf,kapasiteSapCf,gerekliAbapEforCf,gerekliSapEforCf);
        } catch (SearchException e) {
            e.printStackTrace();
        } catch (JqlParseException e) {
            e.printStackTrace();
        }

        return context;
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
                    conditionQuery = jqlQueryParser.parseQuery(Constants.WFA);
            }
            CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();

            SearchResults results = searchService.search(authenticationContext.getLoggedInUser(), conditionQuery, PagerFilter.getUnlimitedFilter());
            List<Issue> issues = results.getResults();
//            for (Issue issue : issues) {
//                Object kapasiteABAPvalue = kapasiteAbapCf.getValue(issue);
//            }
            IssueService.IssueResult kapasiteIssue = issueService.getIssue(authenticationContext.getLoggedInUser(),"FP-17");
            context.put("issues", results.getResults());
            context.put("kapasiteAbap",kapasiteIssue.getIssue().getCustomFieldValue(kapasiteAbapCf));
            context.put("kapasiteSap",kapasiteIssue.getIssue().getCustomFieldValue(kapasiteSapCf));
            context.put("gerekliAbapEforCf", gerekliAbapEforCf);
            context.put("gerekliSapEforCf", gerekliSapEforCf);

            return context;

        } catch (JqlParseException | SearchException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
