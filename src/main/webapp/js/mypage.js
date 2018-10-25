	var username = localStorage.getItem("username");
	var sessionkey = localStorage.getItem("sessionKey");
	var friends = localStorage.getItem("friends");
	var friendRequests = localStorage.getItem("friendRequests");

var mypagecontent = {
		initialize : function() {
			initializePage();
	//		loadFriendsListInfo();
//			loadFriendRequestsInfo();
		},
		initializeLeftMenu : function() {
			leftMenu.initialize();			
		},
};

function initializePage() {
	var page_owner;
	var listImg;
	if(localStorage.getItem("searchMode") === "true") {
		page_owner = localStorage.getItem("requestedpage");
		listImg = localStorage.getItem("images");
		sessionkey = localStorage.getItem("sessionKey");
		//ServerServices.getImgUser(page_owner);
		loadThumbnailsFromUsername(sessionkey, page_owner);
		localStorage.setItem("searchMode","false");
	}
	else {
		page_owner=localStorage.getItem("username");
		listImg = localStorage.getItem("images");
		sessionkey = localStorage.getItem("sessionKey");
		loadThumbnails(sessionkey);
	}
	console.log("Page personnelle de "+page_owner);
	var html = "Page personnelle de "+page_owner;
	var test = document.createElement('h1');
	test.className="test";
	test.innerHTML = html;
	document.getElementById("usernameId").appendChild(test);
	
	console.log("Images correspondant à "+page_owner+": "+listImg);
	document.getElementById("libsmall").title="Mosaïques de "+page_owner;

	return;
}

function loadFriendsListInfo() {
	var friends = localStorage.getItem("friends");
	var friendsArray = friends.split(STRINGIFY_SEPARATOR);
	for (var i = 0; i < friendsArray.length; i++) {
		var friendInfoArray = friendsArray.split(STRINGIFY_ATTRIBUTE_SEPARATOR);
		//displayFriend();
	}
	
	return;
}

function displayImages(list){
	var l=list.split(",");
	

		for (var i = 0; i < l.length; i++) {
			if(l[i].charAt(0)=='[' || l[i].charAt(0)==' '){
				l[i]=l[i].substr(1,l[i].length);
			}
			if (l[i].charAt(l[i].length-1)==']' || l[i].charAt(l[i].length-1)==' '){
				l[i]=l[i].substr(0,l[i].length-1);
			}
    		displayImage(l[i],i);
		}
		return;
}

function displayImage(url,i){
		console.log("Displaying "+url);
		var colmd3 = document.createElement('div');
        colmd3.setAttribute('class','col-md-3 browse-grid');
        colmd3.id = 'divimg'+i;        
        libsmall.appendChild(colmd3);

        var objcolmd3 = document.getElementById(colmd3.id);

        objcolmd3.innerHTML = 
        '<a data-toggle="modal" data-target="#myModal'+i+'"> <img'+
            ' src='+url+
            ' class="img-fluid">'+
        '</a>';

        var divmodalfade = document.createElement('div');
        divmodalfade.id = 'myModal' + i; 
        divmodalfade.setAttribute('class','modal fade');
        divmodalfade.setAttribute('myModal','-1');
        divmodalfade.setAttribute('role','dialog');
        divmodalfade.setAttribute('aria-labelledby','myModalLabel');
        divmodalfade.setAttribute('aria-hidden','true');
        document.getElementById(colmd3.id).appendChild(divmodalfade);

        var divmodalimg = document.createElement('div');
        divmodalimg.id = 'divmodalimg'+i;
        divmodalimg.setAttribute('class','modal img-modal');
        document.getElementById(divmodalfade.id).appendChild(divmodalimg);

        var divmodaldialog = document.createElement('div');
        divmodaldialog.id = divmodaldialog+i;
        divmodaldialog.setAttribute('class','modal-dialog modal-lg');
        document.getElementById(divmodalimg.id).appendChild(divmodaldialog);

        var divmodalcontent = document.createElement('div');
        divmodalcontent.id='divmodalcontent'+i;
        divmodalcontent.setAttribute('class','modal-content');
        document.getElementById(divmodaldialog.id).appendChild(divmodalcontent);

        var divmodalbody = document.createElement('div');
        divmodalbody.id='divmodalbody'+i;
        divmodalbody.setAttribute('class','modal-body');
        document.getElementById(divmodalcontent.id).appendChild(divmodalbody);

        var divmodalrow = document.createElement('div');
        divmodalrow.id = 'divmodalrow'+i;
        divmodalrow.setAttribute('class','row');
        document.getElementById(divmodalbody.id).appendChild(divmodalrow);

        var objdivmodalrow = document.getElementById(divmodalrow.id);

        objdivmodalrow.innerHTML = 
        '<div class="col-md-8 modal-image">'+
            '<img class="img-responsive "'+
            'src='+url+'>'+
        '</div>';

        return;
}

function loadFriendRequestsInfo() {
	var friendRequestsSideNav = $(document.getElementById("friendRequestSideNav"));
	
	var requestsArray = friendRequests.split(STRINGIFY_SEPARATOR);
	localStorage.setItem("requestsArray", requestsArray);
	for (var i = 0; i < requestsArray.length; i++) {
		var div = document.createElement("div");
		var requestInfoArray = requestArray.split(STRINGIFY_ATTRIBUTE_SEPARATOR);
		div.innerHTML = requestInfoArray[1];
		friendRequestsSideNav.append(div);
	}
	return;
}



/*
 * THUMBNAILS
 */
function loadThumbnails(sessionkey) {
	ServerServices.myMozaikThumbnails(sessionkey);
	return;
}

function loadThumbnailsFromUsername(sessionkey, username) {
	ServerServices.myMozaikThumbnailsFromUsername(sessionkey, username);
	return;
}

function addComment(sessionkey, comment, imgid){   
    console.log("Comment button clicked");
    console.log(imgid);
    console.log(comment);
    console.log("---------------------");
    console.log(sessionkey);
    ServerServices.addComment(sessionkey, comment, imgid);
    return;
}


function listImages(json){
	console.log("returned json in thumbnails");
	console.log(json);
	
	var imagesjson = json.myMozaikRslt;
	console.log(imagesjson);

    var libsmall = document.getElementById("libsmall");
    for(let i = 0; i < imagesjson.length; i++) {
        var colmd3 = document.createElement('div');
        colmd3.setAttribute('class','col-md-3 browse-grid');
        colmd3.id = 'divimg'+i;        
        libsmall.appendChild(colmd3);

        var objcolmd3 = document.getElementById(colmd3.id);

        objcolmd3.innerHTML = 
        '<a data-toggle="modal" data-target="#myModal'+i+'"> <img'+
            ' src='+imagesjson[i].link+
            ' class="img-fluid">'+
        '</a>';

        var divmodalfade = document.createElement('div');
        divmodalfade.id = 'myModal' + i; 
        divmodalfade.setAttribute('class','modal fade');
        divmodalfade.setAttribute('myModal','-1');
        divmodalfade.setAttribute('role','dialog');
        divmodalfade.setAttribute('aria-labelledby','myModalLabel');
        divmodalfade.setAttribute('aria-hidden','true');
        document.getElementById(colmd3.id).appendChild(divmodalfade);

        var divmodalimg = document.createElement('div');
        divmodalimg.id = 'divmodalimg'+i;
        divmodalimg.setAttribute('class','modal img-modal');
        document.getElementById(divmodalfade.id).appendChild(divmodalimg);

        var divmodaldialog = document.createElement('div');
        divmodaldialog.id = divmodaldialog+i;
        divmodaldialog.setAttribute('class','modal-dialog modal-lg');
        document.getElementById(divmodalimg.id).appendChild(divmodaldialog);

        var divmodalcontent = document.createElement('div');
        divmodalcontent.id='divmodalcontent'+i;
        divmodalcontent.setAttribute('class','modal-content');
        document.getElementById(divmodaldialog.id).appendChild(divmodalcontent);

        var divmodalbody = document.createElement('div');
        divmodalbody.id='divmodalbody'+i;
        divmodalbody.setAttribute('class','modal-body');
        document.getElementById(divmodalcontent.id).appendChild(divmodalbody);

        var divmodalrow = document.createElement('div');
        divmodalrow.id = 'divmodalrow'+i;
        divmodalrow.setAttribute('class','row');
        document.getElementById(divmodalbody.id).appendChild(divmodalrow);

        var objdivmodalrow = document.getElementById(divmodalrow.id);

        objdivmodalrow.innerHTML = 
        '<div class="col-md-8 modal-image">'+
            '<img class="img-responsive "'+
            'src='+imagesjson[i].link+'>'+
        '</div>';

        var colmd4 = document.createElement('div');
        colmd4.setAttribute('class','col-md-4 modal-meta');
        colmd4.id = 'colmd4'+i;        
        objdivmodalrow.appendChild(colmd4);

        var objcolmd4 = document.getElementById(colmd4.id);

        var modalmetatop = document.createElement('div');
        modalmetatop.id='modalmetatop'+i;
        modalmetatop.setAttribute('class','modal-meta-top');
        objcolmd4.appendChild(modalmetatop);

        var objmodalmetatop = document.getElementById(modalmetatop.id);

        objmodalmetatop.innerHTML = 
        '<button type="button" class="close" data-dismiss="modal">'+
            '<span aria-hidden="true">×</span>'+
        '</button>'+
        '<div class="img-poster clearfix">'+
            '<a href=""><img class="img-circle"'+
            'src="images/avatar.png"'+ /*imagesjson[i].avatar+*/'/></a>'+
            '<strong><a href="">'+imagesjson[i].username+'</a></strong>'+
        '</div>';

        var ulcommentlist = document.createElement('ul');
        ulcommentlist.setAttribute('class','img-comment-list');
        ulcommentlist.id = 'ulcommentlist'+i;        
        objmodalmetatop.appendChild(ulcommentlist);

        var objulcommentlist = document.getElementById(ulcommentlist.id);

        for (let j = 0; j < imagesjson[i].comments.length; j++) {
            objulcommentlist.innerHTML +=      
            '<li>'+
                '<div class="comment-img">'+
                    '<img src="images/avatar.png"'+ /*imagesjson[i].comments[j].avatar+*/'/>'+
                '</div>'+
                '<div class="comment-text">'+
                    '<strong><a href="">'+ imagesjson[i].comments[j].author +'</a></strong>'+
                    '<p>' + imagesjson[i].comments[j].comment +'</p>'+                                                   
                '</div>'+
            '</li>';   
        }
              
        var modalmetabottom = document.createElement('div');
        modalmetabottom.id='modalmetabottom'+i;
        modalmetabottom.setAttribute('class','modal-meta-bottom');
        document.getElementById(colmd4.id).appendChild(modalmetabottom);

        var inputgroup = document.createElement('div');
        inputgroup.id='inputgroup'+i;
        inputgroup.setAttribute('class','input-group input-group-sm chatMessageControls');
        document.getElementById(modalmetabottom.id).appendChild(inputgroup);

        var inputcomment = document.createElement('input');
        inputcomment.id='inputcomment'+i;
        inputcomment.setAttribute('class','form-control');
        inputcomment.setAttribute('type','text');
        inputcomment.setAttribute('placeholder','Leave a commment...');
        inputcomment.setAttribute('aria-describedby','sizing-addon3');
        document.getElementById(inputgroup.id).appendChild(inputcomment);

        var inputgroupbtn = document.createElement('span');
        inputgroupbtn.id='inputgroupbtn'+i;
        inputgroupbtn.setAttribute('class','input-group-btn');
        document.getElementById(inputgroup.id).appendChild(inputgroupbtn);

        var btnsendcomment = document.createElement('button');
        btnsendcomment.id='btnsendcomment'+i;
        btnsendcomment.setAttribute('class','btn btn-primary');
        btnsendcomment.setAttribute('type','button');
  /*      btnsendcomment.addEventListener('click', function(event){
        	console.log("button clicked : ");
        	console.log("input comment : ");
        	console.log(inputcomment.value);
        	console.log(inputcomment.id);
        	console.log(" XXX ");
        	console.log(imagesjson[i]);
        	console.log(" ---");
        	console.log(imagesjson[i].id);
            addComment(sessionkey,inputcomment.id,imagesjson.id);
            event.preventDefault();
        });
        */
        console.log(inputcomment.id);
        btnsendcomment.addEventListener('click', function(event) {
        	console.log(inputcomment.value);
            var sendButton = event.target;
            var inputText = sendButton.parentNode.parentNode.firstChild;
            console.log(inputText.value);
            addComment(sessionkey, inputText.value, imagesjson[i].id);
            var lastCommentData = localStorage.getItem("lastCommentInfo");
        //    var lastCommentDataArray = lastCommentData.split(''+STRINGIFY_SEPARATOR);
            
            var newLi = document.createElement('li');
            
            var newImgDiv = document.createElement('div');
            newImgDiv.className = "comment-img";
            var newAvatar = document.createElement('img');
            newAvatar.src="images/avatar.png";
            
            newImgDiv.append(newAvatar);
            
            var newCommentTextDiv = document.createElement('div');
            newCommentTextDiv.className = "comment-text";
            
            var newStrong = document.createElement('strong');
            var newA = document.createElement('a');
            $(newA).text(localStorage.getItem("username"));
            newStrong.append(newA);
            
            var newP = document.createElement('p');
            $(newP).text(inputText.value);
            
            newCommentTextDiv.append(newStrong);
            newCommentTextDiv.append(newP);
            
            newLi.append(newImgDiv);
            newLi.append(newCommentTextDiv);
/*            
    		var newCommentHTML = '<li>'+
                    '<div class="comment-img">'+
                        '<img src="images/avatar.png"'+ /*imagesjson[i].comments[j].avatar+*//*'/>'+
                    '</div>'+
                    '<div class="comment-text">'+
                        '<strong><a href="">'+ lastCommentDataArray[0] +'</a></strong>'+
                        '<p>' + lastCommentDataArray[1] +'</p>'+                                                   
                    '</div>'+
                '</li>';
                        */
    		sendButton.parentNode.parentNode.parentNode.parentNode.firstChild.lastChild.append(newLi);
    	}, false);
        //btnsendcomment.setAttribute('onclick',addComment(sessionkey,inputcomment.id,imagesjson[i].id));
        document.getElementById(inputgroupbtn.id).appendChild(btnsendcomment);

        btnsendcomment.innerHTML = '<i class="fa fa-send"></i>Send';
    }
    return;
}


/*
 * THUMBNAILS
 */

window.onload = function() {
	mypagecontent.initialize();
	mypagecontent.initializeLeftMenu();
}
