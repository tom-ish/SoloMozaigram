validateForm = {};

//ready event
validateForm.ready = function() {
	
	var loginValidationRules = {
		inline : true,
		delay : false,
		on : 'blur',
		fields : {
			email : {
				identifier : 'email',
				rules : [
					{
						type : 'empty',
						prompt : 'Please enter your email'
					}
				]
			},
			password : {
				identifier : 'password',
				rules : [
					{
						type : 'empty',
						prompt : 'Please enter a password'
					}
				]
			}
		},
		onSuccess : function(event, fields) {
			event.preventDefault();
			console.log("login success");
			login();
		}
	};
	
	var login = function() {
		var formulaire = $('.form#loginForm');
		
		username = formulaire.find('#usernameInput').val();
		pwd = formulaire.find('#passwordInput').val();
/*
		var ok = verif_connect_form(username, pwd);
		console.log("verif_connect_form" + ok);
		console.log("username : " + username + ", pwd : " + pwd);
		if(ok) {
			ServerServices.connect(username, pwd);
		}
		*/
		ServerServices.connect(username, pwd);
	};
	
	
	
	var signupValidationRules = {
		inline : true,
		delay: false,
		on	   : 'blur',
		fields : {
			username : {
				identifier : 'username',
				rules : [
					{
						type : 'empty',
						prompt : 'Please enter your username'
					}
				]
			},
			email : {
				identifier : 'email',
				rules : [
					{
						type : 'empty',
						prompt : 'Please enter your email'
					}
				]
			},
			password : {
				identifier : 'password',
				rules : [
					{
						type : 'empty',
						prompt : 'Please enter a password'
					},
					{
						type : 'length[6]',
						prompt : 'Your password must be at least 6 characters'
					}
				]
			},
			confirmPassword : {
				identifier : 'confirm-password',
				rules : [
					{
						type : 'empty',
						prompt : 'Please enter a password'
					},
					{
						identifier : 'confirm-password',
						type : 'match[password]',
						prompt : 'Your password must be at least 6 characters'
					}
				]
			},
			terms : {
				identifier : 'terms',
				rules : [
					{
						type : 'checked',
						prompt : 'You must agree to the terms and conditions'
					}
				]
			}
		},
		onSuccess: function(event, fields) {
			event.preventDefault();
			console.log("signup success");
			createUser();
		}
	};
	

	
	var createUser = function() {
		var formulaire = $('.form#signupForm');
		
		username = formulaire.find('#usernameInput').val();
		email = formulaire.find('#emailInput').val();
		pwd = formulaire.find('#passwordInput').val();
		pwd2 = formulaire.find('#confirmPasswordInput').val();
		
		console.log("input values : " + username + ", " + pwd + ", " + pwd2 + ", " + email);

		ServerServices.signup(username, pwd, pwd2, email);
		switchToHomePage();
	};
	
	$('.form#loginForm').form(loginValidationRules);
	$('.form#signupForm').form(signupValidationRules);
	
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

// attach ready event
$(document).ready(validateForm.ready);

/*
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

function createUser() {
	var formulaire = $('.form#signupForm');
	
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
*/
/*
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
*/