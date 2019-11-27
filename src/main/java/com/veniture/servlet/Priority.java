package com.veniture.servlet;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.bc.project.ProjectService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.issue.Issue;
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
import java.util.Optional;

@Scanned
public class Priority extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(Priority.class);

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

    private static final String PRIORITIZATION_SCREEN_TEMPLATE = "/templates/prioritization.vm";

    public Priority(IssueService issueService, ProjectService projectService,
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
        Map<String, Object> context = new HashMap<String, Object>();
        action = Optional.ofNullable(req.getParameter("action")).orElse("");
        JqlQueryParser jqlQueryParser = ComponentAccessor.getComponent(JqlQueryParser.class);
        Query conditionQuery = null;
        try {
            conditionQuery = jqlQueryParser.parseQuery(Constants.PROJECTCARDS);
            SearchResults results = searchService.search(authenticationContext.getLoggedInUser(), conditionQuery, PagerFilter.getUnlimitedFilter());

            List<Issue> issues = results.getResults();
            context.put("issues", issues);
            for (Issue issue : issues) {
                // Object kapasiteABAPvalue = kapasiteAbapCf.getValue(issue);
            }
        } catch (JqlParseException | SearchException e) {
            e.printStackTrace();
        }

        resp.setContentType("text/html;charset=utf-8");
        templateRenderer.render(PRIORITIZATION_SCREEN_TEMPLATE,context,resp.getWriter());
    }
}
