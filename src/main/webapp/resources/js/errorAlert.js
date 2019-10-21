const showAlert = (message) => {
	const errorAlert = document.getElementById("ErrorAlert");
	if (message === null){
		errorAlert.classList.add("hidden");
	} else {
		 errorAlert.classList.remove("hidden");
		errorAlert.innerText = message;
	}
};

