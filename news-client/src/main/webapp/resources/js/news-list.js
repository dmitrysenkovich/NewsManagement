$("#authors").multiselect({
    header: news_list.authors_prompt,
    noneSelectedText: news_list.authors_prompt,
    selectedText: '# ' + news_list.selected
});

$("#tags").multiselect({
    header: news_list.tags_prompt,
    noneSelectedText: news_list.tags_prompt,
    selectedText: '# ' + news_list.selected
});


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

var newNewsTemplate = "<div class='short-news'>\
                            <div class='short-news-title-row'>\
                                <div class='short-news-title'>\
                                    <a href='/news-client/news/{0}'>\
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
                                    <span style='color: #ff0000'>" + news_list.comments + "({6})</span> <a href='/news-client/news/{7}'>" + news_list.view + "</a>\
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


function fillNewsList(newsListInfo, excludedNewsIds) {
    var newsList = newsListInfo.newsList;
    var authorsByNewsId = newsListInfo.authorsByNewsId;
    var tagsByNewsId = newsListInfo.tagsByNewsId;
    var commentsCountByNewsId = newsListInfo.commentsCountByNewsId;
    for (var i = 0; i < newsList.length; i++) {
        var news = newsList[i];

        if (excludedNewsIds)
            if ($.inArray(news.newsId, excludedNewsIds) != -1)
                continue;

        var authorsString = ('(' + news_list.by + ' {0})').format(authorsByNewsId[news.newsId]
            .map(function(author) { return author.authorName; }).join(', '));
        var tagsString = tagsByNewsId[news.newsId]
            .map(function(tag) { return tag.tagName; }).join(', ');
        var commentsCount = commentsCountByNewsId[news.newsId];
        var lastEditDate = news.modificationDate ? news.modificationDate : news.creationDate;
        var lastEditDate = new Date(lastEditDate);
        lastEditDate = lastEditDate.toLocaleString(localeCode.substring(0, 2), {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
        });
        var newNewsRow = newNewsTemplate
            .format(news.newsId, news.title, authorsString,
                lastEditDate, news.shortText.replaceAll('\\n', '<br>'),
                tagsString, commentsCount, news.newsId);
        $(newNewsRow).appendTo($('#news-list')).slideDown('fast');
    }
}


function refreshNewsList(newsListInfo) {
    var newsList = newsListInfo.newsList;
    if ($('#no-news-found-message').length && (!newsList || newsList.length == 0)) {
        processing = false;
        return;
    }

    var childs = $('#news-list').children();
    for (var i = 0; i < childs.length; i++) {
        $(childs[i]).toggle("slide", 500, function() { $(this).remove(); });
    }

    if (!newsList || newsList.length == 0) {
        $("<div id='no-news-found-message'>" + news_list.no_news + "</div>").appendTo($('#news-list')).slideDown('fast');
        processing = false;
        return;
    }
    $('#no-news-found-message').toggle("slide", 500, function() { $(this).remove(); });

    fillNewsList(newsListInfo);

    processing = false;
}


function refreshPaginationRow(newsListInfo, pageIndex) {
    if ($('#no-news-found-message').length && (!newsListInfo || newsListInfo.newsList.length == 0))
        return;

    var pageLinks = $('.pagination > li > a');
    var firstPageLink = pageLinks[2];
    var currentFirstPageLinkValue = parseInt($(firstPageLink).text());
    var lastPageLink = pageLinks[pageLinks.length - 3];
    var currentLastPageLinkValue = parseInt($(lastPageLink).text());
    var currentActivePageLinkValue = parseInt($('.active').text());

    var pagesCount = newsListInfo.pagesCount > 5 ? 5 : newsListInfo.pagesCount;
    var firstValue = isNaN(currentFirstPageLinkValue) ? 1 : currentFirstPageLinkValue;
    if (!isNaN(currentFirstPageLinkValue) && pageIndex == currentFirstPageLinkValue - 1)
        firstValue = currentFirstPageLinkValue - 1;
    else if (!isNaN(currentFirstPageLinkValue) && pageIndex < currentFirstPageLinkValue)
        firstValue = 1;
    else if (!isNaN(currentLastPageLinkValue) && pageIndex == currentLastPageLinkValue + 1)
        firstValue = currentFirstPageLinkValue + 1;
    else if (!isNaN(currentLastPageLinkValue) && (pageIndex > currentLastPageLinkValue || currentActivePageLinkValue == newsListInfo.pagesCount+1))
        firstValue = pageIndex - pagesCount + 1;
    var activePageLinkIndex = pageIndex - firstValue;

    var firstLinksClass = !pagesCount || pagesCount == 0 || (activePageLinkIndex == 0 && firstValue == 1) ? 'disabled-page-arrow' : '';
    var secondLinksClass = !pagesCount || pagesCount == 0 || ((firstValue + pagesCount == newsListInfo.pagesCount + 1) && activePageLinkIndex == pagesCount - 1) ? 'disabled-page-arrow' : '';

    $('.pagination').children().each(function () {
        $(this).toggle("slide", 500, function() { $(this).remove(); });
    });

    var firstPageLinkRow = firstPageLinkTemplate.format(firstLinksClass);
    $(firstPageLinkRow).appendTo($('.pagination')).slideDown('fast');
    var previousPageLinkRow = previousPageLinkTemplate.format(firstLinksClass);
    $(previousPageLinkRow).appendTo($('.pagination')).slideDown('fast');

    if (newsListInfo.pagesCount > 0) {
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
    var checkedAuthors = $('#authors').multiselect('getChecked');
    var checkedAuthorsIds = [];
    for (var i = 0; i < checkedAuthors.length; i++) {
        var checkedAuthorId = $(checkedAuthors[i]).val();
        checkedAuthorsIds.push(parseInt(checkedAuthorId));
    }
    var checkedTags = $('#tags').multiselect('getChecked');
    var checkedTagsIds = [];
    for (var i = 0; i < checkedTags.length; i++) {
        var checkedTagId = $(checkedTags[i]).val();
        checkedTagsIds.push(parseInt(checkedTagId));
    }
    searchCriteria = {
        authorIds : checkedAuthorsIds,
        tagIds : checkedTagsIds,
        pageIndex : 1
    };
}


$('#reset-button').on('click', function () {
    if (processing)
        return;
    processing = true;

    $.ajax({
        url: '/news-client/news/reset',
        type: 'GET',
        success: function(newsListInfo) {
            console.log(newsListInfo);
            searchCriteria = null;
            $('#authors').multiselect('uncheckAll');
            $('#tags').multiselect('uncheckAll');

            refreshPaginationRow(newsListInfo, 1);
            refreshNewsList(newsListInfo);
        }
    });
});


$('#filter-button').on('click', function () {
    if (processing)
        return;
    processing = true;

    fillSearchCriteria();

    $.ajax({
        url: '/news-client/news/filter?searchCriteria=' + encodeURIComponent(JSON.stringify(searchCriteria)),
        type: 'GET',
        success: function(newsListInfo) {
            refreshPaginationRow(newsListInfo, 1);
            refreshNewsList(newsListInfo);
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
        url: '/news-client/news/page?searchCriteria=' + encodeURIComponent(JSON.stringify(searchCriteria)),
        type: 'GET',
        success: function(newsListInfo) {
            $('.pagination > li > a').removeClass('active');
            $(event.target).addClass('active');

            var pagesCount = newsListInfo.pagesCount;
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

            refreshNewsList(newsListInfo);
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
        url: '/news-client/news/page?searchCriteria=' + encodeURIComponent(JSON.stringify(searchCriteria)),
        type: 'GET',
        success: function(newsListInfo) {
            refreshPaginationRow(newsListInfo, 1);
            refreshNewsList(newsListInfo);
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
        url: '/news-client/news/page?searchCriteria=' + encodeURIComponent(JSON.stringify(searchCriteria)),
        type: 'GET',
        success: function(newsListInfo) {
            refreshPaginationRow(newsListInfo, newsListInfo.pagesCount);
            refreshNewsList(newsListInfo);
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
        url: '/news-client/news/page?searchCriteria=' + encodeURIComponent(JSON.stringify(searchCriteria)),
        type: 'GET',
        success: function(newsListInfo) {
            refreshPaginationRow(newsListInfo, pageIndex);
            refreshNewsList(newsListInfo);
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
        url: '/news-client/news/page?searchCriteria=' + encodeURIComponent(JSON.stringify(searchCriteria)),
        type: 'GET',
        success: function(newsListInfo) {
            refreshPaginationRow(newsListInfo, pageIndex);
            refreshNewsList(newsListInfo);
        }
    });
});