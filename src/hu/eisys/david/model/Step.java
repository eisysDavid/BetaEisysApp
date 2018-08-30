package hu.eisys.david.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = Efile.class, name = "eFile"), @Type(value = Adhock.class, name = "adhock"),
		@Type(value = DbChange.class, name = "dBChange") })
public interface Step {

	void setComment(String comment);

	String getComment();

	String getShowElement();

	StepNames getType();
}
