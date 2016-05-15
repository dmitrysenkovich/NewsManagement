$("#authors").multiselect({
    multiple: false,
    header: "Please select the author",
    noneSelectedText: "Please select the author",
    selectedList: 1
});

$("#tags").multiselect({
    header: "Please select the tag",
    noneSelectedText: "Please select the tag"
});

var url = window.location.pathname;
if (url.indexOf('news-list-administration', url.length - 'news-list-administration'.length) !== -1) {
    $('#news-list-link').css('font-weight', 'bold');
}
else if (url.indexOf('add-news', url.length - 'add-news'.length) !== -1) {
    $('#add-news-link').css('font-weight', 'bold');
}
else if (url.indexOf('authors', url.length - 'authors'.length) !== -1) {
    $('#authors-link').css('font-weight', 'bold');
}
else if (url.indexOf('tags', url.length - 'tags'.length) !== -1) {
    $('#tags-link').css('font-weight', 'bold');
}

String.prototype.format = function() {
    var formatted = this;
    for( var arg in arguments ) {
        formatted = formatted.replace("{" + arg + "}", arguments[arg]);
    }
    return formatted;
};

var newNewsTemplate = "<div class='short-news'>\
                            <div class='short-news-title-row'>\
                                <div class='short-news-title'>\
                                    <a href='/news-management/view-news/{0}'>\
                                    {1}</a>\
                                </div>\
                                <div class='short-news-authors'>\
                                {2}</div>\
                                <div class='short-news-last-edit'>\
                                    <u>\
                                    {3}</u>\
                                </div>\
                            </div>\
                            <div class='short-news-short-text-row'>\
                            {4}</div>\
                            <div class='short-news-footer-row'>\
                                <div class='short-news-tags'>\
                                {5}</div>\
                                <div class='short-news-others'>\
                                    <span style='color: #ff0000'>Comments({6})</span> <a href='#'/news-management/edit-news/{7}'>Edit</a> <input type='checkbox' />\
                                </div>\
                            </div>\
                      </div>";

var newPageLinkTemplate = "<li><a class='{0}' href='#'>{1}</a></li>";
var firstPageLinkTemplate = "<li><a id='first-page' class='{0}' href='#'>«</a></li>";
var previousPageLinkTemplate = "<li><a id='previous-page' class='{0}' href='#'>❮</a></li>";
var nextPageLinkTemplate = "<li><a id='next-page' class='{0}' href='#'>❯</a></li>";
var lastPageLinkTemplate = "<li><a id='last-page' class='{0}' href='#'>»</a></li>";

$('#reset-button').on('click', function (){
    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");

    $.ajax({
        url: '/news-management/news-list-administration/reset',
        type: 'POST',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(newsInfo) {
            document.getElementById('authors').selectedIndex = 0;
            document.getElementById('tags').selectedIndex = 0;

            $('.pagination').children().each(function () {
                $(this).toggle("slide", 500, function() { $(this).remove(); });
            });

            var firstPageLinkRow = firstPageLinkTemplate.format('disabled-page-arrow');
            $(firstPageLinkRow).appendTo($('.pagination')).slideDown('fast');
            var previousPageLinkRow = previousPageLinkTemplate.format('disabled-page-arrow');
            $(previousPageLinkRow).appendTo($('.pagination')).slideDown('fast');
            var firstPageLink = newPageLinkTemplate.format('active', 1);
            $(firstPageLink).appendTo($('.pagination')).slideDown('fast');

            var pagesCount = newsInfo.pagesCount;
            var visiblePagesCount = pagesCount < 6 ? pagesCount : 5;
            for (var i = 1; i < visiblePagesCount; i++) {
                var newPageLink = newPageLinkTemplate.format('', i+1);
                $(newPageLink).appendTo($('.pagination')).slideDown('fast');
            }

            var disabledClass = visiblePagesCount == 1 ? 'disabled-page-arrow' : '';
            var nextPageLinkRow = nextPageLinkTemplate.format(disabledClass);
            $(nextPageLinkRow).appendTo($('.pagination')).slideDown('fast');
            var lastPageLinkRow = lastPageLinkTemplate.format(disabledClass);
            $(lastPageLinkRow).appendTo($('.pagination')).slideDown('fast');

            var childs = $('#news-list').children();
            for (var i = 0; i < childs.length; i++) {
                $(childs[i]).toggle("slide", 500, function() { $(this).remove(); });
            }

            var newsList = newsInfo.newsList;
            var authorsByNewsId = newsInfo.authorsByNewsId;
            var tagsByNewsId = newsInfo.tagsByNewsId;
            var commentsCountByNewsId = newsInfo.commentsCountByNewsId;
            for (var i = 0; i < newsList.length; i++) {
                var news = newsList[i];
                var authorsString = '(by {0})'.format(authorsByNewsId[news.newsId]
                    .map(function(author) {return author.authorName;}).join(', '));
                var tagsString = tagsByNewsId[news.newsId]
                    .map(function(tag) {return tag.tagName;}).join(', ');
                var commentsCount = commentsCountByNewsId[news.newsId];
                var localeCode = 'En';
                var lastEditDate = news.modificationDate ? news.modificationDate : news.creationDate;
                var lastEditDate = new Date(lastEditDate);
                lastEditDate = lastEditDate.toLocaleString(localeCode, {
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric',
                });
                var newsNewsRow = newNewsTemplate
                    .format(news.newsId, news.title, authorsString, lastEditDate,
                        news.shortText.replace(/[^\w\s]/gi, ''), tagsString, commentsCount, news.newsId);
                $(newsNewsRow).appendTo($('#news-list')).slideDown('fast');
            }
            $("<button id='delete-button'>Delete</button>").appendTo($('#news-list')).slideDown('fast');
        }
    });
});
