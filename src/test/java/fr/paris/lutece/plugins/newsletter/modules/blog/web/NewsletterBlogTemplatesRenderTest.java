/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.plugins.newsletter.modules.blog.web;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.blog.business.Blog;
import fr.paris.lutece.plugins.blog.business.ContentType;
import fr.paris.lutece.plugins.blog.business.DocContent;
import fr.paris.lutece.plugins.blog.business.portlet.BlogPublication;
import fr.paris.lutece.portal.service.template.HtmlMarkup;
import fr.paris.lutece.util.ReferenceList;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Renders with HTML auto-escaping (LUT-33153) the templates of the module: the list of posts inserted into the editor is
 * HTML built by the application (HtmlMarkup) and written as is, the values typed by the editors and the search date are
 * written as text. Only the FreeMarker engine is needed.
 */
public class NewsletterBlogTemplatesRenderTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String TEMPLATE_INSERT = "admin/plugins/newsletter/modules/blog/insert_blogs.html";
    private static final String TEMPLATE_SELECT = "admin/plugins/newsletter/modules/blog/select_blogs.html";
    private static final String TEMPLATE_MODEL = "skin/plugins/newsletter/templates/model_blogs.html";
    /** Stand-ins of the core macros the templates call, escaping their arguments as the core macros do. */
    private static final String MACROS = "newsletter_blog_test_macros.ftl";
    private static final String MACROS_SOURCE = "<#macro cTpl rest...><#nested></#macro><#macro row rest...><#nested></#macro>"
            + "<#macro columns rest...><#nested></#macro><#macro tform rest...><form><#nested></form></#macro>"
            + "<#macro h rest...><#nested></#macro><#macro formGroup rest...><#nested></#macro><#macro inputGroup rest...><#nested></#macro>"
            + "<#macro inputGroupItem rest...><#nested></#macro><#macro icon rest...></#macro><#macro button rest...></#macro>"
            + "<#macro card rest...><#nested></#macro><#macro empty rest...></#macro><#macro listGroup rest...><#nested></#macro>"
            + "<#macro listGroupItem rest...><#nested></#macro><#macro select name rest...><select name=\"${name}\"></select></#macro>"
            + "<#macro input name value='' rest...><input name=\"${name}\" value=\"${value}\"></#macro>"
            + "<#macro checkBox name value labelKey rest...><label><input name=\"${name}\" value=\"${value}\"> ${labelKey}</label></#macro>";

    private static final String POSTS_HTML = "<table><tr><td><h3>Agenda</h3></td></tr></table>";
    private static final String TEXT_WITH_MARKUP = "Horaires <b>\"l'accueil\"</b>";
    private static final String TEXT_ESCAPED = "Horaires &lt;b&gt;&quot;l&#39;accueil&quot;&lt;/b&gt;";
    private static final String DATE_WITH_MARKUP = "\"><b>01/01/2026";
    private static final String DATE_ESCAPED = "&quot;&gt;&lt;b&gt;01/01/2026";

    /**
     * The configuration of AbstractFreeMarkerTemplateService for HTML templates, with the stand-ins of the core macros.
     *
     * @return the configuration
     * @throws IOException
     *             if the templates directory cannot be read
     */
    private static Configuration configuration( ) throws IOException
    {
        StringTemplateLoader macros = new StringTemplateLoader( );
        macros.putTemplate( MACROS, MACROS_SOURCE );
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                new FileTemplateLoader( new File( TEMPLATES_ROOT ) ), macros
        } ) );
        cfg.addAutoInclude( MACROS );

        return cfg;
    }

    /**
     * Renders a template of the module.
     *
     * @param strTemplate
     *            the template path
     * @param model
     *            the model
     * @return the rendering
     * @throws IOException
     *             if the template cannot be read
     * @throws TemplateException
     *             if the rendering fails
     */
    private static String render( String strTemplate, Map<String, ? extends Object> model ) throws IOException, TemplateException
    {
        StringWriter writer = new StringWriter( );
        configuration( ).getTemplate( strTemplate ).process( model, writer );

        return writer.toString( );
    }

    /**
     * The list of posts given by NewsletterDocumentServiceJspBean is written as HTML.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testInsertedPostsAreWrittenAsHtml( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "documents_list", HtmlMarkup.of( POSTS_HTML ) );

        String strHtml = render( TEMPLATE_INSERT, model );

        assertTrue( strHtml.contains( POSTS_HTML ), strHtml );
    }

    /**
     * The search date typed by the user and the names of the posts found are written as text.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testSearchFormWritesValuesAsText( ) throws Exception
    {
        Blog blog = new Blog( );
        blog.setId( 1234 );
        blog.setContentLabel( TEXT_WITH_MARKUP );

        Map<String, Object> model = new HashMap<>( );
        model.put( "documents_lists_list", new ReferenceList( ) );
        model.put( "documents_templates_list", new ReferenceList( ) );
        model.put( "document_list_id", "0" );
        model.put( "start_published_date", DATE_WITH_MARKUP );
        model.put( "document_list", List.of( blog ) );

        String strHtml = render( TEMPLATE_SELECT, model );

        assertTrue( strHtml.contains( "value=\"" + DATE_ESCAPED + "\"" ), strHtml );
        assertFalse( strHtml.contains( DATE_WITH_MARKUP ), strHtml );
        assertTrue( strHtml.contains( TEXT_ESCAPED ), strHtml );
        assertFalse( strHtml.contains( TEXT_WITH_MARKUP ), strHtml );
    }

    /**
     * Without a search date, the field holds the date of fifteen days ago.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testSearchFormDefaultsToARecentDate( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "documents_lists_list", new ReferenceList( ) );
        model.put( "document_list_id", "0" );

        String strHtml = render( TEMPLATE_SELECT, model );

        assertTrue( strHtml.matches( "(?s).*name=\"published_date\" value=\"\\d{2}/\\d{2}/\\d{4}\".*" ), strHtml );
    }

    /**
     * The newsletter model writes the title and the description of a post as text, and the identifiers of its links as
     * numbers.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testNewsletterModelWritesPostsAsText( ) throws Exception
    {
        ContentType image = new ContentType( );
        image.setIdContentType( 1 );
        DocContent docContent = new DocContent( );
        docContent.setId( 4321 );
        docContent.setContentType( image );
        BlogPublication publication = new BlogPublication( );
        publication.setIdBlog( 1234 );
        publication.setIdPortlet( 5678 );
        Blog blog = new Blog( );
        blog.setId( 1234 );
        blog.setContentLabel( TEXT_WITH_MARKUP );
        blog.setDescription( TEXT_WITH_MARKUP );
        blog.setDocContent( List.of( docContent ) );
        blog.setBlogPublication( List.of( publication ) );

        Map<String, Object> model = new HashMap<>( );
        model.put( "blogs_list", List.of( blog ) );
        model.put( "base_url", "http://localhost/" );

        String strHtml = render( TEMPLATE_MODEL, model );

        assertTrue( strHtml.contains( "alt=\"" + TEXT_ESCAPED + "\"" ), strHtml );
        assertTrue( strHtml.contains( "<td>" + TEXT_ESCAPED + "</td>" ), strHtml );
        assertFalse( strHtml.contains( TEXT_WITH_MARKUP ), strHtml );
        assertTrue( strHtml.contains( "id_file=4321\"" ), strHtml );
        assertTrue( strHtml.contains( "page=blog&id=1234&portlet_id=5678\"" ), strHtml );
    }
}
