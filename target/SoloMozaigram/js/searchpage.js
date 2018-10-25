

var searchpagecontent = {
		initialize : function() {
			$(document).ready(function(){
				$("#launchSearchButton").click(function() {
					console.log("button clicked");
					console.log(SearchForm);
					console.log("---------------------");
					console.log(sessionkey);
//					ServerServices.uploadData(dragNdropForm.userKeyword.value, fileList[0]);
					ServerServices.getSearchResults(document.getElementById("searchWord").value);
					return false;
				});

			});

		}

};



function displayResults(searchkey){
	console.log("Search results for "+searchkey)
	var html = "Resultat de recherche pour "+searchkey;
	var test = document.createElement('h1');

	test.className="test";
	test.innerHTML = html;

	document.getElementById("usernameId").value = test;

	flushAccessArea();

	/*
	 * Creation des elements qui vont contenir la liste de resultat
	 */
	var accessAreaDiv = document.getElementById("AccessArea");
	accessAreaDiv.className="jp-type-playlist";
	var searchRsltDiv = document.createElement('div');
	searchRsltDiv.id = "searchRslt";
	searchRsltDiv.className="jp-playlist";
	$(accessAreaDiv).append(searchRsltDiv);
	var rsltList = document.createElement('ul');
	rsltList.id="rsltList";
	rsltList.style="display: block;";
	$(searchRsltDiv).append(rsltList);

	var List=localStorage.getItem("listResearch");
	console.log("Search's results are: "+List);


	var res=List.split(",");


	for (var i = 0; i < res.length; i++) {
		if(res[i].charAt(0)=='[' || res[i].charAt(0)==' '){
			res[i]=res[i].substr(1,res[i].length);
		}
		if (res[i].charAt(res[i].length-1)==']' || res[i].charAt(res[i].length-1)==' '){
			res[i]=res[i].substr(0,res[i].length-1);
		}
		displayAccessButton(res[i], i);
	}
	var listImg=localStorage.getItem("listImg");

	document.getElementById("libsmall").title="Mosaïques de "+username;
	var l=listImg.split(",");


	for (var i = 0; i < l.length; i++) {
		if(l[i].charAt(0)=='[' || l[i].charAt(0)==' '){
			l[i]=l[i].substr(1,l[i].length);
		}
		if (l[i].charAt(l[i].length-1)==']' || l[i].charAt(l[i].length-1)==' '){
			l[i]=l[i].substr(0,l[i].length-1);
		}
		displayImage(l[i],i);
	}
	localStorage.setItem("listImg",null);
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


}


function displayAccessButton(name, i) {
	console.log("displaying access button for "+name+"'s page");

	/*
	var button = document.createElement("button");
	button.id = name+"Button";
	button.form = "AccessForm";
	button.value = name;
	button.textContent=name;
	button.type = "submit";
	button.style.height="50px";
	button.style.width="200px";

	button.addEventListener('click', accessClick,false);

	$(document.getElementById("rsltList")).append(button);
	 */


	var li = document.createElement('li');
	li.className = "jp-playlist-current";
	var innerDiv = document.createElement('div');
	innerDiv.className = "listDiv"
	innerDiv.addEventListener('click', function(e) {
		accessClick(e);
	}, false);
	
	var innerA1Element = document.createElement('a');
	innerA1Element.href = "javascript:;";
	innerA1Element.className = "jp-playlist-item jp-playlist-current";
	innerA1Element.tabindex = i;
	$(innerA1Element).text(name);

	var addImg = document.createElement('img');
	addImg.className = "addFriend";
	addImg.setAttribute('src', 'images/add.png');
	addImg.addEventListener('click', function(e) {
		addFriendClick(e);
	}, false);

	$(innerDiv).append(innerA1Element);
	$(li).append(innerDiv);
	$(li).append(addImg);

	$(document.getElementById("rsltList")).append(li);
	console.log("index : " + i);
	return;
}





function flushAccessArea() {
	while (document.getElementById("AccessArea").firstChild) {
		document.getElementById("AccessArea").removeChild(document.getElementById("AccessArea").firstChild);
	}
}

function accessClick(e) {
	var a = e.target;
	console.log("Going to "+$(a).text()+"'s page");
	localStorage.setItem("requestedpage", $(a).text());
	localStorage.setItem("searchMode", "true");
	window.location.href = "./myspace.html";
}

function addFriendClick(e) {
	var add = e.target;
	var nameToAdd = $(add.parentNode.firstChild.firstChild).text();
	console.log("attempting to add ");
	if(confirm("Souhaitez-vous envoyer une demande d'ami ?") == true) {
		ServerServices.sendFriendRequest(localStorage.getItem("sessionKey"),nameToAdd);
	}
}

window.onload = function() {
	searchpagecontent.initialize();
	leftMenu.initialize();
}
