var username = localStorage.getItem("username");
var sessionkey = localStorage.getItem("sessionKey");

var fileUploaded = false;
var userKeywordMissing = true;
var userKeyword;

$(document).ready(function(){
	$('#dragNdropInput').change(function () {
		var fileList = this.files;
		console.log("this.files : ");
		console.log(fileList);
		if(!fileUploaded) {			
			if(checkFileExtension(fileList[0].name)) {
				//$('#dragNdropForm p').text(this.files.length + " file(s) selected");
				//var image = adaptImageDimensions(this.files[0]);
				loadImage(fileList[0]);
				fileUploaded = true;
				$('#reset-button').removeClass("disabled");
				
				console.log("userKeyword: " + userKeyword);
				if(!userKeywordMissing) {
					$('#generate-button').removeClass("disabled");					
				}
				/*
			$("#dragNdropButton").click(function(){
				console.log("button clicked");
				var keyword = document.getElementById("userKeyword").value;
				var image = document.getElementById("dragNdropImg");
				//console.log(image.getAttribute("src"));
				checkDataUploadValidity(keyword, image);
				ServerServices.uploadData(keyword, image);
				return false;
			});
				 */
			}
			else {
				$('#dragNdropForm p').text("Please check your file extension... Accepting only images !");
				submitEvent.preventDefault();
			}
		}
		else {
			console.log("already uploaded file...");
			console.log(this.files.length);
		}
	});
});


function checkFileExtension(filename) {
	// regular expression to trim everything before final dot
	var extension = filename.replace(/^.*\./, '');
	
	// case for filename without dot <=> extension == filename
	if(extension == filename) 
		extension = '';
	// if extension exists, convert it to lower case
	else
		extension = extension.toLowerCase();
	
	switch (extension) {
		case 'jpg':
		case 'jpeg':
		case 'png':
			return true;
			
		default:
			// Cancel the form submission
			submitEvent.preventDefault();
			return false;
	}
	return false;
}

function checkDataUploadValidity(keyword, image) {
//	if(keyword )
	return;
}

function loadImage(imgFile) {
	var img = document.createElement("img"),
		reader = new FileReader(),
		dropZone = $(document.getElementById("dropZone")),
		dragNdropForm = $(document.getElementById("dragNdropForm"));
	
	reader.onloadend = function() {
		var image = new Image;
		image.src = reader.result;
		$("#dragNdropInput").hide();

		img.id = "dragNdropImg";
		img.src = reader.result;
		img.classList = "ui centered medium image";
	};
	reader.readAsDataURL(imgFile);
	
	var droppedZone = document.createElement("div");
	droppedZone.id = "droppedZone";
	droppedZone.classList = "ui container";
	droppedZone.append(img);
	
	var droppedImgPath = $('#droppedImgPath');
	droppedImgPath.html(""+imgFile.name);
	droppedImgPath.show();

	dropZone.hide();
	
	$("#dropZoneParent").append(droppedZone);
	return;
}
