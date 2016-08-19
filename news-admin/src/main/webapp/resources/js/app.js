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

(function() {
	'use strict';

	var i18n = new I18nAdapter(['en_US', 'ru_RU'], 'en_US');
    i18n.setLocale('en-us', ['/news-admin/resources/js/i18n/en-us/localization.json']);
    window.i18nAdapter = i18n;
	var newsManagementApp = angular.module('newsManagementApp', ['I18nAngular', 'ngRoute', 'angular-repeat-n', 'ngLoadingSpinner', 'spring-security-csrf-token-interceptor', 'ipCookie']);

	newsManagementApp.config(['$routeProvider', '$locationProvider', 'csrfProvider', function($routeProvider, $locationProvider, csrfProvider) {
		$routeProvider
			.when('/login/:error?/:logout?', {
				templateUrl : '/news-admin/resources/pages/login.html',
				controller  : 'loginController'
			})
			.when('//:error?/:logout?', {
				templateUrl : '/news-admin/resources/pages/login.html',
				controller  : 'loginController'
			})
			.when('/news-list', {
				templateUrl : '/news-admin/resources/pages/news-list.html',
				controller  : 'newsListController'
			})
			.when('/news/:newsId/:searchCriteria?', {
				templateUrl : '/news-admin/resources/pages/news.html',
				controller  : 'newsController'
			})
			.when('/add', {
				templateUrl : '/news-admin/resources/pages/news-edit.html',
				controller  : 'newsEditController'
			})
			.when('/edit/:newsId', {
				templateUrl : '/news-admin/resources/pages/news-edit.html',
				controller  : 'newsEditController'
			})
			.when('/authors', {
				templateUrl : '/news-admin/resources/pages/authors.html',
				controller  : 'authorsController'
			})
			.when('/tags', {
				templateUrl : '/news-admin/resources/pages/tags.html',
				controller  : 'tagsController'
			})
			.when('/400', {
				templateUrl : '/news-admin/resources/pages/error.html',
				controller : 'errorController'
			})
			.when('/403', {
				templateUrl : '/news-admin/resources/pages/error.html',
				controller : 'errorController'
			})
			.when('/404', {
				templateUrl : '/news-admin/resources/pages/error.html',
				controller : 'errorController'
			})
			.when('/415', {
				templateUrl : '/news-admin/resources/pages/error.html',
				controller : 'errorController'
			})
			.when('/500', {
				templateUrl : '/news-admin/resources/pages/error.html',
				controller : 'errorController'
			});
		$routeProvider.otherwise({ redirectTo: '/404' });

		$locationProvider.html5Mode(true);

		csrfProvider.config({
            url: '/news-admin/csrf',
            maxRetries: 1,
            csrfHttpType: 'get',
            httpTypes: ['GET', 'PUT', 'POST', 'DELETE']
        });
	}]);

	newsManagementApp.filter('newslines', function() {
		return function(text) {
			return text.replaceAll('\\n', '<br>');
		}
	});

	newsManagementApp.controller('authenticationController', ['$http', '$location', '$rootScope', '$scope', 'ipCookie', function($http, $location, $rootScope, $scope, ipCookie) {
		$rootScope.authenticated = false;
		$http.get('/news-admin/username').then(function(response) {
			$rootScope.userName = response.data;
			if (!($rootScope.userName === '')) {
				$rootScope.authenticated = true;
				if ($location.path() === '/' || $location.path().startsWith('/?') ||
					$location.path() === '/login' || $location.path().startsWith('/login?'))
					$location.path('/news-list');
			}
			else {
				if ($location.path() === '/news-list')
					$location.path('/login');
			}
		});

		$scope.logout = function() {
			$rootScope.loaded = false;
			$http.post('/news-admin/j_spring_security_logout', '_csrf='+ipCookie('XSRF-TOKEN'),
				{
				    headers:
				    {
				        'Content-Type': 'application/x-www-form-urlencoded'
				    }
				}).then(function() {
					$rootScope.authenticated = false;
					$rootScope.logoutMessage = true;
					$location.path('/login').search('logout');
			});
		};
	}]);

	/*newsManagementApp.controller('rootController', ['$http', '$rootScope', '$scope', function($http, $rootScope, $scope) {
		var self = this;
		$scope.locales = {
			'English': 'en-us',
			'Russian': 'ru-ru'
		};

		$scope.safeApply = function() {
			if (!$scope.$$phase) {
				console.log('asdasd');
				$scope.$apply();
				console.log('asdasd');
			}
		}

		$scope.changeLocale = function(language) {
			var locale = $scope.locales[language];
			i18nAdapter.setLocale(locale, ['/news-admin/resources/js/i18n/' + locale + '/localization.json'])
				.then($scope.safeApply);
		};

		$scope.logout = function() {

		};
	}]);*/

	newsManagementApp.controller('errorController', ['$http', '$rootScope', '$location', function($http, $rootScope, $location) {
		$rootScope.loaded = false;
		$http.get($location.path()).then(function(response) {
			$rootScope.errorMessage = response.data;
	  		$rootScope.loaded = true;
		});
	}]);

	newsManagementApp.controller('loginController', ['$http', '$rootScope', '$location', '$scope', function($http, $rootScope, $location, $scope) {
		$rootScope.loaded = false;
		if (!$rootScope.authenticated) {
			var logoutMessage = $location.search().logout;
			if (!(logoutMessage === 'undefined')) {
				$rootScope.logoutMessage = logoutMessage;
			}
			var loginError = $location.search().error;
			if (!(loginError === 'undefined')) {
				$rootScope.loginError = loginError;
			}
		}
		$scope.credentials = {
			'login': '',
			'password': ''
		};

		$scope.authenticate = function() {
			$rootScope.loaded = false;

			var data = 'login='+$scope.credentials.login+'&password='+$scope.credentials.password;

			$http.post('/news-admin/j_spring_security_check',
					data,
					{
					    headers:
					    {
					        'Content-Type': 'application/x-www-form-urlencoded',
					    }
					})
			.then(
				function(response) {
					if (response.status == 403) {
						delete $rootScope.logoutMessage;
						$location.search('logout', null);
						if (response.data === 'invalid') {
							$location.path('/login').search('error');
						}
						else {
							$location.search('error', null);
							delete $rootScope.loginError;
							$location.path('/403');
						}
	  					$rootScope.loaded = true;
					}
					else {
						$location.search('error', null);
						delete $rootScope.loginError;
						$http.get('/news-admin/username').then(function(response) {
							$rootScope.userName = response.data;
							$rootScope.authenticated = true;
							$location.path('/news-list');
						});
					}
				});
	  	};

	  	$rootScope.loaded = true;
	}]);

	newsManagementApp.controller('authorsController', ['$http', '$rootScope', '$scope', 'AuthorsService', function($http, $rootScope, $scope, AuthorsService) {
		$rootScope.loaded = false;
		$scope.authors = [];
		$scope.authorAdded = false;
		$scope.authorUpdated = false;
		$scope.authorExpired = false;
		$scope.authorExists = false;
		$scope.invalidAuthor = false;

		$('#authors-link').css('font-weight', 'bold');

		AuthorsService.getAll().then(function(response) {
			$scope.authors = response.data;
			$scope.loaded = true;
		});

		$scope.showEditBar = function(event) {
			$(event.target).parent().hide();
		    $($(event.target).parent().parent().prev().children()[0]).removeAttr('disabled');
		    $(event.target).parent().prev().show();
		};

		$scope.hideEditBar = function(event) {
			$(event.target).parent().parent().hide();
		    $(event.target).parent().parent().next().show();
		    $($(event.target).parent().parent().parent().prev().children()[0]).attr('disabled', true);
		};

		$scope.updateScope = function() {
			if (!$scope.$$phase) {
            	$scope.$apply();
            }
		}

		$scope.add = function(event) {
		    var newAuthorName = $($(event.target).parent().parent().prev().children()[0]).val();
		    if (!newAuthorName) {
		        $("#content").animate({ scrollTop: 0 }, "slow", function() {
	                $scope.invalidAuthor = true;
		        });
		        return;
		    }

            $scope.invalidAuthor = false;

		    var newAuthor = { authorName: newAuthorName };

		    AuthorsService.add(newAuthor).then(function(response) {
		    	var newAuthorId = response.data;
		    	if (newAuthorId) {
                    $scope.authorExists = false;
	                newAuthor.authorId = newAuthorId;
	                $scope.authors.push(newAuthor);
	                $("#content").animate({ scrollTop: 0 }, "slow", function() {
                    	$scope.authorAdded = true;
                        $scope.updateScope();
                        setTimeout(function() {
                        	$scope.authorAdded = false;
	                        $scope.updateScope();
                        }, 3000);
	                });
	            }
	            else {
                    $scope.authorExists = true;
                    $scope.updateScope();
	            }
		    });
		};

		$scope.update = function(event) {
		    var newAuthorName = $($(event.target).parent().parent().parent().prev().children()[0]).val();
		    if (!newAuthorName) {
		        $("#content").animate({ scrollTop: 0 }, "slow", function() {
	                $scope.invalidAuthor = true;
		        });
		        return;
		    }

            $scope.invalidAuthor = false;
		    var authorId = parseInt($(event.target).parent().parent().parent().parent().attr('id'));

		    var author = { authorId: authorId, authorName: newAuthorName };

		    AuthorsService.update(author).then(function(response) {
		    	var exists = response.data;
		    	if (!exists) {
                    $scope.authorExists = false;
	                $("#content").animate({ scrollTop: 0 }, "slow", function() {
                        $scope.authorUpdated = true;
                        $scope.updateScope();
                        setTimeout(function() {
                        	$scope.authorUpdated = false;
	                        $scope.updateScope();
                        }, 3000);
	                });
	            }
	            else
	            	$scope.authorExists = true;
		    });
		};

		$scope.expire = function(event) {
		    $scope.authorExists = false;
		    $scope.invalidAuthor = false;
		    var authorId = parseInt($(event.target).parent().parent().parent().parent().attr('id'));

		    AuthorsService.expire(authorId).then(function() {
		    	$(event.target).parent().toggle("slide", 500, function() {
	                $(this).remove();
	                $scope.authorExpired = true;
                    $scope.updateScope();
                    setTimeout(function() {
                    	$scope.authorExpired = false;
                        $scope.updateScope();
                    }, 3000);
	            });
		    });
		};
	}]);

	newsManagementApp.controller('newsListController', ['$filter', '$locale', '$rootScope', '$scope', '$templateRequest', '$compile', '$location', 'DropdownService', 'NewsService', function($filter, $locale, $rootScope, $scope, $templateRequest, $compile, $location, DropdownService, NewsService) {
		$rootScope.loaded = false;
		$scope.localeCode = $locale.id;
		$scope.searchCriteria = null;
		$scope.notExpiredAuthors = [];
		$scope.tags = [];
		$scope.newsList = [];
		$scope.authorsInStringByNewsId = {};
		$scope.tagsInStringByNewsId = {};
		$scope.lastEditDateByNewsId = {};
		$scope.commentsCountByNewsId = {};
		$scope.pagesCount = 0;
		$scope.activePagesCount = 0;
		$scope.activePageIndex = 0;
		$scope.firstValue = 1;

		$('#news-list-link').css('font-weight', 'bold');

		DropdownService.getAuthorsAndTags().then(function(response) {
			$scope.unpackAuthorsAndTagsInfo(response.data);
			$scope.initDropdownOptions();
			NewsService.reset().then(function(response) {
				$scope.unpackNewsListInfo(response.data);
				$scope.loadTemplates();
			});
		});

		$scope.unpackAuthorsAndTagsInfo = function(authorsAndTagsInfo) {
			$scope.notExpiredAuthors = authorsAndTagsInfo.notExpiredAuthors;
			$scope.tags = authorsAndTagsInfo.tags;
		};

		$scope.unpackNewsListInfo = function(newsListInfo) {
			$scope.newsList = newsListInfo.newsList;
			$scope.commentsCountByNewsId = newsListInfo.commentsCountByNewsId;
			$scope.pagesCount = newsListInfo.pagesCount;
			$scope.activePagesCount = $scope.pagesCount > 5 ? 5 : $scope.pagesCount;
			for (var i = 0; i < $scope.newsList.length; i++) {
				var news = $scope.newsList[i];
				var authorsByNewsId = newsListInfo.authorsByNewsId;
				var authorsString = ('(' + $filter('translate')('news_list.by') + ' {0})').format(authorsByNewsId[news.newsId]
					.map(function(author) { return author.authorName; }).join(', '));
				$scope.authorsInStringByNewsId[news.newsId] = authorsString;
				var tagsByNewsId = newsListInfo.tagsByNewsId;
				var tagsString = tagsByNewsId[news.newsId]
					.map(function(tag) { return tag.tagName; }).join(', ');
				$scope.tagsInStringByNewsId[news.newsId] = tagsString;
				var commentsCount = newsListInfo.commentsCountByNewsId[news.newsId];
				var lastEditDate = news.modificationDate ? news.modificationDate : news.creationDate;
				lastEditDate = new Date(lastEditDate);
				lastEditDate = lastEditDate.toLocaleString($scope.localeCode.substring(0, 2), {
					year: 'numeric',
					month: 'long',
					day: 'numeric',
				});
				$scope.lastEditDateByNewsId[news.newsId] = lastEditDate;
			}
		}

		$scope.initDropdownOptions = function() {
			for (var i = 0; i < $scope.notExpiredAuthors.length; i++) {
				 $('#authors')
	         		.append($("<option></option>")
	                    	.attr("value", $scope.notExpiredAuthors[i].authorId)
		                    .text($scope.notExpiredAuthors[i].authorName));
			}
			$('#authors').multiselect('refresh');
			for (var i = 0; i < $scope.tags.length; i++) {
				 $('#tags')
	         		.append($("<option></option>")
	                    	.attr("value", $scope.tags[i].tagId)
		                    .text($scope.tags[i].tagName));
			}
			$('#tags').multiselect('refresh');
		};

		$scope.loadTemplates = function() {
			$templateRequest('/news-admin/resources/pages/news-list-template.html').then(function(newsListTemplate) {
				$scope.newsListTemplate = newsListTemplate;
				$templateRequest('/news-admin/resources/pages/pagination-row-template.html').then(function(paginationRowTemplate) {
					$scope.paginationRowTemplate = paginationRowTemplate;
			        $rootScope.loaded = true;
			    });
		    });
		};

		$scope.initAuthorsDropdown = function() {
			$("#authors").multiselect({
				header: $filter('translate')('news_list.authors_prompt'),
				noneSelectedText: $filter('translate')('news_list.authors_prompt'),
				selectedText: '# ' + $filter('translate')('news_list.selected')
			});
		};

		$scope.initTagsDropdown = function() {
			$("#tags").multiselect({
				header: $filter('translate')('news_list.tags_prompt'),
				noneSelectedText: $filter('translate')('news_list.tags_prompt'),
				selectedText: '# ' + $filter('translate')('news_list.selected')
			});
		}

		$scope.updateActivePageIndex = function(pageIndex) {
			if ($('#no-news-found-message').length && $scope.newsList.length == 0)
				return;

			var pageLinks = $('.pagination > li > a');
			var firstPageLink = pageLinks[2];
			var currentFirstPageLinkValue = parseInt($(firstPageLink).text());
			var lastPageLink = pageLinks[pageLinks.length - 3];
			var currentLastPageLinkValue = parseInt($(lastPageLink).text());
			var currentActivePageLinkValue = parseInt($('.active').text());

			var firstValue = isNaN(currentFirstPageLinkValue) ? 1 : currentFirstPageLinkValue;
			if (!isNaN(currentFirstPageLinkValue) && pageIndex == currentFirstPageLinkValue - 1)
				firstValue = currentFirstPageLinkValue - 1;
			else if (!isNaN(currentFirstPageLinkValue) && pageIndex < currentFirstPageLinkValue)
				firstValue = 1;
			else if (!isNaN(currentLastPageLinkValue) && pageIndex == currentLastPageLinkValue + 1)
				firstValue = currentFirstPageLinkValue + 1;
			else if (!isNaN(currentLastPageLinkValue) && (pageIndex > currentLastPageLinkValue || currentActivePageLinkValue == $scope.activePagesCount+1))
				firstValue = pageIndex - $scope.activePagesCount + 1;
			$scope.firstValue = firstValue;
			$scope.activePageIndex = pageIndex - firstValue;
		};

		$scope.fillSearchCriteria = function() {
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
			$scope.searchCriteria = {
				authorIds : checkedAuthorsIds,
				tagIds : checkedTagsIds,
				pageIndex : 1
			};
		};

		$scope.reset = function() {
			if (!$rootScope.loaded)
				return;
			$rootScope.loaded = false;

			NewsService.reset().then(function(response) {
				$scope.unpackNewsListInfo(response.data);
				$scope.searchCriteria = null;
				$('#authors').multiselect('uncheckAll');
				$('#tags').multiselect('uncheckAll');
				$compile($("#news-list").html($scope.newsListTemplate).contents())($scope);
				$scope.updateActivePageIndex(1);
				$compile($("#pagination-row").html($scope.paginationRowTemplate).contents())($scope);
				$rootScope.loaded = true;
			});
		};

		$scope.filter = function() {
			if (!$rootScope.loaded)
				return;
			$rootScope.loaded = false;

			$scope.fillSearchCriteria();

			NewsService.filter($scope.searchCriteria).then(function(response) {
				$scope.unpackNewsListInfo(response.data);
				$compile($("#news-list").html($scope.newsListTemplate).contents())($scope);
				$scope.updateActivePageIndex(1);
				$compile($("#pagination-row").html($scope.paginationRowTemplate).contents())($scope);
				$rootScope.loaded = true;
			});
		};

		$scope.page = function(event) {
			if ($(event.target).hasClass('disabled-page-arrow') || !$rootScope.loaded)
				return;
			$rootScope.loaded = false;

			var pageIndex = parseInt($(event.target).text());
			if (!$scope.searchCriteria) {
				$scope.fillSearchCriteria();
				$scope.searchCriteria.pageIndex = pageIndex;
			}
			else
				$scope.searchCriteria.pageIndex = pageIndex;

			NewsService.page($scope.searchCriteria).then(function(response) {
				$('.pagination > li > a').removeClass('active');
				$(event.target).addClass('active');

				var newsListInfo = response.data;
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

				$scope.unpackNewsListInfo(newsListInfo);
				$rootScope.loaded = true;
			});
		};

		$scope.firstPage = function(event) {
			if ($(event.target).hasClass('disabled-page-arrow')|| !$rootScope.loaded)
				return;
			$rootScope.loaded = false;

			if (!$scope.searchCriteria) {
				$scope.fillSearchCriteria();
			}
			else
				$scope.searchCriteria.pageIndex = 1;

			NewsService.page($scope.searchCriteria).then(function(response) {
				$scope.unpackNewsListInfo(response.data);
				$scope.updateActivePageIndex(1);
				$compile($("#pagination-row").html($scope.paginationRowTemplate).contents())($scope);
				$rootScope.loaded = true;
			});

		};

		$scope.lastPage = function(event) {
			if ($(event.target).hasClass('disabled-page-arrow')|| !$rootScope.loaded)
				return;
			$rootScope.loaded = false;

			if (!$scope.searchCriteria) {
				$scope.fillSearchCriteria();
			}
			$scope.searchCriteria.pageIndex = null;

			NewsService.page($scope.searchCriteria).then(function(response) {
				$scope.unpackNewsListInfo(response.data);
				$scope.updateActivePageIndex(response.data.pagesCount);
				$compile($("#pagination-row").html($scope.paginationRowTemplate).contents())($scope);
				$rootScope.loaded = true;
			});
		};

		$scope.previousPage = function(event) {
			if ($(event.target).hasClass('disabled-page-arrow') || !$rootScope.loaded)
				return;
			$rootScope.loaded = false;

			var pageIndex = -1;
			var childs = $('.pagination > li > a');
			for (var i = 0; i < childs.length; i++) {
				if ($(childs[i]).hasClass('active'))
					pageIndex = parseInt($(childs[i]).text()) - 1;
			}

			if (!$scope.searchCriteria) {
				$scope.fillSearchCriteria();
			}
			$scope.searchCriteria.pageIndex = pageIndex;

			NewsService.page($scope.searchCriteria).then(function(response) {
				$scope.unpackNewsListInfo(response.data);
				$scope.updateActivePageIndex(pageIndex);
				$compile($("#pagination-row").html($scope.paginationRowTemplate).contents())($scope);
				$rootScope.loaded = true;
			});
		};

		$scope.nextPage = function() {
			if ($(event.target).hasClass('disabled-page-arrow') || !$rootScope.loaded)
				return;
			$rootScope.loaded = false;

		    var pageIndex = -1;
		    var childs = $('.pagination > li > a');
		    for (var i = 0; i < childs.length; i++) {
		        if ($(childs[i]).hasClass('active'))
		            pageIndex = parseInt($(childs[i]).text()) + 1;
		    }

		    if (!$scope.searchCriteria) {
		        $scope.fillSearchCriteria();
		    }
		    $scope.searchCriteria.pageIndex = pageIndex;


			NewsService.page($scope.searchCriteria).then(function(response) {
				$scope.unpackNewsListInfo(response.data);
				$scope.updateActivePageIndex(pageIndex);
				$compile($("#pagination-row").html($scope.paginationRowTemplate).contents())($scope);
				$rootScope.loaded = true;
			});
		};

		$scope.goToNews = function(event) {
			$rootScope.loaded = false;
			var newsId = $(event.target).parent().parent().parent().attr('id');
			var newsUrl = '/news/' + newsId;
			if ($scope.searchCriteria && ($scope.searchCriteria != null)) {
				var searchCriteriaInString = encodeURIComponent(JSON.stringify($scope.searchCriteria));
				$location.path(newsUrl).search({ searchCriteria: searchCriteriaInString });
			}
			else
				$location.path(newsUrl);
		}

		$scope.deleteNews = function() {
			if ($(this).hasClass('disabled-page-arrow') || !$rootScope.loaded)
		        return;

		    $rootScope.loaded = false;

			var newsIds = [];
			var checkboxes = $('.short-news > .short-news-footer-row > .short-news-others > input');
			for (var i = 0; i < checkboxes.length; i++) {
				var checkbox = checkboxes[i];
				if (checkboxes[i].checked)
					newsIds.push(parseInt(checkbox.id));
			}

			if (newsIds.length == 0)
				return;

			var pageIndex = -1;
		    var childs = $('.pagination > li > a');
		    for (var i = 0; i < childs.length; i++) {
		        if ($(childs[i]).hasClass('active'))
		            pageIndex = parseInt($(childs[i]).text());
		    }

			if (!$scope.searchCriteria) {
				$scope.fillSearchCriteria();
			}
			$scope.searchCriteria.pageIndex = pageIndex;
			$scope.searchCriteria.pageSize = 5;

			NewsService.deleteNews(newsIds, $scope.searchCriteria).then(function(response) {
				$scope.unpackNewsListInfo(response.data);
				var newsCount = checkboxes.length;
				var remainedNewsCount = newsCount - newsIds.length;
				if (remainedNewsCount == 0 && $scope.activePageIndex > 1)
					$scope.activePageIndex -= 1;
				$scope.updateActivePageIndex(pageIndex);
				$rootScope.loaded = true;
			});
		};
	}]);

	newsManagementApp.controller('newsController', ['$rootScope', '$scope', '$routeParams', '$filter', '$locale', 'NewsService', 'CommentService', function($rootScope, $scope, $routeParams, $filter, $locale, NewsService, CommentService) {
		$rootScope.loaded = false;
		$scope.localeCode = $locale.id;
		$scope.news = { newsId: 0, title: '', shortText: '', fullText: '', creationDate: 0, modificationDate: 0 };
		$scope.authorsInString = '';
		$scope.lastEditDate = '';
		$scope.comments = [];

		NewsService.news($routeParams.newsId, $routeParams.searchCriteria).then(function(response) {
			var newsInfo = response.data;
			$scope.news = newsInfo.news;
			$scope.authorsInString = ('(' + $filter('translate')('news_list.by') + ' {0})').format(newsInfo.authors
				.map(function(author) { return author.authorName; }).join(', '));
			var lastEditDate = newsInfo.news.modificationDate ? newsInfo.news.modificationDate : newsInfo.news.creationDate;
            lastEditDate = new Date(lastEditDate);
            $scope.lastEditDate = lastEditDate.toLocaleString($scope.localeCode.substring(0, 2), {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
            });
            $scope.comments = newsInfo.comments;
            for (var i = 0; i > $scope.comments.length; i++) {
            	var comment = $scope.comments[i];
            	var creationDate = new Date(comment.creationDate);
                comment.creationDate = lastEditDate.toLocaleString($scope.localeCode.substring(0, 2), {
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric',
                });
            }
            if (newsInfo.previousId) {
				var previousNewsLink = '/news-admin/news/' + newsInfo.previousId;
				if ($routeParams.searchCriteria) {
					previousNewsLink += '?searchCriteria=' + $routeParams.searchCriteria;
				}
            	$scope.previousNewsLink = previousNewsLink;
            }
            if (newsInfo.nextId) {
				var nextNewsLink = '/news-admin/news/' + newsInfo.nextId;
				if ($routeParams.searchCriteria) {
					nextNewsLink += '?searchCriteria=' + $routeParams.searchCriteria;
				}
            	$scope.nextNewsLink = nextNewsLink;
            }
            $rootScope.loaded = true;
		});

		$scope.comment = function() {
			var commentText = $('#new-post-textarea').val().replaceAll('\n', '\\n');
		    if (!commentText || commentText == '') {
		        $('#new-post-textarea').addClass('invalid-comment-text');
		        return;
		    }
			var newsId = $scope.news.newsId;

		    CommentService.add(newsId, commentText).then(function(response) {
		    	var comment = response.data;
		    	var creationDate = new Date(comment.creationDate);
	            creationDate = creationDate.toLocaleString($scope.localeCode.substring(0, 2), {
	                year: 'numeric',
	                month: 'long',
	                day: 'numeric',
	            });
	            comment.creationDate = creationDate;
	            $scope.comments.push(comment);
	            $('#new-post-textarea').val('');
		    });
		}

		$scope.deleteComment = function(event) {
			var commentId = event.target.id;
			CommentService.delete(commentId).then(function(response) {
				var comment = $(event.target).parent().parent().parent();
	            $(comment).toggle("slide", 500, function() { $(this).remove(); });
			});
		}
	}]);

	newsManagementApp.controller('tagsController', ['$http', '$rootScope', '$scope', 'TagsService', function($http, $rootScope, $scope, TagsService) {
		$rootScope.loaded = false;
		$scope.tags = [];
		$scope.tagAdded = false;
		$scope.tagUpdated = false;
		$scope.tagDeleted = false;
		$scope.tagExists = false;
		$scope.invalidTag = false;

		$('#tags-link').css('font-weight', 'bold');

		TagsService.getAll().then(function(response) {
			$scope.tags = response.data;
			$scope.loaded = true;
		});

		$scope.showEditBar = function(event) {
			$(event.target).parent().hide();
		    $($(event.target).parent().parent().prev().children()[0]).removeAttr('disabled');
		    $(event.target).parent().prev().show();
		};

		$scope.hideEditBar = function(event) {
			$(event.target).parent().parent().hide();
		    $(event.target).parent().parent().next().show();
		    $($(event.target).parent().parent().parent().prev().children()[0]).attr('disabled', true);
		};

		$scope.updateScope = function() {
			if (!$scope.$$phase) {
            	$scope.$apply();
            }
		}

		$scope.add = function(event) {
		    var newTagName = $($(event.target).parent().parent().prev().children()[0]).val();
		    if (!newTagName) {
		        $("#content").animate({ scrollTop: 0 }, "slow", function() {
	                $scope.invalidTag = true;
		        });
		        return;
		    }

            $scope.invalidTag = false;

		    var newTag = { tagName: newTagName };

		    TagsService.add(newTag).then(function(response) {
		    	var newTagId = response.data;
		    	if (newTagId) {
                    $scope.tagExists = false;
	                newTag.tagId = newTagId;
	                $scope.tags.push(newTag);
	                $("#content").animate({ scrollTop: 0 }, "slow", function() {
                    	$scope.tagAdded = true;
                        $scope.updateScope();
                        setTimeout(function() {
                        	$scope.tagAdded = false;
	                        $scope.updateScope();
                        }, 3000);
	                });
	            }
	            else {
                    $scope.tagExists = true;
                    $scope.updateScope();
	            }
		    });
		};

		$scope.update = function(event) {
		    var newTagName = $($(event.target).parent().parent().parent().prev().children()[0]).val();
		    if (!newTagName) {
		        $("#content").animate({ scrollTop: 0 }, "slow", function() {
	                $scope.invalidTag = true;
		        });
		        return;
		    }

            $scope.invalidTag = false;
		    var tagId = parseInt($(event.target).parent().parent().parent().parent().attr('id'));

		    var tag = { tagId: tagId, tagName: newTagName };

		    TagsService.update(tag).then(function(response) {
		    	var exists = response.data;
		    	if (!exists) {
                    $scope.tagExists = false;
	                $("#content").animate({ scrollTop: 0 }, "slow", function() {
                        $scope.tagUpdated = true;
                        $scope.updateScope();
                        setTimeout(function() {
                        	$scope.tagUpdated = false;
	                        $scope.updateScope();
                        }, 3000);
	                });
	            }
	            else
	            	$scope.tagExists = true;
		    });
		};

		$scope.delete = function(event) {
		    $scope.tagExists = false;
		    $scope.invalidTag = false;
		    var tagId = parseInt($(event.target).parent().parent().parent().parent().attr('id'));

		    TagsService.delete(tagId).then(function() {
		    	$('#'+tagId).toggle("slide", 500, function() {
	                $(this).remove();
	                $scope.tagDeleted = true;
                    $scope.updateScope();
                    setTimeout(function() {
                    	$scope.tagDeleted = false;
                        $scope.updateScope();
                    }, 3000);
	            });
		    });
		};
	}]);

	newsManagementApp.controller('newsEditController', ['$rootScope', '$scope', '$routeParams', '$filter', '$locale', '$location', 'NewsService', 'DropdownService', function($rootScope, $scope, $routeParams, $filter, $locale, $location, NewsService, DropdownService) {
		$rootScope.loaded = false;
		$scope.localeCode = $locale.id;
		$scope.news = { newsId: 0, title: '', shortText: '', fullText: '', creationDate: 0, modificationDate: 0 };
		$scope.newsAuthors = [];
		$scope.newsTags = [];
		$scope.now = 0;
		$scope.newsDate = 0;
		$scope.notExpiredAuthors = [];
		$scope.tags = [];
		$scope.invalidNews = false;
		$scope.noAuthorSelected = false;
		$scope.newsSaved = false;
		$scope.concurrentModification = false;

		if ($location.path() == '/add')
			$('#add-news-link').css('font-weight', 'bold');

		DropdownService.getAuthorsAndTags().then(function(response) {
			var authorsAndTagsInfo = response.data;
			$scope.notExpiredAuthors = authorsAndTagsInfo.notExpiredAuthors;
			$scope.tags = authorsAndTagsInfo.tags;
			var now = new Date();
            $scope.now = (Math.ceil(now/1000)-1)*1000;
            $scope.newsDate = now.toLocaleString($scope.localeCode.substring(0, 2), {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
            });
			if ($routeParams.newsId) {
				NewsService.newsEdit($routeParams.newsId).then(function(response) {
					var newsEditInfo = response.data;
					$scope.news = newsEditInfo.news;
					$scope.newsDate = new Date($scope.news.modificationDate.length ? $scope.news.modificationDate : $scope.news.creationDate);
					$scope.newsDate = $scope.newsDate.toLocaleString($scope.localeCode.substring(0, 2), {
		                year: 'numeric',
		                month: 'long',
		                day: 'numeric',
		            });
					$scope.newsAuthors = newsEditInfo.authors;
					$scope.newsTags = newsEditInfo.tags;
					$scope.initDropdownOptions();
					$rootScope.loaded = true;
				});
			}
			else {
				$scope.initDropdownOptions();
				$rootScope.loaded = true;
			}
		});

		$scope.initAuthorsDropdown = function() {
			$("#authors").multiselect({
				header: $filter('translate')('news_list.authors_prompt'),
				noneSelectedText: $filter('translate')('news_list.authors_prompt'),
				selectedText: '# ' + $filter('translate')('news_list.selected')
			});
		};

		$scope.initTagsDropdown = function() {
			$("#tags").multiselect({
				header: $filter('translate')('news_list.tags_prompt'),
				noneSelectedText: $filter('translate')('news_list.tags_prompt'),
				selectedText: '# ' + $filter('translate')('news_list.selected')
			});
		}

		$scope.initDropdownOptions = function() {
			for (var i = 0; i < $scope.notExpiredAuthors.length; i++) {
				$('#authors')
	         		.append($("<option></option>")
	                    	.attr("value", $scope.notExpiredAuthors[i].authorId)
		                    .text($scope.notExpiredAuthors[i].authorName));
			}
			$('#authors').multiselect('refresh');
			for (var i = 0; i < $scope.notExpiredAuthors.length; i++) {
				var author = $scope.notExpiredAuthors[i];
		        for (var j = 0; j < $scope.newsAuthors.length; j++)
		        	if (author.authorId === $scope.newsAuthors[j].authorId) {
		        		$("#authors").multiselect("widget").find(":checkbox[value="+author.authorId+"]").each(function() {
						    this.click();
						});
		        	}
			}
			for (var i = 0; i < $scope.tags.length; i++) {
				$('#tags')
	         		.append($("<option></option>")
	                    	.attr("value", $scope.tags[i].tagId)
		                    .text($scope.tags[i].tagName));
			}
			$('#tags').multiselect('refresh');
			for (var i = 0; i < $scope.tags.length; i++) {
				var tag = $scope.tags[i];
		        for (var j = 0; j < $scope.newsTags.length; j++)
		        	if (tag.tagId === $scope.newsTags[j].tagId) {
		        		$("#tags").multiselect("widget").find(":checkbox[value="+tag.tagId+"]").each(function() {
						    this.click();
						});
		        	}
			}
		};

		$scope.updateScope = function() {
			if (!$scope.$$phase) {
            	$scope.$apply();
            }
		};

		$scope.save = function() {
			var title = $('#new-news-textarea').val();
		    var shortText = $('#short-text-textarea').val();
		    var fullText = $('#text-textarea').val();

		    if (title == '' || shortText == '' || fullText == '') {
		        if (!$scope.invalidNews)
		            $scope.invalidNews = true;
		        return;
		    }

		    if ($scope.invalidNews)
	            $scope.invalidNews = false;

		    var checkedAuthors = $('#authors').multiselect('getChecked');
		    var checkedAuthorsIds = [];
		    for (var i = 0; i < checkedAuthors.length; i++) {
		        var checkedAuthorId = $(checkedAuthors[i]).val();
		        checkedAuthorsIds.push(parseInt(checkedAuthorId));
		    }

		    if (checkedAuthorsIds.length == 0) {
		        if (!$scope.noAuthorSelected)
		            $scope.noAuthorSelected = true;
		        return;
		    }

		    if ($scope.noAuthorSelected)
	            $scope.noAuthorSelected = false;

		    var checkedTags = $('#tags').multiselect('getChecked');
		    var checkedTagsIds = [];
		    for (var i = 0; i < checkedTags.length; i++) {
		        var checkedTagId = $(checkedTags[i]).val();
		        checkedTagsIds.push(parseInt(checkedTagId));
		    }

		    var news = {
		        newsId: $scope.news.newsId,
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

		    NewsService.save(news, authors, tags).then(function(response) {
		    	if (response.status == 409) {
		    		$scope.concurrentModification = true;
                    setTimeout(function() {
                    	$scope.concurrentModification = false;
                    }, 3000);
		    	}
		    	else {
		    		var newNews = response.data;
		    		$scope.newsSaved = true;
                    setTimeout(function() {
                    	$scope.newsSaved = false;
                    }, 3000);
		            if (newNews) {
		                $('.new-news').attr('id', newNews.newsId);
		                $('#modification-date-in-milliseconds').val((Math.ceil(newNews.modificationDate/1000)-1)*1000);
		                var lastEditDate = new Date(newNews.modificationDate);
		                lastEditDate = lastEditDate.toLocaleString($scope.localeCode.substring(0, 2), {
		                    year: 'numeric',
		                    month: 'long',
		                    day: 'numeric',
		                });
		                $('#date-textarea').val(lastEditDate);
		            }
		    	}
		    });
		};
	}]);

	newsManagementApp.factory('AuthorsService', ['$http', function($http) {
		return {
			add: function(author) {
				return $http.put('/news-admin/authors/add', JSON.stringify(author));
			},

			update: function(author) {
				return $http.put('/news-admin/authors/update', JSON.stringify(author));
			},

			expire: function(authorId) {
				return $http.post('/news-admin/authors/expire/' + authorId);
			},

			getAll: function() {
				return $http.get('/news-admin/authors/all');
			}
		};
	}]);

	newsManagementApp.factory('CommentService', ['$http', function($http) {
		return {
			add: function(newsId, commentText) {
				return $http.put('/news-admin/comment/add', JSON.stringify({ newsId: newsId, commentText: commentText }));
			},

			delete: function(commentId) {
				return $http.post('/news-admin/comment/delete/' + commentId);
			}
		};
	}]);

	newsManagementApp.factory('DropdownService', ['$http', function($http) {
		return {
			getAuthorsAndTags: function() {
				return $http.get('/news-admin/authors-and-tags/all');
			}
		};
	}]);

	newsManagementApp.factory('NewsService', ['$http', function($http) {
		return {
			reset: function() {
				return $http.get('/news-admin/news/reset');
			},

			filter: function(searchCriteria) {
				return $http.get('/news-admin/news/filter?searchCriteria=' + encodeURIComponent(JSON.stringify(searchCriteria)));
			},

			page: function(searchCriteria) {
				return $http.get('/news-admin/news/page?searchCriteria=' + encodeURIComponent(JSON.stringify(searchCriteria)));
			},

			news: function(newsId, searchCriteriaInString) {
				var url = '/news-admin/news?newsId=' + newsId;
				if (searchCriteriaInString) {
					url += '&searchCriteria=' + searchCriteriaInString;
				}
				return $http.get(url);
			},

			newsEdit: function(newsId) {
				return $http.get('/news-admin/news/edit?newsId=' + newsId);
			},

			deleteNews: function(newsIds, searchCriteria) {
				return $http.post('/news-admin/news/delete',
						JSON.stringify({
							newsIds: newsIds,
							searchCriteria: searchCriteria
						})
					);
			},

			save: function(news, authors, tags) {
				return $http.put('/news-admin/news/save',
					JSON.stringify({
							news: news,
							authors: authors,
							tags: tags
						})
					);
			}
		};
	}]);

	newsManagementApp.factory('TagsService', ['$http', function($http) {
		return {
			add: function(tag) {
				return $http.put('/news-admin/tags/add', JSON.stringify(tag));
			},

			update: function(tag) {
				return $http.put('/news-admin/tags/update', JSON.stringify(tag));
			},

			delete: function(tagId) {
				return $http.post('/news-admin/tags/delete/' + tagId);
			},

			getAll: function() {
				return $http.get('/news-admin/tags/all');
			}
		};
	}]);
}());
