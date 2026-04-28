<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../insert/InsertServiceHeader.jsp" />

<%@page import="fr.paris.lutece.plugins.newsletter.modules.blog.web.NewsletterDocumentServiceJspBean"%>

${ newsletterDocumentServiceJspBean.doSearchDocuments( pageContext.request ) }
