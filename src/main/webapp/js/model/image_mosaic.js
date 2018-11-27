function ImageMosaic(id, link, originalFilename, keyword, creationDate, author, allComments) {
	console.log("...ImageMosaic");
	this.id = id;
	this.link = link;
	this.originalFilename = originalFilename;
	this.keyword = keyword;
	this.creationDate = creationDate;
	this.author = author;
	this.comments = initializeComments(allComments);
	console.log(this.comments);
	this.cardView = generateCardView(this);
	console.log(this.cardView);
	this.modalView = generateModalView(this);
	console.log(this.modalView);
}

function initializeComments (allComments) {
	var comments = [];
	if(!(allComments == null || typeof allComments == "undefined" || allComments.length == 0)) {
		for (var i = 0; i < allComments.length; i++) {
			var commentItem = allComments[i];
			var comment = new Comment(commentItem.id, commentItem.image, commentItem.auteur, commentItem.text, commentItem.date);
			comments.push(comment);
		}
	}
	return comments;
}

function generateCardView (image) {
	console.log("generateCardView");
	return 
		'<div class="column">' +
			'<div class="ui centered fluid card">' +
				'<div class="image">' +
					'<img src="' + image.link + '" />' +
				'</div>' +
				'<div class="content">' +
					'<a class="header mosaic-card" id="' + image.id + '">' + image.originalFilename + '</a>' +
					'<div class="meta">' +
						'<span class="date">' + image.creationDate +'</span>' +
					'</div>' +
					'<div class="description">Keyword used : ' + image.keyword + '</div>' +
				'</div>'+
				'<div class="extra content">' +
					'<span class="left floated">' + 
						'<i class="comments outline icon">'+ '</i>' +
						((image.comments == null || typeof image.comments == "undefined") ? 0 : image.comments.length) +' comments'+
					'</span>' +
				'</div>' +
			'</div>'+
		'</div>';
}

function generateModalView (image) {
	console.log("generateModalView");
	var commentsView = "";
	if(!(image.comments == null || typeof image.comments == "undefined" || image.comments.length == 0)) {
		for (var i = 0; i < image.comments.length; i++) {
			commentView = image.comments[i].commentView;
			commentsView += commentView;
		}
	}
	return
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
					'<div class="ui comments comments-view">' +
						'<h3 class="ui dividing header">Comments</h3>' +
						commentsView +
					'</div>' +
				'</div>' +
			'</div>' +
			'<div class="actions">' +
				'<div class="ui button">' + "Cancel" + '</div>' +
				'<div class="ui button">' + "OK" + '</div>' +
			'</div>' +
		'</div>';
}

	
/*
	this.generateMosaicModalView = function (image) {
		if(this.comments == null || typeof this.comments == "undefined")
			return;


		var modalView = 
		for (var i = 0; i < this.comments.length; i++) {
			let c = comments[i];
			var comment = new Comment(c.id, c.img.id, c.auteur, c.text, c.date);
			rslt.push(comment);
		}
		return rslt;
	};*/ 
