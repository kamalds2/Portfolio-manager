document.addEventListener("DOMContentLoaded", function () {
    const deleteLinks = document.querySelectorAll(".delete-link");

    deleteLinks.forEach(link => {
        link.addEventListener("click", function (e) {
            const confirmed = confirm("Are you sure you want to delete this item?");
            if (!confirmed) {
                e.preventDefault();
            }
        });
    });

    const formInputs = document.querySelectorAll("input, textarea");
    formInputs.forEach(input => {
        input.addEventListener("focus", () => {
            input.style.borderColor = "#3498db";
        });
        input.addEventListener("blur", () => {
            input.style.borderColor = "#ccc";
        });
    });
});