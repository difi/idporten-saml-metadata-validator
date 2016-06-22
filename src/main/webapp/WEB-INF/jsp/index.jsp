<%@ include file="frags/top.jspf" %>

<body>
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
                    <span>Last opp fil</span>
                    <span id="filename" class="section-container-filename">Ingen fil valgt</span>
                    <div class="button-group">
                        <label id="file-picker-overlay" for="file-picker" class="file-selector-overlay">Velg fil</label>
                        <input id="file-picker" type="file" name="file" accept=".xml" onchange="setFilename()"/>
                        <input id="validate" class="button-disabled" type="submit" value="Valider" disabled
                               onclick="showResult()"/>
                    </div>
                </div>
            </form>
        </div>

        <c:if test="${showpanel==true}">
            <div id="result-panel" class="panel">
                <div class="panel-heading">Resultat av validiering av <c:out value="${filename}"/></div>
                <div class="panel-body">
                    <c:out value="${result}"/>
                </div>
                <div class="panel-footer">
                    <c:out value="${message}"/>
                </div>
            </div>
        </c:if>
    </div>
</div>

</body>

</html>