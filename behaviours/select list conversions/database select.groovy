getFieldByName("Secret Data").convertToSingleSelect([ 
    ajaxOptions: [
        url : getBaseUrl() + "/rest/scriptrunner/latest/custom/getSecretData", 
        query: true, 
        minQueryLength: 4, 
        keyInputPeriod: 500, 
        formatResponse: "general", 
    ]
])