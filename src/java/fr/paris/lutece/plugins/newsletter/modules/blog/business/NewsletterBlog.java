package fr.paris.lutece.plugins.newsletter.modules.blog.business;

/**
 * Newsletter blogs topic class
 */
public class NewsletterBlog
{
    private int _nId;
    private int _nIdTemplate;
    private boolean _bUseDocumentTags;

    /**
     * Get the id of the topic
     * 
     * @return The id of the topic
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Set the id of the topic
     * 
     * @param nId
     *            The id of the topic
     */
    public void setId( int nId )
    {
        this._nId = nId;
    }

    /**
     * Get the id of the template to apply to blogs of this topic
     * 
     * @return The id of the template to apply to blogs of this topic
     */
    public int getIdTemplate( )
    {
        return _nIdTemplate;
    }

    /**
     * Set the id of the template to apply to blogs of this topic
     * 
     * @param nIdTemplate
     *            The id of the template to apply to blogs of this topic
     */
    public void setIdTemplate( int nIdTemplate )
    {
        this._nIdTemplate = nIdTemplate;
    }

    /**
     * Check if this topic use blogs tags to get the blogs list, or document list portlets
     * 
     * @return True if this topic use blogs categories to get the blogs list, false if it use portlets.
     */
    public boolean getUseDocumentTags( )
    {
        return _bUseDocumentTags;
    }

    /**
     * Set the flag to indicates if this topic use blogs tags to get the blogs list, or blogs list portlets
     * 
     * @param bUseDocumentTags
     *            True if this topic use blogs tags to get the document list, false if it use portlets.
     */
    public void setUseDocumentTags( boolean bUseDocumentTags )
    {
        this._bUseDocumentTags = bUseDocumentTags;
    }
}
