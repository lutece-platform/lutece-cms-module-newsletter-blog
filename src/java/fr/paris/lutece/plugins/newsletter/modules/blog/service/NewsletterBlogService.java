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
package fr.paris.lutece.plugins.newsletter.modules.blog.service;

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.BlogFilter;
import fr.paris.lutece.plugins.blog.business.portlet.BlogListPortletHome;
import fr.paris.lutece.plugins.blog.service.BlogPlugin;
import fr.paris.lutece.plugins.blog.service.BlogService;
import fr.paris.lutece.plugins.blog.service.PublishingService;
import fr.paris.lutece.plugins.newsletter.modules.blog.business.NewsletterBlog;
import fr.paris.lutece.plugins.newsletter.modules.blog.business.NewsletterBlogHome;
import fr.paris.lutece.plugins.newsletter.service.NewsletterPlugin;
import fr.paris.lutece.plugins.newsletter.service.NewsletterService;
import fr.paris.lutece.plugins.newsletter.util.NewsLetterConstants;
import fr.paris.lutece.plugins.newsletter.util.NewsletterUtils;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.portlet.PortletService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Newsletter blog service. This class implements the singleton design pattern.
 */
public class NewsletterBlogService
{

    private static final String MARK_PROD_URL = "prod_url";
    private static final String MARK_LIST_BLOG = "blogs_list";
    private static final String MARK_FILE_ID = "id_file";

    private static final String DOCUMENT_RESOURCE_SERVLET_URL = "servlet/plugins/blogs/file";

    private static NewsletterBlogService _singleton = new NewsletterBlogService( );
    private NewsletterService _newsletterService = NewsletterService.getService( );

    /**
     * Returns the instance of the singleton
     * 
     * @return The instance of the singleton
     */
    public static NewsletterBlogService getInstance( )
    {
        return _singleton;
    }

    /**
     * Copy specified document's type file into a given folder
     * 
     * @param document
     *            the blog
     * @param strFileType
     *            the file type
     * @param strDestFolderPath
     *            the destination folder
     * @return name of the copy file or null if there is no copied file
     */
    /*
     * public String copyFileFromDocument( Blog document, String strFileType, String strDestFolderPath ) { String strFileName = null;
     * 
     * if ( document.getDocContent( ).size( ) == 0 ) {
     * 
     * return strFileName; } // get the first file DocContent docContent = document.getDocContent( ).get( 0 ); byte [ ] tabByte = docContent.getBinaryValue( );
     * strFileName = NewsletterBlogUtils.formatInteger( document.getId( ), 5 ) + NewsletterBlogUtils.formatInteger( docContent.getId( ), 5 ) +
     * NewsletterBlogUtils.formatInteger( 1, 5 ) + FULLSTOP + StringUtils.substringAfterLast( docContent.getTextValue( ), FULLSTOP );
     * 
     * FileOutputStream fos = null;
     * 
     * try { File file = new File( strDestFolderPath ); if ( !file.exists( ) ) { if ( !file.mkdir( ) ) { throw new IOException( ); } }
     * 
     * file = new File( strDestFolderPath + strFileName ); fos = new FileOutputStream( file ); IOUtils.write( tabByte, fos ); } catch( IOException e ) {
     * AppLogService.error( e ); } catch( Exception e ) { AppLogService.error( e ); } finally { IOUtils.closeQuietly( fos ); }
     * 
     * return strFileName; }
     */

    /**
     * Generate the html code for documents corresponding to the documents associated with the topic and to a given publishing date
     * 
     * @param newsletterDocument
     *            the topic to generate
     * @param nTemplateId
     *            the document id to use
     * @param datePublishing
     *            minimum date of publishing of documents. Documents published before this date will not be considered
     * @param strBaseUrl
     *            the url of the portal
     * @param user
     *            The current admin user
     * @param locale
     *            The locale
     * @return the html code for the document list of null if no document template available
     */
    public String generateDocumentsList( NewsletterBlog newsletterDocument, int nTemplateId, Timestamp datePublishing, String strBaseUrl, AdminUser user,
            Locale locale )
    {
        Plugin pluginNewsLetter = PluginService.getPlugin( NewsletterPlugin.PLUGIN_NAME );
        BlogFilter documentFilter = new BlogFilter( );
        String strTemplatePath = NewsletterUtils.getHtmlTemplatePath( nTemplateId, pluginNewsLetter );
        GregorianCalendar calendar = new java.util.GregorianCalendar( );
        Date dateEndPublishing = new Date( calendar.getTimeInMillis( ) );
        Date dateStartPublishing = new Date( datePublishing.getTime( ) + 86400000 );

        if ( strTemplatePath == null )
        {
            return null;
        }
        Collection<Blog> listBlogs = null;
        if ( newsletterDocument.getUseDocumentTags( ) )
        {
            Integer [ ] arrayTagIds = ArrayUtils.toObject( NewsletterBlogHome.findNewsletterTagIds( newsletterDocument.getId( ), pluginNewsLetter ) );
            if ( arrayTagIds != null && arrayTagIds.length > 0 )
            {
                documentFilter.setTagsId( arrayTagIds );
            }
            listBlogs = PublishingService.getInstance( ).getPublishedBlogsSinceDate( dateStartPublishing, dateEndPublishing, documentFilter, locale );
        }
        else
        {
            int [ ] arrayPortletsIds = NewsletterBlogHome.findNewsletterPortletsIds( newsletterDocument.getId( ), pluginNewsLetter );
            if ( arrayPortletsIds != null && arrayPortletsIds.length > 0 )
            {
                Plugin documentPlugin = PluginService.getPlugin( BlogPlugin.PLUGIN_NAME );
                List<Integer> listDocumentIds = PublishingService.getLastPublishedBlogsIdsListByPortletIds( arrayPortletsIds, datePublishing, documentPlugin );
                if ( listDocumentIds != null && listDocumentIds.size( ) > 0 )
                {
                    Integer [ ] arrayDocumentsId = new Integer [ listDocumentIds.size( )];
                    int nIndex = 0;
                    for ( int nDocumentId : listDocumentIds )
                    {
                        arrayDocumentsId [nIndex] = nDocumentId;
                        nIndex++;
                    }
                    documentFilter.setIds( arrayDocumentsId );
                    // documentFilter.setLoadBinaries( true );
                    listBlogs = BlogService.getInstance( ).findByFilter( documentFilter );
                }
            }
        }

        if ( listBlogs == null || listBlogs.size( ) == 0 )
        {
            return StringUtils.EMPTY;
        }

        String strContent = fillTemplateWithDocumentInfos( strTemplatePath, listBlogs, locale, strBaseUrl, user );

        return strContent;
    }

    /**
     * Fills a given document template with the document data
     * 
     * @return the html code corresponding to the document data
     * @param strBaseUrl
     *            The base url of the portal
     * @param strTemplatePath
     *            The path of the template file
     * @param listBlogs
     *            the object gathering the document data
     * @param locale
     *            the locale used to build the template
     * @param user
     *            The current user
     */
    public String fillTemplateWithDocumentInfos( String strTemplatePath, Collection<Blog> listBlogs, Locale locale, String strBaseUrl, AdminUser user )
    {
        Collection<Portlet> porletCollec = null;
        Map<String, Object> model = new HashMap<String, Object>( );
        Collection<Blog> listBlogAuthorized = new ArrayList<Blog>( );

        for ( Blog blog : listBlogs )
        {

            porletCollec = PublishingService.getInstance( ).getPortletsByBlogId( Integer.toString( blog.getId( ) ) );
            porletCollec = PortletService.getInstance( ).getAuthorizedPortletCollection( porletCollec, user );

            // the document insert in the buffer must be publish in a authorized portlet
            if ( porletCollec.size( ) > 0 )
            {

                String strProdUrl = AppPathService.getProdUrl( strBaseUrl );
                model.put( MARK_PROD_URL, strProdUrl );

                Blog blg = BlogService.getInstance( ).loadBlog( blog.getId( ) );
                listBlogAuthorized.add( blg );
            }
        }

        if ( listBlogAuthorized.size( ) != 0 )
        {

            model.put( MARK_LIST_BLOG, listBlogAuthorized );
            model.put( NewsLetterConstants.MARK_BASE_URL, strBaseUrl );

            HtmlTemplate template = AppTemplateService.getTemplate( strTemplatePath, locale, model );

            return template.getHtml( );
        }

        return StringUtils.EMPTY;
    }

    /**
     * Load the portlet of type blog_LIST
     * 
     * @return
     */
    public ReferenceList getPortletBlogList( )
    {

        ReferenceList list = new ReferenceList( );
        String className = BlogListPortletHome.class.getName( );
        String strPortletTypeId = PortletTypeHome.getPortletTypeId( className );

        for ( Portlet pt : PublishingService.getInstance( ).getBlogsPortlets( ) )
        {

            if ( pt.getPortletTypeId( ).equals( strPortletTypeId ) )
            {
                list.addItem( pt.getId( ), pt.getName( ) );
            }

        }
        return list;

    }
}
