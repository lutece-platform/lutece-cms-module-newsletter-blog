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

import fr.paris.lutece.plugins.blog.business.Tag;
import fr.paris.lutece.plugins.blog.service.PublishingService;
import fr.paris.lutece.plugins.blog.service.TagService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Home for newsletter document
 */
public final class NewsletterBlogHome
{
    private static INewsletterBlogDAO _dao = SpringContextService.getBean( "newsletter-blog.newsletterBlogDAO" );

    /**
     * Private constructor
     */
    private NewsletterBlogHome( )
    {
    }

    /**
     * Get a newsletter blogs topic from its id
     * 
     * @param nIdTopic
     *            the id of the topic to get
     * @param plugin
     *            The plugin
     * @return The topic, or null if no topic was found
     */
    public static NewsletterBlog findByPrimaryKey( int nIdTopic, Plugin plugin )
    {
        return _dao.findByPrimaryKey( nIdTopic, plugin );
    }

    /**
     * Update a newsletter blogs topic
     * 
     * @param topic
     *            The topic to update
     * @param plugin
     *            The plugin
     */
    public static void updateDocumentTopic( NewsletterBlog topic, Plugin plugin )
    {
        _dao.updateDocumentTopic( topic, plugin );
    }

    /**
     * Remove a newsletter blogs topic from the database
     * 
     * @param nIdTopic
     *            The id of the newsletter blogs topic to remove
     * @param plugin
     *            The plugin
     */
    public static void deleteDocumentTopic( int nIdTopic, Plugin plugin )
    {
        _dao.deleteDocumentTopic( nIdTopic, plugin );
    }

    /**
     * Insert a new newsletter blogs topic into the database
     * 
     * @param topic
     *            The newsletter blogs topic to insert
     * @param plugin
     *            the plugin
     */
    public static void createDocumentTopic( NewsletterBlog topic, Plugin plugin )
    {
        _dao.createDocumentTopic( topic, plugin );
    }

    /**
     * Fetches all the tags defined
     * 
     * @param user
     *            the current user
     * @return A list of all tags
     */
    public static ReferenceList getAllTag( AdminUser user )
    {
        ReferenceList list = new ReferenceList( );
        Collection<Tag> listTagDisplay = TagService.getInstance( ).getAllTagDisplay( );

        for ( Tag tg : listTagDisplay )
        {
            list.addItem( tg.getIdTag( ), tg.getName( ) );
        }

        return list;
    }

    /**
     * Associate a blogs category to a newsletter topic
     * 
     * @param nTopicId
     *            the topic identifier
     * @param nDocumentTagId
     *            the id of the blogs tag to associate
     * @param plugin
     *            the Plugin
     */
    public static void associateNewsLetterDocumentCategory( int nTopicId, int nDocumentTagId, Plugin plugin )
    {
        _dao.associateNewsLetterDocumentTag( nTopicId, nDocumentTagId, plugin );
    }

    /**
     * Removes the relationship between a list of blogs categories and a newsletter topic
     * 
     * @param nTopicId
     *            the newsletter identifier
     * @param plugin
     *            the Plugin
     */
    public static void removeNewsLetterDocumentTags( int nTopicId, Plugin plugin )
    {
        _dao.deleteNewsLetterDocumentTags( nTopicId, plugin );
    }

    /**
     * loads the list of categories of the newsletter
     * 
     * @param nTopicId
     *            the topic identifier
     * @param plugin
     *            the plugin
     * @return the array of categories id
     */
    public static int [ ] findNewsletterTagIds( int nTopicId, Plugin plugin )
    {
        return _dao.selectNewsletterTagIds( nTopicId, plugin );
    }

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
    public static void associateNewsLetterDocumentPortlet( int nTopicId, int nPortletId, Plugin plugin )
    {
        _dao.associateNewsLetterDocumentPortlet( nTopicId, nPortletId, plugin );
    }

    /**
     * Remove the relationship between a topic and the list of portlets
     * 
     * @param nTopicId
     *            the topic id
     * @param plugin
     *            the newsletter blogs plugin
     */
    public static void removeNewsLetterDocumentPortlet( int nTopicId, Plugin plugin )
    {
        _dao.deleteNewsLetterDocumentPortlet( nTopicId, plugin );
    }

    /**
     * loads the list of blogs list portlets linked to the newsletter
     * 
     * @param nTopicId
     *            the topic identifier
     * @param plugin
     *            the plugin
     * @return the array of portlets id
     */
    public static int [ ] findNewsletterPortletsIds( int nTopicId, Plugin plugin )
    {
        return _dao.selectNewsletterPortletsIds( nTopicId, plugin );
    }

    /**
     * Get the list of id of published blogs associated with a given collection of portlets.
     * 
     * @param nPortletsIds
     *            The list of portlet ids.
     * @param datePublishing
     *            TODO
     * @param plugin
     *            The document plugin
     * @return The list of documents id.
     */
    public static List<Integer> getPublishedDocumentsIdsListByPortletIds( int [ ] nPortletsIds, Date datePublishing, Date dateEndPublishing, Plugin plugin )
    {
        return PublishingService.getInstance( ).getPublishedBlogsIdsListByPortletIds( nPortletsIds, datePublishing, dateEndPublishing, plugin );
    }

    /**
     * Check if a template is used by a topic
     * 
     * @param nIdNewsletterTemplate
     *            The id of the template
     * @param plugin
     *            The newsletter plugin
     * @return True if the template is used by a topic, false otherwise
     */
    public static boolean findTemplate( int nIdNewsletterTemplate, Plugin plugin )
    {
        return _dao.findTemplate( nIdNewsletterTemplate, plugin );
    }
}
