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
package fr.paris.lutece.plugins.newsletter.modules.blog.business;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * The use of a newsletter template by the blog topics
 */
public class NewsletterBlogDAOTest extends LuteceTestCase
{
    /**
     * A template is in use when a blog topic refers to it
     */
    @Test
    public void testFindTemplate( )
    {
        // the tables of the plugins are in the default pool of the tests
        Plugin plugin = null;
        int nIdTemplate = 987654;
        assertTrue( "no topic uses the template yet", !NewsletterBlogHome.findTemplate( nIdTemplate, plugin ) );

        NewsletterBlog topic = new NewsletterBlog( );
        topic.setId( 987654 );
        topic.setIdTemplate( nIdTemplate );
        NewsletterBlogHome.createDocumentTopic( topic, plugin );
        try
        {
            assertTrue( "a topic uses the template", NewsletterBlogHome.findTemplate( nIdTemplate, plugin ) );
        }
        finally
        {
            NewsletterBlogHome.deleteDocumentTopic( topic.getId( ), plugin );
        }
    }
}
