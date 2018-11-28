function Comment(id, img, author, text, date) {
	this.id = id;
	this.img = img;
	this.author = author;
	this.content = text;
	this.date = date;
	this.commentView = "";

	generateCommentView(this);
}

function generateCommentView(comment) {
	comment.commentView = 
		'<div class="comment">' +
    		'<a class="avatar">' +
				'<img src="/images/avatar/small/matt.jpg">' +
			'</a>' +
			'<div class="content">' +
				'<a class="author">' + comment.author + '</a>' +
				'<div class="metadata">' +
					'<span class="date">' + "Today at 5:42PM" + '</span>' +
				'</div>' +
				'<div class="text">' + comment.content + '</div>' +
				'<div class="actions">' +
					'<a class="reply">Reply</a>' +
				'</div>' +
			'</div>' +
		'</div>';
}

