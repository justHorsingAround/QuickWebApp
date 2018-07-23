let prevViewDivEl;
let currViewDivEl;

function getAuthorization() {
    return JSON.parse(localStorage.getItem('user'));
}


function updateView(div) {
    if (currViewDivEl != null) {
        prevViewDivEl = currViewDivEl;
        prevViewDivEl.style.display='none';
    }
    currViewDivEl = document.getElementById(div);
    currViewDivEl.style.display='block';
}

function deleteCurrentDiv(div){
    while(div.firstChild){
    div.removeChild(div.firstChild);
    }
}


function setUp(){
    setUpShowData();
    setUpAsap();
    setUpAddProduct();

}

document.addEventListener('DOMContentLoaded', function(){
    setUp(); });