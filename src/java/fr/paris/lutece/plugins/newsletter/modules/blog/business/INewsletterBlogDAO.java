/*
 * Copyright (c) 2002-2021, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
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
