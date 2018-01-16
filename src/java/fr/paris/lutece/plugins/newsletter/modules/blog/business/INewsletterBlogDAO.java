package fr.paris.lutece.plugins.newsletter.modules.blog.business;

import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * Interface for INewsletterblogDAO
 */
public interface INewsletterBlogDAO
{

    /**
     * Get a newsletter blogs topic from its id
     * 
     * @param nIdTopic
     *            the id of the topic to get
     * @param plugin
     *            The plugin
     * @return The topic, or null if no topic was found
     */
    NewsletterBlog findByPrimaryKey( int nIdTopic, Plugin plugin );

    /**
     * Update a newsletter blogs topic
     * 
     * @param topic
     *            The topic to update
     * @param plugin
     *            The plugin
     */
    void updateDocumentTopic( NewsletterBlog topic, Plugin plugin );

    /**
     * Remove a newsletter blogs topic from the database
     * 
     * @param nIdTopic
     *            The id of the newsletter blogs topic to remove
     * @param plugin
     *            The plugin
     */
    void deleteDocumentTopic( int nIdTopic, Plugin plugin );

    /**
     * Insert a new newsletter blogs topic into the database
     * 
     * @param topic
     *            The newsletter blogs topic to insert
     * @param plugin
     *            the plugin
     */
    void createDocumentTopic( NewsletterBlog topic, Plugin plugin );

    /**
     * Associate a new category to a newsletter topic
     * 
     * @param nTopicId
     *            the topic id
     * @param nDocumentCategoryId
     *            the blogs tag identifier
     * @param plugin
     *            the newsletter blogs plugin
     */
    void associateNewsLetterDocumentTag( int nTopicId, int nTagId, Plugin plugin );

    /**
     * Remove the relationship between a topic and the list of blogs
     * 
     * @param nTopicId
     *            the topic id
     * @param plugin
     *            the newsletter blogs plugin
     */
    void deleteNewsLetterDocumentTags( int nTopicId, Plugin plugin );

    /**
     * loads the list of tags linked to the newsletter
     * 
     * @param nTopicId
     *            the topic id
     * @param plugin
     *            the newsletter blogs plugin
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
     *            the newsletter blogs plugin
     */
    void associateNewsLetterDocumentPortlet( int nTopicId, int nPortletId, Plugin plugin );

    /**
     * Remove the relationship between a topic and the list of portlets
     * 
     * @param nTopicId
     *            the topic id
     * @param plugin
     *            the newsletter blogs plugin
     */
    void deleteNewsLetterDocumentPortlet( int nTopicId, Plugin plugin );

    /**
     * loads the list of blogs list portlets linked to the newsletter
     * 
     * @param nTopicId
     *            the topic id
     * @param plugin
     *            the newsletter blogs plugin
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
