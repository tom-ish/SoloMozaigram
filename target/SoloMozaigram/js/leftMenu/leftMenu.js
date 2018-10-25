var username = localStorage.getItem("username");
var sessionkey = localStorage.getItem("sessionKey");

var leftMenu = {
	initialize : function() {
		$('#logoutButton').click(function(){
			console.log("Logout Clicked!");
			ServerServices.logout(username, sessionkey);
			window.location.href = "./index.html"
			return false;
		});
		$('#homeButton').click(function(){
			goHome();
			return false;
		});
		$('#mozaikButton').click(function(){
			goMozaik();
			return false;
		});
		$('#friendRequestButton').click(function(){
			showFriendRequest();
			return false;
		});
		$('#searchButton').click(function(){
			goSearch();
			return false;
		});
	}
};

function goHome() {
	console.log("go Home called...");
	window.location.href = "./after_login_page.html";
	localStorage.setItem("requestedpage", username);
	window.location.href = "./myspace.html";
	return;
}

function goMozaik(){
	console.log("go to Mozaik called ...");
	window.location.href = "./after_login_page.html";
	return;
}

function goSearch(){
	console.log("go to Search called ...");
	window.location.href = "./search.html";
}

function showFriendRequest(){
	console.log("showing friends request...");
	openFriendRequestSideNav();
	return;
}

function openFriendRequestSideNav() {
	ServerServices.getAllFriends(localStorage.getItem("sessionKey"));
	var friendRequestSideNav = document.getElementById("friendRequestSideNav").style.width="300px";
	document.getElementById("visibleContent").style.marginLeft="300px";
	
	var friends = localStorage.getItem("friends");
	for(let i = 0; i < friends.length; i++) {
		var friendNameDiv = document.createElement('div');
		friendNameDiv.id = friends[i].userid;
		$(friendNameDiv).text(friends[i].username);
		friendRequestSideNav.append(friendNameDiv);
	}
	
	return;
}

function closeFriendRequestSideNav() {
	document.getElementById("friendRequestSideNav").style.width="0";
	document.getElementById("visibleContent").style.marginLeft="0";
	return;
}

window.onload = function() {
	leftMenu.initialize();
}
