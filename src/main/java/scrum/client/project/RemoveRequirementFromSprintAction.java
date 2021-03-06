package scrum.client.project;

import scrum.client.common.TooltipBuilder;
import scrum.client.sprint.Sprint;

public class RemoveRequirementFromSprintAction extends GRemoveRequirementFromSprintAction {

	public RemoveRequirementFromSprintAction(Requirement requirement) {
		super(requirement);
	}

	@Override
	public String getLabel() {
		return "Kick from Sprint";
	}

	@Override
	public String getTooltip() {
		TooltipBuilder tb = new TooltipBuilder("Remove this Story from the current Sprint.");

		if (!requirement.getProject().isProductOwner(getCurrentUser())) {
			tb.addRemark(TooltipBuilder.NOT_PRODUCT_OWNER);
		} else {
			if (!requirement.isSprintSet()) tb.addRemark("Story is not in in the current Sprint.");
		}

		return tb.getTooltip();
	}

	@Override
	public boolean isExecutable() {
		if (!requirement.isInCurrentSprint()) return false;
		return true;
	}

	@Override
	public boolean isPermitted() {
		if (!requirement.getProject().isProductOwner(getCurrentUser())) return false;
		return true;
	}

	@Override
	protected void onExecute() {
		Sprint sprint = requirement.getSprint();
		requirement.removeFromSprint();
		addUndo(new Undo(sprint));
	}

	class Undo extends ALocalUndo {

		private Sprint s;

		public Undo(Sprint s) {
			this.s = s;
		}

		@Override
		public String getLabel() {
			return "Undo Remove " + requirement.getReferenceAndLabel() + " from Sprint";
		}

		@Override
		protected void onUndo() {
			requirement.setSprint(s);
		}

	}

}
