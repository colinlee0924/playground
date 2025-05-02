package com.hanlinuniverse.playground.JiraMCPServer.servlet;

import com.hanlinuniverse.playground.JiraMCPServer.config.McpServerConfig;
import com.hanlinuniverse.playground.JiraMCPServer.mcp.JiraMcpServerFactory;
import com.hanlinuniverse.playground.JiraMCPServer.mcp.tools.JiraToolProvider;
// import com.hanlinuniverse.playground.JiraMCPServer.mcp.resources.JiraResourceProvider;
import com.hanlinuniverse.playground.JiraMCPServer.mcp.prompts.JiraPromptProvider;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.HttpServletSseServerTransportProvider;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Main servlet that handles MCP protocol communication.
 * This servlet initializes the MCP server and registers tools, resources, and prompts.
 */
public class McpServlet extends HttpServlet {
    private static final String MESSAGE_PATH = "/mcp/message";
    
    private HttpServletSseServerTransportProvider transportProvider;
    private McpSyncServer mcpServer;
    private JiraToolProvider toolProvider;
    // private JiraResourceProvider resourceProvider;
    private JiraPromptProvider promptProvider;
    private McpServerConfig config;
    
    @Override
    public void init() throws ServletException {
        super.init();
        
        // Load configuration
        config = new McpServerConfig();
        
        // Create MCP server using factory
        mcpServer = JiraMcpServerFactory.createServer(
            MESSAGE_PATH,
            config.isToolsEnabled(),
            config.isResourcesEnabled(),
            config.isPromptsEnabled(),
            config.isLoggingEnabled(),
            getServletContext()
        );
        
        // Get transport provider - in MCP SDK 0.9.0, we need to create it separately
        // The transportProvider is already created and passed to the server
        // We'll create it directly here
        transportProvider = new HttpServletSseServerTransportProvider(new com.fasterxml.jackson.databind.ObjectMapper(), MESSAGE_PATH);
        
        // Initialize providers
        toolProvider = new JiraToolProvider();
        // resourceProvider = new JiraResourceProvider();
        promptProvider = new JiraPromptProvider();
        
        // Register features if enabled
        if (config.isToolsEnabled()) {
            JiraMcpServerFactory.registerTools(mcpServer, toolProvider, getServletContext());
        }
        
        /*
        if (config.isResourcesEnabled()) {
            JiraMcpServerFactory.registerResources(mcpServer, resourceProvider, getServletContext());
        }
        */
        
        if (config.isPromptsEnabled()) {
            JiraMcpServerFactory.registerPrompts(mcpServer, promptProvider, getServletContext());
        }
        
        getServletContext().log("MCP Server initialized successfully");
    }
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // In MCP SDK 0.9.0, we need to delegate to the transport provider
        // The HttpServletSseServerTransportProvider extends HttpServlet, so we can use its service method
        transportProvider.service(req, resp);
    }
    
    @Override
    public void destroy() {
        // Close the MCP server
        if (mcpServer != null) {
            mcpServer.close();
            getServletContext().log("MCP Server closed");
        }
        super.destroy();
    }
}