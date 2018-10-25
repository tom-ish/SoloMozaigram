var username = localStorage.getItem("username");
var sessionkey = localStorage.getItem("sessionKey");

var mozaikcontent = {
		initialize : function() {
			initializeUser();
			console.log("USERNAME : " + username + ", sessionKey : " + sessionkey);
		}
		
};

function initializeUser() {
	console.log("USERNAME : " + username + ", sessionKey : " + sessionkey);

}



window.onload = function() {
	mozaikcontent.initialize();
	leftMenu.initialize();
}
