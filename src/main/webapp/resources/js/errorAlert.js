
const showAlert = (message) => {
	const errorAlert = document.getElementById("ErrorAlert");
	if (message === null){
		errorAlert.style.display = "none";
	} else {
		errorAlert.style.display = "block";
		errorAlert.innerText = message;
	}
};
