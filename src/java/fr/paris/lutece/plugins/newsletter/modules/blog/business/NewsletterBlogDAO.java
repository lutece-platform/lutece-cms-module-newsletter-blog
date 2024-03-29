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

/**
 * DAO implementation for newsletter document
 */
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
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NEWSLETTER_DOCUMENT_TOPIC, plugin );
        daoUtil.setInt( 1, nIdTopic );
        daoUtil.executeQuery( );
        NewsletterBlog topic = null;
        if ( daoUtil.next( ) )
        {
            topic = new NewsletterBlog( );
            topic.setId( daoUtil.getInt( 1 ) );
            topic.setIdTemplate( daoUtil.getInt( 2 ) );
            topic.setUseDocumentTags( daoUtil.getBoolean( 3 ) );
        }
        daoUtil.free( );
        return topic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDocumentTopic( NewsletterBlog topic, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_NEWSLETTER_DOCUMENT_TOPIC, plugin );
        daoUtil.setInt( 1, topic.getIdTemplate( ) );
        daoUtil.setBoolean( 2, topic.getUseDocumentTags( ) );
        daoUtil.setInt( 3, topic.getId( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDocumentTopic( int nIdTopic, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_NEWSLETTER_DOCUMENT_TOPIC, plugin );
        daoUtil.setInt( 1, nIdTopic );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createDocumentTopic( NewsletterBlog topic, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_NEWSLETTER_DOCUMENT_TOPIC, plugin );
        daoUtil.setInt( 1, topic.getId( ) );
        daoUtil.setInt( 2, topic.getIdTemplate( ) );
        daoUtil.setBoolean( 3, topic.getUseDocumentTags( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void associateNewsLetterDocumentTag( int nTopicId, int nCategoryId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ASSOCIATE_NEWSLETTER_CATEGORY_LIST, plugin );
        daoUtil.setInt( 1, nTopicId );
        daoUtil.setInt( 2, nCategoryId );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNewsLetterDocumentTags( int nTopicId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_NEWSLETTER_CATEGORY_LIST, plugin );

        daoUtil.setInt( 1, nTopicId );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int [ ] selectNewsletterTagIds( int nTopicId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NEWSLETTER_CATEGORY_IDS, plugin );
        daoUtil.setInt( 1, nTopicId );
        daoUtil.executeQuery( );

        List<Integer> list = new ArrayList<Integer>( );

        while ( daoUtil.next( ) )
        {
            int nResultId = daoUtil.getInt( 1 );
            list.add( Integer.valueOf( nResultId ) );
        }

        int [ ] nIdsArray = new int [ list.size( )];

        for ( int i = 0; i < list.size( ); i++ )
        {
            Integer nId = list.get( i );
            nIdsArray [i] = nId.intValue( );
        }

        daoUtil.free( );

        return nIdsArray;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void associateNewsLetterDocumentPortlet( int nTopicId, int nPortletId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ASSOCIATE_NEWSLETTER_PORTLET, plugin );
        daoUtil.setInt( 1, nTopicId );
        daoUtil.setInt( 2, nPortletId );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNewsLetterDocumentPortlet( int nTopicId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_NEWSLETTER_PORTLET, plugin );

        daoUtil.setInt( 1, nTopicId );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int [ ] selectNewsletterPortletsIds( int nTopicId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NEWSLETTER_PORTLET_IDS, plugin );
        daoUtil.setInt( 1, nTopicId );
        daoUtil.executeQuery( );

        List<Integer> list = new ArrayList<Integer>( );

        while ( daoUtil.next( ) )
        {
            int nResultId = daoUtil.getInt( 1 );
            list.add( Integer.valueOf( nResultId ) );
        }

        int [ ] nIdsArray = new int [ list.size( )];

        for ( int i = 0; i < list.size( ); i++ )
        {
            Integer nId = list.get( i );
            nIdsArray [i] = nId.intValue( );
        }

        daoUtil.free( );

        return nIdsArray;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean findTemplate( int nIdNewsletterTemplate, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_TEMPLATE, plugin );
        daoUtil.setInt( 1, nIdNewsletterTemplate );
        daoUtil.executeQuery( );

        boolean bRes = false;
        if ( daoUtil.next( ) )
        {
            bRes = daoUtil.getInt( 1 ) > 0;
        }

        daoUtil.free( );

        return bRes;
    }
}
