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
