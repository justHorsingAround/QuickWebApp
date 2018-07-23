<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <c:url value="/style.css" var="styleUrl"/>
        <c:url value="/index.js" var="indexUrl"/>
        <link rel="stylesheet" type="text/css" href="${styleUrl}">
         <script src="${indexUrl}"></script>
        <title>pa3</title>
    </head>
<body>
<div id='login' class='login'>
    <div id='login-header-text'>Login</div>
    <form id='login-form' onsubmit="return false;">
        <p>
            <input type='text' name='supplier' value='Supplier ID'>
            <input type='text' name='shipper' value='Shipper ID'>
        </p>
        <p>
            <button id='login-btn'>Login</button>
        </p>
        <p>
            <div id='login-error' class='status-error'></div>
        </p>
    </form>
</div>




</body>
</html>
