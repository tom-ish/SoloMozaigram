function Mosaic(id, link, originalFilename, keyword, creationDate, author, comments) {

	var self = {};

	self.id = id;
	self.link = link;
	self.originalFilename = originalFilename;
	self.keyword = keyword;
	self.creationDate = creationDate;
	self.author = author;
	self.comments = comments;


	self.generateCardView = function() {
		return $(
			'<div class="column">' +
				'<div class="ui centered fluid card">' +
					'<div class="image">' +
						'<img src="' + self.link + '" />' +
					'</div>' +
					'<div class="content">' +
						'<a class="header mosaic-card" id="' + self.id + '">' + self.originalFilename + '</a>' +
						'<div class="meta">' +
							'<span class="date">' + self.creationDate +'</span>' +
						'</div>' +
						'<div class="description">Keyword used : ' + self.keyword + '</div>' +
					'</div>'+
					'<div class="extra content">' +
						'<span class="left floated">' + 
							'<i class="comments outline icon">'+ '</i>' +
							((self.comments == null || typeof self.comments == "undefined") ? 0 : self.comments.length) +' comments'+
						'</span>' +
					'</div>' +
				'</div>'+
			'</div>'
			);
	}

	return self;
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
