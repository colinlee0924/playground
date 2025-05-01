package com.hanlinuniverse.playground.JiraMCPServer.mcp.prompts;

import io.modelcontextprotocol.sdk.McpSchema;
import io.modelcontextprotocol.sdk.McpSchema.GetPromptResult;
import io.modelcontextprotocol.sdk.McpSchema.Prompt;
import io.modelcontextprotocol.sdk.McpSchema.PromptArgument;
import io.modelcontextprotocol.sdk.McpSchema.SamplingMessage;
import io.modelcontextprotocol.sdk.McpSchema.Role;
import io.modelcontextprotocol.sdk.McpSchema.TextContent;
import io.modelcontextprotocol.sdk.McpServerFeatures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provider for Jira-specific MCP prompts.
 * This class creates and provides prompt templates that can be used by AI clients to interact with Jira.
 */
public class JiraPromptProvider {
    
    /**
     * Get all available prompts that this provider can offer.
     * 
     * @return List of prompt specifications
     */
    public List<McpServerFeatures.SyncPromptSpecification> getAvailablePrompts() {
        List<McpServerFeatures.SyncPromptSpecification> prompts = new ArrayList<>();
        
        // Add issue summary prompt
        prompts.add(createIssueSummaryPrompt());
        
        // Add issue creation prompt
        prompts.add(createIssueCreationPrompt());
        
        // Add code review prompt
        prompts.add(createCodeReviewPrompt());
        
        return prompts;
    }
    
    /**
     * Create a prompt for summarizing Jira issues.
     * 
     * @return Prompt specification for issue summarization
     */
    private McpServerFeatures.SyncPromptSpecification createIssueSummaryPrompt() {
        return new McpServerFeatures.SyncPromptSpecification(
            new Prompt("issue-summary", "Generate a concise summary of a Jira issue", List.of(
                new PromptArgument("issueKey", "Key of the issue to summarize", true),
                new PromptArgument("includeComments", "Whether to include comments in the summary", false)
            )),
            (exchange, request) -> {
                // Extract arguments
                Map<String, Object> args = request.arguments();
                String issueKey = (String) args.get("issueKey");
                boolean includeComments = args.containsKey("includeComments") ? 
                    (boolean) args.get("includeComments") : false;
                
                // Log the request
                exchange.loggingNotification(
                    McpSchema.LoggingMessageNotification.builder()
                        .level(McpSchema.LoggingLevel.INFO)
                        .logger("issue-summary-prompt")
                        .data("Generating summary for issue: " + issueKey + 
                              ", includeComments: " + includeComments)
                        .build());
                
                // TODO: Fetch issue data from Jira
                // For now, use mock data
                String issueDescription = "This is a sample issue description for " + issueKey;
                
                // Create the prompt messages
                List<SamplingMessage> messages = new ArrayList<>();
                
                // System message
                messages.add(new SamplingMessage(Role.SYSTEM, new TextContent(
                    "You are a Jira assistant that summarizes issues concisely. " +
                    "Focus on the key points and provide a clear, structured summary."
                )));
                
                // User message with the issue details
                StringBuilder userMessage = new StringBuilder();
                userMessage.append("Please summarize the following Jira issue:\n\n");
                userMessage.append("Issue Key: ").append(issueKey).append("\n");
                userMessage.append("Description: ").append(issueDescription).append("\n");
                
                if (includeComments) {
                    userMessage.append("\nComments:\n");
                    userMessage.append("- Comment 1: This is a sample comment\n");
                    userMessage.append("- Comment 2: Another sample comment\n");
                }
                
                messages.add(new SamplingMessage(Role.USER, new TextContent(userMessage.toString())));
                
                // Return the prompt
                return new GetPromptResult(
                    "This prompt generates a concise summary of a Jira issue.",
                    messages
                );
            }
        );
    }
    
    /**
     * Create a prompt for creating Jira issues.
     * 
     * @return Prompt specification for issue creation
     */
    private McpServerFeatures.SyncPromptSpecification createIssueCreationPrompt() {
        return new McpServerFeatures.SyncPromptSpecification(
            new Prompt("issue-creation", "Generate a well-structured Jira issue from a description", List.of(
                new PromptArgument("description", "Description of the issue to create", true),
                new PromptArgument("projectKey", "Key of the project where the issue will be created", true),
                new PromptArgument("issueType", "Type of the issue (e.g., Bug, Task, Story)", true)
            )),
            (exchange, request) -> {
                // Extract arguments
                Map<String, Object> args = request.arguments();
                String description = (String) args.get("description");
                String projectKey = (String) args.get("projectKey");
                String issueType = (String) args.get("issueType");
                
                // Log the request
                exchange.loggingNotification(
                    McpSchema.LoggingMessageNotification.builder()
                        .level(McpSchema.LoggingLevel.INFO)
                        .logger("issue-creation-prompt")
                        .data("Generating issue creation prompt for project: " + projectKey + 
                              ", type: " + issueType)
                        .build());
                
                // Create the prompt messages
                List<SamplingMessage> messages = new ArrayList<>();
                
                // System message
                messages.add(new SamplingMessage(Role.SYSTEM, new TextContent(
                    "You are a Jira assistant that helps create well-structured issues. " +
                    "Based on the description provided, generate a proper issue with a clear summary, " +
                    "detailed description, and appropriate fields. " +
                    "Format your response as JSON with the following fields: " +
                    "summary, description, priority, components (if applicable), labels (if applicable)."
                )));
                
                // User message with the issue details
                StringBuilder userMessage = new StringBuilder();
                userMessage.append("Please create a ").append(issueType)
                          .append(" for project ").append(projectKey)
                          .append(" based on the following description:\n\n")
                          .append(description);
                
                messages.add(new SamplingMessage(Role.USER, new TextContent(userMessage.toString())));
                
                // Return the prompt
                return new GetPromptResult(
                    "This prompt generates a well-structured Jira issue from a description.",
                    messages
                );
            }
        );
    }
    
    /**
     * Create a prompt for code review in Jira.
     * 
     * @return Prompt specification for code review
     */
    private McpServerFeatures.SyncPromptSpecification createCodeReviewPrompt() {
        return new McpServerFeatures.SyncPromptSpecification(
            new Prompt("code-review", "Generate a code review for a pull request", List.of(
                new PromptArgument("code", "The code to review", true),
                new PromptArgument("language", "The programming language of the code", true),
                new PromptArgument("context", "Additional context about the code", false)
            )),
            (exchange, request) -> {
                // Extract arguments
                Map<String, Object> args = request.arguments();
                String code = (String) args.get("code");
                String language = (String) args.get("language");
                String context = args.containsKey("context") ? 
                    (String) args.get("context") : "";
                
                // Log the request
                exchange.loggingNotification(
                    McpSchema.LoggingMessageNotification.builder()
                        .level(McpSchema.LoggingLevel.INFO)
                        .logger("code-review-prompt")
                        .data("Generating code review prompt for language: " + language)
                        .build());
                
                // Create the prompt messages
                List<SamplingMessage> messages = new ArrayList<>();
                
                // System message
                messages.add(new SamplingMessage(Role.SYSTEM, new TextContent(
                    "You are a code review assistant that provides thorough, constructive feedback. " +
                    "Focus on code quality, best practices, potential bugs, security issues, and performance improvements. " +
                    "Be specific in your feedback and provide examples of how to improve the code when possible. " +
                    "Format your review with clear sections: Summary, Issues (Critical, Major, Minor), and Recommendations."
                )));
                
                // User message with the code to review
                StringBuilder userMessage = new StringBuilder();
                userMessage.append("Please review the following ").append(language).append(" code:\n\n");
                userMessage.append("```").append(language).append("\n");
                userMessage.append(code).append("\n```\n\n");
                
                if (!context.isEmpty()) {
                    userMessage.append("Additional context:\n").append(context);
                }
                
                messages.add(new SamplingMessage(Role.USER, new TextContent(userMessage.toString())));
                
                // Return the prompt
                return new GetPromptResult(
                    "This prompt generates a thorough code review for a pull request.",
                    messages
                );
            }
        );
    }
}