package idv.mission.example.SpringSecurityExample;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyErrorController implements ErrorController {
	
	@Override
    public String getErrorPath() {
        return "/error";
    }

	@RequestMapping("/error")
	public @ResponseBody String renderErrorPage(HttpServletRequest httpRequest) {
		String errorMsg = "";
		Integer httpErrorCode = getErrorCode(httpRequest);
		if( httpErrorCode == null ) {
			errorMsg = "Http Error Code: 403. Forbidden";
		}
		else {
			switch (httpErrorCode) {
			case 400:
				errorMsg = "Http Error Code: 400. Bad Request";
				break;
			case 401:
				errorMsg = "Http Error Code: 401. Unauthorized";
				break;
			case 404:
				errorMsg = "Http Error Code: 404. Resource not found";
				break;
			case 500:
				errorMsg = "Http Error Code: 500. Internal Server Error";
				break;
			}
		}
		return errorMsg;
	}

	private Integer getErrorCode(HttpServletRequest httpRequest) {
		Integer errorCode = (Integer) httpRequest.getAttribute("javax.servlet.error.status_code"); 
		return errorCode;
	}
	
}