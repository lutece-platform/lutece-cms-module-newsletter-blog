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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Newsletter blog service.
 */
@ApplicationScoped
public class NewsletterBlogService
{

    private static final String MARK_PROD_URL = "prod_url";
    private static final String MARK_LIST_BLOG = "blogs_list";

    @Inject
    private PortletService _portletService;

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
        String templateFileKey = fr.paris.lutece.plugins.newsletter.business.NewsLetterTemplateHome.findByPrimaryKey( nTemplateId, pluginNewsLetter ).getFileKey( );
        String strTemplatePath = NewsletterUtils.getHtmlTemplatePath( nTemplateId, pluginNewsLetter );
        GregorianCalendar calendar = new java.util.GregorianCalendar( );
        Date dateEndPublishing = new Date( calendar.getTimeInMillis( ) );
        Date dateStartPublishing = new Date( datePublishing.getTime( ) + 86400000 );

        if ( strTemplatePath == null && templateFileKey == null )
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
                    listBlogs = BlogService.getInstance( ).findByFilter( documentFilter );
                }
            }
        }

        if ( listBlogs == null || listBlogs.size( ) == 0 )
        {
            return StringUtils.EMPTY;
        }
        String strContent;
        if ( templateFileKey != null && StringUtils.isNumeric( templateFileKey ) )
        {
            strContent = fillTemplateWithDocumentInfos( Integer.parseInt( templateFileKey ), listBlogs, locale, strBaseUrl, user );
        }
        else
        {
            strContent = fillTemplateWithDocumentInfos( strTemplatePath, listBlogs, locale, strBaseUrl, user );
        }
        return strContent;
    }

    /**
     * Fills a given document template with the document data.
     *
     * @param strTemplatePath
     *            The path of the template file
     * @param listBlogs
     *            the object gathering the document data
     * @param locale
     *            the locale used to build the template
     * @param strBaseUrl
     *            The base url of the portal
     * @param user
     *            The current user
     * @return the html code corresponding to the document data
     */
    public String fillTemplateWithDocumentInfos( String strTemplatePath, Collection<Blog> listBlogs, Locale locale, String strBaseUrl, AdminUser user )
    {
        Map<String, Object> model = new HashMap<>( );
        Collection<Blog> listBlogAuthorized = new ArrayList<>( );

        for ( Blog blog : listBlogs )
        {
            Collection<Portlet> porletCollec = PublishingService.getInstance( ).getPortletsByBlogId( Integer.toString( blog.getId( ) ) );
            porletCollec = _portletService.getAuthorizedPortletCollection( porletCollec, user );
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
     * Fills a given document template (from an uploaded file) with the document data.
     *
     * @param strTemplateFilekey
     *            The id of the template file
     * @param listBlogs
     *            the object gathering the document data
     * @param locale
     *            the locale used to build the template
     * @param strBaseUrl
     *            The base url of the portal
     * @param user
     *            The current user
     * @return the html code corresponding to the document data
     */
    public String fillTemplateWithDocumentInfos( Integer strTemplateFilekey, Collection<Blog> listBlogs, Locale locale, String strBaseUrl, AdminUser user )
    {
        Map<String, Object> model = new HashMap<>( );
        Collection<Blog> listBlogAuthorized = new ArrayList<>( );

        for ( Blog blog : listBlogs )
        {
            Collection<Portlet> porletCollec = PublishingService.getInstance( ).getPortletsByBlogId( Integer.toString( blog.getId( ) ) );
            porletCollec = _portletService.getAuthorizedPortletCollection( porletCollec, user );
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
            fr.paris.lutece.portal.business.file.File file = fr.paris.lutece.plugins.newsletter.service.NewsletterFileService.getFileByKey( strTemplateFilekey.toString( ) );
            byte [ ] bTemplate = file.getPhysicalFile( ).getValue( );
            String strTemplate = new String( bTemplate );
            HtmlTemplate template = AppTemplateService.getTemplateFromStringFtl( strTemplate, locale, model );
            return template.getHtml( );
        }
        return StringUtils.EMPTY;
    }

    /**
     * Load the portlet of type BLOG_LIST.
     *
     * @return the reference list of portlets of type BLOG_LIST
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
