
const showAlert = (message) => {
	const errorAlert = document.getElementById("ErrorAlert");
	//If there was a previous list from a fieldError, erase it; :(
	const previousUl= document.getElementById("list");
	if (previousUl){
		previousUl.remove();
	}

	if (message === null){
		//
	
		errorAlert.classList.add("hidden");
	} else {
		
	    if (message.globalError) {
	        globalError = message.globalError;
	        errorAlert.innerText = globalError;
	    } else if (message.fieldErrors) {
	    
	    	
	        fieldErrors = [];
	        message.fieldErrors.forEach(e => {
	            let fieldName = e.fieldName;
	            fieldErrors.push(`${fieldName}: ${e.message}`);
	        });
	        let ul = document.createElement("ul");
	        ul.setAttribute("id","list");
	        errorAlert.appendChild(ul);
	        
	        fieldErrors.map((fieldError, index) =>{ 
	        let li = document.createElement("li");
	        li.setAttribute("id", index);
	        li.appendChild(document.createTextNode(fieldError));
	        ul.appendChild(li);
	    });
	       }
	    else {
	    	errorAlert.innerText = message;	
	    }

		 errorAlert.classList.remove("hidden");

		
	}
};
