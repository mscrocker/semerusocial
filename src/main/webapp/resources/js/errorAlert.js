const customAlert = {
	hideAlert: () => {
        const errorAlert = document.getElementById("ErrorAlert");
        errorAlert.classList.add("hidden");
	},
    showAlert: (message) => {
        const errorAlert = document.getElementById("ErrorAlert");
        document.getElementById("alertButton").onclick = customAlert.hideAlert;
        
        const text = document.getElementById("alertText");

        // If there was a previous list from a fieldError, erase it; :(
        const previousUl = document.getElementById("list");
        if (previousUl) {
            previousUl.remove();
        }

      
    

            if (message.globalError) {
                let globalError = message.globalError;
                text.innerText = globalError;
            } else if (message.fieldErrors) {
                text.innerText = "";


                let fieldErrors = [];
                message.fieldErrors.forEach(e => {
                    let fieldName = e.fieldName;
                    fieldErrors.push(`${fieldName}: ${e.message}`);
                });
                let ul = document.createElement("ul");
                ul.setAttribute("id", "list");
                ul.style.flexGrow = 100;
                errorAlert.insertBefore(ul,errorAlert.childNodes[2]);

                fieldErrors.map((fieldError, index) => {
                    let li = document.createElement("li");
                    li.setAttribute("id", index);
                    li.appendChild(document.createTextNode(fieldError));
                    ul.appendChild(li);
                });
            } else {
               text.innerText = message;
            }

            errorAlert.classList.remove("hidden");

        },
    showAlertFromResponse: (response) => {
    	 const previousUl = document.getElementById("list");
         if (previousUl) {
             previousUl.remove();
         }
    	response.json()
    	.then((body) => customAlert.showAlert(body))
    	.catch((e) => customAlert.showAlert("Internal Server Error: " + response.status));
    }
};