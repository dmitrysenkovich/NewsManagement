$("#authors").multiselect({
    header: news_edit.authors_prompt,
    noneSelectedText: news_edit.authors_prompt,
    selectedText: '# ' + news_edit.selected
});

$("#tags").multiselect({
    header: news_edit.tags_prompt,
    noneSelectedText: news_edit.tags_prompt,
    selectedText: '# ' + news_edit.selected
});


var uri = window.location.pathname;
if (uri == '/news-admin/add')
    $('#add-news-link').css('font-weight', 'bold');


String.prototype.format = function() {
    var formatted = this;
    for(var arg in arguments) {
        formatted = formatted.replace("{" + arg + "}", arguments[arg]);
    }
    return formatted;
};

String.prototype.replaceAll = function(search, replacement) {
    var target = this;
    return target.split(search).join(replacement);
};


var processing = false;


var header = $("meta[name='_csrf_header']").attr("content");
var token = $("meta[name='_csrf']").attr("content");


$('#save-news-button').on('click', function () {
    if (processing)
        return;

    processing = true;

    var newsId = parseInt($('.new-news').attr('id'));

    var title = $('#new-news-textarea').val();
    var shortText = $('#short-text-textarea').val();
    var fullText = $('#text-textarea').val();

    if (title == '' || shortText == '' || fullText == '') {
        if ($('#no-author-or-tag-selected').length)
            $('#no-author-or-tag-selected').toggle("slide", 500, function() { $(this).remove(); });
        if (!$('#invalid-news').length)
            $("<div id='invalid-news'>" + news_edit.invalid + "</div>").prependTo($('.new-news')).slideDown('fast');
        processing = false;
        return;
    }

    var checkedAuthors = $('#authors').multiselect('getChecked');
    var checkedAuthorsIds = [];
    for (var i = 0; i < checkedAuthors.length; i++) {
        var checkedAuthorId = $(checkedAuthors[i]).val();
        checkedAuthorsIds.push(parseInt(checkedAuthorId));
    }

    if (checkedAuthorsIds.length == 0) {
        if ($('#invalid-news').length)
            $('#invalid-news').toggle("slide", 500, function() { $(this).remove(); });
        if (!$('#no-author-selected').length)
            $("<div id='no-author-selected'>" + news_edit.no_author + "</div>").prependTo($('.new-news')).slideDown('fast');
        processing = false;
        return;
    }

    if ($('#invalid-news').length)
        $('#invalid-news').toggle("slide", 500, function() { $(this).remove(); });
    if ($('#no-author-selected').length)
        $('#no-author-selected').toggle("slide", 500, function() { $(this).remove(); });

    var checkedTags = $('#tags').multiselect('getChecked');
    var checkedTagsIds = [];
    for (var i = 0; i < checkedTags.length; i++) {
        var checkedTagId = $(checkedTags[i]).val();
        checkedTagsIds.push(parseInt(checkedTagId));
    }

    var news = {
        newsId: newsId,
        title: title,
        shortText: shortText.replaceAll('\n', '\\n'),
        fullText: fullText.replaceAll('\n', '\\n'),
        creationDate: $('#creation-date-in-milliseconds').val(),
        modificationDate: $('#modification-date-in-milliseconds').val()
    };
    var authors = [];
    for (var i = 0; i < checkedAuthorsIds.length; i++)
        authors.push({ authorId: checkedAuthorsIds[i] });
    var tags = [];
    for (var i = 0; i < checkedTagsIds.length; i++)
        tags.push({ tagId: checkedTagsIds[i] });

    var data = {
        news: news,
        authors: authors,
        tags: tags
    }

    $.ajax({
        url: '/news-admin/news/save',
        type: 'PUT',
        data:  JSON.stringify(data),
        contentType: "application/json",
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success: function(newNews) {
            $("<div id='news-saved'>" + news_edit.saved + "</div>").prependTo($('.new-news')).slideDown('fast');
            $('#news-saved').delay(3000).fadeOut(function() { $(this).remove(); });
            if (newNews) {
                $('.new-news').attr('id', newNews.newsId);
                $('#modification-date-in-milliseconds').val((Math.ceil(newNews.modificationDate/1000)-1)*1000);
                var lastEditDate = new Date(newNews.modificationDate);
                lastEditDate = lastEditDate.toLocaleString(localeCode.substring(0, 2), {
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric',
                });
                $('#date-textarea').val(lastEditDate);
                processing = false;
            }
        },
        error: function(xhr) {
            if (xhr.status == 409) {
                $("<div id='concurrent-modification'>" + news_edit.concurrent_modification + "</div>").prependTo($('.new-news')).slideDown('fast');
                $('#concurrent-modification').delay(3000).fadeOut(function() { $(this).remove(); });
                processing = false;
            }
        }
    });
});