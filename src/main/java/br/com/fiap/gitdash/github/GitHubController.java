package br.com.fiap.gitdash.github;

import java.util.List;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GitHubController {

    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/user")
    public String getUserInfo(Model model,
            @RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient authorizedClient,
            OAuth2AuthenticationToken authentication) {

        String tokenValue = authorizedClient.getAccessToken().getTokenValue();

        OAuth2User user = authentication.getPrincipal();
        String username = user.getAttribute("login");
        String avatarUrl = user.getAttribute("avatar_url");

        List<RepositoryInfo> repos = gitHubService.getUserRepositories(tokenValue);

        model.addAttribute("repositories", repos);
        model.addAttribute("name", username);
        model.addAttribute("avatarUrl", avatarUrl);

        return "user";
    }
}
