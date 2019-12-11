package com.veniture.util;

import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.search.SearchContext;
import com.atlassian.jira.jql.parser.JqlParseException;
import com.atlassian.jira.jql.parser.JqlQueryParser;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.veniture.constants.Constants;

import java.util.List;

public class JiraUtilClasses {

    public static class GetCustomFieldsInSearchContext {

        private JqlQueryParser jqlQueryParser= ComponentAccessor.getComponent(JqlQueryParser.class);
        private CustomFieldManager cfMgr=ComponentAccessor.getCustomFieldManager();
        private SearchService searchService=ComponentAccessor.getComponent(SearchService.class);
        private JiraAuthenticationContext authenticationContext =ComponentAccessor.getJiraAuthenticationContext();

        public List<CustomField> invoke() throws JqlParseException {
            SearchContext searchContext= searchService.getSearchContext(authenticationContext.getLoggedInUser(),jqlQueryParser.parseQuery(Constants.SC_SORGUSU));
            return cfMgr.getCustomFieldObjects(searchContext);
        }
    }
}
