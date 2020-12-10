import com.onresolve.scriptrunner.runner.rest.common.CustomEndpointDelegate
import groovy.json.JsonBuilder
import groovy.transform.BaseScript
import groovyx.net.http.RESTClient
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.Response
import groovy.sql.Sql
import java.sql.Driver
import java.sql.Connection

@BaseScript CustomEndpointDelegate delegate
getSecretData(httpMethod: "GET", groups: ["jira-administrators"]) { MultivaluedMap queryParams ->
    def rt = [:]
    def query = queryParams.getFirst("query") as String
    if (!query) {
        return Response.ok(new JsonBuilder(rt).toString()).build();
    } 
   
    def driver = Class.forName("org.postgresql.Driver").newInstance() as Driver

    def props = new Properties()
    props.setProperty("user", "developer")
    props.setProperty("password", "password")

    // создаем соединение с бд, в нем выполняем запрос и получаем данные
    driver.connect("jdbc:postgresql://localhost:5432/my_db", props).withCloseable { conn ->
        def sql = new Sql(conn) 
        def rows = sql.rows("select id, value from secret_data where enabled = true and value ilike ?", ["%${query}%".toString()])
        rt = [
            items: rows.collect { row ->
                [
                    value: row.value,
                    html: row.value.replaceAll(/(?i)$query/) { "<b>${it}</b>" },
                    label: row.value
                ]
            },
            total: rows.size()
        ]
    }
    return Response.ok(new JsonBuilder(rt).toString()).build();
}