package com.bluewhale.jira;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jira")
public class JiraController {

	private final JiraService jiraService;

	public JiraController(JiraService jiraService) {
		this.jiraService = jiraService;
	}

	@GetMapping("/projects")
	public JsonNode getAllProjects() {
		try {
			return jiraService.getAllProjects();
		} catch (Exception e) {
			throw new RuntimeException("Failed to fetch projects", e);
		}
	}

	@PostMapping("/epics")
	public JsonNode getEpicsByProject(@RequestBody ProjectRequest projectRequest) {
		try {
			return jiraService.getEpicsByProject(projectRequest.getProjectId());
		} catch (Exception e) {
			throw new RuntimeException("Failed to fetch epics", e);
		}
	}
}

class ProjectRequest {
	private String projectId;

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
