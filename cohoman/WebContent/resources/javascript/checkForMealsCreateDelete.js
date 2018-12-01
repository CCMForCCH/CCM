function checkForMealDelete(form) {

	var text = "Are you sure you want to delete this common meal and associated signups?\n" +
		"Click OK to delete the common meal.\nClick Cancel to avoid deleting the common meal."
	var changeButton = document.getElementById("editMealFormId:radioButtons:0");
	var deleteButton = document.getElementById("editMealFormId:radioButtons:1");
	// alert("value is " + changeButton.checked + ", value is" + deleteButton.checked);
	if (deleteButton.checked) {
	   if (confirm(text)) {
	       return true;
	   } 
	   return false;
    }
	return true;
}

function checkForPizzaDelete(form) {

	var text = "Are you sure you want to delete this pizza/potluck meal and associated signups?\n" +
		"Click OK to delete the pizza/potluck meal.\nClick Cancel to avoid deleting the pizza/potluck meal."
	var changeButton = document.getElementById("editPizzaFormId:radioButtons:0");
	var deleteButton = document.getElementById("editPizzaFormId:radioButtons:1");
	if (deleteButton.checked) {
	   if (confirm(text)) {
	       return true;
	   } 
	   return false;
    }
	return true;
}

function checkForPotluckDelete(form) {

	var text = "Are you sure you want to delete this potluck meal and associated signups?\n" +
		"Click OK to delete the potluck meal.\nClick Cancel to avoid deleting the potluck meal."
	var changeButton = document.getElementById("editPotluckFormId:radioButtons:0");
	var deleteButton = document.getElementById("editPotluckFormId:radioButtons:1");
	if (deleteButton.checked) {
	   if (confirm(text)) {
	       return true;
	   } 
	   return false;
    }
	return true;
}

function checkForMealCreate(form) {

	var text = "Are you sure you want to create a new common meal?\n" +
		"Click OK to create the new common meal.\nClick Cancel to avoid creating a new common meal."
	if (confirm(text)) {
	    return true;
	} 
	return false;
}

function checkForPizzaCreate(form) {

	var text = "Are you sure you want to create a new pizza/potluck meal?\n" +
		"Click OK to create the new pizza/potluck meal.\nClick Cancel to avoid creating a new pizza/potluck meal."
	if (confirm(text)) {
	    return true;
	} 
	return false;
}

function checkForPotluckCreate(form) {

	var text = "Are you sure you want to create a new potluck meal?\n" +
		"Click OK to create the new potluck meal.\nClick Cancel to avoid creating a new potluck meal."
	if (confirm(text)) {
	    return true;
	} 
	return false;
}