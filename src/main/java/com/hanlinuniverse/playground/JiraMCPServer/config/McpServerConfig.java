package com.hanlinuniverse.playground.JiraMCPServer.config;

import io.modelcontextprotocol.sdk.McpSchema;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration manager for the MCP server.
 * This class provides methods for loading, saving, and accessing MCP server configuration.
 */
public class McpServerConfig {
    
    // Configuration file path
    private static final String CONFIG_FILE = "mcp-server.properties";
    
    // Configuration keys
    private static final String KEY_TOOLS_ENABLED = "mcp.tools.enabled";
    private static final String KEY_RESOURCES_ENABLED = "mcp.resources.enabled";
    private static final String KEY_PROMPTS_ENABLED = "mcp.prompts.enabled";
    private static final String KEY_LOGGING_ENABLED = "mcp.logging.enabled";
    private static final String KEY_LOGGING_LEVEL = "mcp.logging.level";
    
    // Default values
    private static final boolean DEFAULT_TOOLS_ENABLED = true;
    private static final boolean DEFAULT_RESOURCES_ENABLED = true;
    private static final boolean DEFAULT_PROMPTS_ENABLED = true;
    private static final boolean DEFAULT_LOGGING_ENABLED = true;
    private static final String DEFAULT_LOGGING_LEVEL = "INFO";
    
    // Configuration properties
    private Properties properties;
    
    /**
     * Create a new configuration manager.
     */
    public McpServerConfig() {
        properties = new Properties();
        loadConfig();
    }
    
    /**
     * Load configuration from the properties file.
     */
    public void loadConfig() {
        File configFile = new File(CONFIG_FILE);
        
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                properties.load(fis);
            } catch (IOException e) {
                System.err.println("Error loading MCP server configuration: " + e.getMessage());
                // Use default values
                setDefaults();
            }
        } else {
            // Use default values
            setDefaults();
        }
    }
    
    /**
     * Save configuration to the properties file.
     */
    public void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fos, "MCP Server Configuration");
        } catch (IOException e) {
            System.err.println("Error saving MCP server configuration: " + e.getMessage());
        }
    }
    
    /**
     * Set default configuration values.
     */
    private void setDefaults() {
        properties.setProperty(KEY_TOOLS_ENABLED, String.valueOf(DEFAULT_TOOLS_ENABLED));
        properties.setProperty(KEY_RESOURCES_ENABLED, String.valueOf(DEFAULT_RESOURCES_ENABLED));
        properties.setProperty(KEY_PROMPTS_ENABLED, String.valueOf(DEFAULT_PROMPTS_ENABLED));
        properties.setProperty(KEY_LOGGING_ENABLED, String.valueOf(DEFAULT_LOGGING_ENABLED));
        properties.setProperty(KEY_LOGGING_LEVEL, DEFAULT_LOGGING_LEVEL);
    }
    
    /**
     * Check if tools are enabled.
     * 
     * @return true if tools are enabled, false otherwise
     */
    public boolean isToolsEnabled() {
        return Boolean.parseBoolean(properties.getProperty(KEY_TOOLS_ENABLED, String.valueOf(DEFAULT_TOOLS_ENABLED)));
    }
    
    /**
     * Set whether tools are enabled.
     * 
     * @param enabled true to enable tools, false to disable
     */
    public void setToolsEnabled(boolean enabled) {
        properties.setProperty(KEY_TOOLS_ENABLED, String.valueOf(enabled));
    }
    
    /**
     * Check if resources are enabled.
     * 
     * @return true if resources are enabled, false otherwise
     */
    public boolean isResourcesEnabled() {
        return Boolean.parseBoolean(properties.getProperty(KEY_RESOURCES_ENABLED, String.valueOf(DEFAULT_RESOURCES_ENABLED)));
    }
    
    /**
     * Set whether resources are enabled.
     * 
     * @param enabled true to enable resources, false to disable
     */
    public void setResourcesEnabled(boolean enabled) {
        properties.setProperty(KEY_RESOURCES_ENABLED, String.valueOf(enabled));
    }
    
    /**
     * Check if prompts are enabled.
     * 
     * @return true if prompts are enabled, false otherwise
     */
    public boolean isPromptsEnabled() {
        return Boolean.parseBoolean(properties.getProperty(KEY_PROMPTS_ENABLED, String.valueOf(DEFAULT_PROMPTS_ENABLED)));
    }
    
    /**
     * Set whether prompts are enabled.
     * 
     * @param enabled true to enable prompts, false to disable
     */
    public void setPromptsEnabled(boolean enabled) {
        properties.setProperty(KEY_PROMPTS_ENABLED, String.valueOf(enabled));
    }
    
    /**
     * Check if logging is enabled.
     * 
     * @return true if logging is enabled, false otherwise
     */
    public boolean isLoggingEnabled() {
        return Boolean.parseBoolean(properties.getProperty(KEY_LOGGING_ENABLED, String.valueOf(DEFAULT_LOGGING_ENABLED)));
    }
    
    /**
     * Set whether logging is enabled.
     * 
     * @param enabled true to enable logging, false to disable
     */
    public void setLoggingEnabled(boolean enabled) {
        properties.setProperty(KEY_LOGGING_ENABLED, String.valueOf(enabled));
    }
    
    /**
     * Get the logging level.
     * 
     * @return the logging level
     */
    public String getLoggingLevel() {
        return properties.getProperty(KEY_LOGGING_LEVEL, DEFAULT_LOGGING_LEVEL);
    }
    
    /**
     * Get the logging level as an enum.
     * 
     * @return the logging level enum
     */
    public McpSchema.LoggingLevel getLoggingLevelEnum() {
        String level = getLoggingLevel();
        
        try {
            return McpSchema.LoggingLevel.valueOf(level);
        } catch (IllegalArgumentException e) {
            // Default to INFO if the level is invalid
            return McpSchema.LoggingLevel.INFO;
        }
    }
    
    /**
     * Set the logging level.
     * 
     * @param level the logging level
     */
    public void setLoggingLevel(String level) {
        properties.setProperty(KEY_LOGGING_LEVEL, level);
    }
}