package com.hanlinuniverse.playground.JiraMCPServer.servlet;

import com.hanlinuniverse.playground.JiraMCPServer.config.McpServerConfig;
import io.modelcontextprotocol.spec.McpSchema;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet for configuring the MCP server.
 * This servlet provides a simple UI for enabling/disabling features and configuring the MCP server.
 */
public class ConfigServlet extends HttpServlet {
    
    private McpServerConfig config;
    
    @Override
    public void init() throws ServletException {
        super.init();
        
        // Load configuration
        config = new McpServerConfig();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Set content type
        response.setContentType("text/html;charset=UTF-8");
        
        // Get writer
        PrintWriter out = response.getWriter();
        
        // Write HTML response
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>MCP Server Configuration</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
        out.println("h1 { color: #0052CC; }");
        out.println("h2 { color: #0052CC; margin-top: 20px; }");
        out.println(".section { margin-bottom: 20px; padding: 10px; border: 1px solid #ddd; border-radius: 5px; }");
        out.println("label { display: block; margin-bottom: 5px; }");
        out.println("input, select { margin-bottom: 10px; padding: 5px; width: 300px; }");
        out.println("input[type='checkbox'] { width: auto; }");
        out.println("button { background-color: #0052CC; color: white; padding: 10px 15px; border: none; border-radius: 3px; cursor: pointer; }");
        out.println("button:hover { background-color: #0747A6; }");
        out.println(".status { margin-top: 20px; padding: 10px; border-radius: 5px; }");
        out.println(".status.running { background-color: #E3FCEF; color: #006644; }");
        out.println(".status.stopped { background-color: #FFEBE6; color: #DE350B; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>MCP Server Configuration</h1>");
        
        // Server Status
        out.println("<div class='status running'>");
        out.println("<h3>Server Status: Running</h3>");
        out.println("<p>Server Name: jira-mcp-server</p>");
        out.println("<p>Server Version: 1.0.0</p>");
        out.println("<p>Endpoint: /plugins/servlet/mcp</p>");
        out.println("</div>");
        
        // Configuration Form
        out.println("<form action='mcp-config' method='post'>");
        
        // Capabilities Section
        out.println("<div class='section'>");
        out.println("<h2>Server Capabilities</h2>");
        out.println("<label><input type='checkbox' name='capability_tools' " +
            (config.isToolsEnabled() ? "checked" : "") + "> Enable Tools</label>");
        out.println("<label><input type='checkbox' name='capability_resources' " +
            (config.isResourcesEnabled() ? "checked" : "") + "> Enable Resources</label>");
        out.println("<label><input type='checkbox' name='capability_prompts' " +
            (config.isPromptsEnabled() ? "checked" : "") + "> Enable Prompts</label>");
        out.println("<label><input type='checkbox' name='capability_logging' " +
            (config.isLoggingEnabled() ? "checked" : "") + "> Enable Logging</label>");
        out.println("</div>");
        
        // Tools Section
        out.println("<div class='section'>");
        out.println("<h2>Available Tools</h2>");
        out.println("<label><input type='checkbox' name='tool_issue_search' checked> Issue Search Tool</label>");
        out.println("<label><input type='checkbox' name='tool_issue_create' checked> Issue Creation Tool</label>");
        out.println("<label><input type='checkbox' name='tool_issue_analyze' checked> Issue Analysis Tool</label>");
        out.println("</div>");
        
        // Resources Section
        out.println("<div class='section'>");
        out.println("<h2>Available Resources</h2>");
        out.println("<label><input type='checkbox' name='resource_project' checked> Project Resource</label>");
        out.println("<label><input type='checkbox' name='resource_issue' checked> Issue Resource</label>");
        out.println("<label><input type='checkbox' name='resource_project_documentation' checked> Project Documentation Resource</label>");
        out.println("</div>");
        
        // Prompts Section
        out.println("<div class='section'>");
        out.println("<h2>Available Prompts</h2>");
        out.println("<label><input type='checkbox' name='prompt_issue_summary' checked> Issue Summary Prompt</label>");
        out.println("<label><input type='checkbox' name='prompt_issue_creation' checked> Issue Creation Prompt</label>");
        out.println("<label><input type='checkbox' name='prompt_code_review' checked> Code Review Prompt</label>");
        out.println("</div>");
        
        // Logging Section
        out.println("<div class='section'>");
        out.println("<h2>Logging Configuration</h2>");
        out.println("<label for='logging_level'>Minimum Logging Level:</label>");
        out.println("<select name='logging_level' id='logging_level'>");
        
        // Get current logging level
        String currentLevel = config.getLoggingLevel();
        
        // Add options for all logging levels
        for (McpSchema.LoggingLevel level : McpSchema.LoggingLevel.values()) {
            String selected = level.name().equals(currentLevel) ? "selected" : "";
            out.println("<option value='" + level.name() + "' " + selected + ">" + level.name() + "</option>");
        }
        out.println("</select>");
        out.println("</div>");
        
        // Submit Button
        out.println("<button type='submit'>Save Configuration</button>");
        out.println("</form>");
        
        // Server Control Buttons
        out.println("<div class='section'>");
        out.println("<h2>Server Control</h2>");
        out.println("<form action='mcp-config' method='post'>");
        out.println("<input type='hidden' name='action' value='restart'>");
        out.println("<button type='submit'>Restart Server</button>");
        out.println("</form>");
        out.println("</div>");
        
        // End HTML
        out.println("</body>");
        out.println("</html>");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get action parameter
        String action = request.getParameter("action");
        
        // Handle restart action
        if ("restart".equals(action)) {
            // TODO: Implement server restart logic
            getServletContext().log("MCP Server restart requested");
        } else {
            // Handle configuration update
            boolean enableTools = request.getParameter("capability_tools") != null;
            boolean enableResources = request.getParameter("capability_resources") != null;
            boolean enablePrompts = request.getParameter("capability_prompts") != null;
            boolean enableLogging = request.getParameter("capability_logging") != null;
            
            String loggingLevel = request.getParameter("logging_level");
            
            // Update configuration
            config.setToolsEnabled(enableTools);
            config.setResourcesEnabled(enableResources);
            config.setPromptsEnabled(enablePrompts);
            config.setLoggingEnabled(enableLogging);
            config.setLoggingLevel(loggingLevel);
            
            // Save configuration
            config.saveConfig();
            
            getServletContext().log("MCP Server configuration updated");
            getServletContext().log("- Tools: " + enableTools);
            getServletContext().log("- Resources: " + enableResources);
            getServletContext().log("- Prompts: " + enablePrompts);
            getServletContext().log("- Logging: " + enableLogging);
            getServletContext().log("- Logging Level: " + loggingLevel);
        }
        
        // Redirect back to the configuration page
        response.sendRedirect(request.getContextPath() + "/plugins/servlet/mcp-config");
    }
}