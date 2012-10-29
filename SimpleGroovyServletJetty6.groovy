@Grab(group='org.mortbay.jetty', module='jetty-embedded', version='6.1.26')

import org.mortbay.jetty.Server
import org.mortbay.jetty.servlet.*
import groovy.servlet.*
import javax.servlet.http.*
import javax.servlet.ServletConfig
 
class SimpleGroovyServletJetty6 extends HttpServlet {
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
        def servlet = new SimpleGroovyServletJetty6(requestHandler: requestHandler)
        def jetty = new Server(port)
        def context = new Context(jetty, '/', Context.SESSIONS)
        context.addServlet(new ServletHolder(servlet), '/*')
        jetty.start()
    }
}