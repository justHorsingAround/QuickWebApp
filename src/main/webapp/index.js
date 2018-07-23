let errorDivEl;



function onLoad() {
    errorDivEl = document.getElementById('login-error');

    const loginButtonEl = document.getElementById('login-btn');
    loginButtonEl.addEventListener('click', onLoginButtonClicked);
}


function onLoginButtonClicked(){

    const loginFormEl = document.getElementById('login-form');
    const supplierInput = loginFormEl.querySelector('input[name=supplier]');
    const shipperInput = loginFormEl.querySelector('input[name=shipper]');



    if(supplierInput != null && shipperInput != null) {
        const supplierValue = supplierInput.value;
        const shipperValue = shipperInput.value;

        validateLoginInput(supplierValue, shipperValue);
    }
}


function setAuthorization(user) {
    return localStorage.setItem('user', JSON.stringify(user));
}


function onLoginResponse() {
    let responseStatus = this.status;
    clearMessages();
    if(responseStatus == 200){
        const user = JSON.parse(this.responseText);
        setAuthorization(user);
        window.location.href = "protected/main.jsp";
    }
    else if (responseStatus == 500){
        alert("Internal server error");
    }
    else {
        const loginErrDivEl = document.getElementById("login-error");
        loginErrDivEl.style.display = 'block';

        const response = JSON.parse(this.responseText);

        const pEl = document.createElement('p');
        pEl.classList.add('message');
        pEl.textContent = response.message;

        loginErrDivEl.appendChild(pEl);
    }
}


function clearMessages() {
    const messageEls = document.getElementsByClassName('message');
    for (let i = 0; i < messageEls.length; i++) {
        const messageEl = messageEls[i];
        messageEl.remove();
    }

    while(errorDivEl.firstChild){
        errorDivEl.removeChild(errorDivEl.firstChild)
    }
}

function makeRequest(supplierValue, shipperValue){
    const params = new URLSearchParams();
    params.append('supplier', supplierValue);
    params.append('shipper', shipperValue);



    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onLoginResponse);
    xhr.open('POST', 'login');
    xhr.send(params);
}

function validateLoginInput(supplierValue, shipperValue) {
    clearMessages();

    if (supplierValue == "" && shipperValue == "") {
        errorDivEl.style.display = 'block';
        createLoginErrorMessage(errorDivEl);
        }
    else {
         makeRequest(supplierValue, shipperValue);
    }
}

function createLoginErrorMessage(errorDivEl){
    const pEl = document.createElement('p');
    const errorMessage = "Please fill one of the fields!";
    pEl.appendChild(document.createTextNode(errorMessage));
    errorDivEl.appendChild(pEl);
}



document.addEventListener('DOMContentLoaded', function(){ onLoad();});