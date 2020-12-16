$("#LOGIN").submit(function (event) {
    event.preventDefault();

    if (usernameCheck() === false) {
        return;
    }

    let data = JSON.stringify({
        "username": $("#username").val(),
        "password": $("#password").val(),
    });

    $.ajax({
        url: '/api/login',
        type: "POST",
        contentType: 'application/json',
        dataType: 'json',
        data: data,
        beforeSend: function (xhr) {
            // CSRF COOKIE SET EXAMPLE
            // Origin
            // xhr.setRequestHeader("X-CSRF-Token", $("#_csrf").val());

            // New
            xhr.setRequestHeader("X-XSRF-TOKEN", $("#_csrf").val());
        },
        success: function (data) {
            if (data.message === "success") {
                window.location.href="/";
            } else if(data.message === "failure"){
                alert("아이디 또는 비밀번호를 다시 확인해주세요.");
            } else {
                alert("service error")
            }
        },
        error: function (error) {
            console.log(error);
            alert("error");
        }
    })
});

function usernameCheck() {
    var idReg = /^[a-z]+[a-z0-9]{5,19}$/g;
    if (!idReg.test($("#username").val())) {
        alert("아이디는 영문자로 시작하는 6~20자 영문자 또는 숫자이어야 합니다.");
        return false;
    }
}