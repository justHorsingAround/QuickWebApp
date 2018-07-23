let baseAddProductUrl = "http://localhost:8080/pa3/protected/addproduct";
let contentDivEl;



function setUpAddProduct(){
    const userData = getAuthorization();
    const supplierId = userData.supplier_id;

    contentDivEl = document.getElementById('content');

    if(supplierId != null){
        const addProductDivEl = document.getElementById('add-product');
        addProductDivEl.addEventListener('click', getTableFieldInfo);
    }
}

function getTableFieldInfo(){
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onInfoResponse);

    xhr.open('GET', baseAddProductUrl);
    xhr.send();
}


function onInfoResponse(){
    const infoStatus = this.status;
    if(infoStatus == 200){
        let infoResponseContent = JSON.parse(this.responseText);
        processInfoJson(infoResponseContent);
    }
    else {
        alert("An error happened, HTTP code: " + infoStatus);
    }

}

function processInfoJson(jsonData){
    let columnNames = [];
    let notNullableList = [];
    for(let i in jsonData){
        columnNames.push(i);
        let value = jsonData[i];
        if(value == 0){
        notNullableList.push(i);
        }
    }
    onAddProductLoad(columnNames, notNullableList);
}


function onAddProductLoad(inputNameFields, mandatory){
    deleteCurrentDiv(contentDivEl);
    const addContentDivEl = contentDivEl;

    let pEl = document.createElement("p");
    pEl.style.color = "red";
    pEl.appendChild(document.createTextNode("Must fill these fields:"));
    pEl.appendChild(document.createElement("br"));
    for (let i in mandatory){
        pEl.appendChild(document.createTextNode(mandatory[i] + ", "));
    }
    pEl.appendChild(document.createElement("br"));
    addContentDivEl.appendChild(pEl);

    addContentDivEl.style.display = 'block';

    addContentDivEl.appendChild(createForm(inputNameFields, mandatory));
}

function createForm(inputNameFields, mandatory){
    let formEl = document.createElement("form");
    formEl.setAttribute('onclick',"return false;");
    formEl.setAttribute('method',"post");
    formEl.setAttribute('id', "add-form");

    for(let i = 0; i < inputNameFields.length; i++){
        let titleTextPEl = document.createTextNode(inputNameFields[i]);
        let inputEl = document.createElement("input");

        inputEl.setAttribute('type',"text");
        inputEl.setAttribute('name', inputNameFields[i]);
        formEl.appendChild(titleTextPEl);
        formEl.appendChild(document.createElement("br"));
        formEl.appendChild(inputEl);
        formEl.appendChild(document.createElement("br"));
        }

    let submitBtnEl = document.createElement('button');

    submitBtnEl.setAttribute('type',"submit");
    submitBtnEl.appendChild(document.createTextNode("Submit"));
    formEl.appendChild(submitBtnEl);

    submitBtnEl.addEventListener('click', function() {onSubmitBtnClicked(inputNameFields, mandatory);});

    return formEl;
}

function onSubmitBtnClicked(inputNameFields, mandatory){
    const addFormEl = document.getElementById('add-form');

    const params = new URLSearchParams();
    let isFilled = true;

    for(let j = 0; j < inputNameFields.length; j++){
        let getInput = addFormEl.querySelector('input[name=' + inputNameFields[j] + ']');
        let inputValue = getInput.value;
        if (required(mandatory, inputNameFields[j])) {
            if(!(validateInput(inputValue, mandatory))){
                isFilled = false;

            }
        }
        params.append(inputNameFields[j], inputValue);
    }
    if(isFilled){
        sendRequest(params);
    }
}

function required(mandatory, currentField){
    if (mandatory.indexOf(currentField) >= 0) {
        return true;
    }
    return false;
}

function sendRequest(params){
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onAddContentResponse);
    xhr.open('POST', baseAddProductUrl);
    xhr.send(params);

}

function validateInput(input, mandatory){
    if (input == ""){
        deleteCurrentDiv(contentDivEl);
            for(let l = 0; l < mandatory.length; l++){
                let pEl = document.createElement('p');
                let message = document.createTextNode("Please fill the " + mandatory[l] + " field");
                pEl.style.color = "red";
                pEl.appendChild(message);
                contentDivEl.appendChild(pEl);
            }
            addBackButton(contentDivEl);  // from table.js
            return false;
        }
    return true;
}

function onAddContentResponse(){
    deleteCurrentDiv(contentDivEl);
    let pEl = document.createElement('p');
    let addResponseStatus = this.status;

    if (addResponseStatus == 200){
        let addInfoMessage = document.createTextNode(JSON.parse(this.responseText).message);
        pEl.style.color = "green";
        pEl.appendChild(addInfoMessage);
    } else {
        let addErrorMessage = document.createTextNode(JSON.parse(this.responseText).message);
        pEl.style.color = "red";
        pEl.appendChild(addErrorMessage);
    }
    contentDivEl.appendChild(pEl);
}
