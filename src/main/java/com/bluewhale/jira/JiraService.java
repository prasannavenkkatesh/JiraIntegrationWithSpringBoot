package com.bluewhale.jira;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class JiraService {

    @Value("${jira.url}")
    private String jiraUrl;

    @Value("${jira.username}")
    private String jiraUsername;

    @Value("${jira.api.token}")
    private String jiraApiToken;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public JiraService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public JsonNode getAllProjects() throws Exception {
        String url = jiraUrl + "/rest/api/2/project";

        HttpHeaders headers = createHeaders();

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            JsonNode projects = objectMapper.readTree(response.getBody());
            ArrayNode filteredProjects = objectMapper.createArrayNode();

            for (JsonNode project : projects) {
                ObjectNode filteredProject = objectMapper.createObjectNode();
                filteredProject.put("id", project.get("id").asText());
                filteredProject.put("name", project.get("name").asText());
                filteredProjects.add(filteredProject);
            }
            return filteredProjects;
        } else {
            throw new RuntimeException("Failed to fetch projects from JIRA: " + response.getStatusCode());
        }
    }

    public JsonNode getEpicsByProject(String projectId) throws Exception {
        String jql = "project=" + projectId + " AND issuetype=Epic";
        String url = jiraUrl + "/rest/api/2/search?jql=" + jql;

        HttpHeaders headers = createHeaders();

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            JsonNode issues = objectMapper.readTree(response.getBody()).get("issues");
            ArrayNode filteredEpics = objectMapper.createArrayNode();

            for (JsonNode issue : issues) {
                ObjectNode filteredEpic = objectMapper.createObjectNode();
                filteredEpic.put("id", issue.get("id").asText());
                filteredEpic.put("title", issue.get("fields").get("summary").asText());
                filteredEpic.put("link", jiraUrl + "/browse/" + issue.get("key").asText());
                filteredEpics.add(filteredEpic);
            }
            return filteredEpics;
        } else {
            throw new RuntimeException("Failed to fetch epics from JIRA: " + response.getStatusCode());
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = jiraUsername + ":" + jiraApiToken;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);
        headers.set("Content-Type", "application/json");
        return headers;
    }
}
