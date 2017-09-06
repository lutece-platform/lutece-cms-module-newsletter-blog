package fr.paris.lutece.plugins.newsletter.modules.htmldocs.business;

import fr.paris.lutece.plugins.htmldocs.business.HtmlDoc;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * DAO implementation for newsletter document
 */
public class NewsletterHtmlDocDAO implements INewsletterHtmlDocDAO
{
    private static final String SQL_QUERY_SELECT_NEWSLETTER_DOCUMENT_TOPIC = " SELECT id_topic, id_template, use_tags FROM newsletter_htmldocs_topic WHERE id_topic = ? ";
    private static final String SQL_QUERY_INSERT_NEWSLETTER_DOCUMENT_TOPIC = " INSERT INTO newsletter_htmldocs_topic(id_topic, id_template, use_tags) VALUES (?,?,?) ";
    private static final String SQL_QUERY_UPDATE_NEWSLETTER_DOCUMENT_TOPIC = " UPDATE newsletter_htmldocs_topic SET id_template = ?, use_tags = ? WHERE id_topic = ? ";
    private static final String SQL_QUERY_DELETE_NEWSLETTER_DOCUMENT_TOPIC = " DELETE FROM newsletter_htmldocs_topic WHERE id_topic = ? ";

    private static final String SQL_QUERY_SELECT_DOCUMENT_BY_DATE_AND_LIST_DOCUMENT = "SELECT DISTINCT a.id_html_doc, a.version, a.content_label, a.creation_date, a.update_date, a.html_content, a.user_editor, a.user_creator, a.attached_portlet_id, a.edit_comment, a.description, a.shareable FROM htmldocs a INNER JOIN htmldocs_list_portlet_htmldocs b ON a.id_document=b.id_html_doc INNER JOIN core_portlet c ON b.id_portlet=c.id_portlet WHERE c.id_portlet_type='HTMLDOC_LIST_PORTLET' ";
    private static final String SQL_QUERY_DOCUMENT_TYPE_PORTLET = " SELECT DISTINCT id_portlet , name FROM core_portlet WHERE id_portlet_type='HTMLDOCSLIST_PORTLET'  ";
    private static final String SQL_QUERY_ASSOCIATE_NEWSLETTER_CATEGORY_LIST = "INSERT INTO newsletter_htmldocs_tag ( id_topic , id_tag ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE_NEWSLETTER_CATEGORY_LIST = "DELETE FROM newsletter_htmldocs_tag WHERE id_topic = ?";
    private static final String SQL_QUERY_SELECT_NEWSLETTER_CATEGORY_IDS = "SELECT id_tag FROM newsletter_htmldocs_tag WHERE id_topic = ?";
    private static final String SQL_QUERY_ASSOCIATE_NEWSLETTER_PORTLET = "INSERT INTO newsletter_htmldocs_portlet ( id_topic , id_portlet ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_SELECT_NEWSLETTER_PORTLET_IDS = " SELECT id_portlet FROM newsletter_htmldocs_portlet WHERE id_topic = ? ";
    private static final String SQL_QUERY_DELETE_NEWSLETTER_PORTLET = "DELETE FROM newsletter_htmldocs_portlet WHERE id_topic = ?";
    private static final String SQL_QUERY_FIND_TEMPLATE = " SELECT count(id_template) FROM newsletter_htmldocs_portlet WHERE id_template = ? ";
    private static final String SQL_FILTER_DATE_MODIF = " a.date_modification >=? ";
    private static final String SQL_FILTER_ID_PORTLET = " c.id_portlet = ? ";

    private static final String CONSTANT_AND = " AND ";
    private static final String CONSTANT_ORDER_BY_DATE_MODIF = " ORDER BY a.date_modification DESC ";

    /**
     * {@inheritDoc}
     */
    @Override
    public NewsletterHtmlDoc findByPrimaryKey( int nIdTopic, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NEWSLETTER_DOCUMENT_TOPIC, plugin );
        daoUtil.setInt( 1, nIdTopic );
        daoUtil.executeQuery( );
        NewsletterHtmlDoc topic = null;
        if ( daoUtil.next( ) )
        {
            topic = new NewsletterHtmlDoc( );
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
    public void updateDocumentTopic( NewsletterHtmlDoc topic, Plugin plugin )
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
    public void createDocumentTopic( NewsletterHtmlDoc topic, Plugin plugin )
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
    public Collection<HtmlDoc> selectDocumentsByDateAndTag( int nPortletId, Timestamp dateLastSending, Plugin plugin )
    {
        StringBuilder sbSql = new StringBuilder( SQL_QUERY_SELECT_DOCUMENT_BY_DATE_AND_LIST_DOCUMENT );

        sbSql.append( CONSTANT_AND );
        sbSql.append( SQL_FILTER_DATE_MODIF );
        if ( nPortletId > 0 )
        {
            sbSql.append( CONSTANT_AND );
            sbSql.append( SQL_FILTER_ID_PORTLET );
        }
        sbSql.append( CONSTANT_ORDER_BY_DATE_MODIF );

        DAOUtil daoUtil = new DAOUtil( sbSql.toString( ), plugin );
        int nInde = 1;
        daoUtil.setTimestamp( nInde++, dateLastSending );
        if ( nPortletId > 0 )
        {
            daoUtil.setInt( nInde, nPortletId );
        }

        daoUtil.executeQuery( );

        List<HtmlDoc> list = new ArrayList<HtmlDoc>( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;
            HtmlDoc htmlDoc = new HtmlDoc( );

            htmlDoc.setId( daoUtil.getInt( nIndex++ ) );
            htmlDoc.setVersion( daoUtil.getInt( nIndex++ ) );
            htmlDoc.setContentLabel( daoUtil.getString( nIndex++ ) );
            htmlDoc.setCreationDate( daoUtil.getTimestamp( nIndex++ ) );
            htmlDoc.setUpdateDate( daoUtil.getTimestamp( nIndex++ ) );
            htmlDoc.setHtmlContent( daoUtil.getString( nIndex++ ) );
            htmlDoc.setUser( daoUtil.getString( nIndex++ ) );
            htmlDoc.setUserCreator( daoUtil.getString( nIndex++ ) );
            htmlDoc.setAttachedPortletId( daoUtil.getInt( nIndex++ ) );
            htmlDoc.setEditComment( daoUtil.getString( nIndex++ ) );
            htmlDoc.setDescription( daoUtil.getString( nIndex++ ) );
            htmlDoc.setShareable( daoUtil.getBoolean( nIndex++ ) );
            list.add( htmlDoc );
        }

        daoUtil.free( );

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList selectDocumentListPortlets( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DOCUMENT_TYPE_PORTLET );
        daoUtil.executeQuery( );

        ReferenceList list = new ReferenceList( );

        while ( daoUtil.next( ) )
        {
            list.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free( );

        return list;
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
