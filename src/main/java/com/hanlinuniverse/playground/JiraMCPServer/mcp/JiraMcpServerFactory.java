package com.hanlinuniverse.playground.JiraMCPServer.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanlinuniverse.playground.JiraMCPServer.mcp.tools.JiraToolProvider;
import com.hanlinuniverse.playground.JiraMCPServer.mcp.resources.JiraResourceProvider;
import com.hanlinuniverse.playground.JiraMCPServer.mcp.prompts.JiraPromptProvider;
import io.modelcontextprotocol.sdk.McpServer;
import io.modelcontextprotocol.sdk.McpServerFeatures;
import io.modelcontextprotocol.sdk.McpSyncServer;
import io.modelcontextprotocol.sdk.ServerCapabilities;
import io.modelcontextprotocol.sdk.transport.HttpServletSseServerTransportProvider;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * Factory for creating and configuring MCP servers.
 * This class provides methods for creating and configuring MCP servers with different capabilities.
 */
public class JiraMcpServerFactory {
    
    private static final String SERVER_NAME = "jira-mcp-server";
    private static final String SERVER_VERSION = "1.0.0";
    
    /**
     * Create a new MCP server with default configuration.
     * 
     * @param messagePath The path for MCP messages
     * @param servletContext The servlet context for logging
     * @return A configured MCP server
     */
    public static McpSyncServer createServer(String messagePath, ServletContext servletContext) {
        return createServer(messagePath, true, true, true, true, servletContext);
    }
    
    /**
     * Create a new MCP server with custom capability configuration.
     * 
     * @param messagePath The path for MCP messages
     * @param enableTools Whether to enable tools
     * @param enableResources Whether to enable resources
     * @param enablePrompts Whether to enable prompts
     * @param enableLogging Whether to enable logging
     * @param servletContext The servlet context for logging
     * @return A configured MCP server
     */
    public static McpSyncServer createServer(
            String messagePath, 
            boolean enableTools, 
            boolean enableResources, 
            boolean enablePrompts, 
            boolean enableLogging,
            ServletContext servletContext) {
        
        // Initialize Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Create transport provider
        HttpServletSseServerTransportProvider transportProvider = 
            new HttpServletSseServerTransportProvider(objectMapper, messagePath);
        
        // Build server capabilities
        ServerCapabilities.Builder capabilitiesBuilder = ServerCapabilities.builder();
        
        if (enableTools) {
            capabilitiesBuilder.tools(true);
        }
        
        if (enableResources) {
            capabilitiesBuilder.resources(true);
        }
        
        if (enablePrompts) {
            capabilitiesBuilder.prompts(true);
        }
        
        if (enableLogging) {
            capabilitiesBuilder.logging();
        }
        
        // Create MCP server
        McpSyncServer server = McpServer.sync(transportProvider)
            .serverInfo(SERVER_NAME, SERVER_VERSION)
            .capabilities(capabilitiesBuilder.build())
            .build();
        
        // Log server creation
        if (servletContext != null) {
            servletContext.log("Created MCP server with configuration:");
            servletContext.log("- Server Name: " + SERVER_NAME);
            servletContext.log("- Server Version: " + SERVER_VERSION);
            servletContext.log("- Tools Enabled: " + enableTools);
            servletContext.log("- Resources Enabled: " + enableResources);
            servletContext.log("- Prompts Enabled: " + enablePrompts);
            servletContext.log("- Logging Enabled: " + enableLogging);
        }
        
        return server;
    }
    
    /**
     * Register all available tools with the server.
     * 
     * @param server The MCP server
     * @param toolProvider The tool provider
     * @param servletContext The servlet context for logging
     */
    public static void registerTools(
            McpSyncServer server, 
            JiraToolProvider toolProvider,
            ServletContext servletContext) {
        
        List<McpServerFeatures.SyncToolSpecification> tools = toolProvider.getAvailableTools();
        
        for (McpServerFeatures.SyncToolSpecification tool : tools) {
            server.addTool(tool);
            
            if (servletContext != null) {
                servletContext.log("Registered tool: " + tool.tool().name());
            }
        }
    }
    
    /**
     * Register all available resources with the server.
     * 
     * @param server The MCP server
     * @param resourceProvider The resource provider
     * @param servletContext The servlet context for logging
     */
    public static void registerResources(
            McpSyncServer server, 
            JiraResourceProvider resourceProvider,
            ServletContext servletContext) {
        
        List<McpServerFeatures.SyncResourceSpecification> resources = resourceProvider.getAvailableResources();
        
        for (McpServerFeatures.SyncResourceSpecification resource : resources) {
            server.addResource(resource);
            
            if (servletContext != null) {
                servletContext.log("Registered resource: " + resource.resource().uri());
            }
        }
    }
    
    /**
     * Register all available prompts with the server.
     * 
     * @param server The MCP server
     * @param promptProvider The prompt provider
     * @param servletContext The servlet context for logging
     */
    public static void registerPrompts(
            McpSyncServer server, 
            JiraPromptProvider promptProvider,
            ServletContext servletContext) {
        
        List<McpServerFeatures.SyncPromptSpecification> prompts = promptProvider.getAvailablePrompts();
        
        for (McpServerFeatures.SyncPromptSpecification prompt : prompts) {
            server.addPrompt(prompt);
            
            if (servletContext != null) {
                servletContext.log("Registered prompt: " + prompt.prompt().name());
            }
        }
    }
}