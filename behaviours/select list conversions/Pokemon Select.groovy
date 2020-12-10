getFieldByName("Pokemon").convertToMultiSelect([ 
    ajaxOptions: [
        url : getBaseUrl() + "/rest/scriptrunner/latest/custom/getPokemons", 
        query: true, 
        minQueryLength: 4, 
        keyInputPeriod: 500, 
        formatResponse: "general", 
    ]
])