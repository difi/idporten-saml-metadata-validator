<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="frags/top.jspf" %>

<body>
    <div class="language-selector">
        <%-- used in accordance with http://fontawesome.io/license/ --%>
        <img src="/images/font-awesome-language.svg">
        <span><fmt:message key="validation.top.changelang" /></span>:
        <select id="locales" onchange="setLocale(this.value)" >
            <option value=""></option>
            <option value="no">Norsk</option>
            <option value="en">English</option>
        </select>
    </div>
<div id="wrapper">
    <div class="container">
        <div class="page-header header-frame">
            <h1>
                <img class="head-logo" alt=" " src="/images/eid-logo.gif">
                SAML Metadata Validator
            </h1>
        </div>

        <div class="form-block">
            <form method="POST" enctype="multipart/form-data" action="/">
                <div class="section-container">
                    <span><fmt:message key="validation.index.uploadfile"/></span>
                    <span id="filename" class="section-container-filename">
                        <fmt:message key="validation.index.nofile"/>
                    </span>
                    <div class="button-group">
                        <label id="file-picker-overlay" for="file-picker" class="file-selector-overlay">
                            <fmt:message key="validation.index.choosefile"/>
                        </label>
                        <input id="file-picker" type="file" name="file" accept=".xml" onchange="setFilename()"/>
                        <fmt:message key="validation.index.validate" var="validate" />
                        <input id="validate" class="button-disabled" type="submit" value='${validate}' disabled
                               onclick="showResult()"/>
                    </div>
                </div>
            </form>
        </div>

        <c:if test="${showpanel==true}">
            <div id="result-panel" class="panel">
                <div class="panel-heading">
                    <fmt:message key="validation.index.resultof"/> <c:out value="${filename}"/>
                </div>
                <div class="panel-body">
                    <c:forEach items="${validationResult.details}" var="detailList">
                        <span class="<c:out value="${detailList.status}"/>"><c:out value="${detailList.detail}"/></span>
                    </c:forEach>
                </div>
                <c:if test="not empty ${validationResult.result}">
                    <div class="panel-body">
                        <c:out value="${validationResult.result}"/>
                    </div>
                </c:if>
                <div class="panel-footer">
                    <c:out value="${validationResult.message}"/>
                </div>
            </div>
        </c:if>
        <div class="footer">
            Version: <c:out value="${appVersion}"/>
        </div>
    </div>
</div>

<%@ include file="frags/bottom.jspf" %>