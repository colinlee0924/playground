package com.hanlinuniverse.playground.JiraMCPServer.mcp.tools;

import io.modelcontextprotocol.sdk.McpSchema;
import io.modelcontextprotocol.sdk.McpSchema.CallToolResult;
import io.modelcontextprotocol.sdk.McpSchema.Tool;
import io.modelcontextprotocol.sdk.McpServerFeatures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provider for Jira-specific MCP tools.
 * This class creates and provides tools that can be used by AI clients to interact with Jira.
 */
public class JiraToolProvider {
    
    /**
     * Get all available tools that this provider can offer.
     * 
     * @return List of tool specifications
     */
    public List<McpServerFeatures.SyncToolSpecification> getAvailableTools() {
        List<McpServerFeatures.SyncToolSpecification> tools = new ArrayList<>();
        
        // Add issue search tool
        tools.add(createIssueSearchTool());
        
        // Add issue creation tool
        tools.add(createIssueCreationTool());
        
        // Add issue analysis tool
        tools.add(createIssueAnalysisTool());
        
        return tools;
    }
    
    /**
     * Create a tool for searching Jira issues using JQL.
     * 
     * @return Tool specification for issue search
     */
    private McpServerFeatures.SyncToolSpecification createIssueSearchTool() {
        String issueSearchSchema = """
                {
                  "type": "object",
                  "properties": {
                    "jql": {
                      "type": "string",
                      "description": "JQL query to search for issues"
                    },
                    "maxResults": {
                      "type": "integer",
                      "description": "Maximum number of results to return",
                      "default": 10
                    }
                  },
                  "required": ["jql"]
                }
                """;
        
        return new McpServerFeatures.SyncToolSpecification(
            new Tool("issue-search", "Search for Jira issues using JQL", issueSearchSchema),
            (exchange, arguments) -> {
                // Extract arguments
                String jql = (String) arguments.get("jql");
                Integer maxResults = arguments.containsKey("maxResults") ? 
                    (Integer) arguments.get("maxResults") : 10;
                
                // Log the request
                exchange.loggingNotification(
                    McpSchema.LoggingMessageNotification.builder()
                        .level(McpSchema.LoggingLevel.INFO)
                        .logger("issue-search-tool")
                        .data("Searching issues with JQL: " + jql + ", maxResults: " + maxResults)
                        .build());
                
                // TODO: Implement actual Jira API call to search for issues
                // For now, return a mock response
                String mockResponse = """
                        {
                          "issues": [
                            {
                              "key": "DEMO-1",
                              "summary": "Sample issue 1",
                              "status": "Open"
                            },
                            {
                              "key": "DEMO-2",
                              "summary": "Sample issue 2",
                              "status": "In Progress"
                            }
                          ],
                          "total": 2
                        }
                        """;
                
                return new CallToolResult(mockResponse, false);
            }
        );
    }
    
    /**
     * Create a tool for creating new Jira issues.
     * 
     * @return Tool specification for issue creation
     */
    private McpServerFeatures.SyncToolSpecification createIssueCreationTool() {
        String issueCreationSchema = """
                {
                  "type": "object",
                  "properties": {
                    "projectKey": {
                      "type": "string",
                      "description": "Key of the project where the issue will be created"
                    },
                    "issueType": {
                      "type": "string",
                      "description": "Type of the issue (e.g., Bug, Task, Story)"
                    },
                    "summary": {
                      "type": "string",
                      "description": "Summary of the issue"
                    },
                    "description": {
                      "type": "string",
                      "description": "Detailed description of the issue"
                    }
                  },
                  "required": ["projectKey", "issueType", "summary"]
                }
                """;
        
        return new McpServerFeatures.SyncToolSpecification(
            new Tool("issue-create", "Create a new Jira issue", issueCreationSchema),
            (exchange, arguments) -> {
                // Extract arguments
                String projectKey = (String) arguments.get("projectKey");
                String issueType = (String) arguments.get("issueType");
                String summary = (String) arguments.get("summary");
                String description = arguments.containsKey("description") ? 
                    (String) arguments.get("description") : "";
                
                // Log the request
                exchange.loggingNotification(
                    McpSchema.LoggingMessageNotification.builder()
                        .level(McpSchema.LoggingLevel.INFO)
                        .logger("issue-create-tool")
                        .data("Creating issue in project: " + projectKey + 
                              ", type: " + issueType + 
                              ", summary: " + summary)
                        .build());
                
                // TODO: Implement actual Jira API call to create an issue
                // For now, return a mock response
                String mockResponse = """
                        {
                          "key": "DEMO-3",
                          "self": "https://your-domain.atlassian.net/rest/api/3/issue/10000",
                          "success": true
                        }
                        """;
                
                return new CallToolResult(mockResponse, false);
            }
        );
    }
    
    /**
     * Create a tool for analyzing Jira issues using AI.
     * 
     * @return Tool specification for issue analysis
     */
    private McpServerFeatures.SyncToolSpecification createIssueAnalysisTool() {
        String issueAnalysisSchema = """
                {
                  "type": "object",
                  "properties": {
                    "issueKey": {
                      "type": "string",
                      "description": "Key of the issue to analyze"
                    }
                  },
                  "required": ["issueKey"]
                }
                """;
        
        return new McpServerFeatures.SyncToolSpecification(
            new Tool("issue-analyze", "Analyze a Jira issue using AI", issueAnalysisSchema),
            (exchange, arguments) -> {
                // Extract arguments
                String issueKey = (String) arguments.get("issueKey");
                
                // Log the request
                exchange.loggingNotification(
                    McpSchema.LoggingMessageNotification.builder()
                        .level(McpSchema.LoggingLevel.INFO)
                        .logger("issue-analyze-tool")
                        .data("Analyzing issue: " + issueKey)
                        .build());
                
                // TODO: Fetch issue data from Jira
                // For now, use mock data
                String issueDescription = "This is a sample issue description for " + issueKey;
                
                // Check if client supports sampling (AI capabilities)
                if (exchange.getClientCapabilities().sampling() == null) {
                    return new CallToolResult("Client does not support AI capabilities", false);
                }
                
                try {
                    // Create a sampling request to analyze the issue
                    McpSchema.CreateMessageRequest request = McpSchema.CreateMessageRequest.builder()
                        .messages(List.of(new McpSchema.SamplingMessage(McpSchema.Role.USER,
                            new McpSchema.TextContent("Analyze this issue: " + issueDescription))))
                        .systemPrompt("You are a Jira issue analysis assistant. Analyze the issue and provide insights.")
                        .maxTokens(500)
                        .build();
                    
                    // Request sampling from the client
                    McpSchema.CreateMessageResult result = exchange.createMessage(request);
                    
                    // Return the analysis
                    return new CallToolResult(result.content().text(), false);
                } catch (Exception e) {
                    // If sampling fails, return an error message
                    return new CallToolResult("Failed to analyze issue: " + e.getMessage(), true);
                }
            }
        );
    }
}