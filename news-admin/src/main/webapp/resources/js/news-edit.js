$("#authors").multiselect({
    header: "Please select authors",
    noneSelectedText: "Please select authors"
});

$("#tags").multiselect({
    header: "Please select tags",
    noneSelectedText: "Please select tags"
});


var uri = window.location.pathname;
if (uri == '/news-management/add-news')
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


$('#save-news-button').click(function () {
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
            $("<div id='invalid-news'>News is invalid. Check if all fields are no empty.</div>").prependTo($('.new-news')).slideDown('fast');
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
            $("<div id='no-author-selected'>Every news must have at least one author.</div>").prependTo($('.new-news')).slideDown('fast');
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
        fullText: fullText.replaceAll('\n', '\\n')
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
        url: '/news-management/news/save',
        type: 'POST',
        data:  JSON.stringify(data),
        contentType: "application/json",
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success: function(newNewsId) {
            $("<div id='news-saved'>News was successfully saved.</div>").prependTo($('.new-news')).slideDown('fast');
            $('#news-saved').delay(3000).fadeOut(function() { $(this).remove(); });
            if (!newsId)
                $('.new-news').attr('id', newNewsId);
            processing = false;
        }
    });
});