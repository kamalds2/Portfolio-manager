package Managefolio.admin.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSize(MaxUploadSizeExceededException ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("errorMessage", "Uploaded file exceeds the server limit. Please use files under 5MB.");
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/dashboard");
    }
}
