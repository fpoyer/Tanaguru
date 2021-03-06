<%@taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>
<compress:html>
<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<c:choose>
    <c:when test="${fn:contains(pageContext.response.locale, '_')}">
        <c:set var="lang">
            ${fn:substringBefore(pageContext.response.locale, "_")}
        </c:set>
    </c:when>
    <c:otherwise>
        <c:set var="lang" value="${pageContext.response.locale}"/>
    </c:otherwise>
</c:choose>
<c:choose>
    <c:when test="${not empty configProperties['cdnUrl']}">
        <c:set var="jqueryUrl" value="${pageContext.request.scheme}://${configProperties['cdnUrl']}/External-Js/jquery-1.9.1.min.js"/>
        <c:set var="auditSetUpDetailsJsUrl" value="${pageContext.request.scheme}://${configProperties['cdnUrl']}/Js/expand-collapse/audit-set-up-details-min.js"/>
    </c:when>
    <c:otherwise>
        <c:set var="jqueryUrl">
            <c:url value="/External-Js/jquery-1.9.1.min.js"/>  
        </c:set>
        <c:set var="auditSetUpDetailsJsUrl">
            <c:url value="/Js/expand-collapse/audit-set-up-details-min.js"/>
        </c:set>
    </c:otherwise>
</c:choose>
<html lang="${lang}">
    <c:set var="pageTitle" scope="page">
        <fmt:message key="auditSetUpSite.pageTitle"/>
        <spring:hasBindErrors name="auditSetUpCommand">
            <fmt:message key="auditSetUp.errorPageTitle"/>
        </spring:hasBindErrors>
    </c:set>
    <%@include file="template/head.jsp" %>
    <body id="tgm-site-set-up">
        <%@include file="template/header-utils.jsp" %>
        <div class="container">
            <c:set var="pageName" scope="page">
                <fmt:message key="auditSetUpSite.h1"/>
            </c:set>
            <ul class="breadcrumb">
                <li><a href="<c:url value="/home.html"/>"><fmt:message key="home.h1"/></a> <span class="divider"></span></li>
                <li><a href="<c:url value="/home/contract.html?cr=${param.cr}"/>">${contractName}</a> <span class="divider"></span></li>
                <li class="active">${pageName}</li>
            </ul>
            <div class="row">
                <div class="span16">
                    <h1>
                        <fmt:message key="auditSetUpSite.h1"/>
                    </h1>
                </div><!-- class="span16" -->
                <c:if test="${defaultParamSet == 'false'}">
                <div class="span16">
                    <div class="alert-message block-message warning">
                        <fmt:message key="auditSetUp.keptLastAuditParameters">
                            <fmt:param>${url}</fmt:param>
                        </fmt:message>
                    </div><!-- class="alert-message warning" -->
                </div><!-- class="span16" -->
                </c:if>
                <div class="span16">
                    <div class="alert-message block-message info">
                        <p><fmt:message key="auditSetUp.message"/></p>
                    </div>
                </div><!-- class="span16" -->
                <c:set var="postUrl" scope="page">
                    <c:url value="/home/contract/launch-audit-site.html?cr=${contractIdValue}"/>
                </c:set>
                <%@include file="template/set-up.jsp" %>
            </div><!-- class="row" -->
        </div><!-- class="container"-->
    <%@include file="template/footer.jsp" %>
    <script type="text/javascript" src="${jqueryUrl}"></script>
    <script type="text/javascript" src="${auditSetUpDetailsJsUrl}"></script>
    </body>
</html>
</compress:html>