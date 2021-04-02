let hideEmployeeSavedAlertTimer = undefined;

document.addEventListener("DOMContentLoaded", () => {
	// TODO: Things that need doing when the view is loaded
	document.getElementById("saveButton").addEventListener("click", saveActionClick);
});

// Save
function saveActionClick(event) {
	// TODO: Actually save the employee via an AJAX call

		if (!validateSave()) {
				return;
		}

		const saveActionElement = event.target;
		saveActionElement.disabled = true;

		const employeeId = getEmployeeId();
		const employeeIdIsDefined = (employeeId.trim() !== "");
		const saveActionUrl = ("/api/employee/" + (employeeIdIsDefined ? employeeId : ""));
		const saveEmployeeRequest = {
			id: employeeId,
			managerId: getEmployeeManagerId(),
			lastName: getEmployeeLastNameEditElement().value,
			firstName: getEmployeeFirstNameEditElement().value,
			password: getEmployeePasswordEditElement().value,
			classification: getEmployeeTypeSelectElement().value };

		if (employeeIdIsDefined) {
			ajaxPatch(saveActionUrl, saveEmployeeRequest, (callbackResponse) => {
				saveActionElement.disabled = false;

				if (isSuccessResponse(callbackResponse)) {
						completeSaveAction(callbackResponse);
				}
			});
		}
		else {
			ajaxPost(saveActionUrl, saveEmployeeRequest, (callbackResponse) => {
				saveActionElement.disabled = false;

				if(isSuccessResponse(callbackResponse)) {
					completeSaveAction(callbackResponse);
				}

			});
		}
}

function validateSave() {
	const firstNameEditElement = getEmployeeFirstNameEditElement();
	if (firstNameEditElement.value.trim() === "") {
		displayError("Please enter a valid First Name.");
        firstNameEditElement.focus();
		firstNameEditElement.select();
		return false;
	}

	const lastNameEditElement = getEmployeeLastNameEditElement();
	if (lastNameEditElement.value.trim() === "") {
		displayError("Please enter a valid Last Name.");
        lastNameEditElement.focus();
		lastNameEditElement.select();
		return false;
	}

	const passwordEditElement = getEmployeePasswordEditElement();
	if (passwordEditElement.value.trim() === "") {
		displayError("Please enter a valid password.");
        passwordEditElement.focus();
		passwordEditElement.select();
		return false;
	}

	if (passwordEditElement.value !== getEmployeeConfirmPassword()) {
		displayError("Passwords do not match.");
        passwordEditElement.focus();
		passwordEditElement.select();
		return false;
	}

	const employeeTypeSelectElement = getEmployeeTypeSelectElement();
	if(!employeeTypeSelectElement.closest("tr").classList.contains("hidden")) {	
		if (employeeTypeSelectElement.value <= 0) {
			displayError("Please choose a valid employee Type.");
        	employeeTypeSelectElement.focus();
			return false;
		}
	}

	return true;
}

	
function completeSaveAction(callbackResponse) {
	if (callbackResponse.data == null) {
			return;
	}
	
	if ((callbackResponse.data.redirectUrl != null) && 
			(callbackResponse.data.redirectUrl !== "")) {
				window.location.replace(callbackResponse.data.redirectUrl);
				return;
	}

    displayEmployeeSavedAlertModal();

	//const employeeEmployeeIdElement = getEmployeeEmployeeIdElement();
	const employeeEmployeeIdRowElement = getEmployeeEmployeeIdElement.closest("tr");
	if(employeeEmployeeIdRowElement.classList.contains("hidden")) {
			setEmployeeId(callbackResponse.data.id);
			employeeEmployeeIdRowElement.value = callbackResponse.data.employeeId;
			employeeEmployeeIdRowElement.classList.remove("hidden");
	}
}


function displayEmployeeSavedAlertModal() {
	if (hideEmployeeSavedAlertTimer) {
		clearTimeout(hideEmployeeSavedAlertTimer);
	}

	const savedAlertModalElement = getSavedAlertModalElement();
	savedAlertModalElement.style.display = "none";
	savedAlertModalElement.style.display = "block";

	hideEmployeeSavedAlertTimer = setTimeout(hideEmployeeSavedAlertModal, 1200);
}

function hideEmployeeSavedAlertModal() {
	if (hideEmployeeSavedAlertTimer) {
		clearTimeout(hideEmployeeSavedAlertTimer);
	}

	getSavedAlertModalElement().style.display = "none";
}
// End save

//Getters and setters
function getSavedAlertModalElement() {
	return document.getElementById("employeeSavedAlertModal");
}

function getEmployeeId() {
	return document.getElementById("employeeId").value;
}

function setEmployeeId(employeeId) {
	document.getElementById("employeeId").value = employeeId;
}

function getEmployeeManagerId() {
	return document.getElementById("employeeManagerId").value;
}

function getEmployeeEmployeeId() {
	return getEmployeeEmployeeElement().value;
}

function getEmployeeEmployeeIdElement() {
	return document.getElementById("employeeEmployeeId");
}

function getEmployeeFirstNameEditElement() {
	return document.getElementById("firstName");
}

function getEmployeeLastNameEditElement() {
	return document.getElementById("lastName");
}

function getEmployeePasswordEditElement() {
	return document.getElementById("Password");
}

function getEmployeeConfirmPassword() {
	return document.getElementById("confirmation");
}

function getEmployeeTypeSelectElement() {
	return document.getElementById("EmployeeType");
}

//End getters and setters
