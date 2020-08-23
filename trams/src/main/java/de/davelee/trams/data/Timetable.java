package de.davelee.trams.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import java.time.LocalDate;

/**
 * This class represents timetable outlines for the Easy Timetable Generator in TraMS.
 * @author Dave Lee
 */

@Entity
@Table(name="TIMETABLE", uniqueConstraints=@UniqueConstraint(columnNames = {"routeNumber", "name"}))
@Getter
@Setter
public class Timetable {

	@Id
	@GeneratedValue
	@Column
	private long id;
	
	@Column
    private String name;
	
	@Column
    private LocalDate validFromDate;
	
	@Column
    private LocalDate validToDate;

	@Column
	private String routeNumber;

}
