package fr.paris.lutece.plugins.newsletter.modules.htmldocs.service;

import fr.paris.lutece.plugins.htmldocs.business.HtmlDoc;
import fr.paris.lutece.plugins.htmldocs.business.HtmlDocFilter;
import fr.paris.lutece.plugins.htmldocs.business.HtmlDocHome;
import fr.paris.lutece.plugins.htmldocs.business.portlet.HtmlDocsListPortletHome;
import fr.paris.lutece.plugins.htmldocs.service.HtmldocsPlugin;
import fr.paris.lutece.plugins.htmldocs.service.PublishingService;
import fr.paris.lutece.plugins.newsletter.modules.htmldocs.business.NewsletterHtmlDoc;
import fr.paris.lutece.plugins.newsletter.modules.htmldocs.business.NewsletterHtmlDocHome;
import fr.paris.lutece.plugins.newsletter.modules.htmldocs.util.NewsletterHtmlDocUtils;
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
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Newsletter document service. This class implements the singleton design pattern.
 */
public class NewsletterHtmlDocService
{
    private static final String FULLSTOP = ".";

    private static final String MARK_IMG_PATH = "img_path";
    private static final String MARK_DOCUMENT_PORTLETS_COLLEC = "portlets_collec";
    private static final String MARK_DOCUMENT = "document";
    private static final String MARK_FILE_ID = "id_file";

    private static final String DOCUMENT_RESOURCE_SERVLET_URL = "servlet/plugins/htmldocs/file";

    private static NewsletterHtmlDocService _singleton = new NewsletterHtmlDocService( );
    private NewsletterService _newsletterService = NewsletterService.getService( );

    /**
     * Returns the instance of the singleton
     * 
     * @return The instance of the singleton
     */
    public static NewsletterHtmlDocService getInstance( )
    {
        return _singleton;
    }

    /**
     * Copy specified document's type file into a given folder
     * 
     * @param document
     *            the htmldoc
     * @param strFileType
     *            the file type
     * @param strDestFolderPath
     *            the destination folder
     * @return name of the copy file or null if there is no copied file
     */
    public String copyFileFromDocument( HtmlDoc document, String strFileType, String strDestFolderPath )
    {
        String strFileName = null;

        byte [ ] tabByte = document.getDocContent( ).getBinaryValue( );
        strFileName = NewsletterHtmlDocUtils.formatInteger( document.getId( ), 5 )
                + NewsletterHtmlDocUtils.formatInteger( document.getDocContent( ).getId( ), 5 ) + NewsletterHtmlDocUtils.formatInteger( 1, 5 ) + FULLSTOP
                + StringUtils.substringAfterLast( document.getDocContent( ).getTextValue( ), FULLSTOP );

        FileOutputStream fos = null;

        try
        {
            File file = new File( strDestFolderPath );
            if ( !file.exists( ) )
            {
                if ( !file.mkdir( ) )
                {
                    throw new IOException( );
                }
            }

            file = new File( strDestFolderPath + strFileName );
            fos = new FileOutputStream( file );
            IOUtils.write( tabByte, fos );
        }
        catch( IOException e )
        {
            AppLogService.error( e );
        }
        catch( Exception e )
        {
            AppLogService.error( e );
        }
        finally
        {
            IOUtils.closeQuietly( fos );
        }

        return strFileName;
    }

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
    public String generateDocumentsList( NewsletterHtmlDoc newsletterDocument, int nTemplateId, Timestamp datePublishing, String strBaseUrl, AdminUser user,
            Locale locale )
    {
        Plugin pluginNewsLetter = PluginService.getPlugin( NewsletterPlugin.PLUGIN_NAME );
        HtmlDocFilter documentFilter = new HtmlDocFilter( );
        String strTemplatePath = NewsletterUtils.getHtmlTemplatePath( nTemplateId, pluginNewsLetter );

        if ( strTemplatePath == null )
        {
            return null;
        }
        Collection<HtmlDoc> listDocuments = null;
        if ( newsletterDocument.getUseDocumentTags( ) )
        {
            int [ ] arrayTagIds = NewsletterHtmlDocHome.findNewsletterTagIds( newsletterDocument.getId( ), pluginNewsLetter );
            if ( arrayTagIds != null && arrayTagIds.length > 0 )
            {
                documentFilter.setTagsId( arrayTagIds );
            }
            listDocuments = PublishingService.getInstance( ).getPublishedDocumentsSinceDate( datePublishing, datePublishing, documentFilter, locale );
        }
        else
        {
            int [ ] arrayPortletsIds = NewsletterHtmlDocHome.findNewsletterPortletsIds( newsletterDocument.getId( ), pluginNewsLetter );
            if ( arrayPortletsIds != null && arrayPortletsIds.length > 0 )
            {
                Plugin documentPlugin = PluginService.getPlugin( HtmldocsPlugin.PLUGIN_NAME );
                List<Integer> listDocumentIds = NewsletterHtmlDocHome.getPublishedDocumentsIdsListByPortletIds( arrayPortletsIds, datePublishing,
                        datePublishing, documentPlugin );
                if ( listDocumentIds != null && listDocumentIds.size( ) > 0 )
                {
                    int [ ] arrayDocumentsId = new int [ listDocumentIds.size( )];
                    int nIndex = 0;
                    for ( int nDocumentId : listDocumentIds )
                    {
                        arrayDocumentsId [nIndex] = nDocumentId;
                        nIndex++;
                    }
                    documentFilter.setIds( arrayDocumentsId );
                    documentFilter.setLoadBinaries( true );
                    listDocuments = HtmlDocHome.findByFilter( documentFilter, locale );
                }
            }
        }

        if ( listDocuments == null )
        {
            return StringUtils.EMPTY;
        }

        StringBuffer sbDocumentLists = new StringBuffer( );

        // get html from templates
        for ( HtmlDoc document : listDocuments )
        {
            String strContent = fillTemplateWithDocumentInfos( strTemplatePath, document, locale, strBaseUrl, user );
            sbDocumentLists.append( strContent );
        }

        return sbDocumentLists.toString( );
    }

    /**
     * Fills a given document template with the document data
     * 
     * @return the html code corresponding to the document data
     * @param strBaseUrl
     *            The base url of the portal
     * @param strTemplatePath
     *            The path of the template file
     * @param document
     *            the object gathering the document data
     * @param locale
     *            the locale used to build the template
     * @param user
     *            The current user
     */
    public String fillTemplateWithDocumentInfos( String strTemplatePath, HtmlDoc document, Locale locale, String strBaseUrl, AdminUser user )
    {
        Collection<Portlet> porletCollec = PublishingService.getInstance( ).getPortletsByDocumentId( Integer.toString( document.getId( ) ) );
        porletCollec = PortletService.getInstance( ).getAuthorizedPortletCollection( porletCollec, user );
        // the document insert in the buffer must be publish in a authorized portlet
        if ( porletCollec.size( ) > 0 )
        {
            // the document insert in the buffer must be publish in a authorized portlet
            Map<String, Object> model = new HashMap<String, Object>( );
            model.put( MARK_DOCUMENT, document );

            // if noSecuredImg is true, it will copy all document's picture in a no secured folder
            if ( _newsletterService.useUnsecuredImages( ) )
            {
                String strImgFolder = _newsletterService.getUnsecuredImagefolder( );
                String pictureName = NewsletterHtmlDocService.getInstance( ).copyFileFromDocument( document, NewsletterHtmlDocUtils.CONSTANT_IMG_FILE_TYPE,
                        _newsletterService.getUnsecuredFolderPath( ) + strImgFolder );
                if ( pictureName != null )
                {
                    model.put( MARK_IMG_PATH, _newsletterService.getUnsecuredWebappUrl( ) + strImgFolder + pictureName );
                }
            }
            else
            {
                String strProdUrl = AppPathService.getProdUrl( strBaseUrl );

                UrlItem urlItem = new UrlItem( strProdUrl + DOCUMENT_RESOURCE_SERVLET_URL );
                urlItem.addParameter( MARK_FILE_ID, ( document.getDocContent( ) != null ) ? document.getDocContent( ).getId( ) : 0 );
                model.put( MARK_IMG_PATH, urlItem.getUrl( ) );

            }

            model.put( NewsLetterConstants.MARK_BASE_URL, strBaseUrl );
            model.put( MARK_DOCUMENT_PORTLETS_COLLEC, porletCollec );

            HtmlTemplate template = AppTemplateService.getTemplate( strTemplatePath, locale, model );

            return template.getHtml( );
        }
        return StringUtils.EMPTY;
    }
    /**
     * Load the portlet of type HTMLDOC_LIST
     * @return
     */
    public ReferenceList getPortletHtmlDocList(){
    	
    	ReferenceList list= new ReferenceList();
    	String className = HtmlDocsListPortletHome.class.getName( );
        String strPortletTypeId = PortletTypeHome.getPortletTypeId( className );
        
    	for(Portlet pt:PublishingService.getInstance().getHtmlDocsPortlets()){
    		
    		if(pt.getPortletTypeId().equals(strPortletTypeId)){
    			list.addItem(pt.getId( ), pt.getName( ));
    		}
    		
    	}
    	return list;
    	
    }
}
