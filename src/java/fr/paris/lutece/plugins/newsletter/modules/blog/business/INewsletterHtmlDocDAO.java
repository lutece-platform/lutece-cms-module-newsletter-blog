package fr.paris.lutece.plugins.newsletter.modules.blog.business;

import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * Interface for INewsletterHtmlDocDAO
 */
public interface INewsletterHtmlDocDAO
{

    /**
     * Get a newsletter htmldocs topic from its id
     * 
     * @param nIdTopic
     *            the id of the topic to get
     * @param plugin
     *            The plugin
     * @return The topic, or null if no topic was found
     */
    NewsletterHtmlDoc findByPrimaryKey( int nIdTopic, Plugin plugin );

    /**
     * Update a newsletter htmldocs topic
     * 
     * @param topic
     *            The topic to update
     * @param plugin
     *            The plugin
     */
    void updateDocumentTopic( NewsletterHtmlDoc topic, Plugin plugin );

    /**
     * Remove a newsletter htmldocs topic from the database
     * 
     * @param nIdTopic
     *            The id of the newsletter htmldocs topic to remove
     * @param plugin
     *            The plugin
     */
    void deleteDocumentTopic( int nIdTopic, Plugin plugin );

    /**
     * Insert a new newsletter htmldocs topic into the database
     * 
     * @param topic
     *            The newsletter htmldocs topic to insert
     * @param plugin
     *            the plugin
     */
    void createDocumentTopic( NewsletterHtmlDoc topic, Plugin plugin );

    /**
     * Associate a new category to a newsletter topic
     * 
     * @param nTopicId
     *            the topic id
     * @param nDocumentCategoryId
     *            the htmldocs tag identifier
     * @param plugin
     *            the newsletter htmldocs plugin
     */
    void associateNewsLetterDocumentTag( int nTopicId, int nTagId, Plugin plugin );

    /**
     * Remove the relationship between a topic and the list of htmldocs
     * 
     * @param nTopicId
     *            the topic id
     * @param plugin
     *            the newsletter htmldocs plugin
     */
    void deleteNewsLetterDocumentTags( int nTopicId, Plugin plugin );

    /**
     * loads the list of tags linked to the newsletter
     * 
     * @param nTopicId
     *            the topic id
     * @param plugin
     *            the newsletter htmldocs plugin
     * @return the array of tag id
     */
    int [ ] selectNewsletterTagIds( int nTopicId, Plugin plugin );

    /**
     * Associate a new portlet to a newsletter topic
     * 
     * @param nTopicId
     *            the topic id
     * @param nPortletId
     *            the portlet identifier
     * @param plugin
     *            the newsletter htmldocs plugin
     */
    void associateNewsLetterDocumentPortlet( int nTopicId, int nPortletId, Plugin plugin );

    /**
     * Remove the relationship between a topic and the list of portlets
     * 
     * @param nTopicId
     *            the topic id
     * @param plugin
     *            the newsletter htmldocs plugin
     */
    void deleteNewsLetterDocumentPortlet( int nTopicId, Plugin plugin );

    /**
     * loads the list of htmldocs list portlets linked to the newsletter
     * 
     * @param nTopicId
     *            the topic id
     * @param plugin
     *            the newsletter htmldocs plugin
     * @return the array of categories id
     */
    int [ ] selectNewsletterPortletsIds( int nTopicId, Plugin plugin );

    /**
     * Check if a template is used by a topic
     * 
     * @param nIdNewsletterTemplate
     *            The id of the template
     * @param plugin
     *            The newsletter plugin
     * @return True if the template is used by a topic, false otherwise
     */
    boolean findTemplate( int nIdNewsletterTemplate, Plugin plugin );
}
