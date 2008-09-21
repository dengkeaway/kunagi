package scrum.client.impediments;

import scrum.client.common.ABlockWidget;
import scrum.client.common.ItemFieldsWidget;
import scrum.client.common.editable.AEditableTextWidget;
import scrum.client.common.editable.AEditableTextareaWidget;
import scrum.client.img.Img;
import scrum.client.service.Service;
import scrum.client.workspace.WorkspaceWidget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ImpedimentWidget extends ABlockWidget implements SourcesMouseEvents {

	/**
	 * The impediment, this widget is managing.
	 */
	private Impediment impediment;

	public ImpedimentWidget(Impediment impediment) {
		this.impediment = impediment;
	}

	@Override
	protected String getBlockTitle() {
		return impediment.getLabel();
	}

	@Override
	protected AbstractImagePrototype getIcon() {
		// return different icon depending on solved-status
		if (impediment.isSolved()) return Img.bundle.impedimentSolvedIcon32();
		return Img.bundle.impedimentIcon32();
	}

	@Override
	protected Widget buildContent() {
		if (!isExtended()) // block is not extended -> return only a label with the summary
			return new Label(impediment.getSummary());

		// block is extended -> create an ItemFieldsWidget
		ItemFieldsWidget fieldsWidget = new ItemFieldsWidget();
		fieldsWidget.addField("Label", new AEditableTextWidget() {

			@Override
			protected String getText() {
				return impediment.getLabel();
			}

			@Override
			protected void setText(String text) {
				impediment.setLabel(text);
				rebuild();
			}

		});
		fieldsWidget.addField("Description", new AEditableTextareaWidget() {

			@Override
			protected String getText() {
				return impediment.getDescription();
			}

			@Override
			protected void setText(String text) {
				impediment.setDescription(text);
			}
		});
		fieldsWidget.addField("Solution", new AEditableTextareaWidget() {

			@Override
			protected String getText() {
				return impediment.getSolution();
			}

			@Override
			protected void setText(String text) {
				impediment.setSolution(text);
			}
		});
		return fieldsWidget;
	}

	@Override
	protected Widget buildToolbar() {
		if (!isExtended()) // block is not extended -> no toolbar
			return null;

		// block is extended -> create toolbar with buttons
		VerticalPanel toolbar = new VerticalPanel();
		toolbar.setStyleName("Toolbar");

		Button deleteButton = new Button("Delete");
		deleteButton.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				Service.getProject().deleteImpediment(impediment);
				WorkspaceWidget.impediments.list.removeSelectedRow();
			}
		});
		toolbar.add(deleteButton);

		if (!impediment.isSolved()) {
			// impediment not solved -> add [Solve] button
			Button solveButton = new Button("Solve");
			solveButton.addClickListener(new ClickListener() {

				public void onClick(Widget sender) {
					impediment.setSolved(true);
					rebuild();
				}
			});
			toolbar.add(solveButton);
		} else {
			// impediment not solved -> add [Unsolve] button
			Button unsolveButton = new Button("Unsolve");
			unsolveButton.addClickListener(new ClickListener() {

				public void onClick(Widget sender) {
					impediment.setSolved(false);
					rebuild();
				}
			});
			toolbar.add(unsolveButton);
		}

		return toolbar;
	}

	// --- dnd-related ---
	MouseListenerCollection mouseListeners;

	public void addMouseListener(MouseListener listener) {
		if (mouseListeners == null) {
			mouseListeners = new MouseListenerCollection();
			sinkEvents(Event.MOUSEEVENTS);
		}
		mouseListeners.add(listener);
	}

	public void removeMouseListener(MouseListener listener) {
		if (mouseListeners != null) {
			mouseListeners.remove(listener);
		}
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
			case Event.ONMOUSEDOWN:
			case Event.ONMOUSEUP:
			case Event.ONMOUSEMOVE:
			case Event.ONMOUSEOVER:
			case Event.ONMOUSEOUT:
				mouseListeners.fireMouseEvent(this, event);
				break;
		}
	}
}