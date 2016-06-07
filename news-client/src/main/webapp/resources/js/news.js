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
                                <div class='comment-text'>\
                                {1}\
                                </div>\
                            </div>\
                        </div>";


var processing = false;


function refreshNews(newsInfo) {
    $('#previous').removeClass('disabled-page-link');
    $('#next').removeClass('disabled-page-link');
    if (newsInfo.first)
        $('#previous').addClass('disabled-page-link');
    if (newsInfo.last)
        $('#next').addClass('disabled-page-link');

    var news = newsInfo.news;
    var authors = newsInfo.authors;
    var authorsString = '(' + $.i18n.prop('news.by') + ' {0})'.format(authors
        .map(function(author) { return author.authorName; }).join(', '));
    var lastEditDate = news.modificationDate ? news.modificationDate : news.creationDate;
    var lastEditDate = new Date(lastEditDate);
    lastEditDate = lastEditDate.toLocaleString(localeCode.substring(0, 2), {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
    });

    var comments = newsInfo.comments;
    var commentsDiv = '';
    for (var i = 0; i < comments.length; i++) {
        var comment = comments[i];
        var date = new Date(comment.creationDate);
        date = date.toLocaleString(localeCode.substring(0, 2), {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
        });
        var newCommentRow = commentTemplate.format(date, comment.commentText.replaceAll(/[^\w\s]/gi, ''));
        commentsDiv += newCommentRow;
    }

    var newNewsRow = newsTemplate.format(news.title,
        authorsString, lastEditDate, news.fullText.replaceAll('\\n', '<br>'), commentsDiv);

    $("#user-content").animate({ scrollTop: 0 }, "slow", function() {
        $('.news').toggle("slide", 500, function() {
            $(this).remove();
            $('#back-link').after(newNewsRow);
        });
    });

    processing = false;
}


$(document).on('click', '#previous', function () {
    if ($(this).hasClass('disabled-page-link') || processing)
        return;

    processing = true;

    $('#new-post-textarea').removeClass('invalid-comment-text');

    $.ajax({
        url: '/news-client/news/previous',
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
        url: '/news-client/news/next',
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

    var commentText = $('#new-post-textarea').val().replaceAll('\n', '\\n');
    if (!commentText || commentText == '') {
        $('#new-post-textarea').addClass('invalid-comment-text');
        processing = false;
        return;
    }

    $.ajax({
        url: '/news-client/comment/add',
        type: 'POST',
        data: {
            commentText: commentText
        },
        success: function(comment) {
            $('#new-post-textarea').removeClass('invalid-comment-text');
            var date = new Date(comment.creationDate);
            date = date.toLocaleString(localeCode.substring(0, 2), {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
            });
            var newCommentRow = commentTemplate.format(date, comment.commentText.replaceAll('\\n', '<br>'));
            $(newCommentRow).appendTo($('#comments')).slideDown('slow');
            $('#new-post-textarea').val('');
            processing = false;
        }
    });
});

