var mySpaceContent = {
	initialize : function(){			
		$('ui.labeled.icon.sidebar').sidebar('toggle');
		var page_owner = localStorage.getItem("username");
		$('#usernameDiv').innerHTML = "Welcome " + page_owner;
	}
};


window.onload = function() {
	mySpaceContent.initialize();
}