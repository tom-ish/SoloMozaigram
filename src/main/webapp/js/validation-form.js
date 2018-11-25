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
			event.stopImmediatePropagation();
			console.log("login form validation success");
			login();
		}
	};
	
	var login = function() {
		var formulaire = $('#loginForm');
		
		username = formulaire.find('#loginUsernameInput').val();
		pwd = formulaire.find('#loginPasswordInput').val();
/*
		var ok = verif_connect_form(username, pwd);
		console.log("verif_connect_form" + ok);
		console.log("username : " + username + ", pwd : " + pwd);
		if(ok) {
			ServerServices.connect(username, pwd);
		}
		*/
		
		$('#loginButton').addClass('loading disabled');
		
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
			event.stopImmediatePropagation();
			console.log("signup form vaildation success");
			createUser();
		}
	};
	

	
	var createUser = function() {
		var formulaire = $('#signupForm');
		
		username = formulaire.find('#signupUsernameInput').val();
		email = formulaire.find('#signupEmailInput').val();
		pwd = formulaire.find('#signupPasswordInput').val();
		pwd2 = formulaire.find('#signupConfirmPasswordInput').val();
		
		console.log("input values : " + username + ", " + pwd + ", " + pwd2 + ", " + email);
		$('#signupButton').addClass('loading disabled');

		ServerServices.signup(username, pwd, pwd2, email);
	};
	
	
	$('#loginForm').form(loginValidationRules);
	$('#signupForm').form(signupValidationRules);
	
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