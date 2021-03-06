package fr.paris.lutece.plugins.newsletter.modules.blog.util;

import org.apache.commons.lang.StringUtils;

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
   /* public static String rewriteImgUrls( String strContent, String strBaseUrl, String strUnsecuredBaseUrl, String strUnsecuredFolderPath,
            String strUnsecuredFolder )
    {
        if ( strContent == null )
        {
            return StringUtils.EMPTY;
        }
        BlogNewsletterDocument doc = new BlogNewsletterDocument( strContent, strBaseUrl );
        doc.convertUrlsToUnsecuredUrls( BlogNewsletterDocument.ELEMENT_IMG, strUnsecuredBaseUrl, strUnsecuredFolderPath, strUnsecuredFolder );
        doc.convertUrlsToUnsecuredUrls( BlogNewsletterDocument.ELEMENT_A, strUnsecuredBaseUrl, strUnsecuredFolderPath, strUnsecuredFolder );

        return doc.getContent( );
    }*/

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
