var mySpaceContent = {
	initialize : function(){
		if($('.ui.labeled.icon.sidebar') == undefined)
			console.log("jquery $$$ not working");
		else  {
			console.log("jquery $$$ working \n");
			console.log($('.ui.labeled.icon.sidebar'));
		}
		
		$('a.sidebar-toggle').click(function() {
			$('#sidebar').sidebar('toggle')
		});
		
		document.getElementById("username").innerHTML = localStorage.getItem("username");
		
		/*
		$('#toggle').click(function() {
			$('.ui.labeled.icon.sidebar').sidebar('toggle');
		});
		*/
		/*
		var page_owner = localStorage.getItem("username");
		document.getElementById("usernameDiv").innerHTML = "Welcome " + page_owner;
		*/
	}
};


window.onload = function() {
	mySpaceContent.initialize();
}