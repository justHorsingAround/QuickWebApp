function requestContent(placeIntoThisDivEl, targetServletUrl){
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', function(evt){onResponse(evt, placeIntoThisDivEl);});

    xhr.open('GET', targetServletUrl);
    xhr.send();
}


function onResponse(evt, currentDivEl){
    const currentResponseStatus = evt.target.status;
    clearExistingContent(currentDivEl);
    if(currentResponseStatus == 200){
        const currentResponseContent = JSON.parse(evt.target.responseText);
        addBackButton(currentDivEl);
        printResponseContent(currentResponseContent, currentDivEl);

    } else if(currentResponseStatus == 204){
        let infoPEl = document.createElement('p');
        let infoMessage = document.createTextNode("No content found");
        infoPEl.appendChild(infoMessage);
        infoPEl.id = "return-status-info";
        infoPEl.style.color = 'blue';
        currentDivEl.appendChild(infoPEl);

    } else {
        alert(" HTTP ERROR, code: " + currentResponseStatus);
    }

}

function clearExistingContent(currentDivEl){
    while(currentDivEl.firstChild){
        currentDivEl.removeChild(currentDivEl.firstChild);
    }
}

function printResponseContent(currentResponseContent, currentDivEl){
    document.getElementById('sidenav').style.display = 'none';  // the make the sidnav disappear

    let tableEl = document.createElement('table');
    tableEl.id = 'content-table';

    let columns = addColumnHeaders(currentResponseContent, tableEl);

    for (let i = 0; i < currentResponseContent.length; ++i) {
        let trEl = document.createElement('tr');
        for (var j = 0; j < columns.length; ++j) {
            let tdEl = document.createElement('td');
            tdEl.appendChild(document.createTextNode(currentResponseContent[i][columns[j]] || '-- NA --'));
            trEl.appendChild(tdEl);
        }
        tableEl.appendChild(trEl);
    }
    currentDivEl.appendChild(tableEl);
}

function addColumnHeaders(currentResponseContent, tableEl){
    let columnSet = [];
    let trEl = document.createElement('tr');

    for (let i = 0; i < currentResponseContent.length; i++) {
        for (let key in currentResponseContent[i]) {
            if (currentResponseContent[i].hasOwnProperty(key) && columnSet.indexOf(key)===-1) {
                 columnSet.push(key);
            }
        }
    }
    for (let k = 0; k < columnSet.length; k++){
        let thEl = document.createElement('th');
        thEl.appendChild(document.createTextNode(columnSet[k]));
        trEl.appendChild(thEl);
    }
    tableEl.appendChild(trEl);
    return columnSet;
}

function addBackButton(currentDivEl){
    let a = document.createElement('a');
    let linkText = document.createTextNode("Back to the main page");
    a.appendChild(linkText);
    a.title = "Back to the main page";
    a.href = "main.jsp";
    a.id ="back-btn";

    currentDivEl.appendChild(a);
}
