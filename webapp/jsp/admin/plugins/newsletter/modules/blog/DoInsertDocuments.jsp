<%@ page errorPage="../../../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.plugins.newsletter.modules.blog.web.NewsletterDocumentServiceJspBean"%>

${ pageContext.response.sendRedirect( newsletterDocumentServiceJspBean.doInsert( pageContext.request )) }
