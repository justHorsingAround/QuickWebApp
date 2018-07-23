<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <c:url value="/style.css" var="styleUrl"/>
        <c:url value="/protected/main.js" var="mainUrl"/>
        <c:url value="/protected/table.js" var="tableUrl"/>
        <c:url value="/protected/show-data.js" var="showUrl"/>
        <c:url value="/protected/add-product.js" var="adUrl"/>
        <c:url value="/protected/see-all-available-products.js" var="asapUrl"/>
        <link rel="stylesheet" type="text/css" href="${styleUrl}">
        <script src="${mainUrl}"></script>
        <script src="${tableUrl}"></script>
         <script src="${adUrl}"></script>
         <script src="${asapUrl}"></script>
         <script src="${showUrl}"></script>


        <script src="${showAllCustomersUrl}"></script>
    </head>
<body>
    <div id="main-menu">
        <div class="sidenav" id="sidenav">
            <a href="" id="asap" onclick="return false;">Show all avaialable products</a>
            <a href="" id="add-product" onclick="return false;">Add product</a>
        </div>
        <div id="add-content"></div>
        <div id="content">
        </div>
    </div>
</body>
</html>