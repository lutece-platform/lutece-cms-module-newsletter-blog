<%@ page errorPage="../../../../ErrorPage.jsp" %>

<jsp:useBean id="newsletterService" scope="session" class="fr.paris.lutece.plugins.newsletter.modules.blog.web.NewsletterDocumentServiceJspBean" />
<%
	response.sendRedirect( newsletterService.doInsert( request ) );
%>

