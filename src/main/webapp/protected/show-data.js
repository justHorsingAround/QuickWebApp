function setUpShowData(){
    const userData = getAuthorization();
    const supplierId = userData.supplier_id;
    const shipperId = userData.shipper_id;

    const currentDivEl = document.getElementById('content');


    if(supplierId != null){
        createSuppList(currentDivEl, userData);
        console.log("supplier " + userData);


    }
    else if (shipperId != null){
        createShippList(currentDivEl, userData);
        console.log("shipper " + userData);
    }

}

function createSuppList(placeIntoThisDivEl, responseData){
    placeIntoThisDivEl.appendChild(document.createTextNode("Welcome "));
    placeIntoThisDivEl.appendChild(document.createTextNode(responseData.supplier_id));
    placeIntoThisDivEl.appendChild(document.createElement("br"));

}

function createShippList(placeIntoThisDivEl, responseData){
    placeIntoThisDivEl.appendChild(document.createTextNode("Welcome "));
    placeIntoThisDivEl.appendChild(document.createTextNode(responseData.shipper_id));
    placeIntoThisDivEl.appendChild(document.createElement("br"));

}