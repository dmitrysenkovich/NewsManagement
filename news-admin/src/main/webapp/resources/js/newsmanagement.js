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
                                    <span style='color: #ff0000'>Comments({6})</span> <a href='/news-management/edit-news/{7}'>Edit</a> <input type='checkbox' />\
                                </div>\
                            </div>\
                      </div>";
var newPageLinkTemplate = "<li><a class='page-link {0}' href='javascript:void(0)'>{1}</a></li>";
var firstPageLinkTemplate = "<li><a id='first-page' class='{0}' href='javascript:void(0)'>«</a></li>";
var previousPageLinkTemplate = "<li><a id='previous-page' class='{0}' href='javascript:void(0)'>❮</a></li>";
var nextPageLinkTemplate = "<li><a id='next-page' class='{0}' href='javascript:void(0)'>❯</a></li>";
var lastPageLinkTemplate = "<li><a id='last-page' class='{0}' href='javascript:void(0)'>»</a></li>";


var searchCriteria = null;


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
            searchCriteria = null;
            $('#authors').multiselect('uncheckAll');
            $('#tags').multiselect('uncheckAll');

            $('.pagination').children().each(function () {
                $(this).toggle("slide", 500, function() { $(this).remove(); });
            });

            var firstPageLinkRow = firstPageLinkTemplate.format('disabled-page-arrow');
            $(firstPageLinkRow).appendTo($('.pagination')).slideDown('fast');
            var previousPageLinkRow = previousPageLinkTemplate.format('disabled-page-arrow');
            $(previousPageLinkRow).appendTo($('.pagination')).slideDown('fast');

            var newsList = newsInfo.newsList;
            if (newsList.length != 0) {
                var firstPageLink = newPageLinkTemplate.format('active', 1);
                $(firstPageLink).appendTo($('.pagination')).slideDown('fast');
            }

            var pagesCount = newsInfo.pagesCount;
            var visiblePagesCount = pagesCount < 6 ? pagesCount : 5;
            for (var i = 1; i < visiblePagesCount; i++) {
                var newPageLink = newPageLinkTemplate.format('', i+1);
                $(newPageLink).appendTo($('.pagination')).slideDown('fast');
            }

            var disabledClass = visiblePagesCount <= 1 ? 'disabled-page-arrow' : '';
            var nextPageLinkRow = nextPageLinkTemplate.format(disabledClass);
            $(nextPageLinkRow).appendTo($('.pagination')).slideDown('fast');
            var lastPageLinkRow = lastPageLinkTemplate.format(disabledClass);
            $(lastPageLinkRow).appendTo($('.pagination')).slideDown('fast');

            if ($('#no-news-found-message').length && newsList.length == 0)
                return;

            var childs = $('#news-list').children();
            for (var i = 0; i < childs.length; i++) {
                $(childs[i]).toggle("slide", 500, function() { $(this).remove(); });
            }

            if (newsList.length == 0) {
                $("<div id='no-news-found-message'>No news found:c</div>").appendTo($('#news-list')).slideDown('fast');
                return;
            }
            $('#no-news-found-message').toggle("slide", 500, function() { $(this).remove(); });

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


$('#filter-button').on('click', function (){
    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");

    var checkedAuthorId = parseInt($('#authors').multiselect('getChecked').val());
    var checkedTags = $('#tags').multiselect('getChecked');
    var checkedTagsIds = [];
    for (var i = 0; i < checkedTags.length; i++) {
        var checkedTagId = $(checkedTags[i]).val();
        checkedTagsIds.push(parseInt(checkedTagId));
    }
    searchCriteria = {
        authorId : checkedAuthorId,
        tagIds : checkedTagsIds,
        pageIndex : 1
    };

    $.ajax({
        url: '/news-management/news-list-administration/filter',
        type: 'POST',
        data: JSON.stringify(searchCriteria),
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(newsInfo) {
            $('.pagination').children().each(function () {
                $(this).toggle("slide", 500, function() { $(this).remove(); });
            });

            var firstPageLinkRow = firstPageLinkTemplate.format('disabled-page-arrow');
            $(firstPageLinkRow).appendTo($('.pagination')).slideDown('fast');
            var previousPageLinkRow = previousPageLinkTemplate.format('disabled-page-arrow');
            $(previousPageLinkRow).appendTo($('.pagination')).slideDown('fast');

            var newsList = newsInfo.newsList;
            if (newsList.length != 0) {
                var firstPageLink = newPageLinkTemplate.format('active', 1);
                $(firstPageLink).appendTo($('.pagination')).slideDown('fast');
            }

            var pagesCount = newsInfo.pagesCount;
            var visiblePagesCount = pagesCount < 6 ? pagesCount : 5;
            for (var i = 1; i < visiblePagesCount; i++) {
                var newPageLink = newPageLinkTemplate.format('', i+1);
                $(newPageLink).appendTo($('.pagination')).slideDown('fast');
            }

            var disabledClass = visiblePagesCount <= 1 ? 'disabled-page-arrow' : '';
            var nextPageLinkRow = nextPageLinkTemplate.format(disabledClass);
            $(nextPageLinkRow).appendTo($('.pagination')).slideDown('fast');
            var lastPageLinkRow = lastPageLinkTemplate.format(disabledClass);
            $(lastPageLinkRow).appendTo($('.pagination')).slideDown('fast');

            if ($('#no-news-found-message').length && newsList.length == 0)
                return;

            var childs = $('#news-list').children();
            for (var i = 0; i < childs.length; i++) {
                $(childs[i]).toggle("slide", 500, function() { $(this).remove(); });
            }

            if (newsList.length == 0) {
                $("<div id='no-news-found-message'>No news found:c</div>").appendTo($('#news-list')).slideDown('fast');
                return;
            }
            $('#no-news-found-message').toggle("slide", 500, function() { $(this).remove(); });

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


$(document).on('click', '.page-link', function (event) {
    if ($(this).hasClass('disabled-page-arrow'))
        return;

    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");

    var pageIndex = parseInt($(this).text());

    if (!searchCriteria) {
        var checkedAuthorId = parseInt($('#authors').multiselect('getChecked').val());
        var checkedTags = $('#tags').multiselect('getChecked');
        var checkedTagsIds = [];
        for (var i = 0; i < checkedTags.length; i++) {
            var checkedTagId = $(checkedTags[i]).val();
            checkedTagsIds.push(parseInt(checkedTagId));
        }

        searchCriteria = {
            authorId : checkedAuthorId,
            tagIds : checkedTagsIds,
            pageIndex : pageIndex
        };
    }
    else
        searchCriteria.pageIndex = pageIndex;

    $.ajax({
        url: '/news-management/news-list-administration/page',
        type: 'POST',
        data: JSON.stringify(searchCriteria),
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(newsInfo) {
            $('.pagination > li > a').removeClass('active');

            $(event.target).addClass('active');

            var pagesCount = newsInfo.pagesCount;
            var currentPageIndex = parseInt($(event.target).text());
            if (currentPageIndex == 1) {
                $('#first-page').addClass('disabled-page-arrow');
                $('#previous-page').addClass('disabled-page-arrow');
                $('#last-page').removeClass('disabled-page-arrow');
                $('#next-page').removeClass('disabled-page-arrow');
            }
            if (currentPageIndex == pagesCount) {
                $('#first-page').removeClass('disabled-page-arrow');
                $('#previous-page').removeClass('disabled-page-arrow');
                $('#last-page').addClass('disabled-page-arrow');
                $('#next-page').addClass('disabled-page-arrow');
            }
            if (currentPageIndex > 1 && currentPageIndex < pagesCount) {
                $('#first-page').removeClass('disabled-page-arrow');
                $('#previous-page').removeClass('disabled-page-arrow');
                $('#last-page').removeClass('disabled-page-arrow');
                $('#next-page').removeClass('disabled-page-arrow');
            }
            if (pagesCount == 1) {
                $('#first-page').addClass('disabled-page-arrow');
                $('#previous-page').addClass('disabled-page-arrow');
                $('#last-page').addClass('disabled-page-arrow');
                $('#next-page').addClass('disabled-page-arrow');
            }

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


$(document).on('click', '#first-page', function () {
    if ($(this).hasClass('disabled-page-arrow'))
        return;

    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");

    if (!searchCriteria) {
        var checkedAuthorId = parseInt($('#authors').multiselect('getChecked').val());
        var checkedTags = $('#tags').multiselect('getChecked');
        var checkedTagsIds = [];
        for (var i = 0; i < checkedTags.length; i++) {
            var checkedTagId = $(checkedTags[i]).val();
            checkedTagsIds.push(parseInt(checkedTagId));
        }

        searchCriteria = {
            authorId : checkedAuthorId,
            tagIds : checkedTagsIds,
            pageIndex : 1
        };
    }
    else
        searchCriteria.pageIndex = 1;

    $.ajax({
        url: '/news-management/news-list-administration/page',
        type: 'POST',
        data: JSON.stringify(searchCriteria),
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(newsInfo) {
            $('.pagination').children().each(function () {
                $(this).toggle("slide", 500, function() { $(this).remove(); });
            });

            var firstPageLinkRow = firstPageLinkTemplate.format('disabled-page-arrow');
            $(firstPageLinkRow).appendTo($('.pagination')).slideDown('fast');
            var previousPageLinkRow = previousPageLinkTemplate.format('disabled-page-arrow');
            $(previousPageLinkRow).appendTo($('.pagination')).slideDown('fast');

            var newsList = newsInfo.newsList;
            if (newsList.length != 0) {
                var firstPageLink = newPageLinkTemplate.format('active', 1);
                $(firstPageLink).appendTo($('.pagination')).slideDown('fast');
            }

            var pagesCount = newsInfo.pagesCount;
            var visiblePagesCount = pagesCount < 6 ? pagesCount : 5;
            for (var i = 1; i < visiblePagesCount; i++) {
                var newPageLink = newPageLinkTemplate.format('', i+1);
                $(newPageLink).appendTo($('.pagination')).slideDown('fast');
            }

            var disabledClass = visiblePagesCount <= 1 ? 'disabled-page-arrow' : '';
            var nextPageLinkRow = nextPageLinkTemplate.format(disabledClass);
            $(nextPageLinkRow).appendTo($('.pagination')).slideDown('fast');
            var lastPageLinkRow = lastPageLinkTemplate.format(disabledClass);
            $(lastPageLinkRow).appendTo($('.pagination')).slideDown('fast');

            var childs = $('#news-list').children();
            for (var i = 0; i < childs.length; i++) {
                $(childs[i]).toggle("slide", 500, function() { $(this).remove(); });
            }

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


$(document).on('click', '#last-page', function () {
    if ($(this).hasClass('disabled-page-arrow'))
        return;

    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");

    if (!searchCriteria) {
        var checkedAuthorId = parseInt($('#authors').multiselect('getChecked').val());
        var checkedTags = $('#tags').multiselect('getChecked');
        var checkedTagsIds = [];
        for (var i = 0; i < checkedTags.length; i++) {
            var checkedTagId = $(checkedTags[i]).val();
            checkedTagsIds.push(parseInt(checkedTagId));
        }

        searchCriteria = {
            authorId : checkedAuthorId,
            tagIds : checkedTagsIds
        };
    }
    else
        searchCriteria.pageIndex = null;

    $.ajax({
        url: '/news-management/news-list-administration/page',
        type: 'POST',
        data: JSON.stringify(searchCriteria),
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(newsInfo) {
            $('.pagination').children().each(function () {
                $(this).toggle("slide", 500, function() { $(this).remove(); });
            });


            var pagesCount = newsInfo.pagesCount;
            var visiblePagesCount = pagesCount < 6 ? pagesCount : 5;
            var disabledClass = visiblePagesCount <= 1 ? 'disabled-page-arrow' : '';
            var firstPageLinkRow = firstPageLinkTemplate.format(disabledClass);
            $(firstPageLinkRow).appendTo($('.pagination')).slideDown('fast');
            var previousPageLinkRow = previousPageLinkTemplate.format(disabledClass);
            $(previousPageLinkRow).appendTo($('.pagination')).slideDown('fast');

            for (var i = pagesCount - visiblePagesCount; i < pagesCount - 1; i++) {
                var newPageLink = newPageLinkTemplate.format('', i+1);
                $(newPageLink).appendTo($('.pagination')).slideDown('fast');
            }
            var lastPageLink = newPageLinkTemplate.format('active', pagesCount);
            $(lastPageLink).appendTo($('.pagination')).slideDown('fast');

            var nextPageLinkRow = nextPageLinkTemplate.format('disabled-page-arrow');
            $(nextPageLinkRow).appendTo($('.pagination')).slideDown('fast');
            var lastPageLinkRow = lastPageLinkTemplate.format('disabled-page-arrow');
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


$(document).on('click', '#previous-page', function () {
    if ($(this).hasClass('disabled-page-arrow'))
        return;

    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");

    var pageIndex = -1;
    var childs = $('.pagination > li > a');
    for (var i = 0; i < childs.length; i++) {
        if ($(childs[i]).hasClass('active'))
            pageIndex = parseInt($(childs[i]).text()) - 1;
    }

    if (!searchCriteria) {
        var checkedAuthorId = parseInt($('#authors').multiselect('getChecked').val());
        var checkedTags = $('#tags').multiselect('getChecked');
        var checkedTagsIds = [];
        for (var i = 0; i < checkedTags.length; i++) {
            var checkedTagId = $(checkedTags[i]).val();
            checkedTagsIds.push(parseInt(checkedTagId));
        }

        searchCriteria = {
            authorId : checkedAuthorId,
            tagIds : checkedTagsIds,
            pageIndex : pageIndex
        };
    }
    else
        searchCriteria.pageIndex = pageIndex;

    $.ajax({
        url: '/news-management/news-list-administration/page',
        type: 'POST',
        data: JSON.stringify(searchCriteria),
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(newsInfo) {
            var pageLinkExists = false;
            var pageLinkIndex = -1;
            var childs = $('.pagination > li > a');
            for (var i = 0; i < childs.length; i++) {
                $(childs[i]).removeClass('active');
                if (parseInt($(childs[i]).text()) == pageIndex) {
                    pageLinkExists = true;
                    pageLinkIndex = i;
                }
            }

            if (pageLinkExists) {
                $(childs[pageLinkIndex]).addClass('active');
            }
            else {
                $('.pagination').children().each(function () {
                    $(this).toggle("slide", 500, function() { $(this).remove(); });
                });

                var disabledRow = pageIndex == 1 ? 'disabled-page-arrow' : '';
                var firstPageLinkRow = firstPageLinkTemplate.format(disabledRow);
                $(firstPageLinkRow).appendTo($('.pagination')).slideDown('fast');
                var previousPageLinkRow = previousPageLinkTemplate.format(disabledRow);
                $(previousPageLinkRow).appendTo($('.pagination')).slideDown('fast');
                for (var i = 0; i < 5; i++) {
                    var active = i == 0 ? 'active' : '';
                    var pageLink = newPageLinkTemplate.format(active, pageIndex+i);
                    $(pageLink).appendTo($('.pagination')).slideDown('fast');
                }
                var nextPageLinkRow = nextPageLinkTemplate.format('');
                $(nextPageLinkRow).appendTo($('.pagination')).slideDown('fast');
                var lastPageLinkRow = lastPageLinkTemplate.format('');
                $(lastPageLinkRow).appendTo($('.pagination')).slideDown('fast');
            }

            $('#next-page').removeClass('disabled-page-arrow');
            $('#last-page').removeClass('disabled-page-arrow');

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


$(document).on('click', '#next-page', function () {
    if ($(this).hasClass('disabled-page-arrow'))
        return;

    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");

    var pageIndex = -1;
    var childs = $('.pagination > li > a');
    for (var i = 0; i < childs.length; i++) {
        if ($(childs[i]).hasClass('active'))
            pageIndex = parseInt($(childs[i]).text()) + 1;
    }

    if (!searchCriteria) {
        var checkedAuthorId = parseInt($('#authors').multiselect('getChecked').val());
        var checkedTags = $('#tags').multiselect('getChecked');
        var checkedTagsIds = [];
        for (var i = 0; i < checkedTags.length; i++) {
            var checkedTagId = $(checkedTags[i]).val();
            checkedTagsIds.push(parseInt(checkedTagId));
        }

        searchCriteria = {
            authorId : checkedAuthorId,
            tagIds : checkedTagsIds,
            pageIndex : pageIndex
        };
    }
    else
        searchCriteria.pageIndex = pageIndex;

    $.ajax({
        url: '/news-management/news-list-administration/page',
        type: 'POST',
        data: JSON.stringify(searchCriteria),
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(newsInfo) {
            var pageLinkExists = false;
            var pageLinkIndex = -1;
            var childs = $('.pagination > li > a');
            for (var i = 0; i < childs.length; i++) {
                $(childs[i]).removeClass('active');
                if (parseInt($(childs[i]).text()) == pageIndex) {
                    pageLinkExists = true;
                    pageLinkIndex = i;
                }
            }

            var pagesCount = newsInfo.pagesCount;
            if (pageLinkExists) {
                $(childs[pageLinkIndex]).addClass('active');
            }
            else {
                $('.pagination').children().each(function () {
                    $(this).toggle("slide", 500, function() { $(this).remove(); });
                });

                var firstPageLinkRow = firstPageLinkTemplate.format('');
                $(firstPageLinkRow).appendTo($('.pagination')).slideDown('fast');
                var previousPageLinkRow = previousPageLinkTemplate.format('');
                $(previousPageLinkRow).appendTo($('.pagination')).slideDown('fast');
                for (var i = 0; i < 5; i++) {
                    var active = i == 4 ? 'active' : '';
                    var pageLink = newPageLinkTemplate.format(active, pageIndex+i-4);
                    $(pageLink).appendTo($('.pagination')).slideDown('fast');
                }
                var disabledRow = pageIndex == pagesCount ? 'disabled-page-arrow' : '';
                var nextPageLinkRow = nextPageLinkTemplate.format(disabledRow);
                $(nextPageLinkRow).appendTo($('.pagination')).slideDown('fast');
                var lastPageLinkRow = lastPageLinkTemplate.format(disabledRow);
                $(lastPageLinkRow).appendTo($('.pagination')).slideDown('fast');
            }

            $('#first-page').removeClass('disabled-page-arrow');
            $('#previous-page').removeClass('disabled-page-arrow');

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
