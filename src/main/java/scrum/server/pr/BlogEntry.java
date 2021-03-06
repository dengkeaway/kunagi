package scrum.server.pr;

import ilarkesto.base.Utl;
import ilarkesto.base.time.DateAndTime;
import scrum.client.common.LabelSupport;
import scrum.client.common.ReferenceSupport;
import scrum.server.admin.User;
import scrum.server.common.Numbered;

public class BlogEntry extends GBlogEntry implements Numbered, ReferenceSupport, LabelSupport, Comparable<BlogEntry> {

	public String getUrl() {
		String url = getProject().getHomepageUrl();
		if (url == null) return null;
		if (!url.endsWith("/")) url += "/";
		url += getReference() + ".html";
		return url;
	}

	@Override
	public String getLabel() {
		return getTitle();
	}

	@Override
	public boolean isVisibleFor(User user) {
		return getProject().isVisibleFor(user);
	}

	public String getReferenceAndLabel() {
		return getReference() + " " + getTitle();
	}

	@Override
	public String getReference() {
		return scrum.client.pr.BlogEntry.REFERENCE_PREFIX + getNumber();
	}

	@Override
	public void updateNumber() {
		if (getNumber() == 0) setNumber(getProject().generateBlogEntryNumber());
	}

	@Override
	public void ensureIntegrity() {
		super.ensureIntegrity();
		updateNumber();
		if (!isDateAndTimeSet()) setDateAndTime(DateAndTime.now());
	}

	@Override
	public int compareTo(BlogEntry other) {
		return Utl.compare(getDateAndTime(), other.getDateAndTime()) * -1;
	}

	@Override
	public String toString() {
		return getReferenceAndLabel();
	}
}