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
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

/**
 * DAO implementation for newsletter document
 */
@ApplicationScoped
@Named( "newsletter-blog.newsletterBlogDAO" )
public class NewsletterBlogDAO implements INewsletterBlogDAO
{
    private static final String SQL_QUERY_SELECT_NEWSLETTER_DOCUMENT_TOPIC = " SELECT id_topic, id_template, use_tags FROM newsletter_blogs_topic WHERE id_topic = ? ";
    private static final String SQL_QUERY_INSERT_NEWSLETTER_DOCUMENT_TOPIC = " INSERT INTO newsletter_blogs_topic(id_topic, id_template, use_tags) VALUES (?,?,?) ";
    private static final String SQL_QUERY_UPDATE_NEWSLETTER_DOCUMENT_TOPIC = " UPDATE newsletter_blogs_topic SET id_template = ?, use_tags = ? WHERE id_topic = ? ";
    private static final String SQL_QUERY_DELETE_NEWSLETTER_DOCUMENT_TOPIC = " DELETE FROM newsletter_blogs_topic WHERE id_topic = ? ";

    private static final String SQL_QUERY_ASSOCIATE_NEWSLETTER_CATEGORY_LIST = "INSERT INTO newsletter_blogs_tag ( id_topic , id_tag ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE_NEWSLETTER_CATEGORY_LIST = "DELETE FROM newsletter_blogs_tag WHERE id_topic = ?";
    private static final String SQL_QUERY_SELECT_NEWSLETTER_CATEGORY_IDS = "SELECT id_tag FROM newsletter_blogs_tag WHERE id_topic = ?";
    private static final String SQL_QUERY_ASSOCIATE_NEWSLETTER_PORTLET = "INSERT INTO newsletter_blogs_portlet ( id_topic , id_portlet ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_SELECT_NEWSLETTER_PORTLET_IDS = " SELECT id_portlet FROM newsletter_blogs_portlet WHERE id_topic = ? ";
    private static final String SQL_QUERY_DELETE_NEWSLETTER_PORTLET = "DELETE FROM newsletter_blogs_portlet WHERE id_topic = ?";
    private static final String SQL_QUERY_FIND_TEMPLATE = " SELECT count(id_template) FROM newsletter_blogs_portlet WHERE id_template = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public NewsletterBlog findByPrimaryKey( int nIdTopic, Plugin plugin )
    {
        NewsletterBlog topic = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NEWSLETTER_DOCUMENT_TOPIC, plugin ) )
        {
            daoUtil.setInt( 1, nIdTopic );
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                topic = new NewsletterBlog( );
                topic.setId( daoUtil.getInt( 1 ) );
                topic.setIdTemplate( daoUtil.getInt( 2 ) );
                topic.setUseDocumentTags( daoUtil.getBoolean( 3 ) );
            }
        }
        return topic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDocumentTopic( NewsletterBlog topic, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_NEWSLETTER_DOCUMENT_TOPIC, plugin ) )
        {
            daoUtil.setInt( 1, topic.getIdTemplate( ) );
            daoUtil.setBoolean( 2, topic.getUseDocumentTags( ) );
            daoUtil.setInt( 3, topic.getId( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDocumentTopic( int nIdTopic, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_NEWSLETTER_DOCUMENT_TOPIC, plugin ) )
        {
            daoUtil.setInt( 1, nIdTopic );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createDocumentTopic( NewsletterBlog topic, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_NEWSLETTER_DOCUMENT_TOPIC, plugin ) )
        {
            daoUtil.setInt( 1, topic.getId( ) );
            daoUtil.setInt( 2, topic.getIdTemplate( ) );
            daoUtil.setBoolean( 3, topic.getUseDocumentTags( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void associateNewsLetterDocumentTag( int nTopicId, int nCategoryId, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ASSOCIATE_NEWSLETTER_CATEGORY_LIST, plugin ) )
        {
            daoUtil.setInt( 1, nTopicId );
            daoUtil.setInt( 2, nCategoryId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNewsLetterDocumentTags( int nTopicId, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_NEWSLETTER_CATEGORY_LIST, plugin ) )
        {
            daoUtil.setInt( 1, nTopicId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int [ ] selectNewsletterTagIds( int nTopicId, Plugin plugin )
    {
        List<Integer> list = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NEWSLETTER_CATEGORY_IDS, plugin ) )
        {
            daoUtil.setInt( 1, nTopicId );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                list.add( Integer.valueOf( daoUtil.getInt( 1 ) ) );
            }
        }
        int [ ] nIdsArray = new int [ list.size( )];
        for ( int i = 0; i < list.size( ); i++ )
        {
            nIdsArray [i] = list.get( i ).intValue( );
        }
        return nIdsArray;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void associateNewsLetterDocumentPortlet( int nTopicId, int nPortletId, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ASSOCIATE_NEWSLETTER_PORTLET, plugin ) )
        {
            daoUtil.setInt( 1, nTopicId );
            daoUtil.setInt( 2, nPortletId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNewsLetterDocumentPortlet( int nTopicId, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_NEWSLETTER_PORTLET, plugin ) )
        {
            daoUtil.setInt( 1, nTopicId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int [ ] selectNewsletterPortletsIds( int nTopicId, Plugin plugin )
    {
        List<Integer> list = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NEWSLETTER_PORTLET_IDS, plugin ) )
        {
            daoUtil.setInt( 1, nTopicId );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                list.add( Integer.valueOf( daoUtil.getInt( 1 ) ) );
            }
        }
        int [ ] nIdsArray = new int [ list.size( )];
        for ( int i = 0; i < list.size( ); i++ )
        {
            nIdsArray [i] = list.get( i ).intValue( );
        }
        return nIdsArray;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean findTemplate( int nIdNewsletterTemplate, Plugin plugin )
    {
        boolean bRes = false;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_TEMPLATE, plugin ) )
        {
            daoUtil.setInt( 1, nIdNewsletterTemplate );
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                bRes = daoUtil.getInt( 1 ) > 0;
            }
        }
        return bRes;
    }
}
