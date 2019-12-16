package com.veniture.servlet;

import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.component.ComponentAccessor;
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
import com.veniture.util.JiraUtilClasses;
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

//    @JiraImport
//    private IssueService issueService;
//    @JiraImport
//    private ProjectService projectService;
//    @JiraImport
//    private ConstantsManager constantsManager;
//    @JiraImport
//    private RequestFactory requestFactory;
    @JiraImport
    private  SearchService searchService;
    @JiraImport
    private TemplateRenderer templateRenderer;
    @JiraImport
    private  JiraAuthenticationContext authenticationContext;
    private final Logger logger = LoggerFactory.getLogger(Priority.class);// The transition ID

    private static final String PRIORITIZATION_SCREEN_TEMPLATE = "/templates/prioritization.vm";

    public Priority(   SearchService searchService,
                       TemplateRenderer templateRenderer,
                       JiraAuthenticationContext authenticationContext) {
        this.searchService = searchService;
        this.templateRenderer = templateRenderer;
        this.authenticationContext = authenticationContext;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> context = new HashMap<String, Object>();
        String restriction = Optional.ofNullable(req.getParameter("restriction")).orElse("");
        JqlQueryParser jqlQueryParser = ComponentAccessor.getComponent(JqlQueryParser.class);
        Query conditionQuery;
        try {
            if (restriction.equals("gmy")){
                conditionQuery = jqlQueryParser.parseQuery(Constants.gmyJQL);
            }
            else if (restriction.equals("dp")) {
                conditionQuery = jqlQueryParser.parseQuery(Constants.departmanJQL);
            }
            else {
                conditionQuery = jqlQueryParser.parseQuery(Constants.DEMO_JQL);
            }

            SearchResults results = searchService.search(authenticationContext.getLoggedInUser(), conditionQuery, PagerFilter.getUnlimitedFilter());

            List<CustomField> customFieldsInProject = new JiraUtilClasses.GetCustomFieldsInSearchContext().invoke();
            context.put("issues", results.getResults());
            context.put("restriction", restriction);
            context.put("baseUrl",ComponentAccessor.getApplicationProperties().getString("jira.baseurl"));
            context.put("customFieldsInProject",customFieldsInProject);
            context.put("birimOncelikCF",ComponentAccessor.getCustomFieldManager().getCustomFieldObject(Constants.BIRIM_ONCELIK_ID_STRING));
            context.put("gmyOncelikCF",ComponentAccessor.getCustomFieldManager().getCustomFieldObject(Constants.GMY_ONCELIK_STRING));

        } catch (JqlParseException | SearchException e) {
            logger.error(e.getMessage());
        }

        resp.setContentType("text/html;charset=utf-8");
        templateRenderer.render(PRIORITIZATION_SCREEN_TEMPLATE,context,resp.getWriter());
    }
}
