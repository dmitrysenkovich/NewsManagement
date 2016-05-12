$(function(){
    $("#authors").multiselect({
        multiple: false,
        header: "Please select the author",
        noneSelectedText: "Please select the author",
        selectedList: 1
    });
});

$(function(){
   $("#tags").multiselect({
        header: "Please select the tag",
        noneSelectedText: "Please select the tag"
    });
});
