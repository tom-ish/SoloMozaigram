var app = {
		// Application Constructor
		initialize: function() {
			
			logZoneVisible = true;
			isLoginPage = true;
			$('#signupZone').hide();
			$('#homePage').hide();
			
			checkBrowserCompatibility();

			$('#goToSignUpZone').click(function(){
				showSignupLoginPage();
			});
			$('#goToSignInZone').click(function(){
				showSignupLoginPage();
			});

			$('#signupButton').click(function(){
				createUser(signupForm);
				return false;
			});
			$('#logButton').click(function(){
				login(logForm);
				return false;
			})
		}
};


function showSignupLoginPage() {
	console.log("showSignupPage called with logZoneVisible : "+logZoneVisible);
	if(logZoneVisible) {
		$('#logZone').hide();
		$('#signupZone').show();
		logZoneVisible=false;
	}
	else {
		$('#signupZone').hide();
		$('#logZone').show();
		logZoneVisible=true;
	}
}

function login(formulaire) {
	username = formulaire.username.value;
	pwd = formulaire.password.value;

	var ok = verif_connect_form(username, pwd);
	console.log("verif_connect_form" + ok);
	console.log("username : " + username + ", pwd : " + pwd);
	if(ok) {
		ServerServices.connect(username, pwd);

	}
}

function createUser(formulaire) {
	username = formulaire.username.value;
	pwd = formulaire.password.value;
	pwd2 = formulaire.password2.value;
	email = formulaire.email.value;

	var ok = verif_form(username, pwd, email);
	if(pwd != pwd2) ok = !ok;

	if(ok) {
		ServerServices.signup(username, pwd, pwd2, email);
		switchToHomePage();
	}
}

function verif_form(login, pwd, email) {
	console.log("verif_form executing...");
	// a implementer en detail...
	if(login.length == 0) {
		error("#usernameInput");
		return false;
	}
	if(pwd.length == 0) {
		error("#passwordInput");
		return false;
	}
	if(pwd.length < 6) {
		error("#passwordInput");
		return false;
	}
	if(email.length == 0) {
		error("#emailInput");
		return false;
	}
	return true;
}

function verif_connect_form(login, pwd) {
	console.log("verif_connect_form executing...");
	// a implementer en detail
	if(login.length == 0) {		
		error("#usernameInput");
		return false;
	}
	if(pwd.length == 0 || pwd.length < 6) {
		error("#passwordInput");
		return false;
	}
	return true;
}

function error(input) {
	console.log("error called with input : " + input);
	// a implementer...
}


function switchToHomePage() {
	console.log("switchToHomePage called");
	if(isLoginPage) {
		isLoginPage = false;
	}
	
	$('#logZone').hide();
	$('#signupZone').hide();
	$('#homePage').show();
	logZoneVisible=false;
}

function switchToMyPage() {
	console.log("switchToMyPage called");
	window.location.href = "./myspace.html";
}

function checkBrowserCompatibility() {
	console.log("checking Browser Compatibility...");
	if(typeof(Storage) !== "undefined") {
		// Code for LocalStorage/sessionStage
	}	
	else {
		// No Web Storage support...
	}
}


window.onload = function(){
	app.initialize();
}
