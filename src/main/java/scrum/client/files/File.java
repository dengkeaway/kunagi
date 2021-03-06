package scrum.client.files;

import java.util.Comparator;
import java.util.Map;

import scrum.client.ScrumGwt;
import scrum.client.common.LabelSupport;
import scrum.client.common.ReferenceSupport;

public class File extends GFile implements ReferenceSupport, LabelSupport {

	public static final String REFERENCE_PREFIX = "fle";

	public File(Map data) {
		super(data);
	}

	public boolean isImage() {
		String name = getFilename().toLowerCase();
		return name.endsWith(".png") || name.endsWith(".gif") || name.endsWith(".jpg");
	}

	@Override
	public String getReference() {
		return REFERENCE_PREFIX + getNumber();
	}

	@Override
	public String toHtml() {
		return ScrumGwt.toHtml(this, getLabel());
	}

	@Override
	public String toString() {
		return getFilename();
	}

	public static final Comparator<File> UPLOAD_TIME_COMPARATOR = new Comparator<File>() {

		@Override
		public int compare(File a, File b) {
			return a.getUploadTime().compareTo(b.getUploadTime());
		}
	};

	public static final Comparator<File> REVERSE_UPLOAD_TIME_COMPARATOR = new Comparator<File>() {

		@Override
		public int compare(File a, File b) {
			return UPLOAD_TIME_COMPARATOR.compare(b, a);
		}
	};

}