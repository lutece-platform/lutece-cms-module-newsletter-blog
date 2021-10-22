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

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for newsletter document
 */
public final class NewsletterBlogUtils
{
    /**
     * Image file type
     */
    public static final String CONSTANT_IMG_FILE_TYPE = "image";

    private static final String CONSTANT_ZERO = "0";

    /**
     * Private constructor
     */
    private NewsletterBlogUtils( )
    {

    }

    /**
     * Rewrite secured img urls to absolutes urls
     * 
     * @param strContent
     *            The content to analyze
     * @param strBaseUrl
     *            The base url
     * @param strUnsecuredBaseUrl
     *            The unsecured base URL
     * @param strUnsecuredFolderPath
     *            The unsecured folder path
     * @param strUnsecuredFolder
     *            The unsecured folder
     * @return The converted content
     */
    /*
     * public static String rewriteImgUrls( String strContent, String strBaseUrl, String strUnsecuredBaseUrl, String strUnsecuredFolderPath, String
     * strUnsecuredFolder ) { if ( strContent == null ) { return StringUtils.EMPTY; } BlogNewsletterDocument doc = new BlogNewsletterDocument( strContent,
     * strBaseUrl ); doc.convertUrlsToUnsecuredUrls( BlogNewsletterDocument.ELEMENT_IMG, strUnsecuredBaseUrl, strUnsecuredFolderPath, strUnsecuredFolder );
     * doc.convertUrlsToUnsecuredUrls( BlogNewsletterDocument.ELEMENT_A, strUnsecuredBaseUrl, strUnsecuredFolderPath, strUnsecuredFolder );
     * 
     * return doc.getContent( ); }
     */

    /**
     * Get the string representation of an integer with a specified number of digits.
     * 
     * @param nNbToformat
     *            The number to format
     * @param nNbDigits
     *            The number of digits to add
     * @return The string representing the integer
     */
    public static String formatInteger( int nNbToformat, int nNbDigits )
    {
        String strNumber = Integer.toString( nNbToformat );
        if ( strNumber.length( ) > nNbDigits )
        {
            return strNumber;
        }
        StringBuffer sbNumber = new StringBuffer( nNbDigits );
        int i = strNumber.length( );
        while ( i < nNbDigits )
        {
            sbNumber.append( CONSTANT_ZERO );
            i++;
        }
        sbNumber.append( strNumber );
        return sbNumber.toString( );
    }
}
