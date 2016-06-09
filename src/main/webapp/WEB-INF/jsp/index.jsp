<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">

<body>
    <h1>Metadatavalidator</h1>
    <div>
        <p><c:out value="${file}"/></p>
        <p><c:out value="${message}"/></p>
        <p><c:out value="${error}"/></p>
    </div>
    <div>
    	<form method="POST" enctype="multipart/form-data" action="/">
            <table>
                <tr><td>Last opp fil:</td><td><input type="file" name="file" /></td></tr>
                <tr><td></td><td><input type="submit" value="Valider" /></td></tr>
            </table>
        </form>
    </div>
</body>

</html>