package scrum.client.common.editable;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class for a editable integer widget.
 * 
 * In view mode this widget displays a label for the value. In edit mode a <code>TextBox</code> is used as
 * editor to allow editing the value.
 */
public abstract class AEditableIntegerWidget extends AEditableWidget {

	private Label viewer;
	private TextBox editor;

	/**
	 * Provide the value for view mode and edit mode.
	 */
	protected abstract Integer getValue();

	/**
	 * Set the value inputed by the user.
	 */
	protected abstract void setValue(Integer value);

	public AEditableIntegerWidget() {
		viewer = new Label();
		viewer.addClickListener(new ViewerClickListener());
		rebuild();
	}

	@Override
	protected Widget getEditor() {
		if (editor == null) {
			Integer value = getValue();
			String text = value == null ? null : getValue().toString();
			editor = new TextBox();
			editor.setMaxLength(3);
			editor.setText(text);
			editor.addKeyboardListener(new EditorKeyboardListener());
		}
		return editor;
	}

	@Override
	protected void updateEditor() {
		editor.setSelectionRange(0, editor.getText().length());
	}

	@Override
	protected Widget getViewer() {
		Integer value = getValue();
		viewer.setText(value == null ? null : value.toString());
		return viewer;
	}

	private class ViewerClickListener implements ClickListener {

		public void onClick(Widget sender) {
			setEditMode(true);
		}

	}

	private class EditorKeyboardListener extends KeyboardListenerAdapter {

		@Override
		public void onKeyPress(Widget sender, char keyCode, int modifiers) {
			if (keyCode == KeyboardListener.KEY_ENTER) {
				String text = editor.getText();
				if (text == null) {
					setValue(null);
				} else {
					text = text.trim();
					if (text.length() == 0) {
						setValue(null);
					} else {
						try {
							setValue(Integer.parseInt(text));
						} catch (NumberFormatException ex) {
							System.err.println("not an integer: " + text);
							setValue(null);
						}
					}
				}
				setEditMode(false);
			}
			if (keyCode == KeyboardListener.KEY_ESCAPE) {
				setEditMode(false);
			}
		}

	}
}