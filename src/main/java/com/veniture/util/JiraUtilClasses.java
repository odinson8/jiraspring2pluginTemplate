package com.veniture.util;

import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.search.SearchContext;
import com.atlassian.jira.jql.parser.JqlParseException;
import com.atlassian.jira.jql.parser.JqlQueryParser;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.veniture.constants.Constants;
import com.veniture.servlet.Priority;

import java.util.List;

public class JiraUtilClasses {

    public static class GetCustomFieldsInProjectContext {

        private JqlQueryParser jqlQueryParser= ComponentAccessor.getComponent(JqlQueryParser.class);
        private CustomFieldManager cfMgr=ComponentAccessor.getCustomFieldManager();
        private SearchService searchService1=ComponentAccessor.getComponent(SearchService.class);
        private JiraAuthenticationContext authenticationContext =ComponentAccessor.getJiraAuthenticationContext();

        public List<CustomField> invoke() throws JqlParseException {
            SearchContext searchContext= searchService1.getSearchContext(authenticationContext.getLoggedInUser(),jqlQueryParser.parseQuery(Constants.SC_SORGUSU));
            return cfMgr.getCustomFieldObjects(searchContext);
        }
    }
}
