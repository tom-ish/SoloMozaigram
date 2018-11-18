var mySpaceContent = {
	initialize : function(){
		$('a.sidebar-toggle').click(function() {
			$('#sidebar').sidebar('toggle')
		});
		
		$('#reset-button').click(function() {
			$('#userKeyword').value = "";
			$('#dragNdropInput').value = "";
			$('#dragNdropInput').show();
			$('#droppedZone').remove();
			$('#droppedImgPath').html("");
			$('#dropZone').show();
			fileUploaded = false;
		});
		
		$('#generate-button').click(function() {
			var str = document.getElementById('dragNdropInput').value;
			if( (typeof str === "undefined") || (str.length <= 0) )
				return;
			console.log(str);
			
//			ServerServices.uploadData(dragNdropForm.userKeyword.value, fileList[0]);
			//ServerServices.asyncUploadData(dragNdropForm, sessionkey);


		});
		
		
		$('#userKeyword').focusout(function() {
			if(fileUploaded)
				$('#generate-button').removeClass("disabled");
		})
		
		$('#username').html(localStorage.getItem("username"));
		
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