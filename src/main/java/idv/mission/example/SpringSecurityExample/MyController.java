package idv.mission.example.SpringSecurityExample;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MyController {

	@RequestMapping("/")
	public @ResponseBody String hello() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if( auth instanceof AnonymousAuthenticationToken ) {
			return "Please <a href='/login'>Login</a> first.";
		}
		else {
			return "Hello, " + auth.getName().toUpperCase();
		}
	}

	@RequestMapping("/test")
	public @ResponseBody String test() {
		return "test";
	}

	@RequestMapping("/success/logined")
	public @ResponseBody String logined() {
		return "Login Success！Click <a href='/logout'>Here</a> to logout.";
	}

	@RequestMapping("/jump")
	public RedirectView redirectWithUsingRedirectView(RedirectAttributes attributes) {
		// You can uncomment these two lines, these will appear in the url and output
		attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
		attributes.addAttribute("attribute", "redirectWithRedirectView");
		return new RedirectView("/jumped");
	}

	@RequestMapping("/jumped")
	public @ResponseBody Map<String, String> jumped(HttpServletRequest httpRequest) {
		Enumeration<String> attributeNames = httpRequest.getAttributeNames();
		Map<String, String> result = new HashMap<String, String>();
		while (attributeNames.hasMoreElements()) {
			String attributeName = attributeNames.nextElement();
			result.put(attributeName, httpRequest.getAttribute(attributeName).toString());
		}
		return result;
	}

	/**
	 * 未登入前，接觸沒權限的頁面會被導到 /login；登入後，接觸沒權限的頁面會被導到 /403(AccessDeniedHandler)
	 */
	@RequestMapping("/403")
	public @ResponseBody String error403() {
		return "403";
	}

	/**
	 * 注意 @GetMapping 跟 @RequestMapping 不同，以及回傳 @ResponseBody 的不同
	 */
	@GetMapping("/login")
	public String login() {
		return "login.jsp";
	}

	/**
	 * 這段根本跑不進來，不過登入會被自動做掉
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody String login(@RequestParam("username") String userName, @RequestParam("password") String password) {
		System.out.println(userName + ",  " + password);
		return "Test";
	}
}