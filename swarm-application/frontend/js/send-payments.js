$("form").submit(function (event) {
    var data_array = $('#form').serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});

    $.ajax({
        url:"http://172.17.0.3:8080/api/v1.0/payment",
        type:"POST",
        data:JSON.stringify(data_array),
        contentType:"application/json; charset=utf-8",
        dataType:"json",
        success: function(){
            $('#myModal').modal('show');
        }
    });
    event.preventDefault();
});
