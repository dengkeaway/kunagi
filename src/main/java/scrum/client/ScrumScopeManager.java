package scrum.client;

import ilarkesto.core.scope.CascadingScope;
import ilarkesto.core.scope.NonConcurrentScopeManager;
import ilarkesto.core.scope.Scope;
import scrum.client.admin.Auth;
import scrum.client.admin.SystemMessageManager;
import scrum.client.admin.User;
import scrum.client.calendar.Calendar;
import scrum.client.collaboration.Chat;
import scrum.client.collaboration.UsersStatus;
import scrum.client.collaboration.Wiki;
import scrum.client.communication.Pinger;
import scrum.client.files.Uploader;
import scrum.client.journal.ChangeHistoryManager;
import scrum.client.project.Project;
import scrum.client.search.Search;
import scrum.client.undo.Undo;
import scrum.client.workspace.DndManager;
import scrum.client.workspace.PublicWorkspaceWidgets;
import scrum.client.workspace.Ui;
import scrum.client.workspace.UsersWorkspaceWidgets;

public class ScrumScopeManager {

	private static NonConcurrentScopeManager scopeManager;
	private static CascadingScope appScope;
	private static CascadingScope userScope;
	private static CascadingScope projectScope;

	private static ComponentManager cm;

	static void initialize(ComponentManager cm) {
		assert scopeManager == null;

		ScrumScopeManager.cm = cm;

		scopeManager = NonConcurrentScopeManager.createCascadingScopeInstance("app", new ScrumComponentsReflector());
		appScope = (CascadingScope) scopeManager.getScope();
		Scope scope = appScope;

		scope.putComponent("app", cm.getApp());
		scope.putComponent(cm.getDao());
		scope.putComponent(new Pinger());
		scope.putComponent(new Ui());
		scope.putComponent(new SystemMessageManager());
		scope.putComponent(new Auth());
		scope.putComponent(new PublicWorkspaceWidgets());

		appScope.wireComponents();
	}

	public static void createUserScope(User user) {
		assert user != null;

		userScope = appScope.createScope("user");
		Scope scope = scopeManager.setScope(userScope);

		scope.putComponent("user", user);
		scope.putComponent(new UsersWorkspaceWidgets());

		userScope.wireComponents();
	}

	public static void createProjectScope(Project project) {
		assert project != null;

		projectScope = userScope.createScope("project");
		Scope scope = scopeManager.setScope(projectScope);

		scope.putComponent("project", project);
		scope.putComponent(new Chat());
		scope.putComponent(new ChangeHistoryManager());
		scope.putComponent(new Wiki());
		scope.putComponent(new Calendar());
		scope.putComponent(new Undo());
		scope.putComponent(new DndManager());
		scope.putComponent(new Uploader());
		scope.putComponent(new Search());
		scope.putComponent(new UsersStatus());

		projectScope.wireComponents();
		cm.getProjectContext().openProject(project);
	}

	public static void destroyProjectScope() {
		projectScope = null;
		scopeManager.setScope(userScope);
	}

	public static void destroyUserScope() {
		destroyProjectScope();
		userScope = null;
		scopeManager.setScope(appScope);
	}

	public static boolean isProjectScope() {
		return projectScope != null;
	}

	public static Project getProject() {
		return (Project) projectScope.getComponent("project");
	}
}