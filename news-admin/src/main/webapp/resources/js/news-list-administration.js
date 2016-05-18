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
                                    <span style='color: #ff0000'>Comments({6})</span> <a href='/news-management/edit-news/{7}'>Edit</a> <input id={8} type='checkbox' />\
                                </div>\
                            </div>\
                      </div>";
var newPageLinkTemplate = "<li><a class='page-link {0}' href='javascript:void(0)'>{1}</a></li>";
var firstPageLinkTemplate = "<li><a id='first-page' class='{0}' href='javascript:void(0)'>«</a></li>";
var previousPageLinkTemplate = "<li><a id='previous-page' class='{0}' href='javascript:void(0)'>❮</a></li>";
var nextPageLinkTemplate = "<li><a id='next-page' class='{0}' href='javascript:void(0)'>❯</a></li>";
var lastPageLinkTemplate = "<li><a id='last-page' class='{0}' href='javascript:void(0)'>»</a></li>";


var searchCriteria = null;
var processing = false;


var header = $("meta[name='_csrf_header']").attr("content");
var token = $("meta[name='_csrf']").attr("content");


function fillNewsList(newsInfo, excludedNewsIds) {
    var newsList = newsInfo.newsList;
    var authorsByNewsId = newsInfo.authorsByNewsId;
    var tagsByNewsId = newsInfo.tagsByNewsId;
    var commentsCountByNewsId = newsInfo.commentsCountByNewsId;
    for (var i = 0; i < newsList.length; i++) {
        var news = newsList[i];

        if (excludedNewsIds)
            if ($.inArray(news.newsId, excludedNewsIds) != -1)
                continue;

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
            .format(news.newsId, news.title, authorsString,
                lastEditDate, news.shortText.replace(/[^\w\s]/gi, ''),
                tagsString, commentsCount, news.newsId, news.newsId);
        $(newsNewsRow).appendTo($('#news-list')).slideDown('fast');
    }
    if (newsInfo && newsInfo.newsList.length > 0 && !$('#delete-button').length)
        $("<button id='delete-button'>Delete</button>").appendTo($('#news-list')).slideDown('fast');
}


function refreshNewsList(newsInfo) {
    var newsList = newsInfo.newsList;
    if ($('#no-news-found-message').length && (!newsList || newsList.length == 0)) {
        processing = false;
        return;
    }

    var childs = $('#news-list').children();
    for (var i = 0; i < childs.length; i++) {
        $(childs[i]).toggle("slide", 500, function() { $(this).remove(); });
    }

    if (!newsList || newsList.length == 0) {
        $("<div id='no-news-found-message'>No news found:c</div>").appendTo($('#news-list')).slideDown('fast');
        processing = false;
        return;
    }
    $('#no-news-found-message').toggle("slide", 500, function() { $(this).remove(); });

    fillNewsList(newsInfo);

    processing = false;
}


function refreshPaginationRow(newsInfo, pageIndex) {
    if ($('#no-news-found-message').length && (!newsInfo || newsInfo.newsList.length == 0))
        return;

    var pageLinks = $('.pagination > li > a');
    var firstPageLink = pageLinks[2];
    var currentFirstPageLinkValue = parseInt($(firstPageLink).text());
    var lastPageLink = pageLinks[pageLinks.length - 3];
    var currentLastPageLinkValue = parseInt($(lastPageLink).text());
    var currentActivePageLinkValue = parseInt($('.active').text());

    var pagesCount = newsInfo.pagesCount > 5 ? 5 : newsInfo.pagesCount;
    var firstValue = isNaN(currentFirstPageLinkValue) ? 1 : currentFirstPageLinkValue;
    if (!isNaN(currentFirstPageLinkValue) && pageIndex == currentFirstPageLinkValue - 1)
        firstValue = currentFirstPageLinkValue - 1;
    else if (!isNaN(currentFirstPageLinkValue) && pageIndex < currentFirstPageLinkValue)
        firstValue = 1;
    else if (!isNaN(currentLastPageLinkValue) && pageIndex == currentLastPageLinkValue + 1)
        firstValue = currentFirstPageLinkValue + 1;
    else if (!isNaN(currentLastPageLinkValue) && (pageIndex > currentLastPageLinkValue || currentActivePageLinkValue == newsInfo.pagesCount+1))
        firstValue = pageIndex - pagesCount + 1;
    var activePageLinkIndex = pageIndex - firstValue;

    var firstLinksClass = !pagesCount || pagesCount == 0 || (activePageLinkIndex == 0 && firstValue == 1) ? 'disabled-page-arrow' : '';
    var secondLinksClass = !pagesCount || pagesCount == 0 || ((firstValue + pagesCount == newsInfo.pagesCount + 1) && activePageLinkIndex == pagesCount - 1) ? 'disabled-page-arrow' : '';

    $('.pagination').children().each(function () {
        $(this).toggle("slide", 500, function() { $(this).remove(); });
    });

    var firstPageLinkRow = firstPageLinkTemplate.format(firstLinksClass);
    $(firstPageLinkRow).appendTo($('.pagination')).slideDown('fast');
    var previousPageLinkRow = previousPageLinkTemplate.format(firstLinksClass);
    $(previousPageLinkRow).appendTo($('.pagination')).slideDown('fast');

    if (newsInfo.pagesCount > 0) {
        for (var i = 0; i < pagesCount; i++) {
            var active = firstValue + i == pageIndex ? 'active' : '';
            var pageLink = newPageLinkTemplate.format(active, firstValue + i);
            $(pageLink).appendTo($('.pagination')).slideDown('fast');
        }
    }

    var nextPageLinkRow = nextPageLinkTemplate.format(secondLinksClass);
    $(nextPageLinkRow).appendTo($('.pagination')).slideDown('fast');
    var lastPageLinkRow = lastPageLinkTemplate.format(secondLinksClass);
    $(lastPageLinkRow).appendTo($('.pagination')).slideDown('fast');
}


function fillSearchCriteria() {
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


$('#reset-button').on('click', function () {
    if (processing)
        return;
    processing = true;

    $.ajax({
        url: '/news-management/news-list-administration/reset',
        type: 'GET',
        success: function(newsInfo) {
            searchCriteria = null;
            $('#authors').multiselect('uncheckAll');
            $('#tags').multiselect('uncheckAll');

            refreshPaginationRow(newsInfo, 1);
            refreshNewsList(newsInfo);
        }
    });
});


$('#filter-button').on('click', function () {
    if (processing)
        return;
    processing = true;

    fillSearchCriteria();

    $.ajax({
        url: '/news-management/news-list-administration/filter?searchCriteria=' + encodeURIComponent(JSON.stringify(searchCriteria)),
        type: 'GET',
        success: function(newsInfo) {
            refreshPaginationRow(newsInfo, 1);
            refreshNewsList(newsInfo);
        }
    });
});


$(document).on('click', '.page-link', function (event) {
    if ($(this).hasClass('disabled-page-arrow') || processing)
        return;
    processing = true;

    var pageIndex = parseInt($(this).text());
    if (!searchCriteria) {
        fillSearchCriteria();
        searchCriteria.pageIndex = pageIndex;
    }
    else
        searchCriteria.pageIndex = pageIndex;

    $.ajax({
        url: '/news-management/news-list-administration/page?searchCriteria=' + encodeURIComponent(JSON.stringify(searchCriteria)),
        type: 'GET',
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

            refreshNewsList(newsInfo);
        }
    });
});


$(document).on('click', '#first-page', function () {
    if ($(this).hasClass('disabled-page-arrow')|| processing)
        return;
    processing = true;

    if (!searchCriteria) {
        fillSearchCriteria();
    }
    else
        searchCriteria.pageIndex = 1;

    $.ajax({
        url: '/news-management/news-list-administration/page?searchCriteria=' + encodeURIComponent(JSON.stringify(searchCriteria)),
        type: 'GET',
        success: function(newsInfo) {
            refreshPaginationRow(newsInfo, 1);
            refreshNewsList(newsInfo);
        }
    });
});


$(document).on('click', '#last-page', function () {
    if ($(this).hasClass('disabled-page-arrow') || processing)
        return;
    processing = true;

    if (!searchCriteria) {
        fillSearchCriteria();
    }
    searchCriteria.pageIndex = null;

    $.ajax({
        url: '/news-management/news-list-administration/page?searchCriteria=' + encodeURIComponent(JSON.stringify(searchCriteria)),
        type: 'GET',
        success: function(newsInfo) {
            refreshPaginationRow(newsInfo, newsInfo.pagesCount);
            refreshNewsList(newsInfo);
        }
    });
});


$(document).on('click', '#previous-page', function () {
    if ($(this).hasClass('disabled-page-arrow') || processing)
        return;
    processing = true;

    var pageIndex = -1;
    var childs = $('.pagination > li > a');
    for (var i = 0; i < childs.length; i++) {
        if ($(childs[i]).hasClass('active'))
            pageIndex = parseInt($(childs[i]).text()) - 1;
    }

    if (!searchCriteria) {
        fillSearchCriteria();
    }
    searchCriteria.pageIndex = pageIndex;

    $.ajax({
        url: '/news-management/news-list-administration/page?searchCriteria=' + encodeURIComponent(JSON.stringify(searchCriteria)),
        type: 'GET',
        success: function(newsInfo) {
            refreshPaginationRow(newsInfo, pageIndex);
            refreshNewsList(newsInfo);
        }
    });
});


$(document).on('click', '#next-page', function () {
    if ($(this).hasClass('disabled-page-arrow') || processing)
        return;

    processing = true;

    var pageIndex = -1;
    var childs = $('.pagination > li > a');
    for (var i = 0; i < childs.length; i++) {
        if ($(childs[i]).hasClass('active'))
            pageIndex = parseInt($(childs[i]).text()) + 1;
    }

    if (!searchCriteria) {
        fillSearchCriteria();
    }
    searchCriteria.pageIndex = pageIndex;

    $.ajax({
        url: '/news-management/news-list-administration/page?searchCriteria=' + encodeURIComponent(JSON.stringify(searchCriteria)),
        type: 'GET',
        success: function(newsInfo) {
            refreshPaginationRow(newsInfo, pageIndex);
            refreshNewsList(newsInfo);
        }
    });
});


$(document).on('click', '#delete-button', function () {
    var newsIds = [];
    var excludedNewsIds = [];
    var checkboxes = $('.short-news > .short-news-footer-row > .short-news-others > input');
    for (var i = 0; i < checkboxes.length; i++) {
        var checkbox = checkboxes[i];
        if (checkboxes[i].checked)
            newsIds.push(parseInt(checkbox.id));
        else
            excludedNewsIds.push(parseInt(checkbox.id));
    }

    if (newsIds.length == 0)
        return;

    var pageIndex = -1;
    var childs = $('.pagination > li > a');
    for (var i = 0; i < childs.length; i++) {
        if ($(childs[i]).hasClass('active'))
            pageIndex = parseInt($(childs[i]).text());
    }

    if (!searchCriteria) {
        fillSearchCriteria();
    }
    searchCriteria.pageIndex = pageIndex;
    searchCriteria.pageSize = 5;

    $.ajax({
        url: '/news-management/news-list-administration/delete',
        type: 'POST',
        data:  JSON.stringify({
            newsIds: newsIds,
            searchCriteria: searchCriteria
        }),
        contentType: "application/json",
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success: function(newsInfo) {
            if (!newsInfo)
                $('#delete-button').toggle("slide", 500, function() { $(this).remove(); });

            var newsCount = checkboxes.length;
            var remainedNewsCount = newsCount - newsIds.length;
            if (remainedNewsCount == 0) {
                if (pageIndex > 1)
                    pageIndex -= 1;

                refreshPaginationRow(newsInfo, pageIndex);
                refreshNewsList(newsInfo);
            }
            else {
                for (var i = 0; i < checkboxes.length; i++) {
                    var checkbox = checkboxes[i];
                    if ($.inArray(parseInt(checkbox.id), newsIds) != -1) {
                        var news = $(checkbox).parent().parent().parent();
                        $(news).toggle("slide", 500, function() { $(this).remove(); });
                    }
                }
                fillNewsList(newsInfo, excludedNewsIds);
                refreshPaginationRow(newsInfo, pageIndex);
            }
        }
    });
});
