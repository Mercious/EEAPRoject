function displayRegisterForm() {
    var pitchForm = document.getElementById("registerPitch");
    pitchForm.style.display = "none";
    var registerForm = document.getElementById("registerForm");
    registerForm.style.display = "block";
    registerForm.style.width = "1000px";

    var registerFormParent = document.getElementById("registerFormParent");
    registerFormParent.style.width = "800px";
}

window.onload = function() {



    if(document.title === "Login / Registrierung") {
        var beginRegisterButton = document.getElementById("beginRegisterButton");
        beginRegisterButton.addEventListener("click", displayRegisterForm);

        // wenn es bereits errorMessages im DOM gibt, dann ist wohl ein Registierungs
        // greift auch, wenn die Anmeldung fehlschlägt -> Schönheitsfehler, TODO: Lösung finden
        if (document.getElementsByClassName("errorMessage").length > 0) {
            displayRegisterForm();
        }
    }
};