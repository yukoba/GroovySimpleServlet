@Grapes([
    @Grab(group='org.eclipse.jetty.aggregate', module='jetty-server', version='8.1.7.v20120910'),
    @Grab(group='org.eclipse.jetty.aggregate', module='jetty-servlet', version='8.1.7.v20120910'),
    @Grab(group='javax.servlet', module='javax.servlet-api', version='3.0.1')])

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.*
import groovy.servlet.*
import javax.servlet.http.*
import javax.servlet.ServletConfig
 
class SimpleGroovyServlet extends HttpServlet {
    def requestHandler
    def context
    
    void init(ServletConfig config) {
        super.init(config)
        context = config.servletContext
    }
    
    void service(HttpServletRequest request, HttpServletResponse response) {
        requestHandler.binding = new ServletBinding(request, response, context)
        use (ServletCategory) {
            requestHandler.call()
        }
    }
    
    static void run(int port, Closure requestHandler) {
        SimpleGroovyServlet servlet = new SimpleGroovyServlet(requestHandler: requestHandler)
        Server jetty = new Server(port)
        ServletContextHandler context = new ServletContextHandler(jetty, "/", ServletContextHandler.SESSIONS)
        context.addServlet(new ServletHolder(servlet), "/*")
        jetty.start()
        jetty.join()
    }
}