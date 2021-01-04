$("#SIGNUP").submit(function(event) {
    event.preventDefault();
    if (usernameCheck() === false) {
        return;
    }

    if (chkPW($("#password").val(), $("#password_check").val()) === false) {
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
        beforeSend: function(xhr) {
            xhr.setRequestHeader("X-XSRF-TOKEN", $("#_csrf").val());
        },
        success: function(data) {
            console.log(data);
            if (data.message === "success") {
                window.location.href = "/login";
            } else if (data.message === "exist") {
                alert("이미 존재하는 유저 아이디입니다.");
                $("#username").focus();
            } else if (data.message === "accessDenied") {
                alert("옳바른 접근 방법을 사용하세요.");
                window.location.reload();
            } else {
                alert("service error")
            }
        },
        error: function(error) {
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
    // var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);
    var spe = pw.search(/[!@#$%^&*()\-_=+\\\/\[\]{};:\`"',.<>\/?\|~]/gi);


    if (pw !== pwc) {
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
        console.log("통과")
        return true;
    }


}

function valifyEmailSend() {
    // $("#valifyEmailBox").css("display","block");
    if ($("#email").val() == '' | $("#email").val() == null) {
        alert('이메일을 정확히 입력해 주세요.');
        return;
    }

    $('#email').attr('disabled', true);
    let data = {
        "email": $("#email").val()
    }
    emailAuthHtmlView(true);
    $.ajax({
        url: "/api/sendmail",
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        data: data,
        success: function(returnData) {
            if (returnData.message === "exist") {
                emailAuthHtmlView(false);
                return alert("이미 등록된 이메일 입니다.");
            } else if (returnData.message === "success") {
                return;
            } else {
                emailAuthHtmlView(false);
                alert("예상치 못한 에러가 발생했습니다. 다시 시도해 주세요.");
                return window.location.reload();
            }

        },
        error: function(error) {
            console.log(error);
            alert("mail service error");
        }
    })
}

function emailAuthHtmlView(bool) {
    if (bool === true) {
        $("#valifyEmailBtn").attr("disabled", true);
        $("#valifyEmailBox").html(
            `
                <label for="emailCode">인증번호</label>
                <input class="st-ty-singup-input" type="text" placeholder="인증번호 입력" name="emailCode" id="emailCode" value="">
                <small id="passwordHelp" class="form-text text-muted">"인증 메일의 번호를 입력해 주세요."</small>
                <button type="button" class="btn st-ty-signup-email-btn mt-2" onclick="emailCodeCheckHandler()">인증번호 확인</button>
                <div class="st-ty-signup-check-email text-center">
                    <p>인증 메일을 ${$("#email").val()} 로 전송했습니다.</p>
                    <p style="font-weight:700">인증 메일을 받지 못하셨나요 ?</p>
                    <p style="font-size:12px; color:red;">메일 서비스의 스팸함을 확인해주세요. gmail의 경우 프로모션 탭을 확인해 주세요.</p>
                </div>
            `
        );
    } else {
        $('#email').attr('disabled', false);
        $("#valifyEmailBtn").attr("disabled", false);
        $("#valifyEmailBox").html('');
    }

}

function emailCodeCheckHandler() {
    let data = {
        "emailCode": $("#emailCode").val()
    }

    $.ajax({
        url: "/api/email/checkcode",
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        data: data,
        success: function(returnData) {
            if (returnData.message === "success") {
                $("#valifyEmailBox").css("display", "none");
                $("#email").attr("disabled", true)
                $("#signupSubmitBtn").attr("disabled", false);
            } else {
                alert("인증 코드가 일치하지 않습니다.");
            }
        },
        error: function(error) {
            console.log(error);
            alert("mail service error");
        }
    })
}