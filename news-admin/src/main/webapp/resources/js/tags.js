$('#tags-link').css('font-weight', 'bold');


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


var newTagRowTemplate = "<div id='{0}' class='item'>" +
    "                       <div class='item-label'>Tag:</div>" +
    "                       <div class='item-name'><input class='item-name-textarea' type='text' value='{1}' disabled></div>" +
    "                       <div class='item-action'>" +
    "                           <div class='item-update-links' hidden>" +
    "                               <a href='javascript:void(0)' class='update-tag-link'><u>update</u></a>" +
    "                               <a href='javascript:void(0)' class='delete-tag-link'><u>delete</u></a>" +
    "                               <a href='javascript:void(0)' class='cancel-tag-link'><u>cancel</u></a>" +
    "                           </div>" +
    "                           <a href='javascript:void(0)' class='edit-tag-link'><u>edit</u></a>" +
    "                       </div>" +
    "                   </div>";


$(document).on('click', '.edit-tag-link', function (event) {
    $(event.target).parent().hide();
    $($(event.target).parent().parent().prev().children()[0]).removeAttr('disabled');
    $(event.target).parent().prev().show();
});


$(document).on('click', '.cancel-tag-link', function (event) {
    $(event.target).parent().parent().hide();
    $(event.target).parent().parent().next().show();
    $($(event.target).parent().parent().parent().prev().children()[0]).attr('disabled', true);
});


$(document).on('click', '.add-tag-link', function (event) {
    if (processing)
        return;

    processing = true;

    var newTagName = $($(event.target).parent().parent().prev().children()[0]).val();
    if (!newTagName) {
        $("#content").animate({ scrollTop: 0 }, "slow", function() {
            if (!$('#invalid-tag-name').length) {
                $("<div id='invalid-tag-name'>Invalid tag name</div>").prependTo($('#items-list')).slideDown('slow', function () {
                    processing = false;
                });
            }
            else
                processing = false;
        });
        return;
    }

    $('#invalid-tag-name').toggle("slide", 500, function() { $(this).remove(); });

    $.ajax({
        url: '/news-management/tags/add',
        type: 'POST',
        data: JSON.stringify({
            tagName: newTagName
        }),
        contentType: "application/json",
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success: function(newTagId) {
            if (newTagId) {
                if ($('#tag-exists').length)
                    $('#tag-exists').toggle("slide", 500, function() { $(this).remove(); });
                var newTagRow = newTagRowTemplate.format(newTagId, newTagName);
                $(newTagRow).appendTo($('#items-list')).slideDown('fast');
                $("#content").animate({ scrollTop: 0 }, "slow", function() {
                    $("<div id='tag-added'>Successfully added tag</div>").prependTo($('#items-list')).slideDown("fast");
                    $('#tag-added').delay(3000).fadeOut(function () {
                        $(this).remove();
                        processing = false;
                    });
                });
            }
            else if (!$('#tag-exists').length) {
                $("<div id='tag-exists'>Tag with specified name is already exists!</div>").prependTo($('#items-list')).slideDown('fast');
                processing = false;
            }
            else
                processing = false;
        }
    });
});


$(document).on('click', '.update-tag-link', function (event) {
    if (processing)
        return;

    processing = true;

    var newTagName = $($(event.target).parent().parent().parent().prev().children()[0]).val();
    if (!newTagName) {
        $("#content").animate({ scrollTop: 0 }, "slow", function() {
            if (!$('#invalid-tag-name').length) {
                $("<div id='invalid-tag-name'>Invalid tag name</div>").prependTo($('#items-list')).slideDown('slow', function () {
                    processing = false;
                });
            }
            else
                processing = false;
        });
        return;
    }

    $('#invalid-tag-name').toggle("slide", 500, function() { $(this).remove(); });
    var tagId = parseInt($(event.target).parent().parent().parent().parent().attr('id'));


    $.ajax({
        url: '/news-management/tags/update',
        type: 'POST',
        data: JSON.stringify({
            tagId: tagId,
            tagName: newTagName
        }),
        contentType: "application/json",
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success: function(exists) {
            if (!exists) {
                if ($('#tag-exists').length)
                    $('#tag-exists').toggle("slide", 500, function() { $(this).remove(); });
                $("#content").animate({ scrollTop: 0 }, "slow", function() {
                    $("<div id='tag-updated'>Successfully updated tag</div>").prependTo($('#items-list')).slideDown("fast");
                    $('#tag-updated').delay(3000).fadeOut(function () {
                        $(this).remove();
                        processing = false;
                    });
                });
            }
            else if (!$('#tag-exists').length) {
                $("<div id='tag-exists'>Tag with specified name is already exists!</div>").prependTo($('#items-list')).slideDown('fast');
                processing = false;
            }
            else
                processing = false;
        }
    });
});


$(document).on('click', '.delete-tag-link', function (event) {
    if (processing)
        return;

    processing = true;

    if ($('#tag-exists').length)
        $('#tag-exists').toggle("slide", 500, function() { $(this).remove(); });

    var tagId = parseInt($(event.target).parent().parent().parent().parent().attr('id'));

    $.ajax({
        url: '/news-management/tags/delete',
        type: 'POST',
        data: JSON.stringify({
            tagId: tagId
        }),
        contentType: "application/json",
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success: function() {
            $('#'+tagId).toggle("slide", 500, function() {
                $(this).remove();
                $("<div id='tag-deleted'>Tag was successfully deleted.</div>").prependTo($('#items-list')).slideDown('fast');
                $('#tag-deleted').delay(3000).fadeOut(function() { $(this).remove(); });
            });
            processing = false;
        }
    });
});