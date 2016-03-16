$("#form").validate({
    rules: {
        senderLocationId: {
            required: true,
            digits: true
        },
        receiverLocationId: {
            required: true,
            digits: true
        },
        totalAmount: {
            required: true,
            digits: true
        },
        phone: {
            required: true
        },
        firstName: {
            required: true
        },
        lastName: {
            required: true
        }

    }
});

$("form").submit(function (event) {
    if (!$("#form").valid()) {
        return;
    }

    var data_array = $('#form').serializeArray().reduce(function (obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});

    $.ajax({
        url: "http://172.17.0.3:8080/api/v1.0/payment",
        type: "POST",
        data: JSON.stringify(data_array),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            console.log(data)
            if (data['status'] == 'SUCCESS') {
                $('#info-msg').html("You money has been wired successfully.");
                $('#tracking-number').html("Tracking identification: <strong>" + data['message'] + "</strong>");
            }
            else {
                $('#info-msg').html("Something went very wrong. I have failed to send money for you.");
                $('#tracking-number').html("Problem description: <strong>" + data['message'] + "</strong>");
            }
            $('#myModal').modal('show')
        }
    });
    event.preventDefault();

});
