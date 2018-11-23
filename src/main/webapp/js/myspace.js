var mySpaceContent = {
	initialize : function(){
		$('a.sidebar-toggle').click(function() {
			$('#sidebar')
				.sidebar('setting', 'transition', 'overlay')
				.sidebar('toggle')
		});
		
		$('#reset-button').click(function() {
			$('#userKeyword').value = "";
			$('#dragNdropInput').value = "";
			$('#dragNdropInput').show();
			$('#droppedZone').remove();
			$('#droppedImgPath').html("");
			$('#reset-button').addClass("disabled");
			$('#generate-button').addClass("disabled");
			$('#dropZone').show();
			
			fileUploaded = false;
			userKeywordMissing = true;
			userKeyword = "";
		});
		
		$('#generate-button').click(function() {
			var imgFile = document.getElementById('dragNdropInput').files[0];
			ServerServices.uploadData(userKeyword, imgFile, localStorage.getItem("sessionKey"));
			//ServerServices.asyncUploadData(dragNdropForm, sessionkey);

			$('#generate-button').addClass('loading disabled');
			return false;
		});
		
		
		$('#userKeyword').focusout(function() {
			userKeyword = this.value;
			console.log(userKeyword);
			if( (typeof userKeyword !== "undefined") && (userKeyword.length > 0) )
				userKeywordMissing = false;
			if(!userKeywordMissing && fileUploaded && !is_uploading)
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
	},

	initializeUserImagesGallery : function() {
		var images = JSON.parse(localStorage.getItem("images"));
		for(i in images) {
			console.log(images[i]);
			let image = images[i];
			var aImg = document.createElement('a');
			aImg.className = "item";

			var img = document.createElement('img');
			img.className = "ui image";
			img.id = image.id;
			img.src = image.link;
			aImg.appendChild(img);

			$('#user_images_gallery').append(aImg);
		}
	}
};


window.onload = function() {
	mySpaceContent.initialize();
	mySpaceContent.initializeUserImagesGallery();
}