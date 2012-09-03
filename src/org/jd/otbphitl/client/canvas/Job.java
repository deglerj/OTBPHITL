package org.jd.otbphitl.client.canvas;

public abstract class Job {

	private Job	nextJob;

	public abstract void execute();

	protected void executeNext() {
		if (nextJob != null) {
			nextJob.execute();
		}
	}

	public void setNextJob(final Job nextJob) {
		this.nextJob = nextJob;
	}

}