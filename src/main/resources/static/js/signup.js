$("#SIGNUP").submit(function (event) {
    event.preventDefault();
    if (usernameCheck() === false) {
        return;
    }

    if(chkPW($("#password").val(), $("#password_check").val())===false){
        return;
    }

    let data = JSON.stringify({
        "username": $("#username").val(),
        "password": $("#password").val(),
        "name": $("#name").val(),
        "email": $("#email").val()
    });

    $.ajax({
        url: '/api/signup',
        type: "POST",
        contentType: 'application/json',
        dataType: 'json',
        data: data,
        beforeSend: function (xhr) {
            xhr.setRequestHeader("X-CSRF-Token", $("#_csrf").val());
        },
        success: function (data) {
            console.log(data);
            if (data.message === "success") {
                window.location.href="/login";
            } else if (data.message === "exist") {
                alert("이미 존재하는 유저 아이디입니다.");
                $("#username").focus();
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

function chkPW(password, checkPassword) {

    var pw = password;
    var pwc = checkPassword;
    var num = pw.search(/[0-9]/g);
    var eng = pw.search(/[a-z]/ig);
    var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

    if(pw !== pwc){
        alert("비밀번호를 다시 확인해 주세요.");
        return false;
    }
    if (pw.length < 8 || pw.length > 20) {

        alert("비밀번호는 8자리 ~ 20자리 이내로 입력해주세요.");
        return false;
    } else if (pw.search(/\s/) != -1) {
        alert("비밀번호는 공백 없이 입력해주세요.");
        return false;
    } else if (num < 0 || eng < 0 || spe < 0) {
        alert("비밀번호는 영문, 숫자, 특수문자를 혼합하여 입력해주세요.");
        return false;
    } else {
        return true;
    }

}