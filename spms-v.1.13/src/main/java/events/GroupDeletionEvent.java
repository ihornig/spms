package events;

public class GroupDeletionEvent {
	
	public GroupDeletionEvent() {}
	
	public GroupDeletionEvent(String name) {
		groupName = name;
	}
	
	private String groupName;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
