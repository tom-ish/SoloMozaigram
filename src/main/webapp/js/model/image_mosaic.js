function ImageMosaic(id, link, originalFilename, keyword, creationDate, author, allComments) {
	console.log("...ImageMosaic");
	this.id = id;
	this.link = link;
	this.originalFilename = originalFilename;
	this.keyword = keyword;
	this.creationDate = creationDate;
	this.author = author;
	this.comments = initializeComments(allComments);
	this.cardView = "";
	this.modalView = "";

	generateCardView(this);
	generateModalView(this);
	setCommentValidationRules(this);
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
	image.cardView = 
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
					'<div class="ui label">' + image.keyword + '</div>' +
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
	var commentsView = "";
	if(!(image.comments == null || typeof image.comments == "undefined" || image.comments.length == 0)) {
		for (var i = 0; i < image.comments.length; i++) {
			commentView = image.comments[i].commentView;
			commentsView += commentView;
		}
	}
	image.modalView =
		'<div class="ui modal">' +
			'<i class="close icon"></i>' +
			'<div class="ui segment">' +
				'<div class="header">' +
					'<div class="ui label">' +
						'Keyword' +
						'<div class="detail">' + image.keyword + '</div>' +
					'</div>' +
					'<div class="ui label">' +
						'Filename' +
						'<div class="detail">' + image.originalFilename + '</div>' +
					'</div>' +
				'</div>' +
			'</div>' +
			'<div class="image">' +
				'<img class="ui fluid bordered rounded image" src="' + image.link + '" />' +
			'</div>' +
			'<div class="ui container">' +
				'<div class="row">' +/*
					'<div class="ui comments comments-view">' +*/
						'<h3 class="ui horizontal divider">Comments</h3>' +
						commentsView +
/*					'</div>' +*/
				'</div>' +
				'<div class="row">' +
					'<form class="ui reply form" id="commentForm_' + image.id + '">' +
						'<div class="field">' +
							'<textarea rows="3" name="comment_content" id="commentContent_' + image.id + '"></textarea>' +
						'</div>' +
						'<div class="ui blue right floated labeled submit icon button">' +
							'<i class="icon edit"></i>Leave a comment' +
						'</div>' +
					'</form>' +
				'</div>' +
			'</div>' +
		'</div>';
}

function setCommentValidationRules(image) {
	var commentValidationRules = {
		inline : true,
		delay : false,
		on : 'submit',
		fields : {
			commentContent : {
				identifier : 'comment_content',
				rules : [
					{
						type : 'empty',
						prompt : 'Your comment\'s content is empty'
					}
				]
			}
		},
		onSuccess : function(event, fields) {
			event.preventDefault();
			event.stopImmediatePropagation();
			console.log("comment content form validation success");

			console.log(image);
			console.log(fields.commentContent);
			console.log(event.target.id);
			//login();
		}
	};

	$('#commentForm_'+image.id).form(commentValidationRules);
}

/*

var createUser = function() {
		var formulaire = $('#signupForm');
		
		username = formulaire.find('#signupUsernameInput').val();
		email = formulaire.find('#signupEmailInput').val();
		pwd = formulaire.find('#signupPasswordInput').val();
		pwd2 = formulaire.find('#signupConfirmPasswordInput').val();
		
		console.log("input values : " + username + ", " + pwd + ", " + pwd2 + ", " + email);
		$('#signupButton').addClass('loading disabled');

		ServerServices.signup(username, pwd, pwd2, email);
	};

	*/