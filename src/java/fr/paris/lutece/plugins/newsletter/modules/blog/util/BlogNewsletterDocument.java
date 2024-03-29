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
package fr.paris.lutece.plugins.newsletter.modules.blog.util;

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.service.BlogService;
import fr.paris.lutece.plugins.newsletter.modules.blog.service.NewsletterBlogService;
import fr.paris.lutece.plugins.newsletter.util.HtmlDomDocNewsletter;
import fr.paris.lutece.plugins.newsletter.util.NewsLetterConstants;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Dom document parser for newsletter document.
 */
public class BlogNewsletterDocument extends HtmlDomDocNewsletter
{
    private static final String CONSTANT_IMG = "img";
    private static final String CONSTANT_A = "a";
    private static final String CONSTANT_SUBSTRING_BEGIN = "document?id=";
    private static final String CONSTANT_SUBSTRING_END = "&";

    private static NewsletterBlogService _newsletterDocumentService = NewsletterBlogService.getInstance( );

    /**
     * Instantiates an Blogument after having built the DOM tree.
     * 
     * @param strHtml
     *            The Html code to be parsed.
     * @param strBaseUrl
     *            The Base url used to retrieve urls.
     */
    public BlogNewsletterDocument( String strHtml, String strBaseUrl )
    {
        super( strHtml, strBaseUrl );
    }

    /**
     * Get the urls of all html elements specified by elementType and convert its to unsecured urls and copy this elements into an unsecured folder
     * 
     * @param elementType
     *            the type of element to get
     * @param strUnsecuredBaseUrl
     *            The unsecured base URL
     * @param strUnsecuredFolderPath
     *            The unsecured folder path
     * @param strUnsecuredFolder
     *            The unsecured folder
     */
    /*
     * public void convertUrlsToUnsecuredUrls( ElementUrl elementType, String strUnsecuredBaseUrl, String strUnsecuredFolderPath, String strUnsecuredFolder ) {
     * NodeList nodes = getDomDocument( ).getElementsByTagName( elementType.getTagName( ) );
     * 
     * for ( int i = 0; i < nodes.getLength( ); i++ ) { Node node = nodes.item( i ); NamedNodeMap attributes = node.getAttributes( );
     * 
     * // Test if the element matches the required attribute if ( elementType.getTestedAttributeName( ) != null ) { String strRel = attributes.getNamedItem(
     * elementType.getTestedAttributeName( ) ).getNodeValue( );
     * 
     * if ( !elementType.getTestedAttributeValue( ).equals( strRel ) ) { continue; } }
     * 
     * Node nodeAttribute = attributes.getNamedItem( elementType.getAttributeName( ) );
     * 
     * if ( nodeAttribute != null ) { String strSrc = nodeAttribute.getNodeValue( );
     * 
     * if ( strSrc.contains( CONSTANT_SUBSTRING_BEGIN ) && !strSrc.contains( strUnsecuredBaseUrl + strUnsecuredFolder ) ) { String strDocumentId =
     * StringUtils.substringBetween( strSrc, CONSTANT_SUBSTRING_BEGIN, CONSTANT_SUBSTRING_END ); Blog document = BlogService.getInstance(
     * ).findByPrimaryKeyWithoutBinaries( Integer.valueOf( strDocumentId ) );
     * 
     * String strFileName = StringUtils.EMPTY;
     * 
     * if ( elementType.getTagName( ).equals( CONSTANT_IMG ) ) { strFileName = _newsletterDocumentService.copyFileFromDocument( document,
     * NewsletterBlogUtils.CONSTANT_IMG_FILE_TYPE, strUnsecuredFolderPath + strUnsecuredFolder ); } else { if ( elementType.getTagName( ).equals( CONSTANT_A ) )
     * { strFileName = _newsletterDocumentService.copyFileFromDocument( document, NewsLetterConstants.CONSTANT_PDF_FILE_TYPE, strUnsecuredFolderPath +
     * strUnsecuredFolder ); } }
     * 
     * nodeAttribute.setNodeValue( strUnsecuredBaseUrl + strUnsecuredFolder + strFileName ); } } } }
     */
}
