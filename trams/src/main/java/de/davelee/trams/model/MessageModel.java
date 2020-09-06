package de.davelee.trams.model;

import de.davelee.trams.util.MessageFolder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageModel {
	
	private String subject;
	private String text;
	private String sender;
	private String date;
	private MessageFolder messageFolder;

}
