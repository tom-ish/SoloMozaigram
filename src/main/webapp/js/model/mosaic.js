function Mosaic(id, link, originalFilename, keyword, creationDate, author, comments) {
	this.id = id;
	this.link = link;
	this.originalFilename = originalFilename;
	this.keyword = keyword;
	this.creationDate = creationDate;
	this.author = author;
	this.comments = comments;

	this.cardView = generateCardView();
}

function generateCardView  () {
	return '<div class="column">' +
			'<div class="ui centered fluid card">' +
				'<div class="image">' +
					'<img src="' + this.link + '" />' +
				'</div>' +
				'<div class="content">' +
					'<a class="header mosaic-card" id="' + this.id + '">' + this.originalFilename + '</a>' +
					'<div class="meta">' +
						'<span class="date">' + this.creationDate +'</span>' +
					'</div>' +
					'<div class="description">Keyword used : ' + this.keyword + '</div>' +
				'</div>'+
				'<div class="extra content">' +
					'<span class="left floated">' + 
						'<i class="comments outline icon">'+ '</i>' +
						((this.comments == null || typeof this.comments == "undefined") ? 0 : this.comments.length) +' comments'+
					'</span>' +
				'</div>' +
			'</div>'+
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
