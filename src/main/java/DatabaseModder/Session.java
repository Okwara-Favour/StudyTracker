package DatabaseModder;

import java.time.LocalDate;
import java.time.LocalTime;

public class Session
{
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;
	private String topic;
	private String comment;
	
	public Session(LocalDate date, LocalTime startTime, LocalTime endTime, String topic, String comment)
	{
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.topic = topic;
		this.comment = comment;
	}
	
	public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getTopic() {
        return topic;
    }

    public String getComment() {
        return comment;
    }
}