package fr.paris.lutece.plugins.newsletter.modules.blog.business;

/**
 * Newsletter htmldocs topic class
 */
public class NewsletterHtmlDoc
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
     * Get the id of the template to apply to htmldocs of this topic
     * 
     * @return The id of the template to apply to htmldocs of this topic
     */
    public int getIdTemplate( )
    {
        return _nIdTemplate;
    }

    /**
     * Set the id of the template to apply to htmldocs of this topic
     * 
     * @param nIdTemplate
     *            The id of the template to apply to htmldocs of this topic
     */
    public void setIdTemplate( int nIdTemplate )
    {
        this._nIdTemplate = nIdTemplate;
    }

    /**
     * Check if this topic use htmldocs tags to get the htmldocs list, or document list portlets
     * 
     * @return True if this topic use htmldocs categories to get the htmldocs list, false if it use portlets.
     */
    public boolean getUseDocumentTags( )
    {
        return _bUseDocumentTags;
    }

    /**
     * Set the flag to indicates if this topic use htmldocs tags to get the htmldocs list, or htmldocs list portlets
     * 
     * @param bUseDocumentTags
     *            True if this topic use htmldocs tags to get the document list, false if it use portlets.
     */
    public void setUseDocumentTags( boolean bUseDocumentTags )
    {
        this._bUseDocumentTags = bUseDocumentTags;
    }
}
