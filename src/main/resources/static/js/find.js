$("#FINDID").submit(function(event){
    event.preventDefault();

    console.log($("#email_find_id").val());
    let data = JSON.stringify({
        "email":$("#email_find_id").val()
    });
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
                return alert("등록된 이메일이 없습니다.");
            }
            window.location.href = "/mail/sended";
        },
        error:function(error){
            window.location.href = "/mail/sended";
        }
    })
    // window.location.href="/";
})

$("#FINDPW").submit(function(event){
    event.preventDefault();

    console.log($("#email_find_pw").val());
    let data = JSON.stringify({
        "email":$("#email_find_pw").val()
    });
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
                return alert("등록된 이메일이 없습니다.");
            }
            window.location.href = "/mail/sended";
        },
        error:function(error){
            window.location.href = "/mail/sended";
        }
    })
})