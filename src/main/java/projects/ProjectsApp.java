package projects;


import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ProjectsApp  {
    // @formatter:off
    // List of available operations
    private List<String> operations = List.of(
            "1) Add a project",
            "2) List projects",
            "3) Select a project",
            "4) Update project details",
            "5) Delete a project"
    );
    // @formatter:on

    // Scanner object for reading user input
    private Scanner scanner = new Scanner(System.in);

    // ProjectService object for accessing the project database
    private ProjectService projectService = new ProjectService();
    private Project curProject;


    public static void main(String[] args) {
        new ProjectsApp().processUserSelections();
    }
    private void processUserSelections() {
        boolean done = false;
        while (!done) {
            try {
                // Get user selection
                int selection = getUserSelection();

                // Perform selected operation
                switch (selection) {
                    case -1:
                        done = exitMenu(); // Exit the menu
                        break;

                    case 1:
                        createProject(); // Add a new project
                        break;

                    case 2:
                        listProjects();
                        break;
                        
                    case 3:
                        selectProject();
                        break;

                    case 4:
                        updateProjectDetails();
                        break;

                    case 5:
                        deleteProject();
                        break;

                    default:
                        // Invalid selection
                        System.out.println("\n" + selection + " is not a valid selection. Try again.");
                }

            }
            catch (Exception e) {
                // Print error message and try again
                System.out.println("\nError: " + e + " Try again.");
            }
        }
    }

    private void deleteProject() {
        listProjects();

        Integer projectId = getIntInput("Enter the ID of the project to delete");

        projectService.deleteProject(projectId);
        System.out.println("Project " + projectId + " was deleted successfully.");

        if (Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId)) {
            curProject = null;
        }
    }

    private void updateProjectDetails() {
        if (Objects.isNull(curProject)) {
            System.out.println("\nPlease select a project.");
            return;
        }

        String projectName = getStringInput("Enter the project name [" + curProject.getProjectName() + "]");

        BigDecimal estimatedHours =
                getDecimalInput("Enter the estimated hours + [" + curProject.getEstimatedHours() + "]");

        BigDecimal actualHours =
                getDecimalInput("Enter the actualy hours + [" + curProject.getActualHours() + "]");

        Integer difficulty =
                getIntInput("Enter the project difficulty (1-4) + [" + curProject.getDifficulty() + "]");

      String notes =
               getStringInput("Enter the project notes + [" + curProject.getNotes() + "]");

      Project project = new Project();

      project.setProjectId(curProject.getProjectId());
      project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);

      project.setEstimatedHours(Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);

      project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
      project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
      project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);

      projectService.modifyProjectDetails(project);

      curProject = projectService.fetchProjectById(curProject.getProjectId());
    }

    private void selectProject() {
        listProjects();
        Integer projectId = getIntInput("Enter a project ID to select a project");

        /** Unselect the current project. */
        curProject = null;

        /** This will throw an exception if an invalid project ID is entered. */

        curProject = projectService.fetchProjectById(projectId);
    }

    private void listProjects() {
        List<Project> projects = projectService.fetchAllProjects();

        System.out.println("\nProjects: ");

        projects.forEach(project -> System.out.println(" " + project.getProjectId() + ": " + project.getProjectName()));
    }

    private boolean exitMenu() {
        // Print message and exit the menu loop
        System.out.println("\nExiting the menu.");
        return true;
    }

    private void createProject() {
        // Prompt the user for project details
        String projectName = getStringInput("Enter the project name");
        BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
        BigDecimal actualHours = getDecimalInput("Enter the actual hours");
        Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
        String notes = getStringInput("Enter the project notes");

        // Create a new Project object and set its properties
        Project project = new Project();

     project.setProjectName(projectName);
     project.setEstimatedHours(estimatedHours);
     project.setActualHours(actualHours);
     project.setDifficulty(difficulty);
     project.setNotes(notes);

     // Add the project to the database
     Project dbProject = projectService.addProject(project);

     curProject = dbProject;

     System.out.println("Project created successfully: " + dbProject);
    }

    private BigDecimal getDecimalInput(String prompt) {
        // Prompt the user for a decimal value and convert it to a BigDecimal
        String input = getStringInput(prompt);

        if (Objects.isNull(input)) {
            return null;
        }
        try {
            return new BigDecimal(input).setScale(2);
        }
        catch(NumberFormatException e) {
            // Throw an exception if the input is not a valid decimal value
            throw new DbException(input + " is not a valid decimal number.");
        }
    }

    private int getUserSelection() {
        // Display the available operations and get user selection
        printOperations();

        Integer input = getIntInput("Enter a menu selection");

        return Objects.isNull(input) ? -1 : input;
    }

    private Integer getIntInput(String prompt) {
        // Prompt the user for an integer value and convert it to an Integer
        String input = getStringInput(prompt);

        if (Objects.isNull(input)) {
            return null;
        }

        try {
            return Integer.valueOf(input);
        }
        catch(NumberFormatException e) {
            // Throw an exception if the input is not a valid integer value
            throw new DbException(input + " is not valid number.");
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt + ": ");
        String input = scanner.nextLine();
        return input.isBlank() ? null : input.trim();
    }

    private void printOperations() {
        System.out.println("\nThese are the available selections. Press the Enter key to quit:");

       operations.forEach(line -> System.out.println(" " + line));

       if (Objects.isNull(curProject)) {
           System.out.println("\nYou are not working with a project.");
       }
       else {
           System.out.println("\nYou are working with project: " + curProject);
       }
    }


}
