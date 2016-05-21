String.prototype.format = function() {
    var formatted = this;
    for( var arg in arguments ) {
        formatted = formatted.replace("{" + arg + "}", arguments[arg]);
    }
    return formatted;
};

String.prototype.replaceAll = function(search, replacement) {
    var target = this;
    return target.split(search).join(replacement);
};


var newsTemplate = "<div class='news'>\
                        <div class='title'>\
                        {0}\
                        </div>\
                        <div class='data-and-authors-row'>\
                            <div class='news-authors'>\
                            {1}\
                            </div>\
                            <div class='news-last-edit'>\
                                <u>\
                                {2}\
                                </u>\
                            </div>\
                        </div>\
                        <div class='news-text'>\
                        {3}\
                        </div>\
                        <div id='comments'>\
                        {4}\
                        </div>\
                    </div>";
var commentTemplate = "<div class='comment'>\
                            <div class='comment-date'>\
                                <u>\
                                {0}\
                                </u>\
                            </div>\
                            <div class='comment-text-wrapper'>\
                                <span class='delete-comment-button-span'><button id='{1}' class='delete-comment-button'>&times;</button></span>\
                                <div class='comment-text'>\
                                {2}\
                                </div>\
                            </div>\
                        </div>";


var processing = false;


var header = $("meta[name='_csrf_header']").attr("content");
var token = $("meta[name='_csrf']").attr("content");


function refreshNews(newsInfo) {
    $('#previous').removeClass('disabled-page-link');
    $('#next').removeClass('disabled-page-link');
    if (newsInfo.first)
        $('#previous').addClass('disabled-page-link');
    if (newsInfo.last)
        $('#next').addClass('disabled-page-link');

    var news = newsInfo.news;
    var authors = newsInfo.authors;
    var authorsString = '(by {0})'.format(authors
        .map(function(author) { return author.authorName; }).join(', '));
    var lastEditDate = news.modificationDate ? news.modificationDate : news.creationDate;
    var lastEditDate = new Date(lastEditDate);
    var localeCode = 'En';
    lastEditDate = lastEditDate.toLocaleString(localeCode, {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
    });

    var comments = newsInfo.comments;
    var commentsDiv = '';
    for (var i = 0; i < comments.length; i++) {
        var comment = comments[i];
        var date = new Date(comment.creationDate);
        date = date.toLocaleString(localeCode, {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
        });
        var newCommentRow = commentTemplate.format(date, comment.commentId, comment.commentText.replaceAll(/[^\w\s]/gi, ''));
        commentsDiv += newCommentRow;
    }

    var newNewsRow = newsTemplate.format(news.title.replaceAll(/[^\w\s]/gi, ''),
        authorsString, lastEditDate, news.fullText.replaceAll('\\n', '<br>'), commentsDiv);

    $("#content").animate({ scrollTop: 0 }, "slow", function() {
        $('.news').toggle("slide", 500, function() {
            $(this).remove();
            $(newNewsRow).prependTo($('#content')).slideDown('slow');
        });
    });

    processing = false;
}


$(document).on('click', '.delete-comment-button', function (event) {
    if (processing)
        return;
    processing = true;

    var commentId = event.target.id;

    $.ajax({
        url: '/news-management/view-news/delete',
        type: 'POST',
        data: commentId,
        contentType: "application/json",
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success: function() {
            var comment = $(event.target).parent().parent().parent();
            $(comment).toggle("slide", 500, function() { $(this).remove(); });
            processing = false;
        }
    });
});


$(document).on('click', '#previous', function () {
    if ($(this).hasClass('disabled-page-link') || processing)
        return;

    processing = true;

    $('#new-post-textarea').removeClass('invalid-comment-text');

    $.ajax({
        url: '/news-management/view-news/previous',
        type: 'GET',
        success: function(newsInfo) {
            refreshNews(newsInfo);
        }
    });
});


$(document).on('click', '#next', function () {
    if ($(this).hasClass('disabled-page-link') || processing)
        return;

    processing = true;

    $('#new-post-textarea').removeClass('invalid-comment-text');

    $.ajax({
        url: '/news-management/view-news/next',
        type: 'GET',
        success: function(newsInfo) {
            refreshNews(newsInfo);
        }
    });
});


$(document).on('click', '#post-comment-button', function () {
    if (processing)
        return;

    processing = true;

    var commentText = $('#new-post-textarea').val();
    if (!commentText || commentText == '') {
        $('#new-post-textarea').addClass('invalid-comment-text');
        processing = false;
        return;
    }

    $.ajax({
        url: '/news-management/view-news/comment',
        type: 'POST',
        data: commentText,
        contentType: "application/json",
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success: function(comment) {
            $('#new-post-textarea').removeClass('invalid-comment-text');
            var localeCode = 'En';
            var date = new Date(comment.creationDate);
            date = date.toLocaleString(localeCode, {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
            });
            var newCommentRow = commentTemplate.format(date, comment.commentId, comment.commentText.replaceAll(/[^\w\s]/gi, ''));
            $(newCommentRow).appendTo($('#comments')).slideDown('slow');
            $('#new-post-textarea').val('');
            processing = false;
        }
    });
});

