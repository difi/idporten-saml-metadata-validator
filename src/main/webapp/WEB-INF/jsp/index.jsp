<%@ include file="frags/top.jspf" %>

<body>
<div id="wrapper">
    <div class="container">
        <div class="page-header header-frame">
            <h1>
                <img alt=" " src="/images/eid-logo.gif" style="padding-right: 10px; vertical-align: bottom;">
                SAML Metadata Validator
            </h1>
        </div>

        <div class="form-block">
            <form method="POST" enctype="multipart/form-data" action="/">
                <div class="section-container">
                    <span>Last opp fil</span>
                    <span id="filename" class="section-container-filename">Ingen fil valgt</span>
                    <div class="button-group">
                        <input type="button" name="file" value="Velg fil"/>
                        <input id="validate" type="submit" value="Valider"/>
                    </div>
                </div>
            </form>
        </div>

        <div id="result-panel" class="panel" style="visibility: hidden">
            <div class="panel-heading">Resultat</div>
            <div class="panel-body">
                <c:out value="${message}"/>
            </div>
            <div class="panel-footer">Footer</div>
        </div>

    </div>
</div>

</body>

</html>