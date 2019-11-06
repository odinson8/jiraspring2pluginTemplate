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
import com.atlassian.templaterenderer.TemplateRenderer;
import com.veniture.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.veniture.constants.Constants.kapasiteAbapCfId;

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

    private static final String LIST_ISSUES_TEMPLATE = "/templates/list.vm";

    public IssuesTable(IssueService issueService, ProjectService projectService,
                       SearchService searchService,
                       TemplateRenderer templateRenderer,
                       JiraAuthenticationContext authenticationContext,
                       ConstantsManager constantsManager) {
        this.issueService = issueService;
        this.projectService = projectService;
        this.searchService = searchService;
        this.templateRenderer = templateRenderer;
        this.authenticationContext = authenticationContext;
        this.constantsManager = constantsManager;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

    private Map<String, Object> createContext() throws SearchException, JqlParseException {
        // String action = Optional.ofNullable(req.getParameter("actionType")).orElse("");
        Map<String, Object> context = new HashMap<String, Object>();
        JqlQueryParser jqlQueryParser = ComponentAccessor.getComponent(JqlQueryParser.class);

        CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();
        CustomField kapasiteAbapCf = customFieldManager.getCustomFieldObject(kapasiteAbapCfId);
        CustomField kapasiteSapCf = customFieldManager.getCustomFieldObject(Constants.kapasiteSapCfId);

        try {
            context = addIssuesToTheContext(context, jqlQueryParser, kapasiteAbapCf,kapasiteSapCf);
        } catch (SearchException e) {
            e.printStackTrace();
        } catch (JqlParseException e) {
            e.printStackTrace();
        }

        return context;
    }

    private Map<String, Object> addIssuesToTheContext(Map<String, Object> context, JqlQueryParser jqlQueryParser, CustomField kapasiteAbapCf,CustomField kapasiteSapCf) throws SearchException, JqlParseException {
        try {
            Query conditionQuery = jqlQueryParser.parseQuery(Constants.QUERY);
            SearchResults results = searchService.search(authenticationContext.getLoggedInUser(), conditionQuery, PagerFilter.getUnlimitedFilter());
            List<Issue> issues = results.getResults();
//            for (Issue issue : issues) {
//                Object kapasiteABAPvalue = kapasiteAbapCf.getValue(issue);
//            }
            context.put("issues", results.getResults());
            context.put("kapasiteAbapCf", kapasiteAbapCf);
            context.put("kapasiteSapCf", kapasiteSapCf);
            return context;

        } catch (JqlParseException | SearchException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
