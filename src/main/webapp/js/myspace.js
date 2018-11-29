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
		
		console.log("images.size() : " + images.length);

		for (var i = 0; i < images.length; i++) {
			let imageMosaic = images[i];
			console.log(imageMosaic);

			let divider = document.createElement('div');
			divider.className = "ui horizontal divider";
			gallery.append(divider);


			var cardView = $(imageMosaic.cardView);
			cardView.find('.mosaic-card').click(function () {
				$('body').append($(imageMosaic.modalView));
				$(imageMosaic.modalView).modal('show');
				$('body').find('.modal').find('.submit').click(function() {
					console.log("button clicked : " + $(this).parent().find('textarea').val());
				});
			});

            gallery.append(cardView);
		}
	}
	/*,
	setCardClickEvent : function(image) {
		card.find('a.mosaic-card').click(function(event) {
			var modal = $(
				'<div class="ui modal">' +
					'<i class="close icon"></i>' +
					'<div class="header">' +
						image.originalFilename +
					'</div>' +
					'<div class="image content">' +
						'<div class="image">' +
							'<img src="' + image.link + '" />' +
						'</div>' +
						'<div class="column">' +
							'<div class="ui comments">' +
								'<h3 class="ui dividing header">Comments</h3>' +
								'<div class="comment">' +
							'</div>' +
						'</div>' +
					'</div>' +
					'<div class="actions">' +
						'<div class="ui button">' + "Cancel" + '</div>' +
						'<div class="ui button">' + "OK" + '</div>' +
					'</div>' +
				'</div>'
				);

			$('body').append(modal);

			modal.modal('show');
		});
	},

	setMosaicCardClickEvent : function() {
		$('.mosaic-card').click(function(event) {
//			event.target.id

			var modal = $(
				'<div class="ui modal">' +
					'<i class="close icon"></i>' +
					'<div class="header">' +
						event.target.innerHTML +
					'</div>' +
					'<div class="image content">' +
						'<div class="image">' +
							"An image can appear on left or an icon" +
						'</div>' +
						'<div class="description">' +
							"A description can appear on the right" +
						'</div>' +
					'</div>' +
					'<div class="actions">' +
						'<div class="ui button">' + "Cancel" + '</div>' +
						'<div class="ui button">' + "OK" + '</div>' +
					'</div>' +
				'</div>'
				);

			$('body').append(modal);

			modal.modal('show');

		});
	}*/
};


window.onload = function() {
	mySpaceContent.initialize();
	mySpaceContent.initializeUserImagesGallery();
	//mySpaceContent.setMosaicCardClickEvent();
}