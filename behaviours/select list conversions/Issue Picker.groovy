getFieldByName("Issue Picker").convertToSingleSelect([ 
    ajaxOptions: [
        url : getBaseUrl() + "/rest/scriptrunner-jira/latest/issue/picker",
        query: true,
        data: [
            currentJql  : "issuetype = Story", 
            label       : "Pick high priority issue in Support project", 
            showSubTasks: false, 
            max       : 7,
        ],
        formatResponse: "issue" 
    ],
    css: "max-width: 500px; width: 500px", 
])