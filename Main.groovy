@Grapes([@Grab("com.h2database:h2:1.3.168"), @GrabConfig(systemClassLoader = true)])
import groovy.sql.Sql

SimpleGroovyServlet.run(8080) { ->
    response.contentType = 'text/plain'
    def lis = ["a", "b"]
    
	def db = Sql.newInstance("jdbc:h2:mem:db1", "org.h2.Driver");
    // sql.execute("drop table log")
    db.execute("create table IF NOT EXISTS log (id IDENTITY PRIMARY KEY, path VARCHAR, time TIMESTAMP DEFAULT now())")
    db.execute("insert into log (path) values(${request.pathInfo})")
    
    switch (request.pathInfo) {
    	case "/test.json":
    		def builder = new groovy.json.JsonBuilder()
    		builder.a {
    			b 1, 2, 3
    			c "hoge"
    			list lis
    		}
    		println builder
    		break
    	case "/log":
    		db.eachRow("select * from log") { println "${it.time}: ${it.path}" }
    		break
    	default:
    		println "path = ${request.pathInfo}"
    		println "params = $params"
    }
}
