$("#FINDID").submit(function(event){
    event.preventDefault();

    let data = JSON.stringify({
        "email":$("#email_find_id").val()
    });
    $("#findIdLodingModal").modal('show');
    $.ajax({
        url:"/api/find/id",
        type:"post",
        contentType:"application/json",
        dataType:"json",
        timeout:1500,
        data:data,
        beforeSend:function(xhr){
            xhr.setRequestHeader("X-CSRF-Token", $("#_csrf_FINDID").val());
        },
        success:function(returnData){
            if(returnData.message==="user_non"){
                setTimeout(function(){$('#findIdLodingModal').modal('hide')}, 10)
                return alert("등록된 이메일이 없습니다.");
            }
            window.location.href = "/mail/sended";
        },
        error:function(error){
            // console.log(error);
            window.location.href = "/mail/sended";
        }
    })
})

$("#FINDPW").submit(function(event){
    event.preventDefault();

    console.log($("#email_find_pw").val());
    let data = JSON.stringify({
        "email":$("#email_find_pw").val()
    });
    $("#findPwLodingModal").modal("toggle");
    $.ajax({
        url:"/api/find/pw",
        type:"post",
        contentType:"application/json",
        dataType:"json",
        timeout:1500,
        data:data,
        beforeSend:function(xhr){
            xhr.setRequestHeader("X-CSRF-Token", $("#_csrf_FINDPW").val());
        },
        success:function(returnData){
            if(returnData.message==="user_non"){
                setTimeout(function(){$('#findPwLodingModal').modal('hide')}, 10)
                return alert("등록된 이메일이 없습니다.");
            }
            window.location.href = "/mail/sended";
        },
        error:function(error){
            // console.log(error);
            window.location.href = "/mail/sended";
        }
    })
})