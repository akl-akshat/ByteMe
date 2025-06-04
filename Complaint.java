public class Complaint {
    private String body;
    private boolean resolved;

    public Complaint(String body) {
        this.body = body;
        this.resolved = false;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void markResolved() {
        this.resolved = true;
    }

    @Override
    public String toString() {
        String status;
        if (resolved) {
            status = "Resolved";
        } else {
            status = "Pending";
        }
        return "Complaint: " + body + " , Status: " + status;
    }
}