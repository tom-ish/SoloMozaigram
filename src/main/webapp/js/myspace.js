var mySpaceContent = {
	initialize : function(){
		$('a.sidebar-toggle').click(function() {
			$('#sidebar')
				.sidebar('setting', 'transition', 'overlay')
				.sidebar('toggle');
		});

		$('a.gallery-toggle').click(function() {
			$('#gallery_area')
				.sidebar('setting', 'transition', 'overlay')
				.sidebar('toggle');
		})
		
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
		var gallery = $('#user_images_gallery');


		for(i in images) {
			console.log(images[i]);
			let image = images[i];


			let divider = document.createElement('div');
			divider.className = "ui horizontal divider";
			gallery.append(divider);

			var card = $(
				'<div class="column">' +
					'<div class="ui centered fluid card">' +
						'<div class="image">' +
							'<img id="' + image.id + '" src="' + image.link + '" />' +
						'</div>' +
						'<div class="content">' +
							'<a class="header">' + image.keyword + '</a>' +
							'<div class="meta">' +
								'<span class="date">' + image.creationDate + ' Filter' +'</span>' +
							'</div>' +
							'<div class="description">' + image.originalFilename + '</div>' +
						'</div>'+
						'<div class="extra content">' +
							'<span class="left floated">' + 
								'<i class="comments outline icon">'+ '</i>' +
								((typeof image.comments == null || typeof image.comments == "undefined") ? 0 : image.comments.count) +' comments'+
							'</span>' +
						'</div>' +
					'</div>'+
				'</div>'
            );

            gallery.append(card);

			/*
			var card = document.createElement('div'),
				imageDiv = document.createElement('div'),
				img = document.createElement('img'),
				cardDesc = document.createElement('div'),
				aHeader = document.createElement('a'),
				metaDiv = document.createElement('div'),
				dateSpan = document.createElement('span'),
				extraContentDiv = document.createElement('div'),
				commentSpan = document.createElement('span');

			card.className = "ui centered fluid card";
			imageDiv.className = "image";
			cardDesc.className = "content";
			aHeader.className = "header";
			metaDiv.className = "meta";
			dateSpan.className = "date";
			extraContentDiv.className = "extra content";
			commentSpan.className = "left floated";


			img.id = image.id;
			img.src = image.link;
			aHeader.innerHTML = image.creationDate;

			imageDiv.appendChild(img);
			cardDesc.appendChild(aHeader);
			card.appendChild(imageDiv);
			card.appendChild(cardDesc);

			gallery.append(card);
			
*/
/*
			var aImg = document.createElement('a');
			aImg.className = "item";

			var img = document.createElement('img');
			img.id = image.id;
			img.src = image.link;
			aImg.appendChild(img);
			
			let headerDiv = document.createElement('div');
			headerDiv.className = "ui horizontal divider";
			gallery.append(headerDiv);
			gallery.append(aImg);
			*/
		}
	}
};


window.onload = function() {
	mySpaceContent.initialize();
	mySpaceContent.initializeUserImagesGallery();
}