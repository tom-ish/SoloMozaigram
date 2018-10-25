var sessionkey = localStorage.getItem("sessionKey");

var mymozaiksjson = ServerServices.myMozaikThumbnails(sessionkey);

var mypagecontent = {
		initialize : function() {
			
		}
		
};

function addComment(buttonid, sessionkey, commentid, imgid){   
    console.log("Comment button clicked");
    console.log("---------------------");
    console.log(sessionkey);
    ServerServices.addComment(sessionkey, document.getElementById(commentid).value, imgid);
    return;
}




window.onload = function() {
	mypagecontent.initialize();
}

