var username = localStorage.getItem("username");
var sessionkey = localStorage.getItem("sessionKey");

var fileUploaded = false;
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
				displayUploadButton();
				fileUploaded = true;
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
				$("#dragNdropButton").click(function() {
					console.log("button clicked");
					console.log(dragNdropForm);
					console.log("---------------------");
					console.log(sessionkey);
					var str = document.getElementById('dragNdropInput').value;
					console.log("test str");
					console.log(str);
					
//					ServerServices.uploadData(dragNdropForm.userKeyword.value, fileList[0]);
					//ServerServices.asyncUploadData(dragNdropForm, sessionkey);
					ServerServices.uploadData(dragNdropForm, sessionkey);
					return false;
				});
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
			// submitEvent.preventDefault();
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
		document.getElementById("dragNdropInput").setAttribute("display", "none");
		var attributes = dragNdropForm.prop("attributes");
		$.each(attributes, function() {
			console.log(this.name + ": " + this.value);
			img.setAttribute(this.name, this.Value);
		});		
		img.id = "dragNdropImg";
		img.src = reader.result;
		img.classList = "ui middle aligned centered medium image";
	};
	reader.readAsDataURL(imgFile);
	
	var droppedZone = document.createElement("div");
	droppedZone.id = "droppedZone";
	droppedZone.classList = "ui container";
	droppedZone.append(img);
	dropZone.hide();
	$(document.getElementById("dropZoneParent")).append(droppedZone);
	return;
	
	/*
	reader.onloadend = function() {
		var imgHeight;
		var imgWidth;
		var image = new Image;
		image.onload = function() {
			imgWidth = this.width;
			imgHeight = this.height;
			
			var frameHeight = document.getElementById("dropZone").offsetHeight;
			var frameWidth = document.getElementById("dropZone").offsetWidth;
			console.log("imgHeight : " + imgHeight + ", frameHeight : " + frameHeight + ", k : " + (imgHeight/frameHeight));
			console.log("imgWidth : " + imgWidth + ", frameWidth : " + frameWidth + ", k : " + (imgWidth/frameWidth));
			
			if((imgHeight/frameHeight) > (imgWidth/frameWidth)) {
				k = imgHeight/frameHeight;
				
				img.setAttribute("height", frameHeight);
				img.setAttribute("width", (imgWidth/k));

				var frame_marginLeft = getStyleRuleValue('margin-left', "#dropZone");
				var frame_ml_value = parseInt(frame_marginLeft.substring(0, frame_marginLeft.length-2));
				var margin_left = (((frameWidth - (imgWidth/k))/2) + frame_ml_value);
				img.style.marginLeft = margin_left+"px";
			}
			else {
				k = imgWidth/frameWidth;
				
				img.setAttribute("width", frameWidth);
				img.setAttribute("height", imgHeight/k);
				
				// frameHeight - (imgHeight/k)/2
				
				var frame_marginTop = getStyleRuleValue('margin-top', "#dropZone");
				var frame_mt_value = parseInt(frame_marginTop.substring(0, frame_marginTop.length-2));
				var margin_top = (((frameHeight - (imgHeight/k))/2));
				img.style.marginLeft = getStyleRuleValue('margin-left', "#dropZone");
				img.style.marginTop = margin_top+"px";
			}
			console.log("test");
			console.log(img);
		};
		image.src = reader.result;
		// Load the image instead of the upload area
		//$(document.getElementById("dragNdropForm").removeChild(document.getElementById("dragNdropInput")));
		//$(document.getElementById("dragNdropForm").removeChild(document.getElementsByTagName("P")[0]));
		document.getElementById("dragNdropInput").setAttribute("display", "none");
		var attributes = dragNdropForm.prop("attributes");
		$.each(attributes, function() {
			console.log(this.name + ": " + this.value);
			img.setAttribute(this.name, this.Value);
		});		
		img.id = "dragNdropImg";
		img.src = reader.result;
	}
	reader.readAsDataURL(imgFile);
	dragNdropForm.append(img);
	return;
	*/
}


function displayUploadButton() {
	console.log("displaying upload button...");
/*	var button = document.createElement("div");
	button.id = "dragNdropButton";
	button.textContent = "Upload";
	var dropZone = $(document.getElementById("dropZone"));
	dropZone.append(button);
*/
	var button = document.createElement("button");
	button.id = "dragNdropButton";
	button.form = "dragNdropForm";
	button.value = "Upload";
	button.type = "submit";
	$(document.getElementById("dropZone")).append(button);
	return;
}

function getStyleRuleValue(style, selector, sheet) {
    var sheets = typeof sheet !== 'undefined' ? [sheet] : document.styleSheets;
    for (var i = 0, l = sheets.length; i < l; i++) {
        var sheet = sheets[i];
        if( !sheet.cssRules ) { continue; }
        for (var j = 0, k = sheet.cssRules.length; j < k; j++) {
            var rule = sheet.cssRules[j];
            if (rule.selectorText && rule.selectorText.split(',').indexOf(selector) !== -1) {
                return rule.style[style];
            }
        }
    }
    return null;
}
