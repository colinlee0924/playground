
// package com.hanlinuniverse.playground.JiraMCPServer.mcp.resources;

// import io.modelcontextprotocol.spec.McpSchema;
// import io.modelcontextprotocol.spec.McpSchema.ReadResourceResult;
// import io.modelcontextprotocol.spec.McpSchema.Resource;
// import io.modelcontextprotocol.spec.McpSchema.ResourceContents;
// import io.modelcontextprotocol.server.McpServerFeatures;

// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;

// /**
//  * Provider for Jira-specific MCP resources.
//  * This class creates and provides resources that can be accessed by AI clients to get context about Jira.
//  */
// public class JiraResourceProvider {
    
//     /**
//      * Get all available resources that this provider can offer.
//      * 
//      * @return List of resource specifications
//      */
//     public List<McpServerFeatures.SyncResourceSpecification> getAvailableResources() {
//         List<McpServerFeatures.SyncResourceSpecification> resources = new ArrayList<>();
        
//         // Add project resource
//         resources.add(createProjectResource());
        
//         // Add issue resource
//         resources.add(createIssueResource());
        
//         // Add project documentation resource
//         resources.add(createProjectDocumentationResource());
        
//         return resources;
//     }
    
//     /**
//      * Create a resource for accessing Jira project information.
//      * 
//      * @return Resource specification for project information
//      */
//     private McpServerFeatures.SyncResourceSpecification createProjectResource() {
//         return new McpServerFeatures.SyncResourceSpecification(
//             new Resource("jira://project/{key}", "Project", "Information about a Jira project", "application/json", null),
//             (exchange, request) -> {
//                 // Extract project key from URI
//                 String uri = request.uri();
//                 String projectKey = uri.substring(uri.lastIndexOf('/') + 1);
                
//                 // Log the request
//                 exchange.loggingNotification(
//                     McpSchema.LoggingMessageNotification.builder()
//                         .level(McpSchema.LoggingLevel.INFO)
//                         .logger("project-resource")
//                         .data("Accessing project resource: " + projectKey)
//                         .build());
                
//                 // TODO: Fetch project data from Jira
//                 // For now, return mock data
//                 String mockProjectData = """
//                         {
//                           "key": "%s",
//                           "name": "Sample Project",
//                           "description": "This is a sample project for demonstration",
//                           "lead": {
//                             "name": "admin",
//                             "displayName": "Administrator"
//                           },
//                           "url": "https://your-domain.atlassian.net/projects/%s",
//                           "issueTypes": [
//                             "Bug",
//                             "Task",
//                             "Story"
//                           ]
//                         }
//                         """.formatted(projectKey, projectKey);
                
//                 // In MCP SDK 0.9.0, ReadResourceResult expects a List<ResourceContents> instead of a String
//                 ResourceContents contents = ResourceContents.builder()
//                     .mimeType("application/json")
//                     .data(mockProjectData)
//                     .build();
                
//                 return new ReadResourceResult(Collections.singletonList(contents));
//             }
//         );
//     }
    
//     /**
//      * Create a resource for accessing Jira issue information.
//      * 
//      * @return Resource specification for issue information
//      */
//     private McpServerFeatures.SyncResourceSpecification createIssueResource() {
//         return new McpServerFeatures.SyncResourceSpecification(
//             new Resource("jira://issue/{key}", "Issue", "Information about a Jira issue", "application/json", null),
//             (exchange, request) -> {
//                 // Extract issue key from URI
//                 String uri = request.uri();
//                 String issueKey = uri.substring(uri.lastIndexOf('/') + 1);
                
//                 // Log the request
//                 exchange.loggingNotification(
//                     McpSchema.LoggingMessageNotification.builder()
//                         .level(McpSchema.LoggingLevel.INFO)
//                         .logger("issue-resource")
//                         .data("Accessing issue resource: " + issueKey)
//                         .build());
                
//                 // TODO: Fetch issue data from Jira
//                 // For now, return mock data
//                 String mockIssueData = """
//                         {
//                           "key": "%s",
//                           "summary": "Sample issue for %s",
//                           "description": "This is a sample issue description for demonstration purposes.",
//                           "status": "Open",
//                           "assignee": {
//                             "name": "admin",
//                             "displayName": "Administrator"
//                           },
//                           "reporter": {
//                             "name": "admin",
//                             "displayName": "Administrator"
//                           },
//                           "created": "2023-01-01T12:00:00.000Z",
//                           "updated": "2023-01-02T14:30:00.000Z"
//                         }
//                         """.formatted(issueKey, issueKey);
                
//                 // In MCP SDK 0.9.0, ReadResourceResult expects a List<ResourceContents> instead of a String
//                 ResourceContents contents = ResourceContents.builder()
//                     .mimeType("application/json")
//                     .data(mockIssueData)
//                     .build();
                
//                 return new ReadResourceResult(Collections.singletonList(contents));
//             }
//         );
//     }
    
//     /**
//      * Create a resource for accessing project documentation.
//      * 
//      * @return Resource specification for project documentation
//      */
//     private McpServerFeatures.SyncResourceSpecification createProjectDocumentationResource() {
//         return new McpServerFeatures.SyncResourceSpecification(
//             new Resource("jira://project/{key}/documentation", "Project Documentation", 
//                         "Documentation for a Jira project", "text/markdown", null),
//             (exchange, request) -> {
//                 // Extract project key from URI
//                 String uri = request.uri();
//                 String projectKey = uri.substring(uri.indexOf("/project/") + 9, uri.lastIndexOf("/documentation"));
                
//                 // Log the request
//                 exchange.loggingNotification(
//                     McpSchema.LoggingMessageNotification.builder()
//                         .level(McpSchema.LoggingLevel.INFO)
//                         .logger("project-documentation-resource")
//                         .data("Accessing project documentation for: " + projectKey)
//                         .build());
                
//                 // TODO: Fetch project documentation from Jira
//                 // For now, return mock documentation
//                 String mockDocumentation = """
//                         # Project Documentation for %s
                        
//                         ## Overview
                        
//                         This is a sample project for demonstration purposes.
                        
//                         ## Getting Started
                        
//                         To get started with this project, follow these steps:
                        
//                         1. Clone the repository
//                         2. Install dependencies
//                         3. Run the application
                        
//                         ## Issue Types
                        
//                         - **Bug**: Something isn't working
//                         - **Task**: A task that needs to be done
//                         - **Story**: A user story
                        
//                         ## Workflow
                        
//                         1. **Open**: Issue is open and ready for the assignee to start work
//                         2. **In Progress**: Issue is being actively worked on
//                         3. **Review**: Issue is being reviewed
//                         4. **Done**: Issue has been completed
                        
//                         ## Contact
                        
//                         For questions or support, contact the project administrator.
//                         """.formatted(projectKey);
                
//                 // In MCP SDK 0.9.0, ReadResourceResult expects a List<ResourceContents> instead of a String
//                 ResourceContents contents = ResourceContents.builder()
//                     .mimeType("text/markdown")
//                     .data(mockDocumentation)
//                     .build();
                
//                 return new ReadResourceResult(Collections.singletonList(contents));
//             }
//         );
//     }
// }
