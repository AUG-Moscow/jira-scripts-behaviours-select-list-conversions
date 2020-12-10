import com.onresolve.scriptrunner.runner.rest.common.CustomEndpointDelegate
import groovy.json.JsonBuilder
import groovy.transform.BaseScript
import groovyx.net.http.RESTClient
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.Response

@BaseScript CustomEndpointDelegate delegate
getPokemons(httpMethod: "GET", groups: ["jira-administrators"]) { MultivaluedMap queryParams ->
    def rt = [:]
    def query = queryParams.getFirst("query") as String
    
    if (!query) {
        return Response.ok(new JsonBuilder(rt).toString()).build();
    } 
    
    query = query.toLowerCase()
    
    // клиент для работы с рест сервисом
    def pokeapi = new RESTClient("https://pokeapi.co/api/v2/")
    
    def getAvatar = { name ->
        def innerResp = pokeapi.get( path: "pokemon/${name}")
        innerResp?.data?.sprites?.front_default ?: ""
    }
    
    // получаем список покемонов и фильтруем только тех, которые содержат query
    def resp = pokeapi.get( path: "pokemon", query : [limit:2000] )
    def pokeNames = resp?.data?.results*.name?.findAll {it.toLowerCase().contains(query)}
    
    // формируем ответный json
    rt = [
            items: pokeNames.collect { poke ->
                [
                    value: poke,
                    html: poke.replaceAll(/(?i)$query/) { "<b>${it}</b>" },
                    label: poke, 
                    icon: getAvatar(poke)
                ]
            },
            total: pokeNames.size()
        ]

    return Response.ok(new JsonBuilder(rt).toString()).build();
}