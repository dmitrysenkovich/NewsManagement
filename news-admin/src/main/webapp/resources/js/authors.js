$('#authors-link').css('font-weight', 'bold');


String.prototype.format = function() {
    var formatted = this;
    for(var arg in arguments) {
        formatted = formatted.replace("{" + arg + "}", arguments[arg]);
    }
    return formatted;
};


processing = false;


var header = $("meta[name='_csrf_header']").attr("content");
var token = $("meta[name='_csrf']").attr("content");


var newAuthorRowTemplate = "<div id='{0}' class='item'>" +
    "                       <div class='item-label'>Author:</div>" +
    "                       <div class='item-name'><input class='item-name-textarea' type='text' value='{1}' disabled></div>" +
    "                       <div class='item-action'>" +
    "                           <div class='item-update-links' hidden>" +
    "                               <a href='javascript:void(0)' class='update-author-link'><u>update</u></a>" +
    "                               <a href='javascript:void(0)' class='expire-author-link'><u>expire</u></a>" +
    "                               <a href='javascript:void(0)' class='cancel-author-link'><u>cancel</u></a>" +
    "                           </div>" +
    "                           <a href='javascript:void(0)' class='edit-author-link'><u>edit</u></a>" +
    "                       </div>" +
    "                   </div>";


$(document).on('click', '.edit-author-link', function (event) {
    $(event.target).parent().hide();
    $($(event.target).parent().parent().prev().children()[0]).removeAttr('disabled');
    $(event.target).parent().prev().show();
});


$(document).on('click', '.cancel-author-link', function (event) {
    $(event.target).parent().parent().hide();
    $(event.target).parent().parent().next().show();
    $($(event.target).parent().parent().parent().prev().children()[0]).attr('disabled', true);
});


$(document).on('click', '.add-author-link', function (event) {
    if (processing)
        return;

    processing = true;

    var newAuthorName = $($(event.target).parent().parent().prev().children()[0]).val();
    if (!newAuthorName) {
        $("#content").animate({ scrollTop: 0 }, "slow", function() {
            if (!$('#invalid-author-name').length) {
                $("<div id='invalid-author-name'>Invalid author name</div>").prependTo($('#items-list')).slideDown('slow', function () {
                    processing = false;
                });
            }
            else
                processing = false;
        });
        return;
    }

    $('#invalid-author-name').toggle("slide", 500, function() { $(this).remove(); });

    $.ajax({
        url: '/news-management/authors/add',
        type: 'POST',
        data: JSON.stringify({
            authorName: newAuthorName
        }),
        contentType: "application/json",
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success: function(newAuthorId) {
            if (newAuthorId) {
                if ($('#item-exists').length)
                    $('#item-exists').toggle("slide", 500, function() { $(this).remove(); });
                var newAuthorRow = newAuthorRowTemplate.format(newAuthorId, newAuthorName);
                $(newAuthorRow).appendTo($('#items-list')).slideDown('fast');
                $("#content").animate({ scrollTop: 0 }, "slow", function() {
                    $("<div id='item-added'>Successfully added author</div>").prependTo($('#items-list')).slideDown("fast");
                    $('#item-added').delay(3000).fadeOut(function () {
                        $(this).remove();
                        processing = false;
                    });
                });
            }
            else if (!$('#item-exists').length) {
                $("<div id='item-exists'>Author with specified name is already exists!</div>").prependTo($('#items-list')).slideDown('fast');
                processing = false;
            }
            else
                processing = false;
        }
    });
});


$(document).on('click', '.update-author-link', function (event) {
    if (processing)
        return;

    processing = true;

    var newAuthorName = $($(event.target).parent().parent().parent().prev().children()[0]).val();
    if (!newAuthorName) {
        $("#content").animate({ scrollTop: 0 }, "slow", function() {
            if (!$('#invalid-author-name').length) {
                $("<div id='invalid-author-name'>Invalid author name</div>").prependTo($('#items-list')).slideDown('slow', function () {
                    processing = false;
                });
            }
            else
                processing = false;
        });
        return;
    }

    $('#invalid-author-name').toggle("slide", 500, function() { $(this).remove(); });
    var authorId = parseInt($(event.target).parent().parent().parent().parent().attr('id'));


    $.ajax({
        url: '/news-management/authors/update',
        type: 'POST',
        data: JSON.stringify({
            authorId: authorId,
            authorName: newAuthorName
        }),
        contentType: "application/json",
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success: function(exists) {
            if (!exists) {
                if ($('#item-exists').length)
                    $('#item-exists').toggle("slide", 500, function() { $(this).remove(); });
                $("#content").animate({ scrollTop: 0 }, "slow", function() {
                    $("<div id='item-updated'>Successfully updated author</div>").prependTo($('#items-list')).slideDown("fast");
                    $('#item-updated').delay(3000).fadeOut(function () {
                        $(this).remove();
                        processing = false;
                    });
                });
            }
            else if (!$('#item-exists').length) {
                $("<div id='item-exists'>Author with specified name is already exists!</div>").prependTo($('#items-list')).slideDown('fast');
                processing = false;
            }
            else
                processing = false;
        }
    });
});


$(document).on('click', '.expire-author-link', function (event) {
    if (processing)
        return;

    processing = true;

    if ($('#item-exists').length)
        $('#item-exists').toggle("slide", 500, function() { $(this).remove(); });

    var authorId = parseInt($(event.target).parent().parent().parent().parent().attr('id'));

    $.ajax({
        url: '/news-management/authors/expire',
        type: 'POST',
        data: JSON.stringify({
            authorId: authorId
        }),
        contentType: "application/json",
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success: function() {
            $(event.target).parent().toggle("slide", 500, function() {
                $(this).remove();
                $("<div id='author-expired'>Author was successfully expired.</div>").prependTo($('#items-list')).slideDown('fast');
                $('#author-expired').delay(3000).fadeOut(function() { $(this).remove(); });
            });
            processing = false;
        }
    });
});