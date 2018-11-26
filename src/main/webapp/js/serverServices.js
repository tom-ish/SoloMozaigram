var ServerServices = {
		signup : function signup(username, pwd, pwd2, email) {
			console.log("signup called with username: " + username + ", password: " + pwd + ", password2: "+pwd2+ ", email: "+email);;
			$.ajax ({
				type: "POST",
				url: "CreateUserServlet",
				data: "username=" + username + "&password=" + pwd + "&password2=" + pwd2 + "&email=" + email,
				dataType: 'json',
				success: function(json){
					if(json.CreateUserServlet == SUCCESS_CODE){
						console.log("signUp success!");
						isLoginPage = false;
					}
					else{
						console.log("signUp failed with code: " + json.CreateUserServlet);
						console.log("username : " + json.username);
						console.log("password : " + json.password);
						console.log("password2 : " + json.password2);
						console.log("email : " + json.email);
					}
				},
				error: function(jqXHR , textStatus , errorThrown ){
					console.log(textStatus);
					console.log(jqXHR.responseText + " status: " + jqXHR.status);
					console.log("errorThrown : " + errorThrown);
//					alert("Erreur Ajax: SignUp is not working.\n" + textStatus + " " + errorThrown);

					loadErrorPage(jqXHR.responseText);
				}
			});
		},
		connect : function connect(username, password) {
			var serverResponded = false;
			$.ajax ({
				type: "POST",
				url: "ConnectUserServlet",
				/*
				contentType: false, // obligatoire pour de l'upload
				processData: false, // obligatoire pour de l'upload
				*/
				data: "username=" + username + "&password=" + password,
				dataType: 'json',
				success: function(json){
					if(json.ConnectUserServlet == SUCCESS_CODE){
						console.log("Connexion success!");
						console.log(json);
						console.log(json.friendRequest);
						console.log(json.friends);
						console.log(json.images);
						
						serverResponded = true;
						// Store username in localStorage Object before switching to MozaikPage
						// Web Storage Compatibility should be checked at start
						localStorage.clear();
						localStorage.setItem("username", json.username);
						localStorage.setItem("sessionKey", json.sessionKey);
						localStorage.setItem("friends", json.friends);
						localStorage.setItem("friendRequests", json.friendRequests);
						localStorage.setItem("requestedpage", json.username);

						var images = [];
						if(!(json.images == null || typeof json.images == "undefined" || json.images.length == 0)) {
							for (var i = 0; i < json.images.length; i++) {
								var imageItem = json.images[i];
								var comments = [];
								if(!(imageItem.allComments == null || typeof imageItem.allComments == "undefined" || imageItem.allComments.length == 0)) {
									for (var i = 0; i < imageItem.allComments.length; i++) {
										var commentItem = imageItem.allComments[i];
										var comment = new Comment(commentItem.id, commentItem.image,
											commentItem.auteur, commentItem.text, commentItem.date);
										comments.push(comment);
									}
								}

								var mosaic = new Mosaic(imageItem.id, imageItem.link, imageItem.originalFilename,
									imageItem.keyword, imageItem.creationDate, imageItem.user, comments);
								images.push(mosaic);
								console.log(mosaic);
							}
						}
						localStorage.setItem("images", images);
//						localStorage.setItem("images", JSON.stringify(json.images));
						switchToMyPage();
					}
					else{
						console.log("Connexion failed!");
						console.log("returned code : " + json.ConnectUserServlet);
					}
				},
				/*
				complete: function(json) {
					if(!serverResponded) {
						setTimeout(function(){
							// Schedule the next
							ServerServices.connect(username, password);
						}, 1000);
						console.log("connect FAILURE");
					}
					else {
						console.log("connect SUCCESS");
					}
					
				},*/
				error: function(jqXHR , textStatus , errorThrown ){
					//alert("Erreur Ajax: Connexion is not working.\n" + textStatus + " " + errorThrown);
					
					//loadErrorPage(jqXHR.responseText);
					
					if(jqXHR.status === 503) {
						console.log("connect error : jqXHR = " + jqXHR.status);
						ServerServices.connect(username, password);
					}
					else {
						console.log(textStatus);
						console.log(jqXHR.responseText + " status: " + jqXHR.status);
					}
				}
			});
		},
		logout : function logout(username, sessionkey) {
			$.ajax({
				type: "POST",
				url: "LogoutUserServlet",
				data: "username=" + username + "&sessionkey=" + sessionkey,
				dataType: 'json',
				success: function(json) {
					if(json.LogoutUserServlet == SUCCESS_CODE) {
						console.log("Deconnexion success!");
						console.log(json);
						localStorage.clear();

					}
				},
				error: function(jqXHR, textStatus, errorThrown) {

					loadErrorPage(jqXHR.responseText);
				}
			});
		},
		uploadData : function uploadData(userKeyword, imgFile, sessionkey) {
			console.log("ServerServices.uploadData()");
			console.log(userKeyword);
			console.log("filename : ");
			console.log(imgFile.name);
			console.log(sessionkey);
			
			var dataform = new FormData();
			dataform.append("userKeyword", userKeyword);
			dataform.append("imageFile", imgFile);
			dataform.append("sessionkey", sessionkey);
			
			$.ajax({
				type: "POST",
				url: "UploadDataServlet",
				data: dataform,
				cache: false,
				contentType: false, // obligatoire pour de l'upload
				processData: false, // obligatoire pour de l'upload
				dataType: 'json',
				success: function(json) {
					if(json.UploadDataServlet == PROCESS_COMPLETABLE_FUTURE_TASKS_STARTED) {
						console.log(json);
						console.log("sessionKey: " + sessionkey);
						console.log("userTaskId: " + json.userTaskId);
						is_uploading = true;
						ServerServices.isMozaikGenerated(sessionkey, json.userTaskId);
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {
					console.log(textStatus);
					console.log(jqXHR.responseText + " status : " + jqXHR.status);
					loadErrorPage(jqXHR.responseText);
				}
			});
		},
		/*
		asyncUploadData : function asyncUploadData(form, sessionkey) {
			console.log("ServerServices.AsyncUploadData()");
			console.log(form);
			console.log(form.userKeyword.value);
			var dataform = new FormData(form);
			dataform.append("sessionkey", sessionkey);
			console.log("filename : ");
			console.log(""+$('#dragNdropInput').val());
			var generated2 = false;
			
			$.ajax({
				type: "POST",
				url: "AsyncUploadDataServlet",
				data: dataform,
				cache: false,
				contentType: false, // obligatoire pour de l'upload
				processData: false, // obligatoire pour de l'upload
				dataType: 'json',
				success: function(json) {
					if(json.AsyncUploadDataServlet == PROCESS_COMPLETABLE_FUTURE_TASKS_STARTED) {
						console.log(json);
					}
					else if(json.AsyncUploadDataServlet == SUCCESS) {
						console.log("sessionKey: " + sessionkey);
						generated2 = true;
					}
				},
				complete: function(json) {
					if(!generated2) {
	                    console.log(json);	
					}
					else {
						console.log("ServerServices.AsyncUploadData() SUCCESS");
						console.log(json);	
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {
					console.log(textStatus);
					console.log(jqXHR.responseText + " status : " + jqXHR.status);
				}
			});
		},*/
		isMozaikGenerated : function isMozaikGenerated(sessionkey, userTaskId) {
			console.log("isMozaikGenerated called from client sessionkey : "+sessionkey+"...");
			$.ajax({
				type: "POST",
				url: "IsMozaikGeneratedServlet",
				data: "sessionkey=" + sessionkey + "&userTaskId=" + userTaskId,
				dataType: 'json',
				success: function(json) {
					if(json.IsMozaikGeneratedServlet == SUCCESS_CODE) {
						if(json.status == PROCESS_COMPLETE) {
							console.log("imgPath available : ")
							console.log(json);
							// imgPath contains the server Path to the image here
							console.log(json.imgPath);
							console.log(json.imgOriginalFilename);
							console.log(json.imgCreationDate);
							
							$('#generate-button').disabled = "";
							$('#generate-button').removeClass('loading disabled');
							is_uploading = false;
							generated = true;
						}
					}
				},
				complete: function(json) {
					console.log(json);
					if(!generated) {
						setTimeout(function(){
							// Schedule the next
							ServerServices.isMozaikGenerated(sessionkey, userTaskId);
						}, 10000);
					}
					else {
						console.log("SUCCESS");
					}
					
				},
				error: function(jqXHR, textStatus, errorThrown) {
					console.log(textStatus);
					console.log(jqXHR.responseText + " status : " + jqXHR.status);
				}
			});
		},
		
		
		sendFriendRequest : function sendFriendRequest(sessionkey, friendname) {
			console.log("sendFriendRequest called...");
			$.ajax({
				type: "POST",
				url: "SendFriendRequestServlet",
				data: "sessionkey=" + sessionkey + "&friendname=" + friendname,
				dataType: 'json',
				success: function(json){
					if(json.SendFriendRequestServlet == SUCCESS_CODE){
						console.log("addFriend success!");
						console.log("returned code : " + json.SendFriendRequestServlet);
					}
					else{
						console.log("addFriend failed!");
						console.log("returned code : " + json.SendFriendRequestServlet);
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {
					console.log(textStatus);
					console.log(jqXHR.responseText + " status : " + jqXHR.status);
				}
			});
		},
		acceptFriendRequest : function acceptFriendRequest(sessionkey, friendid) {
			console.log("acceptFriendRequest called...");
			$.ajax({
				type: "POST",
				url: "AcceptFriendRequestServlet",
				data: "sessionkey=" + sessionkey + "&userid=" + friendid
			});
		},
		
		getAllFriends : function getAllFriends(sessionkey) {
			console.log("getAllFriends called...");
			$.ajax({
				type: "POST",
				url: "GetAllFriendsServlet",
				data: "sessionkey=" + sessionkey,
				dataType: 'json',
				success: function(json){
					if(json.GetAllFriendsServlet == SUCCESS_CODE){
						console.log("getAllFriends success!");
						console.log(json);
						localStorage.setItem("friends", JSON.stringify(json.friends));
					}
					else{
						console.log("getAllFriends failed!");
						console.log("returned code : " + json.GetAllFriendsServlet);
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {
					console.log(textStatus);
					console.log(jqXHR.responseText + " status : " + jqXHR.status);
				}
			});
		},
		getAllFriendRequest : function getAllFriendRequest(sessionkey) {
			console.log("getAllFriendRequest called...");
			$.ajax({
				type: "GET",
				url: "GetAllFriendRequestServlet",
				data: "sessionkey=" + sessionkey,
				datatype: 'json',
				success: function(json) {
					if(json.GetAllFriendRequestServlet == SUCCESS_CODE){
						console.log("getAllFriendRequest success!");
						console.log(json);
						localStorage.setItem("friendRequests", JSON.stringify(json.friendRequests));
					}
					else {
						console.log("getAllFriendRequest failed!");
						console.log("returned code : " + json.getAllFriendRequestServlet);
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {
					console.log(textStatus);
					console.log(jqXHR.responseText + " status : " + jqXHR.status);
				}
			});
		},
		
		getSearchResults : function getSearchResults(searchword) {
			console.log("getSearchResults called...");
			var sessionkey=localStorage.getItem("sessionKey");
			$.ajax({
				type: "POST",
				url: "SearchServlet",
				data: "sessionkey=" + sessionkey+"&searchword="+searchword,
				dataType: 'json',
				success: function(json) {
					if (json.SearchServlet == SUCCESS_CODE){
						var listResearch = JSON.stringify(json.listResearch);
						var listImg = JSON.stringify(json.listImg);
						localStorage.setItem("listResearch",listResearch);
						localStorage.setItem("listImg",listImg);
						displayResults(searchword);
					}
					else {
						console.log("getSearchResults failed!");
						console.log("returned code : " + json.SearchServlet);
					}
				},
			
				error: function(jqXHR, textStatus, errorThrown) {
					console.log(textStatus);
					console.log(jqXHR.responseText + " status : " + jqXHR.status);
				}
			});
		},
		
		getImgUser : function getImgUser(name) {
			console.log("getImgUser called with parameter name = " + name);
			var sessionkey=localStorage.getItem("sessionKey");
			$.ajax({
				type: "POST",
				url: "GetImgUserServlet",
				data: "sessionkey=" + sessionkey+"&username="+name,
				dataType: 'json',
				success: function(json) {
					if (json.GetImgUserServlet == SUCCESS_CODE){
						listImg = JSON.stringify(json.listImg);
						console.log("getImgUser returned "+listImg+" for a search for "+name);
						displayImages(listImg);
					}
					else {
						console.log("getImgUser failed!");
						console.log("returned code : " + json.getImgUserServlet);
					}
				},
			
				error: function(jqXHR, textStatus, errorThrown) {
					console.log(textStatus);
					console.log(jqXHR.responseText + " status : " + jqXHR.status);
				}
			});
		},
		addComment : function addComment(sessionkey, comment, imageId) {
			$.ajax ({
				type: "POST",
				url: "AddCommentServlet",
				/*
				contentType: false, // obligatoire pour de l'upload
				processData: false, // obligatoire pour de l'upload
				*/
				data: "sessionkey=" + sessionkey+ "&text=" + comment +"&imgid="+imageId,
				dataType: 'json',
				success: function(json){
					if(json.AddCommentServlet == SUCCESS_CODE){
						console.log("add Comment success!");
						console.log("returned code : ")
						console.log(json);
						var lastCommentInfo = json.authorId+STRINGIFY_SEPARATOR+json.text+STRINGIFY_SEPARATOR+json.imgId;
						localStorage.setItem("lastCommentInfo", lastCommentInfo);
					}
					else{
						console.log("add Comment failed!");
						console.log("returned code : ");
						console.log(json);
					}
				},
				error: function(jqXHR , textStatus , errorThrown ){
					console.log(textStatus);
					console.log(jqXHR.responseText + " status: " + jqXHR.status);
					//alert("Erreur Ajax: Connexion is not working.\n" + textStatus + " " + errorThrown);
				}
			});
		},
		myMozaikThumbnails : function myMozaikThumbnails(sessionkey){
			console.log("myMozaikThumbnais");
			console.log(sessionkey);
			$.ajax ({
				type: "POST",
				url: "MyMozaikServlet",
				data: "sessionkey=" + sessionkey,
				dataType: 'json',
				success: function(json){
					console.log(json);
					if(json.MyMozaikServlet == SUCCESS_CODE){
						console.log("My Moszaiks success");
						console.log("returned code : ");
						listImages(json);
					}
					else{
						console.log("My Moszaiks failed!");
						console.log("returned code : " + json);
					}
				},
				error: function(jqXHR , textStatus , errorThrown ){
					console.log(textStatus);
					console.log(jqXHR.responseText + " status: " + jqXHR.status);
				}
			});
		},
		myMozaikThumbnailsFromUsername : function myMozaikThumbnailsFromUsername(sessionkey, username){
			console.log("myMozaikThumbnailsFromUserName");
			console.log(username);
			$.ajax ({
				type: "POST",
				url: "MyMozaikFromUsernameServlet",
				data: "sessionkey="+sessionkey+"&username=" + username,
				dataType: 'json',
				success: function(json){
					console.log(json);
					if(json.MyMozaikServlet == SUCCESS_CODE){
						console.log("My Moszaiks success");
						console.log("returned code : ");
						listImages(json);
					}
					else{
						console.log("My Moszaiks failed!");
						console.log("returned code : " + json);
					}
				},
				error: function(jqXHR , textStatus , errorThrown ){
					console.log(textStatus);
					console.log(jqXHR.responseText + " status: " + jqXHR.status);
				}
			});
		}
}


loadErrorPage = function(response) {
	
	document.open();
	document.write(response);
	document.close();
}