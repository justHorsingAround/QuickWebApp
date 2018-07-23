function setUpAsap(){
    const baseShowProductsUrl = "http://localhost:8080/pa3/protected/asap";
    const showProductsDivEl = document.getElementById('asap');
    const targetDivEl = document.getElementById('content');
    showProductsDivEl.addEventListener('click', function(){requestContent(targetDivEl, baseShowProductsUrl);});

}

